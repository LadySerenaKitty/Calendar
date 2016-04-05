package org.terasology.calendar.events;

import org.terasology.calendar.util.WeekType;

public class OnWeekEnd extends OnWeekEvent {

    public OnWeekEnd(int yearNumber, int unitNumber, WeekType weekType) {
        super(yearNumber, unitNumber, weekType);
    }

}
