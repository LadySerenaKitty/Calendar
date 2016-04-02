package org.terasology.calendar.data;

public class Happening extends BasicCalendarData {

    @Override
    public String getLongName() {
        StringBuilder stringBuilder = new StringBuilder(longName);
        stringBuilder.append(" ");
        stringBuilder.append(getName());

        return stringBuilder.toString();
    }

}
