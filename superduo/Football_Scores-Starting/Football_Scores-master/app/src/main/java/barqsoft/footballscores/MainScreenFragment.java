package barqsoft.footballscores;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

//import barqsoft.footballscores.service.myFetchService;
import barqsoft.footballscores.service.myFetchService;
import barqsoft.footballscores.sync.FootballScoresSyncAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {
    public scoresAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private String[] fragmentdate = new String[1];
    private int last_selected_item = -1;
    private String widgetMatchId = null;
    private int mWidgetPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = ListView.INVALID_POSITION;

    //Used butterknife for binding
    @Bind(R.id.scores_list)
    ListView score_list;

    @Bind(R.id.listview_empty)
    TextView emptyTextView;

    public MainScreenFragment() {
    }

/* Method not used
   private void update_scores()
    {
        Intent service_start = new Intent(getActivity(), myFetchService.class);
        getActivity().startService(service_start);
    }
*/

    /**
     *
     * @param date
     */
    public void setFragmentDate(String date) {
        fragmentdate[0] = date;
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // update_scores();
            //Removed call to service and used sync adapter instead
        FootballScoresSyncAdapter.initializeSyncAdapter(getActivity());
        //Received from bundle item index
        Bundle bundle = getArguments();
        if (bundle != null && bundle.get(getString(R.string.match_id)) != null) {
            widgetMatchId = bundle.getString(getString(R.string.match_id));
        }
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);                   //Butterknife used for binding
        // final ListView score_list = (ListView) rootView.findViewById(R.id.scores_list);

        mAdapter = new scoresAdapter(getActivity(), null, 0);
        score_list.setAdapter(mAdapter);
        getLoaderManager().initLoader(SCORES_LOADER, null, this);

        mAdapter.detail_match_id = MainActivity.selected_match_id;
        score_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selected = (ViewHolder) view.getTag();
                mAdapter.detail_match_id = selected.match_id;
                MainActivity.selected_match_id = (int) selected.match_id;
                mAdapter.notifyDataSetChanged();
            }
        });

        //saved current item position in savedinstance state
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

    /**
     *
     * @param i
     * @param bundle
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, fragmentdate, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        int i = 0;
        String matchId;
        int matchDay = 0;
        int leagueId = 0;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            matchId = cursor.getString(scoresAdapter.COL_ID);
            if (matchId.equals(widgetMatchId)) {            //Check if widget match id and recordset match id same
                mWidgetPosition = i;
                matchDay = cursor.getInt(scoresAdapter.COL_MATCHDAY);
                leagueId = cursor.getInt(scoresAdapter.COL_LEAGUE);

            }
            i++;
            cursor.moveToNext();
        }
        if (mWidgetPosition != 0) {
            score_list.setSelection(mWidgetPosition);               //set match id position as selected item from widget selection


        }
        if (mPosition != ListView.INVALID_POSITION) {               //set list position
            score_list.smoothScrollToPosition(mPosition);
        }

        mAdapter.swapCursor(cursor);
        updateEmptyView();
    }

    /**
     *
     * @param cursorLoader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }


    /**
     * Error handling for empty resultset
     */
    private void updateEmptyView() {
        if (mAdapter.getCount() == 0) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            @FootballScoresSyncAdapter.NetworkStatus int state = Utilies.getNetworkState(getActivity());

            if (null != emptyTextView) {
                int message = R.string.list_unavailable;
                switch (state) {
                    case FootballScoresSyncAdapter.SERVER_DOWN:
                        message = R.string.empty_list_server_down;
                        break;
                    case FootballScoresSyncAdapter.SERVER_STATUS_INVALID:
                        message = R.string.empty_list_server_error;
                        break;
                    case FootballScoresSyncAdapter.STATUS_OK:
                        break;

                    default:
                        if (!Utilies.checkNetworkState(getActivity())) {
                            message = R.string.network_unavailable;
                        }
                        break;
                }
                emptyTextView.setText(message);
            }
        }
    }

    /**
     * Update empty view from shared preference key
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(getString(R.string.pref_status_key)))
            updateEmptyView();
    }


    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.unregisterOnSharedPreferenceChangeListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);

    }

    /**
     * Save in saved instance state bundle
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


}