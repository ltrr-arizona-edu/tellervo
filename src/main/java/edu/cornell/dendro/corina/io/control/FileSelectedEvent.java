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
package edu.cornell.dendro.corina.io.control;

import java.io.File;

import com.dmurph.mvc.ObjectEvent;

import edu.cornell.dendro.corina.io.model.ImportModel;

public class FileSelectedEvent extends ObjectEvent<File> {

	private static final long serialVersionUID = 1L;
	
	public final ImportModel model;
	public final File file;
	public final String fileType;
	

	public FileSelectedEvent(ImportModel model, File file, String fileType) {
		super(IOController.FILE_SELECTED, file);
		this.model = model;
		this.file = file;
		this.fileType = fileType;
	}

}
