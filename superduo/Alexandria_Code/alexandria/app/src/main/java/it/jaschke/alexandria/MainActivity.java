package it.jaschke.alexandria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.FragmentManager.BackStackEntry;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import it.jaschke.alexandria.api.Callback;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.util.Utility;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, Callback {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence title;
    public static boolean IS_TABLET = false;
    private BroadcastReceiver messageReciever;

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

    public static final String ADD_BOOK_TAG = "ADD_BOOK_TAG";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();


    public static final String BOOK_LIST_TAG = "BOOK_LIST_TAG";
    public static final String ADD_BOOKS_TAG = "ADD_BOOKS_TAG";
    public static final String ABOUT_TAG = "ABOUT_TAG";


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IS_TABLET = isTablet();
        if (IS_TABLET) {
            setContentView(R.layout.activity_main_tablet);
        } else {
            setContentView(R.layout.activity_main);
        }

        messageReciever = new MessageReciever();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReciever, filter);


        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment nextFragment;
        String fragmentTag = null;
        switch (position) {
            default:
            case 0:
                nextFragment = new ListOfBooks();
                fragmentTag = BOOK_LIST_TAG;
                break;
            case 1:
                nextFragment = new AddBook();
                fragmentTag = ADD_BOOK_TAG;
                break;
            case 2:
                nextFragment = new About();
                fragmentTag = ABOUT_TAG;
                break;

        }

//added fragment tags for identification
        fragmentManager.beginTransaction()
                .replace(R.id.container, nextFragment, fragmentTag)
                .addToBackStack((String) title)
                .commit();
    }

    public void setTitle(int titleId) {
        title = getString(titleId);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReciever);
        super.onDestroy();
    }

    @Override
    public void onItemSelected(String ean) {
        Bundle args = new Bundle();
        args.putString(BookDetail.EAN_KEY, ean);

        BookDetail fragment = new BookDetail();
        fragment.setArguments(args);

        int id = R.id.container;
        if (findViewById(R.id.right_container) != null) {
            id = R.id.right_container;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(id, fragment)
                .addToBackStack(getResources().getString(R.string.book_detail))         //used string resource
                .commit();

    }

    /**
     * This class receives Broadcast messages from service
     */
    private class MessageReciever extends BroadcastReceiver {
        /**
         * This method receives status message and show Toasts for the same
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(MESSAGE_KEY)) {
                @BookService.BookSearchStatus int status = intent.getIntExtra(MESSAGE_KEY, BookService.STATUS_UNKNOWN);
                String message = "";
                switch (status) {
                    case BookService.SERVER_STATUS_OK:
                        message = getString(R.string.add_success);
                        break;
                    case BookService.SERVER_DOWN:
                        message = getString(R.string.empty_list_server_down);
                        break;
                    case BookService.INVALID_REQUEST:
                        message = getString(R.string.invalid_request_error);
                        break;

                    case BookService.SERVER_STATUS_INVALID:
                        message = getString(R.string.empty_list_server_error);
                        break;
                    case BookService.NETWORK_ERROR:
                        message = getString(R.string.network_unavailable);
                        break;

                    default:
                        message = getString(R.string.network_unavailable);
                        break;
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    //Commented code as the visibility of the Image back button is changed since it
    // does not conform to the standard Android Back button Navigation
    /*public void goBack(View view) {
        getSupportFragmentManager().popBackStack();
    }*/

    private boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * This method handled back button press from the app
     */
    @Override
    public void onBackPressed() {


        int position = 0;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int currentSelectedPosition = Integer.parseInt(prefs.getString(getString(R.string.pref_start_fragment),getString(R.string.start_fragment_default_value)));

        if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
            finish();
        }

        //Set title based upon the fragment
        BackStackEntry backStackFragment = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
        this.title = backStackFragment.getName();
        restoreActionBar();

        //Check current fragment is the default set start screen, if so then call destroy()

        ListOfBooks bookListFragment = (ListOfBooks) getSupportFragmentManager().findFragmentByTag(BOOK_LIST_TAG);
        AddBook addBookFragement = (AddBook) getSupportFragmentManager().findFragmentByTag(ADD_BOOK_TAG);

        if (bookListFragment != null && bookListFragment.isVisible()) {
            position = Utility.fragmentPosition(this, BOOK_LIST_TAG);
            if (position == currentSelectedPosition) {
                finish();
                return;
            }
        }
        if (addBookFragement != null && addBookFragement.isVisible()) {

            position = Utility.fragmentPosition(this, ADD_BOOK_TAG);
            if (position == currentSelectedPosition) {
                finish();
                return;
            }
        }


        super.onBackPressed();
    }


}


