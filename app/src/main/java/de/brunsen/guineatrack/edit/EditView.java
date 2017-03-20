package de.brunsen.guineatrack.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import de.brunsen.guineatrack.model.Gender;
import de.brunsen.guineatrack.model.Type;

public interface EditView {

    void setTitle(String title);

    void setPicture(Bitmap bitmap);

    void setPicture(Drawable drawable);

    void showCastrationDateArea(boolean show);

    void showDueDateArea(boolean show);

    void showLastBirthArea(boolean show);

    int getImageViewWidth();

    void setNameText(String text);

    void setBirthText(String text);

    void setColorText(String text);

    void setSpinnerGender(Gender gender);

    void setBreedText(String text);

    void setSpinnerType(Type type);

    void setWeightText(String text);

    void setLastBirthText(String text);

    void setDueDateText(String text);

    void setOriginText(String text);

    void setLimitationsText(String text);

    void setCastrationDateText(String text);

    void showLeaveConfirmation(String message);

    void finish();

    void startActivityForResult(Intent intent, int requestCode);
}
