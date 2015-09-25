package com.itis.practice1.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.itis.practice1.db.PlacesTable;

import java.io.Serializable;
import java.util.List;

public class Place implements Serializable {

    private long mId;
    private String mName;
    private String mDescription;
    private double mLongitute;
    private double mLatitude;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitute() {
        return mLongitute;
    }

    public void setLongitute(double longitute) {
        mLongitute = longitute;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public static Place fromCursor(Cursor cursor) {
        Place place = new Place();
        place.mId = cursor.getLong(cursor.getColumnIndex(PlacesTable.ID));
        place.mName = cursor.getString(cursor.getColumnIndex(PlacesTable.TITLE));
        place.mDescription = cursor.getString(cursor.getColumnIndex(PlacesTable.DESCRIPTION));
        place.mLatitude = cursor.getDouble(cursor.getColumnIndex(PlacesTable.LATITUDE));
        place.mLongitute = cursor.getDouble(cursor.getColumnIndex(PlacesTable.LONGITUDE));
        return place;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(PlacesTable.TITLE, mName);
        values.put(PlacesTable.DESCRIPTION, mDescription);
        values.put(PlacesTable.LATITUDE, mLatitude);
        values.put(PlacesTable.LONGITUDE, mLongitute);

        return values;
    }

    public static Place getByName(List<Place> places, String name) {
        for (Place place : places) {
            if (place.mName.equals(name)) return place;
        }
        return null;
    }
}
