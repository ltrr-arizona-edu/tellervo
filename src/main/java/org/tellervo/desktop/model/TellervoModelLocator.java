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
package org.tellervo.desktop.model;

import org.tellervo.desktop.admin.control.UserGroupAdminController;
import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tellervo.desktop.bulkdataentry.control.ColumnChooserController;
import org.tellervo.desktop.editor.control.EditorController;
import org.tellervo.desktop.io.control.IOController;
import org.tellervo.desktop.io.model.ImportModel;

/**
 *
 * @author daniel
 */
@SuppressWarnings("unused")
public class TellervoModelLocator {
	private static final TellervoModelLocator model = new TellervoModelLocator();
	
	private EditorController editorController = new EditorController();
	private ColumnChooserController columnController = new ColumnChooserController();
	private BulkImportController bulkImportController = new BulkImportController();
	private IOController tellervoImportController = new IOController();
	
	private final ImportModel tellervoImportModel = new ImportModel();
	
	private final UserGroupAdminController userGroupAdminControl = new UserGroupAdminController();
	
	private TellervoModelLocator(){
		
	}
	
	public ImportModel getImportModel()
	{
		return tellervoImportModel;
	}
	
	public static TellervoModelLocator getInstance(){
		return model;
	}
}
