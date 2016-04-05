package org.terasology.calendar.events;

public class OnYearEvent extends OnCalendarEvent {

    public OnYearEvent(int yearNumber, int unitNumber) {
        super(yearNumber, 0);
    }

    @Override
    public int getUnit() {
        return year;
    }
}
