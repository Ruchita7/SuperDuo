package it.jaschke.alexandria.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;

import it.jaschke.alexandria.AddBook;
import it.jaschke.alexandria.MainActivity;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.util.Utility;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class BookService extends IntentService {

    private final String LOG_TAG = BookService.class.getSimpleName();

    public static final String FETCH_BOOK = "it.jaschke.alexandria.services.action.FETCH_BOOK";
    public static final String DELETE_BOOK = "it.jaschke.alexandria.services.action.DELETE_BOOK";

    public static final String EAN = "it.jaschke.alexandria.services.extra.EAN";

    //Support annotation and Constants added to check ISBN scan/input status
    public static final int SERVER_STATUS_OK = 0;
    public static final int SERVER_DOWN = 1;
    public static final int INVALID_REQUEST = 2;
    public static final int SERVER_STATUS_INVALID = 3;
    public static final int NETWORK_ERROR = 4;
    public static final int ALREADY_ADDED = 5;
    public static final int STATUS_UNKNOWN = 6;

    @IntDef({SERVER_STATUS_OK, SERVER_DOWN, INVALID_REQUEST, SERVER_STATUS_INVALID, NETWORK_ERROR, ALREADY_ADDED, STATUS_UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BookSearchStatus {

    }

    public BookService() {
        super("Alexandria");
    }

    private String mEAN;

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (FETCH_BOOK.equals(action)) {
                // Used class variable instead of local final variable
                mEAN = intent.getStringExtra(EAN);
                fetchBook();
            } else if (DELETE_BOOK.equals(action)) {
                // Used class variable instead of local final variable
                mEAN = intent.getStringExtra(EAN);
                deleteBook();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    //Removed formal parameter and used class variable
    private void deleteBook() {
        if (mEAN != null) {
            getContentResolver().delete(AlexandriaContract.BookEntry.buildBookUri(Long.parseLong(mEAN)), null, null);
        }
    }

    /**
     * Handle action fetchBook in the provided background thread with the provided
     * parameters.
     */
    //Removed formal parameter and used class variable
    private void fetchBook() {

        if (mEAN.length() != 13) {
            bookSearchStatus(INVALID_REQUEST);      //set book status as invalid
            return;
        }

        //Check network status
        if (!Utility.checkNetworkState(getApplicationContext())) {
            bookSearchStatus(NETWORK_ERROR);
            return;
        }
        Cursor bookEntry = getContentResolver().query(
                AlexandriaContract.BookEntry.buildBookUri(Long.parseLong(mEAN)),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        if (bookEntry.getCount() > 0) {
            bookEntry.close();
            return;
        }

        bookEntry.close();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJsonString = null;

        try {

            //Moved string resources to strings.xml
            final String FORECAST_BASE_URL = getString(R.string.forecast_base_url);
            final String QUERY_PARAM = getString(R.string.query_param);
            final String ISBN_PARAM = String.format(getString(R.string.isbn_param), mEAN);

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, ISBN_PARAM)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }

            if (buffer.length() == 0) {
                bookSearchStatus(SERVER_DOWN);              //No book information available/server is down
                return;
            }
            bookJsonString = buffer.toString();
        } catch (Exception e) {
            bookSearchStatus(SERVER_DOWN);           //No book information available/server is down
            Log.e(LOG_TAG, "Error ", e);
            return;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }

        }

        //Moved string resources to strings.xml
        final String ITEMS = getString(R.string.items);
        final String VOLUME_INFO = getString(R.string.volume_Info);
        final String TITLE = getString(R.string.title);
        final String SUBTITLE = getString(R.string.subtitle);
        final String AUTHORS = getString(R.string.authors);
        final String DESC = getString(R.string.desc);
        final String CATEGORIES = getString(R.string.categories);
        final String IMG_URL_PATH = getString(R.string.img_url_path);
        final String IMG_URL = getString(R.string.img_url);

        try {
            JSONObject bookJson = new JSONObject(bookJsonString);
            JSONArray bookArray;
            if (bookJson.has(ITEMS)) {
                bookArray = bookJson.getJSONArray(ITEMS);
            } else {
                //Moved broadcast call to generic method bookSearchStatus
                bookSearchStatus(SERVER_STATUS_INVALID);
                return;
            }

            JSONObject bookInfo = ((JSONObject) bookArray.get(0)).getJSONObject(VOLUME_INFO);

            String title = bookInfo.getString(TITLE);

            String subtitle = "";
            if (bookInfo.has(SUBTITLE)) {
                subtitle = bookInfo.getString(SUBTITLE);
            }

            String desc = "";
            if (bookInfo.has(DESC)) {
                desc = bookInfo.getString(DESC);
            }

            String imgUrl = "";
            if (bookInfo.has(IMG_URL_PATH) && bookInfo.getJSONObject(IMG_URL_PATH).has(IMG_URL)) {
                imgUrl = bookInfo.getJSONObject(IMG_URL_PATH).getString(IMG_URL);
            }

            writeBackBook(mEAN, title, subtitle, desc, imgUrl);

            if (bookInfo.has(AUTHORS)) {
                writeBackAuthors(mEAN, bookInfo.getJSONArray(AUTHORS));
            }
            if (bookInfo.has(CATEGORIES)) {
                writeBackCategories(mEAN, bookInfo.getJSONArray(CATEGORIES));
            }

            bookSearchStatus(SERVER_STATUS_OK);         //able to search book
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error ", e);
            bookSearchStatus(SERVER_STATUS_INVALID);    //invalid server state
        }
    }

    /**
     *
     * @param ean
     * @param title
     * @param subtitle
     * @param desc
     * @param imgUrl
     */
    private void writeBackBook(String ean, String title, String subtitle, String desc, String imgUrl) {
        ContentValues values = new ContentValues();
        values.put(AlexandriaContract.BookEntry._ID, ean);
        values.put(AlexandriaContract.BookEntry.TITLE, title);
        values.put(AlexandriaContract.BookEntry.IMAGE_URL, imgUrl);
        values.put(AlexandriaContract.BookEntry.SUBTITLE, subtitle);
        values.put(AlexandriaContract.BookEntry.DESC, desc);
        getContentResolver().insert(AlexandriaContract.BookEntry.CONTENT_URI, values);
    }

    /**
     *
     * @param ean
     * @param jsonArray
     * @throws JSONException
     */
    private void writeBackAuthors(String ean, JSONArray jsonArray) throws JSONException {
        ContentValues values = new ContentValues();
        for (int i = 0; i < jsonArray.length(); i++) {
            values.put(AlexandriaContract.AuthorEntry._ID, ean);
            values.put(AlexandriaContract.AuthorEntry.AUTHOR, jsonArray.getString(i));
            getContentResolver().insert(AlexandriaContract.AuthorEntry.CONTENT_URI, values);
            values = new ContentValues();
        }
    }

    /**
     *
     * @param ean
     * @param jsonArray
     * @throws JSONException
     */
    private void writeBackCategories(String ean, JSONArray jsonArray) throws JSONException {
        ContentValues values = new ContentValues();
        for (int i = 0; i < jsonArray.length(); i++) {
            values.put(AlexandriaContract.CategoryEntry._ID, ean);
            values.put(AlexandriaContract.CategoryEntry.CATEGORY, jsonArray.getString(i));
            getContentResolver().insert(AlexandriaContract.CategoryEntry.CONTENT_URI, values);
            values = new ContentValues();
        }
    }

    /**
     * Generic method called to check ISBN scan/input status and broadcast the message to MainActivity
     * @param errorCode
     */
    private void bookSearchStatus(@BookSearchStatus int errorCode) {
        Intent messageIntent = new Intent(MainActivity.MESSAGE_EVENT);
        messageIntent.putExtra(MainActivity.MESSAGE_KEY, errorCode);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(messageIntent);
    }


}