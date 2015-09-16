package baptista.tiago.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.models.Movie;
import baptista.tiago.popularmovies.utils.URLUtil;

/**
 * Created by Tiggi on 9/15/2015.
 */
public class AllMoviesAdapter extends RecyclerView.Adapter<AllMoviesAdapter.MovieViewHolder> {

    private static final String TAG = AllMoviesAdapter.class.getName();

    private Context mContext;
    private Movie[] mMovies;

    public AllMoviesAdapter(Context context, Movie[] movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindMovie(mMovies[position]);
    }

    @Override
    public int getItemCount() {
        return mMovies.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        public Movie currentMovie;
        public ImageView mPosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.posterImageView);
            itemView.setOnClickListener(this);
        }

        public void bindMovie(Movie movie) {
            currentMovie = movie;
            String url = URLUtil.buildPosterURL(movie.getPoster());
            Picasso.with(mContext).load(url).into(mPosterImageView);
        }

        @Override
        public void onClick(View v) {
            String title = currentMovie.getOriginalTitle();
            Log.d(TAG, "bindMovie(): " + title);
            Toast.makeText(mContext, "You clicked on " + title, Toast.LENGTH_SHORT).show();
            // Start new activity with single movie object as parselable arg
        }
    }
}
