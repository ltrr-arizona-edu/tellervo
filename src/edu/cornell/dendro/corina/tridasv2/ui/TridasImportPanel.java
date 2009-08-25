/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Dimension;
import java.util.List;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleAdapter;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
import edu.cornell.dendro.corina.util.LegacySampleExtractor;

/**
 * @author Lucas Madar
 *
 */
public class TridasImportPanel extends TridasMetadataPanel {
	private static final long serialVersionUID = 1L;

	private LegacySampleExtractor extractor; 
	private Sample s;
	
	/**
	 * @param s
	 */
	public TridasImportPanel(Sample s, LegacySampleExtractor extractor) {
		super(s);
	
		this.s = s;
		
		this.extractor = extractor;
		
		// populate measurement info
		extractor.populateMeasurement((TridasMeasurementSeries) s.getSeries());

		// start out larger...
		setPreferredSize(new Dimension(600, 600));
		
		// reload the series
		EditType.MEASUREMENT_SERIES.setEntity(s, s.getSeries());
		enableEditing(true);		
	}

	/**
	 * 
	 * @return true if the import has been completed
	 */
	public boolean importCompleted() {
		for(EditType type = EditType.OBJECT; type != null; type = type.next()) {
			ITridas entity = type.getEntity(TridasImportPanel.this.s);
			
			// drop out if we don't have it, or if it's not changed...
			if(entity == null || type.hasChanged())
				return false;
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.tridasv2.ui.TridasMetadataPanel#populateNewEntity(edu.cornell.dendro.corina.tridasv2.ui.TridasMetadataPanel.EditType, org.tridas.interfaces.ITridas)
	 */
	@Override
	protected void populateNewEntity(EditType type, ITridas entity) {
		// do this first, in case we don't have a suggested title...
		super.populateNewEntity(type, entity);
		
		switch(type) {
		case OBJECT:
			extractor.populateObject((TridasObject) entity);
			break;
			
		case ELEMENT:
			extractor.populateElement((TridasElement) entity);
			break;
			
		case SAMPLE:
			extractor.populateSample((TridasSample) entity);
			break;
			
		case RADIUS:
			extractor.populateRadius((TridasRadius) entity);
			break;
			
		case MEASUREMENT_SERIES:
			extractor.populateMeasurement((TridasMeasurementSeries) entity);
			break;
			
		}
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.tridasv2.ui.TridasMetadataPanel#suggestSelectedEntity(edu.cornell.dendro.corina.tridasv2.ui.TridasMetadataPanel.EditType, java.util.List)
	 */
	@Override
	protected ITridas suggestSelectedEntity(EditType mode,
			List<? extends ITridas> list) {

		// special case for object
		if(mode == EditType.OBJECT) {
			String code = extractor.getObjectCode();
			
			for(ITridas e : list) {
				if(e instanceof TridasObjectEx) {
					TridasObjectEx obj = (TridasObjectEx) e;
					
					if(obj.hasLabCode() && obj.getLabCode().equalsIgnoreCase(code))
						return obj;
				}
			}
			
			// no suggestion, choose 'new'
			return null;
		}

		// ok, default case is slightly easier
		String title;
		
		switch(mode) {
		case ELEMENT:
			title = extractor.getElementTitle();
			break;
		case SAMPLE:
			title = extractor.getSampleTitle();
			break;
		case RADIUS:
			title = extractor.getRadiusTitle();
			break;
		case MEASUREMENT_SERIES:
		case DERIVED_SERIES:
			title = extractor.getMeasurementTitle();
			break;
			
		// can't extract anything else...
		default:
			return null;
		}
		
		// shortcut out
		if(title == null)
			return null;
		
		// suggest any match
		for(ITridas e : list) {
			if(title.equalsIgnoreCase(e.getTitle()))
				return e;
		}
	
		// no match, choose new!
		return null;
	}
}
