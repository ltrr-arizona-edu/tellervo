/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
/**
  * Created on Jul 13, 2010, 6:28:52 PM
 */
package org.tellervo.desktop.bulkdataentry.model;

import java.util.List;

import net.opengis.gml.schema.PointType;
import net.opengis.gml.schema.Pos;

import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.schema.TridasObject;

import com.dmurph.mvc.model.HashModel;


/**
 * @author Daniel Murphy
 *
 */
public class SingleObjectModel extends HashModel implements IBulkImportSingleRowModel{
	private static final long serialVersionUID = 1L;
	
	public static final String OBJECT_CODE = "Object Code";
	public static final String TITLE = "Title";
	public static final String COMMENTS = "Comments";
	public static final String TYPE = "Type";
	public static final String DESCRIPTION = "Description";
	public static final String LATITUDE = "Latitude";
	public static final String LONGTITUDE = "Longtitude";
	public static final String WAYPOINT = "Waypoint";
	public static final String PARENT_OBJECT = "Parent Object";
	public static final String ADDRESSLINE1 = "Address 1";
	public static final String ADDRESSLINE2 = "Address 2";
	public static final String CITY_TOWN = "City/Town";
	public static final String STATE_PROVINCE_REGION = "State/Province/Region";
	public static final String POSTCODE = "Postal Code";
	public static final String COUNTRY = "Country";
	public static final String LOCATION_PRECISION = "Location precision";
	public static final String LOCATION_COMMENT = "Location comment";
	public static final String OWNER = "Owner";
	public static final String CREATOR = "Creator";
	
	// Not implemented yet
	//public static final String LOCATION_TYPE = "Location type";
	//public static final String FILES = "File references";
	//public static final String COVERAGE_TEMPORAL = "Time period";
	//public static final String COVERAGE_TEMPORAL_FOUNDATION = "Time period foundation";
	
	
	public static final String[] TABLE_PROPERTIES = {
		PARENT_OBJECT, OBJECT_CODE, TITLE, TYPE, DESCRIPTION, COMMENTS, LATITUDE, LONGTITUDE, WAYPOINT, LOCATION_PRECISION, LOCATION_COMMENT,
		ADDRESSLINE1, ADDRESSLINE2,	CITY_TOWN, STATE_PROVINCE_REGION, POSTCODE, COUNTRY,  OWNER, CREATOR
	};
	
