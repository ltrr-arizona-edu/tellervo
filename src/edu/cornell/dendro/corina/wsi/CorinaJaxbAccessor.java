package edu.cornell.dendro.corina.wsi;

import java.io.IOException;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.schema.CorinaSvcCorina;
import edu.cornell.dendro.corina.schema.CorinaSvcEntity;
import edu.cornell.dendro.corina.schema.CorinaSvcRequest;

public class CorinaJaxbAccessor extends WebJaxbAccessor<CorinaSvcCorina, CorinaSvcCorina> {
	/**
	 * @param noun
	 */
	public CorinaJaxbAccessor(String noun) {
		super(noun, CorinaSvcCorina.class);	
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
		return CorinaJaxbAccessor.getCorinaContext();
	}

	/**
	 * @return
	 * @throws JAXBException
	 */
	private synchronized static JAXBContext getCorinaContext() throws JAXBException {
		if(corinaContext == null)
			corinaContext = JAXBContext.newInstance(CORINA_CONTEXT_CLASSES);

		return corinaContext;
	}

	private static JAXBContext corinaContext;
	private static final Class<?> CORINA_CONTEXT_CLASSES[] = {
		net.opengis.gml.schema.ObjectFactory.class,
		org.tridas.schema.ObjectFactory.class,
		edu.cornell.dendro.corina.schema.ObjectFactory.class
	};
	
	public static void main(String args[]) throws IOException {	
		App.platform = new Platform();
		App.platform.init();
		App.prefs = new Prefs();
		App.prefs.init();
	
		CorinaJaxbAccessor c = new CorinaJaxbAccessor("cows");
		CorinaSvcCorina s = new CorinaSvcCorina();
		CorinaSvcRequest req = new CorinaSvcRequest();
		CorinaSvcEntity ent = new CorinaSvcEntity();
		
		ent.setType("measurementSeries");
		ent.setId(BigInteger.valueOf(2258));
		
		s.setRequest(req);		
		req.setType("read");
		req.getEntity().add(ent);
		
		c.setRequestObject(s);
		
		c.execute();
	}
}
