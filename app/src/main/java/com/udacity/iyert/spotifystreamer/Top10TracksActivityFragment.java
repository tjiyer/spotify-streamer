package com.udacity.iyert.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.squareup.picasso.Picasso;
import com.udacity.iyert.spotifystreamer.adapter.TrackListAdapter;
import com.udacity.iyert.spotifystreamer.dto.TrackItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

/**
 * The Top10TracksFragment which contains the list of the top 10 tracks for an artist
 * Parent View - mainActivity
 */
public class Top10TracksActivityFragment extends Fragment {

    private ListView mTracksListView;
    private TrackListAdapter mTrackListAdapter;

    private List<TrackItem> mTracksList = new ArrayList<TrackItem>();

    public static final String TRACKS_LIST_KEY = "tracksList";

    public Top10TracksActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRACKS_LIST_KEY, (ArrayList<TrackItem>) mTracksList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_top10_tracks, container, false);

        if(savedInstanceState!=null && savedInstanceState.containsKey(TRACKS_LIST_KEY)){
            mTracksList = savedInstanceState.getParcelableArrayList(TRACKS_LIST_KEY);
        } else{
            String artistId = getActivity().getIntent().getStringExtra(MainActivityFragment.ARTIST_ID_EXTRA);
            FetchTracksTask fetchTracksTask = new FetchTracksTask();
            fetchTracksTask.execute(new String[]{artistId});
        }

        mTracksListView = (ListView) view.findViewById(R.id.tracks_list);
        mTrackListAdapter = new TrackListAdapter(getActivity(), R.layout.track_item_layout, mTracksList);
        mTracksListView.setAdapter(mTrackListAdapter);

        return view;
    }


    /**
     * Private Asyctask Class for quering the artist's top tracks on background thread
     */
    private class FetchTracksTask extends AsyncTask<String, Void, List<TrackItem>> {
        @Override
        protected List<TrackItem> doInBackground(String... params) {

            List<TrackItem> tracks = null;

            // return null if params null or empty
            if(params==null || params[0]==null || params[0].equals(""))
                return null;

            //get the query
            String query = params[0];

            //Connect to SpotifyApi
            SpotifyApi api = new SpotifyApi();

            //Create A Spotify Service object
            SpotifyService spotify = api.getService();

            //country setting needed for topTracks API call
            Map<String, Object> options = new HashMap<>();
            options.put("country", "US");

            Tracks topTracks = null;

            try{
                //get the topTracks for the artist query
                topTracks= spotify.getArtistTopTrack(query, options);
            } catch (RetrofitError r){
                Log.e("FetchTracks", r.getMessage());
            } catch (Exception e){
                Log.e("FetchTracks", e.getMessage());
            }


            if (topTracks!=null && topTracks.tracks!=null && topTracks.tracks.size()!=0){
                //create new arraylist
                tracks = new ArrayList<TrackItem>();

                for(Track track: topTracks.tracks){
                    TrackItem trackItem = new TrackItem();
                    trackItem.setTrackId(track.id);
                    trackItem.setTrackName(track.name);
                    trackItem.setAlbumName(track.album.name);
                    if(track.album.images!=null && track.album.images.size()>0) {
                        for (Image image : track.album.images){
                            if(image.height==64 && image.width==64){
                                //Set image in ImageView
                                trackItem.setAlbumImageUrl(image.url);
                            }
                        }
                    }
                    tracks.add(trackItem);
                }
            }

            return tracks;
        }

        @Override
        protected void onPostExecute(List<TrackItem> tracks) {
            super.onPostExecute(tracks);
            if(tracks!=null){
                mTrackListAdapter.clear();
                mTrackListAdapter.addAll(tracks);
                mTracksList = tracks;
            }
        }
    }
}
