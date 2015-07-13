package com.udacity.iyert.spotifystreamer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.iyert.spotifystreamer.dto.ArtistItem;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Tejas on 7/4/2015.
 */
public class ArtistListAdapter extends ArrayAdapter<ArtistItem> {

    public ArtistListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ArtistListAdapter(Context context, int resource, List<ArtistItem> list){
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_item_layout, parent, false);
        }

        ArtistItem artist = getItem(position);

        ImageView iconView = (ImageView) convertView.findViewById(R.id.artist_image_view);
        if(artist.getArtistImageUrl()!=null && !artist.getArtistImageUrl().equals("")) {
                //Set image in ImageView
                Picasso.with(getContext()).load(artist.getArtistImageUrl()).into(iconView);
        }

        TextView versionNameView = (TextView) convertView.findViewById(R.id.artist_name_view);
        versionNameView.setText(artist.getArtistName());

        /*ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);*/

        /*viewHolder.artistImageView.setImageURI(Uri.parse(artist.images.get(0).url));
        viewHolder.artistNameView.setText(artist.name);
*/
        return convertView;
    }

    public static class ViewHolder {
        public final ImageView artistImageView;
        public final TextView artistNameView;

        public ViewHolder(View view) {
            artistImageView = (ImageView) view.findViewById(R.id.artist_image_view);
            artistNameView = (TextView) view.findViewById(R.id.artist_name_view);
        }
    }
}
