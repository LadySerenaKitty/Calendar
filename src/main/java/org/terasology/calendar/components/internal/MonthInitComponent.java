package org.terasology.calendar.components.internal;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class MonthInitComponent implements Component {
    public int month;

    public String shortName;
    public String mediumName;
    public String longName;

    public MonthInitComponent() {
    }

    public MonthInitComponent(int monthNumber, String sName, String mName, String lName) {
        month = monthNumber;

        shortName = sName;
        mediumName = mName;
        longName = lName;
    }
}
