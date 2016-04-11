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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.calendar.components.CalendarComponent;
import org.terasology.calendar.components.DateComponent;
import org.terasology.calendar.components.HolidayComponent;
import org.terasology.calendar.components.MonthComponent;
import org.terasology.calendar.components.SeasonComponent;
import org.terasology.calendar.components.WeekdayComponent;
import org.terasology.calendar.components.internal.HolidayInitComponent;
import org.terasology.calendar.components.internal.MonthInitComponent;
import org.terasology.calendar.components.internal.SeasonInitComponent;
import org.terasology.calendar.components.internal.WeekdayInitComponent;
import org.terasology.calendar.events.OnCalendarEvent;
import org.terasology.calendar.events.OnHolidayEnd;
import org.terasology.calendar.events.OnHolidayEvent;
import org.terasology.calendar.events.OnHolidayStart;
import org.terasology.calendar.events.OnMonthEnd;
import org.terasology.calendar.events.OnMonthEvent;
import org.terasology.calendar.events.OnMonthStart;
import org.terasology.calendar.events.OnSeasonEnd;
import org.terasology.calendar.events.OnSeasonEvent;
import org.terasology.calendar.events.OnSeasonStart;
import org.terasology.calendar.events.OnWeekEnd;
import org.terasology.calendar.events.OnWeekEvent;
import org.terasology.calendar.events.OnWeekStart;
import org.terasology.calendar.events.OnYearEnd;
import org.terasology.calendar.events.OnYearEvent;
import org.terasology.calendar.events.OnYearStart;
import org.terasology.calendar.util.BroadcastHelper;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.chat.ChatMessageEvent;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.math.TeraMath;
import org.terasology.network.ClientComponent;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.world.WorldProvider;
import org.terasology.world.time.WorldTime;

/** The Calendar System.
 * This system is responsible for tracking the years, months, weeks, and days from a provided world file.
 * Months are always the same length, but seasons and holidays can vary in length.
 */
