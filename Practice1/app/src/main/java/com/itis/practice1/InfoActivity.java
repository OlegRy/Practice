package com.itis.practice1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.itis.practice1.model.Place;

public class InfoActivity extends AppCompatActivity {

    public static final String PLACE_TAG = "place";
    public static final String DELETE_TAG = "delete";

    private Place mPlace;

    private Toolbar mToolbar;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getData(getIntent());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEditText = (EditText) findViewById(R.id.et_description);
        mToolbar.setTitle(mPlace.getName());
        setSupportActionBar(mToolbar);
        mEditText.setText(mPlace.getDescription());
    }

    private void getData(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            mPlace = (Place) intent.getSerializableExtra(PLACE_TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        intent.putExtra(PLACE_TAG, mPlace);
        if (item.getItemId() == R.id.me_ready) {
            mPlace.setDescription(mEditText.getText().toString());
            intent.putExtra(DELETE_TAG, false);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        } else {
            intent.putExtra(DELETE_TAG, true);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
