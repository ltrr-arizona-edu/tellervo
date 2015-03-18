package org.tellervo.desktop.gui.seriesidentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.io.IdentityItem;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.util.TridasManipUtil;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.NormalTridasMeasuringMethod;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasMeasuringMethod;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;




/**
 * Table model for a table containing rows for imported data series from legacy text files.  Table contains columns to enable the 
 * user to identify the various entities that each series should be assigned to.
 * 
 * @author pbrewer
 *
 */
public class SeriesIdentityTableModel extends AbstractTableModel {

	private final static Logger log = LoggerFactory.getLogger(SeriesIdentityTableModel.class);

	private String[] columns = new String[] {"Path", "Filename", "Keycode", "Object", "Element", "Sample", "Radius", "Series"};
	
	private ArrayList<SeriesIdentity> ids = new ArrayList<SeriesIdentity>();
	
	private HashMap<String, ITridas> tridasCache = new HashMap<String, ITridas>();
	private String codeDelimiter = "XXXXXDELIMITERXXXXX";
	
	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return ids.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		SeriesIdentity id = getSeriesIdentity(row);
		
		switch(col)
		{
			case 0: return FileUtils.getPath(id.getFile().getAbsolutePath()); 
			case 1: return id.getFile().getName();
			case 2: return id.getSample().getDisplayTitle();
			case 3: return id.getObjectItem();
			case 4: return id.getElementItem();
			case 5: return id.getSampleItem();
			case 6: return id.getRadiusItem();
			case 7: return id.getSeriesItem();
		}
		
