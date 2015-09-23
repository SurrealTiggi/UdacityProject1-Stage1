package baptista.tiago.popularmovies.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.settings.SettingsActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private Fragment fragmentData;

    @Bind(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Start progress bar to show user that something is happening
        mProgressBar.setVisibility(View.VISIBLE);

        // Check if network is available before doing anything
        if (isNetworkAvailable()) {
            Log.d(TAG, "onCreate(): Initializing fragment manager...");
            FragmentManager fm = getFragmentManager();
            fragmentData = fm.findFragmentByTag("mContext");

            if (fragmentData == null) {
                Log.d(TAG, "onCreate(): Creating fragment for the first time");
                fragmentData = new Fragment();
                fm.beginTransaction().add(fragmentData, "mContext").commit();
            } else {
                Log.d(TAG, "onCreate(): Found existing fragment " + fragmentData);
            }
        } else {
            popNetworkError();
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.general_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            this.startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
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
    }

    private void popNetworkError() {
        //AlertDialogFragment dialog = new AlertDialogFragment();
        //dialog.show(getFragmentManager(), "error_dialog");
        Toast.makeText(this, getString(R.string.network_is_broken), Toast.LENGTH_SHORT).show();
    }
}


