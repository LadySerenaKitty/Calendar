package org.terasology.calendar.components;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class MonthComponent implements Component {
    public int length;
    public int month;

    public String shortName;
    public String mediumName;
    public String longName;

    public MonthComponent() {
    }

    public MonthComponent(int monthLength, int monthNumber, String sName, String mName, String lName) {
        length = monthLength;
        month = monthNumber;

        shortName = sName;
        mediumName = mName;
        longName = lName;
    }


    public int getLength() {
        return length;
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
