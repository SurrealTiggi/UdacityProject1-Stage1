package baptista.tiago.popularmovies.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.json.JSONException;

import java.util.List;

import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.interfaces.OnAPITaskCompleted;
import baptista.tiago.popularmovies.models.Movie;
import baptista.tiago.popularmovies.models.Reviews;
import baptista.tiago.popularmovies.models.Trailers;
import baptista.tiago.popularmovies.storage.DataStore;
import baptista.tiago.popularmovies.tasks.APITask;
import baptista.tiago.popularmovies.utils.ParseUtil;
import baptista.tiago.popularmovies.utils.URLUtil;

public class MovieDetailActivityFragment extends Fragment implements OnAPITaskCompleted {

    private static final String TAG = MovieDetailActivityFragment.class.getName();
    //public static final String CURRENT_MOVIE = TAG + ".CURRENT.MOVIE";
    public boolean mRan;
    private Activity mActivity;
    private View mView;
    private Intent mIntent;
    private Movie mCurrentMovieDetails;
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
        //Log.d(TAG, "Initializing MovieDetailActivityFragment()");
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
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

        if (mTablet && mCurrentMovieDetails == null) {
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


            if (!mTablet) {
                mCurrentMovieDetails = mIntent.getParcelableExtra("CURRENT_MOVIE");
                mTrailers = mCurrentMovieDetails.getTrailers();
                mReviews = mCurrentMovieDetails.getReviews();
            }
            updateDisplay();
        }
            return mView;
        }

/*    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(savedInstanceState);
        // Set this to not run trailers/reviews api call again
        this.mRan = true;
    }*/

    private void updateDisplay() {
        mPlaceholderFrameLayout.setVisibility(View.GONE);

        // Populate main view
        mTitleView.setText(mCurrentMovieDetails.getOriginalTitle());
        mSynopsisView.setText(mCurrentMovieDetails.getSynopsis());
        Picasso.with(getActivity())
                .load(
                        URLUtil.buildPosterURL(mCurrentMovieDetails.getPoster())
                )
                .placeholder(R.drawable.placeholder_poster)
                .error(R.drawable.error_poster)
                .into(mPosterView);
        mReleaseDateView.setText(mCurrentMovieDetails.getReleaseDate());
        mRatingView.setText(mCurrentMovieDetails.getRating().toString());
        //mRuntimeView.setText(mCurrentMovieDetails[7]);

        // Setup favorites
        this.mIsFavorite = mDataStore.isMovieFavorite(mCurrentMovieDetails.getMovieID());
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

        if (!mRan) {
            fetchTrailersAndReviews();
        }

        // Populate trailers/reviews, if null do nothing else create dynamically
        if(mCurrentMovieDetails.getTrailers().size() == 0) {
            /*View trailerView = (View)getActivity().getLayoutInflater().inflate(R.layout.list_item_no_items_explanation, this.container, false);
            TextView name = (TextView)trailerView.findViewById(R.id.no_items);
            name.setText("No Trailers Available");
            trailersContainer.addView(trailerView);*/
            Log.d(TAG, "No Trailers available");
        }
        if(mCurrentMovieDetails.getReviews().size() == 0) {
            /*View reviewView = (View)getActivity().getLayoutInflater().inflate(R.layout.list_item_no_items_explanation, this.container, false);
            TextView name = (TextView)reviewView.findViewById(R.id.no_items);
            name.setText("No Reviews Available");
            reviewsContainer.addView(reviewView);*/
            Log.d(TAG, "No Reviews available");
        }

        for (Trailers trailer : mCurrentMovieDetails.getTrailers()) {
            Log.d(TAG, "Trailer info: " + trailer.getName() + ", " + trailer.getType());
        }
        for (Reviews review : mCurrentMovieDetails.getReviews()) {
            Log.d(TAG, "Review info: " + review.getAuthor());
        }

    }

    private void fetchTrailersAndReviews() {
        Log.d(TAG, "setupTrailersAndReviews()");
        mAPIKey = getString(R.string.API_KEY);

        if (mTrailers.size() == 0 || mReviews.size() == 0 ) {
            Uri trailerURL = URLUtil.buildTrailerURL(mCurrentMovieDetails.getMovieID(), mAPIKey);
            Uri reviewURL = URLUtil.buildReviewURL(mCurrentMovieDetails.getMovieID(), mAPIKey);

            //Log.d(TAG, "Initiating asynctask...");
            APITask trailersTask = new APITask(this, 0);
            trailersTask.execute(trailerURL);

            APITask reviewsTask = new APITask(this, 1);
            reviewsTask.execute(reviewURL);
            mRan = true;
        }
    }

    @Override
    public void onAPITaskCompleted(String result, int task) {
        switch(task) {
        case 0 :    try {
                        mTrailers = ParseUtil.parseTrailers(result);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                    break;
        case 1 :    try {
                        mReviews = ParseUtil.parseReviews(result);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                    break;
        }
        mCurrentMovieDetails.setTrailers(mTrailers);
        mCurrentMovieDetails.setReviews(mReviews);
        try {
            updateDisplay();
        } catch (Exception e){
            Log.e(TAG, "Pre-empting user being dumb and selecting another movie before the view is updated");
        }
    }

    private void playYoutube(String trailerTag){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerTag));
            startActivity(intent);
        } catch (ActivityNotFoundException ex){
            Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerTag));
            startActivity(intent);
        }
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


    public void setCurrentMovieDetails(Movie currentMovieDetails) {
        mCurrentMovieDetails = currentMovieDetails;
        mTrailers = mCurrentMovieDetails.getTrailers();
        mReviews = mCurrentMovieDetails.getReviews();
    }

    public void setTablet(boolean tablet) {
        mTablet = tablet;
    }
}
