package org.tellervo.server;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestStatus;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasBedrock;
import org.tridas.schema.TridasCoverage;
import org.tridas.schema.TridasDimensions;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasSlope;
import org.tridas.schema.TridasSoil;
import org.tridas.schema.TridasUnit;
import org.tridas.util.TridasObjectEx;

/**
 * Class for turning SQL ResultSets into TRiDaS Java objects
 * 
 * @author pbrewer
 *
 */
public class SQLMarshaller {

	private static final Logger log = LoggerFactory.getLogger(TridasObjectHandler.class);

	/**
	 * Helper function to create a controlled vocabulary object from it's attributes
	 * 
	 * @param normal
	 * @param normalId
	 * @param normalStd
	 * @return
	 */
	private static ControlledVoc getControlledVoc(String normal, String normalId, String normalStd, String lang)
	{
		ControlledVoc cv = new ControlledVoc();
		cv.setLang(lang);
		cv.setNormal(normal);
		cv.setNormalId(normalId);
		cv.setNormalStd(normalStd);
		return cv;
	}
	
	/**
	 * Turn a standard Tellervo SQL ResultSet into a TridasObjectEx
	 * 
	 * @param rs
	 * @param handler
	 * @return
	 * @throws SQLException
	 */
	public static TridasObjectEx getTridasObjectFromResultSet(ResultSet rs, RequestHandler handler) throws SQLException
	{
		TridasObjectEx object = new TridasObjectEx();
		
		object.setTitle(rs.getString("title"));
		TridasIdentifier id = new TridasIdentifier();
		//TODO Add domain programmatically 
		//id.setDomain("");
		id.setValue(rs.getString("objectid"));
		object.setIdentifier(id);
		object.setCreatedTimestamp(DBUtil.getDateTimeFromDB(rs,"createdtimestamp"));
		object.setLastModifiedTimestamp(DBUtil.getDateTimeFromDB(rs,"lastmodifiedtimestamp"));
		object.setComments(rs.getString("comments"));
		// TODO replace hard coded vocabulary authority with one derived from DB
		object.setType(getControlledVoc(rs.getString("objecttype"), rs.getString("objecttypeid"), "Tellervo", "en"));
		object.setDescription(rs.getString("description"));
		//TODO linkseries

		try{
			String[] files2 = rs.getString("file").split("><");
			for(String file : files2)
			{
				TridasFile tf = new TridasFile();
				tf.setHref(file);
				object.getFiles().add(tf);
			}
		} catch (Exception ex)
		{
			log.debug("Problem getting file array");
		}
		
		object.setCreator(rs.getString("creator"));
		object.setOwner(rs.getString("owner"));
		TridasCoverage coverage = new TridasCoverage();
		coverage.setCoverageTemporal(rs.getString("coveragetemporal"));
		coverage.setCoverageTemporalFoundation(rs.getString("coveragetemporalfoundation"));
		object.setCoverage(coverage);
		object.setLocation(getTridasLocationFromResultSet(rs, handler));
		
		
		// Generic fields
		TridasGenericField labCode = new TridasGenericField();
		labCode.setName("tellervo.objectLabCode");
		labCode.setType("xs:string");
		labCode.setValue(rs.getString("code"));
		object.getGenericFields().add(labCode);
		
		TridasGenericField vegType = new TridasGenericField();
		vegType.setName("tellervo.vegetationType");
		vegType.setType("xs:string");
		vegType.setValue(rs.getString("vegetationtype"));
		object.getGenericFields().add(vegType);
		
		TridasGenericField countOfSeries = new TridasGenericField();
		countOfSeries.setName("tellervo.countOfChildSeries");
		countOfSeries.setType("xs:int");
		countOfSeries.setValue(String.valueOf(rs.getInt("countofchildvmeasurements")));
		object.getGenericFields().add(countOfSeries);
		
		
		return object;
	}
	
	
	/**
	 * Turn a standard Tellervo SQL ResultSet into a TridasElement
	 * 
	 * @param rs
	 * @param handler
	 * @return
	 * @throws SQLException
	 */
	public static TridasElement getTridasElementFromResultSet(ResultSet rs, RequestHandler handler) throws SQLException
	{
		TridasElement element = new TridasElement();
		
		element.setTitle(rs.getString("title"));
		TridasIdentifier id = new TridasIdentifier();
		//TODO Add domain programmatically 
		//id.setDomain("");
		id.setValue(rs.getString("elementid"));
		element.setIdentifier(id);
		element.setCreatedTimestamp(DBUtil.getDateTimeFromDB(rs,"createdtimestamp"));
		element.setLastModifiedTimestamp(DBUtil.getDateTimeFromDB(rs,"lastmodifiedtimestamp"));
		element.setComments(rs.getString("comments"));
		// TODO replace hard coded vocabulary authority with one derived from DB
		element.setType(getControlledVoc(rs.getString("elementtype"), rs.getString("elementtypeid"), "Tellervo", "en"));
		element.setDescription(rs.getString("description"));
		//TODO linkseries
		try{
			String[] files2 = rs.getString("file").split("><");
			for(String file : files2)
			{
				TridasFile tf = new TridasFile();
				tf.setHref(file);
				element.getFiles().add(tf);
			}
		} catch (Exception ex)
		{
			log.debug("Problem getting file array");
		}
		
		//TODO Set taxonomic authority properly
		element.setTaxon(getControlledVoc(rs.getString("taxonlabel"), rs.getInt("taxonid")+"", "Catalogue of Life Annual Checklist 2008", "la"));
		try{
			if(rs.getString("elementshape")!=null)
			{
				TridasShape shape = new TridasShape();
				shape.setNormalTridas(NormalTridasShape.fromValue(rs.getString("elementshape")));
				element.setShape(shape);
			}
		} catch (Exception e)
		{
			log.debug("Failed to set element shape");
		}
		TridasDimensions dims = new TridasDimensions();
		
		try{
			TridasUnit units = new TridasUnit();
			units.setNormalTridas(NormalTridasUnit.valueOf(rs.getString("units")));
			dims.setUnit(units);
			dims.setDepth(BigDecimal.valueOf(rs.getDouble("depth")));
			dims.setHeight(BigDecimal.valueOf(rs.getDouble("height")));
			dims.setWidth(BigDecimal.valueOf(rs.getDouble("width")));
			dims.setDiameter(BigDecimal.valueOf(rs.getDouble("diameter")));
		} catch (Exception e)
		{
			log.debug("Failed to set dimensions");
		}
		element.setDimensions(dims);		
		element.setAuthenticity(rs.getString("authenticity"));
		element.setLocation(getTridasLocationFromResultSet(rs, handler));
		element.setProcessing(rs.getString("processing"));
		element.setMarks(rs.getString("marks"));
		element.setAltitude(rs.getDouble("altitude"));
		TridasSlope slope = new TridasSlope();
		slope.setAngle(rs.getInt("slopeangle"));
		slope.setAzimuth(rs.getInt("slopeazimuth"));
		element.setSlope(slope);
		TridasSoil soil = new TridasSoil();
		soil.setDepth(rs.getDouble("soildepth"));
		soil.setDescription(rs.getString("soildescription"));
		element.setSoil(soil);
		TridasBedrock bedrock = new TridasBedrock();
		bedrock.setDescription(rs.getString("bedrockdescription"));
		element.setBedrock(bedrock);
		
		return element;
	}
	
