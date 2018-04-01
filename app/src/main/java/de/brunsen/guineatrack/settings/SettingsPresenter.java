package de.brunsen.guineatrack.settings;

import de.brunsen.guineatrack.util.BasePresenter;

public interface SettingsPresenter extends BasePresenter {

    void onWeightChecked(boolean checked);

    void onOriginChecked(boolean checked);

    void onLimitationsChecked(boolean checked);

    void onEntryChecked(boolean checked);

    void onDepartureChecked(boolean checked);

    void onCastrationChecked(boolean checked);

    void onLastBirthChecked(boolean checked);

    void onDueDateChecked(boolean checked);
}
