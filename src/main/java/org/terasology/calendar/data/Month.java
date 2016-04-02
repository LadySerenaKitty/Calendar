package org.terasology.calendar.data;

public class Month extends BasicCalendarData {

    private int monthNumber;

    public Month(int monthNumber, float startDay, float length, String name, String longName, String mediumName, String shortName) {
        super(startDay, length, name, longName, mediumName, shortName);
        this.monthNumber = monthNumber;
    }

    public Month(int monthNumber, float startDay, float length, String name, String longName, String mediumName) {
        super(startDay, length, name, longName, mediumName);
        this.monthNumber = monthNumber;
    }

    public Month(int monthNumber, float startDay, float length, String name, String longName) {
        super(startDay, length, name, longName);
        this.monthNumber = monthNumber;
    }

    public Month(int monthNumber, float startDay, float length, String name) {
        super(startDay, length, name);
        this.monthNumber = monthNumber;
    }

    public Month(int monthNumber, float startDay, float length) {
        super(startDay, length);
        this.monthNumber = monthNumber;
    }

    public Month(int monthNumber, float startDay) {
        super(startDay);
        this.monthNumber = monthNumber;
    }

    public Month(int monthNumber) {
        super();
        this.monthNumber = monthNumber;
    }

    public Month() {
        super();
        this.monthNumber = 1;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

}
