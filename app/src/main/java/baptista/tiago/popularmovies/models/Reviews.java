package baptista.tiago.popularmovies.models;

/**
 * Created by Tiggi on 11/2/2015.
 */
public class Reviews {

    private String mID;
    private String mAuthor;

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String URL) {
        mURL = URL;
    }

    private String mContent;
    private String mURL;

    public Reviews() {}
}
