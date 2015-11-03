package baptista.tiago.popularmovies.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import baptista.tiago.popularmovies.interfaces.OnAPITaskCompleted;
import baptista.tiago.popularmovies.models.Trailers;

/**
 * Created by Tiggi on 11/3/2015.
 */
public class APITask extends AsyncTask<Uri, Void, String> {

    private static final String TAG = Trailers.class.getName();
    private OnAPITaskCompleted<String> delegate;
    private String resultsJson;
    private int task;

    public APITask(OnAPITaskCompleted<String> delegate, int task) {
        Log.d(TAG, "APITask()");
        this.delegate = delegate;
        this.task = task;
    }

    @Override
    protected String doInBackground(Uri... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        resultsJson = null;
        Uri searchURL = params[0];
        Log.d(TAG, "Initiating AsyncTask with task: " + this.task + ", and url: " + searchURL);

        try {
            URL url = new URL(searchURL.toString());

            // Creates request and opens connections
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read input stream and store it
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // No trailers found
                return resultsJson;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return resultsJson;
            }
            resultsJson = buffer.toString();
        } catch (Exception e) {
            Log.e(TAG, "doInBackground(): Exception caught! " + e);
        } finally {
            if (urlConnection != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "doInBackground(): Exception caught! " + e);
                }
            }
        }
        return resultsJson;
    }

    @Override
    protected void onPostExecute(String result) {
        this.delegate.onAPITaskCompleted(result, this.task);
    }
}
