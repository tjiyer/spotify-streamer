package com.udacity.iyert.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.udacity.iyert.spotifystreamer.adapter.TrackListAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * The Top10TracksFragment which contains the list of the top 10 tracks for an artist
 * Parent View - mainActivity
 */
public class Top10TracksActivityFragment extends Fragment {

    private ListView mTracksListView;
    private TrackListAdapter mTrackListAdapter;


    public Top10TracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_top10_tracks, container, false);

        mTracksListView = (ListView) view.findViewById(R.id.tracks_list);
        mTrackListAdapter = new TrackListAdapter(getActivity(), R.layout.track_item_layout);
        mTracksListView.setAdapter(mTrackListAdapter);

        String artistId = getActivity().getIntent().getStringExtra(MainActivityFragment.ARTIST_ID_EXTRA);
        FetchTracksTask fetchTracksTask = new FetchTracksTask();
        fetchTracksTask.execute(new String[]{artistId});

        return view;
    }


    /**
     * Private Asyctask Class for quering the artist's top tracks on background thread
     */
    private class FetchTracksTask extends AsyncTask<String, Void, List<Track>> {
        @Override
        protected List<Track> doInBackground(String... params) {

            List<Track> tracks = null;

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

            //get the topTracks for the artist query
            Tracks topTracks= spotify.getArtistTopTrack(query, options);

            tracks = topTracks.tracks;

            return tracks;
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            super.onPostExecute(tracks);
            if(tracks!=null){
                mTrackListAdapter.clear();
                mTrackListAdapter.addAll(tracks);
            }
        }
    }
}
