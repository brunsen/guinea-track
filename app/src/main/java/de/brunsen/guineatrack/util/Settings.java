package de.brunsen.guineatrack.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static final String PREFERENCES_FILE_KEY = "de.brunsen.guineatrack.preferences";
    private static final String KEY_DISPLAY_WEIGHT_FIELD = "displayWeightField";
    private static final String KEY_DISPLAY_ORIGIN_FIELD = "displayOriginField";
    private static final String KEY_DISPLAY_LIMITATIONS_FIELD= "displayLimitationsField";
    private static final String KEY_DISPLAY_ENTRY_FIELD = "displayEntryField";
    private static final String KEY_DISPLAY_DEPARTURE_FIELD = "displayDepartureField";
    private static final String KEY_DISPLAY_CASTRATION_FIELD = "displayCastrationField";
    private static final String KEY_DISPLAY_LAST_BIRTH_FIELD = "displayLastBirthField";
    private static final String KEY_DISPLAY_DUE_DATE_FIELD = "displayDueDateField";

    private Context mContext;

    private Settings(Context context) {
        mContext = context;
    }

    public static Settings getSettings(Context context) {
        return new Settings(context);
    }

    public boolean displayWeightField() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DISPLAY_WEIGHT_FIELD, true);
    }

    public void setDisplayWeightField(boolean displayWeightField) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_DISPLAY_WEIGHT_FIELD, displayWeightField).apply();
    }

    public boolean displayOriginField() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DISPLAY_ORIGIN_FIELD, true);
    }

    public void setDisplayOriginField(boolean displayOriginField) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_DISPLAY_ORIGIN_FIELD, displayOriginField).apply();
    }

    public boolean displayLimitationsField() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DISPLAY_LIMITATIONS_FIELD, true);
    }

    public void setDisplayLimitationsField(boolean displayLimitationsField) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_DISPLAY_LIMITATIONS_FIELD, displayLimitationsField).apply();
    }

    public boolean displayEntryField() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DISPLAY_ENTRY_FIELD, true);
    }

    public void setDisplayEntryField(boolean displayEntryField) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_DISPLAY_ENTRY_FIELD, displayEntryField).apply();
    }

    public boolean displayDepartureField() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DISPLAY_DEPARTURE_FIELD, true);
    }

    public void setDisplayDepartureField(boolean displayDepartureField) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_DISPLAY_DEPARTURE_FIELD, displayDepartureField).apply();
    }

    public boolean displayCastrationField() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DISPLAY_CASTRATION_FIELD, true);
    }

    public void setDisplayCastrationField(boolean displayCastrationField) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_DISPLAY_CASTRATION_FIELD, displayCastrationField).apply();
    }

    public boolean displayLastBirthField() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DISPLAY_LAST_BIRTH_FIELD, true);
    }

    public void setDisplayLastBirthField(boolean displayLastBirthField) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_DISPLAY_LAST_BIRTH_FIELD, displayLastBirthField).apply();
    }

    public boolean displayDueDateField() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_DISPLAY_DUE_DATE_FIELD, true);
    }

    public void setDisplayDueDateField(boolean displayDueDateField) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_DISPLAY_DUE_DATE_FIELD, displayDueDateField).apply();
    }
}
