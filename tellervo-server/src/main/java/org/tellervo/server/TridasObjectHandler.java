package org.tellervo.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestStatus;
import org.tridas.interfaces.ITridasGeneric;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

/**
 * Class for handler requests to Create, Read, Update and Delete TRiDaS Objects
 * 
 * @author pbrewer
 *
 */
public class TridasObjectHandler {

	private static final Logger log = LoggerFactory.getLogger(TridasObjectHandler.class);
	RequestHandler handler;
	
	/**
	 * Standard constructor 
	 * 
	 * @param handler
	 */
	public TridasObjectHandler(RequestHandler handler)
	{
		this.handler = handler;
	}
	
	/**
	 * Read a TRiDaS Object from the database based on the specified ID.  The ID is passed as a string but
	 * needs to be a valid UUID to match the Tellervo database
	 * 
	 * @param id
	 */
	public void readTridasObject(String id)
	{
		if(id==null){
			handler.addMessage(TellervoRequestStatus.ERROR, 902,
					"An identifier is required when reading an entity");
			handler.sendResponse();
			return;
		}
		handler.timeKeeper.log("Reading TRiDaS object from DB");

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		
		if(handler.getRequest().getFormat().equals(TellervoRequestFormat.STANDARD) || 
				   handler.getRequest().getFormat().equals(TellervoRequestFormat.MINIMAL) || 
				   handler.getRequest().getFormat().equals(TellervoRequestFormat.SUMMARY))
		{
			// Standard, Minimal and Summary formats all return a single simple object

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
					while (rs.next()) 
					{
					
							handler.timeKeeper.log("Getting object row from database ");
	
							// Standard, Minimal and Summary formats all return a simple single object
							handler.getContent().getSqlsAndObjectsAndElements().add(SQLMarshaller.getTridasObject(rs));
							handler.timeKeeper.log("Marshalled object into XML");
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
						ex.getMessage());
	
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
		else if (handler.getRequest().getFormat().equals(TellervoRequestFormat.COMPREHENSIVE))
		{
			//Comprehensive format returns the full object hierarchy
			try {
				con = Main.getDatabaseConnection();
				con.setAutoCommit(false);

				String sql = "SELECT * FROM vwtblobject WHERE objectid IN (SELECT objectid FROM cpgdb.findobjectancestors(?::uuid, true)) ";
				
				st = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				st.setString(1, id);
				rs = st.executeQuery();
				
				int rowCount =0;
				if(rs.last())
				{
					rowCount = rs.getRow();
					rs.beforeFirst();
					TridasObject object = null;
					while (rs.next()) 
					{
					
							handler.timeKeeper.log("Getting object row from database ");
							if(object==null)
							{
								object = SQLMarshaller.getTridasObject(rs);
							}
							else
							{
								TridasObject childObject = (TridasObject) object.clone();
								object = SQLMarshaller.getTridasObject(rs);
								ArrayList<TridasObject> objects = new ArrayList<TridasObject>();
								objects.add(childObject);
								object.setObjects(objects);
							}
					}
					// Standard, Minimal and Summary formats all return a simple single object
					handler.getContent().getSqlsAndObjectsAndElements().add(object);
					handler.timeKeeper.log("Marshalled object into XML");
					
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
						ex.getMessage());
	
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
		
	}
	
	public void writeTridasObject(TridasObject object)
	{
		if(object==null) return;
		
		// TODO check for mandatory fields
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			con = Main.getDatabaseConnection();
			con.setAutoCommit(false);

			String sql = "INSERT INTO tblobject ( "		
					+ "title, "	//1
					+ "code, "  //2
					+ "objecttypeid ";//3

					/*+ "locationgeometry, " //4
					+ "locationtypeid, " //5
					+ "locationprecision, " //6
					+ "locationcomment, "//7
					+ "file, "//8
					+ "creator, "//9
					+ "owner, "//10
					+ "parentobjectid "//11
					+ "description "//12
					+ "comments, "//13
					+ "coveragetemporal, "//14
					+ "coveragetemporalfoundation, "//15
					+ "locationaddressline1, "//16
					+ "locationaddressline2, "//17
					+ "locationcityortown, "//18
					+ "locationstateprovinceregion, "//19
					+ "locationpostalcode, "//20
					+ "locationcountry, "//21
					+ "vegetationtype ";//22
*/
					
					//+ "createdtimestamp, "
					//+ "lastmodifiedtimestamp, "
					//+ "domainid, "
					//+ "projectid"
					
			if(object.isSetIdentifier() && object.getIdentifier().isSetValue()) sql += ", objectid, ";//23

			//sql += ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
			sql += ") VALUES (?, ?, ?";

			
			if(object.isSetIdentifier() && object.getIdentifier().isSetValue()) sql += ", ? ";
			sql+= ") RETURNING objectid";

			
			
			st = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			st.setString(1, object.getTitle());
			st.setString(2, TridasUtils.getGenericFieldByName((ITridasGeneric) object, "tellervo.objectCode").getValue());
			st.setInt(3, Integer.valueOf(object.getType().getNormalId()));
			
			
			rs = st.executeQuery();
			
			int rowCount =0;
			if(rs.last())
			{
				rowCount = rs.getRow();
				rs.beforeFirst();
				while (rs.next()) 
				{
				
						handler.timeKeeper.log("Getting object row from database ");

						// Standard, Minimal and Summary formats all return a simple single object
						readTridasObject(rs.getString("objectid"));
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
					ex.getMessage());

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
	
	public void writeTridasObjects(Collection<TridasObject> objects)
	{
		for(TridasObject o : objects)
		{
			writeTridasObject(o);
		}
	}

	
}
