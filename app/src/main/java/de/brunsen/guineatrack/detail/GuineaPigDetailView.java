package de.brunsen.guineatrack.detail;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface GuineaPigDetailView {

    void finish();

    void setPicture(Bitmap bitmap);

    void setPicture(Drawable drawable);

    void showCastrationDateArea();

    void showDueDateArea();

    void showLastBirthArea();

    int getImageViewWidth();

    int getImageViewHeight();

    void setNameText(String text);

    void setBirthText(String text);

    void setColorText(String text);

    void setGenderText(String text);

    void setBreedText(String text);

    void setTypeText(String text);

    void setWeightText(String text);

    void setLastBirthText(String text);

    void setDueDateText(String text);

    void setOriginText(String text);

    void setLimitationsText(String text);

    void setCastrationDateText(String text);
}
