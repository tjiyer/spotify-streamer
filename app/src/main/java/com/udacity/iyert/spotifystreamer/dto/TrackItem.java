package com.udacity.iyert.spotifystreamer.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tejas on 7/12/2015.
 */
public class TrackItem implements Parcelable {

    private String trackName;
    private String trackId;
    private String albumName;
    private String albumImageUrl;

    public TrackItem() {

    }

    public TrackItem(String trackName, String trackId, String albumName, String albumImageUrl){
        this.trackName = trackName;
        this.trackId = trackId;
        this.albumName = albumName;
        this.albumImageUrl = albumImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Track: " + trackName + " Id:" + trackId + " Album: " + albumName;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(trackName);
        parcel.writeString(trackId);
        parcel.writeString(albumName);
        parcel.writeString(albumImageUrl);
    }


    /** Getters and Setters  **/

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumImageUrl() {
        return albumImageUrl;
    }

    public void setAlbumImageUrl(String albumImageUrl) {
        this.albumImageUrl = albumImageUrl;
    }
}
