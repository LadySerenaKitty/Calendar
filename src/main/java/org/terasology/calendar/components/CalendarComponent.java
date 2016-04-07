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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.terasology.calendar.components.internal.HolidayInitComponent;
import org.terasology.calendar.components.internal.MonthInitComponent;
import org.terasology.calendar.components.internal.SeasonInitComponent;
import org.terasology.calendar.components.internal.WeekdayInitComponent;
import org.terasology.entitySystem.Component;

/**
 * A copy of the calendar data which is used to initialize the {@link CalendarSystem}.
 */
public class CalendarComponent implements Component {

    /**
     * Number of days per month.
     */
    private int daysPerMonth;

    /**
     * Name of the calendar.
     */
    private String name;

    /**
     * Default date format for use with {@link CalendarFormatter}.
     */
    private String defaultFormat;

    /**
     * List of holidays in the calendar configuration.
     */
    private List<HolidayInitComponent> holidays;
    /**
     * List of months in the calendar configuration.
     */
    private List<MonthInitComponent> months;
    /**
     * List of seasons in the calendar configuration.
     */
    private List<SeasonInitComponent> seasons;
    /**
     * List of weekdays in the calendar configuration.
     */
    private List<WeekdayInitComponent> weekdays;

    /**
     * Constructs a new, empty calendar configuration.
     */
    private CalendarComponent() {
        holidays = new ArrayList<>();
        months = new ArrayList<>();
        seasons = new ArrayList<>();
        weekdays = new ArrayList<>();
    }

    /**
     * Creates a calendar configuration component.
     * @param monthLength Number of days per month.
     * @param calendarName Name of the calendar.
     * @param format Default date format to be used with {@link CalendarFormatter}.
     * @param holidayList List of holidays.
     * @param monthList List of months.
     * @param seasonList List of seasons.
     * @param weekdayList List of weekdays.
     */
    public CalendarComponent(int monthLength, String calendarName, String format,
            List<HolidayInitComponent> holidayList, List<MonthInitComponent> monthList, List<SeasonInitComponent> seasonList, List<WeekdayInitComponent> weekdayList) {
        daysPerMonth = monthLength;
        name = calendarName;
        defaultFormat = format;

        holidays = holidayList;
        months = monthList;
        seasons = seasonList;
        weekdays = weekdayList;
   }

    /**
     * Get the number of days per month.
     * @return Number of days per month.
     */
    public int getDaysPerMonth() {
        return daysPerMonth;
    }

    /**
     * Get the name of the calendar.
     * @return Calendar name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the default date format for use with {@link CalendarFormatter}.
     * @return Default date format.
     */
    public String getDefaultFormat() {
        return defaultFormat;
    }

    /**
     * Get the list of holidays.
     * @return List of holidays.
     */
    public List<HolidayInitComponent> getHolidays() {
        if (holidays == null) {
            return Collections.EMPTY_LIST;
        }
        return holidays;
    }

    /**
     * Get the holiday list iterator.
     * @return List iterator.
     */
    public Iterator<HolidayInitComponent> getHolidayIterator() {
        return getHolidays().iterator();
    }

    /**
     * Get the list of months.
     * @return List of months.
     */
    public List<MonthInitComponent> getMonths() {
        if (months == null) {
            return Collections.EMPTY_LIST;
        }
        return months;
    }

    /**
     * Get the month list iterator.
     * @return List iterator.
     */
    public Iterator<MonthInitComponent> getMonthIterator() {
        return getMonths().iterator();
    }

    /**
     * Get the list of seasons.
     * @return List of seasons.
     */
    public List<SeasonInitComponent> getSeasons() {
        if (seasons == null) {
            return Collections.EMPTY_LIST;
        }
        return seasons;
    }

    /**
     * Get the season list iterator.
     * @return List iterator.
     */
    public Iterator<SeasonInitComponent> getSeasonIterator() {
        return getSeasons().iterator();
    }

    /**
     * Get the list of weekdays.
     * @return List of weekdays.
     */
    public List<WeekdayInitComponent> getWeekdays() {
        if (weekdays == null) {
            return Collections.EMPTY_LIST;
        }
        return weekdays;
    }

    /**
     * Get the weekday list iterator.
     * @return List iterator.
     */
    public Iterator<WeekdayInitComponent> getWeekdayIterator() {
        return getWeekdays().iterator();
    }
}
