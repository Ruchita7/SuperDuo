package barqsoft.footballscores;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.stetho.Stetho;

import barqsoft.footballscores.sync.FootballScoresSyncAdapter;

//Extended AppCompatActivity for toolbar
public class MainActivity extends AppCompatActivity {
    public static int selected_match_id;
    public static int current_fragment = 2;
    //   public static String LOG_TAG = "MainActivity";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    // private final String save_tag = "Save Test";          Unused string
    private PagerFragment my_main;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
        //  Log.d(LOG_TAG, "Reached MainActivity onCreate");					//commented unnecessary log statments

        //Used toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        if (savedInstanceState == null) {
            my_main = new PagerFragment();

            //Traverse to selected match in the tab from widget by saving in bundle arguments
            String indexRes = getString(R.string.index);
            String matchRes = getString(R.string.match_id);
            if (getIntent() != null && getIntent().hasExtra(indexRes) && getIntent().hasExtra(matchRes)) {

                int index = getIntent().getIntExtra(indexRes, 2);
                bundle = new Bundle();
                bundle.putInt(getResources().getString(R.string.selected_tab), index);
                bundle.putString(matchRes, getIntent().getStringExtra(matchRes));
                my_main.setArguments(bundle);
            }


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, my_main)
                    .commit();
        }

    }


    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Removed unnecessary log statements
        //used string resources as key
        outState.putInt(getString(R.string.pager_current), my_main.mPagerHandler.getCurrentItem());
        outState.putInt(getString(R.string.selected_match), selected_match_id);
        getSupportFragmentManager().putFragment(outState, getString(R.string.my_main), my_main);
        super.onSaveInstanceState(outState);
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //Removed unnecessary log statements
        //used string resources as key
        current_fragment = savedInstanceState.getInt(getString(R.string.pager_current));
        selected_match_id = savedInstanceState.getInt(getString(R.string.selected_match));
        my_main = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, getString(R.string.my_main));
        super.onRestoreInstanceState(savedInstanceState);
    }
}
