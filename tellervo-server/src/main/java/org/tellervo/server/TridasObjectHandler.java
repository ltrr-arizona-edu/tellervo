package org.tellervo.server;

import java.io.StringReader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.opengis.gml.PointMember;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestStatus;
import org.tellervo.schema.WSIRootElement;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasCoverage;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

public class TridasObjectHandler {

	private static final Logger log = LoggerFactory.getLogger(TridasObjectHandler.class);
	RequestHandler handler;
	
	public TridasObjectHandler(RequestHandler handler)
	{
		this.handler = handler;
	}
	
	public void readTridasObject(String id)
	{
		if(id==null){
			handler.addMessage(TellervoRequestStatus.ERROR, 902,
					"An identifier is required when reading an entity");
			handler.sendResponse();
			return;
		}
				
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = Main.getDatabaseConnection();
			con.setAutoCommit(false);
			String sql = "SELECT * FROM vwtblobject WHERE objectid= ?::uuid ";
			
			st = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			st.setString(1, id);
			rs = st.executeQuery();
			
			int rowCount =0;
			if(rs.last())
			{
				rowCount = rs.getRow();
				rs.beforeFirst();
				while (rs.next()) {
					handler.getContent().getSqlsAndObjectsAndElements().add(getTridasObjectFromResultSet(rs, handler));
				}
			}
			
			if(rowCount==0)
			{
				handler.addMessage(TellervoRequestStatus.ERROR, 903,
						"There are no matches for the specified identifier in the database");
				handler.sendResponse();
				return;
			}

		} catch (SQLException ex) {
			log.error(ex.getMessage());
			handler.addMessage(TellervoRequestStatus.ERROR, 701,
					"Error connecting to database");

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				log.error(ex.getMessage());
				handler.addMessage(TellervoRequestStatus.ERROR, 701,
						"Error connecting to database");
			}
		}
		
	}
	
	private static ControlledVoc getControlledVoc(String normal, String normalId, String normalStd)
	{
		ControlledVoc cv = new ControlledVoc();
		cv.setLang("en");
		cv.setNormal(normal);
		cv.setNormalId(normalId);
		cv.setNormalStd(normalStd);
		return cv;
	}
	
	private static TridasObjectEx getTridasObjectFromResultSet(ResultSet rs, RequestHandler handler) throws SQLException
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
		object.setType(getControlledVoc(rs.getString("objecttype"), rs.getString("objecttypeid"), "Tellervo"));
		object.setDescription(rs.getString("description"));
		//TODO linkseries
		if(rs.getArray("file")!=null)
		{
			Array filearr = rs.getArray("file");
			try{
				String[] files2 = (String[])filearr.getArray();
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
		}
		object.setCreator(rs.getString("creator"));
		object.setOwner(rs.getString("owner"));
		TridasCoverage coverage = new TridasCoverage();
		coverage.setCoverageTemporal(rs.getString("coveragetemporal"));
		coverage.setCoverageTemporalFoundation(rs.getString("coveragetemporalfoundation"));
		object.setCoverage(coverage);
		object.setLocation(getTridasLocationFromResultSet(rs, handler));
		
		return object;
	}
	
	public static TridasLocation getTridasLocationFromResultSet(ResultSet rs, RequestHandler handler) throws SQLException
	{
		TridasLocation location = new TridasLocation();
		
		location.setLocationComment(rs.getString("locationcomment"));
		location.setLocationPrecision(rs.getInt("locationprecision")+"");
		
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

			log.debug("GML sent to unmarshal: "+gml);
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
