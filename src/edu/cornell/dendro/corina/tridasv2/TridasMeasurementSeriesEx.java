/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasInterpretation;
import org.tridas.schema.TridasMeasurementSeries;
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

	public TridasMeasurementSeriesEx(Sample s) {
		if(s.getSampleType() != SampleType.DIRECT)
			throw new IllegalStateException("Can't map from a derived series to a measurement series!");
		
		setIdentifier((TridasIdentifier) s.getMeta(Metadata.TRIDAS_IDENTIFIER));
		setTitle(s.getMetaString(Metadata.NAME));
		
		// year
		Year startYear = s.getRange().getStart();
		TridasInterpretation interpretation = new TridasInterpretation();
		interpretation.setFirstYear(startYear.tridasYearValue());
		setInterpretation(interpretation);
		
		// ring widths
		int sz = s.getData().size();
		List<Object> data = s.getData();
		List<Integer> count = s.getCount();
		TridasValues valueHolder = new TridasValues();
		List<TridasValue> values = valueHolder.getValue();
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

		// and make the list...
		for(int i = 0; i < sz; i++) {
			TridasValue tv = new TridasValue();
			tv.setValue(data.get(i).toString());
			if(count != null)
				tv.setCount(BigInteger.valueOf(count.get(i)));
			
			values.add(tv);
		}
	}

}
