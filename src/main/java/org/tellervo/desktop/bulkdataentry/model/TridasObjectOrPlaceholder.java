package org.tellervo.desktop.bulkdataentry.model;

import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

public class TridasObjectOrPlaceholder {

	
	private TridasObject obj;
	private String code;
	
	public TridasObjectOrPlaceholder(TridasObject obj)
	{
		this.obj = obj;
	}
	
	public TridasObjectOrPlaceholder(String code)
	{
		this.code = code;
	}
	
	
	public TridasObject getTridasObject()
	{
		return obj;
	}
	
	public String getCode()
	{
		if(obj!=null)
		{
			if(obj instanceof TridasObjectEx)
			{
				TridasObjectEx o = (TridasObjectEx) obj;
				TridasObjectEx parent = o.getParent();
				
				if(parent!=null)
				{
					return parent.getLabCode()+">"+o.getLabCode();
				}
				else
				{
					return o.getLabCode();
				}
			}
			else
			{
				TridasGenericField gf = TridasUtils.getGenericFieldByName(obj, "tellervo.objectLabCode");
				if(gf==null) return "";
				
				return gf.getValue();
			}
		}
		else
		{
			return code;
		}
	}
	
	public String toString()
	{
		return getCode();
	}
	
	public String getTitle()
	{
		if(obj!=null)
		{
			if(obj instanceof TridasObjectEx)
			{
				return ((TridasObjectEx) obj).getTitle();
			}
			else if (obj instanceof TridasObject)
			{
				return ((TridasObject) obj).getTitle();

			}
		}

		return "";
		
	}
	
}
