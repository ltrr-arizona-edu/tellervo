/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2.ui.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.tridasv2.ui.TellervoGenericFieldProperty;
import org.tellervo.schema.UserExtendableDataType;
import org.tellervo.schema.UserExtendableEntity;
import org.tellervo.schema.WSIUserDefinedField;
import org.tellervo.schema.WSIUserDefinedTerm;
import org.tridas.annotations.TridasCustomDictionary;
import org.tridas.annotations.TridasEditProperties;
import org.tridas.interfaces.NormalTridasVoc;
import org.tridas.schema.BaseSeries;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.model.MVCArrayList;


public class TridasEntityDeriver {
	private final static Logger log = LoggerFactory.getLogger(TridasEntityDeriver.class);

	private static Set<Class<?>> ignoreClasses = new HashSet<Class<?>>();
	
	static {
		ignoreClasses.add(TridasProject.class);
		ignoreClasses.add(TridasObject.class);
		ignoreClasses.add(TridasElement.class);
		ignoreClasses.add(TridasSample.class);
		ignoreClasses.add(TridasRadius.class);
		ignoreClasses.add(TridasMeasurementSeries.class);
		ignoreClasses.add(TridasDerivedSeries.class);
		ignoreClasses.add(BaseSeries.class);
	}

	
	/**
	 * Builds a PropertyData derivation list
	 * 
	 * @param entityName
	 * @param clazz
	 * @param parent
	 * @param ignoreMachineOnlyFlag
	 * @return the number of direct child properties of clazz
	 */
	private static int buildDerivationList(String entityName, Class<?> clazz,
			TridasEntityProperty parent, String rootName, Boolean ignoreMachineOnlyFlag) {
				
		Map<String, TridasEntityProperty> fieldMap = new HashMap<String, TridasEntityProperty>();
		int nChildren = 0;
		
		// get any property names and stick them at the head of the list
		XmlType type = clazz.getAnnotation(XmlType.class);
		String[] typeProperties;
		
		if (type != null && (typeProperties = type.propOrder()) != null && typeProperties.length > 0) {


			
			// load into type properties
			for (String s : typeProperties) {
				
				
				// ignore zero properties
				if (s.length() == 0)
					continue;

				TridasEntityProperty pd = new TridasEntityProperty(entityName + "." + s, s);
				pd.setCategoryPrefix(rootName);
				
				//log.debug("Adding "+s+" to field map");
				fieldMap.put(s, pd);
			}
			
			if(clazz.equals(TridasObject.class))
			{
				// Hack on a project id to TridasObjects
				log.debug("Adding projectid field to TridasObject");
				
				TridasProjectDictionaryProperty pd3 = new TridasProjectDictionaryProperty("object.project", "project");
				
				pd3.setCategoryPrefix(rootName);
				fieldMap.put("project", pd3);
				parent.addChildProperty(pd3);
				nChildren++;
			}
						
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field f : clazz.getDeclaredFields()) {
				String fieldType = f.getGenericType().toString();
				TridasEntityProperty pd = fieldMap.get(f.getName());
				
				if(pd!=null)
				{
					log.debug("Setting up field "+pd.getDisplayName());
				}
				
				
				XmlElement xmlElement;
				XmlAttribute attribute;
				TridasEditProperties fieldprops = f.getAnnotation(TridasEditProperties.class);

				// is it an xml attribute?
				if ((attribute = f.getAnnotation(XmlAttribute.class)) != null) {
					if (pd != null)
						throw new IllegalStateException("Attribute exists as element?");
					
					pd = new TridasEntityProperty(entityName + ".@" + f.getName(), f.getName());
					pd.setCategoryPrefix(rootName);
					pd.required = attribute.required();
					pd.setType(f.getType(), f);
				} 
				else 
				{
					// ignore this field if we don't have property data for it
					if (pd == null) {
						 log.debug("No property data for " + f.getName() + " as " + fieldType);
						continue;
					}

					// does it say it's required?
					if ((xmlElement = f.getAnnotation(XmlElement.class)) != null
							&& xmlElement.required())
						pd.required = true;

					if (fieldType.startsWith("class ")) 
					{
						// it's a java class, whew
						pd.setType(f.getType(), f);
					} 
					else if (fieldType.startsWith("java.util.List<")) 
					{
						// it's a list of something, yay generics!
						int a = fieldType.indexOf('<');
						int b = fieldType.lastIndexOf('>');
						String listType = fieldType.substring(a + 1, b);

						try {
							pd.setType(Class.forName(listType), f);
							pd.isList = true;
						} catch (ClassNotFoundException e) {
							System.out.println("Can't find class for list: "
									+ listType);
							continue;
						}
					} 
					else 
					{
						System.out.println("Unknown field type: " + fieldType);
						continue;
					}
					
					// if it's a dictionary element, use our copy constructor and get a different class
					TridasCustomDictionary dictionary = f.getAnnotation(TridasCustomDictionary.class);
					if(dictionary != null)
						pd = new TridasDictionaryEntityProperty(pd, dictionary);
				}

				Class<?> entType = pd.getType();
								
				// shouldn't happen...
				if (entType == null)
					throw new NullPointerException();

				// skip things we ignore
				if (ignoreClasses.contains(entType))
					continue;			

				TridasEditProperties classprops = entType.getAnnotation(TridasEditProperties.class);

				
				
				/*
				 * THIS IS A HORRIBLE MULTI-LOCATION KLUDGE FIX
				 * to show lab codes in objects. 
				 */
				/*if(pd.qname.equals("object.genericFields")) 
				{
					// Special case for object lab code
					pd.setReadOnly(false);
					
					// add type to property list
					parent.addChildProperty(pd, 1);
					nChildren++;
										
				}		

				else
				{*/
					// is it machine only? skip it! But only as long as we're not ignoring the machine-only flag
					if((classprops != null && classprops.machineOnly()) || (fieldprops != null && fieldprops.machineOnly()))
						continue;

					// how about read only?
					if((classprops != null && classprops.readOnly()) || (fieldprops != null && fieldprops.readOnly()))
						pd.setReadOnly(true);
					
					
					pd.setReadOnly(false);
					
					// add type to property list
					parent.addChildProperty(pd);
					nChildren++;

				//}


				
				// is it marked as editor final?
				if((classprops != null && classprops.finalType()) || (fieldprops != null && fieldprops.finalType())) 
					continue;


				// don't delve any deeper for enums
				// only delve deeper if it's an XML-annotated class
				if (entType.isEnum() || 
						entType.getAnnotation(XmlType.class) == null ||
						pd.qname.equals("object.genericFields") ||
						pd.qname.equals("sample.genericFields") ||
						pd.qname.equals("project.files")||
						pd.qname.equals("object.files")||
						pd.qname.equals("element.files") ||
						pd.qname.equals("sample.files") || 
						pd.qname.equals("project.types") ||
						pd.qname.equals("project.category")||
						pd.qname.equals("project.laboratories")||
						pd.qname.equals("project.references")||
						pd.qname.equals("project.researches")
						){
					continue;
				}

				// don't infinitely recurse
				if (entType.equals(clazz))
					continue;

				buildDerivationList(pd.qname, entType, pd, rootName, ignoreMachineOnlyFlag);
				
				// do we have a NormalTridas property?
				// construct it now, after we've built its child list
				if(NormalTridasVoc.class.isAssignableFrom(entType)) {
					parent.replaceChildProperty(pd, new TridasNormalProperty(pd));
				}
			}
		}
				
