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
import org.tellervo.desktop.bulkImport.control.BulkImportController;
import org.tellervo.desktop.bulkImport.control.ColumnChooserController;
import org.tellervo.desktop.editor.control.EditorController;
import org.tellervo.desktop.io.control.IOController;
import org.tellervo.desktop.io.model.ImportModel;

/**
 *
 * @author daniel
 */
@SuppressWarnings("unused")
public class CorinaModelLocator {
	private static final CorinaModelLocator model = new CorinaModelLocator();
	
	private EditorController editorController = new EditorController();
	private ColumnChooserController columnController = new ColumnChooserController();
	private BulkImportController bulkImportController = new BulkImportController();
	private IOController corinaImportController = new IOController();
	
	private final ImportModel corinaImportModel = new ImportModel();
	
	private final UserGroupAdminController userGroupAdminControl = new UserGroupAdminController();
	
	private CorinaModelLocator(){
		
	}
	
	public ImportModel getImportModel()
	{
		return corinaImportModel;
	}
	
	public static CorinaModelLocator getInstance(){
		return model;
	}
}
