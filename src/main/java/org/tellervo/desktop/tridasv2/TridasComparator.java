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
/**
 * 
 */
package org.tellervo.desktop.tridasv2;

import java.util.Comparator;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.BaseSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

/**
 * A simple comparator for sorting lists of ITridas objects by
 * title. Also supports site codes.
 * 
 * @author Lucas Madar
 * @version $Id$
 */
public class TridasComparator implements Comparator<ITridas> {
	
	/**
	 * The type of comparator that we support
	 */
	public enum Type {
		SITE_CODES_THEN_TITLES,
		TITLES, 
		LAB_CODE_THEN_TITLES,
		SENIOR_ENTITIES_THEN_LAB_CODE_THEN_TITLES,
		SENIOR_ENTITIES_THEN_TITLES
	}

	public enum CompareBehavior {
		IGNORE_CASE,
		CASE_SENSITIVE,
		AS_NUMBERS_THEN_STRINGS
	}
	
	public enum NullBehavior {
		NULLS_FIRST,
		NULLS_LAST
	}
	
	/** The type of comparator, specified on construction */
	private Type comparatorType;
	/** The behavior when we encounter nulls */
	private final NullBehavior nullBehavior;
	/** How we compare strings */
	private final CompareBehavior compareBehavior;
	
	/**
	 * Constructs a TridasComparator of the 'SITE_CODES_THEN_TITLES' type
	 * with the 'NULLS_LAST' null behavior and case insensitive.
	 */
	public TridasComparator() {
		this(Type.SITE_CODES_THEN_TITLES, NullBehavior.NULLS_LAST, CompareBehavior.IGNORE_CASE);
	}
	
	/**
	 * Construct a comparator of the given type with the given null behavior
	 * @param comparatorType
	 * @param nullBehavior
	 */
	public TridasComparator(Type comparatorType, NullBehavior nullBehavior, CompareBehavior compareBehavior) {
		if(comparatorType == null || nullBehavior == null || compareBehavior == null)
			throw new NullPointerException();
		
		this.comparatorType = comparatorType;
		this.nullBehavior = nullBehavior;
		this.compareBehavior = compareBehavior;
	}
	
	public int compare(ITridas o1, ITridas o2) {
		String v1, v2;
		
		switch(comparatorType) {
		
		case SENIOR_ENTITIES_THEN_LAB_CODE_THEN_TITLES:
		{
			if(!o1.getClass().equals(o2.getClass()))
			{
				Integer o1level = TridasComparator.getEntityLevel(o1);
				Integer o2level = TridasComparator.getEntityLevel(o2);
				return o1level.compareTo(o2level);
			}
			
			// Set to LAB_CODE_THEN_TITLES from now on
			comparatorType = Type.LAB_CODE_THEN_TITLES;
		}
		
		case SENIOR_ENTITIES_THEN_TITLES:
		{
			if(!o1.getClass().equals(o2.getClass()))
			{
				Integer o1level = TridasComparator.getEntityLevel(o1);
				Integer o2level = TridasComparator.getEntityLevel(o2);
				return o1level.compareTo(o2level);
			}
			
			// Set to LAB_CODE_THEN_TITLES from now on
			comparatorType = Type.TITLES;
		}
		
		case SITE_CODES_THEN_TITLES: {
			if(o1 instanceof TridasObjectEx) {
				TridasObjectEx t1 = (TridasObjectEx) o1;
				
				v1 = t1.hasLabCode() ? t1.getLabCode() : t1.getTitle();
				
				if(o2 instanceof TridasObjectEx) {
					TridasObjectEx t2 = (TridasObjectEx) o2;
					
					v2 = t2.hasLabCode() ? t2.getLabCode() : t2.getTitle();
				}
				else
				{
					v2 = o2.getTitle();
				}
				
			}
			else
			{
				v1 = o1.getTitle();
				v2 = o2.getTitle();
			}			
		}
		
		case LAB_CODE_THEN_TITLES: {
			
			if(o1 instanceof TridasObjectEx) {
				TridasObjectEx t1 = (TridasObjectEx) o1;
				
				v1 = t1.hasLabCode() ? t1.getLabCode() : t1.getTitle();
			}
			else if(o1 instanceof TridasSample){
				TridasSample s1 = (TridasSample) o1;
				try{
				v1 = GenericFieldUtils.findField(s1, "tellervo.internal.labcodeText").getValue().toString();
				} catch (NullPointerException e)
				{
					v1 = o1.getTitle();
				}
			}
			else{
				v1 =o1.getTitle();
			}
			
			if(o2 instanceof TridasObjectEx) {
				TridasObjectEx t2 = (TridasObjectEx) o2;
				
				v2 = t2.hasLabCode() ? t2.getLabCode() : t2.getTitle();
			}
			else if(o2 instanceof TridasSample){
				TridasSample s2 = (TridasSample) o2;
				try{
				v2 = GenericFieldUtils.findField(s2, "tellervo.internal.labcodeText").getValue().toString();
				} catch (NullPointerException e)
				{
					v2 = o2.getTitle();
				}
			}
			else{
				v2 =o2.getTitle();
			}
			
			break;
			
		}
		
		case TITLES:
		default:
			v1 = o1.getTitle();
			v2 = o2.getTitle();
			break;						
		}

		// they're equal if they're both null, otherwise they go at the beginning or the end
		if(v1 == null)
			return (v2 == null) ? 0 : ((nullBehavior == NullBehavior.NULLS_FIRST) ? -1 : 1);
		
		// we know v1 is not null at this point...
		if(v2 == null)
			return (nullBehavior == NullBehavior.NULLS_LAST) ? -1 : 1; 

		// ok, so we are trying to find things that might be numbers, or prefixed as numbers then strings
		if(compareBehavior == CompareBehavior.AS_NUMBERS_THEN_STRINGS) {
			Integer i1 = null, i2 = null;
			
			try {
				i1 = Integer.valueOf(v1);
			} catch (NumberFormatException nfe) {	
			}			
			
			try {
				i2 = Integer.valueOf(v2);
			} catch (NumberFormatException nfe) {	
			}
			
			// string vs int: Strings go last
			if(i1 == null && i2 != null)
				return 1;
			if(i2 == null && i1 != null)
				return -1;
	
			// both int
			if(i1 != null && i2 != null)
				return i1.compareTo(i2);
		}
		
		// flat-out string compare
		return (compareBehavior != CompareBehavior.CASE_SENSITIVE) ? v1.compareToIgnoreCase(v2) : v1.compareTo(v2);
	}

	public static Integer getEntityLevel(ITridas entity)
	{
		if(entity instanceof TridasObjectEx || entity instanceof TridasObject)
		{
			return 1;
		}
		else if (entity instanceof TridasElement)
		{
			return 2;
		}
		else if (entity instanceof TridasSample)
		{
			return 3;	
		}
		else if (entity instanceof TridasRadius)
		{
			return 4;	
		}
		else if (entity instanceof BaseSeries)
		{
			return 5;	
		}
		
		return null;
	}
}
