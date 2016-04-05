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

public class CalendarComponent implements Component {
    private int daysPerMonth;
    private String name;
    private String defaultFormat;

    private List<HolidayInitComponent> holidays;
    private List<MonthInitComponent> months;
    private List<SeasonInitComponent> seasons;
    private List<WeekdayInitComponent> weekdays;

    private CalendarComponent() {
        holidays = new ArrayList<>();
        months = new ArrayList<>();
        seasons = new ArrayList<>();
        weekdays = new ArrayList<>();
    }

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

    public int getDaysPerMonth() {
        return daysPerMonth;
    }

    public String getName() {
        return name;
    }

    public String getDefaultFormat() {
        return defaultFormat;
    }

    public List<HolidayInitComponent> getHolidays() {
        if (holidays == null) {
            return Collections.EMPTY_LIST;
        }
        return holidays;
    }

    public Iterator<HolidayInitComponent> getHolidayIterator() {
        return getHolidays().iterator();
    }

    public List<MonthInitComponent> getMonths() {
        if (months == null) {
            return Collections.EMPTY_LIST;
        }
        return months;
    }

    public Iterator<MonthInitComponent> getMonthIterator() {
        return getMonths().iterator();
    }

    public List<SeasonInitComponent> getSeasons() {
        if (seasons == null) {
            return Collections.EMPTY_LIST;
        }
        return seasons;
    }

    public Iterator<SeasonInitComponent> getSeasonIterator() {
        return getSeasons().iterator();
    }

    public List<WeekdayInitComponent> getWeekdays() {
        if (weekdays == null) {
            return Collections.EMPTY_LIST;
        }
        return weekdays;
    }

    public Iterator<WeekdayInitComponent> getWeekdayIterator() {
        return getWeekdays().iterator();
    }
}
