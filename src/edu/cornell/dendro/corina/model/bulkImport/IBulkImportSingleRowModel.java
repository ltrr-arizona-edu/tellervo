/**
 * Created at Aug 1, 2010, 3:07:26 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import org.tridas.schema.TridasIdentifier;

import com.dmurph.mvc.ICloneable;
import com.dmurph.mvc.model.HashModel.PropertyType;

/**
 * @author daniel
 *
 */
public interface IBulkImportSingleRowModel extends ICloneable{
	public static final String IMPORTED = "Imported";
	
	public TridasIdentifier getImported();
	
	public Object setProperty(String argName, Object argProperty);
	
	public Object getProperty(String argName);
	
	public PropertyType getPropertyType(String argName);
}
