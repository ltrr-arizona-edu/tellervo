// 
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina;

import corina.gui.Bug;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Enumeration;
import java.util.StringTokenizer;

// a complete list of all the species.
// the file came from web.utk.edu/~grissino/species.htm
// I hope I didn't murder it too bad in converting it to a text file

public class Species {
    public static Properties species = new Properties(); // code => name hash

    static {
        try {
            // load properties
            ClassLoader cl = Class.forName("corina.Species").getClassLoader();
            species.load(cl.getResource("species.properties").openStream());
        } catch (Exception e) {
            // can't happen
            Bug.bug(e); // move to its own class so the exception can be caught and it can be unit tested?
        }
    }

    public static String getCode(String s) throws UnknownSpeciesException {
        Enumeration e = species.keys();
        while (e.hasMoreElements()) {
            String maybe = (String) e.nextElement();
            if (stringAlmostEquals((String) species.get(maybe), s))
                return maybe;
        }
        throw new UnknownSpeciesException();
    }

    public static String getName(String c) throws UnknownSpeciesException {
        String r = (String) species.get(c);
        if (r == null)
            throw new UnknownSpeciesException();
        else
            return r;
    }

    public static List common = new ArrayList(); // list of strings, like ("PISP" "QUSP")

    static {
        // load most-common species
        String s = System.getProperty("corina.species.common", "PISP,PCSP,JUSP,QUSP,UNKN");

        StringTokenizer t = new StringTokenizer(s, ", ");
        while (t.hasMoreTokens())
            common.add(t.nextToken());
        // sums will need the same sort of routine: "QUSP,PISP" => { "QUSP", "PISP" } => "Quercus, Pinus"
    }

    // "string-almost-equals" algorithm.  good for finding typos.
    // isn't species-specific, but i only plan to use it here.
    // taken from: http://www.faqts.com/knowledge_base/view.phtml/aid/4418/fid/538
    // (originally in python)
    public static int stringDistance(String a, String b) {
        int n = a.length();
        int m = b.length();
        int c[][] = new int[n+1][m+1];

        for (int i=0; i<n+1; i++)
            c[i][0] = i;
        for (int j=0; j<m+1; j++)
            c[0][j] = j;

        for (int i=1; i<n+1; i++) {
            for (int j=1; j<m+1; j++) {
                int x = c[i-1][j] + 1;
                int y = c[i][j-1] + 1;
                int z = c[i-1][j-1];
                if (a.charAt(i-1) != b.charAt(j-1))
                    z++;
                c[i][j] = Math.min(Math.min(x, y), z);
            }
        }

        return c[n][m];
    }
    public static boolean stringAlmostEquals(String s1, String s2, int typosAllowed) {
        return (stringDistance(s1, s2) <= typosAllowed);
    }
    public static boolean stringAlmostEquals(String s1, String s2) {
        int distance = stringDistance(s1, s2);
        int minLength = Math.min(s1.length(), s2.length());
        return (distance / (double) minLength <= 0.20); // 80% ok?
    }

    // IDEA: this algorithm could be used to implement a findClosestMatch(String, String[]),
    // which could be quite powerful.
    // (it'd be a hell of a lot easier in lisp with #key, though.  what am i supposed to do,
    // use reflection for every member-access?)
}
