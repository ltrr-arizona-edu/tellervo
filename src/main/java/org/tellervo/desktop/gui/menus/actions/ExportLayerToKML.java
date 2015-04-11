package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwindx.examples.kml.KMLDocumentBuilder;

import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.xml.stream.XMLStreamException;

import org.tellervo.desktop.gis.TridasMarker;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.util.TridasObjectEx;

public class ExportLayerToKML extends AbstractAction {


	private static final long serialVersionUID = 1L;
	private ArrayList<TridasMarker> markers;
	
	public ExportLayerToKML(ArrayList<TridasMarker> markers)
	{
		super("Export to KML", Builder.getIcon("kml.png", 22));
		this.markers =markers;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		
		
		
		try {
		    // Create a StringWriter to collect KML in a string buffer
	        Writer stringWriter = new StringWriter();
	
	        // Create a document builder that will write KML to the StringWriter
	        KMLDocumentBuilder kmlBuilder = new KMLDocumentBuilder(stringWriter);
	
	        // Export the objects
	        for(PointPlacemark p : getPlacemarks())
	        {
	        	kmlBuilder.writeObject(p);
				
	        }
	        kmlBuilder.close();
	
	        FileWriter fw = new FileWriter("/tmp/out.kml");
       
	        fw.write(stringWriter.toString());
	        fw.close();
	        
	        

		} catch (IOException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private ArrayList<PointPlacemark> getPlacemarks()
	{
		ArrayList<PointPlacemark> placemarks = new ArrayList<PointPlacemark>();
 	   
  	   
		for(TridasMarker tmarker : markers)
		{        
			
			 PointPlacemark placemark = new PointPlacemark(tmarker.getPosition());

			 
		     placemark.setLabelText(tmarker.getEntity().getTitle());
		     placemark.setLineEnabled(false);
		     
		     
		     placemark.setValue(AVKey.BALLOON_TEXT,  getBalloonText(tmarker.getEntity()));
		     placemark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
	         placemarks.add(placemark);
	    }
	    
	    return placemarks;
	}
	
	
	
	 private String getBalloonText(ITridas entity)
	    {
	    	String content = "";
	    	
	    	if (entity instanceof TridasObjectEx)
	    	{
	    		TridasObjectEx obj = (TridasObjectEx) entity;   		
	    		if(obj.getLabCode()!=null)
	    		{
	    			content += "<font color=\"#04258E\"><b>"+obj.getLabCode()+"</b></font>";
	    		}
	    		
	    		content+= "<br><b>"+obj.getTitle()+"</b><br/>";
	    		
	    		if(obj.isSetType())
	    		{
	    			content += "<font size=\"2\"><i>"+obj.getType().getNormal()+"</i></font><hr/><br/><br>";
	    		}

	    	}
	    	else if (entity instanceof TridasElement)
	    	{

	    		TridasElement elem = (TridasElement) entity;
	    		String objectCode = "??";
	    		for(TridasGenericField gf : elem.getGenericFields())
	    		{
	    			if (gf.getName().equals(TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE)){
	    				objectCode = gf.getValue().toString();
	    			}
	    		}
	    		
	    		content += "<b>"+objectCode+"-"+elem.getTitle()+"</b><br/>";
	    		   		
	    		if(elem.isSetTaxon())
	    		{
	    			content += "<font size=\"2\"><i>"+elem.getTaxon().getNormal()+"</i></font><hr/><br/><br>";
	    		}
	    		
	    		if(elem.isSetType())
	    		{
	    			content += "<br><font size=\"2\">Element type : "+elem.getType().getNormal()+"</font><hr/><br/><br>";

	    		}
	    		
	    	}

	    	
	    	return content;
	    }

}
