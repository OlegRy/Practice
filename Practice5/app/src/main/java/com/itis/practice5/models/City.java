package com.itis.practice5.models;

import java.util.List;

public class City {

    public enum Marker {
        COUNTRY_CAPITAL, STATE_CENTER, WITH_AIRPORT, BUSINESS_CENTER, RESORT
    }

    private String mName;
    private long mPopulation;
    private Location mLocation;
    private List<District> mDistricts;
    private List<Marker> mMarkers;

    public List<District> getDistricts() {
        return mDistricts;
    }

    public void setDistricts(List<District> districts) {
        mDistricts = districts;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public List<Marker> getMarkers() {
        return mMarkers;
    }

    public void setMarkers(List<Marker> markers) {
        mMarkers = markers;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getPopulation() {
        return mPopulation;
    }

    public void setPopulation(long population) {
        mPopulation = population;
    }
}
