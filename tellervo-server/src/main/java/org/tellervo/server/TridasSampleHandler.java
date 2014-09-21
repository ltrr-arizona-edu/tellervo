package org.tellervo.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestStatus;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasSample;

public class TridasSampleHandler {

	private static final Logger log = LoggerFactory.getLogger(TridasSampleHandler.class);
	RequestHandler handler;
	
	/**
	 * Standard constructor 
	 * 
	 * @param handler
	 */
	public TridasSampleHandler(RequestHandler handler)
	{
		this.handler = handler;
	}
	
	
	/**
	 * Get an ArrayList of TridasSamples but specifying the 'where' clause (without the actual where)
	 * 
	 * @param whereClause
	 * @return
	 */
	public ArrayList<TridasSample> readTridasSamplesWithSQL(String whereClause)
	{
		ArrayList<TridasSample> sampleList = new ArrayList<TridasSample>();
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = Main.getDatabaseConnection();
			con.setAutoCommit(false);
			String sql = "SELECT * FROM vwtblsample WHERE "+whereClause;
			
			log.debug("Sample SQL : "+sql);
			st = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery();
			
			int rowCount =0;
			if(rs.last())
			{
				rowCount = rs.getRow();
				rs.beforeFirst();
				while (rs.next()) 
				{
					if(handler.getRequest().getFormat().equals(TellervoRequestFormat.STANDARD) || 
							   handler.getRequest().getFormat().equals(TellervoRequestFormat.MINIMAL) || 
							   handler.getRequest().getFormat().equals(TellervoRequestFormat.SUMMARY))
					{
						sampleList.add(SQLMarshaller.getTridasSample(rs));
					}
					else if (handler.getRequest().getFormat().equals(TellervoRequestFormat.COMPREHENSIVE))
					{
						sampleList.add(SQLMarshaller.getTridasSample(rs));
						
					}
				}
				
			}
			
			if(rowCount==0)
			{
				handler.addMessage(TellervoRequestStatus.ERROR, 903,
						"No records match your request");
				handler.sendResponse();
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
		
			
		return sampleList;
	}
	
	/**
	 * Read a TRiDaS Sample from the database based on the specified ID.  The ID is passed as a string but
	 * needs to be a valid UUID to match the Tellervo database
	 * 
	 * @param id
	 */
	public void readTridasSample(String id)
	{
		ArrayList<TridasSample> samples = readTridasSamplesWithSQL("elementid='"+StringEscapeUtils.escapeSql(id)+"'::uuid");
		
		if(samples.size()==1)
		{	
			handler.getContent().getSqlsAndObjectsAndElements().add(samples.get(0));
		}
		else if (samples.size()==0)
		{
			
		}
		
		return ;
		
		
	}

	
}
