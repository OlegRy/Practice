package com.itis.practice1.model;

public class Place {

    private String mName;
    private double mLongitute;
    private double mLatitude;

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
}
