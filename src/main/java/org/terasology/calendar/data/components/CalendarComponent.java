package org.terasology.calendar.data.components;

import org.terasology.entitySystem.Component;

public class CalendarComponent implements Component {
    private int lenWeek;
    private String name;

    private CalendarComponent() {
    }

    public CalendarComponent(int daysPerWeek, String calendarName) {
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
