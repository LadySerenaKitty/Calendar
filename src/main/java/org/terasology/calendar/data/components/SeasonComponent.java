package org.terasology.calendar.data.components;

import org.terasology.entitySystem.Component;

public class SeasonComponent implements Component {
    private int sDay;
    private int sMonth;

    private int eDay;
    private int eMonth;

    private String sName;

    private SeasonComponent() {
    }

    public SeasonComponent(int startDay, int startMonth, int endDay, int endMonth, String name) {
        sDay = startDay;
        sMonth = startMonth;

        eDay = endDay;
        eMonth = endMonth;

        sName = name;
    }

    public int getStartDay() {
        return sDay;
    }

   public int getStartMonth() {
        return sMonth;
    }

    public int getEndDay() {
        return eDay;
    }

    public int getEndMonth() {
        return eMonth;
    }

    public String getName() {
        return sName;
    }

}
