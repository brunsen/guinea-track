package de.brunsen.guineatrack.model;

import java.util.Comparator;

public class GuineaPigComparator implements Comparator<GuineaPig>{

    @Override
    public int compare(GuineaPig a, GuineaPig b) {
        String currentRace = a.getRace().replaceAll("\\s+","");
        String anotherRace = b.getRace().replaceAll("\\s+","");
        int compareResult = currentRace.compareToIgnoreCase(anotherRace);
        if (compareResult == 0) {
            compareResult = a.getName().compareToIgnoreCase(b.getName());
        }
        return compareResult;
    }
}
