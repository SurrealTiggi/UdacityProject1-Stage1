package baptista.tiago.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.utils.URLUtil;

public class MovieDetailActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getName();
    private Activity mActivity;
    private View mView;
    private Intent mIntent;
    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mSynopsisView;
    private TextView mRatingView;
    private TextView mReleaseDateView;


    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        // Declaring variables only visible inside this view
        this.mView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        this.mActivity = getActivity();
        this.mIntent = mActivity.getIntent();
        // Container views
        this.mTitleView = (TextView) mView.findViewById(R.id.detailOriginalTitle);
        this.mSynopsisView = (TextView) mView.findViewById(R.id.detailSynopsis);
        this.mPosterView = (ImageView) mView.findViewById(R.id.detailPosterImageView);
        this.mRatingView = (TextView) mView.findViewById(R.id.detailRating);
        this.mReleaseDateView = (TextView) mView.findViewById(R.id.detailReleaseDate);

        // This is dirty but it works for now...
        String[] currentMovieDetails = mIntent.getStringArrayExtra("CURRENT_MOVIE");

        // Populate the dirtiest detail view
        mTitleView.setText(currentMovieDetails[0]);
        mSynopsisView.setText(currentMovieDetails[1]);
        Picasso.with(getActivity())
                .load(
                        URLUtil.buildPosterURL(currentMovieDetails[2])
                )
                .into(mPosterView);
        mReleaseDateView.setText(currentMovieDetails[3]);
        mRatingView.setText(currentMovieDetails[4]);

        setRetainInstance(true);

        return mView;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Log.d(TAG, "onCreate()");
        setHasOptionsMenu(true);
    }
}
