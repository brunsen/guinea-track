package de.brunsen.guineatrack.backup;

import de.brunsen.guineatrack.util.BasePresenter;

public interface BackupRecoveryPresenter extends BasePresenter {

    void onImportButtonClicked();

    void onBackupButtonClicked();
}
