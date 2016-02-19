package de.brunsen.guineatrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.model.Type;

public class GuineaPigCRUD {
    private static final String TAG = GuineaPigCRUD.class.getName();
    private Context mContext;

    public GuineaPigCRUD(Context c) {
        mContext = c;
    }

    public GuineaPig getGuineaPigForId(int id) {
        GuineaPig guineaPig = new GuineaPig();
        guineaPig.setId(id);
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_ID + " = ?";
        String[] arguments = new String[] {"" + id};
        Cursor cursor = database.query(GuineaPigDbContract.GuineaPigEntry.TABLE_NAME, null, selection, arguments, null, null, null);
        if (cursor.moveToFirst()) {
            guineaPig.setName(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_NAME)));
            guineaPig.setBirth(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BIRTH)));
            int genderInt = cursor.getInt(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_GENDER));
            guineaPig.setGender(Gender.fromInt(genderInt));
            guineaPig.setColor(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_COLOR)));
            guineaPig.setBreed(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BREED)));
            int typeInt = cursor.getInt(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_TYPE));
            guineaPig.setType(Type.fromInt(typeInt));
        }
        cursor.close();
        database.close();
        guineaPig.setOptionalData(getOptionalDataForID(id));
        return guineaPig;
    }

    public List<GuineaPig> getGuineaPigs() {
        List<GuineaPig> guineaPigs = new ArrayList<>();
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(GuineaPigDbContract.GuineaPigEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GuineaPig guineaPig = new GuineaPig();
            guineaPig.setId(cursor.getInt(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_ID)));
            guineaPig.setName(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_NAME)));
            guineaPig.setBirth(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BIRTH)));
            int genderInt = cursor.getInt(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_GENDER));
            guineaPig.setGender(Gender.fromInt(genderInt));
            guineaPig.setColor(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_COLOR)));
            guineaPig.setBreed(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BREED)));
            int typeInt = cursor.getInt(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_TYPE));
            guineaPig.setType(Type.fromInt(typeInt));
            guineaPigs.add(guineaPig);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        for (GuineaPig guineaPig : guineaPigs) {
            guineaPig.setOptionalData(getOptionalDataForID(guineaPig.getId()));
        }
        return guineaPigs;
    }

    public void storeGuineaPig(GuineaPig guineaPig) throws IOException, JSONException {
        int id = getHighestID() + 1;
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_ID, id);
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_NAME, guineaPig.getName());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BIRTH, guineaPig.getBirth());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_GENDER, guineaPig.getGender().getPosition());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_COLOR, guineaPig.getColor());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BREED, guineaPig.getBreed());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_TYPE, guineaPig.getType().getPosition());
        database.insert(GuineaPigDbContract.GuineaPigEntry.TABLE_NAME, null, values);
        database.close();
        storeOptionalData(id, guineaPig.getOptionalData());
    }

    public void updateGuineaPig(GuineaPig guineaPig){
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_ID, guineaPig.getId());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_NAME, guineaPig.getName());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BIRTH, guineaPig.getBirth());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_GENDER, guineaPig.getGender().getPosition());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_COLOR, guineaPig.getColor());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BREED, guineaPig.getBreed());
        values.put(GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_TYPE, guineaPig.getType().getPosition());
        String whereClause = GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_ID + " = ?";
        String[] arguments = new String[] {"" + guineaPig.getId()};
        database.update(GuineaPigDbContract.GuineaPigEntry.TABLE_NAME, values, whereClause, arguments);
        database.close();
        updateOptionalData(guineaPig.getId(), guineaPig.getOptionalData());
    }

    public void storeGuineaPigs(List<GuineaPig> guineaPigs) throws IOException, JSONException {
        for (GuineaPig guineaPig : guineaPigs) {
            storeGuineaPig(guineaPig);
        }
    }

    public void deleteGuineaPig(GuineaPig guineaPig) throws IOException, JSONException {
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_ID + " = ?";
        String[] arguments = new String[] {"" + guineaPig.getId()};
        database.delete(GuineaPigDbContract.GuineaPigEntry.TABLE_NAME, selection, arguments);
        database.close();
        deleteOptionalData(guineaPig.getId());
    }

    private GuineaPigOptionalData getOptionalDataForID(int id) {
        GuineaPigOptionalData optionalData = new GuineaPigOptionalData();
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ID + " = ?";
        String[] arguments = new String[] {"" + id};
        Cursor cursor = database.query(GuineaPigDbContract.GuineaPigOptionalEntry.TABLE_NAME, null, selection, arguments, null, null, null);
        if (cursor.moveToFirst()) {
            optionalData.setWeight(cursor.getInt(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_WEIGHT)));
            optionalData.setLastBirth(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_LAST_BIRTH)));
            optionalData.setOrigin(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ORIGIN)));
            optionalData.setLimitations(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_LIMITATIONS)));
            optionalData.setCastrationDate(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_CASTRATION_DATE)));
            optionalData.setPicturePath(cursor.getString(cursor.getColumnIndex(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_PICTURE_PATH)));
        }
        cursor.close();
        database.close();
        return optionalData;
    }

    private void storeOptionalData(int id, GuineaPigOptionalData optionalData) {
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ID, id);
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_WEIGHT, optionalData.getWeight());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_LAST_BIRTH, optionalData.getWeight());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ORIGIN, optionalData.getOrigin());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_LIMITATIONS, optionalData.getOrigin());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_CASTRATION_DATE, optionalData.getCastrationDate());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_PICTURE_PATH, optionalData.getPicturePath());
        database.insert(GuineaPigDbContract.GuineaPigOptionalEntry.TABLE_NAME, null, values);
        database.close();
    }

    private void updateOptionalData(int id, GuineaPigOptionalData optionalData) {
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ID, id);
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_WEIGHT, optionalData.getWeight());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_LAST_BIRTH, optionalData.getWeight());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ORIGIN, optionalData.getOrigin());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_LIMITATIONS, optionalData.getOrigin());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_CASTRATION_DATE, optionalData.getCastrationDate());
        values.put(GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_PICTURE_PATH, optionalData.getPicturePath());
        String whereClause = GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ID + " = ?";
        String[] arguments = new String[] {"" + id};
        database.update(GuineaPigDbContract.GuineaPigOptionalEntry.TABLE_NAME, values, whereClause, arguments);
        database.close();
    }

    private void deleteOptionalData(int id) {
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ID + " = ?";
        String[] arguments = new String[] {"" + id};
        database.delete(GuineaPigDbContract.GuineaPigOptionalEntry.TABLE_NAME, selection, arguments);
        database.close();
    }

    private int getHighestID() {
        int id = 0;
        GuineaPigDbHelper dbHelper = GuineaPigDbHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        try {
            String query = "SELECT MAX(" + GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_ID + ") AS _id FROM " + GuineaPigDbContract.GuineaPigEntry.TABLE_NAME;
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getInt(cursor.getColumnIndex("_id"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error occurred while trying to get the highest Id in Database: " + e.getMessage());
        } finally {
            database.close();
        }
        return id;
    }
}