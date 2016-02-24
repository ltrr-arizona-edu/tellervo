package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

public class ODKFields {

	private ArrayList<Class<? extends ODKFieldInterface>> fieldsList = new ArrayList<Class<? extends ODKFieldInterface>>();
	
	public ODKFields()
	{
		
		// OBJECT FIELDS
		fieldsList.add(ODKTridasParentObjectCode.class);
		fieldsList.add(ODKTridasObjectCode.class);
		fieldsList.add(ODKTridasObjectTitle.class);
		fieldsList.add(ODKTridasObjectType.class);
		fieldsList.add(ODKTridasObjectComments.class);
		fieldsList.add(ODKTridasObjectDescription.class);
		fieldsList.add(ODKTridasObjectPhoto.class);
		fieldsList.add(ODKTridasObjectSound.class);
		fieldsList.add(ODKTridasObjectVideo.class);
		fieldsList.add(ODKTridasObjectLocation.class);
		fieldsList.add(ODKTridasObjectLocationType.class);
		fieldsList.add(ODKTridasObjectLocationComments.class);
		fieldsList.add(ODKTridasObjectAddressLine1.class);
		fieldsList.add(ODKTridasObjectAddressLine2.class);
		fieldsList.add(ODKTridasObjectAddressCityOrTown.class);
		fieldsList.add(ODKTridasObjectAddressStateProvince.class);
		fieldsList.add(ODKTridasObjectAddressPostalCode.class);
		fieldsList.add(ODKTridasObjectAddressCountry.class);
		fieldsList.add(ODKTridasObjectVegetationType.class);
		fieldsList.add(ODKTridasObjectOwner.class);
		fieldsList.add(ODKTridasObjectCreator.class);

		
		// ELEMENT FIELDS
		fieldsList.add(ODKTridasElementObjectCode.class);
		fieldsList.add(ODKTridasElementCode.class);
		fieldsList.add(ODKTridasElementComments.class);
		fieldsList.add(ODKTridasElementType.class);
		fieldsList.add(ODKTridasElementDescription.class);
		fieldsList.add(ODKTridasElementPhoto.class);
		fieldsList.add(ODKTridasElementSound.class);
		fieldsList.add(ODKTridasElementVideo.class);
		fieldsList.add(ODKTridasElementTaxon.class);
		fieldsList.add(ODKTridasElementShape.class);
		fieldsList.add(ODKTridasElementAuthenticity.class);
		fieldsList.add(ODKTridasElementLocation.class);
		fieldsList.add(ODKTridasElementLocationType.class);
		fieldsList.add(ODKTridasElementLocationComments.class);
		fieldsList.add(ODKTridasElementAddressLine1.class);
		fieldsList.add(ODKTridasElementAddressLine2.class);
		fieldsList.add(ODKTridasElementAddressCityOrTown.class);
		fieldsList.add(ODKTridasElementAddressStateProvince.class);
		fieldsList.add(ODKTridasElementAddressPostalCode.class);
		fieldsList.add(ODKTridasElementAddressCountry.class);
		fieldsList.add(ODKTridasElementProcessing.class);
		fieldsList.add(ODKTridasElementMarks.class);
		fieldsList.add(ODKTridasElementSlopeAngle.class);
		fieldsList.add(ODKTridasElementSlopeAzimuth.class);
		fieldsList.add(ODKTridasElementSoilDepth.class);
		fieldsList.add(ODKTridasElementSoil.class);
		fieldsList.add(ODKTridasElementBedrock.class);
		fieldsList.add(ODKTridasElementAltitude.class);
		// dimensions

		
		// SAMPLE FIELDS
		fieldsList.add(ODKTridasSampleCode.class);
		fieldsList.add(ODKTridasSampleComments.class);
		fieldsList.add(ODKTridasSampleType.class);
		fieldsList.add(ODKTridasSampleDescription.class);
		fieldsList.add(ODKTridasSamplePhoto.class);
		fieldsList.add(ODKTridasSampleSamplingDate.class);
		fieldsList.add(ODKTridasSamplePosition.class);
		fieldsList.add(ODKTridasSampleState.class);
		fieldsList.add(ODKTridasSampleKnots.class);
		fieldsList.add(ODKTridasSampleExternalID.class);
		// sampling date

		
		// RADIUS FIELDS
		//fieldsList.add(ODKTridasRadiusAzimuth.class);

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
		
		if(!clazz.equals(TridasObject.class) && !clazz.equals(TridasElement.class) && !clazz.equals(TridasSample.class) && !clazz.equals(TridasRadius.class))
		{
			throw new IllegalArgumentException("ODKFields only valid for Tridas objects, elements, samples and radii");
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
