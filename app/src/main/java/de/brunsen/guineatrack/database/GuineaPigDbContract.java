package de.brunsen.guineatrack.database;

import android.provider.BaseColumns;

public final class GuineaPigDbContract {
    public GuineaPigDbContract() {}

    /* Inner class that defines the table contents */
    public static abstract class GuineaPigEntry implements BaseColumns {
        public static final String TABLE_NAME = "GuineaPig";
        public static final String COLUMN_NAME_ID = "GuineaPigId";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_GENDER = "Gender";
        public static final String COLUMN_NAME_COLOR = "Color";
        public static final String COLUMN_NAME_BREED = "Breed";
        public static final String COLUMN_NAME_TYPE = "Type";
    }
}
