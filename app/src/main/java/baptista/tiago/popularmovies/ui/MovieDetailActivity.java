package baptista.tiago.popularmovies.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.utils.URLUtil;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG = MovieDetailActivity.class.getName();
    @Bind(R.id.detailOriginalTitle) TextView mTitleView;
    @Bind(R.id.detailPosterImageView) ImageView mPosterView;
    @Bind(R.id.detailSynopsis) TextView mSynopsisView;
    @Bind(R.id.detailRating) TextView mRatingView;
    @Bind(R.id.detailReleaseDate) TextView mReleaseDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Log.d(TAG, "onCreate()");

        // This is dirty but it works for now...
        String[] currentMovieDetails = intent.getStringArrayExtra("CURRENT_MOVIE");

        /*
        [0] = title
        [1] = synopsis
        [2] = poster
        [3] = release date
        [4] = rating
        */

        // Populate the dirtiest detail view
        mTitleView.setText(currentMovieDetails[0]);
        mSynopsisView.setText(currentMovieDetails[1]);
        Picasso.with(this)
                .load(
                        URLUtil.buildPosterURL(currentMovieDetails[2])
                )
                .into(mPosterView);
        mReleaseDateView.setText(currentMovieDetails[3]);
        mRatingView.setText(currentMovieDetails[4]);
    }
}
