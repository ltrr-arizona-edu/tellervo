/**
 * Created on Aug 18, 2010, 1:12:40 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import java.math.BigDecimal;

import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasUnit;

import edu.cornell.dendro.corina.gis.GPXParser.GPXWaypoint;
import edu.cornell.dendro.corina.schema.WSIElementTypeDictionary;
import edu.cornell.dendro.corina.schema.WSITaxonDictionary;

/**
 * @author Daniel Murphy
 *
 */
public class ElementTableModel extends AbstractBulkImportTableModel {
	private static final long serialVersionUID = 2L;
	
	public ElementTableModel(ElementModel argModel){
		super(argModel);
	}
	
	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.AbstractBulkImportTableModel#getColumnClass(java.lang.String)
	 */
	public Class<?> getColumnClass(String argColumn){
		if(argColumn.equals(SingleElementModel.TYPE)){
			return WSIElementTypeDictionary.class;
		}else if(argColumn.equals(SingleElementModel.IMPORTED)){
			return Boolean.class;
		}else if(argColumn.equals(SingleElementModel.OBJECT)){
			return TridasObject.class;
		}else if(argColumn.equals(SingleElementModel.DEPTH)){
			return BigDecimal.class;
		}else if(argColumn.equals(SingleElementModel.WIDTH)){
			return BigDecimal.class;
		}else if(argColumn.equals(SingleElementModel.DIAMETER)){
			return BigDecimal.class;
		}else if(argColumn.equals(SingleElementModel.HEIGHT)){
			return BigDecimal.class;
		}else if(argColumn.equals(SingleElementModel.LATITUDE)){
			return Double.class;
		}else if(argColumn.equals(SingleElementModel.LONGTITUDE)){
			return Double.class;
		}else if(argColumn.equals(SingleElementModel.SLOPE_ANGLE)){
			return Integer.class;
		}else if(argColumn.equals(SingleElementModel.SLOPE_AZIMUTH)){
			return Integer.class;
		}else if(argColumn.equals(SingleElementModel.SOIL_DEPTH)){
			return Double.class;
		}else if(argColumn.equals(SingleElementModel.SHAPE)){
			return TridasShape.class;
		}else if(argColumn.equals(SingleElementModel.TAXON)){
			return WSITaxonDictionary.class;
		}else if(argColumn.equals(SingleElementModel.UNIT)){
			return TridasUnit.class;
		}else if(argColumn.equals(SingleElementModel.WAYPOINT)){
			return GPXWaypoint.class;
		}
		return null;
	}
	
	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.AbstractBulkImportTableModel#setValueAt(java.lang.Object, java.lang.String, edu.cornell.dendro.corina.model.bulkImport.IBulkImportSingleRowModel, int)
	 */
	@Override
	public void setValueAt(Object argAValue, String argColumn, IBulkImportSingleRowModel argModel, int argRowIndex) {
		// TODO tie this into a command, only commands can modify the model!
		
		// If it's a waypoint set the lat and long
		if(argColumn.equals(SingleElementModel.WAYPOINT))
		{
			GPXWaypoint wp = (GPXWaypoint) argAValue;
			argModel.setProperty(SingleElementModel.LATITUDE, wp.getLatitude());
			argModel.setProperty(SingleElementModel.LONGTITUDE, wp.getLongitude());
		}
		// If it's lat/long data, remove the waypoint
		if(argColumn.equals(SingleElementModel.LATITUDE) || argColumn.equals(SingleElementModel.LONGTITUDE)){
			argModel.setProperty(SingleElementModel.WAYPOINT, null);
		}
		
		argModel.setProperty(argColumn, argAValue);
	}
}