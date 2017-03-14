package com.rediscov.util;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.tridas.schema.TridasProject;

import com.rediscov.schema.RediscoveryExport;

public class ICMSImporter {

	List<RediscoveryExport> records;
 
	HashMap<String, TridasProject> projectHash = new HashMap<String, TridasProject>();
	HashMap<String, UUID> objectHash = new HashMap<String, UUID>();
	HashMap<String, UUID> elementHash = new HashMap<String, UUID>();
	HashMap<String, UUID> sampleHash = new HashMap<String, UUID>();

	
	public ICMSImporter(String filename)
	{
		records = RediscoveryExportEx.getICMSRecordsFromXMLFile(filename, true);
		
		for(RediscoveryExport rec : records)
		{
			
		}
	}
	
	private TridasProject getProject(String code)
	{
		if(projectHash.containsKey(code))
		{
			// Seen previously in this import so just return
			return projectHash.get(code);
		}
		
		// Not seen this yet, so check if it's in the DB
		
		
		
		
		
		return null;
	}
	
	
	
}
