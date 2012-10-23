/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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
 *     Peter Brewer
 ******************************************************************************/

package org.tellervo.desktop.io;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
   A file load operation was attempted with the wrong format loader.
   (Or, from the Sample(filename) constructor if no usable loader was
   found.)

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
@SuppressWarnings("serial")
public class WrongFiletypeException extends IOException {
	private final static Logger log = LoggerFactory.getLogger(WrongFiletypeException.class);

    /** Make a new wrong filetype exception. */
    public WrongFiletypeException() {
        // nothing needed (this only exists for the javadoc tag)
    }
    
    public WrongFiletypeException(String s) {
    	// For now, just dump debug info
    	log.error(s);
    }
}
