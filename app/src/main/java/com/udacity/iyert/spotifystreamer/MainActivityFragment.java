package com.udacity.iyert.spotifystreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    EditText mArtistText;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mArtistText = (EditText) getActivity().findViewById(R.id.artist_search_edittext);
        mArtistText = (EditText) view.findViewById(R.id.artist_search_edittext);
        //mArtistText.setOnEditorActionListener(new SearchEditListener());
        mArtistText.addTextChangedListener(new SearchListener());

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

                    return true; // consume.
                }
            }
            return false;
        }
    }

    private class SearchListener implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                    s, Toast.LENGTH_SHORT);
            toast.show();

            //Connect to SpotifyApi
            SpotifyApi api = new SpotifyApi();

            //Create A Spotify Service object
            SpotifyService spotify = api.getService();

            ArtistsPager artistsPager = spotify.searchArtists(String.valueOf(s));

            if(artistsPager.artists.next!=null) {
                Toast toastArt = Toast.makeText(getActivity().getApplicationContext(),
                        artistsPager.artists.next, Toast.LENGTH_SHORT);
                toastArt.show();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
