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

    private static final String TAG = MovieDetailActivityFragment.class.getName();
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
        if (mView != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
            return mView;
        } else {
            this.mView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
            updateDisplay();
            return mView;
        }
    }

    private void updateDisplay() {
        // Container views
        this.mTitleView = (TextView) mView.findViewById(R.id.detailOriginalTitle);
        this.mSynopsisView = (TextView) mView.findViewById(R.id.detailSynopsis);
        this.mPosterView = (ImageView) mView.findViewById(R.id.detailPosterImageView);
        this.mRatingView = (TextView) mView.findViewById(R.id.detailRating);
        this.mReleaseDateView = (TextView) mView.findViewById(R.id.detailReleaseDate);

        // This is dirty but it works for now...
        String[] currentMovieDetails = mIntent.getStringArrayExtra("CURRENT_MOVIE");

        // Populate the dirtiest detail view
        Log.d(TAG, "Doing a bunch of stuff");
        mTitleView.setText(currentMovieDetails[0]);
        mSynopsisView.setText(currentMovieDetails[1]);

        // change to get from local cache
        Picasso.with(getActivity())
                .load(
                        URLUtil.buildPosterURL(currentMovieDetails[2])
                )
                .placeholder(R.drawable.placeholder_poster)
                .error(R.drawable.error_poster)
                .into(mPosterView);
        mReleaseDateView.setText(currentMovieDetails[3]);
        mRatingView.setText(currentMovieDetails[4]);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Log.d(TAG, "onCreate()");
        this.mActivity = getActivity();
        this.mIntent = mActivity.getIntent();
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }
}
