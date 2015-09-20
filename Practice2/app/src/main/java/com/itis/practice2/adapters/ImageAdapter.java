package com.itis.practice2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import itis.com.practice2.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> mImageUrls;
    private Context mContext;
    private OnItemClickListener mCallback;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public ImageAdapter(List<String> imageUrls, Context context) {
        mImageUrls = imageUrls;
        mContext = context;
    }

    public void setItemClickListener(OnItemClickListener callback) {
        mCallback = callback;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(mContext)
                .load(mImageUrls.get(position))
                .asBitmap()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_insert_photo_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.iv_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mCallback != null) mCallback.onItemClick(getAdapterPosition(), itemView);
        }
    }
}
