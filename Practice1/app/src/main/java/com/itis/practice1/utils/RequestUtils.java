package com.itis.practice1.utils;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.itis.practice1.model.Place;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RequestUtils extends AsyncTask<Void, Void, String> implements RoutingListener {

    private double mLatitude;
    private double mLongitude;
    private WeakReference<GoogleMap> mGoogleMapWeakReference;


    private static final String REQUEST_URL = "https://geocode-maps.yandex.ru/1.x/?geocode=";
    private static final String PARAM_KIND = "&kind=locality";
    private static final String PARAM_FSPN = "&rspn=0";
    private static final String PARAM_FORMAT = "&format=json";
    private static final String PARAM_SCO = "&sco=latlong";

    public RequestUtils(double latitude, double longitude, GoogleMap map) {
        mLatitude = latitude;
        mLongitude = longitude;
        mGoogleMapWeakReference = new WeakReference<>(map);
    }

    public String executeRequest(double latitude, double longitude) throws IOException {
        String url = REQUEST_URL + latitude + "," + longitude + PARAM_SCO + PARAM_FSPN
                + PARAM_KIND + PARAM_FORMAT;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return executeRequest(mLatitude, mLongitude);
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result != null) {
            GoogleMap map = mGoogleMapWeakReference.get();
            if (map != null) {
                try {
                    List<Place> places = ParseUtils.parse(result);
                    addMarkers(map, places);
                    Routing routing = new Routing.Builder()
                            .travelMode(Routing.TravelMode.WALKING)
                            .withListener(this)
                            .waypoints(getWaypoints(places))
                            .build();
                    routing.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addMarkers(GoogleMap map, List<Place> places) {
        if (places != null) {
            for (Place place : places) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getLatitude(), place.getLongitute()))
                        .title(place.getName()));
            }
        }
    }

    private List<LatLng> getWaypoints(List<Place> places) {
        Log.d("tag", "current: " + mLatitude + "; " + mLongitude);
        List<LatLng> waypoints = new ArrayList<>();

        waypoints.add(new LatLng(mLatitude, mLongitude));

        for (Place place : places) {
            waypoints.add(new LatLng(place.getLatitude(), place.getLongitute()));
        }
        return waypoints;
    }

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions polylineOptions, Route route) {
        GoogleMap map = mGoogleMapWeakReference.get();
        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.BLUE);
        polyoptions.width(10);
        polyoptions.addAll(polylineOptions.getPoints());
        if (map != null) map.addPolyline(polyoptions);
    }

    @Override
    public void onRoutingCancelled() {

    }
}
