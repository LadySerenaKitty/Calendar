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
package org.terasology.calendar.components;

import org.terasology.entitySystem.Component;

public class DateComponent implements Component {
    private int day;
    private int month;
    private int year;
    private int gameDay;

    private DateComponent() {
    }

    public DateComponent(int newDay, int newMonth, int newYear, int totalDay) {
        day = newDay;
        month = newMonth;
        year = newYear;
        gameDay = totalDay;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getGameDay() {
        return gameDay;
    }
}
