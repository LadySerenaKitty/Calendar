package org.terasology.calendar.data;

public class Holiday extends BasicCalendarData {

    @Override
    public String getLongName() {
        StringBuilder stringBuilder = new StringBuilder(longName);
        stringBuilder.append(" ");
        stringBuilder.append(getName());

        return stringBuilder.toString();
    }

    @Override
    public float getLength() {
        return 0;
    }

    @Override
    public boolean isOneDayOnly() {
        return true;
    }
}
