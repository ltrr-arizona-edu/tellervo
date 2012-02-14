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
package org.tellervo.desktop.util.openrecent;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "openables"
})
@XmlRootElement(name = "recentDocuments")
public class RecentlyOpenedDocuments {
	@XmlElements({
		@XmlElement(name = "seriesDescriptor", type = SeriesDescriptor.class)
		// add more types in here...!
	})
	protected List<OpenableDocumentDescriptor> openables;
	@XmlAttribute(name = "tag")
	protected String tag;

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the openables
	 */
	public List<OpenableDocumentDescriptor> getOpenables() {
		if(openables == null)
			openables = new ArrayList<OpenableDocumentDescriptor>();
		return openables;
	}

	/**
	 * @param openables the openables to set
	 */
	public void setOpenables(List<OpenableDocumentDescriptor> openables) {
		this.openables = openables;
	}
}
