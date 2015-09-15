package com.itis.practice1;

import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itis.practice1.utils.RequestUtils;

public class PracticeActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {
            double currentLatitude = mCurrentLocation.getLatitude();
            double currentLongitude = mCurrentLocation.getLongitude();

            LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .title(getResources().getString(R.string.current_position))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            addClosestPlaces(currentLatitude, currentLongitude);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, R.string.connection_suspended, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_LONG).show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void addClosestPlaces(double latitude, double longitude) {
        new RequestUtils(latitude, longitude, mGoogleMap).execute();
    }


    //https://geocode-maps.yandex.ru/1.x/?geocode=55.7852241,49.1635873&rspn=0&format=json&sco=latlong&kind=locality
}
