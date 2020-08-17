package org.tellervo.desktop.maventests;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.util.DictionaryUtil;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.TestCase;

public class GsonTest extends TestCase  {
	
	public GsonTest(String name) {
		super(name);
	}
	
	public void GsonSerializeTest() {
			
		
		TridasObject object = new TridasObject();
		object.setTitle("Test object");
		//object.setType(DictionaryUtil.getControlledVocForName("Forest", "objectTypeDictionary"));
		object.setOwner("Test owner");
		object.setComments("Comment comment comment");
		TridasIdentifier id = new TridasIdentifier();
		id.setValue("02fe4ee2-b3cf-11e9-9342-ab3534663ea7");
		object.setIdentifier(id);
		
		
		
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		System.out.println(gson.toJson(object));

	}
	
	public void GsonDeSerializeTest() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = "{\"owner\":\"Test owner\",\"title\":\"Test object\",\"identifier\":{\"value\":\"02fe4ee2-b3cf-11e9-9342-ab3534663ea7\"},\"comments\":\"Comment comment comment\"}"; 
	
		TridasObject object = gson.fromJson(json, TridasObject.class);
		
		System.out.println(object.toString());
	}
	
}
