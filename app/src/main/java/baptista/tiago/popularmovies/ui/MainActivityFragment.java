package baptista.tiago.popularmovies.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import baptista.tiago.popularmovies.R;

public class MainActivityFragment extends android.support.v4.app.Fragment{

    public static final String TAG = MainActivityFragment.class.getName();
    private Context mContext;

    public MainActivityFragment() {
        Log.d(TAG, "MainActivityFragment()");
        mContext = getActivity();
        Log.d(TAG, mContext + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
