package org.notlocalhost.coffeeshop.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

import org.notlocalhost.coffeeshop.ui.interfaces.OnDrawerListener;

public abstract class BaseDrawerFragment extends Fragment {
    protected OnDrawerListener mDrawerListener;

    public BaseDrawerFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDrawerListener = (OnDrawerListener)activity;
        } catch (ClassCastException cce) {
            throw new RuntimeException("Activity " + activity.getClass().getCanonicalName()
                    + " must implement OnDrawerListener");
        }
    }

    public abstract void onPanelSlide(View view, float v);
}
