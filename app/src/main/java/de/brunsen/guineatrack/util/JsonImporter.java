package de.brunsen.guineatrack.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.model.GuineaPig;

public class JsonImporter {
    private String path;
    private String fileName;
    private Context mContext;

    public JsonImporter(Context c) {
        path = Environment.getExternalStoragePublicDirectory("").getAbsolutePath() + c.getString(R.string.folder_path);
        fileName = c.getString(R.string.storage_file_name);
        mContext = c;
    }

    public void loadGuineaPigs() throws IOException, JSONException {
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
        } else {
            throw new FileNotFoundException();
        }
        GuineaPigCRUD crud = new GuineaPigCRUD(mContext);
        crud.storeGuineaPigs(guineaPigs);
        Toast.makeText(mContext.getApplicationContext(), R.string.successful_import, Toast.LENGTH_SHORT).show();
    }
}
