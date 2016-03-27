package de.brunsen.guineatrack.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigComparator;
import de.brunsen.guineatrack.services.JsonExporter;
import de.brunsen.guineatrack.services.JsonImporter;
import de.brunsen.guineatrack.ui.adapter.MainListAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainActivity extends BaseActivity implements OnClickListener,
        OnItemClickListener {

    private List<GuineaPig> mGuineaPigs;
    private StickyListHeadersListView listView;
    private MainListAdapter mMainListAdapter;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        initToolbar();
        addButton.setOnClickListener(this);
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
            case R.id.option_import:
                importGuineaPigs();
                return true;
            case R.id.option_export:
                exportGuineaPigs();
                return true;
            default:
                return true;
        }
    }

    private void initComponents() {
        listView = (StickyListHeadersListView) findViewById(R.id.guinea_pig_list);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGuineaPigs();
        mMainListAdapter = new MainListAdapter(this, mGuineaPigs);
        setGenderText();
        listView.setAdapter(mMainListAdapter);
        listView.setOnItemClickListener(this);
        if (mGuineaPigs.isEmpty()) {
            handleEmptyList();
        }
    }

    private void loadGuineaPigs() {
        List<GuineaPig> tempPigs = new ArrayList<>();
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            tempPigs.addAll(crud.getGuineaPigs());
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_loading_pigs),
                    Toast.LENGTH_LONG).show();
            tempPigs = new ArrayList<>();
            e.printStackTrace();
        }
        Collections.sort(tempPigs, new GuineaPigComparator());
        mGuineaPigs = tempPigs;
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
        getToolbar().setSubtitle(getString(R.string.main_activity_subtitle, totalMale, totalFemale, totalCastrato));
    }

    private void importGuineaPigs() {
        JsonImporter importer = new JsonImporter(this);
        try {
            importer.loadGuineaPigs();
            loadGuineaPigs();
            setGenderText();
            mMainListAdapter.setGuineaPigs(mGuineaPigs);
            mMainListAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            // TODO: show dialog with helpful text: file not found
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO: show dialog with helpful text: json file corrupt
            e.printStackTrace();
        }
    }

    private void exportGuineaPigs() {
        JsonExporter exporter = new JsonExporter(this);
        try {
            exporter.exportGuineaPigs(mGuineaPigs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEmptyList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(getString(R.string.no_guinea_pigs_title));
        builder.setMessage(getString(R.string.no_guinea_pigs_message));
        builder.setPositiveButton(getString(android.R.string.ok), null);
        builder.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        GuineaPig guineaPig = mGuineaPigs.get(position);
        if (guineaPig != null) {
            Intent intent = new Intent(getApplicationContext(),
                    GuineaPigDetailActivity.class);
            intent.putExtra(getString(R.string.pig_identifier), guineaPig.getId());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_button) {
            Intent intent = new Intent(getApplicationContext(),
                    GuineaPigAddEditActivity.class);
            startActivity(intent);
        }
    }
}
