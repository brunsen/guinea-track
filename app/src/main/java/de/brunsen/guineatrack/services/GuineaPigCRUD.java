package de.brunsen.guineatrack.services;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class GuineaPigCRUD {
    private String path;
    private String fileName;

    public GuineaPigCRUD(Context c) {
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + c.getString(R.string.folder_path);
        fileName = c.getString(R.string.storage_file_name);
    }

    public void storePig(GuineaPig pig) throws IOException {
        int newID = 1;
        List<GuineaPig> pigs = new ArrayList<>();
        try {
            pigs.addAll(getPigs());

        } catch (Exception e) {
            Log.e("Failed to load pigs", e.getMessage(), e.getCause());
        }

        if (!pigs.isEmpty()) {
            newID = getHighestID(pigs) + 1;
        }
        pig.setId(newID);
        pigs.add(pig);
        storePigs(pigs);
    }

    private void storePigs(List<GuineaPig> pigs) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(pigs);
        storeJson(json);
    }

    public void deletePig(GuineaPig pig) throws IOException {
        int deleteId = pig.getId();
        List<GuineaPig> pigs = getPigs();
        Iterator<GuineaPig> iterator = pigs.iterator();
        while (iterator.hasNext()) {
            GuineaPig pigToCheck = iterator.next();
            if (pigToCheck.getId() == deleteId) {
                iterator.remove();
            }
        }
        storePigs(pigs);
    }

    public List<GuineaPig> getPigs() throws IOException {
        File f = new File(path + fileName);
        List<GuineaPig> pigs = new ArrayList<>();
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
                Gson gson = new Gson();
                List<GuineaPig> pigToReturn = gson.fromJson(json, new TypeToken<List<GuineaPig>>() {
                }.getType());
                pigs.addAll(pigToReturn);
            }
        }
        return pigs;
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

    public void updatePig(GuineaPig pigUpdate) throws IOException {
        List<GuineaPig> pigs = getPigs();
        Iterator<GuineaPig> iterator = pigs.iterator();
        while (iterator.hasNext()) {
            GuineaPig pig = iterator.next();
            if (pig.getId() == pigUpdate.getId()) {
                iterator.remove();
                break;
            }
        }
        pigs.add(pigUpdate);
        storePigs(pigs);
    }
}