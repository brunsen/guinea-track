package de.brunsen.guineatrack.overview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.ui.activities.BackupRecoveryActivity;
import de.brunsen.guineatrack.ui.activities.BaseActivity;
import de.brunsen.guineatrack.ui.activities.GuineaPigAddActivity;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class OverviewActivity extends BaseActivity implements OverViewView,
        OnItemClickListener, OnItemLongClickListener {

    private static final String TAG = OverviewActivity.class.getName();
    private OverViewPresenter presenter;
    private StickyListHeadersListView listView;
    private OverviewListAdapter mOverviewListAdapter;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new OverViewPresenterImpl(this);
        presenter.setView(this);
        initComponents();
        initToolbar();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.option_backup_recovery:
                startBackupActivity();
            default:
                return true;
        }
    }

    private void initComponents() {
        listView = (StickyListHeadersListView) findViewById(R.id.guinea_pig_list);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        mOverviewListAdapter = new OverviewListAdapter(this, presenter);
        listView.setAdapter(mOverviewListAdapter);
    }

    private void setListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GuineaPigAddActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void setGenderText(String text) {
        getToolbar().setSubtitle(text);
    }

    @Override
    public void showErrorDialog(String title, String message, String okMessage) {
        showError(title, message, okMessage);
    }

    private void startBackupActivity() {
        Intent intent = new Intent(this, BackupRecoveryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        presenter.onItemClick(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemLongClick(position);
        return true;
    }

    @Override
    public void refreshList() {
        mOverviewListAdapter.notifyDataSetChanged();
    }
}
