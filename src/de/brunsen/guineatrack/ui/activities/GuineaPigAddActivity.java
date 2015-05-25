package de.brunsen.guineatrack.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.services.GuineaPigCRUD;
import de.brunsen.guineatrack.services.ImageService;

public class GuineaPigAddActivity extends AbstractPigActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    @Override
    protected void setData() {
        ImageService.getInstance().setDefaultImage(editImage);
    }

    @Override
    protected void storeWithCrud(GuineaPig pig) {
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            crud.storePig(pig);
            finish();
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.save_error_message),
                    Toast.LENGTH_LONG).show();
        }
    }
}
