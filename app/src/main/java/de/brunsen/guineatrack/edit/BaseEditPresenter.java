package de.brunsen.guineatrack.edit;

import android.content.Intent;

import java.util.List;

import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.Type;
import de.brunsen.guineatrack.util.BasePresenter;

public interface BaseEditPresenter extends BasePresenter {

    void onOptionsItemSelected(int id);

    void onActivityResult(int requestCode, Intent data);

    void onAddPictureButtonClicked();

    void onTypeItemSelected(int position);

    void onGenderItemSelected(int position);

    List<Gender> getGenderList();

    List<Type> getTypeList();

    void onBackPressed();

    void updateName(String name);

    void updateWeight(String weight);

    void updateBirthDate(String date);

    void updateColor(String color);

    void updateBreed(String breed);

    void updateOrigin(String origin);

    void updateLimitations(String limitations);

    void updateCastrationDate(String date);

    void updateLastBirthDate(String date);

    void updateDueDate(String date);

}
