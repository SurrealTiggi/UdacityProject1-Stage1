package baptista.tiago.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.utils.URLUtil;

public class MovieDetailActivityFragment extends Fragment {

    private static final String TAG = MovieDetailActivityFragment.class.getName();
    private Activity mActivity;
    private View mView;
    private Intent mIntent;
    private String[] mCurrentMovieDetails;
    private boolean mTablet;

    // Placeholder views
    private FrameLayout mPlaceholderFrameLayout;

    // Detail views
    private ScrollView mScrollView;
    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mSynopsisView;
    private TextView mRatingView;
    private TextView mReleaseDateView;


    public MovieDetailActivityFragment() {
        Log.d(TAG, "Initializing MovieDetailActivityFragment()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(): tablet? " + mTablet);

        //if (mIntent.getStringArrayExtra("CURRENT_MOVIE") == null) {
        if (mTablet && mCurrentMovieDetails == null) {
            Log.d(TAG, "I'm a tablet with no movies, so initializing empty placeholder fragment...");
            // Keep display as is and make other elements disappear
            this.mView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            // Container views
            this.mScrollView = (ScrollView) mView.findViewById(R.id.scrollView);
            this.mTitleView = (TextView) mView.findViewById(R.id.detailOriginalTitle);
            this.mTitleView.setVisibility(View.GONE);
            this.mScrollView.setVisibility(View.GONE);

            return mView;
        } else {
            // need to check if this way doesn't load the detail view in certain instances
            if (mView != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
                return mView;
            } else {
                this.mView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
                this.mPlaceholderFrameLayout = (FrameLayout) mView.findViewById(R.id.placeholderLayout);
                this.mScrollView = (ScrollView) mView.findViewById(R.id.scrollView);
                this.mTitleView = (TextView) mView.findViewById(R.id.detailOriginalTitle);
                this.mSynopsisView = (TextView) mView.findViewById(R.id.detailSynopsis);
                this.mPosterView = (ImageView) mView.findViewById(R.id.detailPosterImageView);
                this.mRatingView = (TextView) mView.findViewById(R.id.detailRating);
                this.mReleaseDateView = (TextView) mView.findViewById(R.id.detailReleaseDate);
                updateDisplay();
            }
            return mView;
        }
    }

    private void updateDisplay() {
        this.mPlaceholderFrameLayout.setVisibility(View.GONE);

        // This is dirty but it works for now...
        if (!mTablet) {
            mCurrentMovieDetails = mIntent.getStringArrayExtra("CURRENT_MOVIE");
        }

        // Populate the dirtiest detail view
        Log.d(TAG, "Attempting to populate detail fragment");
        mTitleView.setText(mCurrentMovieDetails[0]);
        mSynopsisView.setText(mCurrentMovieDetails[1]);

        // change to get from local cache
        Picasso.with(getActivity())
                .load(
                        URLUtil.buildPosterURL(mCurrentMovieDetails[2])
                )
                .placeholder(R.drawable.placeholder_poster)
                .error(R.drawable.error_poster)
                .into(mPosterView);
        mReleaseDateView.setText(mCurrentMovieDetails[3]);
        mRatingView.setText(mCurrentMovieDetails[4]);
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

    public void setCurrentMovieDetails(String[] currentMovieDetails) {
        mCurrentMovieDetails = currentMovieDetails;
    }

    public void setTablet(boolean tablet) {
        mTablet = tablet;
    }
}
