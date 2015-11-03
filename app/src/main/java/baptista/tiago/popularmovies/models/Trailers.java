package baptista.tiago.popularmovies.models;

/**
 * Created by Tiggi on 11/2/2015.
 */
public class Trailers {
    private String mID;
    private String mKey;
    private String mName;
    private String mSite;
    private long mSize;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        mSite = site;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        mSize = size;
    }

    private String mType;

    public Trailers() {}
}
