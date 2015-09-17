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

    private RecyclerView rv_images;
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

        rv_images = (RecyclerView) view.findViewById(R.id.rv_images);

        mCurrentColumnsNumber = getColumnsNumber(view);


        mGridLayoutManager = new StaggeredGridLayoutManager(mCurrentColumnsNumber, StaggeredGridLayoutManager.VERTICAL);

        mAdapter = new ImageAdapter(Constants.getImagesUrls(), getActivity());
        mAdapter.setItemClickListener(this);

        rv_images.setLayoutManager(mGridLayoutManager);
        rv_images.setAdapter(mAdapter);
        return view;
    }

    private int getColumnsNumber(View view) {
        boolean isPortraitOrientation = getActivity().getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT;
        if (view.findViewById(R.id.ll_root_big) == null) {
            return isPortraitOrientation ? 1 : 2;
        }
        return isPortraitOrientation ? 2 : 3;
    }

    private int getColumnsNumber(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) mCurrentColumnsNumber--;
        else mCurrentColumnsNumber++;
        return mCurrentColumnsNumber;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        rv_images.setLayoutManager(new StaggeredGridLayoutManager(getColumnsNumber(newConfig.orientation),
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
