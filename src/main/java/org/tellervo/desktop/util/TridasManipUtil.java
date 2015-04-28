package org.tellervo.desktop.util;

import java.util.List;

import org.tellervo.desktop.core.App;
import org.tridas.io.util.TridasUtils;
import org.tridas.util.TridasObjectEx;

public class TridasManipUtil {

	
	/**
	 * Query the object dictionary to find an object based on it's lab code
	 * 
	 * @param code
	 * @return
	 */
	public static TridasObjectEx getTridasObjectByCode(String code)
	{
		if(code==null) return null;

		List<TridasObjectEx> entities = App.tridasObjects.getObjectList();

		for(TridasObjectEx obj : entities)
		{
			if(TridasUtils.getGenericFieldByName(obj, TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE).getValue().equals(code))
			{
				return obj;
			}
		}
		return null;
	}
	
}