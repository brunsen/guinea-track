package de.brunsen.guineatrack.backup;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.ui.activities.BaseActivity;
import de.brunsen.guineatrack.ui.dialogs.PermissionDialog;
import de.brunsen.guineatrack.util.JsonExporter;
import de.brunsen.guineatrack.util.JsonImporter;
import io.reactivex.functions.Consumer;

public class BackupRecoveryActivity extends BaseActivity implements BackupRecoveryView {

    private static String TAG = BackupRecoveryActivity.class.getName();

    @BindView(R.id.backup_button)
    protected Button mBackupButton;
    @BindView(R.id.import_button)
    protected Button mRecoveryButton;

    private BackupRecoveryPresenter mRecoveryPresenter;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_recovery);
        initToolbar();
        mUnbinder = ButterKnife.bind(this);
        mToolbar.setTitle(R.string.title_activity_backup_recovery);
        mRecoveryPresenter = new
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.backup_button)
    protected void onBackupButtonClicked() {
        exportGuineaPigs();
    }

    @OnClick(R.id.import_button)
    protected void onImportButtonClicked() {
        RxPermissions permissions = RxPermissions.getInstance(BackupRecoveryActivity.this);
        boolean hasExternalReadAccess = permissions.isGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasExternalReadAccess) {
            importGuineaPigs();
        } else {
            askForExternalStorageReadAccess();
        }
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

    private void askForExternalStorageReadAccess() {
        RxPermissions permissions = RxPermissions.getInstance(this);
        permissions.requestEach(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            importGuineaPigs();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            String rationaleMessage = getString(R.string.rationale_message_import);
                            PermissionDialog dialog = new PermissionDialog(BackupRecoveryActivity.this, rationaleMessage, null);
                            dialog.show();
                        } else {
                            // TODO: Inform user about the grave mistake...
                        }
                    }});
    }

    @Override
    public void showErrorDialog(String title, String message, String okMessage) {
        showError(title, message, okMessage);
    }
}
