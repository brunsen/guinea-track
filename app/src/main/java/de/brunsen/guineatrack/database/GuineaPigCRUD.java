package de.brunsen.guineatrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.services.JsonReader;
import de.brunsen.guineatrack.services.JsonWriter;

public class GuineaPigCRUD {
    private String path;
    private String fileName;
    private Context mContext;

    public GuineaPigCRUD(Context c) {
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + c.getString(R.string.folder_path);
        fileName = c.getString(R.string.storage_file_name);
        mContext = c;
    }

    public void storeGuineaPig(GuineaPig pig) throws IOException, JSONException {
        int newID = 1;
        List<GuineaPig> pigs = new ArrayList<>();
        try {
            pigs.addAll(getGuineaPigs());

        } catch (Exception e) {
            Log.e("Failed to load pigs", e.getMessage(), e.getCause());
        }

        if (!pigs.isEmpty()) {
            newID = getHighestID(pigs) + 1;
        }
        pig.setId(newID);
        pigs.add(pig);
        storeGuineaPigs(pigs);
    }

    private void storeGuineaPigs(List<GuineaPig> pigs) throws IOException, JSONException {
        JsonWriter writer = new JsonWriter(mContext);
        String json = writer.createJsonArrayFromList(pigs).toString();
        storeJson(json);
    }

    public void deleteGuineaPig(GuineaPig pig) throws IOException, JSONException {
        int deleteId = pig.getId();
        List<GuineaPig> pigs = getGuineaPigs();
        Iterator<GuineaPig> iterator = pigs.iterator();
        while (iterator.hasNext()) {
            GuineaPig pigToCheck = iterator.next();
            if (pigToCheck.getId() == deleteId) {
                iterator.remove();
            }
        }
        storeGuineaPigs(pigs);
    }

    public List<GuineaPig> getGuineaPigs() throws IOException, JSONException {
        File f = new File(path + fileName);
        List<GuineaPig> guineaPigs = new ArrayList<>();
        if (f.exists()) {
            FileInputStream input = new FileInputStream(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String json = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                json += line;
            }
            reader.close();
            reader = null;
            input = null;
            if (!json.equals("")) {
                JsonReader jsonReader = new JsonReader(mContext);
                guineaPigs.addAll(jsonReader.getGuineaListFromString(json));
            }
        }
        return guineaPigs;
    }

    private GuineaPigOptionalData getOptionalDataForID(int id) {
        GuineaPigOptionalData optionalData = new GuineaPigOptionalData();
        GuineaPigDbHelper dbHelper = new GuineaPigDbHelper(mContext);
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
        database.close();
        return optionalData;
    }

    private void storeOptionalData(int id, GuineaPigOptionalData optionalData) {
        GuineaPigDbHelper dbHelper = new GuineaPigDbHelper(mContext);
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
        GuineaPigDbHelper dbHelper = new GuineaPigDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
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
        GuineaPigDbHelper dbHelper = new GuineaPigDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ID + " = ?";
        String[] arguments = new String[] {"" + id};
        database.delete(GuineaPigDbContract.GuineaPigOptionalEntry.TABLE_NAME, selection, arguments);
        database.close();
    }

    private int getHighestID(List<GuineaPig> pigs) {
        int id = 0;
        for (GuineaPig guineaPig : pigs) {
            if (guineaPig.getId() > id) {
                id = guineaPig.getId();
            }
        }
        return id;
    }

    private void storeJson(String json) throws IOException {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        File f = new File(path + fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream outputStream;
        outputStream = new FileOutputStream(f);
        outputStream.write(json.getBytes());
        outputStream.close();
    }

    public void updateGuineaPig(GuineaPig pigUpdate) throws IOException, JSONException {
        List<GuineaPig> pigs = getGuineaPigs();
        Iterator<GuineaPig> iterator = pigs.iterator();
        while (iterator.hasNext()) {
            GuineaPig pig = iterator.next();
            if (pig.getId() == pigUpdate.getId()) {
                iterator.remove();
                break;
            }
        }
        pigs.add(pigUpdate);
        storeGuineaPigs(pigs);
    }
}