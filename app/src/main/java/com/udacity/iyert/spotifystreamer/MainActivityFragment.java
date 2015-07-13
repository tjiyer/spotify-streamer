package com.udacity.iyert.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.iyert.spotifystreamer.adapter.ArtistListAdapter;
import com.udacity.iyert.spotifystreamer.dto.ArtistItem;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


/**
 * Fragment for the Main Activity which contains the artist List.
 */
public class MainActivityFragment extends Fragment {

    EditText mArtistText;
    ListView mArtistListView;

    ArtistListAdapter mArtistListAdapter;

    private List<ArtistItem> mArtistList = new ArrayList<ArtistItem>();

    public static final String ARTIST_ID_EXTRA = "artistId";
    public static final String ARTIST_NAME_EXTRA = "artistName";
    public static final String ARTIST_LIST_KEY = "artistList";
    private static final String NO_RESULTS_TEXT = "No results found for: ";

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save the artist List
        outState.putParcelableArrayList(ARTIST_LIST_KEY, (ArrayList<ArtistItem>) mArtistList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //if artistList is Saved - reload
        if(savedInstanceState != null && savedInstanceState.containsKey(ARTIST_LIST_KEY)) {
            mArtistList = savedInstanceState.getParcelableArrayList(ARTIST_LIST_KEY);
        }
        mArtistListAdapter = new ArtistListAdapter(getActivity(), R.layout.artist_item_layout, mArtistList);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mArtistText = (EditText) view.findViewById(R.id.artist_search_edittext);
        mArtistText.setOnEditorActionListener(new SearchEditListener());

        mArtistListView = (ListView) view.findViewById(R.id.artist_list_view);
        mArtistListView.setAdapter(mArtistListAdapter);
        mArtistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistListAdapter artistListAdapter = (ArtistListAdapter) parent.getAdapter();

                ArtistItem artist = artistListAdapter.getItem(position);

                Intent tracksIntent = new Intent(getActivity(), Top10TracksActivity.class);
                tracksIntent.putExtra(ARTIST_ID_EXTRA, artist.getArtistId());
                tracksIntent.putExtra(ARTIST_NAME_EXTRA, artist.getArtistName());
                startActivity(tracksIntent);
            }
        });

        return view;
    }

    /**
     * Private class for the EdittextListner
     * Code heavily borrowed from stackOverflow
     * @Link http://stackoverflow.com/questions/8063439/android-edittext-finished-typing-event
     */
    private class SearchEditListener implements TextView.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //If user clicks search or hits enter
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||(
                    event!=null && event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                // the user is done typing - query the name
                FetchArtistsClass fetchArtistsClass = new FetchArtistsClass();
                fetchArtistsClass.execute(new String[]{String.valueOf(mArtistText.getText())});

                return true; // consume.
            }
            return false;
        }
    }

    // - This is very network intensive  - so kept for later refactoring
    /*private class SearchListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                    s, Toast.LENGTH_SHORT);
            toast.show();

            /*//*/Connect to SpotifyApi
            SpotifyApi api = new SpotifyApi();

            //Create A Spotify Service object
            SpotifyService spotify = api.getService();*//*

           // ArtistsPager artistsPager = spotify.searchArtists(String.valueOf(s));

            *//*if(artistsPager.artists.next!=null) {
                Toast toastArt = Toast.makeText(getActivity().getApplicationContext(),
                        artistsPager.artists.next, Toast.LENGTH_SHORT);
                toastArt.show();
            }*//*

        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }*/

    /**
     * Private Asynctask class to query and return the Artist results on background thread
     */
    private class FetchArtistsClass extends AsyncTask<String, Void, List<ArtistItem>>{

        /**
         *
         * @param params - string array of params to pass must pass in the artist query
         * @return List<ArtistItem> - list of artistItem objects to populate list view
         */
        @Override
        protected List<ArtistItem> doInBackground(String... params) {

            List<ArtistItem> artists = null;

            //return null if params is null or empty
            if(params==null || params[0]==null || params[0].equals(""))
                return null;

            //get the query
            String query = params[0];

            //Connect to SpotifyApi
            SpotifyApi api = new SpotifyApi();

            //Create A Spotify Service object
            SpotifyService spotify = api.getService();

            //Query for the artists
            ArtistsPager artistsPager = spotify.searchArtists(query);

            //if artists!=null
            if(artistsPager!=null){

                //create new arrayList
                artists = new ArrayList<ArtistItem>();

                //loop through the artists and populate the ArtistItem DTO
                for(Artist artist: artistsPager.artists.items){
                    Log.d("SearchArtistsTask", artist.name);
                    ArtistItem artistItem = new ArtistItem();
                    artistItem.setArtistId(artist.id);
                    artistItem.setArtistName(artist.name);
                    for (Image image : artist.images){
                        // Use only the thumbnail image
                        if(image.height==64 && image.width==64){
                            //Set image in ImageView
                            artistItem.setArtistImageUrl(image.url);
                        }
                    }
                    artists.add(artistItem);
                }

            }

            return artists;
        }

        /**
         * Override onPostExecute to set the new list in the Adapter
         * @param artists - list of <ArtistItem> objects
         */
        @Override
        protected void onPostExecute(List<ArtistItem> artists) {
            super.onPostExecute(artists);
            //if the new list is not null or empty replace the list in listView
            if(artists!=null && artists.size()!=0) {
                mArtistListAdapter.clear();
                mArtistListAdapter.addAll(artists);
                mArtistList = artists;
            } else{
                //clear list - or else earlier results still remain
                mArtistListAdapter.clear();
                mArtistList.clear();  // Not sure if need to do this or keep earlier list active

                //Show message to user for no results found
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        NO_RESULTS_TEXT+mArtistText.getText(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
