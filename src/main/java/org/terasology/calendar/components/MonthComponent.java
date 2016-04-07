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
import org.terasology.reflection.MappedContainer;

/**
 * Complete definition of a month.
 */
@MappedContainer
public class MonthComponent implements Component {

    /**
     * Month number of the year, starting at 1.
     */
    private int month;

    /**
     * Short name of the month (ex: Jan).
     */
    private String shortName;
    /**
     * Medium-length name of the month.
     */
    private String mediumName;
    /**
     * Long name of the month (ex: January).
     */
    private String longName;

    /**
     * Creates an empty month.
     */
    private MonthComponent() {
    }

    /**
     * Creates a month with all the specified information.
     * @param monthNumber Number of the month position in the year, starting at 1.
     * @param sName Short name of the month (ex: Jan).
     * @param mName Medium-length name of the month.
     * @param lName Long name of the month (ex: January).
     */
    public MonthComponent(int monthNumber, String sName, String mName, String lName) {
        month = monthNumber;

        shortName = sName;
        mediumName = mName;
        longName = lName;
    }

    /**
     * Gets the number of the month, starting at 1.
     * @return Number of month.
     */
   public int getMonth() {
        return month;
    }

   /**
    * Gets the name of the month.
    * @return Name of month.
    * @see #getLongName()
    */
    public String getName() {
        return longName;
    }

    /**
     * Gets the short name of the month (ex: Jan).
     * @return Short name of month.
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Gets the medium-length name of the month.
     * @return Medium-length name of month.
     */
    public String getMediumName() {
        return mediumName;
    }

    /**
     * Gets the long name of the month (ex: January).
     * @return Long name of month.
     */
    public String getLongName() {
        return longName;
    }
}
