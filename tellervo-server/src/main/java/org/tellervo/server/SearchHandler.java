package org.tellervo.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestStatus;
import org.tellervo.schema.WSIParam;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

/**
 * Class that builds and executes search SQL based upon the XML parameters section of a Tellervo request
 * 
 * @author pwb48
 *
 */
public class SearchHandler {

	private static final Logger log = LoggerFactory.getLogger(SearchHandler.class);

	private RequestHandler handler;
	private List<WSIParam> paramsArray;
	private SearchReturnObject searchReturnObject;
	
	private static HashMap<SearchParameterName, String> searchParameterTableMap;
	private static HashMap<SearchParameterName, String> searchParameterFieldMap;
	
	public SearchHandler(RequestHandler handler)
	{
		this.handler = handler;
		searchParameterTableMap = new HashMap<SearchParameterName, String>();
		searchParameterTableMap.put(SearchParameterName.OBJECTDBID, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.ANYPARENTOBJECTCODE, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.ANYPARENTOBJECTID, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.BOXCODE,  "vwtblbox");
		searchParameterTableMap.put(SearchParameterName.BOXCOMMENTS,  "vwtblbox");
		searchParameterTableMap.put(SearchParameterName.BOXCREATED,  "vwtblbox");
		searchParameterTableMap.put(SearchParameterName.BOXCURATIONLOCATION,  "vwtblbox");
		searchParameterTableMap.put(SearchParameterName.BOXID,  "vwtblbox");
		searchParameterTableMap.put(SearchParameterName.BOXLASTMODIFIED,  "vwtblbox");
		searchParameterTableMap.put(SearchParameterName.BOXTRACKINGLOCATION, "vwtblbox");
		searchParameterTableMap.put(SearchParameterName.COUNT_OF_CHILD_SERIES_OF_OBJECT, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.DEPENDENTSERIESID, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.ELEMENTAUTHENTICITY, "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTCLASSNAME, "vwtblelement"); 
		searchParameterTableMap.put(SearchParameterName.ELEMENTCODE,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTCREATED,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTDBID,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTDEPTH,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTDESCRIPTION,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTDIAMETER,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTDIMENSIONUNITS,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTDIMENSIONUNITSPOWER,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTFAMILYNAME,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTFILE,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTGENUSNAME,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTHEIGHT,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTID,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTINFRASPECIESNAME,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTINFRASPECIESTYPE,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTLASTMODIFIED,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTLOCATIONCOMMENT,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTLOCATIONPRECISION,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTLOCATIONTYPE,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTMARKS,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTORDERNAME,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTORIGINALTAXONNAME,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTPHYLUMNAME,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTPROCESSING,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTSHAPE,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTSPECIESNAME,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTTYPE,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.ELEMENTWIDTH,  "vwtblelement");
		searchParameterTableMap.put(SearchParameterName.LOANDUEDATE, "vwtblloan");
		searchParameterTableMap.put(SearchParameterName.LOANFIRSTNAME,  "vwtblloan");
		searchParameterTableMap.put(SearchParameterName.LOANID,  "vwtblloan");
		searchParameterTableMap.put(SearchParameterName.LOANISSUEDATE,  "vwtblloan");
		searchParameterTableMap.put(SearchParameterName.LOANLASTNAME,  "vwtblloan");
		searchParameterTableMap.put(SearchParameterName.LOANNOTES,  "vwtblloan");
		searchParameterTableMap.put(SearchParameterName.LOANORGANISATION,  "vwtblloan");
		searchParameterTableMap.put(SearchParameterName.LOANRETURNDATE,  "vwtblloan");
		searchParameterTableMap.put(SearchParameterName.OBJECTCODE, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTCOVERAGE_TEMPORAL_FOUNDATION, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTCOVERAGE_TEMPORAL, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTCREATED, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTCREATOR, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTDBID, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTDESCRIPTION, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTFILE, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTID, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTLASTMODIFIED, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTLOCATION, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTLOCATIONCOMMENT, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTLOCATIONPRECISION, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTLOCATIONTYPE, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTOWNER, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTTITLE, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.OBJECTTYPE, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.PARENTOBJECTID, "vwtblobject");
		searchParameterTableMap.put(SearchParameterName.RADIUSAZIMUTH, "vwtblradius"); 
		searchParameterTableMap.put(SearchParameterName.RADIUSBARK,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSCODE,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSCREATED,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSDBID,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSHEARTWOOD,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSID,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSLASTMODIFIED,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSLASTRINGUNDERBARK,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSMISSINGHEARTWOODRINGSTOPITH,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSMISSINGHEARTWOODRINGSTOPITHFOUNDATION,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSMISSINGSAPWOODRINGSTOBARK,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSMISSINGSAPWOODRINGSTOBARKFOUNDATION,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSNUMBERSAPWOODRINGS,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSPITH,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSSAPWOOD,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.RADIUSTITLE,  "vwtblradius");
		searchParameterTableMap.put(SearchParameterName.SAMPLEBOXID,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLECODE,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLECREATED,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLEDBID,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLEDESCRIPTION,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLEFILE,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLEHASKNOTS,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLEID,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLELASTMODIFIED,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLEPOSITION,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLESTATE,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLESTATUS,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLINGDATE,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SAMPLINGDATECERTAINTY,  "vwtblsample");
		searchParameterTableMap.put(SearchParameterName.SERIESANALYST, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESAUTHOR, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESCODE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESCOMMENTS, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESCOUNT, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESDATINGERRORNEGATIVE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESDATINGERRORPOSITIVE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESDATINGTYPE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESDBID, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESDEATHYEAR, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESDENDROCHRONOLOGIST, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESDERIVATIONDATE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESFIRSTYEAR, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESID, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESISRECONCILED, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESLASTMODIFIED, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESMEASURINGDATE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESMEASURINGMETHOD, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESOBJECTIVE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESOPERATORPARAMETER, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESPOWER, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESPROVENANCE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESSPROUTYEAR, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESSTANDARDIZINGMETHOD, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESTYPE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESUNIT, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESVALUECOUNT, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESVARIABLE, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.SERIESVERSION, "vwcomprehensivevm");
		searchParameterTableMap.put(SearchParameterName.TAGID, "tbltag");
		searchParameterTableMap.put(SearchParameterName.TAGTEXT, "tbltag");
		
		searchParameterFieldMap = new HashMap<SearchParameterName, String>();
		searchParameterFieldMap.put(SearchParameterName.OBJECTDBID, "objectid");
		searchParameterFieldMap.put(SearchParameterName.ANYPARENTOBJECTCODE, "anyparentobjectid");
		searchParameterFieldMap.put(SearchParameterName.ANYPARENTOBJECTID, "anyparentobjectcode");
		searchParameterFieldMap.put(SearchParameterName.BOXCODE,  "title");
		searchParameterFieldMap.put(SearchParameterName.BOXCOMMENTS,  "comments");
		searchParameterFieldMap.put(SearchParameterName.BOXCREATED,  "createdtimestamp");
		searchParameterFieldMap.put(SearchParameterName.BOXCURATIONLOCATION,  "curationlocation");
		searchParameterFieldMap.put(SearchParameterName.BOXID,  "boxid");
		searchParameterFieldMap.put(SearchParameterName.BOXLASTMODIFIED,  "lastmodifiedtimestamp");
		searchParameterFieldMap.put(SearchParameterName.BOXTRACKINGLOCATION, "trackinglocation");
		searchParameterFieldMap.put(SearchParameterName.COUNT_OF_CHILD_SERIES_OF_OBJECT, "countofchildvmeasurements");
		searchParameterFieldMap.put(SearchParameterName.DEPENDENTSERIESID, "dependentseriesid");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTAUTHENTICITY, "elementauthenticity");
		//searchParameterFieldMap.put(SearchParameterName.ELEMENTCLASSNAME, "?????"); 
		searchParameterFieldMap.put(SearchParameterName.ELEMENTCODE,  "code");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTCREATED,  "createdtimestamp");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTDBID,  "elementid");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTDEPTH,  "depth");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTDESCRIPTION,  "description");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTDIAMETER,  "diameter");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTDIMENSIONUNITS,  "units");
		//searchParameterFieldMap.put(SearchParameterName.ELEMENTDIMENSIONUNITSPOWER,  "?????");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTFAMILYNAME,  "taxonfamily");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTFILE,  "file");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTGENUSNAME,  "taxongenus");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTHEIGHT,  "height");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTID,  "elementid");
		//searchParameterFieldMap.put(SearchParameterName.ELEMENTINFRASPECIESNAME,  "?????");
		//searchParameterFieldMap.put(SearchParameterName.ELEMENTINFRASPECIESTYPE,  "?????");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTLASTMODIFIED,  "lastmodifiedtimestamp");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTLOCATIONCOMMENT,  "locationcomment");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTLOCATIONPRECISION,  "locationprecision");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTLOCATIONTYPE,  "locationtype");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTMARKS,  "marks");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTORDERNAME,  "taxonorder");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTORIGINALTAXONNAME,  "taxonlabel");
		//searchParameterFieldMap.put(SearchParameterName.ELEMENTPHYLUMNAME,  "?????");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTPROCESSING,  "processing");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTSHAPE,  "elementshape");
		//searchParameterFieldMap.put(SearchParameterName.ELEMENTSPECIESNAME,  "??????");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTTYPE,  "elementtype");
		searchParameterFieldMap.put(SearchParameterName.ELEMENTWIDTH,  "width");
		searchParameterFieldMap.put(SearchParameterName.LOANDUEDATE, "duedate");
		searchParameterFieldMap.put(SearchParameterName.LOANFIRSTNAME,  "firstname");
		searchParameterFieldMap.put(SearchParameterName.LOANID,  "loanid");
		searchParameterFieldMap.put(SearchParameterName.LOANISSUEDATE,  "issuedate");
		searchParameterFieldMap.put(SearchParameterName.LOANLASTNAME,  "lastname");
		searchParameterFieldMap.put(SearchParameterName.LOANNOTES,  "notes");
		searchParameterFieldMap.put(SearchParameterName.LOANORGANISATION,  "organisation");
		searchParameterFieldMap.put(SearchParameterName.LOANRETURNDATE,  "returndate");
		searchParameterFieldMap.put(SearchParameterName.OBJECTCODE, "code");
		searchParameterFieldMap.put(SearchParameterName.OBJECTCOVERAGE_TEMPORAL_FOUNDATION, "coveragetemporalfoundation");
		searchParameterFieldMap.put(SearchParameterName.OBJECTCOVERAGE_TEMPORAL, "coveragetemporal");
		searchParameterFieldMap.put(SearchParameterName.OBJECTCREATED, "createdtimestamp");
		searchParameterFieldMap.put(SearchParameterName.OBJECTCREATOR, "creator");
		searchParameterFieldMap.put(SearchParameterName.OBJECTDBID, "objectid");
		searchParameterFieldMap.put(SearchParameterName.OBJECTDESCRIPTION, "description");
		searchParameterFieldMap.put(SearchParameterName.OBJECTFILE, "file");
		searchParameterFieldMap.put(SearchParameterName.OBJECTID, "objectid");
		searchParameterFieldMap.put(SearchParameterName.OBJECTLASTMODIFIED, "lastmodifiedtimestamp");
		//searchParameterFieldMap.put(SearchParameterName.OBJECTLOCATION, "??????");
		searchParameterFieldMap.put(SearchParameterName.OBJECTLOCATIONCOMMENT, "locationcomment");
		searchParameterFieldMap.put(SearchParameterName.OBJECTLOCATIONPRECISION, "locationprecision");
		searchParameterFieldMap.put(SearchParameterName.OBJECTLOCATIONTYPE, "locationtype");
		searchParameterFieldMap.put(SearchParameterName.OBJECTOWNER, "owner");
		searchParameterFieldMap.put(SearchParameterName.OBJECTTITLE, "title");
		searchParameterFieldMap.put(SearchParameterName.OBJECTTYPE, "type");
		searchParameterFieldMap.put(SearchParameterName.PARENTOBJECTID, "parentobjectid");
		searchParameterFieldMap.put(SearchParameterName.RADIUSAZIMUTH, "azimuth"); 
		searchParameterFieldMap.put(SearchParameterName.RADIUSBARK,  "barkpresent");
		searchParameterFieldMap.put(SearchParameterName.RADIUSCODE,  "radiuscode");
		searchParameterFieldMap.put(SearchParameterName.RADIUSCREATED,  "createdtimestamp");
		searchParameterFieldMap.put(SearchParameterName.RADIUSDBID,  "radiusid");
		searchParameterFieldMap.put(SearchParameterName.RADIUSHEARTWOOD,  "heartwood");
		searchParameterFieldMap.put(SearchParameterName.RADIUSID,  "radiusid");
		searchParameterFieldMap.put(SearchParameterName.RADIUSLASTMODIFIED,  "lastmodifiedtimestamp");
		searchParameterFieldMap.put(SearchParameterName.RADIUSLASTRINGUNDERBARK,  "lastringunderbark");
		searchParameterFieldMap.put(SearchParameterName.RADIUSMISSINGHEARTWOODRINGSTOPITH,  "missingheartwoodringstopith");
		searchParameterFieldMap.put(SearchParameterName.RADIUSMISSINGHEARTWOODRINGSTOPITHFOUNDATION,  "missingheartwoodringstopithfoundation");
		searchParameterFieldMap.put(SearchParameterName.RADIUSMISSINGSAPWOODRINGSTOBARK,  "missingsapwoodringstobark");
		searchParameterFieldMap.put(SearchParameterName.RADIUSMISSINGSAPWOODRINGSTOBARKFOUNDATION,  "missingsapwoodringstobarkfoundation");
		searchParameterFieldMap.put(SearchParameterName.RADIUSNUMBERSAPWOODRINGS,  "numberofsapwoodrings");
		searchParameterFieldMap.put(SearchParameterName.RADIUSPITH,  "pith");
		searchParameterFieldMap.put(SearchParameterName.RADIUSSAPWOOD,  "sapwood");
		searchParameterFieldMap.put(SearchParameterName.RADIUSTITLE,  "radiuscode");
		searchParameterFieldMap.put(SearchParameterName.SAMPLEBOXID,  "boxid");
		searchParameterFieldMap.put(SearchParameterName.SAMPLECODE,  "code");
		searchParameterFieldMap.put(SearchParameterName.SAMPLECREATED,  "createdtimestamp");
		searchParameterFieldMap.put(SearchParameterName.SAMPLEDBID,  "sampleid");
		searchParameterFieldMap.put(SearchParameterName.SAMPLEDESCRIPTION,  "description");
		searchParameterFieldMap.put(SearchParameterName.SAMPLEFILE,  "file");
		searchParameterFieldMap.put(SearchParameterName.SAMPLEHASKNOTS,  "knots");
		searchParameterFieldMap.put(SearchParameterName.SAMPLEID,  "sampleid");
		searchParameterFieldMap.put(SearchParameterName.SAMPLELASTMODIFIED,  "lastmodifiedtimestamp");
		searchParameterFieldMap.put(SearchParameterName.SAMPLEPOSITION,  "position");
		searchParameterFieldMap.put(SearchParameterName.SAMPLESTATE,  "state");
		searchParameterFieldMap.put(SearchParameterName.SAMPLESTATUS,  "curationstatus");
		searchParameterFieldMap.put(SearchParameterName.SAMPLINGDATE,  "samplingdate");
		searchParameterFieldMap.put(SearchParameterName.SAMPLINGDATECERTAINTY,  "dateuncertainty");
		searchParameterFieldMap.put(SearchParameterName.SERIESANALYST, "measuredby");
		//searchParameterFieldMap.put(SearchParameterName.SERIESAUTHOR, "??????");
		searchParameterFieldMap.put(SearchParameterName.SERIESCODE, "code");
		searchParameterFieldMap.put(SearchParameterName.SERIESCOMMENTS, "comments");
		searchParameterFieldMap.put(SearchParameterName.SERIESCOUNT, "directchildcount");
		//searchParameterFieldMap.put(SearchParameterName.SERIESDATINGERRORNEGATIVE, "????");
		//searchParameterFieldMap.put(SearchParameterName.SERIESDATINGERRORPOSITIVE, "????");
		searchParameterFieldMap.put(SearchParameterName.SERIESDATINGTYPE, "datingtype");
		searchParameterFieldMap.put(SearchParameterName.SERIESDBID, "vmeasurementid");
		//searchParameterFieldMap.put(SearchParameterName.SERIESDEATHYEAR, "????");
		//searchParameterFieldMap.put(SearchParameterName.SERIESDENDROCHRONOLOGIST, "????");
		searchParameterFieldMap.put(SearchParameterName.SERIESDERIVATIONDATE, "birthdate");
		searchParameterFieldMap.put(SearchParameterName.SERIESFIRSTYEAR, "startyear");
		searchParameterFieldMap.put(SearchParameterName.SERIESID, "vmeasurementid");
		searchParameterFieldMap.put(SearchParameterName.SERIESISRECONCILED, "isreconciled");
		searchParameterFieldMap.put(SearchParameterName.SERIESLASTMODIFIED, "lastmodifiedtimestamp");
		searchParameterFieldMap.put(SearchParameterName.SERIESMEASURINGDATE, "birthdate");
		//searchParameterFieldMap.put(SearchParameterName.SERIESMEASURINGMETHOD, "????");
		searchParameterFieldMap.put(SearchParameterName.SERIESOBJECTIVE, "objective");
		searchParameterFieldMap.put(SearchParameterName.SERIESOPERATORPARAMETER, "vmeasurementopparameter");
		//searchParameterFieldMap.put(SearchParameterName.SERIESPOWER, "????");
		searchParameterFieldMap.put(SearchParameterName.SERIESPROVENANCE, "provenance");
		//searchParameterFieldMap.put(SearchParameterName.SERIESSPROUTYEAR, "????");
		//searchParameterFieldMap.put(SearchParameterName.SERIESSTANDARDIZINGMETHOD, "????");
		searchParameterFieldMap.put(SearchParameterName.SERIESTYPE, "opname");
		//searchParameterFieldMap.put(SearchParameterName.SERIESUNIT, "????");
		searchParameterFieldMap.put(SearchParameterName.SERIESVALUECOUNT, "readingcount");
		//searchParameterFieldMap.put(SearchParameterName.SERIESVARIABLE, "????");
		searchParameterFieldMap.put(SearchParameterName.SERIESVERSION, "version");
		searchParameterFieldMap.put(SearchParameterName.TAGID, "tagid");
		searchParameterFieldMap.put(SearchParameterName.TAGTEXT, "tag");
		
		// Check that we're handling all the search parameters defined in the XML schema
		for(SearchParameterName spn : SearchParameterName.values())
		{
			if(!searchParameterTableMap.containsKey(spn))
			{
				log.warn("Search parameter "+spn.toString()+ " does not return a table name");
			}
		}
		
		// Check that we're handling all the search parameters defined in the XML schema
		for(SearchParameterName spn : SearchParameterName.values())
		{
			if(!searchParameterFieldMap.containsKey(spn))
			{
				log.warn("Not handling search parameter "+spn.toString());
			}
		}
	}

	/**
	 * Compile the SQL statement from it's portions then execute  
	 */
	public void doSearch() {
		
		
		if(!handler.getRequest().isSetSearchParams() || handler.getRequest().getSearchParams().getParams().size()==0)
		{
			handler.addMessage(TellervoRequestStatus.ERROR, 901,
					"Search requests must include parameters");
			return;
		}
		
		paramsArray = handler.getRequest().getSearchParams().getParams();
		searchReturnObject = handler.getRequest().getSearchParams().getReturnObject();

		String sql = getSelectSQL() + " \n"+ getFromSQL() + getWhereSQL() + getOrderBySQL() + "\n"+ getPagingSQL(); 
		
		log.debug("Search SQL: \n********\n\n"+sql+"\n********\n");
		
		
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		
		ArrayList<Object> returnEntities = new ArrayList<Object>();
		
		// Standard, Minimal and Summary formats all return a single simple object

		try {
			con = Main.getDatabaseConnection();
			con.setAutoCommit(false);
			
			st = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery();
			
			int rowCount =0;
			if(rs.last())
			{
				rowCount = rs.getRow();
				rs.beforeFirst();
				while (rs.next()) 
				{
				
						handler.timeKeeper.log("Getting object row from database ");

						//TODO Handle output formats
						
						if(searchReturnObject.equals(SearchReturnObject.OBJECT))
						{
							returnEntities.add(SQLMarshaller.getTridasObject(rs));
						}
						else if(searchReturnObject.equals(SearchReturnObject.ELEMENT))
						{
							returnEntities.add(SQLMarshaller.getTridasElement(rs));
						}
						else if(searchReturnObject.equals(SearchReturnObject.SAMPLE))
						{
							returnEntities.add(SQLMarshaller.getTridasSample(rs));
						}
						else if(searchReturnObject.equals(SearchReturnObject.RADIUS))
						{

						}
						//TODO finish implementing different classes
				}
			}
			
			if(rowCount==0)
			{
				handler.addMessage(TellervoRequestStatus.ERROR, 903,
						"There are no matches for the specified identifier in the database");
				return;
			}
			else
			{
				handler.getContent().getSqlsAndObjectsAndElements().addAll(returnEntities);
				handler.timeKeeper.log("Marshalled object into XML");
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
	
	/**
	 * Get the SELECT portion of the SQL statement
	 * 
	 * @return
	 */
	private String getSelectSQL()
	{
		return "SELECT DISTINCT ON (" + this.getDBTableNameForSearchReturnObject(searchReturnObject)+"."+this.getDBPKeyNameForSearchReturnObject(searchReturnObject)+") "+this.getDBTableNameForSearchReturnObject(searchReturnObject)+".*";
	}
	
	
	/**
	 * Get the FROM portion of the SQL statement based on the parameters specified
	 * 
	 * @return
	 */
	private String getFromSQL()
	{
		String sql = "FROM ";
		Boolean withinJoin = false;

		for(int i=getHighestTableRankForQuery(); i>=getLowestTableRankForQuery(); i--)
		{	

			if(i==0)
			{
				// Special case for 
				if(withinJoin)
				{
					sql += "    INNER JOIN tblmeasurement ON tblmeasurement.radiusid = vwtblradius.radiusid\n";
					sql += "    INNER JOIN tblvmeasurementderivedcache dc ON dc.measurementid = tblmeasurement.measurementid\n";
					sql += "    INNER JOIN vwcomprehensivevm ON dc.vmeasurementid = vwcomprehensivevm.vmeasurementid\n";
				}
				else
				{
					sql += getTableNameFromRank(i)+"\n";
					withinJoin = true;
				}				
			}
			else if(withinJoin)
			{
				sql += "    INNER JOIN "+getTableNameFromRank(i)+ " ON "+getTableNameFromRank(i)+"."+getPKeyNameFromRank(i+1)+ " = "+getTableNameFromRank(i+1)+"."+getPKeyNameFromRank(i+1)+"\n";
			}
			else
			{
				sql += getTableNameFromRank(i)+"\n";
				withinJoin = true;
			}
			
			
			if(i==2)
			{
				//TODO handle branch to loans, curation, tags 
			}
		}
		
		
		return sql;
	}
	
	/**
	 * Get the WHERE portion of the SQL statement
	 * 
	 * @return
	 */
	private String getWhereSQL()
	{
		
		String sql = "WHERE\n";
		
		for(WSIParam param : paramsArray)
		{
			if(param.getName().equals(SearchParameterName.ANYPARENTOBJECTID))
			{
				//TODO special case
			}
			else if(param.getName().equals(SearchParameterName.ANYPARENTOBJECTCODE))
			{
				//TODO special case
			}
			else if(param.getName().equals(SearchParameterName.DEPENDENTSERIESID))
			{
				//TODO special case
			}
			else
			{
				sql += "    "+getDBTableFromSearchParameterName(param.getName())+"."+getDBFieldNameFromSearchParameterName(param.getName());
				if(param.getOperator().equals(SearchOperator.LIKE))
				{
					sql+= " ilike '%"+StringEscapeUtils.escapeSql(param.getValue())+"%'\n AND ";
				}
				else
				{
					sql+= " " + param.getOperator().value()+" '"+StringEscapeUtils.escapeSql(param.getValue())+"'\n AND ";
				}
			}
		}
		
		//TODO Force clause to hide personal tags
	
		
		// Remove last AND
		sql = sql.substring(0, sql.length()-5);		
		
		return sql;

	}
	
	
	/**
	 * Get the ORDER BY portion of the SQL statement
	 * 
	 * @return
	 */
	private String getOrderBySQL()
	{
		return "ORDER BY "+this.getDBTableNameForSearchReturnObject(searchReturnObject)+
				"."+this.getDBPKeyNameForSearchReturnObject(searchReturnObject);	
	}
	
	/**
	 * Get the OFFSET and SKIP portion of the SQL statement to enable paging through large result sets
	 * 
	 * @return
	 */
	private String getPagingSQL()
	{
		String sql = "";
		
		if(handler.getRequest().getSearchParams().isSetLimit())
		{
			sql = "LIMIT "+handler.getRequest().getSearchParams().getLimit()+" ";
		}
		
		if(handler.getRequest().getSearchParams().isSetSkip())
		{
			sql += "OFFSET "+handler.getRequest().getSearchParams().getSkip();
		}
				
		return sql;
	}
	

	/**
	 * Get the database table name that corresponds to the SearchReturnObject provided 
	 * 
	 * @param sro
	 * @return
	 */
	public String getDBTableNameForSearchReturnObject(SearchReturnObject sro)
	{
		if(sro.equals(SearchReturnObject.OBJECT))  return "vwtblobject";
		if(sro.equals(SearchReturnObject.ELEMENT)) return "vwtblelement";
		if(sro.equals(SearchReturnObject.SAMPLE))  return "vwtblsample";
		if(sro.equals(SearchReturnObject.RADIUS))  return "vwtblradius";
		if(sro.equals(SearchReturnObject.MEASUREMENT_SERIES) || sro.equals(SearchReturnObject.DERIVED_SERIES)) return "vwcomprehensivevm";
		if(sro.equals(SearchReturnObject.BOX)) return "vwtblbox";
		if(sro.equals(SearchReturnObject.LOAN)) return "vwtblloan";
		if(sro.equals(SearchReturnObject.CURATION)) return "vwtblcuration";
		if(sro.equals(SearchReturnObject.TAG)) return "vwtbltag";
		
		log.error("Unable to determine table name from SearchReturnObject provided");
		return null;
	}
	
	/**
	 * Get the database primary key field name that corresponds to the SearchReturnObject provided 
	 * 
	 * @param sro
	 * @return
	 */
	public String getDBPKeyNameForSearchReturnObject(SearchReturnObject sro)
	{
		if(sro.equals(SearchReturnObject.OBJECT))  return "objectid";
		if(sro.equals(SearchReturnObject.ELEMENT)) return "elementid";
		if(sro.equals(SearchReturnObject.SAMPLE))  return "sampleid";
		if(sro.equals(SearchReturnObject.RADIUS))  return "radiusid";
		if(sro.equals(SearchReturnObject.MEASUREMENT_SERIES) || sro.equals(SearchReturnObject.DERIVED_SERIES)) return "vmeasurementid";
		if(sro.equals(SearchReturnObject.BOX)) return "boxid";
		if(sro.equals(SearchReturnObject.LOAN)) return "loanid";
		if(sro.equals(SearchReturnObject.CURATION)) return "curationid";
		if(sro.equals(SearchReturnObject.TAG)) return "tagid";
		
		log.error("Unable to determine pkey field name from SearchReturnObject provided");
		return null;
	}
	
	/**
	 * Get the database table name for the corresponding table rank
	 * 
	 * @param rank
	 * @return
	 */
	public static String getTableNameFromRank(Integer rank)
	{
		if(rank==null || rank<0 || rank >5) return null;
		
		switch(rank)
		{
			case 0 : return "vwcomprehensivevm";
			case 1 : return "vwtblradius";
			case 2 : return "vwtblsample";
			case 3 : return "vwtblelement";
			case 4 : return "vwtblobject";
			case 5 : return "vwtblproject";			
		}
		
		return null;	
	}
	
	public static String getPKeyNameFromRank(Integer rank)
	{
		if(rank==null || rank<0 || rank >5) return null;
		
		switch(rank)
		{
			case 0 : return "vmeasurementid";
			case 1 : return "radiusid";
			case 2 : return "sampleid";
			case 3 : return "elementid";
			case 4 : return "objectid";
			case 5 : return "projectid";			
		}
		
		return null;	
	}
	
	/**
	 * Get an integer value representing the lowest rank in the TRiDaS hierarchy required for this query 
	 * Value will range from 5 (project) down to 0 (series).
	 * 
	 * @return
	 */
	private Integer getLowestTableRankForQuery()
	{
		Integer lowestRank = 999;
		for(WSIParam param : paramsArray)
		{
			if(getTableRankFromSearchParameterName(param.getName()).compareTo(lowestRank)<0)
			{
				lowestRank = getTableRankFromSearchParameterName(param.getName());
			}
		}
		
		if(getTableRankFromSearchReturnObject(searchReturnObject).compareTo(lowestRank)<0)
		{
			lowestRank = getTableRankFromSearchReturnObject(searchReturnObject);
		}
		
		if(lowestRank.equals(999))
		{
			return null;
		}
		else
		{
			return lowestRank;
		}
	}
	
	/**
	 * Get an integer value representing the highest rank in the TRiDaS hierarchy required for this query.
	 * Value will range from 5 (project) down to 0 (series).
	 * 
	 * @return
	 */
	private Integer getHighestTableRankForQuery()
	{
		Integer highestRank = -999;
		for(WSIParam param : paramsArray)
		{
			if(getTableRankFromSearchParameterName(param.getName()).compareTo(highestRank)>0)
			{
				highestRank = getTableRankFromSearchParameterName(param.getName());
			}
		}
		
		if(getTableRankFromSearchReturnObject(searchReturnObject).compareTo(highestRank)>0)
		{
			highestRank = getTableRankFromSearchReturnObject(searchReturnObject);
		}
		
		if(highestRank.equals(-999))
		{
			return null;
		}
		else
		{
			return highestRank;
		}
	}
	
	private static Integer getTableRankFromSearchReturnObject(SearchReturnObject sro)
	{
		if(sro.equals(SearchReturnObject.OBJECT))  return 4;
		if(sro.equals(SearchReturnObject.ELEMENT)) return 3;
		if(sro.equals(SearchReturnObject.SAMPLE))  return 2;
		if(sro.equals(SearchReturnObject.BOX)) return 2;
		if(sro.equals(SearchReturnObject.LOAN)) return 2;
		if(sro.equals(SearchReturnObject.CURATION)) return 2;
		if(sro.equals(SearchReturnObject.TAG)) return 2;
		if(sro.equals(SearchReturnObject.RADIUS))  return 1;
		if(sro.equals(SearchReturnObject.MEASUREMENT_SERIES) || sro.equals(SearchReturnObject.DERIVED_SERIES)) return 0;

		log.error("Failed to get table rank from search return object");
		return null;
	}
	
	private static Integer getTableRankFromSearchParameterName(SearchParameterName spn)
	{
		String tableName = getDBTableFromSearchParameterName(spn);
		
		if(tableName==null) 
		{
			log.error("Failed to get table rank because parameter is not supported");
			return null;
		}
		
		if(tableName.equals("vwtblproject")) return 5;
		if(tableName.equals("vwtblobject"))	return 4;
		if(tableName.equals("vwtblelement"))	return 3;
		if(tableName.equals("vwtblsample"))	return 2;
		if(tableName.equals("vwtblbox"))	return 2;
		if(tableName.equals("vwtblloan"))	return 2;
		if(tableName.equals("vwtblcuration"))	return 2;
		if(tableName.equals("vwtblradius"))	return 1;
		if(tableName.equals("vwcomprehensivevm"))	return 0;
		if(tableName.equals("element"))	return 2;

		log.error("Failed to get table rank");
		return null;
	}
	
	
	/**
	 * Get the database table name from the search parameter name
	 * 
	 * @param spn
	 * @return
	 */
	public static String getDBTableFromSearchParameterName(SearchParameterName spn)
	{
		if(spn==null) return null;
		
		if (searchParameterTableMap.containsKey(spn))
		{
			return searchParameterTableMap.get(spn);
			
		}
		
		log.warn("Not found table name for search parameter "+spn);

		return null;
	}
	
	/**
	 * Get the database field name from the search parameter name
	 * 
	 * @param spn
	 * @return
	 */
	public static String getDBFieldNameFromSearchParameterName(SearchParameterName spn)
	{
		if(spn==null) return null;

		
		if (searchParameterFieldMap.containsKey(spn))
		{
			return searchParameterFieldMap.get(spn);
		}
		
		log.warn("Not found parameter name for search parameter "+spn);

		return null;
	}
	
}
