package edu.cornell.dendro.corina.tridasv2;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tridas.schema.*;

public class SwineFlu {
	private static class PropertyData {
		String name;
		Class<?> clazz;
		boolean isList;
		boolean required;
	}
	
	private static Set<Class<?>> ignoreClasses = new HashSet<Class<?>>();
	
	static {
		ignoreClasses.add(TridasObject.class);
		ignoreClasses.add(TridasElement.class);
		ignoreClasses.add(TridasSample.class);
		ignoreClasses.add(TridasRadius.class);
		ignoreClasses.add(TridasMeasurementSeries.class);
		ignoreClasses.add(TridasDerivedSeries.class);
		ignoreClasses.add(BaseSeries.class);
	}
	
	private static void blah(String entityName, Class<?> clazz,
			List<PropertyData> propertyList) {
		Map<String, PropertyData> fieldMap = new HashMap<String, PropertyData>();

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

				PropertyData pd = new PropertyData();
				fieldMap.put(s, pd);

				pd.name = entityName + "." + s;
				pd.required = pd.isList = false;
			}

			for (Field f : clazz.getDeclaredFields()) {
				String fieldType = f.getGenericType().toString();
				PropertyData pd = fieldMap.get(f.getName());
				XmlElement xmlElement;

				// ignore this field if we don't have property data for it
				if (pd == null) {
					// System.out.println("No property data for " + f.getName()
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

				// skip things we ignore
				if (ignoreClasses.contains(pd.clazz))
					continue;
				
				// add type to property list
				propertyList.add(pd);

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

				blah(pd.name, pd.clazz, propertyList);
			}
		}
	}

	public static List<PropertyData> bluh(Class<?> clazz) {
		List<PropertyData> propertyList = new ArrayList<PropertyData>();
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
			blah(rootEntityName, myClass, propertyList);

		return propertyList;
	}

	public static void main(String[] args) {
		List<PropertyData> propertyList = new ArrayList<PropertyData>();

		propertyList = bluh(TridasObject.class);

		for (PropertyData pd : propertyList) {
			System.out.print(pd.name + ": " + pd.clazz.getName());
			if (pd.required)
				System.out.print(" [REQUIRED] ");
			if (pd.isList)
				System.out.print(" [LIST] ");
			System.out.println();
		}

	}
}
