package org.terasology.calendar.components;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class HolidayComponent implements Component {

    private int day;
    private int month;
    private int length;

    private String name;

    private HolidayComponent() {
    }

    public HolidayComponent(int calendarDay, int calendarMonth, int days, String calendarName) {
        day = calendarDay;
        month = calendarMonth;
        length = days;
        name = calendarName;
    }

     public int getDay() {
        return day;
    }

    public int getLength() {
        return length;
    }

   public int getMonth() {
        return month;
    }

    public String getName() {
        return name;
    }
}
