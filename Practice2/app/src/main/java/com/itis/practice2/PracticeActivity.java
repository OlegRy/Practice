package com.itis.practice2;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;

import com.itis.practice2.fragments.ImageFragment;
import com.itis.practice2.fragments.ImagesListFragment;
import com.kogitune.activity_transition.fragment.FragmentTransitionLauncher;

import itis.com.practice2.R;

public class PracticeActivity extends AppCompatActivity implements ImagesListFragment.OnImageClickListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.container, new ImagesListFragment()).commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onImageClick(View view, ImagesListFragment imagesListFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
        Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ImageFragment imageFragment = ImageFragment.newInstance(image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            prepareTransition(imagesListFragment, imageFragment);
            transaction.addSharedElement(imageView, getResources().getString(R.string.transition));
        } else {
            FragmentTransitionLauncher.with(view.getContext()).image(image).from(imageView).prepare(imageFragment);
        }
        transaction.replace(R.id.container, imageFragment).addToBackStack(null).commit();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void prepareTransition(ImagesListFragment imagesListFragment, ImageFragment imageFragment) {
        Transition changeTransform = TransitionInflater.from(this)
                .inflateTransition(R.transition.change_transform);
        Transition explodeTransition = TransitionInflater.from(this)
                .inflateTransition(android.R.transition.explode);

        imagesListFragment.setSharedElementReturnTransition(changeTransform);
        imagesListFragment.setExitTransition(explodeTransition);

        imageFragment.setSharedElementEnterTransition(changeTransform);
        imageFragment.setEnterTransition(explodeTransition);
    }

}
