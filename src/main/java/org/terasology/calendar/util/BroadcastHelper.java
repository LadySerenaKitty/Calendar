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
package org.terasology.calendar.util;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.world.WorldProvider;

/**
 *
 */
public class BroadcastHelper {

    private EntityManager entityManager;
    private WorldProvider world;

    public BroadcastHelper(EntityManager broadcastEntityManager, WorldProvider broadcastWorld) {
        entityManager = broadcastEntityManager;
        world = broadcastWorld;
    }

    public static void broadcastEvent(EntityManager entityManager, WorldProvider worldProvider, Event evt, Component... comp) {
        BroadcastHelper bh = new BroadcastHelper(entityManager, worldProvider);
        bh.broadcastEvent(evt, comp);
    }

    /**
     * Dispatches an event for the chat system to receive.
     * @param evt The event to broadcast.
     * @param comp Components to broadcast with the event.
     */
    public void broadcastEvent(Event evt, Component... comp) {
        EntityRef ent = entityManager.create();
        for (Component c : comp) {
            ent.addComponent(c);
        }
        world.getWorldEntity().send(evt);
    }
}
