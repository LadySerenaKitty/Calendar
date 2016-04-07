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
 * The main year event.  This is always fired in conjunction with {@link OnYearStart} or {@link OnYearEnd}.
 */
public class OnYearEvent extends OnCalendarEvent {

    /**
     * Creates the year event.
     * @param yearNumber The year number.
     * @param unitNumber Not used.
     */
    public OnYearEvent(int yearNumber, int unitNumber) {
        super(yearNumber, 0);
    }

    @Override
    public int getUnit() {
        return year;
    }
}
