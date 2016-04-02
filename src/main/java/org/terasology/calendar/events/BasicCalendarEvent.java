package org.terasology.calendar.events;

import org.terasology.calendar.data.CalendarData;

public class BasicCalendarEvent implements CalendarEvent {

    private CalendarData calendarData;

    public BasicCalendarEvent(CalendarData calendarData)
    {
        this.calendarData = calendarData;
    }

    @Override
    public String getName() {
        return calendarData.getName();
    }

    @Override
    public CalendarData getCalendarData() {
        return calendarData;
    }

    @Override
    public float eventDaysElapsed() {
        return calendarData.getDayOf();
    }

    @Override
    public float eventDaysRemaining() {
        return calendarData.getDayOf() - calendarData.getLength();
    }

    @Override
    public boolean isOneDayEvent() {
        return calendarData.isOneDayOnly();
    }

}
