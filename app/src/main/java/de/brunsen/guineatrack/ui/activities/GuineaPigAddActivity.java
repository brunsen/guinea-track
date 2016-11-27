package de.brunsen.guineatrack.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.util.ImageService;

public class GuineaPigAddActivity extends BaseGuineaPigEditActivity {

    private String mDefaultDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    @Override
    protected void setData() {
        DateFormat dateFormat = DateFormat.getDateInstance();
        mDefaultDateText = dateFormat.format(new Date());
        birthEdit.setText(mDefaultDateText);
        ImageService.getInstance().setDefaultImage(editImage);
    }

    private boolean fieldsEmpty() {
        boolean empty = true;
        empty &= nameEdit.getText().toString().equals("");
        String currentDateText = birthEdit.getText().toString();
        empty &= currentDateText.equals(mDefaultDateText) || currentDateText.equals("");
        empty &= colorEdit.getText().toString().equals("");
        empty &= breedEdit.getText().toString().equals("");
        if (selectedGender == Gender.Female) {
            empty &= lastBirthEdit.getText().toString().equals("");
            empty &= dueDateEdit.getText().toString().equals("");
        }
        if (selectedGender != null && !selectedGender.equals(Gender.Male)) {
            if (selectedType != null && !selectedType.equals(Type.BREED)) {
                empty &= castrationDateEdit.getText().toString().equals("");
            }
        }
        empty &= weightEdit.getText().toString().equals("");
        empty &= limitationsEdit.getText().toString().equals("");
        empty &= originEdit.getText().toString().equals("");
        return empty;
    }

    @Override
    protected void storeWithCrud(GuineaPig pig) {
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            crud.storeGuineaPig(pig);
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.save_error_message),
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
