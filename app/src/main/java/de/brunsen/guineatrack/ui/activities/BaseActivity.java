package de.brunsen.guineatrack.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import de.brunsen.guineatrack.R;

public class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;

    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected boolean hasExternalReadAccess() {
        boolean readAccess = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            readAccess = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return readAccess;
    }

    protected void showError(String title, String message, String okMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okMessage, null);
        builder.show();
    }
}
