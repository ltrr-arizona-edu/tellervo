package edu.cornell.dendro.corina.tridasv2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.wsi.corina.CorinaWsiAccessor;

/**
 * Lazy way to clone an object hierarchy that's JAXB-defined
 * 
 * @author Lucas Madar
 */

public class TridasCloner {
	public static Object clone(Serializable o) {
		if(o == null)
			return null;
		
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(buf);
		
			out.writeObject(o);
			
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()));
			
			return in.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
