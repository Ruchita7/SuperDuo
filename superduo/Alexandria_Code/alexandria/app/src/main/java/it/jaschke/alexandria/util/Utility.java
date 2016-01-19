package it.jaschke.alexandria.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

import it.jaschke.alexandria.MainActivity;
import it.jaschke.alexandria.R;

/**
 * Created by dgnc on 1/10/2016.
 */
public class Utility {

    /*public static final int LIST_OF_BOOKS = 0;
    public static final int ADD_BOOK = 1;
    public static final int ABOUT = 2;

    @IntDef({LIST_OF_BOOKS, ADD_BOOK, ABOUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentPosition
    {

    }*/
    public static boolean checkNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //  NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); ||(mobileNetworkInfo!=null && mobileNetworkInfo.isConnectedOrConnecting())
        return ((networkInfo != null && networkInfo.isConnectedOrConnecting()));
    }

    public static int fragmentPosition(Context context, String tag)
    {
   //   @Utility.FragmentPosition  int position = 0;
      List<String> arr= Arrays.asList(context.getResources().getStringArray(R.array.pref_start_options));
        switch(tag)
        {
            case MainActivity.BOOK_LIST_TAG : return arr.indexOf(context.getResources().getString(R.string.books));

            case MainActivity.ADD_BOOK_TAG : return arr.indexOf(context.getResources().getString(R.string.scan));

            default:  return arr.indexOf(context.getResources().getString(R.string.books));
        }
    }
}
