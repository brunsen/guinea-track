package de.brunsen.guineatrack.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.services.GuineaPigCRUD;
import de.brunsen.guineatrack.ui.adapter.MainListAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainActivity extends BaseActivity implements OnClickListener,
        OnItemClickListener {

    private List<GuineaPig> pigs;
    private StickyListHeadersListView listView;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        initToolbar();
        addButton.setOnClickListener(this);
    }

    private void initComponents() {
        listView = (StickyListHeadersListView) findViewById(R.id.guinea_pig_list);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<GuineaPig> tempPigs = new ArrayList<>();
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            tempPigs.addAll(crud.getPigs());
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_loading_pigs),
                    Toast.LENGTH_LONG).show();
            tempPigs = new ArrayList<>();
            e.printStackTrace();
        }
        Collections.sort(tempPigs);
        pigs = tempPigs;
        MainListAdapter adapter = new MainListAdapter(this, pigs);
        setGenderText();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void setGenderText() {
        int totalMale = 0;
        int totalFemale = 0;
        int totalCastrato = 0;
        int total = pigs.size();

        for (int i = 0; i < total; i++) {
            GuineaPig pig = pigs.get(i);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        GuineaPig pig = pigs.get(position);
        if (pig != null) {
            Intent intent = new Intent(getApplicationContext(),
                    GuineaPigDetailActivity.class);
            intent.putExtra(getString(R.string.pig_identifier), pig);
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
}
