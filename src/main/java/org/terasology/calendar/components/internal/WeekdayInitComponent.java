package org.terasology.calendar.components.internal;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class WeekdayInitComponent implements Component {

    public String name;
    public int dayOfWeek;

    public WeekdayInitComponent() {
    }

    public WeekdayInitComponent(String dayName, int dayWeek) {
        name = dayName;
        dayOfWeek = dayWeek;
    }
}
