package de.brunsen.guineatrack.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.services.GuineaPigCRUD;
import de.brunsen.guineatrack.services.JsonReader;

public class GuineaPigEditActivity extends AbstractPigActivity {
    GuineaPig pig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JsonReader reader = new JsonReader(this);
        try {
            pig = reader.getGuineaPigFromString(getIntent().getStringExtra(getString(R.string.pig_identifier)));
            setData();
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }
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
        } catch (Exception e) {
            Toast.makeText(this,
                    getString(R.string.update_error_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void setData() {
        nameEdit.setText(pig.getName());
        birthEdit.setText(pig.getBirth());
        weightEdit.setText("" + pig.getOptionalData().getWeight());
        colorEdit.setText(pig.getColor());
        genderSpinner.setSelection(pig.getGender().getPosition());
        typeSpinner.setSelection(pig.getType().getPosition());
        breedEdit.setText(pig.getBreed());
        selectedImage = pig.getOptionalData().getPicturePath();
        setImage(selectedImage);
        selectedGender = pig.getGender();
        selectedType = pig.getType();
        toggleCastrationDateArea();
        if (pig.getGender() == Gender.Female) {
            setLastBirthAreaVisible(true);
            lastBirthEdit.setText(pig.getOptionalData().getLastBirth());
        }
        originEdit.setText(pig.getOptionalData().getOrigin());
        castrationDateEdit.setText(pig.getOptionalData().getCastrationDate());
        limitationsEdit.setText(pig.getOptionalData().getLimitations());
    }

    private boolean newData() {
        boolean noUpdate = true;
        GuineaPigOptionalData optionalData = pig.getOptionalData();
        noUpdate &= nameEdit.getText().toString().equals(pig.getName());
        noUpdate &= birthEdit.getText().toString().equals(pig.getBirth());
        noUpdate &= colorEdit.getText().toString().equals(pig.getColor());
        noUpdate &= breedEdit.getText().toString().equals(pig.getBreed());
        noUpdate &= pig.getType().equals(selectedType);
        noUpdate &= pig.getGender().equals(selectedGender);
        noUpdate &= optionalData.getPicturePath().equals(selectedImage);
        if (selectedGender != null && selectedGender == Gender.Female) {
            noUpdate &= lastBirthEdit.getText().toString().equals(optionalData.getLastBirth());
        }
        if (selectedGender != null && !selectedGender.equals(Gender.Male)) {
            if (selectedType != null && !selectedType.equals(Type.BREED)) {
                noUpdate &= castrationDateEdit.getText().toString().equals(optionalData.getCastrationDate());
            }
        }
        noUpdate &= optionalData.getLimitations().equals(limitationsEdit.getText().toString());
        noUpdate &= optionalData.getOrigin().equals(originEdit.getText().toString());
        if (weightEdit.getText().equals("")) {
            noUpdate = true;
        }
        noUpdate &= optionalData.getWeight() == Integer.valueOf(weightEdit.getText().toString());
        return !noUpdate;
    }

    @Override
    public void onBackPressed() {
        if (newData()) {
            showLeaveConfirmation(getString(R.string.message_unsaved_update));
        } else {
            super.onBackPressed();
        }
    }
}
