package de.brunsen.guineatrack.model;

import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.services.ContextProvider;

public enum Type {
    SALE(0), BREED(1), RESCUE(2);
    private int position;

    Type(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getText() {
        String value = "";
        switch (this) {
            case SALE:
                value = ContextProvider.getContext().getString(R.string.type_sale);
                break;
            case BREED:
                value = ContextProvider.getContext().getString(R.string.type_breed);
                break;
            case RESCUE:
                value = ContextProvider.getContext().getString(R.string.type_rescue);
                break;
        }
        return value;
    }

    public static Type fromInt(int value) {
        if (value == 0) {
            return SALE;
        } else if (value == 1) {
            return BREED;
        } else {
            return RESCUE;
        }
    }
}
