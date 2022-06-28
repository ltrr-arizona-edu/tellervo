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
/**
 * 
 */
package org.tellervo.desktop.wsi;

import java.io.IOException;

/**
 * An exception thrown when loading or acquiring a resource
 * @author lucasm
 */
public class ResourceException extends IOException {
	private static final long serialVersionUID = 2440001920979509944L;

	/**
	 * @param message
	 * @param cause
	 */
	public ResourceException(String message, Throwable cause) {
		super(message);
		initCause(cause);
	}

	/**
	 * @param cause
	 */
	public ResourceException(Throwable cause) {
		super();
		initCause(cause);
	}

	/**
	 * 
	 */
	public ResourceException() {
	}

	/**
	 * @param message
	 */
	public ResourceException(String message) {
		super(message);
	}
}
