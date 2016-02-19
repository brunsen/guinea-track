package de.brunsen.guineatrack.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.database.GuineaPigCRUD;
import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.GuineaPig;
import de.brunsen.guineatrack.model.GuineaPigOptionalData;
import de.brunsen.guineatrack.model.Type;

public class GuineaPigEditActivity extends AbstractPigActivity {
    GuineaPig mGuineaPig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            mGuineaPig = crud.getGuineaPigForId(getIntent().getIntExtra(getString(R.string.pig_identifier), 0));
            setData();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void storeWithCrud(GuineaPig updatedPig) {
        updatedPig.setId(mGuineaPig.getId());
        GuineaPigCRUD crud = new GuineaPigCRUD(this);
        try {
            crud.updateGuineaPig(updatedPig);
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
        nameEdit.setText(mGuineaPig.getName());
        birthEdit.setText(mGuineaPig.getBirth());
        weightEdit.setText("" + mGuineaPig.getOptionalData().getWeight());
        colorEdit.setText(mGuineaPig.getColor());
        genderSpinner.setSelection(mGuineaPig.getGender().getPosition());
        typeSpinner.setSelection(mGuineaPig.getType().getPosition());
        breedEdit.setText(mGuineaPig.getBreed());
        selectedImage = mGuineaPig.getOptionalData().getPicturePath();
        setImage(selectedImage);
        selectedGender = mGuineaPig.getGender();
        selectedType = mGuineaPig.getType();
        toggleCastrationDateArea();
        if (mGuineaPig.getGender() == Gender.Female) {
            setLastBirthAreaVisible(true);
            lastBirthEdit.setText(mGuineaPig.getOptionalData().getLastBirth());
        }
        originEdit.setText(mGuineaPig.getOptionalData().getOrigin());
        castrationDateEdit.setText(mGuineaPig.getOptionalData().getCastrationDate());
        limitationsEdit.setText(mGuineaPig.getOptionalData().getLimitations());
    }

    private boolean newData() {
        boolean noUpdate = true;
        GuineaPigOptionalData optionalData = mGuineaPig.getOptionalData();
        noUpdate &= nameEdit.getText().toString().equals(mGuineaPig.getName());
        noUpdate &= birthEdit.getText().toString().equals(mGuineaPig.getBirth());
        noUpdate &= colorEdit.getText().toString().equals(mGuineaPig.getColor());
        noUpdate &= breedEdit.getText().toString().equals(mGuineaPig.getBreed());
        noUpdate &= mGuineaPig.getType().equals(selectedType);
        noUpdate &= mGuineaPig.getGender().equals(selectedGender);
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
