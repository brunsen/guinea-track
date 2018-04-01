package de.brunsen.guineatrack.settings;

import android.content.Context;

import de.brunsen.guineatrack.util.Settings;

public class SettingsPresenterImpl implements SettingsPresenter {

    private Context mContext;
    private SettingsView mView;

    SettingsPresenterImpl(Context context) {
        mContext = context;
    }

    @Override
    public void setView(Object view) {
        mView = (SettingsView) view;
        setData();
    }

    @Override
    public void clearView() {
        mView = null;
    }

    @Override
    public void onWeightChecked(boolean checked) {
        Settings.getSettings(mContext).setDisplayWeightField(checked);
    }

    @Override
    public void onOriginChecked(boolean checked) {
        Settings.getSettings(mContext).setDisplayOriginField(checked);
    }

    @Override
    public void onLimitationsChecked(boolean checked) {
        Settings.getSettings(mContext).setDisplayLimitationsField(checked);
    }

    @Override
    public void onEntryChecked(boolean checked) {
        Settings.getSettings(mContext).setDisplayEntryField(checked);
    }

    @Override
    public void onDepartureChecked(boolean checked) {
        Settings.getSettings(mContext).setDisplayDepartureField(checked);
    }

    @Override
    public void onCastrationChecked(boolean checked) {
        Settings.getSettings(mContext).setDisplayCastrationField(checked);
    }

    @Override
    public void onLastBirthChecked(boolean checked) {
        Settings.getSettings(mContext).setDisplayLastBirthField(checked);
    }

    @Override
    public void onDueDateChecked(boolean checked) {
        Settings.getSettings(mContext).setDisplayDueDateField(checked);
    }

    private void setData() {
        Settings settings = Settings.getSettings(mContext);
        mView.setWeightChecked(settings.displayWeightField());
        mView.setOriginChecked(settings.displayOriginField());
        mView.setLimitationsChecked(settings.displayLimitationsField());
        mView.setEntryChecked(settings.displayEntryField());
        mView.setDepartureChecked(settings.displayDepartureField());
        mView.setCastrationChecked(settings.displayCastrationField());
        mView.setLastBirthChecked(settings.displayLastBirthField());
        mView.setDueDateChecked(settings.displayDueDateField());
    }
}
