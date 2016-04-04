package org.terasology.calendar.components;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class SeasonComponent implements Component {
    public int startDay;
    public int startMonth;

    public int endDay;
    public int endMonth;

    public String name;

    public SeasonComponent() {
    }

    public SeasonComponent(int sDay, int sMonth, int eDay, int eMonth, String seasonName) {
        startDay = sDay;
        startMonth = sMonth;

        endDay = eDay;
        endMonth = eMonth;

        name = seasonName;
    }

    public int getStartDay() {
        return startDay;
    }

   public int getStartMonth() {
        return startMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public String getName() {
        return name;
    }

}
