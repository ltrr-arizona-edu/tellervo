package edu.cornell.dendro.corina.gis;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import edu.cornell.dendro.corina.util.IOUtils;
import edu.cornell.dendro.corina.util.UnicodeBOMInputStream;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ITRDBMarkerLayerBuilder {

	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private String layerName = "ITRDB sites";
	private final static Logger log = LoggerFactory.getLogger(ITRDBMarkerLayerBuilder.class);
	private final String itrdbFilename;
	
	public ITRDBMarkerLayerBuilder()
	{
		String home = System.getProperty("user.home");
		if (!home.endsWith(File.separator))
			home = home + File.separator;
		
		itrdbFilename = home+"itrdb.kml";
	}
	
	/**
	 * Create a WWJ marker layer from a KML file
	 * 
	 * @param kmlfile
	 * @return
	 */
	public static MarkerLayer createLayerFromKML(String kmlfile)
	{	
		ITRDBMarkerLayerBuilder layer = new ITRDBMarkerLayerBuilder();
		layer.loadKMLFile(kmlfile);
		return layer.getMarkerLayer();
	}
	

	/**
	 * Create a WWJ marker layer from a KMZ file
	 * 
	 * @param kmzfile
	 * @return
	 */
	public static MarkerLayer createLayerFromKMZ(String kmzfile)
	{
		ITRDBMarkerLayerBuilder layer = new ITRDBMarkerLayerBuilder();	
		layer.loadKMZFile(kmzfile);
		return layer.getMarkerLayer();
	}
	
	/**
	 * Download location data from NOAA FTP server, unzip, then convert into a
	 * WWJ marker layer
	 * 
	 * @return
	 */
	public static MarkerLayer createITRDBLayer()
	{
		ITRDBMarkerLayerBuilder layer = new ITRDBMarkerLayerBuilder();
		URL url;
		String kmzfile = System.getProperty("java.io.tmpdir")+File.separator+"itrdb.kmz";
		try {
			url = new URL("ftp://ftp.ncdc.noaa.gov/pub/data/paleo/site_maps/tree-ring-records.kmz");
			IOUtils.copyStream(url.openStream(), new FileOutputStream(kmzfile), IOUtils.CLOSE_BOTH);
			layer.loadKMZFile(kmzfile);
		} catch (Exception e) {	
			File file = new File(layer.getItrdbFilename());
			if(file.exists())
			{
				log.error("Error downloading ITRDB data from NOAA server.  Attempting to use previously cached data instead.");
				layer.loadKMLFile(layer.getItrdbFilename());
			}
			else
			{
				log.error("Error downloading ITRDB data from NOAA server.");
				return null;
			}
		}
		finally{
			// Clean up and delete kmz file
			try{
			    File kmz = new File(kmzfile);
			    kmz.delete();
			}catch (Exception e){}
		}
		
		
		return layer.getMarkerLayer();
	}
	
	
	/**
	 * Load a KMZ file 
	 * 
	 * @param kmzfile
	 */
	private void loadKMZFile(String kmzfile)
	{
		log.debug("Load KMZ file");
		
		ZipFile zipFile = null;
		@SuppressWarnings("rawtypes")
		Enumeration entries;
		
		try {
		      zipFile = new ZipFile(kmzfile);
		      entries = zipFile.entries();

		      while(entries.hasMoreElements()) {
		    	 		    	  
		        ZipEntry entry = (ZipEntry)entries.nextElement();
		        
		        if(entry.getName().equals("doc.kml"))
		        {
			        log.debug("Extracting file: " + entry.getName());
			        			  
			        
			        UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(zipFile.getInputStream(entry));
			        ubis.skipBOM();
			        
			        copyInputStream(ubis,
			           new BufferedOutputStream(new FileOutputStream(itrdbFilename)));
			        break;
		        }

		        log.error("Unable to find KML file within KMZ file");
		        return;
		      }
  
	    } catch (IOException ioe) {
	    	log.error("Unhandled exception:");
	        ioe.printStackTrace();
	        return;
	      }
	    finally{
	    	try {
				zipFile.close();
			} catch (IOException e) { }
	    }
	    
	    loadKMLFile(itrdbFilename);
		
		
	}
	
	
	public static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		InputStreamReader isr = new InputStreamReader(in);
		OutputStreamWriter osr = new OutputStreamWriter(out);

		int data = isr.read();
		while (data != -1) {
			char theChar = (char) data;
			out.write(theChar);
			data = isr.read();

		}

		in.close();
		out.close();
		isr.close();
		osr.close();
	}
	
	/**
	 * Load KML file from disk
	 * 
	 * @param kmlfile
	 */
	private void loadKMLFile(String kmlfile)
	{
		log.debug("Loading KML file from " +kmlfile);
		
		Kml kml = null;
		try{
		 kml = Kml.unmarshal(new File(kmlfile));
		} catch (Exception e)
		{
			log.error("Error reading KML file");
			return;
		}	
		
		if (kml != null)
		{	
			Feature feature = kml.getFeature();
			processFeature(null, feature);
		}
		
		
	}
	
	/**
	 * Get marker layer from class
	 * 
	 * @return
	 */
	public MarkerLayer getMarkerLayer()
	{
		log.debug("Number of markers: "+markers.size());
		MarkerLayer layer = new MarkerLayer(markers);
		layer.setName(this.getName());
		
        layer.setOverrideMarkerElevation(true);
        layer.setElevation(0);
        layer.setEnablePickSizeReturn(true);
        
        return layer;
	}
	
	
	/**
	 * Get layer name
	 * 
	 * @return
	 */
	public String getName()
	{
		return this.layerName;
	}
	
	/**
	 * Process KML Feature
	 * @param parentFeature
	 * @param feature
	 */
	private void processFeature(Feature parentFeature, Feature feature) {
		
		//log.debug("Processing feature");
		
		if (feature instanceof Document) {
			processDocument(parentFeature, (Document) feature);
		} else if (feature instanceof Folder) {
			processFolder(parentFeature, (Folder) feature);
		} else if (feature instanceof Placemark) {
			processPlacemark(parentFeature, (Placemark) feature);
		} else {
			log.debug("Feature " + feature.getName() + " : " + feature);
		}
	}

	/**
	 * Process KML Document
	 * 
	 * @param parentFeature
	 * @param doc
	 */
	private void processDocument(Feature parentFeature, Document doc) {
		log.debug("Document " + doc.getName());
		
		List<Feature> features = doc.getFeature();

		

		for (Feature docFeature : features) {
			processFeature(doc, docFeature);
		}
	}

	/**
	 * Process KML Folder
	 * 
	 * @param parentFeature
	 * @param folder
	 */
	private void processFolder(Feature parentFeature, Folder folder) {
		log.debug("Folder " + folder.getName());
		
		List<Feature> features =  folder.getFeature();

		

		for (Feature folderFeature : features) {
			processFeature(folder, folderFeature);
		}
	}

	/**
	 * Process KML Placemark
	 * @param parentFeature
	 * @param placemark
	 */
	private void processPlacemark(Feature parentFeature, Placemark placemark) {
		//log.debug("Placemark " + placemark.getName());
		
		Point point = (Point) placemark.getGeometry();
		List<Coordinate> coordinates = point.getCoordinates();
		
		for (Coordinate coordinate : coordinates) {
			//log.debug("Coordinates: " +coordinate.getLongitude() +", "+ coordinate.getLatitude());	
			ITRDBMarker mkr = new ITRDBMarker(Position.fromDegrees(coordinate.getLatitude(), coordinate.getLongitude()), 
					new BasicMarkerAttributes(Material.CYAN, BasicMarkerShape.CUBE, 0.6d));
			
			markers.add(mkr);
		}
	}

	/**
	 * Get the filename for the cached ITRDB KML file
	 * 
	 * @return
	 */
	public String getItrdbFilename() {
		return itrdbFilename;
	}

	
}
