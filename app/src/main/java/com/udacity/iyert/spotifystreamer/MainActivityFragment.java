package com.udacity.iyert.spotifystreamer;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.squareup.picasso.Picasso;
import com.udacity.iyert.spotifystreamer.dto.ArtistItem;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    EditText mArtistText;
    ListView mArtistListView;

    ArtistListAdapter mArtistListAdapter;

    private List<ArtistItem> mArtistList = new ArrayList<ArtistItem>();

    public static final String ARTIST_ID_EXTRA = "artistId";
    public static final String ARTIST_LIST_KEY = "artistList";

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARTIST_LIST_KEY, (ArrayList<ArtistItem>) mArtistList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                tracksIntent.putExtra("artistId", artist.getArtistId());
                startActivity(tracksIntent);
            }
        });


        //mArtistText.addTextChangedListener(new SearchListener());

        return view;
    }


    private class SearchEditListener implements TextView.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                if (!event.isShiftPressed()) {
                    // the user is done typing.

                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            mArtistText.getText(), Toast.LENGTH_LONG);
                    toast.show();

                    FetchArtistsClass fetchArtistsClass = new FetchArtistsClass();
                    fetchArtistsClass.execute(new String[]{String.valueOf(mArtistText.getText())});

                    return true; // consume.
                }
            }
            return false;
        }
    }

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

    private class FetchArtistsClass extends AsyncTask<String, Void, List<ArtistItem>>{
        @Override
        protected List<ArtistItem> doInBackground(String... params) {

            List<ArtistItem> artists = null;

            if(params==null || params[0]==null || params[0].equals(""))
                return null;

            String query = params[0];

            //Connect to SpotifyApi
            SpotifyApi api = new SpotifyApi();

            //Create A Spotify Service object
            SpotifyService spotify = api.getService();

            ArtistsPager artistsPager = spotify.searchArtists(query);


            if(artistsPager==null){

            }else{
                artists = new ArrayList<ArtistItem>();
                for(Artist artist: artistsPager.artists.items){
                    Log.d("SearchArtistsTask", artist.name);
                    ArtistItem artistItem = new ArtistItem();
                    artistItem.setArtistId(artist.id);
                    artistItem.setArtistName(artist.name);
                    for (Image image : artist.images){
                        if(image.height==64 && image.width==64){
                            //Set image in ImageView
                            artistItem.setArtistImageUrl(image.url);
                        }
                    }
                    artists.add(artistItem);
                }


                //mArtistListAdapter.clear();
                //mArtistListAdapter.addAll(artists);


            }

            return artists;
        }

        @Override
        protected void onPostExecute(List<ArtistItem> artists) {
            super.onPostExecute(artists);
            if(artists!=null) {
                mArtistListAdapter.clear();
                mArtistListAdapter.addAll(artists);
                mArtistList = artists;
            }
        }
    }
}
