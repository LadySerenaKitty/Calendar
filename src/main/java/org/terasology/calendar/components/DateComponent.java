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
package org.terasology.calendar.components;

import org.terasology.entitySystem.Component;

/**
 * Simple definition of a date.
 */
public class DateComponent implements Component {
    private int day;
    private int month;
    private int year;
    private int gameDay;

    /**
     * Constructs an empty date.
     */
    private DateComponent() {
    }

    /**
     * Creates a new date based on some information.  These must be zero-based to allow for index retrieval.
     * @param newDay The day of the month.
     * @param newMonth The month of the year.
     * @param newYear The year number.
     * @param totalDay The number of total days in the game.
     * @see CalendarMath
     */
    public DateComponent(int newDay, int newMonth, int newYear, int totalDay) {
        day = newDay;
        month = newMonth;
        year = newYear;
        gameDay = totalDay;
    }

    /**
     * Get the day of the month.
     * @return Day of month.
     */
    public int getDay() {
        return day;
    }

    /**
     * Get the month of the year.
     * @return Month of year.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Get the year number.
     * @return Year number.
     */
    public int getYear() {
        return year;
    }

    /**
     * Total day number in game.
     * @return Day of game.
     */
    public int getGameDay() {
        return gameDay;
    }
}
