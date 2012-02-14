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
package edu.cornell.dendro.corina.wsi.corina;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.wsi.Resource;
import edu.cornell.dendro.corina.wsi.ResourceCacher;

public class CorinaResourceCacher extends ResourceCacher<WSIRootElement> {
	public CorinaResourceCacher(Resource<WSIRootElement, ?> resource, boolean removeOnLoad) {
		super(resource, WSIRootElement.class, removeOnLoad);
	}

	@Override
	protected File getCacheFile() {
		return new File(App.prefs.getCorinaDir() + getResource().getResourceName() + ".xmlcache");
	}

	@Override
	protected JAXBContext getJAXBContext() throws JAXBException {
		return CorinaWsiAccessor.getCorinaContext();
	}
}
