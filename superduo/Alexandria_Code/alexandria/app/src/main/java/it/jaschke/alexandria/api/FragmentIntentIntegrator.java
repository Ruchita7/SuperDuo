package it.jaschke.alexandria.api;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * New class created for callback from scanning activity
 */
public final class FragmentIntentIntegrator extends IntentIntegrator {

    private final Fragment fragment;

    public FragmentIntentIntegrator(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
    }

    @Override
    protected void startActivityForResult(Intent intent, int code) {
        fragment.startActivityForResult(intent, code);
    }


}
