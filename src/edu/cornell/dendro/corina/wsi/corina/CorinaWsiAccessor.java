package edu.cornell.dendro.corina.wsi.corina;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.wsi.WebJaxbAccessor;

public class CorinaWsiAccessor extends WebJaxbAccessor<WSIRootElement, WSIRootElement> {
	/**
	 * @param noun
	 */
	public CorinaWsiAccessor(String noun) {
		super(noun, WSIRootElement.class);	
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.WebJaxbAccessor#getValidationSchema()
	 */
	@Override
	protected Schema getValidationSchema() {
		return CorinaSchema.getCorinaSchema();
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.WebJaxbAccessor#getJAXBContext()
	 */
	@Override
	protected JAXBContext getJAXBContext() throws JAXBException {
		return CorinaWsiAccessor.getCorinaContext();
	}	

	@Override
	protected NamespacePrefixMapper getNamespacePrefixMapper() {
		return new CorinaNamespacePrefixMapper();
	}
	
	/**
	 * @return
	 * @throws JAXBException
	 */
	protected synchronized static JAXBContext getCorinaContext() throws JAXBException {
		if(corinaContext == null)
			corinaContext = JAXBContext.newInstance(CORINA_CONTEXT_CLASSES);

		return corinaContext;
	}
	
	/**
	 * Load and cache the corina context!
	 */
	public static void loadCorinaContext() {
		try {
			getCorinaContext();
		} catch (Exception e) {
			new Bug(e);
		}
	}

	private static JAXBContext corinaContext;
	private static final Class<?> CORINA_CONTEXT_CLASSES[] = {
		net.opengis.gml.schema.ObjectFactory.class,
		org.tridas.schema.ObjectFactory.class,
		edu.cornell.dendro.corina.schema.ObjectFactory.class,
		edu.cornell.dendro.corina.tridasv2.TridasObjectEx.class
	};
}
