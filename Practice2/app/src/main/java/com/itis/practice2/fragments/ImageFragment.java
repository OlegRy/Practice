package com.itis.practice2.fragments;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kogitune.activity_transition.fragment.ExitFragmentTransition;
import com.kogitune.activity_transition.fragment.FragmentTransition;

import itis.com.practice2.R;

/**
 * Shows image on full screen
 */
public class ImageFragment extends Fragment {

    private ImageView iv_image;

    private Bitmap mBitmap;

    public static final String BITMAP = "bitmap";

    public static ImageFragment newInstance(Bitmap bitmap) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(BITMAP, bitmap);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        Bundle args = getArguments();

        if (args != null) mBitmap = args.getParcelable(BITMAP);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        iv_image = (ImageView) view.findViewById(R.id.iv_image);

        if (mBitmap != null) iv_image.setImageBitmap(mBitmap);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            final ExitFragmentTransition exitFragmentTransition = FragmentTransition.with(this).to(view.findViewById(R.id.iv_image)).start(savedInstanceState);
            exitFragmentTransition.startExitListening();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
