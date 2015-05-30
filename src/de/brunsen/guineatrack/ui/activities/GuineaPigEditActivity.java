package de.brunsen.guineatrack.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.services.GuineaPigCRUD;

public class GuineaPigEditActivity extends AbstractPigActivity {
    GuineaPig pig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pig = getIntent().getParcelableExtra("pig");
        setData();
    }

    @Override
    protected void storeWithCrud(GuineaPig updatedPig) {
        updatedPig.setId(pig.getId());
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            crud.updatePig(updatedPig);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(this,
                    getString(R.string.update_error_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void setData() {
        nameEdit.setText(pig.getName());
        birthEdit.setText(pig.getBirth());
        colorEdit.setText(pig.getColor());
        genderSpinner.setSelection(pig.getGender().getPosition());
        typeSpinner.setSelection(pig.getType().getPosition());
        raceEdit.setText(pig.getRace());
        selectedImage = pig.getPicturePath();
        setImage(selectedImage);
        if (pig.getGender() == Gender.Female) {
            setLastBirthAreaVisible(true);
            String lastBirth = "";
            if (pig.getLastBirth() != null) {
                lastBirth = pig.getLastBirth();
            }
            lastBirthEdit.setText(lastBirth);
        }
    }

    @Override
    public void onBackPressed() {
        showLeaveConfirmation(getString(R.string.message_unsaved_update));
    }
}
