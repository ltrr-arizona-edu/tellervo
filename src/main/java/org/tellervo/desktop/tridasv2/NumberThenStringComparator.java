package org.tellervo.desktop.tridasv2;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.interfaces.ITridas;

public class NumberThenStringComparator implements Comparator<Object> {
	private final static Logger log = LoggerFactory.getLogger(NumberThenStringComparator.class);

	@Override
	public int compare(Object oo1, Object oo2) {
		boolean isFirstNumeric, isSecondNumeric;
		String o1;
		String o2;
		
		if(oo1 instanceof ITridas)
		{
			o1 = ((ITridas)oo1).getTitle();
		}
		else	
		{
			o1 = oo1.toString();
		}
		 
		if(oo2 instanceof ITridas)
		{
			o2 = ((ITridas)oo2).getTitle();
		}
		else	
		{
			o2 = oo2.toString();
		}


		isFirstNumeric = o1.matches("\\d+");
		isSecondNumeric = o2.matches("\\d+");

		if (isFirstNumeric) {
			if (isSecondNumeric) {
				return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
			} else {
				return -1; // numbers always smaller than letters
			}
		} else {
			if (isSecondNumeric) {
				return 1; // numbers always smaller than letters
			} else {
				return o1.compareToIgnoreCase(o2);
			}
		}
	}
}


