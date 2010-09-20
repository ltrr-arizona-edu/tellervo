/**
 * Created on Jul 16, 2010, 7:34:07 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import java.math.BigDecimal;

import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasRadius;

import com.dmurph.mvc.model.HashModel;

/**
 * @author Daniel Murphy
 *
 */
public class SingleRadiusModel extends HashModel implements ISingleRowModel{
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Radius Title";
	public static final String COMMENTS = "Radius Comments";
	public static final String AZIMUTH = "Radius Azimuth";
	
	public static final String[] PROPERTIES = {
		TITLE, COMMENTS, AZIMUTH
	};
	
	public SingleRadiusModel(){
		registerProperty(PROPERTIES, PropertyType.READ_WRITE);
		registerProperty(IMPORTED, PropertyType.READ_ONLY, false);
	}
	
	public void setImported(TridasIdentifier argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
	
	/**
	 * @see com.dmurph.mvc.support.AbstractMVCSupport#cloneImpl(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object cloneImpl(String argProperty, Object argO) {
		if(argProperty.equals(IMPORTED)){
			return null;
		}
		return super.cloneImpl(argProperty, argO);
	}
	
	public void populateToTridasRadius(TridasRadius argTridasRadius){
		argTridasRadius.setTitle((String) getProperty(TITLE));
		argTridasRadius.setAzimuth((BigDecimal) getProperty(AZIMUTH));
		argTridasRadius.setComments((String) getProperty(COMMENTS));
		argTridasRadius.setIdentifier((TridasIdentifier) getProperty(IMPORTED));
	}
	
	public void populateFromTridasRadius(TridasRadius argTridasRadius){
		setProperty(TITLE, argTridasRadius.getTitle());
		setProperty(COMMENTS, argTridasRadius.getComments());
		setProperty(AZIMUTH, argTridasRadius.getAzimuth());
	}
}
