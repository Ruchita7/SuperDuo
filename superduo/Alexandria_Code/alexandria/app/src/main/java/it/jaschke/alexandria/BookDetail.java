package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.services.DownloadImage;
import it.jaschke.alexandria.util.ConstantUtil;


public class BookDetail extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = BookDetail.class.getSimpleName();
    public static final String EAN_KEY = "EAN";
    private final int LOADER_ID = 10;
    private View rootView;
    private String ean;
    private String bookTitle;
    private ShareActionProvider shareActionProvider;

    //Used Butterknife
    @Bind(R.id.delete_button)
    Button delete_button;

    @Bind(R.id.fullBookTitle)
    TextView bookTitleTextView;

    @Bind(R.id.fullBookCover)
    ImageView bookCoverImageView;

    @Bind(R.id.fullBookSubTitle)
    TextView bookSubTitleTextView;

    @Bind(R.id.fullBookDesc)
    TextView bookDescTextView;

    @Bind(R.id.categories)
    TextView categoriesTextView;

    @Bind(R.id.authors)
    TextView authorsTextView;

    ParcelableBookDetails bookDetails;

    static final String BOOK_DETAIL_KEY = "BOOK_DETAIL_KEY";


    public BookDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            ean = arguments.getString(BookDetail.EAN_KEY);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        rootView = inflater.inflate(R.layout.fragment_full_book, container, false);
        ButterKnife.bind(this, rootView);
        //Moved delete onclick to deleteAction()

        //Restored in bundle book details
        if (savedInstanceState != null && !savedInstanceState.containsKey(BOOK_DETAIL_KEY)) {
            bookDetails = savedInstanceState.getParcelable(BOOK_DETAIL_KEY);
        }

        return rootView;
    }

    /**
     * Delete book method
     */
    @OnClick(R.id.delete_button)
    public void deleteAction() {
        Intent bookIntent = new Intent(getActivity(), BookService.class);
        bookIntent.putExtra(BookService.EAN, ean);
        bookIntent.setAction(BookService.DELETE_BOOK);
        getActivity().startService(bookIntent);
        getActivity().getSupportFragmentManager().popBackStack();
        Toast.makeText(getActivity(), String.format(getString(R.string.deletion_success), bookTitle), Toast.LENGTH_SHORT).show();
    }


    /**
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
    }


    /**
     * @param id
     * @param args
     * @return
     */
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(ean)),
                null,
                null,
                null,
                null
        );
    }


    /**
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {


        if (!data.moveToFirst()) {
            return;
        }

        //Null checks and butterknife binding
        bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        if (bookTitle != null) {
            bookTitleTextView.setText(bookTitle);
        }


        String bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        if (bookSubTitle != null) {
            bookSubTitleTextView.setText(bookSubTitle);
        }

        String desc = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.DESC));
        if (desc != null) {
            bookDescTextView.setText(desc);
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
                Glide.with(this).load(imgUrl).override(ConstantUtil.LARGE_BOOK_COVER_SIZE, ConstantUtil.LARGE_BOOK_COVER_SIZE).placeholder(R.drawable.default_book).error(R.drawable.default_book).into(bookCoverImageView);
                bookCoverImageView.setVisibility(View.VISIBLE);
            }
        }
        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        if (categories != null) {

            categoriesTextView.setText(categories);
        }

        //Commented code for the visibility of the Image back button is changed since it
        // does not conform to the standard Android Back button Navigation
      /*  if(rootView.findViewById(R.id.right_container)!=null){
            rootView.findViewById(R.id.backButton).setVisibility(View.INVISIBLE);
        }*/
        //Added book detail to Parcelable object
        bookDetails = new ParcelableBookDetails(ean, bookTitle, bookSubTitle, desc, authors, imgUrl, categories);

        setShareIntent();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    private void setShareIntent() {
        String message;
        if (shareActionProvider != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            shareIntent.setType("text/plain");
            message = getString(R.string.share_text) + bookDetails.bookTitle;
            if (bookDetails.authors != null) {
                message += "\n" + String.format(getString(R.string.author), bookDetails.authors);
            }
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_text) + bookDetails.bookTitle);
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);

            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onPause() {
        super.onDestroyView();
        if (MainActivity.IS_TABLET && rootView.findViewById(R.id.right_container) == null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Added book details to bundle
        outState.putParcelable(BOOK_DETAIL_KEY, bookDetails);
        super.onSaveInstanceState(outState);
    }


}