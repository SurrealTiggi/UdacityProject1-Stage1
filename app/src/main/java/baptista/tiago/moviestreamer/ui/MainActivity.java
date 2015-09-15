package baptista.tiago.moviestreamer.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import baptista.tiago.moviestreamer.R;
import baptista.tiago.moviestreamer.models.Api;
import baptista.tiago.moviestreamer.models.Movies;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private boolean mNetworkAvailable;
    private Movies mMovies;

    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.loadingTextView)
    TextView mLoadingTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.VISIBLE);

        Api api = new Api();
        api.setAPIKey(getString(R.string.API_KEY));
        api.setQuery(getString(R.string.popularity_query));
        api.setURL(getString(R.string.TMDB_URL));

        getMovies();
    }

    private void getMovies() {
        String tmdbURL; // use URI builder here

        if (isNetworkAvailable()) {
            toggleRefresh();

            tmdbURL = "https://api.themoviedb.org/3/discover/movie?api_key=4ad55f8322cc144be9c7665c5d3bff06&sort_by=popularity.desc";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(tmdbURL)
                    .build();

            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    popError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            //mMovies = parseMovies(jsonData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //updateDisplay();
                                    Toast.makeText(MainActivity.this, "Got my data so will update", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } //catch (JSONException e) {
                     //   Log.e(TAG, "Exception caught: ", e);
                    //}
                }
            });
            Log.d(TAG, "Main UI code is running!!!");
        }
        else {
            Toast.makeText(MainActivity.this, getString(R.string.network_is_broken), Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            //mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            //mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void popError() {
        //AlertDialogFragment dialog = new AlertDialogFragment();
        //dialog.show(getFragmentManager(), "error_dialog");
        Toast.makeText(this, getString(R.string.network_is_broken), Toast.LENGTH_SHORT).show();
    }
}


