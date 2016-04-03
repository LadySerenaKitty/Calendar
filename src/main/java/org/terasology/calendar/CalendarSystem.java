package org.terasology.calendar;

import java.util.ArrayList;
import java.util.List;
import org.terasology.calendar.data.components.CalendarComponent;
import org.terasology.calendar.data.components.HolidayComponent;
import org.terasology.calendar.data.components.MonthComponent;
import org.terasology.calendar.data.components.SeasonComponent;
import org.terasology.entitySystem.Component;
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

    @In
    private WorldProvider world;

    @In
    private WorldTime worldTime;

    @In
    private PrefabManager prefabManager;

    @In
    private EntityRef entityRef;

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
        calendar = entityRef.getComponent(CalendarComponent.class);
        Prefab prefab = prefabManager.getPrefab("calendar");

        if ( prefab.hasComponent(HolidayComponent.class) ) {
            prefab.iterateComponents().forEach((Component t) -> {
                    if ( t instanceof HolidayComponent ) {
                        holidays.add( (HolidayComponent)t );
                    }
                }
            );
        }
        if ( prefab.hasComponent(MonthComponent.class) ) {
            prefab.iterateComponents().forEach((Component t) -> {
                    if ( t instanceof MonthComponent ) {
                        months.add( (MonthComponent)t );
                    }
                }
            );
        }
        if ( prefab.hasComponent(SeasonComponent.class) ) {
            prefab.iterateComponents().forEach((Component t) -> {
                    if ( t instanceof SeasonComponent ) {
                        seasons.add( (SeasonComponent)t );
                    }
                }
            );
        }
    }

    @Override
    public void postBegin() {
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
}
