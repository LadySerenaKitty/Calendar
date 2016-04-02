package org.terasology.calendar.events;

import org.terasology.calendar.data.CalendarData;

public interface CalendarEvent {

    public String getName();
    public CalendarData getCalendarData();
    public float eventDaysElapsed();
    public float eventDaysRemaining();
    public boolean isOneDayEvent();

}
