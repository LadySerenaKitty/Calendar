package org.terasology.calendar.events;

public class OnSeasonEvent extends OnCalendarEvent {
    String name;

    public OnSeasonEvent(int yearNumber, int unitNumber, String seasonName) {
        super(yearNumber, unitNumber);
        name = seasonName;
    }

    public String getName() {
        return name;
    }

}
