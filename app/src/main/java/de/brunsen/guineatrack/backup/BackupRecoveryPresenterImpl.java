package de.brunsen.guineatrack.backup;

import android.content.Context;
import android.util.Log;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.ui.dialogs.PermissionDialog;
import de.brunsen.guineatrack.util.JsonExporter;
import de.brunsen.guineatrack.util.JsonImporter;
import io.reactivex.functions.Consumer;

public class BackupRecoveryPresenterImpl implements BackupRecoveryPresenter {

    private static String TAG = BackupRecoveryPresenterImpl.class.getName();

    private BackupRecoveryView mBackupRecoveryView;

    private Context mContext;

    public BackupRecoveryPresenterImpl(Context context) {
        mContext = context;
    }

    @Override
    public void setView(Object view) {
        mBackupRecoveryView = (BackupRecoveryView) view;
    }

    @Override
    public void clearView() {
        mContext = null;
        mBackupRecoveryView = null;
    }

    @Override
    public void onImportButtonClicked() {
        RxPermissions permissions = RxPermissions.getInstance(mContext);
        boolean hasExternalReadAccess = permissions.isGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasExternalReadAccess) {
            importGuineaPigs();
        } else {
            askForExternalStorageReadAccess();
        }
    }

    @Override
    public void onBackupButtonClicked() {
        exportGuineaPigs();
    }

    private void exportGuineaPigs() {
        JsonExporter exporter = new JsonExporter(mContext);
        GuineaPigCRUD crud = new GuineaPigCRUD(mContext);
        List<GuineaPig> guineaPigs = crud.getGuineaPigs();
        try {
            exporter.exportGuineaPigs(guineaPigs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }private void importGuineaPigs() {
        JsonImporter importer = new JsonImporter(mContext);
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
        String title = mContext.getString(R.string.error);
        String message = mContext.getString(R.string.error_no_file);
        String okMessage = mContext.getString(android.R.string.ok);
        mBackupRecoveryView.showErrorDialog(title, message, okMessage);
    }

    private void handleCorruptedFileError() {
        String title = mContext.getString(R.string.error);
        String message = mContext.getString(R.string.error_corrupted_file);
        String okMessage = mContext.getString(android.R.string.ok);
        mBackupRecoveryView.showErrorDialog(title, message, okMessage);
    }

    private void askForExternalStorageReadAccess() {
        RxPermissions permissions = RxPermissions.getInstance(mContext);
        permissions.requestEach(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            importGuineaPigs();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            String rationaleMessage = mContext.getString(R.string.rationale_message_import);
                            PermissionDialog dialog = new PermissionDialog(mContext, rationaleMessage, null);
                            dialog.show();
                        } else {
                            // TODO: Inform user about the grave mistake...
                        }
                    }});
    }
}
