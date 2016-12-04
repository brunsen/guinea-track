package de.brunsen.guineatrack.ui.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.ui.dialogs.PermissionDialog;
import de.brunsen.guineatrack.util.JsonExporter;
import de.brunsen.guineatrack.util.JsonImporter;

public class BackupRecoveryActivity extends BaseActivity {

    private static String TAG = BackupRecoveryActivity.class.getName();

    private Button mBackupButton;
    private Button mRecoveryButton;

    private static final int PERMISSION_REQUEST_IMPORT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_recovery);
        initToolbar();
        mToolbar.setTitle(R.string.title_activity_backup_recovery);
        initComponents();
        setListeners();
    }

    private void setListeners() {
        mBackupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportGuineaPigs();
            }
        });
        mRecoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasExternalReadAccess()) {
                    importGuineaPigs();
                } else {
                    askForExternalStorageReadAccess();
                }
            }
        });
    }

    private void initComponents() {
        mBackupButton = (Button) findViewById(R.id.backup_button);
        mRecoveryButton = (Button) findViewById(R.id.restore_button);
    }

    private void exportGuineaPigs() {
        JsonExporter exporter = new JsonExporter(this);
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        List<GuineaPig> guineaPigs = crud.getGuineaPigs();
        try {
            exporter.exportGuineaPigs(guineaPigs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importGuineaPigs() {
        JsonImporter importer = new JsonImporter(this);
        try {
            importer.loadGuineaPigs();
        } catch (IOException e) {
            Log.e(TAG, "No file found to import guinea pigs");
            handleNoFileError();
        } catch (JSONException e) {
            Log.e(TAG, "Corrupted import file");
            handleCorruptedFileError();
        }
    }

    private void handleNoFileError() {
        String title = getString(R.string.error);
        String message = getString(R.string.error_no_file);
        String okMessage = getString(android.R.string.ok);
        showError(title, message, okMessage);
    }

    private void handleCorruptedFileError() {
        String title = getString(R.string.error);
        String message = getString(R.string.error_corrupted_file);
        String okMessage = getString(android.R.string.ok);
        showError(title, message, okMessage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_IMPORT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    importGuineaPigs();
                }
                break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void askForExternalStorageReadAccess() {
        final int request = PERMISSION_REQUEST_IMPORT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String rationaleMessage = getString(R.string.rationale_message_import);PermissionDialog dialog = new PermissionDialog(this, rationaleMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(BackupRecoveryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
                    }
                });
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
            }
        }
    }
}
