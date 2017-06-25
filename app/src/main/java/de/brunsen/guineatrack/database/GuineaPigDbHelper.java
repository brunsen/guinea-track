package de.brunsen.guineatrack.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GuineaPigDbHelper extends SQLiteOpenHelper{

    private static final String TAG = GuineaPigDbHelper.class.getSimpleName();
    private static GuineaPigDbHelper mInstance;

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "GuineaPig.db";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String ALTER_TABLE = "ALTER TABLE ";
    private static final String ADD_COLUMN = " ADD COLUMN ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static GuineaPigDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GuineaPigDbHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private GuineaPigDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createGuineaPigTable = CREATE_TABLE + GuineaPigDbContract.GuineaPigEntry.TABLE_NAME + " (" +
                GuineaPigDbContract.GuineaPigEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BIRTH + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_GENDER + INTEGER_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_COLOR + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_BREED + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigEntry.COLUMN_NAME_TYPE + INTEGER_TYPE +
                " )";

        final String createGuineaExtraTable = CREATE_TABLE + GuineaPigDbContract.GuineaPigOptionalEntry.TABLE_NAME + " (" +
                GuineaPigDbContract.GuineaPigOptionalEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_WEIGHT + INTEGER_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_LAST_BIRTH + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_DUE_DATE + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ORIGIN + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_LIMITATIONS + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_CASTRATION_DATE + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_PICTURE_PATH + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ENTRY + TEXT_TYPE + COMMA_SEP +
                GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_DEPARTURE + TEXT_TYPE +
                " )";
        db.execSQL(createGuineaPigTable);
        db.execSQL(createGuineaExtraTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String cmdAddEntryColumn = ALTER_TABLE + GuineaPigDbContract.GuineaPigOptionalEntry.TABLE_NAME
                + ADD_COLUMN + GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_ENTRY
                + TEXT_TYPE + "DEFAULT \"\"";
        String cmdAddDepartureColumn= ALTER_TABLE + GuineaPigDbContract.GuineaPigOptionalEntry.TABLE_NAME
                + ADD_COLUMN + GuineaPigDbContract.GuineaPigOptionalEntry.COLUMN_NAME_DEPARTURE
                + TEXT_TYPE + "DEFAULT \"\"";
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(cmdAddEntryColumn);
            db.execSQL(cmdAddDepartureColumn);
            Log.d(TAG, "executed upgrade commands");
        }
    }
}
