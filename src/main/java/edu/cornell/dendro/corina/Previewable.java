/*******************************************************************************
 * Copyright (C) 2002-2011 Ken Harris and Peter Brewer.
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
 *     Ken Harris - initial implementation
 *     Lucas Madar 
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina;

/**
   <p>Interface for objects that can display a preview of themselves.
   These are shown, for example, in a file-chooser dialog or other
   file browser.</p>

   @see edu.cornell.dendro.corina.Preview
   @see edu.cornell.dendro.corina.gui.PreviewComponent

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public interface Previewable {

    /** Create a Preview for this Object.  Usually (but not necessarily) called once per file.
	@return a Preview for this object */
    public Preview getPreview();
}
