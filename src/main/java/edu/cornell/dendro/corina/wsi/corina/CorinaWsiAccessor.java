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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import edu.cornell.dendro.corina.gui.Bug;
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
		//edu.cornell.dendro.corina.tridasv2.TridasObjectEx.class
		//org.tridas.util.TridasObjectEx.class
	};
}
