package com.itis.practice5.models;

import java.util.List;

public class Country {

    private String mName;
    private String mCode;
    private List<City> mCities;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public List<City> getCities() {
        return mCities;
    }

    public void setCities(List<City> cities) {
        mCities = cities;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