		// Adding in TellervoSpecificGenericFields
		MVCArrayList<WSIUserDefinedField> udfdictionary = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");
		ArrayList<WSIUserDefinedField> myUserDefinedFields = new ArrayList<WSIUserDefinedField>();
		TridasEntityProperty custom = new TridasEntityProperty(entityName + ".customFields", "custom fields");
		custom.setCategoryPrefix(rootName);
		custom.required = false;
		custom.setType(String.class, null);
		custom.readOnly = true;
		
		
		if(clazz.equals(TridasObject.class))
		{
			

			
			// Object lab code
			TellervoGenericFieldProperty pd =  TellervoGenericFieldProperty.getObjectCodeProperty();
			pd.setCategoryPrefix(rootName);
			fieldMap.put(pd.getName(), pd);
			parent.addChildProperty(pd);
			nChildren++;
			

			
			// Vegetation type
			TellervoGenericFieldProperty pd2 =  TellervoGenericFieldProperty.getVegetationTypeProperty();
			pd2.setCategoryPrefix(rootName);
			fieldMap.put(pd2.getName(), pd2);
			parent.addChildProperty(pd2);
			nChildren++;		
			
			// User Defined Fields			
			for(WSIUserDefinedField fld : udfdictionary)
			{
				if(fld.getAttachedto().equals(UserExtendableEntity.OBJECT))
				{
					myUserDefinedFields.add(fld);
				}
			}
		}
		else if (clazz.equals(TridasElement.class))
		{
			// User Defined Fields			
			for(WSIUserDefinedField fld : udfdictionary)
			{
				if(fld.getAttachedto().equals(UserExtendableEntity.ELEMENT))
				{
					myUserDefinedFields.add(fld);
				}
			}
		}
		else if(clazz.equals(TridasSample.class))
		{
			// External sample id
			TellervoGenericFieldProperty pd =  TellervoGenericFieldProperty.getSampleExternalIDProperty();
			pd.setCategoryPrefix(rootName);
			fieldMap.put(pd.getName(), pd);
			parent.addChildProperty(pd);
			nChildren++;	
			
			// Curation status
			TellervoGenericFieldProperty pd2 =  TellervoGenericFieldProperty.getSampleCurationStatusProperty();
			pd2.setCategoryPrefix(rootName);
			fieldMap.put(pd2.getName(), pd2);
			parent.addChildProperty(pd2);
			nChildren++;	
			
			// Sample status
			TellervoGenericFieldProperty pd3 =  TellervoGenericFieldProperty.getSampleStatusProperty();
			pd2.setCategoryPrefix(rootName);
			fieldMap.put(pd3.getName(), pd3);
			parent.addChildProperty(pd3);
			nChildren++;	
			
			// User Defined Fields			
			for(WSIUserDefinedField fld : udfdictionary)
			{
				if(fld.getAttachedto().equals(UserExtendableEntity.SAMPLE))
				{
					myUserDefinedFields.add(fld);
				}
			}
		}
		else if (clazz.equals(TridasRadius.class))
		{
			// User Defined Fields			
			for(WSIUserDefinedField fld : udfdictionary)
			{
				if(fld.getAttachedto().equals(UserExtendableEntity.RADIUS))
				{
					myUserDefinedFields.add(fld);
				}
			}
		}
		else if (clazz.equals(TridasMeasurementSeries.class))
		{
			// User Defined Fields			
			for(WSIUserDefinedField fld : udfdictionary)
			{
				if(fld.getAttachedto().equals(UserExtendableEntity.SERIES))
				{
					myUserDefinedFields.add(fld);
				}
			}
		}
		
		
		
		
		// Add the true user defined fields to the list			
		for(WSIUserDefinedField fld : myUserDefinedFields)
		{
			Class clazz2 = String.class;
			
			if(fld.isSetDictionarykey())
			{
				clazz2 = WSIUserDefinedTerm.class;
			}
			else if(fld.getDatatype().equals(UserExtendableDataType.XS___STRING))
			{
				clazz2 = String.class;
			}
			else if(fld.getDatatype().equals(UserExtendableDataType.XS___INT))
			{
				clazz2 = Integer.class;
			}
			else if(fld.getDatatype().equals(UserExtendableDataType.XS___FLOAT))
			{
				clazz2 = Float.class;
			}
			else if(fld.getDatatype().equals(UserExtendableDataType.XS___BOOLEAN))
			{
				clazz2 = Boolean.class;
			}					
			else
			{
				log.error("Invalid data type!");
			}
			TellervoGenericFieldProperty pd4 = new TellervoGenericFieldProperty(entityName + ".customFields."+fld.getName(), fld.getName(), fld.getName(),
					clazz2, TridasSample.class, false, false);
			
			if(fld.isSetDictionarykey())
			{
				pd4.setDictionary(fld.getDictionarykey());
			}
						
			pd4.humanReadableName = fld.getLongfieldname();
			pd4.setCategoryPrefix(rootName);
			fieldMap.put(pd4.getName(), pd4);
			custom.addChildProperty(pd4);
			nChildren++;	
		}
		
