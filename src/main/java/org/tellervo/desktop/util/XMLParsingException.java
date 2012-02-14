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
package org.tellervo.desktop.util;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;

@SuppressWarnings("serial")
public class XMLParsingException extends IOException {
	
	private Document nonvalidatingDocument;
	private File invalidFile;

	public XMLParsingException(Throwable cause) {
		initCause(cause);
		nonvalidatingDocument = null;
		invalidFile = null;
	}

	public XMLParsingException(Throwable cause, Document doc) {
		initCause(cause);
		
		nonvalidatingDocument = doc;
		invalidFile = null;
	}
	
	public XMLParsingException(Throwable cause, File file) {
		initCause(cause);
		
		nonvalidatingDocument = null;
		invalidFile = file;
	}
	
	public Document getNonvalidatingDocument() {
		return nonvalidatingDocument;
	}
	
	public File getInvalidFile() {
		return invalidFile;
	}
}
