package org.tellervo.desktop.bulkdataentry.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.utils.URIUtils;
import org.tridas.schema.TridasFile;

public class TridasFileList extends ArrayList<TridasFile> {

	private static final long serialVersionUID = 1L;	
	
	
	public TridasFileList()
	{
		super();
	}
	
	public TridasFileList (List<TridasFile> files)
	{
		super();
		this.addAll(files);
	}
	
	public TridasFileList(String str) throws Exception
	{
		super();
		
		List<String> items = Arrays.asList(str.split("\\s*;\\s*"));
		TridasFileList flist  = new TridasFileList();
		
		
		for(String item : items)
		{
			TridasFile f = new TridasFile();
			URI.create(item);
			f.setHref(item);
			flist.add(f);
		}
		
		this.addAll(flist);
    	
	}
	
	@Override
	public String toString()
	{
		String str = "";
		
		if(this.size()<1) return str;
		
		for(int i=0; i<this.size(); i++)
		{
			TridasFile f = this.get(i);			
			str+=URI.create(f.getHref())+"; ";
		}
		
		return str.substring(0, str.length()-1);
		
	}

}
