package org.tellervo.desktop.odk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.tellervo.desktop.odk.fields.AbstractODKChoiceField;
import org.tellervo.desktop.odk.fields.ODKDataType;
import org.tellervo.desktop.odk.fields.ODKFieldInterface;
import org.tridas.io.util.StringUtils;

public class ODKFormGenerator {

	public static void generate(File outfile, String formNameFull, ArrayList<ODKFieldInterface> fields)
	{
		StringBuilder data = new StringBuilder();
		String formName = StringUtils.escapeForXML(formNameFull.replace(" ", "_"));
		
		// Basic form header
		data.append("<h:html xmlns=\"http://www.w3.org/2002/xforms\" xmlns:h=\"http://www.w3.org/1999/xhtml\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:jr=\"http://openrosa.org/javarosa\">");
		data.append("<h:head>");
		data.append("<h:title>"+formNameFull+"</h:title>");
		data.append("<model>");
		            
		
		// Define the fields and default values
		data.append("<instance>");
		data.append("<data id=\""+formName+"\">");
		data.append("<meta>");
		data.append("<instanceID/>");
		data.append("</meta>");
		for(ODKFieldInterface field : fields)
		{
			data.append("<"+field.getFieldCode()+">");
			
			// Include default value when appropriate
			if(field.getDefaultValue()!=null )
			{
				data.append(StringUtils.escapeForXML(field.getDefaultValue().toString()));
			}
					
			data.append("</"+field.getFieldCode()+">");
		}
		data.append("</data>");
		data.append("</instance>");

        
		// Translation information ready for when we go global!
		data.append("<itext>");
		data.append("<translation lang=\"eng\">");
		for(ODKFieldInterface field : fields)
		{
					
			// The field 
			data.append("<text id=\"/data/"+field.getFieldCode()+":label\">");
			data.append("<value>"+StringUtils.escapeForXML(field.getFieldName())+"</value>");
			data.append("</text>");
			
			// The hint text
			data.append("<text id=\"/data/"+field.getFieldCode()+":hint\">");
			
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
					data.append("<text id=\"/data/"+field.getFieldCode()+":option"+i+"\">");
					data.append("<value>"+choice.toString()+"</value>");
					data.append("</text>");
					i++;
				}
			}
		}            
		data.append("</translation>");
		data.append("</itext>");
		

		// The actual fields to display to the user
		data.append("<bind nodeset=\"/data/meta/instanceID\" type=\"string\" readonly=\"true()\" calculate=\"concat(/"+formName+", '-', /tridas_object_code)\"/>");
		for(ODKFieldInterface field : fields)
		{
			if(field.getFieldType().equals(ODKDataType.STRING))
			{
				data.append("<bind nodeset=\"/data/"+field.getFieldCode()+"\" " +
						"type=\"string\" " +
						"required=\""+boolAsText(field.isFieldRequired())+"()\"" +
						"/>");
			}
			
			
		}
		
		
		data.append("</model>");
		data.append("</h:head>");
		data.append("<h:body>");
		
		// Code for the fields to display to the user
		for(ODKFieldInterface field : fields)
		{
			// Standard text input
			if(field.getFieldType().equals(ODKDataType.STRING))
			{			
				data.append("<input ref=\"/data/"+field.getFieldCode()+"\">");
				data.append("<label ref=\"jr:itext('/data/"+field.getFieldCode()+":label')\"/>");
				data.append("<hint ref=\"jr:itext('/data/"+field.getFieldCode()+":hint')\"/>");
				data.append("</input>");
			}
			//Select one option
			else if (field.getFieldType().equals(ODKDataType.SELECT_ONE))
			{
				data.append("<select1 ref=\"/data/"+field.getFieldCode()+"\">");
				data.append("<label ref=\"jr:itext('/data/"+field.getFieldCode()+":label')\"/>");
				data.append("<hint ref=\"jr:itext('/data/"+field.getFieldCode()+":hint')\"/>");
				
				AbstractODKChoiceField choicefield = (AbstractODKChoiceField) field;

				int i =0;
				for(SelectableChoice choice: choicefield.getSelectedChoices())
				{
					data.append("<item>");
					data.append("<label ref=\"jr:itext('/data/"+field.getFieldCode()+":option"+i+"')\"/>");
					data.append("<value>"+choice.toString()+"</value>");
					data.append("</item>");
					i++;
				}
				data.append("</select1>");
			}
	        
		}
	    
		data.append("</h:body>");
		data.append("</h:html>");
		
		try {
			FileUtils.writeStringToFile(outfile, data.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void generate2(File outfile, String formName, ArrayList<ODKFieldInterface> fields)
	{
		StringBuilder data = new StringBuilder();
		
		data.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		data.append("<h:html xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:h=\"http://www.w3.org/2002/xhtml\" xmlns:jr=\"http://openrosa.org/javarosa\" xmlns:orx=\"http://openrosa.org/xforms/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
		data.append("<h:head>");
		data.append("<h:title ref=\"jr:itext('"+formName+"')\">"+formName+"</h:title>");
		data.append("<model>");
		data.append("<instance>");
		data.append("<"+formName+" id=\""+formName+"\">");
		data.append("<deviceid/>");
		data.append("<start/>");
		data.append("<end/>");
		data.append("<meta>");
		data.append("<instanceName/>");
		data.append("</meta>");
		
		for(ODKFieldInterface field : fields)
		{
			data.append("<"+field.getFieldCode()+"/>");
		}
		data.append("</"+formName+">");
		data.append("</instance>");
		
		data.append("<bind id=\"deviceid\" nodeset=\"/"+formName+"/deviceid\" type=\"xsd:string\" jr:preload=\"property\" jr:preloadParams=\"deviceid\" visible=\"false()\"/>");
		data.append("<bind id=\"start\" nodeset=\"/"+formName+"/start\" type=\"xsd:time\" jr:preload=\"timestamp\" jr:preloadParams=\"start\" visible=\"false()\"/>");
		data.append("<bind id=\"end\" nodeset=\"/"+formName+"/end\" type=\"xsd:time\" jr:preload=\"timestamp\" jr:preloadParams=\"end\" visible=\"false()\"/>");
		data.append("<bind calculate=\"concat(/"+formName+", '-', /tridas_object_code)\" nodeset=\"/"+formName+"/meta/instanceName\" type=\"string\"/>");
		
		for(ODKFieldInterface field : fields)
		{
			data.append("<bind id=\""+field.getFieldCode()+"\" nodeset=\"/"+formName+"/"+field.getFieldCode()+"\" type=\"xsd:string\" required=\"true()\"/>");
		}
		
		data.append("<itext>");
		data.append("<translation lang=\"English\">");
		data.append("<text id=\""+formName+"\">");
		data.append("<value>"+formName+"</value>");
		data.append("</text>");
		
		for(ODKFieldInterface field : fields)
		{
			data.append("<text id=\""+field.getFieldCode()+"\">");
			data.append("<value>"+field.getFieldName()+"</value>");
			data.append("</text>");
			data.append("<text id=\""+field.getFieldCode()+"-hint\">");
			data.append("<value>"+field.getFieldDescription()+"</value>");
			data.append("</text>");
		}

		   
		data.append("</translation>");
		data.append("</itext>");
		data.append("</model>");
		data.append("</h:head>");
		
		data.append("<h:body>");
		
		for(ODKFieldInterface field : fields)
		{
			data.append("<input bind=\""+field.getFieldCode()+"\">");
			data.append("<label ref=\"jr:itext('"+field.getFieldCode()+"')\"/>");
			data.append("<hint ref=\"jr:itext('"+field.getFieldCode()+"-hint')\"/>");
			data.append("</input>");
		}
		
		
		data.append("</h:body>");
		data.append("</h:html>");
		
		try {
			FileUtils.writeStringToFile(outfile, data.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
