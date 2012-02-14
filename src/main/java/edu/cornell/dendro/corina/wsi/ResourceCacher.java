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
package edu.cornell.dendro.corina.wsi;

import java.awt.EventQueue;
import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public abstract class ResourceCacher<INTYPE> implements ResourceEventListener {
	/** The resource to cache */
	private Resource<INTYPE, ?> myResource;
	
	/** The class of INTYPE */
	private final Class<INTYPE> intypeClass;

	/** Remove this cacher on load */
	private final boolean removeOnLoad;
	
	private final static Logger log = LoggerFactory.getLogger(ResourceCacher.class);

	
	/**
	 * Generates a resource cacher that saves a resource to disk.
	 * 
	 * @param resource
	 * @param intypeClass
	 * @param removeOnLoad remove after a remote query of the resource (whether failed or not)
	 */
	public ResourceCacher(Resource<INTYPE, ?> resource, Class<INTYPE> intypeClass,
			boolean removeOnLoad) {
		this.myResource = resource;
		this.intypeClass = intypeClass;
		this.removeOnLoad = removeOnLoad;
		
		myResource.addResourceEventListener(this);
	}

	public void resourceChanged(ResourceEvent re) {
		int eventType = re.getEventType();

		// success? save!
		if(eventType == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
			save(re.getAttachedObject());
		}
		
		// if we don't remove on load, leave
		// also, ignore other types of events
		if(!removeOnLoad || !(eventType == ResourceEvent.RESOURCE_QUERY_COMPLETE
				|| eventType == ResourceEvent.RESOURCE_QUERY_FAILED)) {
			return;
		}
		
		// have to do this in another thread...
		final ResourceEventListener glue = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				log.debug("Unloading resource cacher for " + myResource.getResourceName());
				myResource.removeResourceEventListener(glue);
				myResource = null;
			}
		});
	}
	
	/**
	 * Get the resource I'm caching
	 * 
	 * @return
	 */
	public Resource<INTYPE, ?> getResource() {
		return myResource;
	}

	protected abstract JAXBContext getJAXBContext() throws JAXBException;
	
	protected NamespacePrefixMapper getNamespacePrefixMapper() {
		return null;
	}
	
	public boolean load() {
		if(myResource == null)
			return false;
		
		File cacheFile = getCacheFile();
		
		// can't load a cache that isn't there...
		if(!cacheFile.exists())
			return false;

		// time this
		Date startTime = new Date();

		try {
			Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
			
			Object obj = unmarshaller.unmarshal(cacheFile);			
			myResource.processQueryResult(intypeClass.cast(obj));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Couldn't load cache into "
					+ myResource.getResourceName() + ": " + e.getMessage());
			return false;
		}

		Date endTime = new Date();
		long ms = endTime.getTime() - startTime.getTime();
		
		log.debug("Loaded cache for " + myResource.getResourceName() + " [" + ms + " ms]");
		
		return true;
	}
	
	/**
	 * Marshall the object to disk...
	 * 
	 * @param obj
	 */
	private void save(Object obj) {
		if(myResource == null)
			return;
		
		try {
			Marshaller marshaller = getJAXBContext().createMarshaller();
			NamespacePrefixMapper nspm = getNamespacePrefixMapper();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			if (nspm != null)
				marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", nspm);
			
			marshaller.marshal(obj, getCacheFile());
		} catch (JAXBException e) {
			log.error("Couldn't save cache for "
					+ myResource.getResourceName() + ": " + e.getMessage());
		}
	}
	
	protected abstract File getCacheFile();
}
