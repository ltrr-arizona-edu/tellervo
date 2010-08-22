/**
  * Created on Jul 13, 2010, 6:28:52 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import java.util.List;

import net.opengis.gml.schema.PointType;
import net.opengis.gml.schema.Pos;

import org.tridas.schema.ControlledVoc;
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
public class SingleObjectModel extends HashModel implements ISingleRowModel{
	private static final long serialVersionUID = 1L;
	
	public static final String OBJECT_CODE = "Object Code";
	public static final String TITLE = "Title";
	public static final String COMMENTS = "Comments";
	public static final String TYPE = "Type";
	public static final String DESCRIPTION = "Description";
	public static final String LATITUDE = "Latitude";
	public static final String LONGTITUDE = "Longtitude";
	
	
	public static final String[] TABLE_PROPERTIES = {
		OBJECT_CODE, TITLE, COMMENTS, TYPE, DESCRIPTION, LATITUDE, LONGTITUDE
	};
	
	public SingleObjectModel(){
		registerProperty(TABLE_PROPERTIES, PropertyType.READ_WRITE);
		registerProperty(IMPORTED, PropertyType.READ_ONLY, null);
	}
	
	
	public void setImported(TridasIdentifier argImported){
		registerProperty(IMPORTED, PropertyType.READ_ONLY, argImported);
	}
	
	public TridasIdentifier getImported(){
		return (TridasIdentifier)getProperty(IMPORTED);
	}
	
	public void populateFromTridasObject(TridasObject argObject){
		List<TridasGenericField> fields = argObject.getGenericFields();
		
		setImported(argObject.getIdentifier());
		
		boolean found = false;
		for(TridasGenericField field : fields){
			if(field.getName().equals("corina.objectLabCode")){
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
		
		// stupid location
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
	}
	
	public void populateTridasObject(TridasObject argObject){
		TridasGenericField codeField = new TridasGenericField();
		codeField.setName("corina.objectLabCode");
		codeField.setValue(getProperty(OBJECT_CODE)+"");
		argObject.getGenericFields().add(codeField);
		
		Object title = getProperty(TITLE);
		Object identifier = getProperty(IMPORTED);
		Object comments = getProperty(COMMENTS);
		Object type = getProperty(TYPE);
		Object description = getProperty(DESCRIPTION);
		Object latitude = getProperty(LATITUDE);
		Object longtitude = getProperty(LONGTITUDE);
		
		if(title != null){
			argObject.setTitle(title.toString());
		}
		
		if(identifier != null){
			argObject.setIdentifier((TridasIdentifier) identifier);
		}
		
		if(comments != null){
			argObject.setComments(comments.toString());
		}
		
		if(type != null){
			argObject.setType((ControlledVoc) type);
		}
		
		if(description != null){
			argObject.setDescription(description.toString());
		}
		
		if(latitude != null && longtitude != null){
			try{
				double lat = Double.parseDouble(latitude.toString().trim());
				double lon = Double.parseDouble(longtitude.toString().trim());
				Pos p = new Pos();
				p.getValues().add(lat);
				p.getValues().add(lon);
				
				PointType pt = new PointType();
				pt.setPos(p);
				
				TridasLocationGeometry locgeo = new TridasLocationGeometry();
				locgeo.setPoint(pt);
				
				TridasLocation loc = new TridasLocation();
				loc.setLocationGeometry(locgeo);
				argObject.setLocation(loc);
			}catch(NumberFormatException e){
				// don't do anything for now
				e.printStackTrace();
			}
		}
	}
}
