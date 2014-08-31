package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

public class ODKFields {

	private ArrayList<Class<? extends ODKFieldInterface>> fieldsList = new ArrayList<Class<? extends ODKFieldInterface>>();
	
	public ODKFields()
	{
		fieldsList.add(ODKTridasObjectCode.class);
		fieldsList.add(ODKTridasObjectTitle.class);
		fieldsList.add(ODKTridasObjectType.class);
		fieldsList.add(ODKTridasObjectComments.class);
		fieldsList.add(ODKTridasObjectDescription.class);
		fieldsList.add(ODKTridasObjectPhoto.class);
		fieldsList.add(ODKTridasObjectSound.class);
		fieldsList.add(ODKTridasObjectVideo.class);

		
		fieldsList.add(ODKTridasElementCode.class);
		fieldsList.add(ODKTridasElementTitle.class);
	}
	
	/**
	 * Get an array of all the available ODK fields
	 * 
	 * @return
	 */
	public ArrayList<Class<? extends ODKFieldInterface>> getFields()
	{
		return fieldsList;
	}
	
	/**
	 * Get an array of the available ODK field classes based on the parent TRiDaS entity type.  
	 * Supported classes are TridasObject, TridasElement and TridasSample.
	 * 
	 * @param clazz
	 * @return
	 */
	public static ArrayList<ODKFieldInterface> getFields(Class<? extends ITridas> clazz)
	{
		ODKFields c = new ODKFields();
		
		if(!clazz.equals(TridasObject.class) && !clazz.equals(TridasElement.class) && !clazz.equals(TridasSample.class))
		{
			throw new IllegalArgumentException("ODKFields only valid for Tridas objects, elements and samples");
		}
		
		ArrayList<ODKFieldInterface> f = new ArrayList<ODKFieldInterface>();
		
		for(Class<? extends ODKFieldInterface> fieldClass : c.fieldsList)
		{
			try {
				ODKFieldInterface instance = fieldClass.newInstance();
				
				if(instance.getTridasClass().equals(clazz))
				{
					f.add(instance);
				}
				
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return f;
	}
	
	
	public static ODKFieldInterface[] getFieldsAsArray(Class<? extends ITridas> clazz)
	{	
		ArrayList<ODKFieldInterface> fields = ODKFields.getFields(clazz);
		return fields.toArray(new ODKFieldInterface[fields.size()]);
	}

}
