package baptista.tiago.popularmovies.interfaces;

import baptista.tiago.popularmovies.models.Movie;

/**
 * Created by Tiggi on 10/28/2015.
 */
public interface FavoritesInterface {
    public void onItemFavorited(Movie currentMovie);

    public void onItemCheckFavorite(String id);
}
