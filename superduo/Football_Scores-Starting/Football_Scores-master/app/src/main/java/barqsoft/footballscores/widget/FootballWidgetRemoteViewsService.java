package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.scoresAdapter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FootballWidgetRemoteViewsService extends RemoteViewsService {

   /* public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;*/
 //   public double detail_match_id = 0;
    public static final int  detail_match_id = 0;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                Uri uri = DatabaseContract.BASE_CONTENT_URI;

                data = getContentResolver().query(uri, null, null, null, DatabaseContract.scores_table.DATE_COL + " ASC");
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.football_widget_list_item);
                views.setTextViewText(R.id.widget_home_name,data.getString(scoresAdapter.COL_HOME));
                views.setTextViewText(R.id.widget_away_name,data.getString(scoresAdapter.COL_AWAY));
                views.setTextViewText(R.id.widget_data_textview, Utilies.convertTime(data.getString(scoresAdapter.COL_MATCHTIME)));
                views.setTextViewText(R.id.widget_score_textview, Utilies.getScores(data.getInt(scoresAdapter.COL_HOME_GOALS), data.getInt(scoresAdapter.COL_AWAY_GOALS)));
              //  mHolder.match_id = cursor.getDouble(COL_ID);
                views.setImageViewResource(R.id.widget_home_crest, Utilies.getTeamCrestByTeamName(
                        data.getString(scoresAdapter.COL_HOME)));
                views.setImageViewResource(R.id.widget_away_crest,Utilies.getTeamCrestByTeamName(
                        data.getString(scoresAdapter.COL_AWAY)));
                String matchDate = data.getString(scoresAdapter.COL_DATE);
                final Intent fillInIntent = new Intent();

             //   Uri dateUri = DatabaseContract.scores_table.buildScoreWithId();
      /*          Uri uri = DatabaseContract.scores_table.buildFootballScoreWithId(data.getInt(COL_ID));
                fillInIntent.setData(uri);*/
                fillInIntent.putExtra(getApplicationContext().getString(R.string.match_id),data.getString(scoresAdapter.COL_ID));
                fillInIntent.putExtra(getApplicationContext().getString(R.string.index),Utilies.getFragmentIndex(getApplicationContext(),matchDate));
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(),R.layout.football_widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(detail_match_id);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
