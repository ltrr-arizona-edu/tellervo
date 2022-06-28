package org.tellervo.desktop.odk;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.odk.fields.AbstractODKChoiceField;
import org.tellervo.desktop.odk.fields.AbstractODKDecimalField;
import org.tellervo.desktop.odk.fields.AbstractODKIntegerField;
import org.tellervo.desktop.odk.fields.ODKDataType;
import org.tellervo.desktop.odk.fields.ODKFieldInterface;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasSample;

public class ODKFormGenerator {

	private static final Logger log = LoggerFactory.getLogger(ODKFormGenerator.class);

	
	public static String generate(String formNameFull, ODKTreeModel treeModel)
	{
		StringBuilder data = new StringBuilder();		

		if(formNameFull==null) 
		{
			log.error("Need form name to generate an ODK form!");
			return null;
		}
		String formName = StringEscapeUtils.escapeXml(formNameFull.replace(" ", "_"));
				
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
		
		DefaultMutableTreeNode root = treeModel.getRootAsDMTN();
		
		log.debug("Root of tree has "+root.getChildCount()+ " children");
		
		for(int i=0; i<root.getChildCount(); i++)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
			if(node instanceof ODKFieldNode)
			{
				log.debug("ODKFieldNode child of root");
				ODKFieldInterface field = (ODKFieldInterface) node.getUserObject();
				data.append(getFieldDefinitionCode(field));
			}
			else if (node instanceof ODKBranchNode)
			{
				log.debug("ODKBranchNode child of root");
				String groupName = (String) node.getUserObject();
				String groupCode = "group_"+StringEscapeUtils.escapeXml(groupName.replace(" ", "_").toLowerCase());
				
				data.append("<"+groupCode+">");
				for(int j=0; j<node.getChildCount(); j++)
				{
					DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) node.getChildAt(j);
					if(node2 instanceof ODKFieldNode)
					{
						ODKFieldInterface field = (ODKFieldInterface) node2.getUserObject();
						data.append(getFieldDefinitionCode(field));
					}
				}
				data.append("</"+groupCode+">");
			}
			else
			{
				log.debug("Unknown child of root");
			}
		}		
		data.append("</data>");
		data.append("</instance>");

        
		// Translation information ready for when we go global!
		data.append("<itext>");
		data.append("<translation lang=\"eng\">");
		for(int i=0; i<root.getChildCount(); i++)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
			if(node instanceof ODKFieldNode)
			{
				data.append("<!-- translation for node -->");
				
				ODKFieldInterface field = (ODKFieldInterface) node.getUserObject();
				data.append(getTranslationCode(field, null));		
			}
			else if (node instanceof ODKBranchNode)
			{
				String groupName = (String) node.getUserObject();
				String groupCode = "group_"+StringEscapeUtils.escapeXml(groupName.replace(" ", "_").toLowerCase());
				
				data.append("<!-- TRANSLATION FOR GROUP "+groupName+" -->");
				data.append("<text id=\"/data/"+groupCode+":label\">");
		        data.append("<value>"+groupName+"</value>");
		        data.append("</text>");
		        
				for(int j=0; j<node.getChildCount(); j++)
				{
					DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) node.getChildAt(j);
					if(node2 instanceof ODKFieldNode)
					{
						ODKFieldInterface field = (ODKFieldInterface) node2.getUserObject();
						data.append(getTranslationCode(field, groupCode));		
					}
					
				}
			}
		}		
		data.append("</translation>");
		data.append("</itext>");
		
		
		// Data binding code	
		String objectGroupCode = getGroupNameForField(treeModel, "tridas_object_code");
		if(objectGroupCode!=null) {
			objectGroupCode=objectGroupCode+"/";
		}
		else
		{
			objectGroupCode = "";
		}
		String instancename = "concat(/data/"+objectGroupCode+"tridas_object_code"; 
		if(treeModel.getClassType().equals(TridasSample.class)) {
			String elementGroupCode = getGroupNameForField(treeModel, "tridas_element_title");
			if(elementGroupCode!=null) {
				elementGroupCode=elementGroupCode+"/";
			}
			else
			{
				elementGroupCode = "";
			}
			instancename += ", '-', /data/"+elementGroupCode+"tridas_element_title";
		}
		data.append("<bind nodeset=\"/data/meta/instanceName\" type=\"string\" readonly=\"true()\" calculate=\""+instancename+")\"/>");

		for(int i=0; i<root.getChildCount(); i++)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
			if(node instanceof ODKFieldNode)
			{
				ODKFieldInterface field = (ODKFieldInterface) node.getUserObject();
				data.append(getDataBindingCode(field, null));		
			}
			else if (node instanceof ODKBranchNode)
			{				
				for(int j=0; j<node.getChildCount(); j++)
				{
					DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) node.getChildAt(j);
					String groupCode = "group_"+StringEscapeUtils.escapeXml(node.getUserObject().toString().replace(" ", "_").toLowerCase());

					if(node2 instanceof ODKFieldNode)
					{
						ODKFieldInterface field = (ODKFieldInterface) node2.getUserObject();
						data.append(getDataBindingCode(field, groupCode));
					}
				}
			}
		}	
		
		data.append("</model>");
		data.append("</h:head>");
		data.append("<h:body>");

		// The actual fields to display to the user
		for(int i=0; i<root.getChildCount(); i++)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
			if(node instanceof ODKFieldNode)
			{
				ODKFieldInterface field = (ODKFieldInterface) node.getUserObject();
				data.append(getFieldGUICode(field, null));
			}
			else if (node instanceof ODKBranchNode)
			{
				String groupName = (String) node.getUserObject();
				String groupCode = "group_"+StringEscapeUtils.escapeXml(groupName.replace(" ", "_").toLowerCase());
				
				
			   
			    if(((ODKBranchNode) node).isRepeatable()) {
			    	data.append("<group>");
				    data.append("<label ref=\"jr:itext('/data/"+groupCode+":label')\"/>");
			    	data.append("<repeat nodeset=\"/data/"+groupCode+"\">");
			    }
			    else
			    {
			    	data.append("<group appearance=\"field-list\">");
				    data.append("<label ref=\"jr:itext('/data/"+groupCode+":label')\"/>");
			    }
				
				for(int j=0; j<node.getChildCount(); j++)
				{
					DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) node.getChildAt(j);
					if(node2 instanceof ODKFieldNode)
					{
						ODKFieldInterface field = (ODKFieldInterface) node2.getUserObject();
						data.append(getFieldGUICode(field, groupCode));
					}
				}
				if(((ODKBranchNode) node).isRepeatable()) data.append("</repeat>");
				data.append("</group>");
			}
		}	

		data.append("</h:body>");
		data.append("</h:html>");
		
		return data.toString();
	}
	
	
	public static void generate(File outfile, String formNameFull, ODKTreeModel treeModel)
	{
		if(outfile==null) 
		{
			log.error("Need output file to generate an ODK form!");
			return;
		}
		
		String data = generate(formNameFull,treeModel);
		
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
		else if(field.getFieldType().equals(ODKDataType.DATE))
		{
			String defaultto = "";
			if(field.getDefaultValue()=="today")
			{
				defaultto = "calculate=\"today()\"";
			}
			
			data.append("<bind nodeset=\"/data/"+grpCd+field.getFieldCode()+"\" type=\"date\" "+defaultto+" required=\""+boolAsText(field.isFieldRequired())+"()\"/>");
		}
		else
		{
			log.debug("Unsupported data type");
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
					value = ((ControlledVoc)choice.getItem()).getNormal();
				}
				else if (choice.getItem() instanceof NormalTridasShape)
				{
					value = ((NormalTridasShape)choice.getItem()).toString().toLowerCase().replace("_", " ");
				}
				else if (choice.getItem() instanceof NormalTridasLocationType)
				{
					value = ((NormalTridasLocationType)choice.getItem()).toString().toLowerCase().replace("_", " ");
				}
				else if (choice.getItem() instanceof NormalTridasUnit)
				{
					value = ((NormalTridasUnit)choice.getItem()).toString().toLowerCase().replace("_", " ");
				}
				
				data.append("<item>");
				data.append("<label ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":option"+i+"')\"/>");
				data.append("<value>"+StringEscapeUtils.escapeXml(value)+"</value>");
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
		else if (field.getFieldType().equals(ODKDataType.DATE))
		{
			data.append("<input ref=\"/data/"+grpCd+field.getFieldCode()+"\">");
			data.append("<label ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":label')\"/>");
			data.append("<hint ref=\"jr:itext('/data/"+grpCd+field.getFieldCode()+":hint')\"/>");
			data.append("</input>");			
		}
		else
		{
			log.error("The "+field.getFieldType()+" data type is not supported yet");
		}
		
		return data.toString();

	}
	
	/**
	 * Definition of field to be used in Head>model>instance
	 * 
	 * @param field
	 * @return
	 */
	private static String getFieldDefinitionCode(ODKFieldInterface field)
	{
		StringBuilder data = new StringBuilder();

		data.append("<"+StringEscapeUtils.escapeXml(field.getFieldCode())+">");
		
		// Include default value when appropriate
		if(field.getDefaultValue()!=null && !field.getFieldType().equals(ODKDataType.DATE))
		{
			data.append(StringEscapeUtils.escapeXml(field.getDefaultValue().toString()));
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
		data.append("<text id=\"/data/"+StringEscapeUtils.escapeXml(grpCd+field.getFieldCode())+":label\">");
		data.append("<value>"+StringEscapeUtils.escapeXml(field.getFieldName())+"</value>");
		data.append("</text>");
		
		// The hint text
		data.append("<text id=\"/data/"+StringEscapeUtils.escapeXml(grpCd+field.getFieldCode())+":hint\">");
		
		// The description
		if(field.getFieldDescription()==null)
		{
			data.append("<value></value>");
		}
		else
		{
			data.append("<value>"+StringEscapeUtils.escapeXml(field.getFieldDescription())+"</value>");
		}
		data.append("</text>");
		
		
		log.debug("Building field: "+field.getFieldName());
			
		if (field.getFieldType().equals(ODKDataType.SELECT_ONE))
		{
			AbstractODKChoiceField choicefield = (AbstractODKChoiceField) field;

			int i =0;
			for(SelectableChoice choice: choicefield.getSelectedChoices())
			{			 
				data.append("<text id=\"/data/"+StringEscapeUtils.escapeXml(grpCd+field.getFieldCode())+":option"+i+"\">");
				data.append("<value>"+StringEscapeUtils.escapeXml(choice.toString())+"</value>");
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
	
	/**
	 * Gets the name of a group a field is in.  If the field is not in a group, or not present, then null is returned
	 * 
	 * @param fieldname
	 * @return
	 */
	private static String getGroupNameForField(ODKTreeModel treeModel, String fieldname)
	{
		DefaultMutableTreeNode root = treeModel.getRootAsDMTN();

		
		for(int i=0; i<root.getChildCount(); i++)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
			if(node instanceof ODKFieldNode)
			{
				ODKFieldInterface field = (ODKFieldInterface) node.getUserObject();
				if(field.getFieldCode().equals(fieldname)) return null;
			}
			else if (node instanceof ODKBranchNode)
			{
				String groupName = (String) node.getUserObject();
				String groupCode = "group_"+StringEscapeUtils.escapeXml(groupName.replace(" ", "_").toLowerCase());
				
				for(int j=0; j<node.getChildCount(); j++)
				{
					DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) node.getChildAt(j);
					if(node2 instanceof ODKFieldNode)
					{
						ODKFieldInterface field = (ODKFieldInterface) node2.getUserObject();
						if(field.getFieldCode().equals(fieldname)) return groupCode;
					}
				}
			}
			else
			{
				log.debug("Unknown child of root");
			}
		}	
		
		return null;
	}
	
}
