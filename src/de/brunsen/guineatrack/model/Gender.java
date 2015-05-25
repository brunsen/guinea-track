package de.brunsen.guineatrack.model;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.services.ContextProvider;

public enum Gender {
    Male(0), Female(1), CASTRATO(2);
    private int position;

    Gender(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        String value = "";
        switch (this) {
            case Male:
                value = ContextProvider.getContext().getString(R.string.male);
                break;
            case Female:
                value = ContextProvider.getContext().getString(R.string.female);
                break;
            case CASTRATO:
                value = ContextProvider.getContext().getString(R.string.castrato);
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
