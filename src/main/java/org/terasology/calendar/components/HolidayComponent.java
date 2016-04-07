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
import org.terasology.reflection.MappedContainer;

/**
 * Complete definition of a holiday.
 */
@MappedContainer
public class HolidayComponent implements Component {

    /**
     * The starting day of month.
     */
    private int startDay;
    /**
     * The starting month of year.
     */
    private int startMonth;

    /**
     * The ending day of month.
     */
    private int endDay;
    /**
     * The ending month of year.
     */
    private int endMonth;

    /**
     * Length (in days) of the holiday.
     */
    private int length;

    /**
     * Name of the holiday.
     */
    private String name;

    /**
     * Creates an empty holiday.
     */
    private HolidayComponent() {
    }

    /**
     * Creates a holiday with all the specified information.
     * @param calendarStartDay The starting day of month.
     * @param calendarStartMonth The starting month of year.
     * @param calendarEndDay The ending day of month.
     * @param calendarEndMonth The ending month of year.
     * @param calendarLength The length (in days) of the holiday.
     * @param calendarName The name of the holiday.
     */
    public HolidayComponent(int calendarStartDay, int calendarStartMonth, int calendarEndDay, int calendarEndMonth, int calendarLength, String calendarName) {
        startDay = calendarStartDay;
        startMonth = calendarStartMonth;
        endDay = calendarEndDay;
        endMonth = calendarEndMonth;
        length = calendarLength;

        name = calendarName;
    }

    /**
     * Get the starting day of month.
     * @return Day of month.
     */
    public int getStartDay() {
        return startDay;
    }

    /**
     * Get the starting month of year.
     * @return Month of year.
     */
   public int getStartMonth() {
        return startMonth;
    }

   /**
    * Get the ending day of month.
    * @return Day of month.
    */
    public int getEndDay() {
        return endDay;
    }

    /**
     * Get the ending month of year.
     * @return Month of year.
     */
    public int getEndMonth() {
        return endMonth;
    }

    /**
     * Get the length (in days) of the holiday.
     * @return Length in days.
     */
    public int getLength() {
        return length;
    }

    /**
     * Get the name of the holiday.
     * @return Name of holiday.
     */
    public String getName() {
        return name;
    }
}
