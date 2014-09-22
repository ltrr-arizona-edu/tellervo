package org.tellervo.server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.schema.ComplexPresenceAbsence;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasBark;
import org.tridas.schema.TridasBedrock;
import org.tridas.schema.TridasCoverage;
import org.tridas.schema.TridasDimensions;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasHeartwood;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasPith;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasSapwood;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasSlope;
import org.tridas.schema.TridasSoil;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasWoodCompleteness;
import org.tridas.util.TridasObjectEx;

/**
 * Class for turning SQL ResultSets into TRiDaS Java objects
 * 
 * @author pbrewer
 *
 */
public class SQLMarshaller {

	private static final Logger log = LoggerFactory.getLogger(SQLMarshaller.class);


	
	/**
	 * Turn a standard Tellervo SQL ResultSet into a TridasObjectEx
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static TridasObjectEx getTridasObject(ResultSet rs) throws SQLException
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
		object.setType(DBUtil.getControlledVoc(rs.getString("objecttype"), rs.getString("objecttypeid"), "Tellervo", "en"));
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
		object.setLocation(DBUtil.getTridasLocationFromResultSet(rs));
		
		
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
		
		if(DBUtil.getInteger(rs, "countofchildvmeasurements")!=null)
		{
			TridasGenericField countOfSeries = new TridasGenericField();
			countOfSeries.setName("tellervo.countOfChildSeries");
			countOfSeries.setType("xs:int");
			countOfSeries.setValue(String.valueOf(rs.getInt("countofchildvmeasurements")));
			object.getGenericFields().add(countOfSeries);
		}
		
		return object;
	}
	
	
	/**
	 * Turn a standard Tellervo SQL ResultSet into a TridasElement
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static TridasElement getTridasElement(ResultSet rs) throws SQLException
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
		element.setType(DBUtil.getControlledVoc(rs.getString("elementtype"), rs.getString("elementtypeid"), "Tellervo", "en"));
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
		element.setTaxon(DBUtil.getControlledVoc(rs.getString("taxonlabel"), rs.getInt("taxonid")+"", "Catalogue of Life Annual Checklist 2008", "la"));
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
			dims.setDepth(DBUtil.getBigDecimal(rs, "depth"));
			dims.setHeight(DBUtil.getBigDecimal(rs, "height"));
			dims.setWidth(DBUtil.getBigDecimal(rs, "width"));
			dims.setDiameter(DBUtil.getBigDecimal(rs, "diameter"));
		} catch (Exception e)
		{
			log.debug("Failed to set dimensions");
		}
		element.setDimensions(dims);		
		element.setAuthenticity(rs.getString("authenticity"));
		element.setLocation(DBUtil.getTridasLocationFromResultSet(rs));
		element.setProcessing(rs.getString("processing"));
		element.setMarks(rs.getString("marks"));
		element.setAltitude(DBUtil.getDouble(rs, "altitude"));
		TridasSlope slope = new TridasSlope();
		slope.setAngle(DBUtil.getInteger(rs, "slopeangle"));
		slope.setAzimuth(DBUtil.getInteger(rs, "slopeazimuth"));
		element.setSlope(slope);
		TridasSoil soil = new TridasSoil();
		soil.setDepth(DBUtil.getDouble(rs, "soildepth"));
		soil.setDescription(rs.getString("soildescription"));
		element.setSoil(soil);
		TridasBedrock bedrock = new TridasBedrock();
		bedrock.setDescription(rs.getString("bedrockdescription"));
		element.setBedrock(bedrock);
		
		return element;
	}
	
	/**
	 * Turn a standard Tellervo SQL ResultSet (based on vwtblelement) into a TridasSample
     *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static TridasSample getTridasSample(ResultSet rs) throws SQLException
	{
		TridasSample sample = new TridasSample();
		
		sample.setTitle(rs.getString("title"));
		TridasIdentifier id = new TridasIdentifier();
		//TODO Add domain programmatically 
		//id.setDomain("");
		id.setValue(rs.getString("sampleid"));
		sample.setIdentifier(id);
		sample.setCreatedTimestamp(DBUtil.getDateTimeFromDB(rs,"createdtimestamp"));
		sample.setLastModifiedTimestamp(DBUtil.getDateTimeFromDB(rs,"lastmodifiedtimestamp"));
		sample.setComments(rs.getString("comments"));
		// TODO replace hard coded vocabulary authority with one derived from DB
		sample.setType(DBUtil.getControlledVoc(rs.getString("sampletype"), rs.getString("sampletypeid"), "Tellervo", "en"));
		sample.setDescription(rs.getString("description"));
		try{
			String[] files2 = rs.getString("file").split("><");
			for(String file : files2)
			{
				TridasFile tf = new TridasFile();
				tf.setHref(file);
				sample.getFiles().add(tf);
			}
		} catch (Exception ex)
		{
			log.debug("Problem getting file array");
		}
		
		sample.setSamplingDate(DBUtil.getDateFromDB(rs, "samplingdate"));
		sample.setPosition(rs.getString("position"));
		sample.setState(rs.getString("state"));
		sample.setKnots(rs.getBoolean("knots"));
		
		if(rs.getString("curationstatus")!=null)
		{
			TridasGenericField curationStatus = new TridasGenericField();
			curationStatus.setName("tellervo.curationStatus");
			curationStatus.setType("xs:string");
			curationStatus.setValue(String.valueOf(rs.getString("curationstatus")));
			sample.getGenericFields().add(curationStatus);
		}
		
		//TODO investigate what this is and if we need it
		/*if(rs.getString("samplestatus")!=null)
		{
			TridasGenericField sampleStatus = new TridasGenericField();
			sampleStatus.setName("tellervo.sampleStatus");
			sampleStatus.setType("xs:string");
			sampleStatus.setValue(String.valueOf(rs.getString("samplestatus")));
			sample.getGenericFields().add(sampleStatus);
		}*/
		
