package org.terasology.calendar.data.components;

import org.terasology.entitySystem.Component;

public class Calendar implements Component {
    private int lenWeek;
    private String name;

    private Calendar() {
    }

    public Calendar(int daysPerWeek, String calendarName) {
        lenWeek = daysPerWeek;
        name = calendarName;
    }

    public int getDaysPerWeek() {
        return lenWeek;
    }

    public String getName() {
        return name;
    }

}
