package com.itis.practice1;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.itis.practice1.db.PlacesProvider;
import com.itis.practice1.db.PlacesTable;
import com.itis.practice1.model.Place;
import com.itis.practice1.services.RequestService;

import java.util.ArrayList;
import java.util.List;

public class PracticeActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        RoutingListener, android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LATITUDE_TAG = "latitude";
    public static final String LONGITUTE_TAG = "longitude";
    public static final String BROADCAST_MESSAGE = "message";
    public static final String BROADCAST_BOOLEAN = "isString";

    private static final String[] sProjection = {PlacesTable.ID, PlacesTable.TITLE, PlacesTable.DESCRIPTION, PlacesTable.LATITUDE,
            PlacesTable.LONGITUDE};

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    private static final int DB_LOADER_ID = 1;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras() != null) {
                if (intent.getBooleanExtra(BROADCAST_BOOLEAN, true)) {
                    double latitude = intent.getDoubleExtra(LATITUDE_TAG, 0);
                    double longitude = intent.getDoubleExtra(LONGITUTE_TAG, 0);
                    String message = intent.getStringExtra(BROADCAST_MESSAGE);
                    showMessage(message, getResources().getString(R.string.retry), latitude, longitude);
                } else {
                    insertOrUpdate((ArrayList<Place>) intent.getSerializableExtra(BROADCAST_MESSAGE));
                    showMessage(getResources().getString(R.string.message), null, 0, 0);
                }
            }
        }
    };

    // Activity overrided methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Place place = (Place) data.getSerializableExtra(InfoActivity.PLACE_TAG);
        Uri uri = Uri.parse(PlacesProvider.CONTENT_URI + "/" + place.getId());
        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra(InfoActivity.DELETE_TAG, false)) {
                getContentResolver().update(uri, place.toValues(), null, null);
            } else {
                getContentResolver().delete(uri, null, null);
            }
            restartLoad();
        }
    }

    // Google map overrided methods
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        getSupportLoaderManager().initLoader(DB_LOADER_ID, null, PracticeActivity.this).forceLoad();
        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_LONG).show();
        }
    }

    // Fused API overrided methods
    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {
            double currentLatitude = mCurrentLocation.getLatitude();
            double currentLongitude = mCurrentLocation.getLongitude();

            LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));

            insertOrUpdateMyLocation(currentLatitude, currentLongitude);
            loadData(currentLatitude, currentLongitude);
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


    // RoutingListener overrided method
    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions polylineOptions, Route route) {
        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.BLUE);
        polyoptions.width(10);
        polyoptions.addAll(polylineOptions.getPoints());
        mGoogleMap.addPolyline(polyoptions);
    }

    @Override
    public void onRoutingCancelled() {

    }

    // LoaderCallbacks overrided methods
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = new CursorLoader(this, PlacesProvider.CONTENT_URI, sProjection, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            List<Place> places = new ArrayList<>();
            do {
                places.add(Place.fromCursor(data));
            } while (data.moveToNext());
            addMarkers(places);
            createRoute(getWaypoints(places));
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
    }

    // helpful own methods
    private void addMarkers(final List<Place> places) {
        mGoogleMap.clear();
        if (places != null) {
            for (Place place : places) {
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getLatitude(), place.getLongitute()))
                        .title(place.getName())
                        .snippet(place.getDescription()));
                if (places.indexOf(place) == 0) {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                }
            }
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(PracticeActivity.this, InfoActivity.class);

                    intent.putExtra(InfoActivity.PLACE_TAG, Place.getByName(places, marker.getTitle()));
                    startActivityForResult(intent, 0);
                }
            });
        }
    }

    private List<LatLng> getWaypoints(List<Place> places) {
        List<LatLng> waypoints = new ArrayList<>();

        if (places.size() < 11) waypoints.add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        for (Place place : places) {
            waypoints.add(new LatLng(place.getLatitude(), place.getLongitute()));
        }
        return waypoints;
    }

    private void createRoute(List<LatLng> waypoints) {
        for (int i = 0; i < waypoints.size() - 1; i++) {
            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.WALKING)
                    .withListener(this)
                    .waypoints(waypoints.get(i), waypoints.get(i + 1))
                    .build();
            routing.execute();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void loadData(double currentLatitude, double currentLongitude) {
        Intent requestServiceIntent = new Intent(this, RequestService.class);
        requestServiceIntent.putExtra(LATITUDE_TAG, currentLatitude);
        requestServiceIntent.putExtra(LONGITUTE_TAG, currentLongitude);
        startService(requestServiceIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(RequestService.SERVICE_INTENT_BROADCAST));

    }

    public void insertOrUpdate(List<Place> places) {
        Cursor cursor = getContentResolver().query(PlacesProvider.CONTENT_URI, sProjection, null, null, null);

        if (cursor == null || cursor.getCount() < 2) {
            for (Place place : places) {
                getContentResolver().insert(PlacesProvider.CONTENT_URI, place.toValues());
            }
        } else {
            int counter = 0;
            cursor.moveToFirst();
            cursor.moveToNext();
            do {
                Uri uri = getUri(cursor);
                getContentResolver().update(uri, places.get(counter).toValues(), null, null);
                counter++;
            } while (cursor.moveToNext());
        }
        restartLoad();
    }

    private void insertOrUpdateMyLocation(double latitude, double longitute) {
        Cursor cursor = getContentResolver().query(PlacesProvider.CONTENT_URI, sProjection, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Place place = Place.fromCursor(cursor);
            place.setLatitude(latitude);
            place.setLongitute(longitute);
            getContentResolver().update(getUri(cursor), place.toValues(), null, null);
        } else {
            getContentResolver().insert(PlacesProvider.CONTENT_URI, myLocationPlace(latitude, longitute).toValues());
        }
        restartLoad();
    }

    private Place myLocationPlace(double latitude, double longitude) {
        Place place = new Place();
        place.setName(getResources().getString(R.string.current_position));
        place.setDescription(getResources().getString(R.string.current_position));
        place.setLatitude(latitude);
        place.setLongitute(longitude);
        return place;
    }

    private Uri getUri(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(PlacesTable.ID));
        return Uri.parse(PlacesProvider.CONTENT_URI + "/" + id);
    }

    private void showMessage(String message, String retry, final double latitude, final double longitude) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.message_title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (retry != null) {
            builder.setNegativeButton(retry, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadData(latitude, longitude);
                }
            });
        }
        builder.create();
    }

    private void restartLoad() {
        getSupportLoaderManager().restartLoader(DB_LOADER_ID, null, this);
    }
}
