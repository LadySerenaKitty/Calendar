package org.terasology.calendar;

import java.util.ArrayList;
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
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.chat.ChatMessageEvent;
import org.terasology.math.TeraMath;
import org.terasology.network.ClientComponent;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.world.WorldProvider;
import org.terasology.world.time.WorldTime;

@RegisterSystem()
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

    private EntityRef calendarEntity;
    private Prefab calendarPrefab;
    private CalendarComponent calendar;
    private List<HolidayComponent> holidays;
    private List<MonthComponent> months;
    private List<SeasonComponent> seasons;
    private List<WeekdayComponent> weekdays;

    // basic timekeeping
    int prevDay;
    int prevTick;

    @Override
    public void initialise() {

        worldTime = world.getTime();

        calendar = null;
        holidays = new ArrayList<>();
        months = new ArrayList<>();
        seasons = new ArrayList<>();
        weekdays = new ArrayList<>();

        prevDay = 0;
        prevTick = 0;
    }

    @Override
    public void preBegin() {
        calendarPrefab = prefabManager.getPrefab("Calendar");
    }

    @Override
    public void postBegin() {
        calendarEntity = entityManager.create(calendarPrefab);
        calendar = calendarEntity.getComponent(CalendarComponent.class);

        logger.info("Calendar: ".concat(calendar.getName()));

        if ( calendar.getHolidays().isEmpty() ) {
            logger.info("\tNo holidays.  How do the people on this world get extra days off?");
        } else {
            logger.info("\tHolidays: ".concat(String.valueOf(calendar.getHolidays().size())));
            calendar.getHolidayIterator().forEachRemaining((HolidayInitComponent c) -> {
                holidays.add(new HolidayComponent(c.startDay, c.startMonth, c.endDay, c.endMonth, c.name));
                logger.info("\t\t".concat(c.name));
            });
        }

        if ( calendar.getMonths().isEmpty() ) {
            logger.info("\tNo months.  That's no moon...");
        } else {
            logger.info("\tMonths: ".concat(String.valueOf(calendar.getMonths().size())));
            calendar.getMonthIterator().forEachRemaining((MonthInitComponent c) -> {
                months.add(new MonthComponent(c.month, c.shortName, c.mediumName, c.longName));
                logger.info("\t\t".concat(c.longName));
            });
        }

        if ( calendar.getSeasons().isEmpty() ) {
            logger.info("\tNo seasons.  Now that's what I call a stable climate!");
        } else {
            logger.info("\tSeasons: ".concat(String.valueOf(calendar.getSeasons().size())));
            calendar.getSeasonIterator().forEachRemaining((SeasonInitComponent c) -> {
                seasons.add(new SeasonComponent(c.startDay, c.startMonth, c.endDay, c.endMonth, c.name));
                logger.info("\t\t".concat(c.name));
            });
        }

        if ( calendar.getWeekdays().isEmpty() ) {
            logger.info("No weekdays.  So when do we party?");
        } else {
            logger.info("\tWeekdays: ".concat(String.valueOf(calendar.getWeekdays().size())));
            calendar.getWeekdayIterator().forEachRemaining((WeekdayInitComponent c) -> {
                weekdays.add(new WeekdayComponent(c.name, c.dayOfWeek));
                logger.info("\t\t".concat(c.name));
            });
        }

        calendarMath = new CalendarMath(weekdays.size(), calendar.getDaysPerMonth(), calendar.getDaysPerMonth() * months.size(), worldTime);
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

    private void broadcastCalendar() {
        String message = notifyClientString();
        for (EntityRef client : entityManager.getEntitiesWith(ClientComponent.class)) {
            client.iterateComponents().forEach((Component c) -> {
                if ( c instanceof ClientComponent ) {
                    client.send(new ChatMessageEvent(message, calendarEntity));
                }
            });
        }
    }

    private String notifyClientString() {
        StringBuilder sb = new StringBuilder("New day: ");
        sb.append(calendar.getWeekdays().get(calendarMath.getCurrentWeekDay()).name);
        sb.append(", ");
        sb.append(calendarMath.getCurrentMonthDay());
        sb.append(" ");
        sb.append(calendar.getMonths().get(calendarMath.getCurrentYearMonth()).longName );
        sb.append(" ");
        sb.append(calendarMath.getCurrentYear());

        return sb.toString();
    }

    @Override
    public void update(float delta) {
        int day = TeraMath.floorToInt(worldTime.getDays());
        int tick = TeraMath.floorToInt(worldTime.getDays() * 1000) % 1000;

        /*if ( day == prevDay ) {
            return;
        } // */

        if ( tick != 1 && tick != 999 ) {
            return;
        }

        if ( day != prevDay ) {
            calendarMath.updateToday();
            broadcastCalendar();
        }

        prevDay = day;
        prevTick = tick;


        int tDay = calendarMath.getCurrentMonthDay();
        int tMonth = calendarMath.getCurrentYearMonth();
        int tYear = calendarMath.getCurrentYear();

        DateComponent dc = new DateComponent(tDay, tMonth, calendarMath.getCurrentYear());
        WeekdayComponent wic = weekdays.get(calendarMath.getCurrentWeekDay());
        WeekdayComponent wc = new WeekdayComponent(wic.getName(), wic.getDayOfWeek());

        if ( tick == 1 ) {
            broadcastStarts(tDay, tMonth, tYear, dc, wc);
        } else if ( tick == 999 ) {
            broadcastEnds(tDay, tMonth, tYear, dc, wc);
        }
    }

    private void broadcastStarts(int tDay, int tMonth, int tYear, DateComponent dc, WeekdayComponent wc) {
        if ( calendarMath.isYearStart() ) {
            broadcastEvent(new OnYearEvent(tYear, tYear), dc, wc);
            broadcastEvent(new OnYearStart(tYear, tYear), dc, wc);
        }

        // broadcast the month
        if ( calendarMath.isMonthStart() ) {
            MonthComponent mic = months.get(tMonth);
            MonthComponent mc = new MonthComponent(tMonth, mic.getShortName(), mic.getMediumName(), mic.getLongName());
            broadcastEvent(new OnMonthEvent(tYear, tMonth), mc, dc, wc);
            broadcastEvent(new OnMonthStart(tYear, tYear), mc, dc, wc);
        }

        // broadcast the week (currently, only WeekType.GAME is implemented fully
        if ( calendarMath.isWeekStart() ) {
            broadcastEvent(new OnWeekEvent(tYear, calendarMath.getCurrentGameWeek()), dc, wc);
            broadcastEvent(new OnWeekStart(tYear, calendarMath.getCurrentGameWeek()), dc, wc);
        }

        // broadcast the season
        if ( !seasons.isEmpty() ) {
            seasons.iterator().forEachRemaining((SeasonComponent sic) ->{
                int startDay = sic.getStartDay();
                int startMonth = sic.getStartMonth();
                SeasonComponent sc = new SeasonComponent(startDay, startMonth, sic.getEndDay(), sic.getEndMonth(), sic.getName());

                if ( startDay == tDay && startMonth == tMonth ) {
                    broadcastEvent(new OnSeasonEvent(tYear, seasons.indexOf(sic)), sc, dc, wc);
                    broadcastEvent(new OnSeasonStart(tYear, seasons.indexOf(sic)), sc, dc, wc);
                }
            });
        }

        // broadcast any holidays
        if ( !holidays.isEmpty() ) {
            holidays.iterator().forEachRemaining((HolidayComponent hic) -> {
                int startDay = hic.getStartDay();
                int startMonth = hic.getStartMonth();
                HolidayComponent hc = new HolidayComponent(startDay, startMonth, hic.getEndDay(), hic.getEndMonth(), hic.getName());

                if ( startDay == tDay && startMonth == tMonth ) { // event start
                    broadcastEvent(new OnHolidayEvent(tYear, holidays.indexOf(hic)), hc, dc, wc);
                    broadcastEvent(new OnHolidayStart(tYear, holidays.indexOf(hic)), hc, dc, wc);
                }
            });
        }
    }

    private void broadcastEnds(int tDay, int tMonth, int tYear, DateComponent dc, WeekdayComponent wc) {
        if ( calendarMath.isYearEnd() ) {
            broadcastEvent(new OnYearEvent(tYear, tYear), dc, wc);
            broadcastEvent(new OnYearEnd(tYear, tYear), dc, wc);
        }

        // broadcast the month
        if ( calendarMath.isMonthEnd() ) {
            MonthComponent mic = months.get(tMonth);
            MonthComponent mc = new MonthComponent(tMonth, mic.getShortName(), mic.getMediumName(), mic.getLongName());
            broadcastEvent(new OnMonthEvent(tYear, tMonth), mc, dc, wc);
            broadcastEvent(new OnMonthEnd(tYear, tYear), mc, dc, wc);
        }

        // broadcast the week
        if ( calendarMath.isWeekEnd() ) {
            broadcastEvent(new OnWeekEvent(tYear, calendarMath.getCurrentGameWeek()), dc, wc);
            broadcastEvent(new OnWeekEnd(tYear, calendarMath.getCurrentGameWeek()), dc, wc);
        }

        // broadcast the season
        if ( !seasons.isEmpty() ) {
            seasons.iterator().forEachRemaining((SeasonComponent sic) ->{
                int endDay = sic.getEndDay();
                int endMonth = sic.getEndMonth();
                SeasonComponent sc = new SeasonComponent(sic.getStartDay(), sic.getStartMonth(), endDay, endMonth, sic.getName());

                if ( endDay == tDay && endMonth == tMonth ) {
                    broadcastEvent(new OnSeasonEvent(tYear, seasons.indexOf(sic)), sc, dc, wc);
                    broadcastEvent(new OnSeasonEnd(tYear, seasons.indexOf(sic)), sc, dc, wc);
                }
            });
        }

        // broadcast any holidays
        if ( !holidays.isEmpty() ) {
            holidays.iterator().forEachRemaining((HolidayComponent hic) -> {
                int endDay = hic.getEndDay();
                int endMonth = hic.getEndMonth();
                HolidayComponent hc = new HolidayComponent(hic.getStartDay(), hic.getStartMonth(), endDay, endMonth, hic.getName());

                if ( endDay == tDay && endMonth == tMonth ) { // event start
                    broadcastEvent(new OnHolidayEvent(tYear, holidays.indexOf(hic)), hc, dc, wc);
                    broadcastEvent(new OnHolidayEnd(tYear, holidays.indexOf(hic)), hc, dc, wc);
                }
            });
        }
    }

    private void broadcastEvent(Event evt, Component... comp) {
        EntityRef ent = entityManager.create();
        for ( Component c : comp ) {
            ent.addComponent(c);
        }
        ent.send(evt);
    }
}
