package baptista.tiago.moviestreamer.models;

/**
 * Created by Tiggi on 9/14/2015.
 */
public class Api {

    // https://api.themoviedb.org/3/discover/movie?api_key=4ad55f8322cc144be9c7665c5d3bff06&sort_by=popularity.desc
    private static String mAPIKey;
    private String mURL;
    private String mQuery;

    public String getAPIKey() {
        return mAPIKey;
    }

    public void setAPIKey(String APIKey) {
        mAPIKey = APIKey;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String URL) {
        mURL = URL;
    }

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public Api() {
    }
}
