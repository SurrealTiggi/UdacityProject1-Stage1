package baptista.tiago.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

/**
 * Created by Tiggi on 9/16/2015.
 */
public class URLUtil {
    private static final String TAG  = URLUtil.class.getName();

    public static String buildSearchURL(String searchOrder, String key, int page) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("api_key", key)
                .appendQueryParameter("vote_count.gte", "1000")
                .appendQueryParameter("page", page + "")
                .appendQueryParameter("sort_by", searchOrder);

        String URL = builder.build().toString();
        return URL;
    }

    public static String buildPosterURL(String image) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185") // "w92", "w154", "w185", "w342", "w500", "w780", or "original"
                .appendPath(image.startsWith("/") ? image.substring(1) : image);

        String URL = builder.build().toString();
        return URL;
    }
    // Eg: https://api.themoviedb.org/3/movie/76341/videos?api_key=4ad55f8322cc144be9c7665c5d3bff06
    public static Uri buildTrailerURL(String movieID, String key) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + movieID)
                .appendPath("videos")
                .appendQueryParameter("api_key", key);

        Uri URL = builder.build();
        return URL;
    }
    // Eg: https://api.themoviedb.org/3/movie/76341/reviews?api_key=4ad55f8322cc144be9c7665c5d3bff06
    public static Uri buildReviewURL(String movieID, String key) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + movieID)
                .appendPath("reviews")
                .appendQueryParameter("api_key", key);

        Uri URL = builder.build();
        return URL;
    }
}
