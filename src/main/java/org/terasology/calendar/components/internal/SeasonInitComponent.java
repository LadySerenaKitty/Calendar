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
package org.terasology.calendar.components.internal;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

/**
 * Definition of a season.
 */
@MappedContainer
public class SeasonInitComponent implements Component {

    /**
     * The starting day of month.
     */
    public int startDay;
    /**
     * The starting month of year.
     */
    public int startMonth;

    /**
     * The ending day of month.
     */
    public int endDay;
    /**
     * The ending month of year.
     */
    public int endMonth;

    /**
     * Name of the season.
     */
    public String name;

    /**
     * Creates an empty season.
     */
    public SeasonInitComponent() {
    }

    /**
     * Creates a season with all the specified information.
     * @param calendarStartDay The starting day of month.
     * @param calendarStartMonth The starting month of year.
     * @param calendarEndDay The ending day of month.
     * @param calendarEndMonth The ending month of year.
     * @param calendarName The name of the holiday.
     */
    public SeasonInitComponent(int calendarStartDay, int calendarStartMonth, int calendarEndDay, int calendarEndMonth, String calendarName) {
        startDay = calendarStartDay;
        startMonth = calendarStartMonth;
        endDay = calendarEndDay;
        endMonth = calendarEndMonth;

        name = calendarName;
    }
}
