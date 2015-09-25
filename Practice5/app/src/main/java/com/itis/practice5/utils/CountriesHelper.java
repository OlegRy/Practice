package com.itis.practice5.utils;

import com.itis.practice5.models.City;
import com.itis.practice5.models.Country;
import com.itis.practice5.models.District;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountriesHelper {

    public static List<Country> findCountriesByCitiesCount(JSONObject jsonCountries) {
        List<Country> allCountries = getAllCountries(jsonCountries);
        if (allCountries != null && allCountries.size() > 0) {
            List<Country> foundCountries = new ArrayList<>();
            for (Country country : allCountries) {
                if (country.getCities() != null && country.getCities().size() > 27) {
                    foundCountries.add(country);
                }
            }
            return foundCountries;
        }
        return new ArrayList<>();
    }

    public static List<City> findCitiesByPopulation(JSONObject jsonCountries) {
        List<Country> allCountries = getAllCountries(jsonCountries);
        if (allCountries != null && allCountries.size() > 0) {
            List<City> foundCities = new ArrayList<>();
            for (Country country : allCountries) {
                if (country.getCities() != null && country.getCities().size() > 0) {
                    for (City city : country.getCities()) {
                        if (city.getPopulation() > 45000000) foundCities.add(city);
                    }
                }
            }
            return foundCities;
        }
        return new ArrayList<>();
    }

    public static Country findBiggestCountry(JSONObject jsonCountries) {
        List<Country> countries = getAllCountries(jsonCountries);
        long maxPopulation = 0;
        int biggestCountryPosition = -1;
        if (countries != null && countries.size() > 0) {
            for (Country country : countries) {
                long population = countPopulation(country);
                if (population > maxPopulation) {
                    maxPopulation = population;
                    biggestCountryPosition = countries.indexOf(country);
                }

            }
            if (biggestCountryPosition > -1) return countries.get(biggestCountryPosition);
        }
        return null;
    }

    public static Country findCountryByLatitude(JSONObject jsonCountries) {
        List<Country> countries = getAllCountries(jsonCountries);
        if (countries != null && countries.size() > 0) {
            for (Country country : countries) {
                if (country.getCities() != null && country.getCities().size() > 0) {
                    if (allCitiesHigherSixty(country.getCities())) {
                        return country;
                    }
                }
            }
        }
        return null;
    }

    public static boolean hasCountryWithCitiesWithLargeDistricts(JSONObject jsonCountries) {
        List<Country> countries = getAllCountries(jsonCountries);
        if (countries != null && countries.size() > 0) {
            for (Country country : countries) {
                int citiesCount = 0;
                if (country.getCities() != null && country.getCities().size() > 0) {
                    for (City city : country.getCities()) {
                        if (hasLargeDistricts(city)) {
                            citiesCount++;
                            if (citiesCount >= 2) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean hasLargeDistricts(City city) {
        if (city.getDistricts() != null && city.getDistricts().size() > 0) {
            int districtsCount = 0;
            for (District district : city.getDistricts()) {
                if (district.getSize().equals(District.Size.LARGE)) {
                    districtsCount++;
                }
            }
            if (districtsCount >= 7) return true;
        }

        return false;
    }

    private static boolean allCitiesHigherSixty(List<City> cities) {
        for (City city : cities) {
            if (city.getLocation().getLatitude() < 60) {
                return false;
            }
        }
        return true;
    }

    private static long countPopulation(Country country) {
        long population = 0;
        if (country.getCities() != null && country.getCities().size() > 0) {
            for (City city : country.getCities()) {
                population += city.getPopulation();
            }
        }
        return population;
    }

    private static List<Country> getAllCountries(JSONObject jsonCountries) {
        return ParseUtils.parse(jsonCountries);
    }


}
