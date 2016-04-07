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
package org.terasology.calendar.components.internal;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

/**
 * Definition of a month.
 */
@MappedContainer
public class MonthInitComponent implements Component {

    /**
     * Month number of the year, starting at 1.
     */
    public int month;

    /**
     * Short name of the month (ex: Jan).
     */
    public String shortName;
    /**
     * Medium-length name of the month.
     */
    public String mediumName;
    /**
     * Long name of the month (ex: January).
     */
    public String longName;

    /**
     * Creates an empty month.
     */
    public MonthInitComponent() {
    }

    /**
     * Creates a month with all the specified information.
     * @param monthNumber Number of the month position in the year, starting at 1.
     * @param sName Short name of the month (ex: Jan).
     * @param mName Medium-length name of the month.
     * @param lName Long name of the month (ex: January).
     */
    public MonthInitComponent(int monthNumber, String sName, String mName, String lName) {
        month = monthNumber;

        shortName = sName;
        mediumName = mName;
        longName = lName;
    }
}
