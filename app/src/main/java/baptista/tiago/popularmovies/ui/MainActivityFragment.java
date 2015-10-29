package baptista.tiago.popularmovies.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.adapters.AllMoviesAdapter;
import baptista.tiago.popularmovies.interfaces.MovieSelectorInterface;
import baptista.tiago.popularmovies.models.AllMovies;
import baptista.tiago.popularmovies.models.Movie;
import baptista.tiago.popularmovies.utils.ParseUtil;
import baptista.tiago.popularmovies.utils.URLUtil;

public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private Context mContext;
    private View mView;
    private RecyclerView mRecyclerView;
    private RelativeLayout mPlaceholderLayout;

    private AllMovies mAllMovies;
    private String mAPIKey;
    private String mQuery;
    private String mURL;
    private int mColumns;
    private int mPage;

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
    public void onResume() {
        super.onResume();
        Log.d(TAG, "----------------------------------------------");
        Log.d(TAG, "onResume(): " + mQuery);

        if (mQuery != null) {
            if (mQuery != getSortOrder()) {
                this.initiateAPICall(1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        if (mView != null) {
            try {
                ((ViewGroup) mView.getParent()).removeView(mView);
                mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
                mPlaceholderLayout = (RelativeLayout) mView.findViewById(R.id.placeholderLayout);

                mPlaceholderLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);

                updateDisplay();
            } catch (Exception e) {
                Log.w(TAG, "onCreateView(): Exception caught: " + e);
                e.printStackTrace();
            }
        } else {
            this.mView = inflater.inflate(R.layout.fragment_main, container, false);

            mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
            mPlaceholderLayout = (RelativeLayout) mView.findViewById(R.id.placeholderLayout);

            mPlaceholderLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);

            this.initiateAPICall(0);
        }
        return mView;
    }

    // Quick workaround, will fix when implementing infinite scroll
    //[0] = Running for the first time
    //[1] = Running from onResume()
    //[2] = Running from onResume() for favorites
    private void initiateAPICall(int i) {
        if (mAllMovies == null || i == 1) {
            mPage = 1;
            mAPIKey = getString(R.string.API_KEY);
            mQuery = getSortOrder();
            mURL = URLUtil.buildSearchURL(mQuery, mAPIKey, mPage);
            try {
                this.getMovies();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Growing movie model...");
        }
    }

    private void getMovies() {
        Log.d(TAG, "getMovies()");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
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
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } catch (Exception e) {
                            // Quick exception catcher to fix crash. Need to handle this better
                            Log.e(TAG, "getMovies().onResponse(): Exception caught: " + e);
                        }
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
        Log.d(TAG, "updateDisplay()");
        mPlaceholderLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        AllMoviesAdapter adapter = new AllMoviesAdapter(mContext, mAllMovies.getMovies());
        mRecyclerView.setAdapter(adapter);

        // Including a GestureDetector to capture clicks here instead of inside the RecyclerView adapter
        final GestureDetector mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Movie currentMovie = mAllMovies
                            .getMovies()[mRecyclerView.getChildAdapterPosition(child)];

                    // Inform main activity of item being clicked on
                    ((MovieSelectorInterface) getActivity()).onItemSelected(currentMovie);
                    return true;
                }
                return false;
            }

            @Override // Mandatory
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }

            @Override // Mandatory
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, getSpan());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private int getSpan() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        mColumns = Math.round(dpWidth / mView.getWidth());

        return mColumns + 1;
    }

    private String getSortOrder() {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortOrder = (shared.getString(getString(R.string.settings_sort_order_key), ""));
        Log.d(TAG, "Pref Sort Order[" + sortOrder + "]");
        return sortOrder;
    }

}
