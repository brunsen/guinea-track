package de.brunsen.guineatrack.backup;

import android.content.Context;

public class BackupRecoveryPresenterImpl implements BackupRecoveryPresenter {

    private BackupRecoveryView mBackupRecoveryView;

    private Context mContext;

    @Override
    public void setView(Object view) {
        mBackupRecoveryView = (BackupRecoveryView) view;
    }

    @Override
    public void clearView() {
        mBackupRecoveryView = null;
    }

    @Override
    public void onImportButtonClicked() {

    }

    @Override
    public void onBackupButtonClicked() {

    }
}
