package org.terasology.calendar.components.internal;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class HolidayInitComponent implements Component {

    public int startDay;
    public int startMonth;
    public int endDay;
    public int endMonth;

    public String name;

    public HolidayInitComponent() {
    }

    public HolidayInitComponent(int calendarStartDay, int calendarStartMonth, int calendarEndDay, int calendarEndMonth, String calendarName) {
        startDay = calendarStartDay;
        startMonth = calendarStartMonth;
        endDay = calendarEndDay;
        endMonth = calendarEndMonth;

        name = calendarName;
    }
}
