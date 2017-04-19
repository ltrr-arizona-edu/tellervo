package org.tellervo.desktop.odk.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.odk.ODKFormDesignPanel;
import org.tellervo.schema.UserExtendableDataType;
import org.tellervo.schema.UserExtendableEntity;
import org.tellervo.schema.WSIUserDefinedField;
import org.tellervo.schema.WSIUserDefinedTerm;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.model.MVCArrayList;

public class ODKFields {

	private ArrayList<ODKFieldInterface> fieldsList = new ArrayList<ODKFieldInterface>();
	private static final Logger log = LoggerFactory.getLogger(ODKFields.class);

	public ODKFields()
	{
		
		// OBJECT FIELDS
		fieldsList.add(new ODKTridasParentObjectCode());
		fieldsList.add(new ODKTridasObjectCode());
		fieldsList.add(new ODKTridasObjectTitle());
		fieldsList.add(new ODKTridasObjectType());
		fieldsList.add(new ODKTridasObjectComments());
		fieldsList.add(new ODKTridasObjectDescription());
		fieldsList.add(new ODKTridasObjectPhoto());
		//fieldsList.add(new ODKTridasObjectSound());
		fieldsList.add(new ODKTridasObjectVideo());
		fieldsList.add(new ODKTridasObjectLocation());
		fieldsList.add(new ODKTridasObjectLocationType());
		fieldsList.add(new ODKTridasObjectLocationComments());
		fieldsList.add(new ODKTridasObjectAddressLine1());
		fieldsList.add(new ODKTridasObjectAddressLine2());
		fieldsList.add(new ODKTridasObjectAddressCityOrTown());
		fieldsList.add(new ODKTridasObjectAddressStateProvince());
		fieldsList.add(new ODKTridasObjectAddressPostalCode());
		fieldsList.add(new ODKTridasObjectAddressCountry());
		fieldsList.add(new ODKTridasObjectVegetationType());
		fieldsList.add(new ODKTridasObjectOwner());
		fieldsList.add(new ODKTridasObjectCreator());

		
		// ELEMENT FIELDS
		fieldsList.add(ODKTridasElementObjectCode.class);
		fieldsList.add(ODKTridasElementCode.class);
		fieldsList.add(ODKTridasElementComments.class);
		fieldsList.add(ODKTridasElementType.class);
		fieldsList.add(ODKTridasElementDescription.class);
		fieldsList.add(ODKTridasElementPhoto.class);
		//fieldsList.add(ODKTridasElementSound.class);
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
		//fieldsList.add(ODKTridasElementAltitude.class);
		fieldsList.add(ODKTridasElementDimUnit.class);
		fieldsList.add(ODKTridasElementDimHeight.class);
		fieldsList.add(ODKTridasElementDimWidth.class);
		fieldsList.add(ODKTridasElementDimDepth.class);
		fieldsList.add(ODKTridasElementDimDiameter.class);
		
		// SAMPLE FIELDS
		fieldsList.add(new ODKTridasSampleCode());
		fieldsList.add(new ODKTridasSampleComments());
		fieldsList.add(new ODKTridasSampleType());
		fieldsList.add(new ODKTridasSampleDescription());
		fieldsList.add(new ODKTridasSamplePhoto());
		fieldsList.add(new ODKTridasSampleSamplingDate());
		fieldsList.add(new ODKTridasSamplePosition());
		fieldsList.add(new ODKTridasSampleState());
		fieldsList.add(new ODKTridasSampleKnots());
		fieldsList.add(new ODKTridasSampleExternalID());
		// sampling date

		
		// RADIUS FIELDS
		//fieldsList.add(new ODKTridasRadiusAzimuth());

		
		addUserDefinedFields();
	}
	
	
	private void addUserDefinedFields()
	{
		
		MVCArrayList<WSIUserDefinedField> udfdictionary = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");
		
		for(WSIUserDefinedField fld : udfdictionary)
		{
			Class<? extends ITridas> attachedto = null;
			if(fld.getAttachedto().equals(UserExtendableEntity.OBJECT))
			{
				attachedto = TridasObject.class;
			}
			else if (fld.getAttachedto().equals(UserExtendableEntity.ELEMENT))
			{
				attachedto = TridasElement.class;
			}
			else if (fld.getAttachedto().equals(UserExtendableEntity.SAMPLE))
			{
				attachedto = TridasSample.class;
			}
			else {
				// Unsupported
				log.debug("Skipping field "+fld.getName()+ ". Unsupported attachment type");
				continue;
			}
			
			AbstractODKField field;
			
			if(fld.isSetDictionarykey())
			{
				ArrayList<WSIUserDefinedTerm> dict = new ArrayList<WSIUserDefinedTerm>();
				ArrayList<Object> dict2 = new ArrayList<Object>();
				
				dict = App.dictionary.getMutableDictionary("userDefinedTermDictionary");
				
				for(WSIUserDefinedTerm term : dict)
				{
					if(term.getDictionarykey().equals(fld.getDictionarykey()))
					{
						dict2.add(term.getTerm());
					}
				}
				
				field = new ODKUserDefinedChoiceField(fld.getName(), fld.getLongfieldname(), fld.getDescription(), null, attachedto, dict2);

			}
			else if(fld.getDatatype().equals(UserExtendableDataType.XS___STRING))
			{
				field = new ODKUserDefinedField(ODKDataType.STRING, fld.getName(), fld.getLongfieldname(), fld.getDescription(), null, attachedto);
			}
			else if(fld.getDatatype().equals(UserExtendableDataType.XS___BOOLEAN))
			{
				field = new ODKUserDefinedBooleanField(fld.getName(), fld.getLongfieldname(), fld.getDescription(), null, attachedto);
			}
			else if(fld.getDatatype().equals(UserExtendableDataType.XS___FLOAT))
			{
				field = new ODKUserDefinedDecimalField(fld.getName(), fld.getLongfieldname(), fld.getDescription(), null, attachedto);
			}
			else if(fld.getDatatype().equals(UserExtendableDataType.XS___INT))
			{
				field = new ODKUserDefinedIntegerField(fld.getName(), fld.getLongfieldname(), fld.getDescription(), null, attachedto);
			}
			else 
			{
				// Unsupported
				log.debug("Skipping field "+fld.getName()+ ". Unsupported data type");
				continue;
			}
			
			fieldsList.add(field);
		}
	}
	
	/**
	 * Get an array of all the available ODK fields
	 * 
	 * @return
	 */
	public ArrayList<ODKFieldInterface> getFields()
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
		
		for(ODKFieldInterface instance : c.fieldsList)
		{		
			if(instance.getTridasClass().equals(clazz))
			{
				f.add(instance);
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
