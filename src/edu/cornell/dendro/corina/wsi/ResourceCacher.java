package edu.cornell.dendro.corina.wsi;

import java.awt.EventQueue;
import java.io.File;
import java.sql.Time;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import edu.cornell.dendro.corina.core.App;

public abstract class ResourceCacher<INTYPE> implements ResourceEventListener {
	/** The resource to cache */
	private Resource<INTYPE, ?> myResource;
	
	/** The class of INTYPE */
	private final Class<INTYPE> intypeClass;

	/** Remove this cacher on load */
	private final boolean removeOnLoad;
	
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
				System.out.println("Unloading resource cacher for " + myResource.getResourceName());
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
			System.err.println("Couldn't load cache into "
					+ myResource.getResourceName() + ": " + e.getMessage());
			return false;
		}

		Date endTime = new Date();
		long ms = endTime.getTime() - startTime.getTime();
		
		System.out.println("Loaded cache for " + myResource.getResourceName() + " [" + ms + " ms]");
		
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
			System.err.println("Couldn't save cache for "
					+ myResource.getResourceName() + ": " + e.getMessage());
		}
	}
	
	protected abstract File getCacheFile();
}
