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
package edu.cornell.dendro.corina.io;

import java.io.IOException;

/**
 * Something to throw when a loading error occurs that's not related directly to IO
 * (e.g., data is malformed)
 * 
 * @author lucasm
 *
 */

@SuppressWarnings("serial")
public class InvalidDataException extends IOException {

	public InvalidDataException() {
	}

	public InvalidDataException(String s) {
		super(s);
	}

	public InvalidDataException(Throwable cause) {
		super();
		initCause(cause);
	}

	public InvalidDataException(String s, Throwable cause) {
		super(s);
		initCause(cause);
	}
}
