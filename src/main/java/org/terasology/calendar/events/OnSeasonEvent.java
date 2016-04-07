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
package org.terasology.calendar.events;

/**
 * The main season event.  This is always fired in conjunction with {@link OnSeasonStart} or {@link OnSeasonEnd}.
 */
public class OnSeasonEvent extends OnCalendarEvent {

    /**
     * Creates the season event.
     * @param yearNumber The year in which the season is occurring.
     * @param unitNumber The index number of the season.
     */
    public OnSeasonEvent(int yearNumber, int unitNumber) {
        super(yearNumber, unitNumber);
    }
}
