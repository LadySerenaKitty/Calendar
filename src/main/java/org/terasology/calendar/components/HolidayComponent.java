package org.terasology.calendar.components;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class HolidayComponent implements Component {

    private int day;
    private int month;
    private int endDay;
    private int endMonth;

    private String name;

    private HolidayComponent() {
    }

    public HolidayComponent(int calendarStartDay, int calendarStartMonth, int calendarEndDay, int calendarEndMonth, String calendarName) {
        day = calendarStartDay;
        month = calendarStartMonth;
        endDay = calendarEndDay;
        endMonth = calendarEndMonth;

        name = calendarName;
    }

     public int getStartDay() {
        return day;
    }

   public int getStartMonth() {
        return month;
    }

    public int getEndDay() {
        return endDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public String getName() {
        return name;
    }
}
