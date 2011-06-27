/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.bulkImport.control;

import com.dmurph.mvc.control.FrontController;

import edu.cornell.dendro.corina.bulkImport.command.ColumnAddedCommand;
import edu.cornell.dendro.corina.bulkImport.command.ColumnRemovedCommand;

/**
 * @author daniel
 *
 */
public class ColumnChooserController extends FrontController {
	public static final String COLUMN_ADDED = "COLUMN_CHOOSER_COLUMN_ADDED";
	public static final String COLUMN_REMOVED = "COLUMN_CHOOSER_COLUMN_REMOVED";
	
	public ColumnChooserController(){
		registerCommand(COLUMN_ADDED, ColumnAddedCommand.class);
		registerCommand(COLUMN_REMOVED, ColumnRemovedCommand.class);
	}
}
