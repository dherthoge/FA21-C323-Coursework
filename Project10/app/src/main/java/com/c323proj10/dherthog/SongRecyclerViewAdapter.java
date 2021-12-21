package com.c323proj10.dherthog;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Song}.\
 */
public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.ViewHolder> {

    // A list of Songs to display
    private List<Song> songs;
    private ActivityCommunicator activityCommunicator;

    /**
     * Creates a SongRecyclerViewAdapter.
     * @param songs Songs to populate the adapter with
     */
    public SongRecyclerViewAdapter(List<Song> songs, ActivityCommunicator activityCommunicator) {
        this.songs = songs;
        this.activityCommunicator = activityCommunicator;
    }

    /**
     * Inflates the View hierarchy and returns it.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The inflated View
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Binds a Song to the given ViewHolder.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvItemName.setText(songs.get(position).getTitle());

        // Tells the activity communicator the song to be played
        holder.tvItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityCommunicator.onItemSelected(songs.get(holder.getAdapterPosition()).getId(), Category.SONG);
            }
        });

        // Tells the activity communicator the song to be favorited
        holder.imgBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityCommunicator.onItemFavorited(songs.get(holder.getAdapterPosition()).getId(), Category.SONG);
            }
        });
    }

    /**
     * Gets the number of Songs in the RecyclerView.
     * @return The number of Songs in the RecyclerView
     */
    @Override
    public int getItemCount() {
        return songs.size();
    }

    /**
     * A class to hold Views for each song.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemName;
        private ImageButton imgBtnFavorite;
        private int position;

        /**
         * Creates a holder for the Event's information.
         *
         * @param view The parent View
         */
        public ViewHolder(View view) {
            super(view);

            tvItemName = view.findViewById(R.id.tv_item_name);
            imgBtnFavorite = view.findViewById(R.id.imgBtn_favorite);
            imgBtnFavorite.setOnClickListener(new View.OnClickListener() {
                /**
                 * Deletes the chosen item.
                 * @param view The clicked button
                 */
                @Override
                public void onClick(View view) {
                }
            });
        }

        /**
         * Sets the position of the holder's Song
         *
         * @param position The position of the holder's Song
         */
        public void setPosition(int position) {
            this.position = position;
        }
    }
}