package com.example.flickrbrowserapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> mPhotosList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(Context context, List<Photo> photosList) {
        mContext = context;
        mPhotosList = photosList;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        if ((mPhotosList == null) || (mPhotosList.size() == 0)) {
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(R.string.empty_photo);
        } else {
            Photo photoItem = mPhotosList.get(position);
            Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + "------> " + position);

            Picasso.with(mContext).load(photoItem.getImage())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnail);

            holder.title.setText(photoItem.getTitle());

        }
    }

    @Override
    public int getItemCount() {
        // Log.d(TAG, "getItemCount: starts");
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.size() : 1);
    }


    void loadNewData(List<Photo> newPhotos) {
        //    mPhotosList.clear();
        mPhotosList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.get(position) : null);
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail = itemView.findViewById(R.id.thumbnail);
            this.title = itemView.findViewById(R.id.title);
        }
    }
}
