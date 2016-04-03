package org.terasology.calendar.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScanHelper<T> {

    public List<T> scanToList(Iterator<T> iterator)
    {
        List<T> list = new ArrayList<>();
        iterator.forEachRemaining((T t) -> {
            list.add(t);
        });
        return list;
    }
}