		// Add Custom Fields category, but only if there are some custome fields
		if(custom.getChildProperties().size()>0)
		{
			parent.addChildProperty(custom);
		}

		return nChildren;
	}

	public static List<TridasEntityProperty> buildDerivationList(Class<?> clazz) {
		TridasEntityProperty rootEntity = new TridasEntityProperty(null, null);
		List<Class<?>> classDerivationTree = new ArrayList<Class<?>>();
		String rootEntityName = null;

		// build a list of the class derivations
		do {
			XmlRootElement rootElement;
			if (rootEntityName == null
					&& (rootElement = clazz.getAnnotation(XmlRootElement.class)) != null) {
				rootEntityName = rootElement.name();
			}

			classDerivationTree.add(clazz);
			clazz = clazz.getSuperclass();
		} while (!clazz.equals(Object.class));

		// reverse it so we start at the supermost-class
		Collections.reverse(classDerivationTree);

		for (Class<?> myClass : classDerivationTree)
		{			
			buildDerivationList(rootEntityName, myClass, rootEntity, rootEntityName, false);
		}

		return rootEntity.getChildProperties();
	}
	
	public static void dumpPropertyList(List<TridasEntityProperty> propertyList, int depth) {		
		for (TridasEntityProperty pd : propertyList) {
			for(int i = 0; i < depth; i++)
				System.out.print("   ");
			//System.out.print(pd.getNiceName() + ": " + pd.clazz.getName());
			System.out.print(pd.qname + ": " + pd.getType().getName());
			if (pd.required)
				System.out.print(" [REQUIRED] ");
			if (pd.isList)
				System.out.print(" [LIST] ");
			if (pd.isEditable())
				System.out.print(" [EDITABLE] ");
			
			
			if (pd.getChildProperties().size() > 0)
				System.out.print(" " + pd.getChildProperties().size() + " ");
			System.out.println();
			
			dumpPropertyList(pd.getChildProperties(), depth + 1);
		}
	}

	public static void main(String[] args) {
		List<TridasEntityProperty> propertyList = new ArrayList<TridasEntityProperty>();

		propertyList = buildDerivationList(TridasProject.class);
		
		dumpPropertyList(propertyList, 0);
	}
}
