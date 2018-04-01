package de.brunsen.guineatrack.overview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.backup.BackupRecoveryActivity;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.detail.GuineaPigDetailActivity;
import de.brunsen.guineatrack.edit.GuineaPigEditActivity;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigComparator;
import de.brunsen.guineatrack.settings.SettingsActivity;
import de.brunsen.guineatrack.ui.dialogs.PermissionDialog;
import io.reactivex.functions.Consumer;

public class OverViewPresenterImpl implements OverViewPresenter {

    private Context mContext;
    private OverViewView mView;
    private List<GuineaPig> mGuineaPigs;
    private boolean initialPermissionRequest;

    public OverViewPresenterImpl(Context context) {
        mContext = context;
        mGuineaPigs = new ArrayList<>();
        initialPermissionRequest = true;
    }

    @Override
    public void onResume() {
        loadGuineaPigs();
        mView.refreshList();
        setGenderText();

        if (mGuineaPigs.isEmpty()) {
            handleEmptyList();
        } else {
            RxPermissions permissions = RxPermissions.getInstance(mContext);
            boolean hasExternalReadAccess = permissions.isGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (initialPermissionRequest && !hasExternalReadAccess) {
                initialPermissionRequest = false;
                askForExternalStorageReadAccess();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(int itemId) {
        // Handle item selection
        switch (itemId) {
            case R.id.option_backup_recovery:
                startBackupActivity();
                return true;
            case R.id.option_settings:
                startSettingsActivity();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onItemClick(int position) {
        GuineaPig guineaPig = mGuineaPigs.get(position);
        if (guineaPig != null) {
            Intent intent = new Intent(mContext,
                    GuineaPigDetailActivity.class);
            intent.putExtra(mContext.getString(R.string.guinea_pig_identifier), guineaPig.getId());
            mContext.startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        showDeleteDialog(mGuineaPigs.get(position));
    }

    @Override
    public void onAddButtonClicked() {
        Intent intent = new Intent(mContext, GuineaPigEditActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mGuineaPigs.size();
    }

    @Override
    public GuineaPig getGuineaPig(int position) {
        return mGuineaPigs.get(position);
    }

    @Override
    public void setView(Object view) {
        mView = (OverViewView) view;
    }

    @Override
    public void clearView() {
        mView = null;
        mGuineaPigs.clear();
        mGuineaPigs = null;
    }

    private void loadGuineaPigs() {
        mGuineaPigs.clear();
        GuineaPigCRUD crud = new GuineaPigCRUD(mContext);
        try {
            mGuineaPigs.addAll(crud.getGuineaPigs());
        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.error_loading_pigs),
                    Toast.LENGTH_LONG).show();
            mGuineaPigs.clear();
            e.printStackTrace();
        }
        Collections.sort(mGuineaPigs, new GuineaPigComparator());
    }

    private void setGenderText() {
        int totalMale = 0;
        int totalFemale = 0;
        int totalCastrato = 0;
        int total = mGuineaPigs.size();

        for (int i = 0; i < total; i++) {
            GuineaPig pig = mGuineaPigs.get(i);
            switch (pig.getGender()) {
                case Male:
                    totalMale++;
                    break;
                case Female:
                    totalFemale++;
                    break;
                case CASTRATO:
                    totalCastrato++;
                    break;

                default:
                    break;
            }
        }
        String text = mContext.getString(R.string.main_activity_subtitle, totalMale, totalFemale, totalCastrato);
        mView.setGenderText(text);
    }

    private void deleteGuineaPig(GuineaPig guineaPig) {
        GuineaPigCRUD crud = new GuineaPigCRUD(mContext);
        crud.deleteGuineaPig(guineaPig);
        mGuineaPigs.remove(guineaPig);
        mView.refreshList();
    }

    private void handleEmptyList() {
        String title = mContext.getString(R.string.no_guinea_pigs_title);
        String message = mContext.getString(R.string.no_guinea_pigs_message);
        String okMessage = mContext.getString(android.R.string.ok);
        mView.showErrorDialog(title, message, okMessage);
    }

    private void showDeleteDialog(final GuineaPig guineaPig) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
        builder.setTitle(mContext.getString(R.string.confirm));
        builder.setMessage(mContext.getString(R.string.deletion_confirmation_text, guineaPig.getName()));
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteGuineaPig(guineaPig);
                setGenderText();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void askForExternalStorageReadAccess() {
        RxPermissions permissions = RxPermissions.getInstance(mContext);
        permissions.requestEach(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            mView.refreshList();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            String rationaleMessage = mContext.getString(R.string.rationale_message_list);
                            PermissionDialog dialog = new PermissionDialog(mContext, rationaleMessage, null);
                            dialog.show();
                        }
                    }
                });
    }

    private void startBackupActivity() {
        Intent intent = new Intent(mContext, BackupRecoveryActivity.class);
        mContext.startActivity(intent);
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(mContext, SettingsActivity.class);
        mContext.startActivity(intent);
    }
}
