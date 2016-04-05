package org.terasology.calendar.components;

import org.terasology.entitySystem.Component;

public class DateComponent implements Component {
    private int day;
    private int month;
    private int year;

    public DateComponent(int newDay, int newMonth, int newYear) {
        day = day;
        month = month;
        year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
