package com.udacity.iyert.spotifystreamer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.iyert.spotifystreamer.R;
import com.udacity.iyert.spotifystreamer.dto.ArtistItem;
import com.udacity.iyert.spotifystreamer.dto.TrackItem;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Tejas on 7/4/2015.
 * @author Tejas
 * Custom Adapter for the Track List View
 */
public class TrackListAdapter extends ArrayAdapter<TrackItem> {

    public TrackListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public TrackListAdapter(Context context, int resource, List<TrackItem> list){
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_item_layout, parent, false);
        }

        //get the item
        TrackItem track = getItem(position);

        //set the image and other fields
        ImageView iconView = (ImageView) convertView.findViewById(R.id.artist_image_view);
        if(track.getAlbumImageUrl()!=null && !track.getAlbumImageUrl().equals("")) {
            //Set image in ImageView
            Picasso.with(getContext()).load(track.getAlbumImageUrl()).into(iconView);
        }else {
            iconView.setImageResource(R.drawable.no_image_default);
        }


        TextView trackNameView = (TextView) convertView.findViewById(R.id.track_name_view);
        trackNameView.setText(track.getTrackName());

        TextView albumNameView = (TextView) convertView.findViewById(R.id.album_name_view);
        albumNameView.setText(track.getAlbumName());

        /*ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);*/

        /*viewHolder.artistImageView.setImageURI(Uri.parse(artist.images.get(0).url));
        viewHolder.artistNameView.setText(artist.name);
*/
        return convertView;
    }

    /**
     * Private view holder class
     * //had problems while using -- will refactor in stage 2
     */
    public static class ViewHolder {
        public final ImageView artistImageView;
        public final TextView artistNameView;

        public ViewHolder(View view) {
            artistImageView = (ImageView) view.findViewById(R.id.artist_image_view);
            artistNameView = (TextView) view.findViewById(R.id.artist_name_view);
        }
    }
}
