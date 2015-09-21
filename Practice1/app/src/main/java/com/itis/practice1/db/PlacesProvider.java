package com.itis.practice1.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlacesProvider extends ContentProvider {

    private PlacesDatabaseHelper mDatabaseHelper;

    private static final String AUTHORITY = "com.itis.pracice.providers.places";
    private static final String BASE_PATH = "places";
    private static final int PLACES = 1;
    private static final int PLACE_ID = 2;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/places";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "place";

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, PLACES);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PLACE_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new PlacesDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(PlacesTable.TABLE_PLACES);

        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case PLACES:
                break;
            case PLACE_ID:
                queryBuilder.appendWhere(String.format("%s = %s", PlacesTable.ID, uri.getLastPathSegment()));
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case PLACES:
                return CONTENT_TYPE;
            case PLACE_ID:
                return CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long id = 0;

        switch (uriType) {
            case PLACES:
                id = db.insert(PlacesTable.TABLE_PLACES, null, values);
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case PLACE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) rowsDeleted = db.delete(PlacesTable.TABLE_PLACES
                        , PlacesTable.ID + " = " + id, null);
                else rowsDeleted = db.delete(PlacesTable.TABLE_PLACES, PlacesTable.ID + " = " + id +
                        " and " + selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case PLACES:
                rowsUpdated = db.update(PlacesTable.TABLE_PLACES, values, selection, selectionArgs);
                break;
            case PLACE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsUpdated = db.update(PlacesTable.TABLE_PLACES, values, PlacesTable.ID + " = " +
                            id, null);
                else
                    rowsUpdated = db.update(PlacesTable.TABLE_PLACES, values, PlacesTable.ID + " = " +
                            id + " and " + selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {PlacesTable.ID, PlacesTable.TITLE, PlacesTable.DESCRIPTION,
                PlacesTable.LATITUDE, PlacesTable.LONGITUDE};
        if (projection != null) {
            Set<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            Set<String> availableColumns = new HashSet<>(Arrays.asList(available));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknow column in projection");
            }
        }
    }
}
