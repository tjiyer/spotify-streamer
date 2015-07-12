package com.udacity.iyert.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Tejas on 7/4/2015.
 */
public class TrackListAdapter extends ArrayAdapter<Track> {

    public TrackListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_item_layout, parent, false);
        }

        Track track = getItem(position);

        ImageView iconView = (ImageView) convertView.findViewById(R.id.artist_image_view);
        if(track.album.images!=null && track.album.images.size()>0) {
            for (Image image : track.album.images){
                if(image.height==64 && image.width==64){
                    //Set image in ImageView
                    Picasso.with(getContext()).load(image.url).into(iconView);
                }
            }
        }

        TextView versionNameView = (TextView) convertView.findViewById(R.id.artist_name_view);
        versionNameView.setText(track.name);

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
