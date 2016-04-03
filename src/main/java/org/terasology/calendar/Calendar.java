package org.terasology.calendar;

import org.terasology.calendar.data.DateInformation;
import org.terasology.math.TeraMath;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.time.WorldTime;

/**
 * Calendar math system.  All the heavy lifting happens here.
 */
public class Calendar {

    float lenWeek; // days in a week
    float lenMonth; // days in a month
    float lenYear; //  months in a year

    float leapYears; // every N years to add a leap day

    boolean leapSkip; // if true, a day is removed instead of added

    float yearDays; // days in a year

    float leapDiff; // these two are to make things easier
    float yearDiff;

    // current info
    DateInformation today;

    @In
    WorldProvider worldProvider;
    WorldTime worldTime;

    public Calendar(float lenWeek, float lenMonth, float lenYear, float leapYears, boolean leapSkip, float yearDays) {
        this.lenWeek = lenWeek;
        this.lenMonth = lenMonth;
        this.lenYear = lenYear;
        this.leapYears = leapYears;
        this.leapSkip = leapSkip;
        this.yearDays = yearDays;

        if ( leapSkip ) { leapDiff = leapYears / 1; }
        else { leapDiff = 1 / leapYears; }
        leapDiff++;

        yearDiff = yearDays * leapDiff;

        worldTime = worldProvider.getTime();
    }

    public Calendar(float lenWeek, float lenMonth, float lenYear, float leapYears, boolean leapSkip) {
        this.lenWeek = lenWeek;
        this.lenMonth = lenMonth;
        this.lenYear = lenYear;
        this.leapYears = leapYears;
        this.leapSkip = leapSkip;
        this.yearDays = lenMonth * lenYear;

        if ( leapSkip ) { leapDiff = leapYears / 1; }
        else { leapDiff = 1 / leapYears; }
        leapDiff++;

        yearDiff = yearDays * leapDiff;

        worldTime = worldProvider.getTime();
    }

    public DateInformation updateToday() {

        int calcDays = TeraMath.floorToInt(worldTime.getDays());
        boolean leapYear = (calcDays / (yearDays * leapDiff)) == 0;
        // calculate the year
        int year = TeraMath.floorToInt(calcDays / (yearDays * leapDiff));
        int dayNum = calcDays % TeraMath.floorToInt(lenYear);

        // calculate week in year
        // TODO fix weekInYear, currently every year starts a new week
        //int weekInYear = TeraMath.floorToInt(dayNum / lenWeek);
        int weekDay = TeraMath.floorToInt(calcDays % lenWeek);

        // now calculate the month
        int month = TeraMath.floorToInt(calcDays % lenMonth);
        int day = calcDays - TeraMath.floorToInt(month * lenMonth);
        // TODO implement weekInMonth
        // TODO make any month the leap/skip month

        return new DateInformation(year, month, day, (float)calcDays);
    }
}
