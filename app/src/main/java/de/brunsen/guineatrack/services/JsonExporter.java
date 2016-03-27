package de.brunsen.guineatrack.services;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;

public class JsonExporter {
    private String path;
    private String fileName;
    private Context mContext;

    public JsonExporter(Context c) {
        path = Environment.getExternalStoragePublicDirectory("").getAbsolutePath() + c.getString(R.string.folder_path);
        fileName = c.getString(R.string.storage_file_name);
        mContext = c;
    }

    public void exportGuineaPigs(List<GuineaPig> guineaPigs) throws IOException, JSONException {
        if (!guineaPigs.isEmpty()) {
            JsonWriter writer = new JsonWriter(mContext);
            String json = writer.createJsonArrayFromList(guineaPigs).toString();
            storeJson(json);
        } else {
            // TODO: Show dialog with explanation why nothing can be exported
        }
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
        MediaScannerConnection.scanFile(mContext, new String[]{f.getAbsolutePath()}, null, null);
    }
}
