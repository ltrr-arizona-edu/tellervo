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
            if (((String) species.get(maybe)).equalsIgnoreCase(s))
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
}
