package baptista.tiago.popularmovies.utils;

import android.util.Log;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import baptista.tiago.popularmovies.models.AllMovies;
import baptista.tiago.popularmovies.models.Movie;
import baptista.tiago.popularmovies.models.Reviews;
import baptista.tiago.popularmovies.models.Trailers;

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
            movie.setMovieID(jsonMovie.getInt("id"));

            movies[i] = movie;
        }
        return movies;
    }

    private static List<Trailers> parseTrailers(String jsonData) throws JSONException {
        List<Trailers> trailers = new ArrayList<Trailers>();

        JSONObject trailerList = new JSONObject(jsonData);
        JSONArray results = trailerList.getJSONArray("results");
        for (int i = 0; i < results.length(); i++ ) {
            JSONObject t = results.getJSONObject(i);

            if (t.getString("type").equals("Trailer")) {
                Trailers trailer = new Trailers();

                trailer.setID(t.getString("id"));
                trailer.setKey(t.getString("key"));
                trailer.setName(t.getString("name"));
                trailer.setSite(t.getString("site"));
                trailer.setSize(t.getLong("size"));
                trailer.setType(t.getString("type"));

                trailers.add(trailer);
            }
        }
        return trailers;
    }

    private static List<Reviews> parseReviews(String jsonData) throws JSONException {
        List<Reviews> reviews = new ArrayList<Reviews>();

        JSONObject reviewsList = new JSONObject(jsonData);
        JSONArray results = reviewsList.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject r = results.getJSONObject(i);
            Reviews review = new Reviews();

            review.setID(r.getString("id"));
            review.setAuthor(r.getString("author"));
            review.setContent(r.getString("content"));
            review.setURL(r.getString("url"));

            reviews.add(review);
        }
        return reviews;
    }
}
