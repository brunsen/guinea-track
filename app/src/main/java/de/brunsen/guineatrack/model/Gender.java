package de.brunsen.guineatrack.model;

import android.content.Context;

import de.brunsen.guineatrack.R;

public enum Gender {
    Male(0), Female(1), CASTRATO(2);
    private int position;

    Gender(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getText(Context context) {
        String value = "";
        switch (this) {
            case Male:
                value = context.getString(R.string.male);
                break;
            case Female:
                value = context.getString(R.string.female);
                break;
            case CASTRATO:
                value = context.getString(R.string.castrato);
                break;
        }
        return value;
    }

    public static Gender fromInt(int value) {
        if (value == 0) {
            return Male;
        } else if (value == 1) {
            return Female;
        } else {
            return CASTRATO;
        }
    }
}
