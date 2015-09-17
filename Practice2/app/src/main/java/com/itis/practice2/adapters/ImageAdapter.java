package com.itis.practice2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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

        Picasso.with(mContext)
                .load(mImageUrls.get(position))
                .placeholder(R.drawable.ic_launcher)
                .into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_image;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mCallback != null) mCallback.onItemClick(getAdapterPosition(), itemView);
        }
    }
}
