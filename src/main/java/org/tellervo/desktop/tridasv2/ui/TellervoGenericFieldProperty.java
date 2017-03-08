package org.tellervo.desktop.tridasv2.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import org.tellervo.desktop.tridasv2.ui.support.TridasEntityProperty;
import org.tellervo.schema.SampleStatus;
import org.tellervo.schema.WSICuration;
import org.tridas.io.util.TridasUtils;
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
			gf.setValue((String) this.getValue());

		}
		else if (clazz.equals(Integer.class))
		{
			gf.setType("xs:int");
			if(this.getValue()!=null)
			{
				int v = (int) this.getValue();
				gf.setValue(Integer.valueOf(v).toString());
			} else
			{
				gf.setValue(null);
			}

		}
		else if (clazz.equals(Float.class))
		{
			gf.setType("xs:float");
			if(this.getValue()!=null)
			{
				float v = (float) this.getValue();
				gf.setValue(Double.valueOf(v).toString());
			} else
			{
				gf.setValue(null);
			}
		}
		else if (clazz.equals(Boolean.class))
		{
			gf.setType("xs:boolean");
			if(this.getValue()!=null)
			{
				boolean v = (boolean) this.getValue();
				gf.setValue(Boolean.valueOf(v).toString());
			} else
			{
				gf.setValue(null);
			}
		}
		else
		{
			gf.setType("xs:string");
		}

		if(this.getValue() instanceof SampleStatus)
		{
			SampleStatus ss = (SampleStatus) this.getValue();
			gf.setValue(ss.value());
		}

		
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
	
	public static TellervoGenericFieldProperty getSampleCurationStatusProperty()
	{
		TellervoGenericFieldProperty tgf = new TellervoGenericFieldProperty("sample.curationStatus", "curationStatus", "tellervo.curationStatus", 
				WSICuration.class, TridasSample.class, false, false);
		return tgf;
	}
	
	public static TellervoGenericFieldProperty getSampleStatusProperty()
	{
		TellervoGenericFieldProperty tgf = new TellervoGenericFieldProperty("sample.sampleStatus", "sampleStatus", "tellervo.sampleStatus", 
				SampleStatus.class, TridasSample.class, false, false);
		return tgf;
	}
	
	public static TellervoGenericFieldProperty getObjectCodeProperty()
	{
		TellervoGenericFieldProperty tgf = new TellervoGenericFieldProperty("object.code", "objectCode", TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE, 
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
