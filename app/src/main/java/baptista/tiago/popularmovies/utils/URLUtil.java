package baptista.tiago.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

/**
 * Created by Tiggi on 9/16/2015.
 */
public class URLUtil {
    private static final String TAG  = URLUtil.class.getName();

    // Eg. https://api.themoviedb.org/3/discover/movie?api_key=4ad55f8322cc144be9c7665c5d3bff06&sort_by=popularity.desc
    public static String buildSearchURL(String searchOrder, String key) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("api_key", key)
                .appendQueryParameter("vote_count.gte", "1000")
                .appendQueryParameter("page", "1")
                .appendQueryParameter("sort_by", searchOrder);

        String URL = builder.build().toString();
        Log.d(TAG, "Movie URL[" + URL + ")");
        return(URL);
    }

    // Eg: http://image.tmdb.org/t/p/w500/2i0JH5WqYFqki7WDhUW56Sg0obh.jpg
    public static String buildPosterURL(String image) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185") // "w92", "w154", "w185", "w342", "w500", "w780", or "original"
                .appendPath(image.startsWith("/") ? image.substring(1) : image);

        String URL = builder.build().toString();
        Log.d(TAG, "Poster URL[" + URL + ")");
        return( URL );
    }
}
