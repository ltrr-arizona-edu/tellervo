package corina.util;

import java.lang.reflect.Field;

import java.util.Comparator;
import java.util.Collections;
import java.util.List;

// a wrapper for collections.sort() that sucks less.
public class Sort {

    // sort, lowest-to-highest
    public static void sort(List data, String fieldName) throws IllegalArgumentException {
        sort(data, fieldName, false);
    }

    // sort data by field
    public static void sort(List data, String fieldName, boolean decreasing) throws IllegalArgumentException {
        try {
            // possible bug: if data contains different classes of objects, data[0].class
            // might be the declarer of field fieldname -- should i call
            // -- f = f.getDeclaringClass().getDeclaredField(fieldName)?
            Class c = data.get(0).getClass();
            final Field f = c.getDeclaredField(fieldName); // final because my anonymous class needs it

            final boolean reverse = decreasing;

            Collections.sort(data, new Comparator() {
                public int compare(Object o1, Object o2) {
                    try {
                        Comparable v1 = (Comparable) f.get(o1);
                        Comparable v2 = (Comparable) f.get(o2);
                        int x = v1.compareTo(v2); // bug: trouble here if v1 (or maybe v2) is null
                        return (reverse ? -x : x);
                    } catch (IllegalAccessException iae) {
                        // gah, nothing i can do here.
                        throw new IllegalArgumentException("no access to field! -- " + iae);
                    }
                }
            });
        } catch (NoSuchFieldException nsfe) {
            throw new IllegalArgumentException("no such field! --" + nsfe);
        }
    }
}
