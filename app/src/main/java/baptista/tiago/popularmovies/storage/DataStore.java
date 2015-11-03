package baptista.tiago.popularmovies.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import baptista.tiago.popularmovies.models.AllMovies;
import baptista.tiago.popularmovies.models.Movie;

/**
 * Created by Tiggi on 10/28/2015.
 */
public class DataStore extends SQLiteOpenHelper {
    private static final String TAG = DataStore.class.getName();
    private static Context mContext;

    // SQLite Variables
    public static final String DB_NAME = "my_favorites.db";
    public static final String TABLE_NAME = "favorite_movies";
    public static final String F_MOVIE_ID = "id";
    public static final String F_TITLE = "original_title";
    public static final String F_SYNOPSIS = "overview";
    public static final String F_RELEASE_DATE = "release_date";
    public static final String F_RATING = "vote_average";
    public static final String F_POSTER = "poster_path";
   /* public static final String F_RUNNING_TIME = "runtime";
    public static final String F_TRAILER_KEY = "trailer_key";*/
    private static String DB_PATH;

    private static final String[] COLUMNS = {
            F_MOVIE_ID,
            F_TITLE,
            F_SYNOPSIS,
            F_RELEASE_DATE,
            F_RATING,
            F_POSTER
    };

    public DataStore(Context context) {
        super(context, DB_NAME, null, 1);
        Log.d(TAG, "Initializing DataStore()");
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");
        DB_PATH = Environment.getDataDirectory() + "/data/" + mContext.getApplicationContext().getPackageName() + "/databases/" + DB_NAME;
        Log.d(TAG, "Data store >>> " + DB_PATH);

        //Log.d(TAG, "DB doesn't exist, creating it...");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                        F_MOVIE_ID + " INTEGER PRIMARY KEY," +
                        F_TITLE + " TEXT," +
                        F_SYNOPSIS + " TEXT," +
                        F_RELEASE_DATE + " TEXT," +
                        F_RATING + " DOUBLE," +
                        F_POSTER + " TEXT)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading the DB...");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

/*    private boolean doesDatabaseExist() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            Log.e(TAG, "DB does not exist yet");
        }
        return checkDB != null;
    }*/

    public AllMovies getAllFavorites() {
        AllMovies allFavorites = new AllMovies();
        List<Movie> movies = new ArrayList<Movie>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Movie movie = new Movie();

            movie.setMovieID(cursor.getInt(cursor.getColumnIndexOrThrow(F_MOVIE_ID)));
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(F_TITLE)));
            movie.setSynopsis(cursor.getString(cursor.getColumnIndexOrThrow(F_SYNOPSIS)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(F_RELEASE_DATE)));
            movie.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(F_RATING)));
            movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(F_POSTER)));

            movies.add(movie);
        }
        allFavorites.setMovies(movies);
        return allFavorites;
    }

    public void addMovie(Movie movie) {
        Log.d(TAG, "Adding movie: " + movie.getMovieID());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(F_TITLE, movie.getOriginalTitle());
        cv.put(F_SYNOPSIS, movie.getSynopsis());
        cv.put(F_POSTER, movie.getPoster());
        cv.put(F_RELEASE_DATE, movie.getReleaseDate());
        cv.put(F_RATING, movie.getRating());
        cv.put(F_MOVIE_ID, movie.getMovieID());

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public void deleteMovie(Movie movie) {
        Log.d(TAG, "Deleting movie: " + movie.getMovieID());
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, F_MOVIE_ID + " = ?",
                new String[]{
                        movie.getMovieID()
                });
        db.close();
    }

    public boolean isMovieFavorite(String id) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }
}
