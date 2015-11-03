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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;


import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.models.Movie;
import baptista.tiago.popularmovies.models.Reviews;
import baptista.tiago.popularmovies.models.Trailers;
import baptista.tiago.popularmovies.storage.DataStore;
import baptista.tiago.popularmovies.utils.ParseUtil;
import baptista.tiago.popularmovies.utils.URLUtil;

public class MovieDetailActivityFragment extends Fragment {

    private static final String TAG = MovieDetailActivityFragment.class.getName();
    public static final String CURRENT_MOVIE = TAG + ".CURRENT.MOVIE";
    public boolean mRan;
    private Activity mActivity;
    private View mView;
    private Intent mIntent;
    //private String[] mCurrentMovieDetails;
    private Movie mCurrentMovieParc;
    private boolean mTablet;
    private List<Trailers> mTrailers;
    private List<Reviews> mReviews;
    private DataStore mDataStore;
    private String mAPIKey;

    // Placeholder views
    private FrameLayout mPlaceholderFrameLayout;
    private ViewGroup mTrailersContainer;
    private ViewGroup mReviewsContainer;

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
        this.mRan = false;
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreateView()");


        mView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (mTablet && mCurrentMovieParc == null) {
            Log.d(TAG, "I'm a tablet with no movies, so initializing empty placeholder fragment...");
            // Container views
            mScrollView = (ScrollView) mView.findViewById(R.id.scrollView);
            mTitleView = (TextView) mView.findViewById(R.id.detailOriginalTitle);
            mTitleView.setVisibility(View.GONE);
            mScrollView.setVisibility(View.GONE);

            return mView;
        } else {
            //mView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
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

            mTrailersContainer = (ViewGroup) mView.findViewById(R.id.trailerPlaceholder);
            mReviewsContainer = (ViewGroup) mView.findViewById(R.id.reviewPlaceholder);


            // This is dirty but it works for now...PARCELATE!!!
            if (!mTablet) {
                //mCurrentMovieDetails = mIntent.getStringArrayExtra("CURRENT_MOVIE");
                mCurrentMovieParc = mIntent.getParcelableExtra("CURRENT_MOVIE");
                /*mTrailers = mCurrentMovieDetails.getTrailers();
                mReviews = mCurrentMovieDetails.getReviews();*/
            }

            if (!mRan) {
                setupTrailersAndReviews();
            }

            updateDisplay();
        }
            return mView;
        }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(savedInstanceState);
        // Set this to not run trailers/reviews api call again
        mRan = true;
    }

    private void updateDisplay() {
        mPlaceholderFrameLayout.setVisibility(View.GONE);

        // Populate main view
        mTitleView.setText(mCurrentMovieParc.getOriginalTitle());
        mSynopsisView.setText(mCurrentMovieParc.getSynopsis());
        Picasso.with(getActivity())
                .load(
                        URLUtil.buildPosterURL(mCurrentMovieParc.getPoster())
                )
                .placeholder(R.drawable.placeholder_poster)
                .error(R.drawable.error_poster)
                .into(mPosterView);
        mReleaseDateView.setText(mCurrentMovieParc.getReleaseDate());
        mRatingView.setText(mCurrentMovieParc.getRating().toString());
        //mRuntimeView.setText(mCurrentMovieDetails[7]);

        // Setup favorites
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

        // Populate trailers/reviews, if null do nothing else create dynamically
    }

    private void setupTrailersAndReviews() {
        Log.d(TAG, "setupTrailersAndReviews()");
        if(mTrailers == null || mReviews == null) {
            mAPIKey = getString(R.string.API_KEY);
            String trailerURL = URLUtil.buildTrailerURL(mCurrentMovieParc.getMovieID(), mAPIKey);
            String reviewURL = URLUtil.buildReviewURL(mCurrentMovieParc.getMovieID(), mAPIKey);
            //doAPICall(trailerURL);
            //doAPICall(reviewURL);
        }
    }

    private void doAPICall(String url) {
    }

    private boolean checkFavorite() {
        boolean isIt = mDataStore.isMovieFavorite(mCurrentMovieParc.getMovieID());
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
        mDataStore.deleteMovie(mCurrentMovieParc);
    }

    private void addFavorite() {
        mDataStore.addMovie(mCurrentMovieParc);
    }


    public void setCurrentMovieDetails(Movie currentMovieDetails) {
        mCurrentMovieParc = currentMovieDetails;
        /*mTrailers = mCurrentMovieDetails.getTrailers();
        mReviews = mCurrentMovieDetails.getReviews();*/
    }

    public void setTablet(boolean tablet) {
        mTablet = tablet;
    }
}
