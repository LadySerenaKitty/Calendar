package org.terasology.calendar.events;

import org.terasology.calendar.util.WeekType;

public class OnWeekStart extends OnWeekEvent {

    public OnWeekStart(int yearNumber, int unitNumber, WeekType weekType) {
        super(yearNumber, unitNumber, weekType);
    }

}
