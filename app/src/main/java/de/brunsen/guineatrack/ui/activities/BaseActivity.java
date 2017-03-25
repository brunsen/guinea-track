package de.brunsen.guineatrack.ui.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import de.brunsen.guineatrack.R;

public abstract class BaseActivity extends AppCompatActivity {
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

    protected void showError(String title, String message, String okMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okMessage, null);
        builder.show();
    }
}
