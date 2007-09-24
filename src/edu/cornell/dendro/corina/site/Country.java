package edu.cornell.dendro.corina.site;

import edu.cornell.dendro.corina.gui.Bug;

import java.util.Properties;
import java.io.IOException;

/**
   Class for converting between country codes (like "GR") and country
   names (like "Greece").

   <p>This uses ISO 3166-1, with the exception that Turkey is "TU",
   instead of "TR" - but "TU" isn't anything else, so it doesn't cause
   any problems internally.  (It might if another program expected
   pure ISO-3166-1.)</p>

   <p>On first use, it loads the file
   <code>countries.properties</code> (actually, just a resource in the
   Corina application's jar).  It is not (yet) localized.</p>
*/

// TODO: getName() is called a lot; it would probably be much faster (O(1) vs O(n))
// if it used a hash table.

// TODO: include small bitmaps of their flags, too; have a getFlag() method

// (yes, this would be trivial in lisp.  please don't laugh.)

public class Country {

    // somebody suggested 2 maps -- good idea?

    private String code, name;

    private static Country countries[];
    private static int n;

    static {
        Properties prop = new Properties();
        try {
            ClassLoader cl = edu.cornell.dendro.corina.site.Country.class.getClassLoader();
            prop.load(cl.getResource("edu/cornell/dendro/corina_resources/countries.properties").openStream());
        } catch (Exception e) {
            Bug.bug(e); // can't happen
        }

        String keys[] = (String []) prop.keySet().toArray(new String[0]);
        n = keys.length;
        countries = new Country[n];
        for (int i=0; i<n; i++) {
            Country c = new Country(keys[i], prop.getProperty(keys[i]));
            countries[i] = c;
        }
    }
    
    // make a new country, as a (code, name) tuple
    private Country(String code, String name) {
	this.code = code;
	this.name = name;
    }

    /**
       Given a country code, return its name.
     
       @param code the country code, like "GR"
       @return its name, like "Greece"
       @exception IllegalArgumentException if it's not a known country code
    */
    public static String getName(String code) {
        for (int i=0; i<n; i++)
            if (countries[i].code.equals(code))
                return countries[i].name;
        throw new IllegalArgumentException();
    }

    /**
       Given a country name, return its code.
       @param name the country name, like "Greece"
       @return its code, like "GR"
       @exception IllegalArgumentException if it's not a known country
       name
    */
    public static String getCode(String name) {
        for (int i=0; i<n; i++)
            if (countries[i].name.equals(name))
                return countries[i].code;
        throw new IllegalArgumentException();
    }

    /**
       Returns an array of all the country names.
       @return an array of all the country names
    */
    public static String[] getAllNames() {
        String result[] = new String[n];
        for (int i=0; i<n; i++)
            result[i] = countries[i].name;
        return result;
    }
    
    /*
     * Returns a false country name for a bad country code
     */
    public static String badCountry(String code) {
    	return "<unknown " + code + ">";
    }
    
    /*
     * Gets a bad country code from a false country name
     */
    public static String badCode(String country) {    	
    	if(!(country.startsWith("<unknown "))) {
    		return "<unknown '" + country + "'>";
    	}
    	    	
    	return country.substring(9, country.length() - 2);
    }
    
}
