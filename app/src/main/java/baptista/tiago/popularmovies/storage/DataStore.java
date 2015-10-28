package baptista.tiago.popularmovies.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import baptista.tiago.popularmovies.models.Movie;

/**
 * Created by Tiggi on 10/28/2015.
 */
public class DataStore {
    private static final String TAG = DataStore.class.getName();
    public static final String FAVORITES = TAG + ".favorites";

    public static void initialize(Context context) {
        Log.d(TAG, "Initializing DataStore()");
        // Check if preferences exist, create if not
        SharedPreferences favoritesStore = context.getSharedPreferences(FAVORITES, context.MODE_PRIVATE);
        if (favoritesStore.getString("favorites", null) == null) {
            Log.d(TAG, "No saved favorites found");
            SharedPreferences.Editor editor = context.getSharedPreferences(FAVORITES, context.MODE_PRIVATE).edit();
            editor.putString("favorites", null);
            editor.commit();
        }
    }

    public static String getFavorites() {
        return null;
    }

    public static void process(Movie movie) {
    }

    public static boolean checkIfFavorite(String id) {
        return false;
    }
}
