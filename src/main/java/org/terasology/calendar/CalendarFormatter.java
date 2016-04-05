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
package org.terasology.calendar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.terasology.calendar.components.CalendarComponent;
import org.terasology.calendar.components.DateComponent;

public class CalendarFormatter {

    private static CalendarComponent calendarComponent = null;
    private static CalendarMath calendarMath = null;
    private static CalendarMath mather = null;

    public static void init(CalendarComponent component, CalendarMath math) {
        if (calendarComponent == null) {
            calendarComponent = component;
        }

        if (calendarMath == null) {
            calendarMath = math;
            mather = math.dupicate();
        }
    }

    public static String formatDate() {
        return formatDate(calendarComponent.getDefaultFormat());
    }

    public static String formatDate(DateComponent date) {
        return formatDate(calendarComponent.getDefaultFormat(), date);
    }

    public static String formatDate(String formatString) {
        return formatDate(formatString,
                new DateComponent(calendarMath.getCurrentMonthDay(), calendarMath.getCurrentYearMonth(), calendarMath.getCurrentYear(), calendarMath.getCurrentDay()));
    }

    /** Converts a date to kitty-readable form, similar to {@link java.text.SimpleDateFormat}.
     * This mostly follows the same format, with a few exceptions.  Full list of pattern parts follows.
     *
     * <ul>
     *  <li>y - year</li>
     *  <li>M - month in year (M number, MM short, MMM medium, MMMM long)</li>
     *  <li>L - month in year (same as M)</li>
     *  <li>w - week in year (not implemented)</li>
     *  <li>W - week in month (not implemented)</li>
     *  <li>D - day in year</li>
     *  <li>d - day in month</li>
     *  <li>E - day name in week (E short, EE long)
     *  <li>u - day number of week (starts at 0, not 1)
     * </ul>
     *
     * @param formatString The string used to convert the date given.  Similar to {@link java.text.SimpleDateFormat}
     * @param date The date to process.
     * @return Kitty-readable date string.
     */
    public static String formatDate(String formatString, DateComponent date) {
        mather.updateToday(date.getGameDay());

        Pattern pattern = Pattern.compile("([a-zA-Z]+|[^a-zA-Z]+)");
        Matcher matcher = pattern.matcher(formatString);

        int a;
        String test;
        String check;
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            test = matcher.group();
            switch (test.substring(0, 1)) {
                case "y": sb.append(String.valueOf(date.getYear() + 1)); break;
                case "L":
                case "M":
                    switch (test.length()) {
                        case 4: sb.append(calendarComponent.getMonths().get(date.getMonth()).longName); break;
                        case 3: sb.append(calendarComponent.getMonths().get(date.getMonth()).mediumName); break;
                        case 2: sb.append(calendarComponent.getMonths().get(date.getMonth()).shortName); break;
                        default: sb.append(String.valueOf(date.getMonth())); break;
                    }
                    break;
                case "w":
                case "W": break;
                case "D": sb.append(String.valueOf(mather.getCurrentDay())); break;
                case "d": sb.append(String.valueOf(mather.getCurrentMonthDay())); break;
                case "E":
                    switch (test.length()) {
                        case 2: sb.append(calendarComponent.getWeekdays().get(mather.getCurrentWeekDay()).shortName); break;
                        default: sb.append(calendarComponent.getWeekdays().get(mather.getCurrentWeekDay()).name); break;
                    }
                    break;
                case "u": sb.append(String.valueOf(mather.getCurrentWeekDay())); break;
                default: sb.append(test);
            }
        }
        return sb.toString();
    }
}
