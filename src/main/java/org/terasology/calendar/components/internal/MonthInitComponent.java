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

@MappedContainer
public class MonthInitComponent implements Component {
    public int month;

    public String shortName;
    public String mediumName;
    public String longName;

    public MonthInitComponent() {
    }

    public MonthInitComponent(int monthNumber, String sName, String mName, String lName) {
        month = monthNumber;

        shortName = sName;
        mediumName = mName;
        longName = lName;
    }
}
