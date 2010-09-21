/**
 * Created on Jul 17, 2010, 1:35:12 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import edu.cornell.dendro.corina.gis.GPXParser.GPXWaypoint;
import edu.cornell.dendro.corina.schema.WSIObjectTypeDictionary;

/**
 * @author Daniel Murphy
 *
 */
public class ObjectTableModel extends AbstractBulkImportTableModel {
	private static final long serialVersionUID = 2L;

	public ObjectTableModel(ObjectModel argModel){
		super(argModel);
	}
	
	
	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.AbstractBulkImportTableModel#getColumnClass(java.lang.String)
	 */
	@Override
	public Class<?> getColumnClass(String argColumn) {
		// for combo box stuff
		if(argColumn.equals(SingleObjectModel.TYPE)){
			return WSIObjectTypeDictionary.class;
		}else if(argColumn.equals(SingleObjectModel.IMPORTED)){
			return Boolean.class;
		}else if(argColumn.equals(SingleObjectModel.LATITUDE)){
			return Double.class;
		}else if(argColumn.equals(SingleObjectModel.LONGTITUDE)){
			return Double.class;
		}else if(argColumn.equals(SingleObjectModel.WAYPOINT)){
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
		if(argColumn.equals(SingleObjectModel.WAYPOINT))
		{
			GPXWaypoint wp = (GPXWaypoint) argAValue;
			argModel.setProperty(SingleObjectModel.LATITUDE, wp.getLatitude());
			argModel.setProperty(SingleObjectModel.LONGTITUDE, wp.getLongitude());
		}
		// If it's lat/long data, remove the waypoint
		if(argColumn.equals(SingleObjectModel.LATITUDE) || argColumn.equals(SingleObjectModel.LONGTITUDE)){
			argModel.setProperty(SingleObjectModel.WAYPOINT, null);
		}
				
		argModel.setProperty(argColumn, argAValue);
	}
}