@RegisterSystem
@Share(value = CalendarSystem.class)
public class CalendarSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    private static final Logger logger = LoggerFactory.getLogger(CalendarSystem.class);

    @In
    private WorldProvider world;

    private WorldTime worldTime;

    @In
    private PrefabManager prefabManager;

    @In
    private EntityManager entityManager;

    private CalendarMath calendarMath;
    private CalendarFormatter calendarFormatter;

    private EntityRef calendarEntity;
    private CalendarComponent calendar;
    private List<HolidayComponent> holidays;
    private List<MonthComponent> months;
    private List<SeasonComponent> seasons;
    private List<WeekdayComponent> weekdays;

    // basic timekeeping
    private int prevDay;
    private int prevTick;

    // better timekeeping
    private DateComponent currentDateComponent;
    private List<HolidayComponent> currentHolidayComponents;
    private MonthComponent currentMonthComponent;
    private SeasonComponent currentSeasonComponent;
    private WeekdayComponent currentWeekdayComponent;

    @Override
    public void initialise() {

        worldTime = world.getTime();

        calendar = null;
        holidays = new ArrayList<>();
        months = new ArrayList<>();
        seasons = new ArrayList<>();
        weekdays = new ArrayList<>();

        prevDay = -1;
        prevTick = 0;
    }

    @Override
    public void preBegin() {
    }

    @Override
    public void postBegin() {
        calendarEntity = entityManager.create(prefabManager.getPrefab("Calendar"));
        calendar = calendarEntity.getComponent(CalendarComponent.class);

        logger.info("Calendar: ".concat(calendar.getName()));

        if (calendar.getHolidays().isEmpty()) {
            logger.info("\tNo holidays.  How do the people on this world get extra days off?");
        } else {
            logger.info("\tHolidays: ".concat(String.valueOf(calendar.getHolidays().size())));
            calendar.getHolidayIterator().forEachRemaining((HolidayInitComponent c) -> {
                holidays.add(new HolidayComponent(c.startDay, c.startMonth, c.endDay, c.endMonth, getCalendarLength(c), c.name));
                logger.info("\t\t".concat(c.name));
            });
        }

        if (calendar.getMonths().isEmpty()) {
            logger.info("\tNo months.  That's no moon...");
        } else {
            logger.info("\tMonths: ".concat(String.valueOf(calendar.getMonths().size())));
            calendar.getMonthIterator().forEachRemaining((MonthInitComponent c) -> {
                months.add(new MonthComponent(c.month, c.shortName, c.mediumName, c.longName));
                logger.info("\t\t".concat(c.longName));
            });
        }

        if (calendar.getSeasons().isEmpty()) {
            logger.info("\tNo seasons.  Now that's what I call a stable climate!");
        } else {
            logger.info("\tSeasons: ".concat(String.valueOf(calendar.getSeasons().size())));
            calendar.getSeasonIterator().forEachRemaining((SeasonInitComponent c) -> {
                seasons.add(new SeasonComponent(c.startDay, c.startMonth, c.endDay, c.endMonth, getCalendarLength(c), c.name));
                logger.info("\t\t".concat(c.name));
            });
        }

        if (calendar.getWeekdays().isEmpty()) {
            logger.info("No weekdays.  So when do we party?");
        } else {
            logger.info("\tWeekdays: ".concat(String.valueOf(calendar.getWeekdays().size())));
            calendar.getWeekdayIterator().forEachRemaining((WeekdayInitComponent c) -> {
                weekdays.add(new WeekdayComponent(c.name, c.shortName, c.dayOfWeek));
                logger.info("\t\t".concat(c.name));
            });
        }

        calendarMath = new CalendarMath(weekdays.size(), calendar.getDaysPerMonth(), calendar.getDaysPerMonth() * months.size(), worldTime);
        calendarMath.updateToday();

        calendarFormatter = new CalendarFormatter(calendar, calendarMath);
        update(true);

        // client info (needed for chat)
        DisplayNameComponent dnc = new DisplayNameComponent();
        dnc.name = "Calendar (".concat(calendar.getName()).concat(")");
        dnc.description = "Calendar System";
        calendarEntity.addComponent(dnc);
    }

    @Override
    public void preSave() {
    }

    @Override
    public void postSave() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void update(float delta) {
        int day = TeraMath.floorToInt(worldTime.getDays());
        int tick = TeraMath.floorToInt(worldTime.getDays() * 1000) % 1000;

        /*if (day == prevDay) {
            return;
        } // */

        if (tick != 1 && tick != 999) {
            return;
        } else if (tick == prevTick) {
            return;
        }
        prevTick = tick;

        if (tick == 1 && day != prevDay) {
            calendarMath.updateToday(day);
            update(true);
            broadcastCalendar();
        }
        prevDay = day;

        if (tick == 1) {
            broadcastStarts();
        } else if (tick == 999) {
            broadcastEnds();
        }
    }

    /**
     * The current day of a holiday.
     * @param holidayComponent The {@link HolidayComponent} to get the day of.
     * @return The day of the specified holiday, for example: day 1 of fiesta week
     */
    public int currentHolidayDay(HolidayComponent holidayComponent) {
        boolean decrement = false;
        if (holidayComponent.getStartMonth() > currentDateComponent.getMonth()) {
            decrement = true;
        }
        //return calendarMath.dayOf(holidayComponent.getStartDay() - 1, holidayComponent.getStartMonth() - 1, calendarMath.getCurrentYear() - (decrement ? 1 : 0));
        return calendarMath.getCalendarLength(holidayComponent.getStartDay(), holidayComponent.getStartMonth(),
                calendarMath.getCurrentMonthDay() + 1, calendarMath.getCurrentYearMonth() + 1);
    }

    /**
     * The current day of the current season.
     * @return The current day of the current season, for example: day 1 of summer
     */
    public int currentSeasonDay() {
        boolean decrement = false;
        if (currentSeasonComponent.getStartMonth() > currentDateComponent.getMonth()) {
            decrement = true;
        }
        return calendarMath.dayOf(currentSeasonComponent.getStartDay() - 1, currentSeasonComponent.getStartMonth() - 1, calendarMath.getCurrentYear() - (decrement ? 1 : 0));
        //return calendarMath.getCalendarLength(currentSeasonComponent.getStartDay(), currentSeasonComponent.getStartMonth(),
        //        calendarMath.getCurrentMonthDay() + 1, calendarMath.getCurrentYearMonth() + 1);
    }

    /**
     * Get the calendar math object.
     * @return Duplicate of the {@link CalendarMath} object.
     */
    public CalendarMath getMath() {
        return calendarMath.dupicate();
    }

    /**
     * Get the default date format specified by the calendar configuration.
     * @return The default date format.
     * @see CalendarFormatter#formatDate(String, DateComponent)
     */
    public String getDefaultFormat() {
        return calendar.getDefaultFormat();
    }

    /**
     * Get the number of days per week.
     * @return Days per week.
     */
    public int getDaysPerWeek() {
        return weekdays.size();
    }

    /**
     * Get the number of days per month.
     * @return Days per month.
     */
    public int getDaysPerMonth() {
        return calendar.getDaysPerMonth();
    }

    /**
     * Get the number of days per year.
     * @return Days per year.
     */
    public int getDaysPerYear() {
        return calendar.getDaysPerMonth() * months.size();
    }

    /**
     * Retrieve a holiday by its index number.
     * @param number Index number.
     * @return The {@link HolidayComponent} at the specified index.
     */
    public HolidayComponent getHoliday(int number) {
        if (holidays.isEmpty()) {
            return null;
        }
        return holidays.get(number);
    }

    /**
     * Retrieve a month by its index number.
     * @param number Index number.
     * @return The {@link MonthComponent} at the specified index.
     */
    public MonthComponent getMonth(int number) {
        if (months.isEmpty()) {
            return null;
        }
        return months.get(number);
    }

    /**
     * Retrieve a season by its index number.
     * @param number Index number.
     * @return The {@link SeasonComponent} at the specified index.
     */
    public SeasonComponent getSeason(int number) {
        if (seasons.isEmpty()) {
            return null;
        }
        return seasons.get(number);
    }

    /**
     * Retrieve a holiday by its index number.
     * @param number Index number.
     * @return The {@link HolidayComponent} at the specified index.
     */
    public WeekdayComponent getWeekday(int number) {
        if (weekdays.isEmpty()) {
            return null;
        }
        return weekdays.get(number);
    }

    /**
     * Retrieves the current date.
     * @return The current date.
     */
    public DateComponent getCurrentDate() {
        return getCurrentDate(false);
    }

    /**
     * Retrieves a list of all holidays currently in effect.
     * @return List of holidays in effect.
     */
    public List<HolidayComponent> getCurrentHolidays() {
        return getCurrentHolidays(false);
    }

    /**
     * Retrieves the current month.
     * @return Current month.
     */
    public MonthComponent getCurrentMonth() {
        return getCurrentMonth(false);
    }

    /**
     * Retrieves the current season.
     * @return Current season.
     */
    public SeasonComponent getCurrentSeason() {
        return getCurrentSeason(false);
    }

    /**
     * Retrieves the current weekday.
     * @return Current weekday.
     */
    public WeekdayComponent getCurrentWeekday() {
        return getCurrentWeekday(false);
    }

    /**
     * Updates the stored values pertaining to the current date.
     * @param force if {@code true}, forces an update
     */
    private void update(boolean force) {
        currentDateComponent = getCurrentDate(force);
        currentHolidayComponents = getCurrentHolidays(force);
        currentMonthComponent = getCurrentMonth(force);
        currentSeasonComponent = getCurrentSeason(force);
        currentWeekdayComponent = getCurrentWeekday(force);
    }

    /**
     * Retrieves the current date, or generates a new date if the day has changed.
     * @param force If {@code true}, a new date is generated.
     * @return The retrieved or generated date.
     */
    private DateComponent getCurrentDate(boolean force) {
        if (currentDateComponent != null && !force) {
            return currentDateComponent;
        }

        return new DateComponent(calendarMath.getCurrentMonthDay(), calendarMath.getCurrentYearMonth(), calendarMath.getCurrentYear(), calendarMath.getCurrentDay());
    }

    /**
     * Retrieves the current list of active holidays, or generates a new list if the active holidays have changed.
     * @param force If {@code true}, a new list of active holidays is generated.
     * @return The retrieved or generated list of active holidays.
     */
    private List<HolidayComponent> getCurrentHolidays(boolean force) {
        if (holidays.isEmpty()) {
            return new ArrayList<>();
        } else if (!currentHolidayComponents.isEmpty() && !force) {
            return currentHolidayComponents;
        }

        List<HolidayComponent> list = new ArrayList<>();
        HolidayComponent hc;
        Iterator<HolidayComponent> it = holidays.iterator();
        while (it.hasNext()) {
            hc = it.next();

            if (calendarMath.isThingCurrent(hc.getStartDay(), hc.getStartMonth(), hc.getEndDay(), hc.getEndMonth())) {
                list.add(hc);
            }
        }
        return list;
    }

    /**
     * Retrieves the current month, or generates a new month if the month has changed.
     * @param force If {@code true}, a new month is generated.
     * @return The retrieved or generated month.
     */
    private MonthComponent getCurrentMonth(boolean force) {
        if (currentMonthComponent != null && !force) {
            return currentMonthComponent;
        }

        return months.get(calendarMath.getCurrentYearMonth());
    }

    /**
     * Retrieves the current season, or generates a new season if the season has changed.
     * @param force If {@code true}, a new season is generated.
     * @return The retrieved or generated season.
     */
    private SeasonComponent getCurrentSeason(boolean force) {
        if (currentSeasonComponent != null && !force) {
            return currentSeasonComponent;
        }

        SeasonComponent sc;
        Iterator<SeasonComponent> it = seasons.iterator();
        while (it.hasNext()) {
            sc = it.next();

            logger.info(String.format("getCurrentSeason: %s\tsd: %s\tsm: %s\ted: %s\tem: %s", sc.getName(),
                    sc.getStartDay(), sc.getStartMonth(), sc.getEndDay(), sc.getEndMonth()));
            if (calendarMath.isThingCurrent(sc.getStartDay(), sc.getStartMonth(), sc.getEndDay(), sc.getEndMonth())) {
                return sc;
            }
        }
        return null;
    }

    /**
     * Retrieves the current weekday, or generates a new weekday if the day has changed.
     * @param force If {@code true}, a new weekday is generated.
     * @return The retrieved or generated weekday.
     */
    private WeekdayComponent getCurrentWeekday(boolean force) {
        if (currentMonthComponent != null && !force) {
            return currentWeekdayComponent;
        }

        return weekdays.get(calendarMath.getCurrentWeekDay());
    }

    /**
     * Calculates the length of a holiday.  Used only during Calendar setup.
     * @param holidayInitComponent The holiday to calculate the length of.
     * @return Length in days of the specified holiday.
     */
    private int getCalendarLength(HolidayInitComponent holidayInitComponent) {
        return calendarMath.getCalendarLength(holidayInitComponent.startDay, holidayInitComponent.startMonth, holidayInitComponent.endDay, holidayInitComponent.endMonth);
    }

    /**
     * Calculates the length of a season.  Used only during Calendar setup.
     * @param seasonInitComponent The season to calculate the length of.
     * @return Length in days of the specified season.
     */
    private int getCalendarLength(SeasonInitComponent seasonInitComponent) {
        return calendarMath.getCalendarLength(seasonInitComponent.startDay, seasonInitComponent.startMonth, seasonInitComponent.endDay, seasonInitComponent.endMonth);
    }

    /**
     * Dispatches events upon day entry.
     */
    private void broadcastStarts() {
        int tYear = calendarMath.getCurrentYear();
        int tMonth = calendarMath.getCurrentYearMonth();
        int tDay = calendarMath.getCurrentMonthDay();
        BroadcastHelper bh = new BroadcastHelper(entityManager, world);

        // broadcast the year
        if (calendarMath.isYearStart()) {
            bh.broadcastEvent(new OnYearEvent(tYear, tYear), currentDateComponent, currentWeekdayComponent);
            bh.broadcastEvent(new OnYearStart(tYear, tYear), currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the month
        if (calendarMath.isMonthStart()) {
            bh.broadcastEvent(new OnMonthEvent(tYear, tMonth), currentMonthComponent, currentDateComponent, currentWeekdayComponent);
            bh.broadcastEvent(new OnMonthStart(tYear, tMonth), currentMonthComponent, currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the week (currently, only WeekType.GAME is implemented fully
        if (calendarMath.isWeekStart()) {
            bh.broadcastEvent(new OnWeekEvent(tYear, calendarMath.getCurrentGameWeek()), currentDateComponent, currentWeekdayComponent);
            bh.broadcastEvent(new OnWeekStart(tYear, calendarMath.getCurrentGameWeek()), currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the day
        bh.broadcastEvent(new OnCalendarEvent(tYear, calendarMath.getCurrentYearDay()), currentDateComponent, currentWeekdayComponent);

        // broadcast the season
        if (currentSeasonComponent != null) {
            if (tDay == currentSeasonComponent.getStartDay() && tMonth == currentSeasonComponent.getStartMonth()) {
                bh.broadcastEvent(new OnSeasonEvent(tYear, seasons.indexOf(currentSeasonComponent)), currentSeasonComponent, currentDateComponent, currentWeekdayComponent);
                bh.broadcastEvent(new OnSeasonStart(tYear, seasons.indexOf(currentSeasonComponent)), currentSeasonComponent, currentDateComponent, currentWeekdayComponent);
            }
        }

        // broadcast any holidays
        if (!currentHolidayComponents.isEmpty()) {
            currentHolidayComponents.iterator().forEachRemaining((HolidayComponent hc) -> {
                if (tDay == hc.getStartDay() && tMonth == hc.getStartMonth()) {
                    bh.broadcastEvent(new OnHolidayEvent(tYear, holidays.indexOf(hc)), hc, currentDateComponent, currentWeekdayComponent);
                    bh.broadcastEvent(new OnHolidayStart(tYear, holidays.indexOf(hc)), hc, currentDateComponent, currentWeekdayComponent);
                }
            });
        }
    }

    /**
     * Dispatches events upon day exit.
     */
    private void broadcastEnds() {
        int tYear = calendarMath.getCurrentYear();
        int tMonth = calendarMath.getCurrentYearMonth();
        int tDay = calendarMath.getCurrentMonthDay();
        BroadcastHelper bh = new BroadcastHelper(entityManager, world);

        // broadcast the year
        if (calendarMath.isYearEnd()) {
            bh.broadcastEvent(new OnYearEvent(tYear, tYear), currentDateComponent, currentWeekdayComponent);
            bh.broadcastEvent(new OnYearEnd(tYear, tYear), currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the month
        if (calendarMath.isMonthEnd()) {
            bh.broadcastEvent(new OnMonthEvent(tYear, tMonth), currentMonthComponent, currentDateComponent, currentWeekdayComponent);
            bh.broadcastEvent(new OnMonthEnd(tYear, tYear), currentMonthComponent, currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the week
        if (calendarMath.isWeekEnd()) {
            bh.broadcastEvent(new OnWeekEvent(tYear, calendarMath.getCurrentGameWeek()), currentDateComponent, currentWeekdayComponent);
            bh.broadcastEvent(new OnWeekEnd(tYear, calendarMath.getCurrentGameWeek()), currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the season
        if (currentSeasonComponent != null) {
            if (tDay == currentSeasonComponent.getEndDay() && tMonth == currentSeasonComponent.getEndMonth()) {
                bh.broadcastEvent(new OnSeasonEvent(tYear, seasons.indexOf(currentSeasonComponent)), currentSeasonComponent, currentDateComponent, currentWeekdayComponent);
                bh.broadcastEvent(new OnSeasonEnd(tYear, seasons.indexOf(currentSeasonComponent)), currentSeasonComponent, currentDateComponent, currentWeekdayComponent);
            }
        }

        // broadcast any holidays
        if (!currentHolidayComponents.isEmpty()) {
            currentHolidayComponents.iterator().forEachRemaining((HolidayComponent hc) -> {
                if (tDay == hc.getEndDay() && tMonth == hc.getEndMonth()) {
                    bh.broadcastEvent(new OnHolidayEvent(tYear, holidays.indexOf(hc)), hc, currentDateComponent, currentWeekdayComponent);
                    bh.broadcastEvent(new OnHolidayEnd(tYear, holidays.indexOf(hc)), hc, currentDateComponent, currentWeekdayComponent);
                }
            });
        }
    }

    /**
     * Broadcasts a message with the new date.
     */
    @SuppressWarnings("unchecked")
    private void broadcastCalendar() {
        String message = notifyClientString();
        for (EntityRef client : entityManager.getEntitiesWith(ClientComponent.class)) {
            client.iterateComponents().forEach((Component c) -> {
                if (c instanceof ClientComponent) {
                    client.send(new ChatMessageEvent(message, calendarEntity));
                }
            });
        }
    }

    /**
     * Generates the date string used to notify when a new day has arrived.
     * @return Person-readable date string.
     */
    private String notifyClientString() {
        StringBuilder sb = new StringBuilder("New day: ");
        sb.append(calendarFormatter.formatDate());
        return sb.toString();
    }
}
