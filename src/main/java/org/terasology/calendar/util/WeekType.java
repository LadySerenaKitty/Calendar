package org.terasology.calendar.util;

public enum WeekType {
    GAME(0),
    YEAR(1),
    MONTH(2);

    private int identifier;
    private WeekType(int ident) {
        identifier = ident;
    }

    public int getIdentifier() {
        return identifier;
    }
}
