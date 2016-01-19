package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.jaschke.alexandria.api.FragmentIntentIntegrator;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.services.DownloadImage;


public class AddBook extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    //private static final String TAG = "INTENT_TO_SCAN_ACTIVITY";       Unused String

    private final int LOADER_ID = 1;
    private View rootView;
    private final String EAN_CONTENT = "eanContent";
    // private static final String SCAN_FORMAT = "scanFormat";           Unused String
    //  private static final String SCAN_CONTENTS = "scanContents";        Unused String
    String mEAN = null;
    //  private String mScanFormat = "Format:";                            Unused String
    //  private String mScanContents = "Contents:";                        Unused String

    //Butterknife to bind components
    @Bind(R.id.ean)
    EditText ean;

    @Bind(R.id.scan_button)
    Button scan_button;

    @Bind(R.id.bookTitle)
    TextView bookTitleTextView;

    @Bind(R.id.bookSubTitle)
    TextView bookSubTitleTextView;

    @Bind(R.id.authors)
    TextView authorsTextView;

    @Bind(R.id.bookCover)
    ImageView bookCoverImageView;

    @Bind(R.id.categories)
    TextView categoriesTextView;

    @Bind(R.id.delete_button)
    ImageButton delete_button;

    @Bind(R.id.save_button)
    ImageButton save_button;

    public static final String LOG_TAG = AddBook.class.getSimpleName();
    Fragment mContext;
//private Intent mIntent;

    public AddBook() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (ean != null) {
            outState.putString(EAN_CONTENT, ean.getText().toString());
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_book, container, false);

        //Butterknife used
        ButterKnife.bind(this, rootView);

        ean.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //no need
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //no need
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ean = s.toString();
                //catch isbn10 numbers
                if (ean.length() == 10 && !ean.startsWith("978")) {
                    ean = "978" + ean;
                }
                if (ean.length() < 13) {
                    clearFields();
                    return;
                }
                //Once we have an ISBN, start a book intent
                Intent bookIntent = new Intent(getActivity(), BookService.class);
                bookIntent.putExtra(BookService.EAN, ean);
                bookIntent.setAction(BookService.FETCH_BOOK);
                getActivity().startService(bookIntent);
                AddBook.this.restartLoader();
            }
        });

        //Moved scan, save, delete button onclick event handler to seperate method

        if (savedInstanceState != null) {
            ean.setText(savedInstanceState.getString(EAN_CONTENT));
            ean.setHint("");
        }

        return rootView;
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    //Method called to click of save button
    @OnClick(R.id.save_button)
    public void saveAction() {

        Toast.makeText(getActivity(), String.format(getString(R.string.modify_success), bookTitleTextView.getText().toString()), Toast.LENGTH_SHORT).show();
        ean.setText("");
    }

    //Method called to click of delete button
    @OnClick(R.id.delete_button)
    public void deleteAction() {
        String bookTitle = "Book";
        if (bookTitleTextView.getText() != null) {
            bookTitle = (String) bookTitleTextView.getText().toString();
        }
        Intent bookIntent = new Intent(getActivity(), BookService.class);
        bookIntent.putExtra(BookService.EAN, ean.getText().toString());
        bookIntent.setAction(BookService.DELETE_BOOK);
        getActivity().startService(bookIntent);
        Toast.makeText(getActivity(), String.format(getString(R.string.deletion_success), bookTitle), Toast.LENGTH_SHORT).show();
        ean.setText("");
    }

    //Method called to click of scan button
    @OnClick(R.id.scan_button)
    public void scanAction() {
        ean.setText("");
        FragmentIntentIntegrator intentIntegrator = new FragmentIntentIntegrator(this);
        intentIntegrator.initiateScan();

    }

    /**
     *
     * @param id
     * @param args
     * @return
     */
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String eanStr = null;
        //Code added to display scanned book information.
        //Condition added to check if either ISBN is entered manually or book is scanned via scanner
        //and fetch book detail based on the corresponding EAN
        if (ean.getText().length() == 0 && mEAN == null) {
            return null;
        } else {
            if (ean.getText().length() != 0) {
                eanStr = ean.getText().toString();
            } else if (mEAN != null) {
                eanStr = mEAN;
            }
        }

        if (eanStr != null && eanStr.length() == 10 && !eanStr.startsWith("978")) {
            eanStr = "978" + eanStr;
        }
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(eanStr)),
                null,
                null,
                null,
                null
        );
    }

    /**
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        //Null checks added and butterknife bindings used
        String bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        if (bookTitle != null) {
            bookTitleTextView.setText(bookTitle);
        }
        String bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        if (bookSubTitle != null) {
            bookSubTitleTextView.setText(bookSubTitle);
        }
        String authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        if (authors != null) {
            String[] authorsArr = authors.split(",");
            authorsTextView.setLines(authorsArr.length);
            authorsTextView.setText(authors.replace(",", "\n"));
        }
        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        if (imgUrl != null) {
            if (Patterns.WEB_URL.matcher(imgUrl).matches()) {
                Glide.with(this).load(imgUrl).override(144, 144).placeholder(R.drawable.default_book).error(R.drawable.default_book).into(bookCoverImageView);
                bookCoverImageView.setVisibility(View.VISIBLE);
            }
        }
        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        if (categories != null) {
            categoriesTextView.setText(categories);
        }

        save_button.setVisibility(View.VISIBLE);
        delete_button.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    /**
     *  Used butterknife bound components
     */
    private void clearFields() {
        bookTitleTextView.setText("");
        bookSubTitleTextView.setText("");
        authorsTextView.setText("");
        categoriesTextView.setText("");
        bookCoverImageView.setVisibility(View.INVISIBLE);
        save_button.setVisibility(View.INVISIBLE);
        delete_button.setVisibility(View.INVISIBLE);
    }

    /**
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.scan);
    }

    /**
     * Method called to process results of scan activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context context = getActivity();
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null && scanResult.getContents() != null) {

            mEAN = scanResult.getContents();
            if (mEAN.length() == 10 && !mEAN.startsWith("978")) {
                mEAN = "978" + ean;
            }
            ean.setText(mEAN);

            Intent bookIntent = new Intent(getActivity(), BookService.class);
            bookIntent.putExtra(BookService.EAN, mEAN);
            bookIntent.setAction(BookService.FETCH_BOOK);
            context.startService(bookIntent);
            AddBook.this.restartLoader();
            // Handle successful scan
        }
    }


}
