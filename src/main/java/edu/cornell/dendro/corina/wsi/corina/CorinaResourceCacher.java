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
