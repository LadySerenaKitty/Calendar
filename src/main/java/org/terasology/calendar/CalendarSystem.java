package org.terasology.calendar;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.calendar.components.CalendarComponent;
import org.terasology.calendar.components.HolidayComponent;
import org.terasology.calendar.components.MonthComponent;
import org.terasology.calendar.components.SeasonComponent;
import org.terasology.calendar.components.WeekdayComponent;
import org.terasology.calendar.components.internal.HolidayInitComponent;
import org.terasology.calendar.components.internal.MonthInitComponent;
import org.terasology.calendar.components.internal.SeasonInitComponent;
import org.terasology.calendar.components.internal.WeekdayInitComponent;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
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

    private EntityRef entityRef;

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

    @Override
    public void initialise() {

        worldTime = world.getTime();
        entityRef = world.getWorldEntity();

        calendar = null;
        holidays = new ArrayList<>();
        months = new ArrayList<>();
        seasons = new ArrayList<>();
        weekdays = new ArrayList<>();

        prevDay = 0;
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
                logger.info("\t\t".concat(c.name));
            });
        }

        if ( calendar.getMonths().isEmpty() ) {
            logger.info("\tNo months.  That's no moon...");
        } else {
            logger.info("\tMonths: ".concat(String.valueOf(calendar.getMonths().size())));
            calendar.getMonthIterator().forEachRemaining((MonthInitComponent c) -> {
                logger.info("\t\t".concat(c.longName));
            });
        }

        if ( calendar.getSeasons().isEmpty() ) {
            logger.info("\tNo seasons.  Now that's what I call a stable climate!");
        } else {
            logger.info("\tSeasons: ".concat(String.valueOf(calendar.getSeasons().size())));
            calendar.getSeasonIterator().forEachRemaining((SeasonInitComponent c) -> {
                logger.info("\t\t".concat(c.name));
            });
        }

        if ( calendar.getWeekdays().isEmpty() ) {
            logger.info("No weekdays.  So when do we party?");
        } else {
            logger.info("\tWeekdays: ".concat(String.valueOf(calendar.getWeekdays().size())));
            calendar.getWeekdayIterator().forEachRemaining((WeekdayInitComponent c) -> {
                logger.info("\t\t".concat(c.name));
            });
        }

        calendarMath = new CalendarMath(calendar.getWeekdays().size(), calendar.getDaysPerMonth(), calendar.getDaysPerMonth() * calendar.getMonths().size(), worldTime);
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

    private EntityRef newCalenderEntity() {
        EntityRef ref = entityManager.create("Client");
        return ref;
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

        if ( day != prevDay ) {
            prevDay = day;
            calendarMath.updateToday();
            broadcastCalendar();
        }
    }
}
