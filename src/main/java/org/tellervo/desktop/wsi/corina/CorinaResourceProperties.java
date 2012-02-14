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
package org.tellervo.desktop.wsi.corina;

import org.tellervo.desktop.wsi.ResourceProperties;

public abstract class CorinaResourceProperties extends ResourceProperties {
	/**
	 * The request format we want to override.
	 * 
	 * Note that overriding with things like "minimal" and others
	 * can have unintended results (such as crashing). Be careful.
	 * 
	 * @see edu.cornell.dendro.corina.schema.CorinaRequestFormat
	 */
	public final static String ENTITY_REQUEST_FORMAT = "corina.requestFormat";
}
