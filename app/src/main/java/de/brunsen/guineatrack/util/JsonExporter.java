package de.brunsen.guineatrack.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

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
            showNoGuineaPigsError();
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
        Toast.makeText(mContext.getApplicationContext(), R.string.successful_export, Toast.LENGTH_SHORT).show();
    }

    private void showNoGuineaPigsError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.error);
        builder.setMessage(R.string.error_no_guinea_pigs_to_export);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }
}
