package de.brunsen.guineatrack.ui.activities;

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
}