	public SingleObjectModel(){
		registerProperty(TABLE_PROPERTIES, PropertyType.READ_WRITE);
		registerProperty(IMPORTED, PropertyType.READ_ONLY, null);
	}
	
	
	public void setImported(TridasIdentifier argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
	
	public void setWaypoint(GPXWaypoint wp)
	{
		registerProperty(WAYPOINT, PropertyType.READ_WRITE, null);
	}
	
	public TridasIdentifier getImported(){
		return (TridasIdentifier)getProperty(IMPORTED);
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
	
	public void populateFromTridasObject(TridasObject argObject){
		List<TridasGenericField> fields = argObject.getGenericFields();
		
		setImported(argObject.getIdentifier());
		
		boolean found = false;
		for(TridasGenericField field : fields){
			if(field.getName().equals("tellervo.objectLabCode")){
				setProperty(OBJECT_CODE, field.getValue());
				found = true;
			}
		}
		if(!found){
			setProperty(OBJECT_CODE, null);
		}
		
		setProperty(TITLE, argObject.getTitle());
		setProperty(COMMENTS, argObject.getComments());
		if(argObject.getType() != null){
			setProperty(TYPE, argObject.getType());			
		}else{
			setProperty(TYPE, null);
		}
		setProperty(DESCRIPTION, argObject.getDescription());
		
		// i love how nested this is!
		if(argObject.getLocation() != null &&
				argObject.getLocation().getLocationGeometry() != null&&
				argObject.getLocation().getLocationGeometry().getPoint() != null &&
				argObject.getLocation().getLocationGeometry().getPoint().getPos() != null &&
				argObject.getLocation().getLocationGeometry().getPoint().getPos().getValues().size() == 2){
			setProperty(LATITUDE, argObject.getLocation().getLocationGeometry().getPoint().getPos().getValues().get(0));
			setProperty(LONGTITUDE, argObject.getLocation().getLocationGeometry().getPoint().getPos().getValues().get(1));
		}else{
			setProperty(LATITUDE, null);
			setProperty(LONGTITUDE, null);
		}
		
		if(argObject.isSetLocation() &&
				argObject.getLocation().isSetAddress() &&
				argObject.getLocation().getAddress().isSetAddressLine1())
		{
			setProperty(ADDRESSLINE1, argObject.getLocation().getAddress().getAddressLine1());
		}
		
		if(argObject.isSetLocation() &&
				argObject.getLocation().isSetAddress() &&
				argObject.getLocation().getAddress().isSetAddressLine2())
		{
			setProperty(ADDRESSLINE2, argObject.getLocation().getAddress().getAddressLine2());
		}
		
		if(argObject.isSetLocation() &&
				argObject.getLocation().isSetAddress() &&
				argObject.getLocation().getAddress().isSetCityOrTown())
		{
			setProperty(CITY_TOWN, argObject.getLocation().getAddress().getCityOrTown());
		}
		
		if(argObject.isSetLocation() &&
				argObject.getLocation().isSetAddress() &&
				argObject.getLocation().getAddress().isSetCountry())
		{
			setProperty(COUNTRY, argObject.getLocation().getAddress().getCountry());
		}
		
		if(argObject.isSetLocation() &&
				argObject.getLocation().isSetAddress() &&
				argObject.getLocation().getAddress().isSetPostalCode())
		{
			setProperty(POSTCODE, argObject.getLocation().getAddress().getPostalCode());
		}
		
		if(argObject.isSetLocation() &&
				argObject.getLocation().isSetAddress() &&
				argObject.getLocation().getAddress().isSetStateProvinceRegion())
		{
			setProperty(STATE_PROVINCE_REGION, argObject.getLocation().getAddress().getStateProvinceRegion());
		}
		
		if(argObject.isSetLocation() &&
				argObject.getLocation().isSetLocationPrecision())
		{
			setProperty(LOCATION_PRECISION, argObject.getLocation().getLocationPrecision());
		}
		
		if(argObject.isSetLocation() &&
				argObject.getLocation().isSetLocationComment())
		{
			setProperty(LOCATION_COMMENT, argObject.getLocation().getLocationComment());
		}
		
		if(argObject.isSetOwner())
		{
			setProperty(OWNER, argObject.getOwner());
		}
		
		if(argObject.isSetCreator())
		{
			setProperty(CREATOR, argObject.getCreator());
		}
		
		setProperty(IMPORTED, argObject.getIdentifier());
	}
	
	public void populateTridasObject(TridasObject argObject){
		TridasGenericField codeField = new TridasGenericField();
		codeField.setName("tellervo.objectLabCode");
		codeField.setValue(getProperty(OBJECT_CODE)+"");
		argObject.getGenericFields().add(codeField);
		
		argObject.setTitle((String)getProperty(TITLE));
		argObject.setIdentifier((TridasIdentifier) getProperty(IMPORTED));
		argObject.setComments((String)getProperty(COMMENTS));
		argObject.setType((ControlledVoc) getProperty(TYPE));
		argObject.setDescription((String) getProperty(DESCRIPTION));
		argObject.setOwner((String) getProperty(OWNER));
		argObject.setCreator((String) getProperty(CREATOR));
		
		
		Object latitude = getProperty(LATITUDE);
		Object longitude = getProperty(LONGTITUDE);
		Object addressline1 = getProperty(ADDRESSLINE1);
		Object addressline2 = getProperty(ADDRESSLINE2);
		Object city = getProperty(CITY_TOWN);
		Object state = getProperty(STATE_PROVINCE_REGION);
		Object postcode = getProperty(POSTCODE);
		Object country = getProperty(COUNTRY);
		Object locprecision = getProperty(LOCATION_PRECISION);
		Object loccomment = getProperty(LOCATION_COMMENT);

		
		if((latitude != null && longitude != null) || 
			addressline1 !=null ||
			addressline2 !=null ||
			city != null ||
			state != null ||
			postcode != null ||
			country != null ||
			locprecision != null ||
			loccomment != null
			){
			
			TridasLocation loc = new TridasLocation();
			
			loc.setLocationPrecision((String) locprecision);
			loc.setLocationComment((String) loccomment);
			
			if(latitude != null && longitude != null)
			{
				// Lat/Long is set so use these
				double lat = (Double)latitude;
				double lon = (Double)longitude;
				Pos p = new Pos();
				p.getValues().add(lat);
				p.getValues().add(lon);
				
				PointType pt = new PointType();
				pt.setPos(p);
				
				TridasLocationGeometry locgeo = new TridasLocationGeometry();
				locgeo.setPoint(pt);
				
				
				loc.setLocationGeometry(locgeo);
			}
					
			if(addressline1 !=null ||
				addressline2 !=null ||
				city != null ||
				state != null ||
				postcode != null ||
				country != null ||
				locprecision != null ||
				loccomment != null
			)
			{
				TridasAddress addr = new TridasAddress();
				addr.setAddressLine1((String)addressline1);
				addr.setAddressLine2((String)addressline2);
				addr.setCityOrTown((String)city);
				addr.setCountry((String) country);
				addr.setPostalCode((String)postcode);
				addr.setStateProvinceRegion((String)state);
				loc.setAddress(addr);
			}
			else
			{
				loc.setAddress(null);
			}
			argObject.setLocation(loc);		
		}else{
			argObject.setLocation(null);
		}
	}
}
