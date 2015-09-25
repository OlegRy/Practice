package com.itis.practice5.utils;

import com.itis.practice5.models.City;
import com.itis.practice5.models.Country;
import com.itis.practice5.models.District;
import com.itis.practice5.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUtils {

    public static final String COUNTRIES = "countries";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String CITIES = "cities";
    public static final String POPULATION = "population";
    public static final String LOCATION = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DISTRICTS = "districts";
    public static final String SIZE = "size";
    public static final String MARKERS = "markers";
    public static final String ERROR = "error";

    public static List<Country> parse(JSONObject response) {
        List<Country> countries = new ArrayList<>();

        try {
            JSONArray jsonCountries = response.getJSONArray(COUNTRIES);
            for (int i = 0; i < jsonCountries.length(); i++) {
                countries.add(parseCountry(jsonCountries.getJSONObject(i)));
            }
        } catch (JSONException e) {
            tryParseError(response);
            return null;
        }
        return countries;
    }

    private static void tryParseError(JSONObject response) {
        try {
            throw new Exception(response.getString(ERROR));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Country parseCountry(JSONObject jsonCountry) {
        try {
            Country country = new Country();
            country.setName(jsonCountry.getString(NAME));
            country.setCode(jsonCountry.getString(CODE));
            country.setCities(parseCities(jsonCountry.getJSONArray(CITIES)));
            return country;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<City> parseCities(JSONArray jsonCities) {
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < jsonCities.length(); i++) {
            try {
                City city = new City();
                JSONObject jsonCity = jsonCities.getJSONObject(i);
                city.setName(jsonCity.getString(NAME));
                city.setPopulation(jsonCity.getLong(POPULATION));
                city.setLocation(parseLocation(jsonCity.getJSONObject(LOCATION)));
                city.setDistricts(parseDistricts(jsonCity.getJSONArray(DISTRICTS)));
                city.setMarkers(parseMarkers(jsonCity.getJSONArray(MARKERS)));
                cities.add(city);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return cities;
    }

    private static List<City.Marker> parseMarkers(JSONArray jsonMarkers) {
        List<City.Marker> markers = new ArrayList<>();
        for (int i = 0; i < jsonMarkers.length(); i++) {
            try {
                String markerName = jsonMarkers.getString(i);
                if (checkMarker(markerName)) {
                    markers.add(City.Marker.valueOf(markerName));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return markers;
    }

    private static boolean checkMarker(String markerName) {
        for (City.Marker marker: City.Marker.values()) {
            if (marker.toString().equals(markerName)) return true;
        }
        return false;
    }

    private static List<District> parseDistricts(JSONArray jsonDistricts) {
        List<District> districts = new ArrayList<>();
        for (int i = 0; i < jsonDistricts.length(); i++) {
            try {
                District district = new District();
                JSONObject jsonDistrict = jsonDistricts.getJSONObject(i);
                district.setName(jsonDistrict.getString(NAME));
                district.setSize(District.Size.valueOf(jsonDistrict.getString(SIZE)));
                districts.add(district);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return districts;
    }

    private static Location parseLocation(JSONObject jsonLocation) {
        try {
            Location location = new Location();
            location.setLatitude(jsonLocation.getDouble(LATITUDE));
            location.setLongitude(jsonLocation.getDouble(LONGITUDE));
            return location;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
