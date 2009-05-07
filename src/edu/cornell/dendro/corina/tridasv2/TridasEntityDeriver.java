package edu.cornell.dendro.corina.tridasv2;

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

import org.tridas.schema.*;

public class TridasEntityDeriver {
	
	private static Set<Class<?>> ignoreClasses = new HashSet<Class<?>>();
	
	static {
		ignoreClasses.add(TridasObject.class);
		ignoreClasses.add(TridasElement.class);
		ignoreClasses.add(TridasSample.class);
		ignoreClasses.add(TridasRadius.class);
		ignoreClasses.add(TridasMeasurementSeries.class);
		ignoreClasses.add(TridasDerivedSeries.class);
		ignoreClasses.add(BaseSeries.class);
		
		ignoreClasses.add(TridasRadiusPlaceholder.class);
	}
	
	/**
	 * Builds a PropertyData derivation list
	 * 
	 * @param entityName
	 * @param clazz
	 * @param propertyList
	 * @return the number of direct child properties of clazz
	 */
	private static int buildDerivationList(String entityName, Class<?> clazz,
			List<Property> propertyList) {
		Map<String, Property> fieldMap = new HashMap<String, Property>();
		int nChildren = 0;
		
		// get any property names and stick them at the head of the list
		XmlType type = clazz.getAnnotation(XmlType.class);
		String[] typeProperties;
		if (type != null && (typeProperties = type.propOrder()) != null
				&& typeProperties.length > 0) {

			// load into type properties
			for (String s : typeProperties) {
				// ignore zero properties
				if (s.length() == 0)
					continue;

				Property pd = new Property();
				fieldMap.put(s, pd);

				pd.name = entityName + "." + s;
			}

			for (Field f : clazz.getDeclaredFields()) {
				String fieldType = f.getGenericType().toString();
				Property pd = fieldMap.get(f.getName());
				XmlElement xmlElement;
				XmlAttribute attribute;

				// is it an xml attribute?
				if ((attribute = f.getAnnotation(XmlAttribute.class)) != null) {
					if (pd != null)
						throw new IllegalStateException("Attribute exists as element?");
					
					pd = new Property();
					pd.name = entityName + ".@" + f.getName();
					pd.required = attribute.required();
					pd.clazz = f.getType();
				} else {
					// ignore this field if we don't have property data for it
					if (pd == null) {
						// System.out.println("No property data for " +
						// f.getName()
						// + " as " + fieldType);
						continue;
					}

					// does it say it's required?
					if ((xmlElement = f.getAnnotation(XmlElement.class)) != null
							&& xmlElement.required())
						pd.required = true;

					if (fieldType.startsWith("class ")) {
						// it's a java class, whew
						pd.clazz = f.getType();
					} else if (fieldType.startsWith("java.util.List<")) {
						// it's a list of something, yay generics!
						int a = fieldType.indexOf('<');
						int b = fieldType.lastIndexOf('>');
						String listType = fieldType.substring(a + 1, b);

						try {
							pd.clazz = Class.forName(listType);
							pd.isList = true;
						} catch (ClassNotFoundException e) {
							System.out.println("Can't find class for list: "
									+ listType);
							continue;
						}
					} else {
						System.out.println("Unknown field type: " + fieldType);
						continue;
					}
				}

				// skip things we ignore
				if (ignoreClasses.contains(pd.clazz))
					continue;
				
				// add type to property list
				propertyList.add(pd);
				nChildren++;

				if (pd.clazz == null)
					throw new NullPointerException();

				// don't delve any deeper for enums
				if (pd.clazz.isEnum())
					continue;

				// only delve deeper if it's an XML-annotated class
				if (pd.clazz.getAnnotation(XmlType.class) == null)
					continue;

				// don't infinitely recurse
				if (pd.clazz.equals(clazz))
					continue;

				pd.nChildProperties = buildDerivationList(pd.name, pd.clazz, pd.childProperties);
			}
		}
		
		return nChildren;
	}

	public static List<Property> buildDerivationList(Class<?> clazz) {
		List<Property> propertyList = new ArrayList<Property>();
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
			buildDerivationList(rootEntityName, myClass, propertyList);

		return propertyList;
	}
	
	public static void dumpPropertyList(List<Property> propertyList, int depth) {		
		for (Property pd : propertyList) {
			for(int i = 0; i < depth; i++)
				System.out.print("   ");
			System.out.print(pd.getNiceName() + ": " + pd.clazz.getName());
			if (pd.required)
				System.out.print(" [REQUIRED] ");
			if (pd.isList)
				System.out.print(" [LIST] ");
			if (pd.nChildProperties > 0)
				System.out.print(" " + pd.nChildProperties + " ");
			System.out.println();
			
			dumpPropertyList(pd.childProperties, depth + 1);
		}
	}

	public static void main(String[] args) {
		List<Property> propertyList = new ArrayList<Property>();

		propertyList = buildDerivationList(TridasRadius.class);
		
		dumpPropertyList(propertyList, 0);
	}
}
