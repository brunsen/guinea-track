package de.brunsen.guineatrack.detail;

import java.io.File;

public interface GuineaPigDetailView {

    void finish();

    void setPicture(File pictureFile);

    void setPicture(int resId);

    void showCastrationDateArea();

    void showDueDateArea();

    void showLastBirthArea();

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

    void setEntryText(String text);

    void setDepartureText(String text);

    void setCastrationDateText(String text);
}
