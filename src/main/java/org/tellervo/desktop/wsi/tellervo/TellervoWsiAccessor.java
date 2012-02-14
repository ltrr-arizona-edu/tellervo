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
package org.tellervo.desktop.wsi.tellervo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;

import org.tellervo.desktop.gui.Bug;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.desktop.wsi.WebJaxbAccessor;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;


public class TellervoWsiAccessor extends WebJaxbAccessor<WSIRootElement, WSIRootElement> {
	/**
	 * @param noun
	 */
	public TellervoWsiAccessor(String noun) {
		super(noun, WSIRootElement.class);	
	}
	
	/* (non-Javadoc)
	 * @see org.tellervo.desktop.wsi.WebJaxbAccessor#getValidationSchema()
	 */
	@Override
	protected Schema getValidationSchema() {
		return TellervoSchema.getCorinaSchema();
	}

	/* (non-Javadoc)
	 * @see org.tellervo.desktop.wsi.WebJaxbAccessor#getJAXBContext()
	 */
	@Override
	protected JAXBContext getJAXBContext() throws JAXBException {
		return TellervoWsiAccessor.getCorinaContext();
	}	

	@Override
	protected NamespacePrefixMapper getNamespacePrefixMapper() {
		return new TellervoNamespacePrefixMapper();
	}
	
	/**
	 * @return
	 * @throws JAXBException
	 */
	protected synchronized static JAXBContext getCorinaContext() throws JAXBException {
		if(tellervoContext == null)
			tellervoContext = JAXBContext.newInstance(CORINA_CONTEXT_CLASSES);

		return tellervoContext;
	}
	
	/**
	 * Load and cache the tellervo context!
	 */
	public static void loadCorinaContext() {
		try {
			getCorinaContext();
		} catch (Exception e) {
			new Bug(e);
		}
	}

	private static JAXBContext tellervoContext;
	private static final Class<?> CORINA_CONTEXT_CLASSES[] = {
		net.opengis.gml.schema.ObjectFactory.class,
		org.tridas.schema.ObjectFactory.class,
		org.tellervo.schema.ObjectFactory.class,
		//org.tellervo.desktop.tridasv2.TridasObjectEx.class
		//org.tridas.util.TridasObjectEx.class
	};
}