		return null;
	}
	
	/**
	 * Get SeriesIdentity row from table
	 * 
	 * @param row
	 * @return
	 */
	public SeriesIdentity getSeriesIdentity(int row)
	{
		try{
			SeriesIdentity si = ids.get(row);
			return si;
			
		} catch (Exception e)
		{
			return null;
		}
		
		
	}
	
	/**
	 * Add row to table
	 * 
	 * @param si
	 */
	public void addItem(SeriesIdentity si)
	{
		ids.add(si);
		this.fireTableDataChanged();
		
	}
	
	/**
	 * Remove row from table
	 * 
	 * @param si
	 */
	public void removeItem(SeriesIdentity si)
	{
		ids.remove(si);
		this.fireTableDataChanged();
	}
	
	@Override
	public String getColumnName(int col)
	{
		return columns[col];
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		if(col<=2) return false;
		
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col)
	{
		if(!isCellEditable(row, col)) return;
		
		SeriesIdentity id = getSeriesIdentity(row);
		
		switch(col)
		{
			case 3: 
				id.getObjectItem().setCode(value.toString()); 
				id.getObjectItem().setDbChecked(false);
				id.getElementItem().setDbChecked(false);
				id.getSampleItem().setDbChecked(false);
				id.getRadiusItem().setDbChecked(false);
				id.getSeriesItem().setDbChecked(false);
				searchForMatches(true);
				break;
			case 4: id.getElementItem().setCode(value.toString()); 
				id.getElementItem().setDbChecked(false);
				id.getSampleItem().setDbChecked(false);
				id.getRadiusItem().setDbChecked(false);
				id.getSeriesItem().setDbChecked(false);
				searchForMatches(true);
				break;
			case 5: 
				id.getSampleItem().setCode(value.toString()); 
				id.getSampleItem().setDbChecked(false);
				id.getRadiusItem().setDbChecked(false);
				id.getSeriesItem().setDbChecked(false);
				searchForMatches(true);
				break;
			case 6: 
				id.getRadiusItem().setCode(value.toString()); 
				id.getRadiusItem().setDbChecked(false);
				id.getSeriesItem().setDbChecked(false);
				searchForMatches(true);
				break;
			case 7: 
				id.getSeriesItem().setCode(value.toString());
				id.getSeriesItem().setDbChecked(false);
				searchForMatches(true);
				break;
		}
		
		ids.set(row, id);
		this.fireTableRowsUpdated(row, row);
		
		
		
	}
	
	/**
	 * Set the database searched flag for the specified entity and row
	 * 
	 * @param row
	 * @param clazz
	 * @param b
	 */
	public void setDatabaseSearchedStatus(int row, Class<? extends ITridas> clazz, boolean b)
	{
		SeriesIdentity id = getSeriesIdentity(row);
		
		if(clazz.equals(TridasObject.class))
		{
			id.getObjectItem().setDbChecked(b);
		}
		else if(clazz.equals(TridasElement.class))
		{
			id.getElementItem().setDbChecked(b);
		}
		else if(clazz.equals(TridasSample.class))
		{
			id.getSampleItem().setDbChecked(b);
		}
		else if(clazz.equals(TridasRadius.class))
		{
			id.getRadiusItem().setDbChecked(b);
		}
		else if(clazz.equals(TridasMeasurementSeries.class))
		{
			id.getSeriesItem().setDbChecked(b);
		}
	}
	
	/**
	 * Set the 'found in database' flag for the specified entity class 
	 * 
	 * @param row
	 * @param clazz
	 * @param b
	 */
	public void setDatabaseFoundStatus(int row, Class<? extends ITridas> clazz, boolean b)
	{
		SeriesIdentity id = getSeriesIdentity(row);
		
		if(clazz.equals(TridasObject.class))
		{
			id.getObjectItem().setInDatabase(b);
		}
		else if(clazz.equals(TridasElement.class))
		{
			id.getElementItem().setInDatabase(b);
		}
		else if(clazz.equals(TridasSample.class))
		{
			id.getSampleItem().setInDatabase(b);
		}
		else if(clazz.equals(TridasRadius.class))
		{
			id.getRadiusItem().setInDatabase(b);
		}
		else if(clazz.equals(TridasMeasurementSeries.class))
		{
			id.getSeriesItem().setInDatabase(b);
		}
	}
	
	/**
	 * Search the cache for matches based on the codes in the table.
	 */
	public void searchForMatches()
	{
		searchForMatches(false);
	}
	
	/**
	 * Search the cache (as well as the database if specified) for matches based on the codes specified in the table.   
	 * 
	 * @param cacheonly
	 */
	public void searchForMatches(boolean cacheonly)
	{
		for(int col=3; col<getColumnCount(); col++)
		{
			for(int row=0; row<getRowCount(); row++)
			{
				SeriesIdentity id = getSeriesIdentity(row);
				IdentityItem item = null;
				String code = "";
				ITridas entity = null;
				switch(col)
				{
				case 3: 
					item = id.getObjectItem(); 
					if(item==null || item.getCode()==null) continue;
					code = id.getObjectItem().getCode();
					
					if(tridasCache.containsKey(code))
					{
						entity = tridasCache.get(code);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					else 
					{
						entity = TridasManipUtil.getTridasObjectByCode(code);
						tridasCache.put(code, entity);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					break;
				case 4: 
					item = id.getElementItem();
					if(item==null || item.getCode()==null) continue;
					code = id.getObjectItem().getCode() + codeDelimiter + id.getElementItem().getCode(); 
					if(tridasCache.containsKey(code))
					{
						entity = tridasCache.get(code);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					else if (!cacheonly)
					{
						entity = searchForItemByCode(code, SearchReturnObject.ELEMENT);
						tridasCache.put(code, entity);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					break;
				case 5: 
					item = id.getSampleItem();
					if(item==null || item.getCode()==null) continue;
					code = id.getObjectItem().getCode() + codeDelimiter + id.getElementItem().getCode()+ codeDelimiter + id.getSampleItem().getCode();
					if(tridasCache.containsKey(code))
					{
						entity = tridasCache.get(code);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					else if (!cacheonly)
					{
						entity = searchForItemByCode(code, SearchReturnObject.SAMPLE);
						tridasCache.put(code, entity);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					break;
				case 6: 
					item = id.getRadiusItem();
					if(item==null || item.getCode()==null) continue;
					code = id.getObjectItem().getCode() + codeDelimiter + id.getElementItem().getCode()+ codeDelimiter + id.getSampleItem().getCode()+codeDelimiter + id.getRadiusItem().getCode();
					if(tridasCache.containsKey(code))
					{
						entity = tridasCache.get(code);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					else if (!cacheonly)
					{
						entity = searchForItemByCode(code, SearchReturnObject.RADIUS);
						tridasCache.put(code, entity);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					break;
				case 7: 
					item = id.getSeriesItem(); 
					if(item==null || item.getCode()==null) continue;
					code = id.getObjectItem().getCode() + codeDelimiter + id.getElementItem().getCode()+ codeDelimiter + id.getSampleItem().getCode()+codeDelimiter + id.getRadiusItem().getCode()+codeDelimiter + id.getSeriesItem().getCode();
					if(tridasCache.containsKey(code))
					{
						entity = tridasCache.get(code);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					else if (!cacheonly)
					{
						entity = searchForItemByCode(code, SearchReturnObject.MEASUREMENT_SERIES);
						tridasCache.put(code, entity);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					break;
				}
			}
		}
		
		this.fireTableDataChanged();
	}
	
	/**
	 * Search the database for an entity based on the two parameters provided 
	 * 
	 * @param code
	 * @param clazz
	 * @return
	 */
	private ITridas searchForItemByCode(String code, SearchReturnObject clazz)
	{
		String[] codes = code.split(codeDelimiter);
		
		// Find all samples for an element 
    	SearchParameters param = new SearchParameters(clazz);
    	
    	TellervoResourceAccessDialog dialog = null;
    	
    	if(clazz.equals(SearchReturnObject.OBJECT))
    	{
    		if(codes.length!=1) {
    			log.debug("Wrong number of codes"); 
    			return null;
    		}
    		
    		param.addSearchConstraint(SearchParameterName.OBJECTCODE, SearchOperator.EQUALS, codes[0]);
    		EntitySearchResource<TridasObject> resource = new EntitySearchResource<TridasObject>(param, TridasObject.class);
    		dialog = new TellervoResourceAccessDialog(resource);
    		resource.query();
    		dialog.setVisible(true);
    		
    		if(!dialog.isSuccessful()) 
    		{ 
    			return null;
    		}
    		
    		if(resource.getAssociatedResult()!=null && resource.getAssociatedResult().size()>0)
    		{
    			return resource.getAssociatedResult().get(0);
    		}
    		else
    		{
    			return null;
    		}
    	}
    	else if (clazz.equals(SearchReturnObject.ELEMENT))
    	{
    		if(codes.length!=2) {
    			log.debug("Wrong number of codes"); 
    			return null;
    		}
    		
    		param.addSearchConstraint(SearchParameterName.OBJECTCODE, SearchOperator.EQUALS, codes[0]);
    		param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, codes[1]);
    		EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
    		dialog = new TellervoResourceAccessDialog(resource);
    		resource.query();	
    		dialog.setVisible(true);
    		
    		if(!dialog.isSuccessful()) 
    		{ 
    			return null;
    		}
    		
    		if(resource.getAssociatedResult()!=null && resource.getAssociatedResult().size()>0)
    		{
    			return resource.getAssociatedResult().get(0);
    		}
    		else
    		{
    			return null;
    		}
    	}
    	else if (clazz.equals(SearchReturnObject.SAMPLE))
    	{
    		if(codes.length!=3) {
    			log.debug("Wrong number of codes"); 
    			return null;
    		}
    		param.addSearchConstraint(SearchParameterName.OBJECTCODE, SearchOperator.EQUALS, codes[0]);
    		param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, codes[1]);
    		param.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.EQUALS, codes[2]);
    		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
    		dialog = new TellervoResourceAccessDialog(resource);
    		resource.query();
    		dialog.setVisible(true);
    		
    		if(!dialog.isSuccessful()) 
    		{ 
    			return null;
    		}
    		
    		if(resource.getAssociatedResult()!=null && resource.getAssociatedResult().size()>0)
    		{
    			return resource.getAssociatedResult().get(0);
    		}
    		else
    		{
    			return null;
    		}
    	}
    	else if (clazz.equals(SearchReturnObject.RADIUS))
    	{
    		if(codes.length!=4) {
    			log.debug("Wrong number of codes"); 
    			return null;
    		}
    		param.addSearchConstraint(SearchParameterName.OBJECTCODE, SearchOperator.EQUALS, codes[0]);
    		param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, codes[1]);
    		param.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.EQUALS, codes[2]);
    		param.addSearchConstraint(SearchParameterName.RADIUSCODE, SearchOperator.EQUALS, codes[3]);
    		EntitySearchResource<TridasRadius> resource = new EntitySearchResource<TridasRadius>(param, TridasRadius.class);
    		dialog = new TellervoResourceAccessDialog(resource);
    		resource.query();	
    		dialog.setVisible(true);
    		
    		if(!dialog.isSuccessful()) 
    		{ 
    			return null;
    		}
    		
    		if(resource.getAssociatedResult()!=null && resource.getAssociatedResult().size()>0)
    		{
    			return resource.getAssociatedResult().get(0);
    		}
    		else
    		{
    			return null;
    		}
    	}
    	else if (clazz.equals(SearchReturnObject.MEASUREMENT_SERIES))
    	{
    		if(codes.length!=5) {
    			log.debug("Wrong number of codes"); 
    			return null;
    		}
    		param.addSearchConstraint(SearchParameterName.OBJECTCODE, SearchOperator.EQUALS, codes[0]);
    		param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, codes[1]);
    		param.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.EQUALS, codes[2]);
    		param.addSearchConstraint(SearchParameterName.RADIUSCODE, SearchOperator.EQUALS, codes[3]);
    		param.addSearchConstraint(SearchParameterName.SERIESCODE, SearchOperator.EQUALS, codes[4]);
    		EntitySearchResource<TridasMeasurementSeries> resource = new EntitySearchResource<TridasMeasurementSeries>(param, TridasMeasurementSeries.class);
    		dialog = new TellervoResourceAccessDialog(resource);
    		resource.query();	
    		dialog.setVisible(true);
    		
    		if(!dialog.isSuccessful()) 
    		{ 
    			return null;
    		}
    		
    		if(resource.getAssociatedResult()!=null && resource.getAssociatedResult().size()>0)
    		{
    			return resource.getAssociatedResult().get(0);
    		}
    		else
    		{
    			return null;
    		}
    	}
		return null;
	}

	public void generateMissingEntities()
	{
		for(SeriesIdentity id : this.ids)
		{
			
		}
	}
	
	/**
	 * Returns true if the table has been completely filled out by the user
	 * 
	 * @return
	 */
	public boolean isTableComplete()
	{
		for(SeriesIdentity id : this.ids)
		{
			if(id.getObjectItem().getCode()==null  || id.getObjectItem().getCode().length()==0) return false;
			if(id.getElementItem().getCode()==null || id.getElementItem().getCode().length()==0) return false;
			if(id.getSampleItem().getCode()==null  || id.getSampleItem().getCode().length()==0) return false;
			if(id.getRadiusItem().getCode()==null  || id.getRadiusItem().getCode().length()==0) return false;
			if(id.getSeriesItem().getCode()==null  || id.getSeriesItem().getCode().length()==0) return false;
		}
		
		return true;
	}
	
	public ArrayList<Sample> getAllSamples()
	{
		
		ArrayList<Sample> samples = new ArrayList<Sample>();
		
		for(SeriesIdentity id : ids)
		{
			samples.add(getPopulatedSampleFromSeriesIdentity(id));
		}
		
		
		return samples;
		
	}
	
	/**
	 * Populate the given Tellervo sample from the information we acquired
	 * 
	 * @param s
	 */
	private Sample getPopulatedSampleFromSeriesIdentity(SeriesIdentity id) {
		LabCode labcode = new LabCode();
		
		Sample s = id.getSample();

		TridasObject object = (TridasObject) tridasCache.get(id.getObjectItem().getCode());
		TridasElement element = (TridasElement) tridasCache.get(id.getObjectItem().getCode() + codeDelimiter + id.getElementItem().getCode()); 
		TridasSample sample = (TridasSample) tridasCache.get(id.getObjectItem().getCode() + codeDelimiter + id.getElementItem().getCode()  + codeDelimiter + id.getSampleItem().getCode());
		TridasRadius radius = (TridasRadius) tridasCache.get(id.getObjectItem().getCode() + codeDelimiter + id.getElementItem().getCode()  + codeDelimiter + id.getSampleItem().getCode() + codeDelimiter + id.getRadiusItem().getCode());
		

		ITridasSeries series = s.getSeries();
		TellervoWsiTridasElement.attachNewSample(s);

		if(object != null) {
			s.setMeta(Metadata.OBJECT, object);
			
			
			if(object instanceof TridasObjectEx){
				labcode.appendSiteCode(((TridasObjectEx)object).getLabCode());
			}
			else{
				labcode.appendSiteCode(object.getTitle());
			}
			
		}
		
		if(element != null) {
			s.setMeta(Metadata.ELEMENT, element);
			labcode.setElementCode(element.getTitle());
		}
		
		if(sample != null) {
			s.setMeta(Metadata.SAMPLE, sample);
			labcode.setSampleCode(sample.getTitle());
		}
		
		if(radius != null) {
			s.setMeta(Metadata.RADIUS, radius);
			labcode.setRadiusCode(radius.getTitle());
		}

		if(series!=null)
		{
			series.setTitle(id.getSeriesItem().getCode());
			TridasMeasuringMethod mm = new TridasMeasuringMethod();
			mm.setNormalTridas(NormalTridasMeasuringMethod.MEASURING_PLATFORM);
			((TridasMeasurementSeries) series).setMeasuringMethod(mm);
			labcode.setSeriesCode(s.getSeries().getTitle());
		}

		s.setMeta(Metadata.LABCODE, labcode);
		s.setMeta(Metadata.TITLE, LabCodeFormatter.getDefaultFormatter().format(labcode));
		
		
		
		return s;
	}
	
}
