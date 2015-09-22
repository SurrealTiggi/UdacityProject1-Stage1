package baptista.tiago.popularmovies.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MainActivityFragment extends Fragment {

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
            updateDisplay();
            return mView;
        } else {
            this.mView = inflater.inflate(R.layout.fragment_main, container, false);

            // fetch API key
            mAPIKey = getString(R.string.API_KEY);
            // build URL, default=popular if first load
            mQuery = getString(R.string.popularity_query);
            mURL = URLUtil.buildSearchURL(mQuery, mAPIKey);
            this.getMovies();
            return mView;
        }
    }

    private void getMovies() {
        Log.d(TAG, "getMovies()");

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

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);

        AllMoviesAdapter adapter = new AllMoviesAdapter(mContext, mAllMovies.getMovies());
        mRecyclerView.setAdapter(adapter);

        // should be getting recycleview width, not screen width
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        // replace 300 with xml layout
        int columns = Math.round(dpWidth/300);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, columns + 1);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }
}
