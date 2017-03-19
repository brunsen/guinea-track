package de.brunsen.guineatrack.backup;

import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.ui.activities.BaseActivity;

public class BackupRecoveryActivity extends BaseActivity implements BackupRecoveryView {

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
        mRecoveryPresenter = new BackupRecoveryPresenterImpl(this);
        mRecoveryPresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        mRecoveryPresenter.clearView();
        mRecoveryPresenter = null;
        mUnbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.backup_button)
    protected void onBackupButtonClicked() {
        mRecoveryPresenter.onBackupButtonClicked();
    }

    @OnClick(R.id.import_button)
    protected void onImportButtonClicked() {
        mRecoveryPresenter.onImportButtonClicked();
    }

    @Override
    public void showErrorDialog(String title, String message, String okMessage) {
        showError(title, message, okMessage);
    }
}
