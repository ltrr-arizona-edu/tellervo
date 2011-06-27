/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.ui;

import javax.swing.KeyStroke;

import edu.cornell.dendro.corina.platform.Platform;

public class RuntimeUtils {

    // if str contains source, return a new string with target instead of source (once!);
    // returns str if it doesn't contain source.  (think str ~= s/source/target/.)
    // (if source=="", returns target+source, concatenated)
    // this is like java 1.4's java.lang.String.replaceFirst()
    public static String substitute(String str, String source, String target) {
        int index = str.indexOf(source);
        if (index == -1) // not present
            return str;
        int start = index, end = index + source.length();
        return str.substring(0, start) + target + str.substring(end);
    }
    
    // generate a keystroke, substituting either "meta" (mac) or "control" (other) for "accel", if needed
    // -- just be careful, "accel X" works fine but "accel x" returns null (!! -- not my fault...)
    public static KeyStroke getKeyStroke(String str) {
        return KeyStroke.getKeyStroke(substitute(str, "accel", Platform.isMac() ? "meta" : "control"));
    }

    // this is why, even if i could, i shouldn't use serialization for widgets: they need to get
    // instantiated differently on different platforms, because they're going to end up different.
}
