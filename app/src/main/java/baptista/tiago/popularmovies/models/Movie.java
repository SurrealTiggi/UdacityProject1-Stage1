package baptista.tiago.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiggi on 9/14/2015.
 */
public class Movie implements Parcelable {

    private String mOriginalTitle;
    private String mPoster;
    private String mSynopsis;
    private Double mRating;
    private String mReleaseDate;
    private int mMovieID;

    private List<Trailers> mTrailers = new ArrayList<Trailers>();
    private List<Reviews> mReviews = new ArrayList<Reviews>();

    public Movie() {
    }

    private Movie(Parcel in) {
        mOriginalTitle = in.readString();
        mPoster = in.readString();
        mSynopsis = in.readString();
        mRating = in.readDouble();
        mReleaseDate = in.readString();
        mMovieID = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mPoster);
        dest.writeString(mSynopsis);
        dest.writeDouble(mRating);
        dest.writeString(mReleaseDate);
        dest.writeInt(mMovieID);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public List<Reviews> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Reviews> reviews) {
        mReviews = reviews;
    }

    public List<Trailers> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<Trailers> trailers) {
        mTrailers = trailers;
    }

    public String getMovieID() {
        return String.valueOf(mMovieID);
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

/*    public String[] getMovieArray() {
        String[] movieDetails = new String[6];
        movieDetails[0] = this.getOriginalTitle();
        movieDetails[1] = this.getSynopsis();
        movieDetails[2] = this.getPoster();
        movieDetails[3] = this.getReleaseDate();
        movieDetails[4] = this.getRating().toString();
        //movieDetails[5] = this.getRuntime();
        movieDetails[5] = String.valueOf(this.getMovieID());
        return movieDetails;
    }*/

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }
}
