package org.terasology.calendar.data.components;

import org.terasology.entitySystem.Component;

public class Month implements Component {
    private int length;
    private int month;

    private String sName;
    private String mName;
    private String lName;

    private Month() {
    }

    public Month(int monthLength, int monthNumber, String shortName, String mediumName, String longName) {
        length = monthLength;
        month = monthNumber;

        sName = shortName;
        mName = mediumName;
        lName = longName;
    }


    public int getLength() {
        return length;
    }

   public int getMonth() {
        return month;
    }

    public String getShortName() {
        return sName;
    }

    public String getMediumName() {
        return mName;
    }

    public String getLongName() {
        return lName;
    }

}
