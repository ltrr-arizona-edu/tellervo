package org.tellervo.desktop.odk.fields;

import java.util.Comparator;

public class ODKFieldComparator implements Comparator<AbstractODKField> {

	@Override
	public int compare(AbstractODKField o1, AbstractODKField o2) {
		
		
		// Sort by sort indices
		if(o1.getSortIndex()>o2.getSortIndex())
		{
			return 1;
		}
		else if (o1.getSortIndex()<o2.getSortIndex())
		{
			return -1;
		}
		else
		{
			// If indices are the same then sort by field name
			return o1.getFieldName().compareTo(o2.getFieldName());			
		}

	}
}