		if(rs.getString("externalid")!=null)
		{
			TridasGenericField externalID = new TridasGenericField();
			externalID.setName("tellervo.externalID");
			externalID.setType("xs:string");
			externalID.setValue(String.valueOf(rs.getString("externalid")));
			sample.getGenericFields().add(externalID);
		}
		
		return sample;
	}
	
	/**
	 * Turn a standard Tellervo SQL ResultSet (based on vwtblradius) into a TridasRadius
     *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static TridasRadius getTridasRadiusFromResultSet(ResultSet rs) throws SQLException
	{
		TridasRadius radius = new TridasRadius();
		
		radius.setTitle(rs.getString("title"));
		TridasIdentifier id = new TridasIdentifier();
		//TODO Add domain programmatically 
		//id.setDomain("");
		id.setValue(rs.getString("sampleid"));
		radius.setIdentifier(id);
		radius.setCreatedTimestamp(DBUtil.getDateTimeFromDB(rs,"createdtimestamp"));
		radius.setLastModifiedTimestamp(DBUtil.getDateTimeFromDB(rs,"lastmodifiedtimestamp"));
		radius.setComments(rs.getString("comments"));
	
		TridasWoodCompleteness wc = new TridasWoodCompleteness();
		TridasPith pith = new TridasPith();
		try{
			pith.setPresence(ComplexPresenceAbsence.fromValue(rs.getString("pith")));
		} catch (Exception e)
		{
			log.debug("Failed to set pith");
		}
		wc.setPith(pith);
		TridasHeartwood hw = new TridasHeartwood();
		try{
			hw.setPresence(ComplexPresenceAbsence.fromValue(rs.getString("heartwood")));
		} catch (Exception e)
		{
			log.debug("Failed to set heartwood presense");
		}
		if(DBUtil.getInteger(rs, "missingheartwoodringstopith")!=null)
		{
			hw.setMissingHeartwoodRingsToPith(rs.getInt("missingheartwoodringstopith"));
			hw.setMissingHeartwoodRingsToPithFoundation(rs.getString("missingheartwoodringstopithfoundation"));
		}
		wc.setHeartwood(hw);
		TridasSapwood sw = new TridasSapwood();
		try{
			sw.setPresence(ComplexPresenceAbsence.fromValue(rs.getString("sapwood")));
		} catch (Exception e)
		{
			log.debug("failed to set sapwood presense");
		}
		if(DBUtil.getInteger(rs, "missingsapwoodringstobark")!=null)
		{
			sw.setMissingSapwoodRingsToBark(rs.getInt("missingsapwoodringstobark"));
			sw.setMissingSapwoodRingsToBarkFoundation(rs.getString("missingsapwoodringstobarkfoundation"));
		}
		wc.setSapwood(sw);
		TridasBark bark = new TridasBark();
		bark.setPresence(DBUtil.getPresenceAbsence(rs, "barkpresent"));
		wc.setBark(bark);
		wc.setNrOfUnmeasuredInnerRings(DBUtil.getInteger(rs, "nrofunmeasuredinnerrings"));
		wc.setNrOfUnmeasuredOuterRings(DBUtil.getInteger(rs, "nrofunmeasuredouterrings"));
		radius.setWoodCompleteness(wc);
		
		Double az = DBUtil.getDouble(rs, "azimuth");
		if(az!=null)
		{
			radius.setAzimuth(BigDecimal.valueOf(az));
		}

		TridasGenericField externalID = new TridasGenericField();
		externalID.setName("tellervo.externalID");
		externalID.setType("xs:string");
		externalID.setValue(String.valueOf(rs.getString("externalid")));
		radius.getGenericFields().add(externalID);
		
		return radius;
	}
	

	
}
