package org.terasology.calendar.events;

import org.terasology.calendar.data.CalendarData;

public class DayEvent extends BasicCalendarEvent {

    public DayEvent(CalendarData calendarData) {
        super(calendarData);
    }

    @Override
    public float eventDaysElapsed() {
        return 0f;
    }

    @Override
    public float eventDaysRemaining() {
        return 0f;
    }

    @Override
    public boolean isOneDayEvent() {
        return true;
    }

}
