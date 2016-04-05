package org.terasology.calendar.events;

import org.terasology.calendar.util.WeekType;

public class OnWeekEvent extends OnCalendarEvent {

    WeekType type;

    public OnWeekEvent(int yearNumber, int unitNumber, WeekType weekType) {
        super(yearNumber, unitNumber);
    }
}
