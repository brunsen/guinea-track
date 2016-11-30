package de.brunsen.guineatrack.overview;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.ui.activities.BackupRecoveryActivity;
import de.brunsen.guineatrack.ui.activities.BaseActivity;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class OverviewActivity extends BaseActivity implements OverViewView{

    private static final String TAG = OverviewActivity.class.getName();

    @BindView(R.id.guinea_pig_list)
    StickyListHeadersListView listView;

    private OverviewListAdapter mOverviewListAdapter;

    private OverViewPresenter presenter;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        presenter = new OverViewPresenterImpl(this);
        presenter.setView(this);
        initListAdapter();
        initToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        mOverviewListAdapter.clear();
        presenter.clearView();
        presenter = null;
        mUnbinder.unbind();
        super.onDestroy();
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

    private void initListAdapter() {
        mOverviewListAdapter = new OverviewListAdapter(this, presenter);
        listView.setAdapter(mOverviewListAdapter);
        // currently butter knife does not work well with on click methods for the sticky list header lib...
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onItemClick(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onItemLongClick(i);
                return true;
            }
        });
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

    @OnClick(R.id.add_button)
    protected void onAddButtonClicked() {
        presenter.onAddButtonClicked();
    }

    @Override
    public void refreshList() {
        mOverviewListAdapter.notifyDataSetChanged();
    }
}
