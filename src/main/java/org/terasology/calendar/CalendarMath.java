/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.math.TeraMath;
import org.terasology.world.time.WorldTime;

/**
 * Calendar math system.  All the heavy lifting happens here.
 */
public class CalendarMath {
    private static final Logger logger = LoggerFactory.getLogger(CalendarMath.class);

    private WorldTime worldTime;

    private int daysWeek; // days in a week
    private int daysMonth; // days in a month
    private int daysYear; // days in a year

    // internal storage
    private int currentDay;
    private int currentYear;
    private int currentYearMonth;
    private int currentGameMonth;

    private int currentWeekDay;
    private int currentMonthDay;
    private int currentYearDay;

    private int currentMonthWeek;
    private int currentYearWeek;
    private int currentGameWeek;

    /**
     * Creates a new CalendarMath object.
     * @param daysInWeek Number of days in a week.
     * @param daysInMonth Number of days in a month.
     * @param daysInYear Number of days in a year.
     * @param timer This is used for operations that need to work on the current date.
     */
    public CalendarMath(int daysInWeek, int daysInMonth, int daysInYear, WorldTime timer) {
        daysWeek = daysInWeek;
        daysMonth = daysInMonth;
        daysYear = daysInYear;
        worldTime = timer;
    }

    /**
     * Duplicates the CalendarMath object.
     * @return The new CalendarMath object.
     */
    public CalendarMath dupicate() {
        return new CalendarMath(daysWeek, daysMonth, daysYear, worldTime);
    }

    /**
     * Calculates the values based on the current game day.
     */
    public void updateToday() {
        updateToday(TeraMath.floorToInt(worldTime.getDays()));
    }

    /**
     * Calculate the values to a specific game day.
     * @param calcDays
     */
    public void updateToday(int calcDays) {
        currentDay = calcDays;
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
    }

    /**
     * The game day, total days elapsed in the game.
     * @return The day, based on total days elapsed in game.
     */
    public int getCurrentDay() {
        return currentDay;
    }

    /**
     * The month, based on total months elapsed in game.
     * @return The month.
     */
    public int getCurrentGameMonth() {
        return currentGameMonth;
    }

    /**
     * The week, based on total weeks elapsed in game.
     * @return The week of the game.
     */
    public int getCurrentGameWeek() {
        return currentGameWeek;
    }

    /**
     * The day of the month.
     * @return The day of the month.
     */
    public int getCurrentMonthDay() {
        return currentMonthDay + 1;
    }

    /**
     * The week of the month.
     * @return The week number.
     */
    public int getCurrentMonthWeek() {
        return currentMonthWeek;
    }

    /**
     * The day of the week.
     * @return
     */
    public int getCurrentWeekDay() {
        return currentWeekDay;
    }

    /**
     * The year.
     * @return The year.
     */
    public int getCurrentYear() {
        return currentYear;
    }

    /**
     * The day number of the year.
     * @return Day of the year.
     */
    public int getCurrentYearDay() {
        return currentYearDay + 1;
    }

    /**
     * The month of the year.
     * @return Month of the year.
     */
    public int getCurrentYearMonth() {
        return currentYearMonth;
    }

    /**
     * The week of the year.
     * @return Week of year.
     */
    public int getCurrentYearWeek() {
        return currentYearWeek;
    }

    /**
     * Determines if the day is the first day of a year.
     * @return true if the day marks the beginning of a new year.
     */
    public boolean isYearStart() {
        return (currentYearDay == 0);
    }

    /**
     * Determines if the day is the first day of a month.
     * @return true if the day marks the beginning of a new month.
     */
    public boolean isMonthStart() {
        return (currentMonthDay == 0);
    }

    /**
     * Determines if the day is the first day of a week.
     * @return true if the day marks the beginning of a new week.
     */
    public boolean isWeekStart() {
        return (currentWeekDay == 0);
    }

    /**
     * Determines if the day is the last of a year.
     * @return true if the day marks the ending of a year.
     */
    public boolean isYearEnd() {
        return (getCurrentYearDay() == daysYear);
    }

    /**
     * Determines if the day is the last of a month.
     * @return true if the day marks the ending of a month.
     */
    public boolean isMonthEnd() {
        return (getCurrentMonthDay() == daysMonth);
    }

    /**
     * Determines if the day is the last of a week.
     * @return true if the day marks the ending of a week.
     */
    public boolean isWeekEnd() {
        return (getCurrentWeekDay() == (daysWeek - 1));
    }
}
