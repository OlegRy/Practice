package com.itis.practice2;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    public void onImageClick(View view, ImagesListFragment fragment) {

        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        Bitmap image = ((BitmapDrawable) iv_image.getDrawable()).getBitmap();
        ImageFragment imageFragment = ImageFragment.newInstance(image);

        FragmentTransitionLauncher.with(view.getContext()).image(image).from(iv_image).prepare(imageFragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, imageFragment).addToBackStack(null).commit();

    }

}
