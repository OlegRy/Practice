package com.itis.practice5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.itis.practice5.models.Country;
import com.itis.practice5.utils.CountriesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        try {
            List<Country> countries = CountriesHelper.findCountriesByCitiesCount(new JSONObject(getJsonFromAssets("input3.json")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getJsonFromAssets(String fileName) {
        try {
            StringBuilder builder = new StringBuilder();
            InputStream json = getAssets().open(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                builder.append(str);
            }
            in.close();
            return builder.toString();
        } catch (IOException ignored) {
            return "";
        }
    }

}

