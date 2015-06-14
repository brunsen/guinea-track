package de.brunsen.guineatrack.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.Gender;
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

    private boolean fieldsEmpty() {
        boolean empty = true;
        empty &= nameEdit.getText().toString().equals("");
        empty &= birthEdit.getText().toString().equals("");
        empty &= colorEdit.getText().toString().equals("");
        empty &= raceEdit.getText().toString().equals("");
        if (selectedGender == Gender.Female) {
            empty &= lastBirthEdit.getText().toString().equals("");
        }
        return empty;
    }

    @Override
    protected void storeWithCrud(GuineaPig pig) {
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            crud.storePig(pig);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.save_error_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (!fieldsEmpty()) {
            showLeaveConfirmation(getString(R.string.message_unsaved_guinea_pig));
        } else {
            super.onBackPressed();
        }
    }
}
