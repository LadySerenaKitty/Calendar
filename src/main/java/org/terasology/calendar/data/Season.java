package org.terasology.calendar.data;

public class Season extends BasicCalendarData {

    @Override
    public String getLongName() {
        StringBuilder stringBuilder = new StringBuilder("Day ");
        stringBuilder.append(getDayOf());
        stringBuilder.append(" ");
        stringBuilder.append(" of ");
        stringBuilder.append(getName());

        return stringBuilder.toString();
    }


}