	/**
	 * Turn a standard Tellervo SQL ResultSet into a TridasLocation
	 * 
	 * @param rs
	 * @param handler
	 * @return
	 * @throws SQLException
	 */
	public static TridasLocation getTridasLocationFromResultSet(ResultSet rs, RequestHandler handler) throws SQLException
	{
		TridasLocation location = new TridasLocation();
		
		location.setLocationComment(rs.getString("locationcomment"));
		
		if(rs.getInt("locationprecision")>0)
		{
			location.setLocationPrecision(rs.getInt("locationprecision")+"");
		}
		
		try{
			NormalTridasLocationType loctype = NormalTridasLocationType.fromValue(rs.getString("locationtype"));
			location.setLocationType(loctype);
		} catch (IllegalArgumentException e)
		{
			if(rs.getString("locationtype")!=null)
			{
				log.warn("Invalid location type passed by database");
			}
		}
		
		//log.debug("GML return from server: "+rs.getString("gml"));
		
		if(rs.getString("gml")!=null)
		{
			String gml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><locationGeometry xmlns=\"http://www.tridas.org/1.2.2\" "
					+ "xmlns:gml=\"http://www.opengis.net/gml\">" + rs.getString("gml") + "</locationGeometry>";

			//log.debug("GML sent to unmarshal: "+gml);
			StringReader reader = new StringReader(gml);
			try {
	
				XMLStreamReader xmler = Main.xmlInputFactory.createXMLStreamReader(reader);
	
				Unmarshaller u = Main.jaxbContext.createUnmarshaller();
	
				Object root = u.unmarshal(xmler);
	
				if (root instanceof TridasLocationGeometry) {
					TridasLocationGeometry rootElement = (TridasLocationGeometry) root;
					location.setLocationGeometry(rootElement);	
				}
			} catch (JAXBException e2) {
				handler.addMessage(TellervoRequestStatus.ERROR, 905,
						"GML returned by the server does not validate against the GML schema.");
				return null;
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		TridasAddress address = new TridasAddress();	
		address.setAddressLine1(rs.getString("locationaddressline1"));
		address.setAddressLine2(rs.getString("locationaddressline2"));
		address.setCityOrTown(rs.getString("locationcityortown"));
		address.setStateProvinceRegion(rs.getString("locationstateprovinceregion"));
		address.setPostalCode(rs.getString("locationpostalcode"));
		address.setCountry(rs.getString("locationcountry"));
		location.setAddress(address);
		
		return location;
	}
	
}
