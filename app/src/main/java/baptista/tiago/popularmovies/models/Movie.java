package baptista.tiago.popularmovies.models;

import java.util.List;

/**
 * Created by Tiggi on 9/14/2015.
 */
public class Movie {

    private String mOriginalTitle;
    private String mPoster;
    private String mSynopsis;
    private Double mRating;
    private String mReleaseDate;
    private int mMovieID;
    private List<String> mTrailers;
    private List<String> mReviews;

    public int getMovieID() {
        return mMovieID;
    }

    public void setMovieID(int MovieID) {
        this.mMovieID = MovieID;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String synopsis) {
        mSynopsis = synopsis;
    }

    public Double getRating() {
        return mRating;
    }

    public void setRating(Double rating) {
        mRating = rating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String[] getMovieArray() {
        String[] movieDetails = new String[6];
        movieDetails[0] = this.getOriginalTitle();
        movieDetails[1] = this.getSynopsis();
        movieDetails[2] = this.getPoster();
        movieDetails[3] = this.getReleaseDate();
        movieDetails[4] = this.getRating().toString();
        //movieDetails[5] = this.getRuntime();
        movieDetails[5] = String.valueOf(this.getMovieID());
        return movieDetails;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public Movie() {
    }
}
