package baptista.tiago.popularmovies.interfaces;

/**
 * Created by Tiggi on 11/3/2015.
 */
public interface OnAPITaskCompleted<E> {
    void onAPITaskCompleted(String result, int task);
}
