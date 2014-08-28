package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

public class ODKFields {

	private ArrayList<Class<? extends AbstractODKField>> fieldsList = new ArrayList<Class<? extends AbstractODKField>>();
	
	public ODKFields()
	{
		fieldsList.add(ODKTridasObjectCode.class);
		fieldsList.add(ODKTridasObjectTitle.class);
		
		fieldsList.add(ODKTridasElementCode.class);
		fieldsList.add(ODKTridasElementTitle.class);
	}
	
	/**
	 * Get an array of all the available ODK fields
	 * 
	 * @return
	 */
	public ArrayList<Class<? extends AbstractODKField>> getFields()
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
	public static ArrayList<AbstractODKField> getFields(Class<? extends ITridas> clazz)
	{
		ODKFields c = new ODKFields();
		
		if(!clazz.equals(TridasObject.class) && !clazz.equals(TridasElement.class) && !clazz.equals(TridasSample.class))
		{
			throw new IllegalArgumentException("ODKFields only valid for Tridas objects, elements and samples");
		}
		
		ArrayList<AbstractODKField> f = new ArrayList<AbstractODKField>();
		
		for(Class<? extends AbstractODKField> fieldClass : c.fieldsList)
		{
			try {
				AbstractODKField instance = fieldClass.newInstance();
				
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
	
	
	public static AbstractODKField[] getFieldsAsArray(Class<? extends ITridas> clazz)
	{	
		ArrayList<AbstractODKField> fields = ODKFields.getFields(clazz);
		return fields.toArray(new AbstractODKField[fields.size()]);
	}

}
