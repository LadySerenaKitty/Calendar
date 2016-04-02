package org.terasology.calendar.data;

public class DateInformation {

    // standard stuff
    private int year;
    private int month;
    private int day;

    // secondary keeping
    private int weekMonth;
    private int weekYear;

    private int weekDay;

    public DateInformation(int year, int month, int day, float gameDay)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

}
