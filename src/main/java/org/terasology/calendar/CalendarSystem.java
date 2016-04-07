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
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
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
            calendarMath.updateToday();
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

    public boolean isThingCurrent(CalendarMath cm, int startDay, int startMonth, int endDay, int endMonth) {
        int currentDay = cm.getCurrentMonthDay();
        int currentMonth = cm.getCurrentYearMonth();

        if (startMonth <= endMonth) {
            // within the year
            if (startMonth <= currentMonth && currentMonth <= endMonth) {
                if (startMonth < currentMonth && currentMonth < endMonth) {
                    // in a middle month
                    return true;
                } else if (startMonth == currentMonth && startDay <= currentDay) {
                    // in the starting month
                    return true;
                } else if (endMonth == currentMonth && currentDay <= endDay) {
                    // in the ending month
                    return true;
                }
            }
            return false;
        } else if (endMonth < startMonth) {
            // year crossing
            if (startMonth <= currentMonth) {
                if (startMonth == currentMonth && startDay <= currentDay) {
                    return true;
                } else if (startMonth < currentMonth) {
                    return true;
                }
            } else if (currentMonth <= endMonth) {
                if (endMonth == currentMonth && currentDay <= endDay) {
                    return true;
                } else if (currentMonth < endMonth) {
                    return true;
                }
            }
        }
        return false;
    }

    public int dayOf(int gameDayWhenStarted) {
        return gameDayWhenStarted - calendarMath.getCurrentDay() + 1;
    }

    public int dayOf(DateComponent date) {
        return dayOf(date.getGameDay());
    }

    public int dayOf(int startDay, int startMonth, int startYear) {
        int days = startYear * getDaysPerYear();
        days += startMonth * getDaysPerMonth();
        days += startDay;
        return dayOf(days);
    }

    public int currentHolidayDay(HolidayComponent holidayComponent) {
        boolean decrement = false;
        if (holidayComponent.getStartMonth() > currentDateComponent.getMonth()) {
            decrement = true;
        }
        return dayOf(holidayComponent.getStartDay(), holidayComponent.getStartMonth() - 1, calendarMath.getCurrentYear() - (decrement?1:0));
    }

    public int currentSeasonDay() {
        boolean decrement = false;
        if (currentSeasonComponent.getStartMonth() > currentDateComponent.getMonth()) {
            decrement = true;
        }
        return dayOf(currentSeasonComponent.getStartDay(), currentSeasonComponent.getStartMonth() - 1, calendarMath.getCurrentYear() - (decrement?1:0));
    }

    public CalendarMath getMath() {
        return calendarMath;
    }

    public String getDefaultFormat() {
        return calendar.getDefaultFormat();
    }

    public int getDaysPerWeek() {
        return weekdays.size();
    }

    public int getDaysPerMonth() {
        return calendar.getDaysPerMonth();
    }

    public int getDaysPerYear() {
        return calendar.getDaysPerMonth() * months.size();
    }

    public HolidayComponent getHoliday(int number) {
        if (holidays.isEmpty()) {
            return null;
        }
        return holidays.get(number);
    }

    public MonthComponent getMonth(int number) {
        if (months.isEmpty()) {
            return null;
        }
        return months.get(number);
    }

    public SeasonComponent getSeason(int number) {
        if (seasons.isEmpty()) {
            return null;
        }
        return seasons.get(number);
    }

    public WeekdayComponent getWeekday(int number) {
        if (weekdays.isEmpty()) {
            return null;
        }
        return weekdays.get(number);
    }

    public DateComponent getCurrentDate() {
        return getCurrentDate(false);
    }

    public List<HolidayComponent> getCurrentHolidays() {
        return getCurrentHolidays(false);
    }

    public MonthComponent getCurrentMonth() {
        return getCurrentMonth(false);
    }

    public SeasonComponent getCurrentSeason() {
        return getCurrentSeason(false);
    }

    public WeekdayComponent getCurrentWeekday() {
        return getCurrentWeekday(false);
    }

    public int getCalendarLength(int startDay, int startMonth, int endDay, int endMonth) {
        if (startMonth == endMonth) { // within the month
            return (endDay - startDay) + 1;
        } else if (startMonth < endMonth) { // within the same year
            return (calendar.getDaysPerMonth() * (endMonth - startMonth)) + (endDay - startDay) + 1;
        }
        else if (endMonth < startMonth) { // spans a year crossing
            return (calendar.getDaysPerMonth() * (startMonth - endMonth)) + (endDay - startDay) + 1;
        } else {
            return 1;
        }
    }

    private void update(boolean force) {
        currentDateComponent = getCurrentDate(force);
        currentHolidayComponents = getCurrentHolidays(force);
        currentMonthComponent = getCurrentMonth(force);
        currentSeasonComponent = getCurrentSeason(force);
        currentWeekdayComponent = getCurrentWeekday(force);
    }

    private DateComponent getCurrentDate(boolean force) {
        if (currentDateComponent != null && !force) {
            return currentDateComponent;
        }

        return new DateComponent(calendarMath.getCurrentMonthDay(), calendarMath.getCurrentYearMonth(), calendarMath.getCurrentYear(), calendarMath.getCurrentDay());
    }

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

            if (isThingCurrent(calendarMath, hc.getStartDay(), hc.getStartMonth(), hc.getEndDay(), hc.getEndMonth())) {
                list.add(hc);
            }
        }
        return list;
    }

    private MonthComponent getCurrentMonth(boolean force) {
        if (currentMonthComponent != null && !force) {
            return currentMonthComponent;
        }

        return months.get(calendarMath.getCurrentYearMonth());
    }

    private SeasonComponent getCurrentSeason(boolean force) {
        if (currentSeasonComponent != null && !force) {
            return currentSeasonComponent;
        }

        SeasonComponent sc;
        Iterator<SeasonComponent> it = seasons.iterator();
        while (it.hasNext()) {
            sc = it.next();

            if (isThingCurrent(calendarMath, sc.getStartDay(), sc.getStartMonth(), sc.getEndDay(), sc.getEndMonth())) {
                return sc;
            }
        }
        return null;
    }

    private WeekdayComponent getCurrentWeekday(boolean force) {
        if (currentMonthComponent != null && !force) {
            return currentWeekdayComponent;
        }

        return weekdays.get(calendarMath.getCurrentWeekDay());
    }

    private int getCalendarLength(HolidayInitComponent holidayInitComponent) {
        return getCalendarLength(holidayInitComponent.startDay, holidayInitComponent.startMonth, holidayInitComponent.endDay, holidayInitComponent.endMonth);
    }

    private int getCalendarLength(SeasonInitComponent seasonInitComponent) {
        return getCalendarLength(seasonInitComponent.startDay, seasonInitComponent.startMonth, seasonInitComponent.endDay, seasonInitComponent.endMonth);
    }
    private void broadcastStarts() {
        int tYear = calendarMath.getCurrentYear();
        int tMonth = calendarMath.getCurrentYearMonth();
        int tDay = calendarMath.getCurrentMonthDay();

        // broadcast the year
        if (calendarMath.isYearStart()) {
            broadcastEvent(new OnYearEvent(tYear, tYear), currentDateComponent, currentWeekdayComponent);
            broadcastEvent(new OnYearStart(tYear, tYear), currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the month
        if (calendarMath.isMonthStart()) {
            broadcastEvent(new OnMonthEvent(tYear, tMonth), currentMonthComponent, currentDateComponent, currentWeekdayComponent);
            broadcastEvent(new OnMonthStart(tYear, tMonth), currentMonthComponent, currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the week (currently, only WeekType.GAME is implemented fully
        if (calendarMath.isWeekStart()) {
            broadcastEvent(new OnWeekEvent(tYear, calendarMath.getCurrentGameWeek()), currentDateComponent, currentWeekdayComponent);
            broadcastEvent(new OnWeekStart(tYear, calendarMath.getCurrentGameWeek()), currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the day
        broadcastEvent(new OnCalendarEvent(tYear, calendarMath.getCurrentYearDay()), currentDateComponent, currentWeekdayComponent);

        // broadcast the season
        if (!seasons.isEmpty()) {
            if (currentSeasonComponent != null) {
                if (tDay == currentSeasonComponent.getStartDay() && tMonth == currentSeasonComponent.getStartMonth()) {
                    broadcastEvent(
                            new OnSeasonEvent(tYear, seasons.indexOf(currentSeasonComponent)), currentSeasonComponent, currentDateComponent, currentWeekdayComponent);
                    broadcastEvent(
                            new OnSeasonStart(tYear, seasons.indexOf(currentSeasonComponent)), currentSeasonComponent, currentDateComponent, currentWeekdayComponent);
                }
            }
        }

        // broadcast any holidays
        if (!currentHolidayComponents.isEmpty()) {
            currentHolidayComponents.iterator().forEachRemaining((HolidayComponent hc) -> {
                if (tDay == hc.getStartDay() && tMonth == hc.getStartMonth()) {
                    broadcastEvent(new OnHolidayEvent(tYear, holidays.indexOf(hc)), hc, currentDateComponent, currentWeekdayComponent);
                    broadcastEvent(new OnHolidayStart(tYear, holidays.indexOf(hc)), hc, currentDateComponent, currentWeekdayComponent);
                }
            });
        }
    }

    /**
     * Dispatches events upon day entry.
     */
    private void broadcastEnds() {
        int tYear = calendarMath.getCurrentYear();
        int tMonth = calendarMath.getCurrentYearMonth();
        int tDay = calendarMath.getCurrentMonthDay();

        // broadcast the year
        if (calendarMath.isYearEnd()) {
            broadcastEvent(new OnYearEvent(tYear, tYear), currentDateComponent, currentWeekdayComponent);
            broadcastEvent(new OnYearEnd(tYear, tYear), currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the month
        if (calendarMath.isMonthEnd()) {
            MonthComponent mic = months.get(tMonth);
            MonthComponent mc = new MonthComponent(tMonth, mic.getShortName(), mic.getMediumName(), mic.getLongName());
            broadcastEvent(new OnMonthEvent(tYear, tMonth), mc, currentDateComponent, currentWeekdayComponent);
            broadcastEvent(new OnMonthEnd(tYear, tYear), mc, currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the week
        if (calendarMath.isWeekEnd()) {
            broadcastEvent(new OnWeekEvent(tYear, calendarMath.getCurrentGameWeek()), currentDateComponent, currentWeekdayComponent);
            broadcastEvent(new OnWeekEnd(tYear, calendarMath.getCurrentGameWeek()), currentDateComponent, currentWeekdayComponent);
        }

        // broadcast the season
        if (!seasons.isEmpty()) {
            if (currentSeasonComponent != null) {
                if (tDay == currentSeasonComponent.getEndDay() && tMonth == currentSeasonComponent.getEndMonth()) {
                    broadcastEvent(
                            new OnSeasonEvent(tYear, seasons.indexOf(currentSeasonComponent)), currentSeasonComponent, currentDateComponent, currentWeekdayComponent);
                    broadcastEvent(
                            new OnSeasonEnd(tYear, seasons.indexOf(currentSeasonComponent)), currentSeasonComponent, currentDateComponent, currentWeekdayComponent);
                }
            };
        }

        // broadcast any holidays
        if (!currentHolidayComponents.isEmpty()) {
            currentHolidayComponents.iterator().forEachRemaining((HolidayComponent hc) ->{
                if (tDay == hc.getEndDay() && tMonth == hc.getEndMonth()) {
                    broadcastEvent(new OnHolidayEvent(tYear, holidays.indexOf(hc)), hc, currentDateComponent, currentWeekdayComponent);
                    broadcastEvent(new OnHolidayEnd(tYear, holidays.indexOf(hc)), hc, currentDateComponent, currentWeekdayComponent);
                }
            });
        }
    }

    /**
     * Dispatches an event for the chat system to receive.
     * @param evt The event to broadcast.
     * @param comp Components to broadcast with the event.
     */
    private void broadcastEvent(Event evt, Component... comp) {
        EntityRef ent = entityManager.create();
        for (Component c : comp) {
            ent.addComponent(c);
        }
        world.getWorldEntity().send(evt);
    }

    /**
     * Broadcasts a message with the new date.
     */
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
