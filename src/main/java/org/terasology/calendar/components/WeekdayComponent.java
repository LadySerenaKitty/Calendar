package org.terasology.calendar.components;

import org.terasology.entitySystem.Component;

public class WeekdayComponent implements Component {

    private String name;
    private int dayOfWeek;

    private WeekdayComponent() {
    }

    public WeekdayComponent(String dayName, int dayWeek) {
        name = dayName;
        dayOfWeek = dayWeek;
    }
}
