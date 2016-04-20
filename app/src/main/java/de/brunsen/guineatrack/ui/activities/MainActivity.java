package de.brunsen.guineatrack.ui.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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
import de.brunsen.guineatrack.ui.dialogs.PermissionDialog;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainActivity extends BaseActivity implements OnClickListener,
        OnItemClickListener, OnItemLongClickListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int PERMISSION_REQUEST_LIST_PICTURES = 1;
    private static final int PERMISSION_REQUEST_IMPORT = 2;
    private boolean initialPermissionRequest;
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
        initialPermissionRequest = true;
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
                if (hasExternalReadAccess()) {
                    importGuineaPigs();
                } else {
                    askForExternalStorageReadAccess(PERMISSION_REQUEST_IMPORT);
                }
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
        listView.setOnItemLongClickListener(this);
        if (mGuineaPigs.isEmpty()) {
            handleEmptyList();
        } else {
            if (initialPermissionRequest) {
                initialPermissionRequest = false;
                askForExternalStorageReadAccess(PERMISSION_REQUEST_LIST_PICTURES);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LIST_PICTURES: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMainListAdapter.notifyDataSetChanged();
                }
                break;
            }
            case PERMISSION_REQUEST_IMPORT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    importGuineaPigs();
                }
                break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void askForExternalStorageReadAccess(final int request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    String rationaleMessage;
                    switch (request) {
                        case PERMISSION_REQUEST_IMPORT: {
                            rationaleMessage = getString(R.string.rationale_message_import);
                            break;
                        }
                        case PERMISSION_REQUEST_LIST_PICTURES: {
                            rationaleMessage = getString(R.string.rationale_message_list);
                            break;
                        } default:
                            rationaleMessage = "";
                    }
                    PermissionDialog dialog = new PermissionDialog(this, rationaleMessage, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
                        }
                    });
                    dialog.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
                }
            }
        }
    }

    private boolean hasExternalReadAccess() {
        boolean readAccess = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            readAccess = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return readAccess;
    }

    private void loadGuineaPigs() {
        List<GuineaPig> tempPigs = new ArrayList<>();
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            tempPigs.addAll(crud.getGuineaPigs());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_loading_pigs),
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
            Log.e(TAG, "No file found to import guinea pigs");
            handleNoFileError();
        } catch (JSONException e) {
            Log.e(TAG, "Corrupted import file");
            handleCorruptedFileError();
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

    private void deleteGuineaPig(GuineaPig guineaPig) {
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        crud.deleteGuineaPig(guineaPig);
        mGuineaPigs.remove(guineaPig);
        mMainListAdapter.notifyDataSetChanged();
    }

    private void handleNoFileError() {
        String title = getString(R.string.error);
        String message = getString(R.string.error_no_file);
        String okMessage = getString(android.R.string.ok);
        showError(title, message, okMessage);
    }

    private void handleCorruptedFileError() {
        String title = getString(R.string.error);
        String message = getString(R.string.error_corrupted_file);
        String okMessage = getString(android.R.string.ok);
        showError(title, message, okMessage);
    }

    private void handleEmptyList() {
        String title = getString(R.string.no_guinea_pigs_title);
        String message = getString(R.string.no_guinea_pigs_message);
        String okMessage = getString(android.R.string.ok);
        showError(title, message, okMessage);
    }

    private void showError(String title, String message, String okMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okMessage, null);
        builder.show();
    }

    public void showDeleteDialog(final GuineaPig guineaPig) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(getString(R.string.confirm));
        builder.setMessage(getString(R.string.deletion_confirmation_text, guineaPig.getName()));
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
                    GuineaPigAddActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showDeleteDialog(mGuineaPigs.get(position));
        return true;
    }
}
