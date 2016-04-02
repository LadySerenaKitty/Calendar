package org.terasology.calendar;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.world.time.WorldTime;

@RegisterSystem
@Share(value = CalendarSystem.class)
public class CalendarSystem extends BaseComponentSystem {

    @In
    private WorldTime worldTime;

    @Override
    public void initialise() {
    }

    @Override
    public void preBegin() {
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

}
