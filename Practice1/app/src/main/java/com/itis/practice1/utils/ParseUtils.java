package com.itis.practice1.utils;

import com.itis.practice1.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUtils {

    private static final String JSON_RESPONSE = "response";
    private static final String JSON_GEO_OBJECT_COLLECTION = "GeoObjectCollection";
    private static final String JSON_FEATURE_MEMBER = "featureMember";
    private static final String JSON_GEO_OBJECT = "GeoObject";
    private static final String JSON_NAME = "name";
    private static final String JSON_POINT = "Point";
    private static final String JSON_POS = "pos";

    public static List<Place> parse(String json) throws JSONException {
        List<Place> places = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray placesJson = jsonObject.getJSONObject(JSON_RESPONSE).getJSONObject(JSON_GEO_OBJECT_COLLECTION)
                .getJSONArray(JSON_FEATURE_MEMBER);

        for (int i = 0; i < placesJson.length(); i++) {
            JSONObject geoObject = placesJson.getJSONObject(i).getJSONObject(JSON_GEO_OBJECT);
            JSONObject point = geoObject.getJSONObject(JSON_POINT);
            String[] coordinates = point.getString(JSON_POS).split(" ");

            Place place = new Place();

            place.setName(geoObject.getString(JSON_NAME));
            place.setLatitude(Double.parseDouble(coordinates[1]));
            place.setLongitute(Double.parseDouble(coordinates[0]));

            places.add(place);
        }
        return places;
    }

}
