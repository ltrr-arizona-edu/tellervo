package edu.cornell.dendro.corina.tridasv2;

import java.util.List;

import org.jvnet.jaxb2_commons.lang.Copyable;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasValues;

/**
 * Lazy way to clone an object hierarchy that's JAXB-defined
 * 
 * @author Lucas Madar
 */

public class TridasCloner {
	@SuppressWarnings("unchecked")
	public static <T extends Copyable> T clone(T o) {
		Object copy = o.createCopy();
		o.copyTo(copy);
		
		return (T) copy;
	}
	
	/**
	 * Copy a series, but make the values just a reference
	 * @param series
	 * @return A copy of the series with values as reference
	 */
	public static ITridasSeries cloneSeriesRefValues(ITridasSeries series) {
		// safety check
		if(series == null)
			return null;
		
		List<TridasValues> values = series.isSetValues() ? series.getValues() : null;
		
		// remove values from the series temporarily
		series.unsetValues();
	
		// copy the series
		ITridasSeries copy = (ITridasSeries)(((Copyable) series).createCopy());
		((Copyable)series).copyTo(copy);
		
		// re-set the values (and reference them, too)
		series.setValues(values);
		copy.setValues(values);
		
		return copy;
	}
		
	/*
	@SuppressWarnings("unchecked")
	public static <T> T clone(T o) {
		if(o == null)
			return null;
		
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(buf);
		
			out.writeObject(o);
			
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf.toByteArray()));
			
			return (T) in.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	*/
}
