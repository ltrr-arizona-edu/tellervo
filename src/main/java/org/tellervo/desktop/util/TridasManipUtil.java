package org.tellervo.desktop.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
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

	
	public static TridasGenericField createGenericField(String fieldname, String fieldvalue, String datatype)
	{
		TridasGenericField gf = new TridasGenericField();
		gf.setName(fieldname);
		gf.setValue(fieldvalue);
		gf.setType(datatype);
		
		return gf;
	}
	
	/**
	 * NOT TESTED!
	 * 
	 * @param objects
	 * @return
	 */
	public static ArrayList<TridasObject> setInternalLabCodes(ArrayList<TridasObject> objects)
	{
		for(TridasObject o : objects)
		{	
			// make lab code
			LabCode labcode = new LabCode();
			labcode.appendSiteCode(GenericFieldUtils.findField(o, "tellervo.siteCode").getValue());
			
			ArrayList<TridasObject> ob2 = TridasUtils.getObjectList(o);
			for(TridasObject o2 : ob2)
			{
				// Only use top level site code
				//labcode.appendSiteCode(GenericFieldUtils.findField(o2, "tellervo.siteCode").getValue());
				
				for(TridasElement e : o2.getElements())
				{
					labcode.setElementCode(e.getTitle());
					GenericFieldUtils.findOrAddField(e, "tellervo.internal.labcodeText").setValue(LabCodeFormatter.getElementPrefixFormatter().format(labcode));
					
					for(TridasSample s : e.getSamples())
					{
						labcode.setSampleCode(s.getTitle());
						GenericFieldUtils.findOrAddField(e, "tellervo.internal.labcodeText").setValue(LabCodeFormatter.getSamplePrefixFormatter().format(labcode));
						
						for(TridasRadius r : s.getRadiuses())
						{
							labcode.setRadiusCode(r.getTitle());
							GenericFieldUtils.findOrAddField(e, "tellervo.internal.labcodeText").setValue(LabCodeFormatter.getRadiusPrefixFormatter().format(labcode));
							
							for(TridasMeasurementSeries ms : r.getMeasurementSeries())
							{
								labcode.setSeriesCode(ms.getTitle());
								GenericFieldUtils.findOrAddField(e, "tellervo.internal.labcodeText").setValue(LabCodeFormatter.getSeriesPrefixFormatter().format(labcode));

							}
						}
					}
				}
			}
		}
		
		return objects;
	}
	
	
	
	/**
     * Stupid function to get a list of samples from a list of objects
     * 
     * Has the side effect of setting tellervo.internal.labcode generic field ;)
     * Hack to get lab code!
     * 
     * @param objects
     * @param objsThisLevel
     * @param returns
     * @return
     */
    public static List<TridasSample> getSamplesList(List<TridasObject> objects, 
    		TridasObject[] objsThisLevel, List<TridasSample> returns) {
    	
    	// create this on the fly
    	if(returns == null)
    		returns = new ArrayList<TridasSample>();
    	
    	for(TridasObject obj : objects) {
    		
    		// handle stupid recursive objects
    		List<TridasObject> currentObjects;    		
    		if(objsThisLevel == null)
    			currentObjects = new ArrayList<TridasObject>();
    		else 
    			currentObjects = new ArrayList<TridasObject>(Arrays.asList(objsThisLevel));
    		
			currentObjects.add(obj);
			
			// grar...
			for(@SuppressWarnings("unused") TridasObject obj2 : obj.getObjects()) {
				getSamplesList(obj.getObjects(), currentObjects.toArray(new TridasObject[0]), returns);
			}
			
			for(TridasElement ele : obj.getElements()) {
				for(TridasSample samp : ele.getSamples()) {
					
					// make lab code
					LabCode labcode = new LabCode();
					
					// objects first...
					labcode.appendSiteCode(((TridasObjectEx) currentObjects.get(0)).getLabCode());
					
					// Cornell only wants top level object in lab code. 
					// Make this client selectable before releasing to the world
					
					/*for(TridasObject obj2 : currentObjects) {
						if(obj2 instanceof TridasObjectEx)
							labcode.appendSiteCode(((TridasObjectEx) obj2).getLabCode());
						else
							labcode.appendSiteCode(obj2.getTitle());
						labcode.appendSiteTitle(obj2.getTitle());
					}
					*/
					
					labcode.setElementCode(ele.getTitle());
					labcode.setSampleCode(samp.getTitle());
					
					// set the lab code kludgily on the sample
					GenericFieldUtils.setField(samp, "tellervo.internal.labcodeText", 
							LabCodeFormatter.getRadiusPrefixFormatter().format(labcode));
					
					// add the sample to the returns list
					returns.add(samp);
				}
			}
    	}
    	
    	return returns;
    }
 
	
}