package org.terasology.calendar.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.terasology.entitySystem.Component;

public class CalendarComponent implements Component {
    private int daysPerWeek;
    private String name;

    private List<HolidayComponent> holidays;
    private List<MonthComponent> months;
    private List<SeasonComponent> seasons;

    private CalendarComponent() {
        holidays = new ArrayList<>();
        months = new ArrayList<>();
        seasons = new ArrayList<>();
    }

    public CalendarComponent(int weekLength, String calendarName, List<HolidayComponent> holidayComponents, List<MonthComponent> monthComponents, List<SeasonComponent> seasonComponents) {
        daysPerWeek = weekLength;
        name = calendarName;

        holidays = holidayComponents;
        months = monthComponents;
        seasons = seasonComponents;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public String getName() {
        return name;
    }

    public List<HolidayComponent> getHolidays() {
        if ( seasons == null ) { return Collections.EMPTY_LIST; }
        return holidays;
    }

    public Iterator<HolidayComponent> getHolidayIterator() {
        return getHolidays().iterator();
    }

    public List<MonthComponent> getMonths() {
        if ( seasons == null ) { return Collections.EMPTY_LIST; }
        return months;
    }

    public Iterator<MonthComponent> getMonthIterator() {
        return getMonths().iterator();
    }

    public List<SeasonComponent> getSeasons() {
        if ( seasons == null ) { return Collections.EMPTY_LIST; }
        return seasons;
    }

    public Iterator<SeasonComponent> getSeasonIterator() {
        return getSeasons().iterator();
    }
}
