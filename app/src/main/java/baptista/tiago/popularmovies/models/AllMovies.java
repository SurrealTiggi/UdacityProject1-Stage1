package baptista.tiago.popularmovies.models;

import java.util.List;

/**
 * Created by Tiggi on 9/15/2015.
 */
public class AllMovies {

    private List<Movie> mMovies;

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
    }
}
