package de.brunsen.guineatrack.model;

import java.util.Comparator;

public class GuineaPigComparator implements Comparator<GuineaPig>{

    @Override
    public int compare(GuineaPig a, GuineaPig b) {
        int compareResult = a.getRace().compareToIgnoreCase(b.getRace());
        if (compareResult == 0) {
            compareResult = a.getName().compareToIgnoreCase(b.getName());
        }
        return compareResult;
    }
}
