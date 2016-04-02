package org.terasology.calendar.data;

public interface CalendarData {

    public String getName();
    public String getShortName();
    public String getMediumName();
    public String getLongName();

    public float getStartDay();
    public float getLength();
    public float getEndDay();
    public float getDayOf();
    public boolean isOneDayOnly();
}
