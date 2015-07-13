package com.udacity.iyert.spotifystreamer.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tejas on 7/12/2015.
 */
public class ArtistItem implements Parcelable {

    private String artistName;
    private String artistId;
    private String artistImageUrl;

    public ArtistItem() {

    }

    public ArtistItem(String artistName, String artistId, String artistImageUrl){
        this.artistName = artistName;
        this.artistId = artistId;
        this.artistImageUrl = artistImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Artist: " + artistName + " Id:" + artistId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(artistName);
        parcel.writeString(artistId);
        parcel.writeString(artistImageUrl);
    }


    /** Getters and Setters  **/

    public String getArtistImageUrl() {
        return artistImageUrl;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setArtistImageUrl(String artistImageUrl) {
        this.artistImageUrl = artistImageUrl;
    }
}
