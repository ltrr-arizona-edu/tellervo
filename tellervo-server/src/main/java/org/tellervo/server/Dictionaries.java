package org.tellervo.server;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestStatus;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSIBoxDictionary;
import org.tellervo.schema.WSIConfiguration;
import org.tellervo.schema.WSIConfigurationDictionary;
import org.tellervo.schema.WSIDatingTypeDictionary;
import org.tellervo.schema.WSIElementAuthenticityDictionary;
import org.tellervo.schema.WSIElementTypeDictionary;
import org.tellervo.schema.WSIObjectTypeDictionary;
import org.tellervo.schema.WSIReadingNoteDictionary;
import org.tellervo.schema.WSISampleTypeDictionary;
import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.schema.WSISecurityGroupDictionary;
import org.tellervo.schema.WSISecurityUser;
import org.tellervo.schema.WSISecurityUserDictionary;
import org.tellervo.schema.WSITaxonDictionary;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasRemark;

public class Dictionaries {
	private static final Logger log = LoggerFactory.getLogger(Dictionaries.class);

	public static void returnDictionaries(RequestHandler handler)
	{
		handler.timeKeeper.log("Building dictionaries");

		ArrayList<String> dictItemNames = new ArrayList<String>();
		
		// Dictionary with simple controlled vocabs
		dictItemNames.add("objectType");
		dictItemNames.add("elementType");
		dictItemNames.add("sampleType");
		dictItemNames.add("elementAuthenticity");
		dictItemNames.add("datingType");
		
		// Dictionaries that need individual handling
		dictItemNames.add("taxon");
		dictItemNames.add("securityUser");
		dictItemNames.add("securityGroup");
		dictItemNames.add("readingNote");
		dictItemNames.add("box");
		dictItemNames.add("configuration");

		ArrayList<Object> dictionaryList = new ArrayList<Object>();
		

		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			con = Main.getDatabaseConnection();
			
			for(int i=0; i<dictItemNames.size(); i++)
			{
				String dictName = dictItemNames.get(i).toLowerCase();

				if( (dictName.equals("objecttype"))
						||(dictName.equals("elementtype"))
						||(dictName.equals("sampletype"))
						||(dictName.equals("elementauthenticity"))
						||(dictName.equals("datingtype")))
				{
					//
					//  DICTIONARIES WITH CONTROLLED VOCABS
					//
										
					// TODO replace hard coded vocabulary authority with one derived from DB
					//String sql = "SELECT d."+dictName+"id AS id, d."+dictName+" AS value, v.name AS vocabulary FROM tlkp"+dictName+" AS d LEFT JOIN tlkpvocabulary v ON d.vocabularyid = v.vocabularyid WHERE d."+dictName+"id>0 ORDER BY "+dictName+" ASC";
					String sql = "SELECT d."+dictName+"id AS id, d."+dictName+" AS value FROM tlkp"+dictName+" AS d WHERE d."+dictName+"id>0 ORDER BY "+dictName+" ASC";
	
					//log.debug("Dictionary SQL : "+sql);
					st = con.prepareStatement(sql);
					rs = st.executeQuery();
					
					ArrayList<ControlledVoc> cvList = new ArrayList<ControlledVoc>();
					
					while (rs.next()) {
						ControlledVoc item = new ControlledVoc();
						item.setLang("en");
						item.setNormal(rs.getString(2));
						item.setNormalId(rs.getString(1));
						// TODO replace hard coded vocabulary authority with one derived from DB
						//item.setNormalStd(rs.getString(3));
						item.setNormalStd("Tellervo");
						cvList.add(item);
					}
					
					
					if(dictName.equals("objecttype"))
					{
						WSIObjectTypeDictionary dictionary = new WSIObjectTypeDictionary();
						dictionary.getObjectTypes().addAll(cvList);
						dictionaryList.add(dictionary);
					}
					else if(dictName.equals("elementtype"))
					{
						WSIElementTypeDictionary dictionary = new WSIElementTypeDictionary();
						dictionary.getElementTypes().addAll(cvList);
						dictionaryList.add(dictionary);
					}
					else if(dictName.equals("sampletype"))
					{
						WSISampleTypeDictionary dictionary = new WSISampleTypeDictionary();
						dictionary.getSampleTypes().addAll(cvList);
						dictionaryList.add(dictionary);
					}
					else if(dictName.equals("elementauthenticity"))
					{
						WSIElementAuthenticityDictionary dictionary = new WSIElementAuthenticityDictionary();
						dictionary.getElementAuthenticities().addAll(cvList);
						dictionaryList.add(dictionary);
					}
					else if(dictName.equals("datingtype"))
					{
						WSIDatingTypeDictionary dictionary = new WSIDatingTypeDictionary();
						dictionary.getDatingTypes().addAll(cvList);
						dictionaryList.add(dictionary);
					}
				}
				
				//
				//  OTHER DICTIONARIES THAT REQUIRE INDIVIDUAL HANDLING
				//
				
				else if(dictName.equals("taxon"))
				{
					String sql = "SELECT taxonid, label FROM tlkptaxon ORDER BY label asc";
					//log.debug("Dictionary SQL : "+sql);
					st = con.prepareStatement(sql);
					rs = st.executeQuery();
					
					WSITaxonDictionary dictionary = new WSITaxonDictionary();
					
					while (rs.next()) {
						ControlledVoc item = new ControlledVoc();
						item.setNormal(rs.getString(2));
						item.setNormalId(rs.getString(1));
						// TODO replace hard coded vocabulary authority with one derived from DB
						//item.setNormalStd(rs.getString(3));
						item.setNormalStd("Catalogue of Life Annual Checklist 2008");
						dictionary.getTaxons().add(item);
					}
					dictionaryList.add(dictionary);
					
				}				
				else if(dictName.equals("securityuser"))
				{
					String sql = "SELECT securityuserid, username, firstname, lastname, isactive FROM tblsecurityuser ORDER BY username ASC";
					//log.debug("Dictionary SQL : "+sql);
					st = con.prepareStatement(sql);
					rs = st.executeQuery();
					
					WSISecurityUserDictionary dictionary = new WSISecurityUserDictionary();
					
					while (rs.next()) {
						WSISecurityUser user = new WSISecurityUser();
						user.setUsername(rs.getString(2));
						user.setFirstName(rs.getString(3));
						user.setLastName(rs.getString(4));
						user.setIsActive(rs.getBoolean(5));
						dictionary.getSecurityUsers().add(user);
					}
					dictionaryList.add(dictionary);
					
				}
				else if(dictName.equals("securitygroup"))
				{
					
					
					String sql = "SELECT gg.securitygroupid, g.name, g.description, g.isactive, array_agg(gg.securityuserid::uuid) AS arr "
								 +"FROM tblsecuritygroup AS g LEFT JOIN (SELECT securitygroupid, securityuserid FROM tblsecurityusermembership "
								 +"GROUP BY securitygroupid, securityuserid) gg ON g.securitygroupid = gg.securitygroupid "
								 +"GROUP BY gg.securitygroupid, g.name, g.description, g.isactive";
					//log.debug("Dictionary SQL : "+sql);
					st = con.prepareStatement(sql);
					rs = st.executeQuery();
					
					WSISecurityGroupDictionary dictionary = new WSISecurityGroupDictionary();
					
					while (rs.next()) {
						WSISecurityGroup grp = new WSISecurityGroup();
						grp.setId(rs.getString(1));
						grp.setName(rs.getString(2));
						grp.setDescription(rs.getString(3));
						grp.setIsActive(rs.getBoolean(4));
				
						Array members = rs.getArray(5);
						UUID[] members2 = (UUID[])members.getArray();
						ArrayList<String> members3 = new ArrayList<String>();
						for(UUID uuid : members2)
						{
							if(uuid!=null) members3.add(uuid.toString());
						}
						grp.setUserMembers(members3);
				
						
						dictionary.getSecurityGroups().add(grp);
					}
					dictionaryList.add(dictionary);
					
				}
				
				else if(dictName.equals("readingnote"))
				{
					String sql = "SELECT r.standardisedid AS id, r.note, "
								+"v.name AS vocabulary "
								+"FROM tlkpreadingnote AS r LEFT JOIN "
								+"tlkpvocabulary v ON v.vocabularyid=r.vocabularyid";
					
					//log.debug("Dictionary SQL : "+sql);
					st = con.prepareStatement(sql);
					rs = st.executeQuery();
					
					WSIReadingNoteDictionary dictionary = new WSIReadingNoteDictionary();
					
					while (rs.next()) {
						TridasRemark remark = new TridasRemark();
						
						if(rs.getString(3).equals("TRiDaS"))
						{
							remark.setNormalTridas(NormalTridasRemark.fromValue(rs.getString(2)));
						}
						else
						{
							remark.setNormal(rs.getString(2));
							remark.setNormalId(rs.getString(1));
							remark.setNormalStd(rs.getString(3));
						}
						
						dictionary.getRemarks().add(remark);
					}
					dictionaryList.add(dictionary);
					
				}
				else if(dictName.equals("box"))
				{
					String sql = "SELECT * FROM tblbox";
					//log.debug("Dictionary SQL : "+sql);
					st = con.prepareStatement(sql);
					rs = st.executeQuery();
					
					WSIBoxDictionary dictionary = new WSIBoxDictionary();
					
					while (rs.next()) {
						WSIBox box = new WSIBox();
						
						box.setTitle(rs.getString("title"));
						TridasIdentifier id = new TridasIdentifier();
						//TODO Add domain programmatically 
						//id.setDomain("");
						id.setValue(rs.getString("boxid"));
						box.setIdentifier(id);
						box.setCreatedTimestamp(DBUtil.getDateTimeFromDB(rs,"createdtimestamp"));
						box.setLastModifiedTimestamp(DBUtil.getDateTimeFromDB(rs, "lastmodifiedtimestamp"));
						box.setComments(rs.getString("comments"));
						dictionary.getBoxes().add(box);
					}
					dictionaryList.add(dictionary);
					
				}
				
				else if(dictName.equals("configuration"))
				{
					
					WSIConfigurationDictionary dictionary = new WSIConfigurationDictionary();
					
					WSIConfiguration labname = new WSIConfiguration();
					labname.setKey("lab.name");
					labname.setValue(Main.labName);
					dictionary.getConfigurations().add(labname);
					
					WSIConfiguration labacronym = new WSIConfiguration();
					labacronym.setKey("lab.acronym");
					labacronym.setValue(Main.labAcronym);
					dictionary.getConfigurations().add(labacronym);
					
					dictionaryList.add(dictionary);
					
				}
				handler.timeKeeper.log("Built "+dictName+" dictionary");


			}
			
			handler.getContent().setSqlsAndObjectsAndElements(dictionaryList);
			handler.sendResponse();


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
}
