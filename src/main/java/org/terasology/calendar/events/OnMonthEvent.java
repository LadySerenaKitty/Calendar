package org.terasology.calendar.events;

public class OnMonthEvent extends OnCalendarEvent {
    String name;

    public OnMonthEvent(int yearNumber, int monthNumber, String monthName) {
        super(yearNumber, monthNumber);
        name = monthName;
    }
}
