/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
package edu.cornell.dendro.corina.gis;

import gov.nasa.worldwind.examples.AnnotationControls.AppFrame;
import gov.nasa.worldwind.examples.AnnotationControls.ContentAnnotation;
import gov.nasa.worldwind.examples.util.AudioPlayerAnnotation;
import gov.nasa.worldwind.examples.util.AudioPlayerAnnotationController;

public class TridasContentAnnotation extends ContentAnnotation {

    protected Object source;
	
	public TridasContentAnnotation(AppFrame appFrame, AudioPlayerAnnotation annnotation,
            AudioPlayerAnnotationController controller, Object source) {
       
		super(appFrame, annnotation, controller);
        this.source = source;


	}

}
