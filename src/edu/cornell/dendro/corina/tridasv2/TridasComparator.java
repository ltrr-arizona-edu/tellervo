/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.util.Comparator;

import org.tridas.interfaces.ITridas;

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
		TITLES
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
	private final Type comparatorType;
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
		case SITE_CODES_THEN_TITLES: {
			if(o1 instanceof TridasObjectEx) {
				TridasObjectEx t1 = (TridasObjectEx) o1;
				
				v1 = t1.hasLabCode() ? t1.getLabCode() : t1.getTitle();
			}
			else
				v1 = o1.getTitle();
			
			if(o2 instanceof TridasObjectEx) {
				TridasObjectEx t2 = (TridasObjectEx) o2;
				
				v2 = t2.hasLabCode() ? t2.getLabCode() : t2.getTitle();
			}
			else
				v2 = o2.getTitle();
			
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

}
