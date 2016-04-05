package org.terasology.calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.math.TeraMath;
import org.terasology.world.time.WorldTime;

/**
 * Calendar math system.  All the heavy lifting happens here.
 */
public class CalendarMath {
    WorldTime worldTime;

    private static final Logger logger = LoggerFactory.getLogger(CalendarMath.class);

    private int daysWeek; // days in a week
    private int daysMonth; // days in a month
    private int daysYear; // days in a year

    // internal storage
    private int currentYear;
    private int currentYearMonth;
    private int currentGameMonth;

    private int currentWeekDay;
    private int currentMonthDay;
    private int currentYearDay;

    private int currentMonthWeek;
    private int currentYearWeek;
    private int currentGameWeek;

    public CalendarMath(int daysInWeek, int daysInMonth, int daysInYear, WorldTime timer) {
        daysWeek = daysInWeek;
        daysMonth = daysInMonth;
        daysYear = daysInYear;
        worldTime = timer;
    }

    // do the thing
    public void updateToday() {

        int calcDays = TeraMath.floorToInt(worldTime.getDays());
        // calculate the year
        currentYear = TeraMath.floorToInt(calcDays / daysYear) + 1;
        currentYearDay = calcDays % daysYear;
        currentWeekDay = calcDays % daysWeek;

        // now calculate the month
        currentGameMonth = TeraMath.floorToInt(calcDays / daysMonth);
        currentYearMonth = TeraMath.floorToInt(currentYearDay / daysMonth);
        currentMonthDay = currentYearDay % daysMonth;
        // TODO implement weekInMonth

        // calculate week
        // TODO fix weekInYear, currently every year starts a new week
        currentGameWeek = TeraMath.floorToInt(calcDays / daysWeek);
        currentYearWeek = currentGameWeek % TeraMath.floorToInt(daysYear / daysWeek);

        logger.info(String.format("y %s\tyd %s\twd %s", currentYear, currentYearDay, currentWeekDay));
    }

    public int getCurrentGameMonth() {
        return currentGameMonth;
    }

    public int getCurrentGameWeek() {
        return currentGameWeek;
    }

    public int getCurrentMonthDay() {
        return currentMonthDay + 1;
    }

    public int getCurrentMonthWeek() {
        return currentMonthWeek;
    }

    public int getCurrentWeekDay() {
        return currentWeekDay;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getCurrentYearDay() {
        return currentYearDay + 1;
    }

    public int getCurrentYearMonth() {
        return currentYearMonth;
    }

    public int getCurrentYearWeek() {
        return currentYearWeek;
    }

    // utility stuffies
    public boolean isYearStart() {
        return (currentYearDay == 0);
    }

    public boolean isMonthStart() {
        return (currentMonthDay == 0);
    }

    public boolean isWeekStart() {
        return (currentWeekDay == 0);
    }

    public boolean isYearEnd() {
        return (getCurrentYearDay() == daysYear);
    }

    public boolean isMonthEnd() {
        return (getCurrentMonthDay() == daysMonth);
    }

    public boolean isWeekEnd() {
        return (getCurrentWeekDay() == (daysWeek - 1));
    }
}
