package de.brunsen.guineatrack.model;

import android.content.Context;

import de.brunsen.guineatrack.R;

public enum Type {
    SALE(0), BREED(1), RESCUE(2), COLLECTOR(3);
    private int position;

    Type(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getText(Context context) {
        String value = "";
        switch (this) {
            case SALE:
                value = context.getString(R.string.type_sale);
                break;
            case BREED:
                value = context.getString(R.string.type_breed);
                break;
            case RESCUE:
                value = context.getString(R.string.type_rescue);
                break;
            case COLLECTOR:
                value = context.getString(R.string.type_collector);
                break;
        }
        return value;
    }

    public static Type fromInt(int value) {
        switch (value) {
            case 0:
                return SALE;
            case 1:
                return BREED;
            case 2:
                return RESCUE;
            case 3:
                return COLLECTOR;
            default:
                return SALE;
        }
    }
}
