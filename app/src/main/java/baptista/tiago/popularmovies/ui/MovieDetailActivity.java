package baptista.tiago.popularmovies.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import baptista.tiago.popularmovies.R;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG = MovieDetailActivity.class.getName();
    public static final String CURRENT_MOVIE = TAG + "_CURRENT_MOVIE";
    private MovieDetailActivityFragment fragmentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

       /* Log.d(TAG, "onCreate(): Initializing fragment manager...");
        fragmentData = (MovieDetailActivityFragment) getSupportFragmentManager()
                .findFragmentByTag(CURRENT_MOVIE);

        if (fragmentData == null) {
            Log.d(TAG, "onCreate(): Creating fragment for the first time");
            fragmentData = new MovieDetailActivityFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(fragmentData, CURRENT_MOVIE)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_detail_fragment, fragmentData, CURRENT_MOVIE);
            Log.d(TAG, "onCreate(): Found existing fragment " + fragmentData);
        }*/
    }
}
