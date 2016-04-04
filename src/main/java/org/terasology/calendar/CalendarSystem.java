package org.terasology.calendar;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.calendar.components.CalendarComponent;
import org.terasology.calendar.components.HolidayComponent;
import org.terasology.calendar.components.MonthComponent;
import org.terasology.calendar.components.SeasonComponent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.world.WorldComponent;
import org.terasology.world.WorldProvider;
import org.terasology.world.sun.OnMidnightEvent;
import org.terasology.world.time.WorldTime;

@RegisterSystem
@Share(value = CalendarSystem.class)
public class CalendarSystem extends BaseComponentSystem {
    private static final Logger logger = LoggerFactory.getLogger(CalendarSystem.class);

    @In
    private WorldProvider world;

    private WorldTime worldTime;

    @In
    private PrefabManager prefabManager;

    private EntityRef entityRef;

    @In
    private EntityManager entityManager;

    private Prefab calendarPrefab;
    private CalendarComponent calendar;
    private List<HolidayComponent> holidays;
    private List<MonthComponent> months;
    private List<SeasonComponent> seasons;

    @Override
    public void initialise() {

        worldTime = world.getTime();
        entityRef = world.getWorldEntity();

        calendar = null;
        holidays = new ArrayList<>();
        months = new ArrayList<>();
        seasons = new ArrayList<>();
    }

    @Override
    public void preBegin() {
        calendarPrefab = prefabManager.getPrefab("calendar");
    }

    @Override
    public void postBegin() {
        calendar = entityManager.create(calendarPrefab).getComponent(CalendarComponent.class);

        logger.info("Calendar: ".concat(calendar.getName()));

        if ( calendar.getHolidays().isEmpty() ) {
            logger.info("\tNo holidays.  How do the people on this world get extra days off?");
        } else {
            calendar.getHolidayIterator().forEachRemaining((HolidayComponent c) -> {
                logger.info("\tHoliday: ".concat(c.getName()));
            });
        }

        if ( calendar.getMonths().isEmpty() ) {
            logger.info("\tNo months.  That's no moon...");
        } else {
            calendar.getMonthIterator().forEachRemaining((MonthComponent c) -> {
                logger.info("\tMonth: ".concat(c.getName()));
            });
        }

        if ( calendar.getSeasons().isEmpty() ) {
            logger.info("\tNo seasons.  Now that's what I call a stable climate!");
        } else {
            calendar.getSeasonIterator().forEachRemaining((SeasonComponent c) -> {
                logger.info("\tSeason: ".concat(c.getName()));
            });
        }

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

    @ReceiveEvent(components = WorldComponent.class)
    public void onMidnight(OnMidnightEvent event, EntityRef ref) {

    }

/*    @ReceiveEvent(components = CalendarComponent.class)
    public void onAddedCalendar(OnAddedComponent addedComponent, EntityRef ref) {
        logger.info("Calendar event: ".concat(addedComponent.getClass().getName()));
    }
    @ReceiveEvent(components = CalendarComponent.class)
    public void onAddedHoliday(OnAddedComponent addedComponent, EntityRef ref) {
        logger.info("Holiday event: ".concat(addedComponent.getClass().getName()));
    }
    @ReceiveEvent(components = CalendarComponent.class)
    public void onAddedMonth(OnAddedComponent addedComponent, EntityRef ref) {
        logger.info("Month event: ".concat(addedComponent.getClass().getName()));
    }
    @ReceiveEvent(components = CalendarComponent.class)
    public void onAddedSeason(OnAddedComponent addedComponent, EntityRef ref) {
        logger.info("Season event: ".concat(addedComponent.getClass().getName()));
    } // */
}
