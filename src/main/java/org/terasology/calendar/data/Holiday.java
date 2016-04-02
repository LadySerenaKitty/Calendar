package org.terasology.calendar.data;

public class Holiday extends BasicCalendarData {

    @Override
    public String getLongName() {
        StringBuilder stringBuilder = new StringBuilder(lName);
        stringBuilder.append(" ");
        stringBuilder.append(getName());

        return stringBuilder.toString();
    }
}
