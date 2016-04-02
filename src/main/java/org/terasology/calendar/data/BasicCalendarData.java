package org.terasology.calendar.data;

import org.terasology.math.TeraMath;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;

public class BasicCalendarData implements CalendarData {

    // system
    @In
    WorldProvider world;

    // data
    String nName;
    String sName;
    String mName;
    String lName;

    float start;
    float length;

    public BasicCalendarData(float startDay, float length, String name, String longName, String mediumName, String shortName) {
        int_init(startDay, length, name, longName, mediumName, shortName);
    }
    public BasicCalendarData(float startDay, float length, String name, String longName, String mediumName) {
        int_init(startDay, length, name, longName, mediumName);
    }
    public BasicCalendarData(float startDay, float length, String name, String longName) {
        int_init(startDay, length, name, longName);
    }
    public BasicCalendarData(float startDay, float length, String name) {
        int_init(startDay, length, name);
    }
    public BasicCalendarData(float startDay, float length) {
        int_init(startDay, length);
    }
    public BasicCalendarData(float startDay) {
        int_init(startDay);
    }

    public BasicCalendarData() {
        int_init(TeraMath.fastFloor(world.getTime().getDays()));
    }

    private void int_init(float startDay, float days, String name, String longName, String mediumName, String shortName) {
           this.start = startDay;
           this.length = days;

           this.nName = name;
           this.sName = shortName;
           this.mName = mediumName;
           this.lName = longName;
    }
    private void int_init(float startDay, float length, String name, String longName, String mediumName) {
        String shrtName;
        switch ( name.length() ) {
            case 1:
            case 2:
            case 3: shrtName = mediumName; break;
            default: shrtName = mediumName.substring(0, 2);
        }
        int_init(startDay, length, name, longName, mediumName, shrtName);
    }
    private void int_init(float startDay, float length, String name, String longName) {
        String medName;
        switch ( name.length() ) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: medName = name; break;
            default: medName = name.substring(0, 4);
        }
        int_init(startDay, length, name, longName, medName);
    }
    private void int_init(float startDay, float length, String name) {
        int_init(startDay, length, name, "Grand Basic Day");
    }
    private void int_init(float startDay, float length) {
        int_init(startDay, length, "Basic Day");
    }
    private void int_init(float startDay) {
        int_init(startDay, startDay + 10f);
    }

    @Override
    public String getName() {
        return nName;
    }

    @Override
    public String getShortName() {
        return sName;
    }

    @Override
    public String getMediumName() {
        return mName;
    }

    @Override
    public String getLongName() {
        return lName;
    }

    @Override
    public float getStartDay() {
        return start;
    }

    @Override
    public float getLength() {
        return length;
    }

    @Override
    public float getEndDay() {
        return getStartDay() + getLength();
    }

    @Override
    public float getDayOf() {
        return (world.getTime().getDays() - getStartDay()) + 1;
    }

    @Override
    public boolean isOneDayOnly() {
        return (length == 0);
    }

}
