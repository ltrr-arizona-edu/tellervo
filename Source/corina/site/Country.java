package corina.site;

import corina.gui.Bug;

import java.util.Properties;
import java.io.IOException;

// yes, this would be trivial in lisp.  don't laugh.
public class Country {
    // somebody suggested 2 maps -- good idea?
    private String code, name;
    private static Country countries[];
    private static int n;
    static {
        Properties blah = new Properties();
        try {
            ClassLoader cl = Class.forName("corina.site.properties").getClassLoader();
            blah.load(cl.getResource("countries.properties").openStream());
        } catch (Exception e) {
            Bug.bug(e); // can't happen
        }

        String keys[] = (String []) blah.keySet().toArray(new String[0]);
        n = keys.length;
        countries = new Country[n];
        for (int i=0; i<n; i++) {
            Country c = new Country();
            c.code = keys[i];
            c.name = blah.getProperty(keys[i]);
            countries[i] = c;
        }
    }
    
    // given a code, return its name
    public static String getName(String code) {
        for (int i=0; i<n; i++)
            if (countries[i].code.equals(code))
                return countries[i].name;
        throw new IllegalArgumentException();
    }

    // given a name, return its code
    public static String getCode(String name) {
        for (int i=0; i<n; i++)
            if (countries[i].name.equals(name))
                return countries[i].code;
        throw new IllegalArgumentException();
    }

    // return an array of all the names
    public static String[] getAllNames() {
        String result[] = new String[n];
        for (int i=0; i<n; i++)
            result[i] = countries[i].name;
        return result;
    }
}
