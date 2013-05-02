package org.tellervo.desktop.tridasv2.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import org.tellervo.desktop.tridasv2.ui.support.TridasEntityProperty;
import org.tridas.schema.TridasEntity;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

/**
 * TellervoSpecificGenericFields
 * 
 * 
 * @author pwb48
 *
 */
public class TellervoGenericFieldProperty extends TridasEntityProperty {

	private static final long serialVersionUID = 1L;
	private String xmlFieldName;
	private Class<? extends TridasEntity> parentClass;
	
	public TellervoGenericFieldProperty(String qname, String lname, String xmlFieldName, Class clazz, 
			Class<? extends TridasEntity> parentClass, Boolean isRequired, Boolean isReadOnly) {
		super(qname, lname);
		this.xmlFieldName = xmlFieldName;
		this.parentClass = parentClass;
		
		for (Field f : TridasGenericField.class.getDeclaredFields())
		{
			if(f.getName().equals("value"))
			{
				setType(clazz, f);
				setReadOnly(isReadOnly);
				required = isRequired;
			}
		}
	}
		
	public TridasGenericField getTridasGenericField()
	{
		TridasGenericField gf = new TridasGenericField();
		gf.setName(xmlFieldName);
		
		if(clazz.equals(String.class))
		{
			gf.setType("xs:string");
		}
		else if (clazz.equals(Integer.class))
		{
			gf.setType("xs:integer");
		}
		
		gf.setValue((String) this.getValue());	
		return gf;
	}
	
	public Class<? extends TridasEntity> getParentClass()
	{
		return parentClass;
	}
	
	public String getXMLFieldName()
	{
		return xmlFieldName;
	}
		
	public static TridasEntity addOrReplaceGenericField(TridasEntity entity, TridasGenericField gf)
	{
		
		if(entity==null ) return null;
		
		if(entity.isSetGenericFields())
		{
			ArrayList<TridasGenericField> gfs = (ArrayList<TridasGenericField>) entity.getGenericFields();
			
			// Remove if present
			for (Iterator<TridasGenericField> iterator = gfs.iterator() ; iterator.hasNext();)
			{
				TridasGenericField ffl = iterator.next();
				if(ffl.getName().equals(gf.getName()))
				{
					iterator.remove();
				}
			}
		}
			
		entity.getGenericFields().add(gf);
		
		return entity;
	}
	
	
	
	
	
	public static TellervoGenericFieldProperty getSampleExternalIDProperty()
	{
		TellervoGenericFieldProperty tgf = new TellervoGenericFieldProperty("sample.externalId", "externalId", "tellervo.externalID", 
				String.class, TridasSample.class, false, false);
		return tgf;
	}
	
	
	public static TellervoGenericFieldProperty getObjectCodeProperty()
	{
		TellervoGenericFieldProperty tgf = new TellervoGenericFieldProperty("object.code", "objectCode", "tellervo.objectLabCode", 
				String.class, TridasObject.class, true, false);
		return tgf;
	}
	
	
	public static TellervoGenericFieldProperty getVegetationTypeProperty()
	{
		TellervoGenericFieldProperty tgf = new TellervoGenericFieldProperty("object.vegetationType", "vegetationType", "tellervo.vegetationType", 
				String.class, TridasObject.class, false, false);
		return tgf;
	}
	

}
