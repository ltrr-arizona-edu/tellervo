package org.tellervo.server;

import java.util.HashMap;
import java.util.List;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestStatus;
import org.tellervo.schema.WSIParam;

public class SearchHandler {

	private static final Logger log = LoggerFactory.getLogger(SearchHandler.class);

	private RequestHandler handler;
	
	private String orderBySQL;
	private String groupBySQL;
	private String filterBySQL;
	private String skipSQL;
	private String limitSQL;
	private String returnObjectSQL;
	private List<WSIParam> paramsArray;
	private SearchReturnObject searchReturnObject;
	
	private HashMap<SearchParameterName, String> searchParameterTableMap;
	private HashMap<SearchParameterName, String> searchParameterFieldMap;
	
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
	 * Perform the search specified in the search parameters included in the XML request 
	 * 
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

		
		handler.addMessage(TellervoRequestStatus.ERROR,667, "Whatever you've requested hasn't been implemented yet!");

	}
	
	private String getReturnObjectSQL()
	{
		return "DISTINCT ON (" + this.getDBTableNameForSearchReturnObject(searchReturnObject)+"."+searchReturnObject.toString()+"id) ";
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
		
		
		
		return null;
	}
	
	
	
	
	/**
	 * Get the database table name that corresponds to the SearchReturnObject provided 
	 * 
	 * @param sro
	 * @return
	 */
	public String getDBTableNameForSearchReturnObject(SearchReturnObject sro)
	{
		if(sro.equals(SearchReturnObject.OBJECT))
		{
			return "vwtblobject";
		}
		else if(sro.equals(SearchReturnObject.ELEMENT))
		{
			return "vwtblelement";
		}
		else if(sro.equals(SearchReturnObject.SAMPLE))
		{
			return "vwtblsample";
		}
		else if(sro.equals(SearchReturnObject.RADIUS))
		{
			return "vwtblradius";
		}
		else if(sro.equals(SearchReturnObject.MEASUREMENT_SERIES) || sro.equals(SearchReturnObject.DERIVED_SERIES))
		{
			return "vwtblvmeasurement";
		}
		else if(sro.equals(SearchReturnObject.BOX))
		{
			return "vwtblbox";
		}
		else if(sro.equals(SearchReturnObject.LOAN))
		{
			return "vwtblloan";
		}
		else if(sro.equals(SearchReturnObject.CURATION))
		{
			return "vwtblcuration";
		}
		else if(sro.equals(SearchReturnObject.TAG))
		{
			return "vwtbltag";
		}
		else
		{
			Log.error("Unable to determine table name from SearchReturnObject provided");
		}
		return null;
	}
	
	
	
	
	public static String getDBTableFromSearchParameterName(SearchParameterName spn)
	{
		HashMap<SearchParameterName, String> tableNameMap = new HashMap<SearchParameterName, String>();
		tableNameMap.put(SearchParameterName.OBJECTDBID, "vwtblobject");
		tableNameMap.put(SearchParameterName.ANYPARENTOBJECTCODE, "vwtblobject");
		tableNameMap.put(SearchParameterName.ANYPARENTOBJECTID, "vwtblobject");
		tableNameMap.put(SearchParameterName.BOXCODE,  "vwtblbox");
		tableNameMap.put(SearchParameterName.BOXCOMMENTS,  "vwtblbox");
		tableNameMap.put(SearchParameterName.BOXCREATED,  "vwtblbox");
		tableNameMap.put(SearchParameterName.BOXCURATIONLOCATION,  "vwtblbox");
		tableNameMap.put(SearchParameterName.BOXID,  "vwtblbox");
		tableNameMap.put(SearchParameterName.BOXLASTMODIFIED,  "vwtblbox");
		tableNameMap.put(SearchParameterName.BOXTRACKINGLOCATION, "vwtblbox");
		tableNameMap.put(SearchParameterName.COUNT_OF_CHILD_SERIES_OF_OBJECT, "vwtblobject");
		tableNameMap.put(SearchParameterName.DEPENDENTSERIESID, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.ELEMENTAUTHENTICITY, "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTCLASSNAME, "vwtblelement"); 
		tableNameMap.put(SearchParameterName.ELEMENTCODE,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTCREATED,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTDBID,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTDEPTH,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTDESCRIPTION,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTDIAMETER,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTDIMENSIONUNITS,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTDIMENSIONUNITSPOWER,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTFAMILYNAME,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTFILE,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTGENUSNAME,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTHEIGHT,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTID,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTINFRASPECIESNAME,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTINFRASPECIESTYPE,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTLASTMODIFIED,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTLOCATIONCOMMENT,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTLOCATIONPRECISION,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTLOCATIONTYPE,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTMARKS,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTORDERNAME,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTORIGINALTAXONNAME,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTPHYLUMNAME,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTPROCESSING,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTSHAPE,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTSPECIESNAME,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTTYPE,  "vwtblelement");
		tableNameMap.put(SearchParameterName.ELEMENTWIDTH,  "vwtblelement");
		tableNameMap.put(SearchParameterName.LOANDUEDATE, "vwtblloan");
		tableNameMap.put(SearchParameterName.LOANFIRSTNAME,  "vwtblloan");
		tableNameMap.put(SearchParameterName.LOANID,  "vwtblloan");
		tableNameMap.put(SearchParameterName.LOANISSUEDATE,  "vwtblloan");
		tableNameMap.put(SearchParameterName.LOANLASTNAME,  "vwtblloan");
		tableNameMap.put(SearchParameterName.LOANNOTES,  "vwtblloan");
		tableNameMap.put(SearchParameterName.LOANORGANISATION,  "vwtblloan");
		tableNameMap.put(SearchParameterName.LOANRETURNDATE,  "vwtblloan");
		tableNameMap.put(SearchParameterName.OBJECTCODE, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTCOVERAGE_TEMPORAL_FOUNDATION, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTCOVERAGE_TEMPORAL, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTCREATED, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTCREATOR, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTDBID, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTDESCRIPTION, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTFILE, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTID, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTLASTMODIFIED, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTLOCATION, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTLOCATIONCOMMENT, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTLOCATIONPRECISION, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTLOCATIONTYPE, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTOWNER, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTTITLE, "vwtblobject");
		tableNameMap.put(SearchParameterName.OBJECTTYPE, "vwtblobject");
		tableNameMap.put(SearchParameterName.PARENTOBJECTID, "vwtblobject");
		tableNameMap.put(SearchParameterName.RADIUSAZIMUTH, "vwtblradius"); 
		tableNameMap.put(SearchParameterName.RADIUSBARK,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSCODE,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSCREATED,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSDBID,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSHEARTWOOD,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSID,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSLASTMODIFIED,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSLASTRINGUNDERBARK,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSMISSINGHEARTWOODRINGSTOPITH,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSMISSINGHEARTWOODRINGSTOPITHFOUNDATION,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSMISSINGSAPWOODRINGSTOBARK,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSMISSINGSAPWOODRINGSTOBARKFOUNDATION,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSNUMBERSAPWOODRINGS,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSPITH,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSSAPWOOD,  "vwtblradius");
		tableNameMap.put(SearchParameterName.RADIUSTITLE,  "vwtblradius");
		tableNameMap.put(SearchParameterName.SAMPLEBOXID,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLECODE,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLECREATED,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLEDBID,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLEDESCRIPTION,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLEFILE,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLEHASKNOTS,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLEID,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLELASTMODIFIED,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLEPOSITION,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLESTATE,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLESTATUS,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLINGDATE,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SAMPLINGDATECERTAINTY,  "vwtblsample");
		tableNameMap.put(SearchParameterName.SERIESANALYST, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESAUTHOR, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESCODE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESCOMMENTS, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESCOUNT, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESDATINGERRORNEGATIVE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESDATINGERRORPOSITIVE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESDATINGTYPE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESDBID, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESDEATHYEAR, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESDENDROCHRONOLOGIST, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESDERIVATIONDATE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESFIRSTYEAR, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESID, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESISRECONCILED, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESLASTMODIFIED, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESMEASURINGDATE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESMEASURINGMETHOD, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESOBJECTIVE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESOPERATORPARAMETER, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESPOWER, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESPROVENANCE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESSPROUTYEAR, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESSTANDARDIZINGMETHOD, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESTYPE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESUNIT, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESVALUECOUNT, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESVARIABLE, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.SERIESVERSION, "vwcomprehensivevm");
		tableNameMap.put(SearchParameterName.TAGID, "tbltag");
		tableNameMap.put(SearchParameterName.TAGTEXT, "tbltag");

		
		
		return null;
	}
	
}
