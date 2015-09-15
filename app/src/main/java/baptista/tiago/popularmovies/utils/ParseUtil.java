package baptista.tiago.popularmovies.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import baptista.tiago.popularmovies.models.AllMovies;
import baptista.tiago.popularmovies.models.Movie;

/**
 * Created by Tiggi on 9/15/2015.
 */
public class ParseUtil {
    public static final String TAG = ParseUtil.class.getName();

    public static AllMovies parseMovies(String jsonData) throws JSONException {
        //Movie movie = new Movie();
        Log.d(TAG, "parseMovies()");
        AllMovies movies = new AllMovies();
        movies.setMovies(getMovies(jsonData));
        return movies;
    }

    private static Movie[] getMovies(String jsonData) throws JSONException{
        Log.d(TAG, "getMovies()");
        JSONObject movieList = new JSONObject(jsonData);
        JSONArray results = movieList.getJSONArray("results");
        Movie[] movies = new Movie[results.length()];

        for (int i=0; i < results.length(); i++) {
            JSONObject jsonMovie = results.getJSONObject(i);
            Movie movie = new Movie();

            movie.setOriginalTitle(jsonMovie.getString("original_title"));
            movie.setSynopsis(jsonMovie.getString("overview"));
            movie.setRating(jsonMovie.getDouble("vote_average"));
            movie.setReleaseDate(jsonMovie.getString("release_date"));
            movie.setPoster(jsonMovie.getString("poster_path"));

            movies[i] = movie;
        }

        return movies;
    }
}
