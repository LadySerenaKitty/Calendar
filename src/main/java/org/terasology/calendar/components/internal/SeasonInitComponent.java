package org.terasology.calendar.components.internal;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class SeasonInitComponent implements Component {
    public int startDay;
    public int startMonth;

    public int endDay;
    public int endMonth;

    public String name;

    public SeasonInitComponent() {
    }

    public SeasonInitComponent(int sDay, int sMonth, int eDay, int eMonth, String seasonName) {
        startDay = sDay;
        startMonth = sMonth;

        endDay = eDay;
        endMonth = eMonth;

        name = seasonName;
    }
}
