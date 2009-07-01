/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import org.tridas.schema.BaseSeries;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasInterpretation;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasMeasuringMethod;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasValue;
import org.tridas.schema.TridasValues;
import org.tridas.schema.TridasVariable;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;

/**
 * @author Lucas Madar
 *
 */
public class TridasMeasurementSeriesEx extends TridasMeasurementSeries {
	private Sample joinedSample;
	
	/**
	 * Construct a deep copy of this series
	 * Uses introspection and TridasCloner - yuck!
	 * 
	 * @param orig
	 * @param joinedSample the sample to map with - can be null
	 */
	@SuppressWarnings("unchecked")
	public TridasMeasurementSeriesEx(TridasMeasurementSeries orig, Sample joinedSample) {
		this.joinedSample = joinedSample;
		
		XmlType xml = BaseSeries.class.getAnnotation(XmlType.class);
		
		// create a map of methods by name
		// This is so we don't have to care about setXXX parameter types
		Method[] methods = BaseSeries.class.getMethods();
		Map<String, Method> methodMap = new HashMap<String, Method>();

		for(Method method : methods)
			methodMap.put(method.getName(), method);
		
		for(String prop : xml.propOrder()) {
			String suffix = Character.toUpperCase(prop.charAt(0)) + prop.substring(1);
			
			try {
				Method method = BaseSeries.class.getMethod("isSet" + suffix, (Class[]) null);
				boolean used = (Boolean) method.invoke(orig, (Object[]) null);

				if(!used)
					continue;
				
				Method getmethod = BaseSeries.class.getMethod("get" + suffix, (Class[]) null);
				Object value = getmethod.invoke(orig, (Object[]) null);
				
				method = methodMap.get("set" + suffix);
				if(method == null) {
					if(!getmethod.getReturnType().isAssignableFrom(List.class))
						throw new IllegalArgumentException("getter/setter nonmatch, not a list??");
					
					// value is a list
					List<Object> myList = (List<Object>) getmethod.invoke(this, (Object[]) null);
					myList.addAll((List<Object>) TridasCloner.clone(value));
				}
				else
					method.invoke(this, TridasCloner.clone(value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get a finalized series from this sample, ready to be handed to the WebService
	 * 
	 * @param s
	 * @return
	 */
	public static TridasMeasurementSeriesEx getSeriesFromSample(Sample s) {
		if(s.getSampleType() != SampleType.DIRECT)
			throw new IllegalStateException("Can't map from a derived series to a measurement series!");
	
		// if we already have a TridasMeasurementSeriesEx, clone it
		if(s.getMeta(Metadata.SERIES) instanceof TridasMeasurementSeriesEx) {
			TridasMeasurementSeriesEx obj = TridasCloner.clone(s.getMeta(Metadata.SERIES, TridasMeasurementSeriesEx.class));
	
			obj.setupFromSample(s);
			
			return obj;
		}
		else
			return new TridasMeasurementSeriesEx(s);
	}
	
	/**
	 * Make a series from this sample
	 * 
	 * @param s
	 */
	private TridasMeasurementSeriesEx(Sample s) {
		setupFromSample(s);
		
		this.joinedSample = s;
	}
	
	/**
	 * Copy information over from the sample
	 * 
	 * @param s
	 */
	private void setupFromSample(Sample s) {
		setIdentifier((TridasIdentifier) s.getMeta(Metadata.TRIDAS_IDENTIFIER));
		setTitle(s.getMetaString(Metadata.NAME));
		
		// year
		Year startYear = s.getRange().getStart();
		TridasInterpretation interpretation = new TridasInterpretation();
		interpretation.setFirstYear(startYear.tridasYearValue());
		setInterpretation(interpretation);
		
		// measurement method
		TridasMeasuringMethod method = s.getMeta(Metadata.MEASURING_METHOD, TridasMeasuringMethod.class);
		if(method == null) {
			method = new TridasMeasuringMethod();
			method.setNormalStd("corina");
			method.setNormal("hand-carved ivory ruler");
		}
		setMeasuringMethod(method);
		
		///
		/// BEGIN ring widths
		///
		int sz = s.getData().size();
		List<Integer> data = s.getData();
		List<Integer> count = s.getCount();
		TridasValues valueHolder = new TridasValues();
		List<TridasValue> values = valueHolder.getValues();
		TridasVariable variable = new TridasVariable();

		// associate the right variable
		variable.setNormalTridas(NormalTridasVariable.RING_WIDTH);
		valueHolder.setVariable(variable);
		
		if(s.hasMeta(Metadata.UNITS))
			valueHolder.setUnit((TridasUnit) s.getMeta(Metadata.UNITS));
		else {
			// default to 1/100th mm?
			// TODO: Fixme!
			TridasUnit units = new TridasUnit();
			units.setNormalTridas(NormalTridasUnit.HUNDREDTH_MM);
			
			valueHolder.setUnit(units);
		}

		// get a decimal formatter for the ring number
		String maxValue = String.valueOf(sz + 1);
		StringBuffer formatBuffer = new StringBuffer();
		for(int i = 0, j = maxValue.length(); i < j; i++) 
			formatBuffer.append('0');
		DecimalFormat df = new DecimalFormat(formatBuffer.toString());

		// and make the list...
		for(int i = 0; i < sz; i++) {
			TridasValue tv = new TridasValue();
			
			tv.setValue(data.get(i).toString());
			tv.setIndex("ring" + df.format(i+1));
			
			if(count != null)
				tv.setCount(BigInteger.valueOf(count.get(i)));
			
			values.add(tv);
		}

		// add values to this series
		getValues().add(valueHolder);
		///
		/// END RING WIDTHS
		///
	}
}
