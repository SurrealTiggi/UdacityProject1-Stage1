package baptista.tiago.popularmovies.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.adapters.AllMoviesAdapter;
import baptista.tiago.popularmovies.models.AllMovies;
import baptista.tiago.popularmovies.utils.ParseUtil;
import baptista.tiago.popularmovies.utils.URLUtil;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();
    private AllMovies mAllMovies;
    private String mAPIKey;
    private String mQuery;
    private String mURL;

    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Start progress bar to show user that something is happening
        mProgressBar.setVisibility(View.VISIBLE);

        // Check if network is available before doing anything
        if (isNetworkAvailable()) {
            // fetch API key
            mAPIKey = getString(R.string.API_KEY);
            // build URL, default=popular if first load
            mQuery = getString(R.string.popularity_query);
            mURL = URLUtil.buildSearchURL(mQuery, mAPIKey);
            // build movie data model
            getMovies(mURL); // Need to offload this to another class to tidy up main activity
            toggleRefresh();
        } else {
            popNetworkError();
        }
    }

    private void getMovies(String url) {
        String tmdbURL = url;

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
                            mAllMovies = ParseUtil.parseMovies(jsonData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            popError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
            Log.d(TAG, "Main UI code is running!!!");
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
        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        toggleRefresh();
    }

    private void popNetworkError() {
        //AlertDialogFragment dialog = new AlertDialogFragment();
        //dialog.show(getFragmentManager(), "error_dialog");
        Toast.makeText(this, getString(R.string.network_is_broken), Toast.LENGTH_SHORT).show();
        toggleRefresh();
    }

    private void updateDisplay() {
        AllMoviesAdapter adapter = new AllMoviesAdapter(this, mAllMovies.getMovies());
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }
}


