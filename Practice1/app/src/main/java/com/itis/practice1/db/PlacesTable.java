package com.itis.practice1.db;

import android.database.sqlite.SQLiteDatabase;

public class PlacesTable {

    public static final String TABLE_PLACES = "places";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private static final String CREATE_TABLE_PLACES = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(200) NOT NULL, " +
                    "%s TEXT NOT NULL, %s REAL, %s REAL)",
            TABLE_PLACES, ID, TITLE, DESCRIPTION, LATITUDE, LONGITUDE
    );

    private static final String DROP_TABLE_PLACES = String.format("DROP TABLE IF EXISTS %s", TABLE_PLACES);

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLACES);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_PLACES);
        onCreate(db);
    }

}
