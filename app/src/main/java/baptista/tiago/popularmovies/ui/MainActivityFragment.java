package baptista.tiago.popularmovies.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

public class MainActivityFragment extends android.support.v4.app.Fragment{

    public static final String TAG = MainActivityFragment.class.getName();
    private Context mContext;
    private View mView;
    private RecyclerView mRecyclerView;

    private AllMovies mAllMovies;
    private String mAPIKey;
    private String mQuery;
    private String mURL;

    public MainActivityFragment() {
        Log.d(TAG, "Initializing MainActivityFragment()");
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Log.d(TAG, "onCreate()");
        this.mContext = getActivity();
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
            return mView;
        } else {
            this.mView = inflater.inflate(R.layout.fragment_main, container, false);
            this.getMovies();
            return mView;
        }
    }

    private void getMovies() {
        Log.d(TAG, "getMovies()");
        // fetch API key
        mAPIKey = getString(R.string.API_KEY);
        // build URL, default=popular if first load
        mQuery = getString(R.string.popularity_query);
        mURL = URLUtil.buildSearchURL(mQuery, mAPIKey);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mURL)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //popError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        mAllMovies = ParseUtil.parseMovies(jsonData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay();
                                //toggleRefresh();
                            }
                        });
                    } else {
                        //popError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                } catch (JSONException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });
    }

    private void updateDisplay() {
        AllMoviesAdapter adapter = new AllMoviesAdapter(mContext, mAllMovies.getMovies());
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }

}
