package com.itis.practice2.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itis.practice2.adapters.ImageAdapter;
import com.itis.practice2.utils.Constants;

import itis.com.practice2.R;

/**
 * List of images.
 */
public class ImagesListFragment extends Fragment implements ImageAdapter.OnItemClickListener {

    private RecyclerView mRvImages;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private OnImageClickListener mCallback;
    private ImageAdapter mAdapter;

    private int mCurrentColumnsNumber;

    public interface OnImageClickListener {
        void onImageClick(View view, ImagesListFragment fragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_images_list, container, false);

        mCurrentColumnsNumber = getResources().getInteger(R.integer.columns);
        mRvImages = (RecyclerView) view.findViewById(R.id.rv_images);
        mGridLayoutManager = new StaggeredGridLayoutManager(mCurrentColumnsNumber, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new ImageAdapter(Constants.getImagesUrls(), getActivity());
        mAdapter.setItemClickListener(this);
        mRvImages.setLayoutManager(mGridLayoutManager);
        mRvImages.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mCurrentColumnsNumber = getResources().getInteger(R.integer.columns);
        mRvImages.setLayoutManager(new StaggeredGridLayoutManager(mCurrentColumnsNumber,
                StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (OnImageClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onItemClick(int position, View view) {
        if (mCallback != null) mCallback.onImageClick(view, this);
    }
}
