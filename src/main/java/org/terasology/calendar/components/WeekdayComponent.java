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
 * Complete definition of a weekday.
 */
public class WeekdayComponent implements Component {

    /**
     * Name of weekday (ex: Friday).
     */
    private String name;
    /**
     * Short name of weekday (ex: Fri).
     */
    private String shortName;
    /**
     * The day of the week, starting at 0.
     */
    private int dayOfWeek;

    /**
     * Creates an empty weekday.
     */
    private WeekdayComponent() {
    }

    /**
     * Creates a weekday with all the specified information.
     * @param dayName Name of the weekday (ex: Friday).
     * @param shortDayName Short name of weekday (ex: Fri).
     * @param dayWeek Day of the week.
     */
    public WeekdayComponent(String dayName, String shortDayName, int dayWeek) {
        name = dayName;
        shortName = shortDayName;
        dayOfWeek = dayWeek;
    }

    /**
     * Gets the day of the week, starting at 0.
     * @return Day of week.
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Gets the name of the weekday (ex: Friday).
     * @return Name of weekday.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the short name of the weekday (ex: Fri).
     * @return Short name of weekday.
     */
    public String getShortName() {
        return shortName;
    }
}
