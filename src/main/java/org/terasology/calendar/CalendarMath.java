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
import org.terasology.calendar.components.DateComponent;
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
     * @param calcDays The game day to calculate with.
     */
    public void updateToday(int calcDays) {
        currentDay = calcDays;
        logger.info(String.format("updateToday: %s", calcDays));
        // calculate the year
        currentYear = TeraMath.floorToInt(calcDays / daysYear);
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
        return currentMonthDay;
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
     * @return The day of the week.
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
        return currentYearDay;
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
     * @return {@code true} if the day marks the beginning of a new year.
     */
    public boolean isYearStart() {
        return (currentYearDay == 0);
    }

    /**
     * Determines if the day is the first day of a month.
     * @return {@code true} if the day marks the beginning of a new month.
     */
    public boolean isMonthStart() {
        return (currentMonthDay == 0);
    }

    /**
     * Determines if the day is the first day of a week.
     * @return {@code true} if the day marks the beginning of a new week.
     */
    public boolean isWeekStart() {
        return (currentWeekDay == 0);
    }

    /**
     * Determines if the day is the last of a year.
     * @return {@code true} if the day marks the ending of a year.
     */
    public boolean isYearEnd() {
        return (getCurrentYearDay() == (daysYear - 1));
    }

    /**
     * Determines if the day is the last of a month.
     * @return {@code true} if the day marks the ending of a month.
     */
    public boolean isMonthEnd() {
        return (getCurrentMonthDay() == (daysMonth - 1));
    }

    /**
     * Determines if the day is the last of a week.
     * @return {@code true} if the day marks the ending of a week.
     */
    public boolean isWeekEnd() {
        return (getCurrentWeekDay() == (daysWeek - 1));
    }

    /**
     * Calculates the length of a thing based on its start and end dates.
     * @param startDay Starting day to check against.  Must be day in month.
     * @param startMonth Starting month to check against.  Must be month in year.
     * @param endDay Ending day to check against.  Must be day in month.
     * @param endMonth Ending month to check against.  Must be month in year.
     * @return The length (in days) of a thing.
     */
    public int getCalendarLength(int startDay, int startMonth, int endDay, int endMonth) {
        if (startMonth == endMonth) { // within the month
            return (endDay - startDay) + 1;
        } else if (startMonth < endMonth) { // within the same year
            return (daysMonth * (endMonth - startMonth)) + (endDay - startDay) + 1;
        } else if (endMonth < startMonth) { // spans a year crossing
            return (daysMonth * (startMonth - endMonth)) + (endDay - startDay) + 1;
        } else {
            return 1;
        }
    }

    /**
     * The day number of something, checked against the current date.
     * @param gameDayWhenStarted The game day to calculate with.
     * @return The day of something, for example, day 1 of a season.
     */
    public int dayOf(int gameDayWhenStarted) {
        return gameDayWhenStarted - currentDay + 1;
    }

    /**
     * The day number of something, checked against the current date.
     * @param date The date to calculate with.
     * @return The day of something, for example, day 1 of a season.
     */
    public int dayOf(DateComponent date) {
        return dayOf(date.getGameDay());
    }

    /**
     * The day number of something, checked against the current date.
     * @param startDay  The day when the thing started.
     * @param startMonth The month when the thing started.
     * @param startYear The year when the thing started.
     * @return The day of something, for example: day 1 of summer
     */
    public int dayOf(int startDay, int startMonth, int startYear) {
        int days = getCalendarLength(startDay, startMonth, currentMonthDay, currentYearMonth);
        if ( startYear < currentYear) {
            days += (startYear - currentYear) * daysYear;
        }
        return days;
    }

    /**
     * Checks start dates and end dates against a specified date to see if the specified date is within the dates.
     * @param startDay Starting day to check against.  Must be day in month.
     * @param startMonth Starting month to check against.  Must be month in year.
     * @param endDay Ending day to check against.  Must be day in month.
     * @param endMonth Ending month to check against.  Must be month in year.
     * @return true if the specified date is within the date range.
     */
    public boolean isThingCurrent(int startDay, int startMonth, int endDay, int endMonth) {

        if (startMonth <= endMonth) {
            // within the year
            if (startMonth <= currentYearMonth && currentYearMonth <= endMonth) {
                if (startMonth < currentYearMonth && currentYearMonth < endMonth) {
                    // in a middle month
                    return true;
                } else if (startMonth == currentYearMonth && startDay <= currentMonthDay) {
                    // in the starting month
                    return true;
                } else if (startMonth == currentYearMonth && currentMonthDay <= endDay) {
                    // in the ending month
                    return true;
                }
            }
            return false;
        } else if (endMonth < startMonth) {
            // year crossing
            if (startMonth <= currentYearMonth) {
                if (startMonth == currentYearMonth && startDay <= currentMonthDay) {
                    return true;
                } else if (startMonth < currentYearMonth) {
                    return true;
                }
            } else if (currentYearMonth <= endMonth) {
                if (endMonth == currentYearMonth && currentMonthDay <= endDay) {
                    return true;
                } else if (currentYearMonth < endMonth) {
                    return true;
                }
            }
        }
        return false;
    }
}
