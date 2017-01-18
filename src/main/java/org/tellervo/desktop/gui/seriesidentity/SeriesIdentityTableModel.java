package org.tellervo.desktop.gui.seriesidentity;

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.io.IdentityItem;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.TellervoWSILoader;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.TridasManipUtil;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoEntityAssociatedResource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesResource;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesSearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.ComplexPresenceAbsence;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasMeasuringMethod;
import org.tridas.schema.ObjectFactory;
import org.tridas.schema.PresenceAbsence;
import org.tridas.schema.TridasBark;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasHeartwood;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasMeasuringMethod;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasPith;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasSapwood;
import org.tridas.schema.TridasWoodCompleteness;
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

	private String[] columns = new String[] {"Path", "Filename", "Keycode", "Object", "SubObject", "Element", "Sample", "Radius", "Series"};
	
	private ArrayList<SeriesIdentity> ids = new ArrayList<SeriesIdentity>();
	
	private HashMap<String, ITridas> tridasCache = new HashMap<String, ITridas>();
	private String codeDelimiter = "xDELIMx";
	
	private boolean includeSubObjects = false;
	
	private static final long serialVersionUID = 1L;
	private Window parent;
	
	public SeriesIdentityTableModel(Window parent)
	{
		this.parent = parent;
	}
	
	public void setIncludeSubObjects(boolean b)
	{ 
		this.includeSubObjects = b;
	}

	
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
			case 4: return id.getSubObjectItem();
			case 5: return id.getElementItem();
			case 6: return id.getSampleItem();
			case 7: return id.getRadiusItem();
			case 8: return id.getSeriesItem();
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
		
		log.debug("Setting value at row: "+row+", col: "+col+" to value: "+value.toString());

		
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
			case 4: 
				id.getSubObjectItem().setCode(value.toString());
				id.getSubObjectItem().setDbChecked(false);
				id.getElementItem().setDbChecked(false);
				id.getSampleItem().setDbChecked(false);
				id.getRadiusItem().setDbChecked(false);
				id.getSeriesItem().setDbChecked(false);
				searchForMatches(true);
				break;					
			case 5: id.getElementItem().setCode(value.toString()); 
				id.getElementItem().setDbChecked(false);
				id.getSampleItem().setDbChecked(false);
				id.getRadiusItem().setDbChecked(false);
				id.getSeriesItem().setDbChecked(false);
				searchForMatches(true);
				break;
			case 6: 
				id.getSampleItem().setCode(value.toString()); 
				id.getSampleItem().setDbChecked(false);
				id.getRadiusItem().setDbChecked(false);
				id.getSeriesItem().setDbChecked(false);
				searchForMatches(true);
				break;
			case 7: 
				id.getRadiusItem().setCode(value.toString()); 
				id.getRadiusItem().setDbChecked(false);
				id.getSeriesItem().setDbChecked(false);
				searchForMatches(true);
				break;
			case 8: 
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
					
					code = id.getObjectItem().getCode()+ codeDelimiter + "NULL";
					
					if(tridasCache.containsKey(code))
					{
						entity = tridasCache.get(code);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					else 
					{
						entity = TridasManipUtil.getTridasObjectByCode(id.getObjectItem().getCode());
						tridasCache.put(code, entity);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					break;
				case 4: 
					
					// Only search if we're doing subobjects
					if(!includeSubObjects) continue;
					
					item = id.getSubObjectItem(); 
					if(item==null || item.getCode()==null) continue;
					code = id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode(); 
					
					if(tridasCache.containsKey(code))
					{
						entity = tridasCache.get(code);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					else 
					{
						entity = this.searchForSubObjectByCode(id.getObjectItem().getCode(), id.getSubObjectItem().getCode());
						tridasCache.put(code, entity);
						item.setDbChecked(true);
						item.setInDatabase(entity!=null);
					}
					break;					
				case 5: 
					item = id.getElementItem();
					if(item==null || item.getCode()==null) continue;
					
					String parentcode = id.getObjectItem().getCode() + codeDelimiter + "NULL";
					if(this.includeSubObjects)
					{
						parentcode = id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode();
					}
					code = parentcode + codeDelimiter 
							+ id.getElementItem().getCode();
					
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
				case 6: 
					item = id.getSampleItem();
					if(item==null || item.getCode()==null) continue;
					
					parentcode = id.getObjectItem().getCode() + codeDelimiter + "NULL";
					if(this.includeSubObjects)
					{
						parentcode = id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode();
					}
					code = parentcode + codeDelimiter
							+ id.getElementItem().getCode() + codeDelimiter 
							+ id.getSampleItem().getCode();
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
				case 7: 
					item = id.getRadiusItem();
					if(item==null || item.getCode()==null) continue;
					
					parentcode = id.getObjectItem().getCode() + codeDelimiter + "NULL";
					if(this.includeSubObjects)
					{
						parentcode = id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode();
					}
					code = parentcode + codeDelimiter 
							+ id.getElementItem().getCode() + codeDelimiter 
							+ id.getSampleItem().getCode() + codeDelimiter 
							+ id.getRadiusItem().getCode();
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
				case 8: 
					item = id.getSeriesItem(); 
					if(item==null || item.getCode()==null) continue;
					
					parentcode = id.getObjectItem().getCode() + codeDelimiter + "NULL";
					if(this.includeSubObjects)
					{
						parentcode = id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode();
					}
					code = parentcode + codeDelimiter + 
							id.getElementItem().getCode()+ codeDelimiter + 
							id.getSampleItem().getCode()+ codeDelimiter + 
							id.getRadiusItem().getCode()+ codeDelimiter +  
							id.getSeriesItem().getCode();
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
	
	
	private TridasObject searchForSubObjectByCode(String parentcode, String subobjectcode)
	{
		// Find all samples for an element 
    	SearchParameters param = new SearchParameters(SearchReturnObject.SUBOBJECT);
    	
    	TellervoResourceAccessDialog dialog = null;
    	
    	param.addSearchConstraint(SearchParameterName.TOPOBJECTCODE, SearchOperator.EQUALS, parentcode);
    	param.addSearchConstraint(SearchParameterName.SUBOBJECTCODE, SearchOperator.EQUALS, subobjectcode);
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
    	
    	
    	String objectcode = codes[0];
    	if(codes.length<2) return null;
    	
    	if(codes[1]!=null && !codes[1].equals("NULL") && codes[1].length()>0)
    	{
    		objectcode = codes[1];
    	}
    	
    	
    	if(clazz.equals(SearchReturnObject.OBJECT))
    	{
    		if(codes.length!=2) {
    			log.debug("Wrong number of codes for object when splitting: "+code); 
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
    		if(codes.length!=3) {
    			log.debug("Wrong number of codes for element when splitting: "+code); 
    			return null;
    		}

    		param.addSearchConstraint(SearchParameterName.TOPOBJECTCODE, SearchOperator.EQUALS, codes[0]);
    		if(codes[1]!=null && !codes[1].equals("NULL") && codes[1].length()>0) param.addSearchConstraint(SearchParameterName.SUBOBJECTCODE, SearchOperator.EQUALS, codes[1]);    		param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, codes[2]);
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
    		if(codes.length!=4) {
    			log.debug("Wrong number of codes for sample when splitting: "+code); 
    			return null;
    		}
    		param.addSearchConstraint(SearchParameterName.TOPOBJECTCODE, SearchOperator.EQUALS, codes[0]);
    		if(codes[1]!=null && !codes[1].equals("NULL") && codes[1].length()>0) param.addSearchConstraint(SearchParameterName.SUBOBJECTCODE, SearchOperator.EQUALS, codes[1]);    		
    		param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, codes[2]);
    		param.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.EQUALS, codes[3]);
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
    		if(codes.length!=5) {
    			log.debug("Wrong number of codes for radius when splitting: "+code); 
    			return null;
    		}
    		param.addSearchConstraint(SearchParameterName.TOPOBJECTCODE, SearchOperator.EQUALS, codes[0]);
    		if(codes[1]!=null && !codes[1].equals("NULL") && codes[1].length()>0) param.addSearchConstraint(SearchParameterName.SUBOBJECTCODE, SearchOperator.EQUALS, codes[1]);    		
    		param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, codes[2]);
    		param.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.EQUALS, codes[3]);
    		param.addSearchConstraint(SearchParameterName.RADIUSCODE, SearchOperator.EQUALS, codes[4]);
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
    		if(codes.length!=6) {
    			log.debug("Wrong number of codes for series when splitting: "+code); 
    			return null;
    		}
    		param.addSearchConstraint(SearchParameterName.TOPOBJECTCODE, SearchOperator.EQUALS, codes[0]);
    		if(codes[1]!=null && !codes[1].equals("NULL") && codes[1].length()>0) param.addSearchConstraint(SearchParameterName.SUBOBJECTCODE, SearchOperator.EQUALS, codes[1]);
    		param.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, codes[2]);
    		param.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.EQUALS, codes[3]);
    		param.addSearchConstraint(SearchParameterName.RADIUSCODE, SearchOperator.EQUALS, codes[4]);
    		param.addSearchConstraint(SearchParameterName.SERIESCODE, SearchOperator.EQUALS, codes[5]);
    		EntitySearchResource<TridasMeasurementSeries> resource = new EntitySearchResource<TridasMeasurementSeries>(param, TridasMeasurementSeries.class);
			resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);

    		dialog = new TellervoResourceAccessDialog(resource);
    		resource.query();	
    		dialog.setVisible(true);
    		
    		if(!dialog.isSuccessful()) 
    		{ 
    			return null;
    		}
    		
    		if(resource.getAssociatedResult()!=null && resource.getAssociatedResult().size()>0)
    		{
    			
    			TridasMeasurementSeries got = resource.getAssociatedResult().get(0);
    			return got;
    			
    		}
    		else
    		{
    			return null;
    		}
    	}
		return null;
	}

	public void generateMissingEntities(DefaultEntityParametersDialog defaults)
	{
		for(SeriesIdentity id : this.ids)
		{
			if(id.getObjectItem().isInDatabase()==false && 
					id.getObjectItem().getCode()!=null && 
					id.getObjectItem().getCode().length()>0)
			{				
				TridasObjectEx object = new TridasObjectEx();
				object.setTitle(id.getObjectItem().getCode());
				TridasUtils.setObjectCode(object, id.getObjectItem().getCode());
				object.setType((ControlledVoc) defaults.cboObjectType.getSelectedItem());
				
				object = (TridasObjectEx) doSave(object, null);
				
				if(object!=null)
				{
					tridasCache.put(id.getObjectItem().getCode()+ codeDelimiter+"NULL", object);
				}
				
				searchForMatches(true);
			}
			
			if(includeSubObjects)
			{
				if(id.getSubObjectItem().isInDatabase()==false && 
						id.getSubObjectItem().getCode()!=null && 
						id.getSubObjectItem().getCode().length()>0)
				{				
					TridasObjectEx object = new TridasObjectEx();
					object.setTitle(id.getSubObjectItem().getCode());
					TridasUtils.setObjectCode(object, id.getSubObjectItem().getCode());
					object.setType((ControlledVoc) defaults.cboObjectType.getSelectedItem());
					
					
					String parentCode = id.getObjectItem().getCode() + codeDelimiter + "NULL";
					String thisCode   =id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode();
					object = (TridasObjectEx) doSave(object, tridasCache.get(parentCode));
					
					if(object!=null)
					{
						tridasCache.put(thisCode, object);
					}

					searchForMatches(true);

				}
			}
			
			
			if(id.getElementItem().isInDatabase()==false && 
					id.getElementItem().getCode()!=null && 
					id.getElementItem().getCode().length()>0)
			{
				TridasElement element = new TridasElement();
				element.setTitle(id.getElementItem().getCode());
				element.setType((ControlledVoc) defaults.cboElementType.getSelectedItem());
				element.setTaxon((ControlledVoc) defaults.cboTaxon.getSelectedItem());

				String parentCode = id.getObjectItem().getCode()+ codeDelimiter +"NULL";		
				if(includeSubObjects)
				{
					parentCode = id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode();
				}
				String thisCode   = parentCode + codeDelimiter + id.getElementItem().getCode();
				element = (TridasElement) doSave(element, tridasCache.get(parentCode));
				
				if(element!=null)
				{
					tridasCache.put(thisCode, element);
				}

				searchForMatches(true);
			}
			
			if(id.getSampleItem().isInDatabase()==false && 
					id.getSampleItem().getCode()!=null && 
					id.getSampleItem().getCode().length()>0)
			{
				TridasSample sample = new TridasSample();
				sample.setTitle(id.getSampleItem().getCode());
				sample.setType((ControlledVoc) defaults.cboSampleType.getSelectedItem());


				String oCodes = id.getObjectItem().getCode()+ codeDelimiter +"NULL";		
				if(includeSubObjects)
				{
					oCodes = id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode();
				}
				
				String parentCode = oCodes + codeDelimiter + id.getElementItem().getCode();
				String thisCode = parentCode + codeDelimiter + id.getSampleItem().getCode();
				
				ITridas parentitem = tridasCache.get(parentCode);
				sample = (TridasSample) doSave(sample, parentitem);
				
				if(sample!=null)
				{
					tridasCache.put(thisCode, sample);
				}
				
				searchForMatches(true);
			}
						
			if(id.getRadiusItem().isInDatabase()==false && 
					id.getRadiusItem().getCode()!=null && 
					id.getRadiusItem().getCode().length()>0)
			{
				TridasRadius radius = new TridasRadius();
				radius.setTitle(id.getRadiusItem().getCode());
				
				TridasWoodCompleteness wc = new ObjectFactory().createTridasWoodCompleteness();
				
				TridasPith pith = new ObjectFactory().createTridasPith();
				TridasHeartwood heartwd = new ObjectFactory().createTridasHeartwood();
				TridasSapwood sapwd = new ObjectFactory().createTridasSapwood();
				TridasBark bark = new ObjectFactory().createTridasBark();
				
				pith.setPresence(ComplexPresenceAbsence.UNKNOWN);
				heartwd.setPresence(ComplexPresenceAbsence.UNKNOWN);
				sapwd.setPresence(ComplexPresenceAbsence.UNKNOWN);
				bark.setPresence(PresenceAbsence.ABSENT);
				
				wc.setPith(pith);
				wc.setHeartwood(heartwd);
				wc.setSapwood(sapwd);
				wc.setBark(bark);
				radius.setWoodCompleteness(wc);
				
				String oCodes = id.getObjectItem().getCode()+ codeDelimiter +"NULL";		
				if(includeSubObjects)
				{
					oCodes = id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode();
				}
				
				String parentCode = oCodes + codeDelimiter + id.getElementItem().getCode() + codeDelimiter + id.getSampleItem().getCode();
				String thisCode   = parentCode + codeDelimiter + id.getRadiusItem().getCode();
				
				radius = (TridasRadius) doSave(radius, tridasCache.get(parentCode));
				
				if(radius!=null)
				{
					tridasCache.put(thisCode, radius);
				}
				searchForMatches(true);
			}
			
			if(id.getSeriesItem().isInDatabase()==false&& 
					id.getSeriesItem().getCode()!=null && 
					id.getSeriesItem().getCode().length()>0)
			{
				// Get the series provided by the TRiCYCLE reader
				ITridasSeries readerpopulatedseries = (ITridasSeries) id.getSample().getSeries();
				
				// Override the code with that provided by the user
				readerpopulatedseries.setTitle(id.getSeriesItem().getCode());
				
				
				if(readerpopulatedseries instanceof TridasMeasurementSeries)
				{
				
					TridasMeasuringMethod mm = new TridasMeasuringMethod();
					mm.setNormalTridas(NormalTridasMeasuringMethod.MEASURING_PLATFORM);
					((TridasMeasurementSeries) readerpopulatedseries).setMeasuringMethod(mm);
				}
				
				String oCodes = id.getObjectItem().getCode()+ codeDelimiter +"NULL";		
				if(includeSubObjects)
				{
					oCodes = id.getObjectItem().getCode() + codeDelimiter + id.getSubObjectItem().getCode();
				}
				
				String parentCode = oCodes + codeDelimiter + id.getElementItem().getCode() + codeDelimiter + id.getSampleItem().getCode() + codeDelimiter + id.getRadiusItem().getCode();
				String thisCode   = parentCode + codeDelimiter + id.getSeriesItem().getCode();

				ITridasSeries dbseries = (ITridasSeries) doSave(readerpopulatedseries, tridasCache.get(parentCode));
						
				if(dbseries!=null)
				{ 
					id.getSample().setSeries((ITridasSeries) dbseries);
					tridasCache.put(thisCode, dbseries);
				}
				searchForMatches(true);
			}

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
	
	/**
	 * Confirm whether any entries in the table are missing from the database.  The ignoreMissingSeries flag 
	 * sets whether you'd like to ignore missing series.
	 * 
	 * @param ignoreMissingSeries
	 * @return
	 */
	public boolean areThereMissingEntites(boolean ignoreMissingSeries)
	{
		for(SeriesIdentity id : this.ids)
		{
			if(id.getObjectItem().isInDatabase()==false) return true;
			if(id.getElementItem().isInDatabase()==false) return true;
			if(id.getSampleItem().isInDatabase()==false) return true;
			if(id.getRadiusItem().isInDatabase()==false) return true;
			if(!ignoreMissingSeries && id.getSeriesItem().isInDatabase()==false) return true;
		}
		
		return false;
	}
	
	public boolean areThereEmptyCells(boolean includeSubObjects)
	{
		for(int row=0; row<this.getRowCount(); row++)
		{
			for(int col=3; col<this.getColumnCount(); col++)
			{
				if(!includeSubObjects && col==4) continue;
				
				if(this.getValueAt(row, col)==null)
				{
					return true;
				}
				
				String asstr = this.getValueAt(row, col).toString();
				
				if (asstr==null || asstr.length()==0)
				{
					return true;
				}
			}
		}
		
		
		return false;
	}
	
	public ArrayList<Sample> getAllSamples(boolean includeSubObjects)
	{
		
		ArrayList<Sample> samples = new ArrayList<Sample>();
		
		for(SeriesIdentity id : ids)
		{
			samples.add(getPopulatedSampleFromSeriesIdentity(id, includeSubObjects));
		}
		
		
		return samples;
		
	}
	
	/**
	 * Populate the given Tellervo sample from the information we acquired
	 * 
	 * @param s
	 */
	private Sample getPopulatedSampleFromSeriesIdentity(SeriesIdentity id, boolean includeSubObjects) {
		LabCode labcode = new LabCode();
		
		Sample s = id.getSample();

		TridasObjectEx object = (TridasObjectEx) tridasCache.get(id.getObjectItem().getCode());
		TridasObjectEx subobject = (TridasObjectEx) tridasCache.get(id.getObjectItem().getCode() + codeDelimiter +  tridasCache.get(id.getSubObjectItem().getCode()));
		TridasElement element = (TridasElement) tridasCache.get(id.getObjectItem().getCode() + codeDelimiter + tridasCache.get(id.getSubObjectItem().getCode()) + codeDelimiter + id.getElementItem().getCode()); 
		TridasSample sample = (TridasSample) tridasCache.get(id.getObjectItem().getCode() + codeDelimiter + tridasCache.get(id.getSubObjectItem().getCode()) + codeDelimiter + id.getElementItem().getCode()  + codeDelimiter + id.getSampleItem().getCode());
		TridasRadius radius = (TridasRadius) tridasCache.get(id.getObjectItem().getCode() + codeDelimiter + tridasCache.get(id.getSubObjectItem().getCode()) + codeDelimiter + id.getElementItem().getCode()  + codeDelimiter + id.getSampleItem().getCode() + codeDelimiter + id.getRadiusItem().getCode());
				
		
		ITridasSeries series = s.getSeries();
		//TellervoWSILoader.attachNewSample(s);

		if(includeSubObjects)
		{
			if(subobject != null) {
				s.setMeta(Metadata.OBJECT, subobject);
				
				
				if(subobject instanceof TridasObjectEx){
					labcode.appendSiteCode(((TridasObjectEx)subobject).getLabCode());
				}
				else{
					labcode.appendSiteCode(subobject.getTitle());
				}
				
			}
		}
		else
		{
			if(object != null) {
				s.setMeta(Metadata.OBJECT, object);
				
				
				if(object instanceof TridasObjectEx){
					labcode.appendSiteCode(((TridasObjectEx)object).getLabCode());
				}
				else{
					labcode.appendSiteCode(object.getTitle());
				}
				
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
			
			
			if(!series.isSetIdentifier())
			{
				
				TridasIdentifier tid = NewTridasIdentifier.getInstance("unknown");
				series.setIdentifier(tid);
			}
			TellervoWSILoader loader = new TellervoWSILoader(series.getIdentifier());

			s.setSeries(series);
			s.setLoader(loader);
			s.setMeta(Metadata.LABCODE, labcode);
			s.setMeta(Metadata.TITLE, LabCodeFormatter.getDefaultFormatter().format(labcode));
		}
		else
		{
			log.error("No series!");
			return null;
		}
	
		
		
		
		return s;
	}
	
	/**
	 * For creating a new entity on the server
	 * 
	 * @param <T>
	 * @param entity
	 * @param parent
	 * @param type
	 * @return
	 */
	private <T extends ITridas> EntityResource<T> getNewAccessorResource(ITridas entity, ITridas parent, Class<T> type) {
		return new EntityResource<T>(entity, parent, type);
	}
	
	private ITridas doSave(ITridas temporaryEditingEntity, ITridas parentEntity) {
		Class<? extends ITridas> type = temporaryEditingEntity.getClass();
		
		log.debug("Attempting to save TRiDaS entity...");
	  	log.debug("    - Entity type  : "+type);
		if(parentEntity!=null && parentEntity.isSetTitle())
		{
			log.debug("    - Parent title : "+parentEntity.getTitle());
		}
		else if (parentEntity==null)
		{
			log.debug("    - Parent IS NULL!!!");
		}
		if(temporaryEditingEntity!=null && temporaryEditingEntity.isSetTitle())
		{
			log.debug("    - Entity title : "+temporaryEditingEntity.getTitle());
		}
		
		// the resource we'll use
		TellervoEntityAssociatedResource resource;
		
		if(temporaryEditingEntity instanceof TridasObjectEx)
		{
			resource = getNewAccessorResource((TridasObjectEx) temporaryEditingEntity, parentEntity, TridasObjectEx.class);
		}
		else if (temporaryEditingEntity instanceof TridasElement)
		{
			resource = getNewAccessorResource((TridasElement)temporaryEditingEntity, parentEntity, TridasElement.class);
		}
		else if (temporaryEditingEntity instanceof TridasSample)
		{
			resource = getNewAccessorResource((TridasSample)temporaryEditingEntity, parentEntity, TridasSample.class);
		}
		else if (temporaryEditingEntity instanceof TridasRadius)
		{
			resource = getNewAccessorResource((TridasRadius)temporaryEditingEntity, parentEntity, TridasRadius.class);
		}
		else if (temporaryEditingEntity instanceof TridasMeasurementSeries)
		{
			resource = new SeriesResource((ITridasSeries)temporaryEditingEntity, parentEntity.getIdentifier().getValue(), TellervoRequestType.CREATE);
		}
		
		else 
		{
			log.debug("Can't save entity.  Unsupported entity class.");
			return null;
		}
		
		// set up a dialog...
		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(parent, resource);

		// query the resource
		resource.query();
		dialog.setVisible(true);
		
		// on failure, just return
		if(!dialog.isSuccessful()) {
			JOptionPane.showMessageDialog(null, I18n.getText("error.savingChanges") + "\r\n" +
					I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
					I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		log.debug(" Entity saved successfully");
		
		// replace the saved result

		
		if (temporaryEditingEntity instanceof TridasMeasurementSeries)
		{
			try{
				ArrayList<Sample> savedEntity = (ArrayList<Sample>) resource.getAssociatedResult();
				// sanity check the result
				if(savedEntity == null) {
					new BugDialog(new IllegalStateException("CREATE entity returned null"));
					return null;
				}
	
				return (ITridas) savedEntity.get(0).getSeries();
				
			} catch (IllegalStateException e)
			{
				new BugDialog(e);
				return null;
			}
		}
		else
		{
			try{
				ITridas savedEntity = (ITridas) resource.getAssociatedResult();
				// sanity check the result
				if(savedEntity == null) {
					new BugDialog(new IllegalStateException("CREATE entity returned null"));
					return null;
				}
	
				return savedEntity;
			} catch (IllegalStateException e)
			{
				new BugDialog(e);
				return null;
			}
		}
		
		
		

	}
	
}
