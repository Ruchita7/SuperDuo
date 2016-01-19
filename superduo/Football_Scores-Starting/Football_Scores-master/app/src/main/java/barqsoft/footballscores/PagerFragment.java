package barqsoft.footballscores;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import barqsoft.footballscores.util.ConstantUtil;

/**
 * Created by yehya khaled on 2/27/2015.
 */
public class PagerFragment extends Fragment {

    public ViewPager mPagerHandler;
    private myPageAdapter mPagerAdapter;
    private MainScreenFragment[] viewFragments = new MainScreenFragment[5];

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int tabIndex = 2;
        int position = 0;


        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        mPagerHandler = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new myPageAdapter(getChildFragmentManager());

//Render tabs according to layout direction(LTR/RTL)
        if (Utilies.getLayoutDirection(getActivity()) == View.LAYOUT_DIRECTION_LTR) {
            for (int i = 0; i < ConstantUtil.NUM_PAGES; i++) {
                Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * ConstantUtil.NO_OF_MILLIS));
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                viewFragments[i] = new MainScreenFragment();
                viewFragments[i].setFragmentDate(mformat.format(fragmentdate));
            }
        } else {
            for (int i = ConstantUtil.NUM_PAGES - 1; i >= 0; i--) {
                Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * ConstantUtil.NO_OF_MILLIS));
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                viewFragments[position] = new MainScreenFragment();
                viewFragments[position].setFragmentDate(mformat.format(fragmentdate));
                position++;
            }
        }

        mPagerHandler.setAdapter(mPagerAdapter);

        //Received tab/item index bundle from MainActivity, set tab index and stored index in another
        // bundle argument for MainScreenFragment
        Bundle bundle = getArguments();
        String key = getResources().getString(R.string.selected_tab);
        if ((bundle != null) && (bundle.get(key) != null)) {
            tabIndex = bundle.getInt(key);
            mPagerHandler.setCurrentItem(tabIndex);
        } else {
            mPagerHandler.setCurrentItem(MainActivity.current_fragment);
        }
        if (bundle != null && bundle.get(getString(R.string.match_id)) != null) {
            String matchId = bundle.getString(getString(R.string.match_id));
            Bundle itemBundle = new Bundle();
            itemBundle.putString(getString(R.string.match_id), matchId);
            viewFragments[tabIndex].setArguments(itemBundle);

        }
        return rootView;
    }

    private class myPageAdapter extends FragmentStatePagerAdapter {
        @Override
        public Fragment getItem(int i) {
            return viewFragments[i];
        }

        @Override
        public int getCount() {
            return ConstantUtil.NUM_PAGES;              //Used constant value
        }

        public myPageAdapter(FragmentManager fm) {
            super(fm);
        }

        // Returns the page title for the top indicator based upon layout direction
        @Override
        public CharSequence getPageTitle(int position) {
            String dayName = "";
            if (Utilies.getLayoutDirection(getActivity()) == View.LAYOUT_DIRECTION_LTR) {
                dayName = getDayName(getActivity(), System.currentTimeMillis() + ((position - 2) * ConstantUtil.NO_OF_MILLIS));
            } else {

                dayName = getDayName(getActivity(), System.currentTimeMillis() + ((Utilies.getPositionForRTL(position) - 2) * 86400000));

            }
            return dayName;
        }

        /**
         * If the date is today, return the localized version of "Today" instead of the actual
         * day name.
         * @param context
         * @param dateInMillis
         * @return
         */
        public String getDayName(Context context, long dateInMillis) {

            Time t = new Time();
            t.setToNow();
            int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
            int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
            if (julianDay == currentJulianDay) {
                return context.getString(R.string.today);
            } else if (julianDay == currentJulianDay + 1) {
                return context.getString(R.string.tomorrow);
            } else if (julianDay == currentJulianDay - 1) {
                return context.getString(R.string.yesterday);
            } else {
                Time time = new Time();
                time.setToNow();
                // Otherwise, the format is just the day of the week (e.g "Wednesday".
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                return dayFormat.format(dateInMillis);
            }
        }
    }
}
