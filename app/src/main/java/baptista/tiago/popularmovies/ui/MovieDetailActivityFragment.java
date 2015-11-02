package baptista.tiago.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
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
import baptista.tiago.popularmovies.storage.DataStore;
import baptista.tiago.popularmovies.utils.URLUtil;

public class MovieDetailActivityFragment extends Fragment {

    private static final String TAG = MovieDetailActivityFragment.class.getName();
    public static final String CURRENT_MOVIE = TAG + ".CURRENT.MOVIE";
    private Activity mActivity;
    private View mView;
    private Intent mIntent;
    private String[] mCurrentMovieDetails;
    private boolean mTablet;
    private DataStore mDataStore;

    // Placeholder views
    private FrameLayout mPlaceholderFrameLayout;

    // Detail views
    private ScrollView mScrollView;
    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mRatingView;
    private TextView mReleaseDateView;
    private TextView mRuntimeView;
    private ImageView mFavoriteImageView;
    private TextView mFavoriteTextView;
    private TextView mSynopsisView;
    private TextView mTrailer1;
    private TextView mTrailer2;
    private TextView mTrailer3;
    private TextView mReview1;
    private TextView mReview2;
    private boolean mIsFavorite;

    public MovieDetailActivityFragment() {
        Log.d(TAG, "Initializing MovieDetailActivityFragment()");
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Log.d(TAG, "onCreate()");
        this.mActivity = getActivity();
        this.mIntent = mActivity.getIntent();
        this.mDataStore = new DataStore(mActivity);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreateView()");


        mView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (mTablet && mCurrentMovieDetails == null) {
            Log.d(TAG, "I'm a tablet with no movies, so initializing empty placeholder fragment...");
            // Container views
            mScrollView = (ScrollView) mView.findViewById(R.id.scrollView);
            mTitleView = (TextView) mView.findViewById(R.id.detailOriginalTitle);
            mTitleView.setVisibility(View.GONE);
            mScrollView.setVisibility(View.GONE);

            return mView;
        } else {
            mView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
            mPlaceholderFrameLayout = (FrameLayout) mView.findViewById(R.id.placeholderLayout);
            mScrollView = (ScrollView) mView.findViewById(R.id.scrollView);
            mTitleView = (TextView) mView.findViewById(R.id.detailOriginalTitle);
            mSynopsisView = (TextView) mView.findViewById(R.id.detailSynopsis);
            mPosterView = (ImageView) mView.findViewById(R.id.detailPosterImageView);
            mRatingView = (TextView) mView.findViewById(R.id.detailRating);
            mReleaseDateView = (TextView) mView.findViewById(R.id.detailReleaseDate);
            mRuntimeView = (TextView) mView.findViewById(R.id.detailRuntime);
            mFavoriteImageView = (ImageView) mView.findViewById(R.id.favoriteImageView);
            mFavoriteTextView = (TextView) mView.findViewById(R.id.favoriteTextView);

            // Assume not a favorite, update next time
            /*mIsFavorite = false;
            mFavoriteImageView.setImageResource(R.drawable.favorite_off);
            mFavoriteTextView.setText(getString(R.string.favorite_add));*/
            updateDisplay();
        }
            return mView;
        }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(savedInstanceState);
        // save movie details as Parcelable
        savedInstanceState.putStringArray(CURRENT_MOVIE, this.mCurrentMovieDetails);
    }

    private void updateDisplay() {
        mPlaceholderFrameLayout.setVisibility(View.GONE);

        // This is dirty but it works for now...PARCELATE!!!
        if (!mTablet) {
            mCurrentMovieDetails = mIntent.getStringArrayExtra("CURRENT_MOVIE");
        }

        // Populate the dirtiest detail view
        mTitleView.setText(mCurrentMovieDetails[0]);
        mSynopsisView.setText(mCurrentMovieDetails[1]);
        Picasso.with(getActivity())
                .load(
                        URLUtil.buildPosterURL(mCurrentMovieDetails[2])
                )
                .placeholder(R.drawable.placeholder_poster)
                .error(R.drawable.error_poster)
                .into(mPosterView);
        mReleaseDateView.setText(mCurrentMovieDetails[3]);
        mRatingView.setText(mCurrentMovieDetails[4]);
        //mRuntimeView.setText(mCurrentMovieDetails[7]);

        this.mIsFavorite = checkFavorite();

        if (mIsFavorite) {
            mFavoriteImageView.setImageResource(R.drawable.favorite_on);
            mFavoriteTextView.setText(R.string.favorite_remove);
        } else {
            mFavoriteImageView.setImageResource(R.drawable.favorite_off);
            mFavoriteTextView.setText(R.string.favorite_add);
        }

        mFavoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });
    }

    private boolean checkFavorite() {
        boolean isIt = mDataStore.isMovieFavorite(mCurrentMovieDetails[5]);
        return isIt;
    }

    private void toggleFavorite() {
        if (mIsFavorite) {
            removeFavorite();
            mFavoriteImageView.setImageResource(R.drawable.favorite_off);
            mFavoriteTextView.setText(getString(R.string.favorite_add));
            mIsFavorite = false;
        } else {
            addFavorite();
            mFavoriteImageView.setImageResource(R.drawable.favorite_on);
            mFavoriteTextView.setText(getString(R.string.favorite_remove));
            mIsFavorite = true;
        }
    }

    private void removeFavorite() {
        mDataStore.deleteMovie(mCurrentMovieDetails);
    }

    private void addFavorite() {
        mDataStore.addMovie(mCurrentMovieDetails);
    }


    public void setCurrentMovieDetails(String[] currentMovieDetails) {
        mCurrentMovieDetails = currentMovieDetails;
    }

    // Disable this?
    public void setTablet(boolean tablet) {
        mTablet = tablet;
    }
}
