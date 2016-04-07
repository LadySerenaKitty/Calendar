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
 * The month end event.  This is fired at the end of the day when a month ends.
 */
public class OnMonthEnd extends OnMonthEvent {

    /**
     * Creates the month event.
     * @param yearNumber The year in which the month is ending.
     * @param monthNumber  The index number of the month.
     */
    public OnMonthEnd(int yearNumber, int monthNumber) {
        super(yearNumber, monthNumber);
    }

}
