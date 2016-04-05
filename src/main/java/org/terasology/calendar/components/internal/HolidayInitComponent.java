package org.terasology.calendar.components.internal;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class HolidayInitComponent implements Component {

    public int day;
    public int month;
    public int length;

    public String name;

    public HolidayInitComponent() {
    }

    public HolidayInitComponent(int calendarDay, int calendarMonth, int days, String calendarName) {
        day = calendarDay;
        month = calendarMonth;
        length = days;
        name = calendarName;
    }
}
