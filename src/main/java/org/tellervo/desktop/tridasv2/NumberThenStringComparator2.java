package org.tellervo.desktop.tridasv2;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.interfaces.ITridas;

public class NumberThenStringComparator2 implements Comparator<Object> {
	private final static Logger log = LoggerFactory.getLogger(NumberThenStringComparator2.class);

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

		 
		isFirstNumeric = o1.matches("^([0-9]+).*");
		isSecondNumeric = o2.matches("^([0-9]+).*");
		Integer num1 = null;
		Integer num2 = null;
		
		
		Pattern p = Pattern.compile("^([0-9]+)");
		
		try {
		if(isFirstNumeric)
		{
			Matcher m = p.matcher(o1);
		    // if an occurrence if a pattern was found in a given string...
		    if (m.find()) {
		        // ...then you can use group() methods.
		        String found = m.group(0);
		        num1 = Integer.valueOf(found);
		    }
		}
	    
		if(isSecondNumeric)
		{
			Matcher m = p.matcher(o2);
		    // if an occurrence if a pattern was found in a given string...
		    if (m.find()) {
		        // ...then you can use group() methods.
		        String found = m.group(0);
		        num2 = Integer.valueOf(found);
		    }
		}
		} catch (NumberFormatException ex)
		{
			log.debug("Failed to extract number from string.  Falling back to treating as string");
			isFirstNumeric = false;
			isSecondNumeric = false;
		}

		int result;
		if (isFirstNumeric) {
			if (isSecondNumeric) {
				result = num1.compareTo(num2);
				if(result==0) {
					// Numbers match so do a full compare for any possible remaining characters
					result = o1.compareToIgnoreCase(o2);
				}
				
				} else {
				result = -1; // numbers always smaller than letters
			}
		} else {
			if (isSecondNumeric) {
				result = 1; // numbers always smaller than letters
			} else {
				result = o1.compareToIgnoreCase(o2);
			}
		}
		
		log.debug("Comparing '"+o1+"' with '"+02+"' = "+result);
		return result;

	}
}


