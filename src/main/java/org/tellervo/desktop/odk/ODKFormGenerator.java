package org.tellervo.desktop.odk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.odk.fields.AbstractODKChoiceField;
import org.tellervo.desktop.odk.fields.AbstractODKDecimalField;
import org.tellervo.desktop.odk.fields.AbstractODKIntegerField;
import org.tellervo.desktop.odk.fields.ODKDataType;
import org.tellervo.desktop.odk.fields.ODKFieldInterface;
import org.tridas.io.util.StringUtils;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

public class ODKFormGenerator {

	private static final Logger log = LoggerFactory.getLogger(ODKFormGenerator.class);

	public static String generate(String formNameFull, ArrayList<ODKFieldInterface> mainFields, ArrayList<ODKFieldInterface> secondaryFields)
	{
		StringBuilder data = new StringBuilder();
		
		Integer objectFieldCount = 0;
		Integer elementFieldCount = 0;
		Integer sampleFieldCount = 0;
		Integer radiusFieldCount = 0;
		String groupCode = "group_sample";
		String groupName = "Samples";
		
		// Sanity checks
		if(mainFields==null ) {
			log.error("Need fields to generate an ODK form!");
			return null;
		}
		if(secondaryFields==null ) secondaryFields = new ArrayList<ODKFieldInterface>();

		if(formNameFull==null) 
		{
			log.error("Need form name to generate an ODK form!");
			return null;
		}
		String formName = StringUtils.escapeForXML(formNameFull.replace(" ", "_"));

		for(ODKFieldInterface field : mainFields)
		{
			if(field.getTridasClass().equals(TridasObject.class)) objectFieldCount++;
			if(field.getTridasClass().equals(TridasElement.class)) elementFieldCount++;
			if(field.getTridasClass().equals(TridasSample.class)) sampleFieldCount++;
			if(field.getTridasClass().equals(TridasRadius.class)) radiusFieldCount++;
		}
		for(ODKFieldInterface field : secondaryFields)
		{
			if(field.getTridasClass().equals(TridasObject.class)) objectFieldCount++;
			if(field.getTridasClass().equals(TridasElement.class)) elementFieldCount++;
			if(field.getTridasClass().equals(TridasSample.class)) sampleFieldCount++;
			if(field.getTridasClass().equals(TridasRadius.class)) radiusFieldCount++;
		}
		if(objectFieldCount>0 && (elementFieldCount>0 || sampleFieldCount>0 || radiusFieldCount>0))
		{
			log.error("Can't mix object fields with fields from elements, samples and/or radii");
			return null;
		}
				
		// Basic form header
		data.append("<h:html xmlns=\"http://www.w3.org/2002/xforms\" xmlns:h=\"http://www.w3.org/1999/xhtml\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:jr=\"http://openrosa.org/javarosa\">");
		data.append("<h:head>");
		data.append("<h:title>"+formNameFull+"</h:title>");
		data.append("<model>");
		            
		
		// Define the fields and default values
		data.append("<instance>");
		data.append("<data id=\""+formName+"-"+UUID.randomUUID()+"\">");
		data.append("<meta>");
		//data.append("<instanceID/>");
		data.append("<instanceName/>");
		data.append("</meta>");
		for(ODKFieldInterface field : mainFields)
		{
			data.append(getFieldDefinitionCode(field));
		}
		if(secondaryFields.size()>0)
		{
			data.append("<"+groupCode+">");
			for(ODKFieldInterface field : secondaryFields)
			{
				data.append(getFieldDefinitionCode(field));
			}
			data.append("</"+groupCode+">");

		}
		
		data.append("</data>");
		data.append("</instance>");

        
		// Translation information ready for when we go global!
		data.append("<itext>");
		data.append("<translation lang=\"eng\">");
		for(ODKFieldInterface field : mainFields)
		{
			data.append(getTranslationCode(field, null));		
		}   
		if(secondaryFields.size()>0)
		{
			for(ODKFieldInterface field : secondaryFields)
			{
				data.append(getTranslationCode(field, groupCode));		
			} 		
			data.append("<text id=\"/data/"+groupCode+":label\">");
	        data.append("<value>"+groupName+"</value>");
	        data.append("</text>");
		}
		data.append("</translation>");
		data.append("</itext>");
		
		// Data binding code
		String instancename = "concat(/data/tridas_object_code"; 
		if(elementFieldCount>0) instancename += ", '-', /data/tridas_element_title";
		//if(sampleFieldCount>0) instancename += ", '-', /data/tridas_sample_title";
		data.append("<bind nodeset=\"/data/meta/instanceName\" type=\"string\" readonly=\"true()\" calculate=\""+instancename+")\"/>");

		for(ODKFieldInterface field : mainFields)
		{
			data.append(getDataBindingCode(field, null));
		}
		for(ODKFieldInterface field : secondaryFields)
		{
			data.append(getDataBindingCode(field, groupCode));
		}
		data.append("</model>");
		data.append("</h:head>");
		
		data.append("<h:body>");

		// The actual fields to display to the user
		for(ODKFieldInterface field : mainFields)
		{
			data.append(getFieldGUICode(field, null));
		}
		if(secondaryFields.size()>0)
		{
			data.append("<group>");
		    data.append("<label ref=\"jr:itext('/data/"+groupCode+":label')\"/>");
		    data.append("<repeat nodeset=\"/data/"+groupCode+"\">");
			for(ODKFieldInterface field : secondaryFields)
			{
				data.append(getFieldGUICode(field, groupCode));
			}
			data.append("</repeat>");
			data.append("</group>");
		}

		data.append("</h:body>");
		data.append("</h:html>");
		
		return data.toString();
	}
	
	
	public static void generate(File outfile, String formNameFull, ArrayList<ODKFieldInterface> mainFields, ArrayList<ODKFieldInterface> secondaryFields)
	{
		if(outfile==null) 
		{
			log.error("Need output file to generate an ODK form!");
			return;
		}
		
		String data = generate(formNameFull, mainFields, secondaryFields);
		
		try {
			FileUtils.writeStringToFile(outfile, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getDataBindingCode(ODKFieldInterface field, String grpCd)
	{
		StringBuilder data = new StringBuilder();
	
		if(grpCd == null) 
		{
			grpCd="";
		}
		else
		{
			grpCd = grpCd+"/";
		}
		
		if(field.getFieldType().equals(ODKDataType.STRING))
		{
			data.append("<bind nodeset=\"/data/"+grpCd+field.getFieldCode()+"\" type=\"string\" required=\""+boolAsText(field.isFieldRequired())+"()\"/>");
		}
		else if(field.getFieldType().equals(ODKDataType.INTEGER))
		{
			String limitsCode = "";			
			AbstractODKIntegerField intfield = (AbstractODKIntegerField) field;
			if(intfield.getMinValue()>Integer.MIN_VALUE && intfield.getMaxValue()<Integer.MAX_VALUE)
			{
				limitsCode = " constraint=\"(. &gt; '"+intfield.getMinValue()+"' and . &lt; '"+intfield.getMaxValue()+"')\" jr:constraintMsg=\"Value must be between "+intfield.getMinValue()+" and "+intfield.getMaxValue()+"\"";
			}
			data.append("<bind nodeset=\"/data/"+grpCd+field.getFieldCode()+"\" type=\"int\" "+limitsCode+" required=\""+boolAsText(field.isFieldRequired())+"()\"/>");
		}
		else if(field.getFieldType().equals(ODKDataType.DECIMAL))
		{
			String limitsCode = "";			
			AbstractODKDecimalField decfield = (AbstractODKDecimalField) field;
			if(decfield.getMinValue()>Double.MIN_VALUE && decfield.getMaxValue()<Double.MAX_VALUE)
			{
				limitsCode = " constraint=\"(. &gt; '"+decfield.getMinValue()+"' and . &lt; '"+decfield.getMaxValue()+"')\" jr:constraintMsg=\"Value must be between "+decfield.getMinValue()+" and "+decfield.getMaxValue()+"\"";
			}
			data.append("<bind nodeset=\"/data/"+grpCd+field.getFieldCode()+"\" type=\"decimal\" "+limitsCode+" required=\""+boolAsText(field.isFieldRequired())+"()\"/>");
		}
		else if(field.getFieldType().equals(ODKDataType.SELECT_ONE))
		{
			if(field.isFieldHidden())
			{
				// Force data type to String if the field is hidden otherwise form won't validate
				data.append("<bind nodeset=\"/data/"+grpCd+field.getFieldCode()+"\" type=\"string\" required=\""+boolAsText(field.isFieldRequired())+"()\"/>");
			}
			else
			{
				data.append("<bind nodeset=\"/data/"+grpCd+field.getFieldCode()+"\" type=\"select1\" required=\""+boolAsText(field.isFieldRequired())+"()\"/>");
			}
		}
		else if(field.getFieldType().equals(ODKDataType.AUDIO) ||
				field.getFieldType().equals(ODKDataType.IMAGE) || 
				field.getFieldType().equals(ODKDataType.VIDEO))
		{
			data.append("<bind nodeset=\"/data/"+grpCd+field.getFieldCode()+"\" type=\"binary\" required=\""+boolAsText(field.isFieldRequired())+"()\"/>");

		}
		else if(field.getFieldType().equals(ODKDataType.LOCATION))
		{
			data.append("<bind nodeset=\"/data/"+grpCd+field.getFieldCode()+"\" type=\"geopoint\" required=\""+boolAsText(field.isFieldRequired())+"()\"/>");
		}
		return data.toString();
	}
	
	private static String getFieldGUICode(ODKFieldInterface field, String grpCd)
	{
		// If field is hidden then no field GUI code is required
		if(field.isFieldHidden()) return "";
		
		
		StringBuilder data = new StringBuilder();
	
		if(grpCd == null) 
		{
			grpCd="";
		}
		else
		{
			grpCd = grpCd+"/";
		}
		
		
		// Standard text input, numbers or location
		if(field.getFieldType().equals(ODKDataType.STRING) || 
		   field.getFieldType().equals(ODKDataType.LOCATION) || 
		   field.getFieldType().equals(ODKDataType.INTEGER) || 
		   field.getFieldType().equals(ODKDataType.DECIMAL)) 
		{			
			data.append("<input ref=\"/data/"+grpCd+field.getFieldCode()+"\">");
			data.append("<label ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":label')\"/>");
			data.append("<hint ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":hint')\"/>");
			data.append("</input>");
		}
		//Select one option
		else if (field.getFieldType().equals(ODKDataType.SELECT_ONE))
		{
			data.append("<select1 ref=\"/data/"+grpCd+field.getFieldCode()+"\">");
			data.append("<label ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":label')\"/>");
			data.append("<hint ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":hint')\"/>");
			
			AbstractODKChoiceField choicefield = (AbstractODKChoiceField) field;

			int i =0;
			for(SelectableChoice choice: choicefield.getSelectedChoices())
			{
				String value = choice.toString();
				if(choice.getItem() instanceof ControlledVoc)
				{
					value = ((ControlledVoc)choice.getItem()).getNormalId();
				}
				else if (choice.getItem() instanceof NormalTridasShape)
				{
					value = ((NormalTridasShape)choice.getItem()).toString();
				}
				else if (choice.getItem() instanceof NormalTridasLocationType)
				{
					value = ((NormalTridasLocationType)choice.getItem()).toString();
				}
				
				data.append("<item>");
				data.append("<label ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":option"+i+"')\"/>");
				data.append("<value>"+value+"</value>");
				data.append("</item>");
				i++;
			}
			data.append("</select1>");
		}
		else if(field.getFieldType().equals(ODKDataType.AUDIO))
		{
			data.append("<upload ref=\"/data/"+grpCd+field.getFieldCode()+"\" mediatype=\"audio/*\">");
			data.append("<label ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":label')\"/>");
			data.append("<hint ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":hint')\"/>");
		    data.append("</upload>");
		}
		else if(field.getFieldType().equals(ODKDataType.VIDEO))
		{
			data.append("<upload ref=\"/data/"+grpCd+field.getFieldCode()+"\" mediatype=\"video/*\">");
			data.append("<label ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":label')\"/>");
			data.append("<hint ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":hint')\"/>");
		    data.append("</upload>");
		}
		else if(field.getFieldType().equals(ODKDataType.IMAGE))
		{
			data.append("<upload ref=\"/data/"+grpCd+field.getFieldCode()+"\" mediatype=\"image/*\">");
			data.append("<label ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":label')\"/>");
			data.append("<hint ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":hint')\"/>");
		    data.append("</upload>");
		}
		else
		{
			log.error("The "+field.getFieldType()+" data type is not supported yet");
		}
		
		return data.toString();

	}
	
	private static String getFieldDefinitionCode(ODKFieldInterface field)
	{
		StringBuilder data = new StringBuilder();

		data.append("<"+field.getFieldCode()+">");
		
		// Include default value when appropriate
		if(field.getDefaultValue()!=null )
		{
			data.append(StringUtils.escapeForXML(field.getDefaultValue().toString()));
		}
				
		data.append("</"+field.getFieldCode()+">");
		
		return data.toString();
	}
	
	private static String getTranslationCode(ODKFieldInterface field, String grpCd)
	{
		StringBuilder data = new StringBuilder();
		
		
		if(grpCd == null) 
		{
			grpCd="";
		}
		else
		{
			grpCd = grpCd+"/";
		}
		
		// The field 
		data.append("<text id=\"/data/"+grpCd+field.getFieldCode()+":label\">");
		data.append("<value>"+StringUtils.escapeForXML(field.getFieldName())+"</value>");
		data.append("</text>");
		
		// The hint text
		data.append("<text id=\"/data/"+grpCd+field.getFieldCode()+":hint\">");
		
		// The description
		if(field.getFieldDescription()==null)
		{
			data.append("<value></value>");
		}
		else
		{
			data.append("<value>"+StringUtils.escapeForXML(field.getFieldDescription())+"</value>");
		}
		data.append("</text>");
		
			
		if (field.getFieldType().equals(ODKDataType.SELECT_ONE))
		{
			AbstractODKChoiceField choicefield = (AbstractODKChoiceField) field;

			int i =0;
			for(SelectableChoice choice: choicefield.getSelectedChoices())
			{			 
				data.append("<text id=\"/data/"+grpCd+field.getFieldCode()+":option"+i+"\">");
				data.append("<value>"+StringUtils.escapeForXML(choice.toString())+"</value>");
				data.append("</text>");
				i++;
			}
		}
		
		return data.toString();
	}
	
	private static String boolAsText(boolean b)
	{
		if(b)
		{
			return "true";
		}
		else
		{
			return "false";
		}
	}
	
}
