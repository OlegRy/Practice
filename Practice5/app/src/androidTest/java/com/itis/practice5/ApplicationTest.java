package com.itis.practice5;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.itis.practice5.models.City;
import com.itis.practice5.models.Country;
import com.itis.practice5.utils.CountriesHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

    private static final List<String> INPUT_FILES = new ArrayList<String>() {
        {
            add("input1.json");
            add("input2.json");
            add("input3.json");
            add("input4.json");
            add("input5.json");
            add("input_empty.json");
            add("input_error.json");
        }
    };

    @Rule
    public ActivityTestRule<PracticeActivity> mActivityRule = new ActivityTestRule<>(PracticeActivity.class);

    //TODO : put test methods for all methods you've created;
    //TODO : use mActivityRule.getActivity().getJsonFromAssets() to retrieve json from asset files;
    //TODO : test your code on all input files in INPUT_FILES list.

    @Test
    public void testFindCountriesByCitiesCount() throws JSONException {
        String[][] expectedResults = {
                {},
                {},
                {"Belarus", "Great Britain"},
                {"Russia", "Germany", "USA", "Canada", "Great Britain", "Bangladesh"},
                {"Country 15", "Country 17", "Country 41", "Country 71", "Country 72", "Country 80", "Country 93"},
                {},
                {}
        };

        for (int i = 0; i < expectedResults.length; i++) {
            List<Country> countries = CountriesHelper.findCountriesByCitiesCount(getJSONObjects()[i]);
            assertEquals(expectedResults[i].length, countries.size());
            for (int j = 0; j < expectedResults[i].length; j++) {
                assertTrue(expectedResults[i][j].equals(countries.get(j).getName()));
            }

        }
    }

    @Test
    public void testFindCitiesByPopulation() throws JSONException {
        String[][] expectedResults = {
                {},
                {"ru City 7", "fr City 9", "de City 3", "de City 12"},
                {"ru City 6", "ru City 8", "it City 5", "fr City 5", "de City 7", "us City 11", "es City 3", "es City 5", "by City 7", "by City 9", "by City 11", "by City 12", "by City 19", "by City 23", "by City 30", "gb City 5", "gb City 10", "gb City 23", "gb City 28", "az City 3"},
                {"ru City 10", "ru City 11", "it City 2", "it City 19", "fr City 1", "fr City 2", "fr City 5", "fr City 11", "de City 5", "de City 9", "de City 12", "us City 1", "us City 2", "us City 5", "us City 10", "cn City 5", "es City 2", "by City 7", "cn City 2", "cn City 29", "gb City 3", "gb City 21", "gb City 26", "gb City 30", "ar City 2", "az City 7", "al City 2", "al City 4", "al City 6", "al City 21", "dz City 12", "am City 18", "af City 16", "bd City 13", "bd City 14", "bd City 21", "bd City 22", "br City 3", "br City 8", "va City 5", "va City 7", "hn City 10", "gr City 4"},
                {"10 City 4", "10 City 5", "10 City 9", "13 City 12", "15 City 7", "15 City 9", "15 City 10", "15 City 17", "15 City 19", "15 City 27", "16 City 1", "17 City 8", "17 City 9", "20 City 3", "20 City 5", "21 City 5", "21 City 10", "22 City 3", "22 City 5", "22 City 10", "22 City 15", "24 City 12", "24 City 22", "25 City 11", "25 City 17", "25 City 24", "27 City 18", "28 City 8", "28 City 21", "28 City 23", "30 City 2", "31 City 1", "31 City 19", "32 City 13", "32 City 14", "32 City 15", "32 City 17", "35 City 3", "35 City 4", "35 City 15", "36 City 6", "37 City 1", "37 City 2", "37 City 3", "38 City 2", "38 City 4", "38 City 5", "38 City 8", "39 City 21", "40 City 3", "41 City 2", "42 City 8", "42 City 11", "44 City 6", "44 City 23", "45 City 7", "46 City 2", "46 City 11", "46 City 19", "47 City 5", "47 City 9", "47 City 10", "51 City 7", "53 City 2", "53 City 3", "53 City 9", "54 City 3", "55 City 10", "55 City 16", "56 City 1", "56 City 10", "56 City 11", "56 City 13", "57 City 1", "57 City 4", "58 City 4", "58 City 12", "58 City 16", "59 City 14", "59 City 15", "60 City 16", "61 City 6", "62 City 10", "63 City 9", "64 City 3", "64 City 11", "65 City 4", "65 City 7", "65 City 9", "65 City 19", "65 City 20", "66 City 8", "66 City 9", "67 City 1", "67 City 11", "67 City 12", "67 City 13", "70 City 2", "70 City 9", "70 City 11", "70 City 18", "71 City 22", "71 City 25", "72 City 23", "75 City 4", "75 City 7", "77 City 7", "78 City 2", "79 City 1", "79 City 4", "79 City 14", "80 City 11", "80 City 17", "81 City 1", "81 City 2", "81 City 3", "81 City 7", "81 City 12", "82 City 10", "82 City 11", "83 City 5", "83 City 12", "83 City 13", "84 City 17", "85 City 3", "86 City 2", "86 City 6", "87 City 3", "87 City 9", "89 City 7", "91 City 7", "93 City 8", "93 City 11", "93 City 17", "93 City 25", "95 City 6", "96 City 25", "98 City 12", "98 City 13", "99 City 10", "99 City 14"},
                {},
                {}
        };
        for (int i = 0; i < expectedResults.length; i++) {
            List<City> cities = CountriesHelper.findCitiesByPopulation(getJSONObjects()[i]);
            assertEquals(expectedResults[i].length, cities.size());
            for (int j = 0; j < expectedResults[i].length; j++) {
                assertEquals(expectedResults[i][j], cities.get(j).getName());
            }
        }
    }

    @Test
    public void testFindBiggestCountry() throws JSONException {
        String[] expectedResults = {
                "Russia", "Germany", "Belarus", "USA", "Country 15", "", ""
        };
        for (int i = 0; i < expectedResults.length; i++) {
            String result = expectedResults[i];
            Country country = CountriesHelper.findBiggestCountry(getJSONObjects()[i]);
            if (i > 4) assertNull(country);
            else {
                if (country != null) assertEquals(result, country.getName());

            }
        }
    }

    @Test
    public void testFindCountryByLatitude() throws JSONException {
        String[] expectedResults = {
                "Sample country 1", "Germany", "France", "Italy", "Country 72", "", ""
        };
        for (int i = 0; i < expectedResults.length; i++) {
            String result = expectedResults[i];
            Country country = CountriesHelper.findCountryByLatitude(getJSONObjects()[i]);
            if (i > 4) assertNull(country);
            else {
                if (country != null) assertEquals(result, country.getName());
            }
        }
    }

    @Test
    public void testHasCountryWithCitiesWithLargeDistricts() throws JSONException {
        boolean[] expectedResults = { false, true, true, true, true, false, false };
        for (int i = 0; i < expectedResults.length; i++) {
            boolean result = expectedResults[i];
            boolean hasCountry = CountriesHelper.hasCountryWithCitiesWithLargeDistricts(getJSONObjects()[i]);
            assertEquals(result, hasCountry);
        }

    }

    private JSONObject[] getJSONObjects() throws JSONException {
        JSONObject[] jsonObjects = new JSONObject[INPUT_FILES.size()];
        for (int i = 0; i < INPUT_FILES.size(); i++) {
            jsonObjects[i] = new JSONObject(mActivityRule.getActivity().getJsonFromAssets(INPUT_FILES.get(i)));

        }
        return jsonObjects;
    }
}