package org.terasology.calendar.components;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class MonthComponent implements Component {
    private int month;

    private String shortName;
    private String mediumName;
    private String longName;

    private MonthComponent() {
    }

    public MonthComponent(int monthNumber, String sName, String mName, String lName) {
        month = monthNumber;

        shortName = sName;
        mediumName = mName;
        longName = lName;
    }

   public int getMonth() {
        return month;
    }

    public String getName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getMediumName() {
        return mediumName;
    }

    public String getLongName() {
        return longName;
    }

}
