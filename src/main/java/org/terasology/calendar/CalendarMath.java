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

    public CalendarMath(int daysInWeek, int daysInMonth, int daysInYear, WorldTime timer) {
        daysWeek = daysInWeek;
        daysMonth = daysInMonth;
        daysYear = daysInYear;
        worldTime = timer;
    }

    public CalendarMath dupicate() {
        return new CalendarMath(daysWeek, daysMonth, daysYear, worldTime);
    }

    // do the thing
    public void updateToday() {
        updateToday(TeraMath.floorToInt(worldTime.getDays()));
    }
    public void updateToday(int calcDays) {
        currentDay = calcDays;
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

    public int getCurrentDay() {
        return currentDay;
    }

    public int getCurrentGameMonth() {
        return currentGameMonth;
    }

    public int getCurrentGameWeek() {
        return currentGameWeek;
    }

    public int getCurrentMonthDay() {
        return currentMonthDay;
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
        return currentYearDay;
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
        return (getCurrentYearDay() == (daysYear - 1));
    }

    public boolean isMonthEnd() {
        return (getCurrentMonthDay() == (daysMonth - 1));
    }

    public boolean isWeekEnd() {
        return (getCurrentWeekDay() == (daysWeek - 1));
    }
}
