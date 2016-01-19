package barqsoft.footballscores;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder
{
    @Bind(R.id.home_name)
    public TextView home_name;
    @Bind(R.id.away_name)
    public TextView away_name;
    @Bind(R.id.score_textview)
    public TextView score;
    @Bind(R.id.data_textview)
    public TextView date;
    @Bind(R.id.home_crest)
    public ImageView home_crest;
    @Bind(R.id.away_crest)
    public ImageView away_crest;

    public double match_id;
    public ViewHolder(View view)
    {
        ButterKnife.bind(this,view);				//Butterknife used for binding
   /*     home_name = (TextView) view.findViewById(R.id.home_name);
        away_name = (TextView) view.findViewById(R.id.away_name);
        score     = (TextView) view.findViewById(R.id.score_textview);
        date      = (TextView) view.findViewById(R.id.data_textview);
        home_crest = (ImageView) view.findViewById(R.id.home_crest);
        away_crest = (ImageView) view.findViewById(R.id.away_crest);*/
    }
}
