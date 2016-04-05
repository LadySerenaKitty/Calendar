package org.terasology.calendar.events;

import org.terasology.entitySystem.event.Event;

public class OnCalendarEvent implements Event {
    int year;
    int unit;

    public OnCalendarEvent(int yearNumber, int unitNumber) {
        year = yearNumber;
        unit = unitNumber;
    }

    public int getUnit() {
        return unit;
    }

    public int getYear() {
        return year;
    }
}
