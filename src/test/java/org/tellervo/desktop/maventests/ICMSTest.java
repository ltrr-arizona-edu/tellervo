package org.tellervo.desktop.maventests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.schema.DateTime;
import org.tridas.schema.TridasLocation;
import org.tridas.spatial.SpatialUtils;
import org.tridas.spatial.SpatialUtils.UTMDatum;

import com.rediscov.schema.NormalCondition;
import com.rediscov.schema.RediscoveryExport;
import com.rediscov.util.RediscoveryExportEx;

import edu.emory.mathcs.backport.java.util.Collections;

public class ICMSTest extends TestCase {
	private static final Logger log = LoggerFactory.getLogger(ICMSTest.class);

	private String filename = "/home/pbrewer/Documents/Lab/Curation/fullexport.xml";

	
	private static void logFailedCatalogCode(String code)
	{
		try {
			code = code+"\n";
		    Files.write(Paths.get("/tmp/failedCatalogCodes.txt"), code.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}
	
	public void testByRecord()
	{
		log.info("*************************************************************");
		log.info("Running runTestsByRecord()");
		log.info("*************************************************************");

		Double countall = 0.0;
		Double countfailure = 0.0;
		ArrayList<String> catalogcodes = new ArrayList<String>();
		ArrayList<String> ltrrcodes = new ArrayList<String>();

		
		List<RediscoveryExport> lst = RediscoveryExportEx.getICMSRecordsFromXMLFile(filename, false);

		if (lst == null) {
			fail();
		}
		
		for (RediscoveryExport record : lst) 
		{
			RediscoveryExportEx rec = (RediscoveryExportEx) record;
			countall++;
			
			// Check CatalogCode is unique
			String code = rec.getCatalogCode();
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.trim();
			if(code==null || code.length()==0)
			{
				log.error("Blank catalog code: " + rec.getCatalogCode());
				logFailedCatalogCode(rec.getCatalogCode() + "   - CatalogCode is blank");
				countfailure++;
				continue;
			}
			else if (catalogcodes.contains(code)) {
				log.error("Duplicate catalog code: " + rec.getCatalogCode());
				logFailedCatalogCode(rec.getCatalogCode()+ "   - CatalogCode is duplicate");
				countfailure++;
				continue;
			} else {
				catalogcodes.add(code);
			}
			
			// Check Origin field is valid
			if (rec.getCounty() == null || rec.getState() == null
					&& rec.getCountry() == null) {
				log.error("Invalid Origin field: '"+rec.getOrigin()+"' for " + rec.getCatalogCode());
				logFailedCatalogCode(rec.getCatalogCode()+ "   - Origin field is invalid");
				countfailure++;
				continue;
			}
			
			
			// Check LTRR code is valid
			String ltrrcode = null;
			try {
				ltrrcode = rec.getObjectCode() + "-" + rec.getElementCode();
			} catch (Exception e) {
				log.error("Invalid LTRR code: '"+rec.getOtherNumbers()+"' for " + rec.getCatalogCode());
				logFailedCatalogCode(rec.getCatalogCode()+ "   - LTRR code in OtherNumbers field is invalid");
				countfailure++;
			}

			if (ltrrcode != null && rec.getSampleCode() != null) {
				ltrrcode += "-" + rec.getSampleCode();
			}

			if (ltrrcode != null) {
				if (ltrrcodes.contains(ltrrcode)) {
					log.error("Duplicate LTRR code: '"+rec.getOtherNumbers()+"' for " + rec.getCatalogCode());
					logFailedCatalogCode(rec.getCatalogCode()+ "   - LTRR code in OtherNumbers field is a duplicate");
					countfailure++;
				} else {
					ltrrcodes.add(ltrrcode);
				}
			}
			
			// Check UTM is blank or value
			if(rec.getUTMCoords()!=null && rec.getUTMCoords().trim().length()>0)
			{
				if(rec.getTridasLocationFromUTM(UTMDatum.NAD27)==null)
				{
					log.error("UTM field: '"+rec.getUTMCoords()+"' is unparseable for " + rec.getCatalogCode());
					logFailedCatalogCode(rec.getCatalogCode()+ "   - UTM field is unparseable");
					countfailure++;
				}
			}
			
			// Check taxon is valid and parseable
			if(rec.getITRDBSpeciesCode()==null || rec.getITRDBSpeciesCode().length()==0)
			{
				//log.error("ITRDB species code is blank for " + rec.getCatalogCode());
				//logFailedCatalogCode(rec.getCatalogCode()+ "   - ITRDB species code is blank");
				//countfailure++;
			}
			else if (rec.getTaxon()==null || rec.getTaxon().getNormalStd()==null)
			{
				log.error("ITRDB species code "+rec.getITRDBSpeciesCode()+" is invalid for " + rec.getCatalogCode());
				logFailedCatalogCode(rec.getCatalogCode()+ "   - ITRDB species code is invalid");
				countfailure++;
			}
			
		}
		
		if(countfailure>0)
		{
			log.error("******************************************************************************************************************");
			log.error("A total of "+countfailure.intValue()+" records out of "+countall.intValue()+" are invalid for one or more reasons.");
			log.error("******************************************************************************************************************");

			fail();
		}
		
	}
	
	
	
	public void testXML() {

		log.info("*************************************************************");
		log.info("Running testXML() to check validity of XML export");
		log.info("*************************************************************");
		

		
		List<RediscoveryExport> lst = RediscoveryExportEx
				.getICMSRecordsFromXMLFile(filename, true);

		if (lst == null) {
			fail();
		}
		
		
	}
	
	

	public void testCatalogDate() {

		log.info("******************************************");
		log.info("Running testCatalogDate()");
		log.info("******************************************");

		Double countall = 0.0;
		Double count = 0.0;

		List<RediscoveryExport> lst = RediscoveryExportEx
				.getICMSRecordsFromXMLFile(filename, false);

		if (lst == null) {
			fail();
		}

		for (RediscoveryExport record : lst) {
			RediscoveryExportEx rec = (RediscoveryExportEx) record;
			countall++;
			DateTime s = rec.getCatalogDateAsDateTime();
			if (s != null) {
				// log.debug(rec.getCatalogDate()+"  -->  "+
				// s.getValue().toString());
				count++;
			} else {
				log.debug(rec.getCatalogDate() + "  failed to parse for '"
						+ rec.getCatalogCode() + "'");
			}

		}

		log.debug("Total of " + count.intValue()
				+ " records had parseable CatalogDate fields");
		log.debug("Total of " + (countall.intValue() - count.intValue())
				+ " records had unparseable CatalogDate fields");
		log.debug(" = " + ((count / countall) * 100) + "% success rate");

	}

	public void testOrigin() {

		log.info("******************************************");
		log.info("Running testOrigin()");
		log.info("******************************************");

		Double countall = 0.0;
		Double count = 0.0;

		List<RediscoveryExport> lst = RediscoveryExportEx
				.getICMSRecordsFromXMLFile(filename, false);

		if (lst == null) {
			fail();
		}

		for (RediscoveryExport record : lst) {
			RediscoveryExportEx rec = (RediscoveryExportEx) record;
			countall++;

			if (rec.getCounty() != null && rec.getState() != null
					&& rec.getCountry() != null) {
				count++;
			}
			/*
			 * log.debug("Parsing origin field: "+rec.getOrigin());
			 * log.debug(" Country = "+rec.getCountry());
			 * log.debug(" State = "+rec.getState());
			 * log.debug(" County = "+rec.getCounty());
			 * log.debug(rec.getITRDBSpeciesCode() +"  -->  " +
			 * rec.getTaxon().getValue());
			 */
		}

		log.debug("Total of " + count.intValue()
				+ " records had parseable origin fields");
		log.debug("Total of " + (countall - count)
				+ " records had unparseable origin fields");
		log.debug(" = " + ((count / countall) * 100) + "% success rate");
	}

	public void testConditions() {

		log.info("******************************************");
		log.info("Running testConditions()");
		log.info("******************************************");

		List<RediscoveryExport> lst = RediscoveryExportEx
				.getICMSRecordsFromXMLFile(filename, false);

		if (lst == null) {
			fail();
		}

		HashSet<NormalCondition> condition = new HashSet<NormalCondition>();

		for (RediscoveryExport record : lst) {
			RediscoveryExportEx rec = (RediscoveryExportEx) record;
			condition.add(rec.getCondition());
		}

		for (NormalCondition c : condition) {
			// log.debug(c.toString());
		}
	}

	public void testCountFieldUse() {
		log.info("******************************************");
		log.info("Running testCountFieldUse()");
		log.info("******************************************");
		List<RediscoveryExport> lst = RediscoveryExportEx
				.getICMSRecordsFromXMLFile(filename, false);

		Double recordcount = 0.0;
		Double origin = 0.0;
		Double utm = 0.0;

		if (lst == null) {
			fail();
		}

		for (RediscoveryExport record : lst) {
			recordcount++;

			RediscoveryExportEx rec = (RediscoveryExportEx) record;

			if (rec.getOrigin() != null && rec.getOrigin().trim().length() > 0) {
				origin++;
			}

			if (rec.getUTMCoords() != null
					&& rec.getUTMCoords().trim().length() > 0) {
				utm++;
			}
		}

		log.debug("Total records: " + recordcount);
		log.debug("Origin field : " + ((origin / recordcount) * 100) + "%");
		log.debug("UTM field    : " + ((utm / recordcount) * 100) + "%");

	}

	public void testLTRRCodes() {
		log.info("******************************************");
		log.info("Running testLTRRCodes()");
		log.info("******************************************");

		List<RediscoveryExport> lst = RediscoveryExportEx
				.getICMSRecordsFromXMLFile(filename, false);
		Boolean failed = false;
		Double recordcount = 0.0;
		ArrayList<String> codes = new ArrayList<String>();

		if (lst == null) {
			fail();
		}

		for (RediscoveryExport record : lst) {
			recordcount++;

			RediscoveryExportEx rec = (RediscoveryExportEx) record;
			String code = null;
			try {
				code = rec.getObjectCode() + "-" + rec.getElementCode();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getLocalizedMessage());
				failed = true;
			}

			if (code != null && rec.getSampleCode() != null) {
				code += "-" + rec.getSampleCode();
			}

			if (code != null) {
				if (codes.contains(code)) {
					log.error("Duplicate code: '" + code + "' recorded for '"
							+ rec.getCatalogCode() + "'");
					failed = true;
				} else {
					codes.add(code);
				}
			}
		}

		Collections.sort(codes);

		if (failed) {
			log.debug("Total records: " + recordcount.intValue());
			log.debug("Codes count  : " + codes.size());
			log.debug("Records with codes missing: "
					+ (recordcount.intValue() - codes.size()));
			fail();
		} else {
			log.info("All records have LTRR codes");
		}

	}

	public void testCatalogCode() {
		List<RediscoveryExport> lst = RediscoveryExportEx
				.getICMSRecordsFromXMLFile(filename, false);

		Double recordcount = 0.0;
		ArrayList<String> codes = new ArrayList<String>();

		if (lst == null) {
			fail();
		}

		for (RediscoveryExport record : lst) {
			recordcount++;

			RediscoveryExportEx rec = (RediscoveryExportEx) record;
			String code = rec.getCatalogCode();
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");
			code = code.replace("  ", " ");

			if (codes.contains(code)) {
				log.error("Duplicate catalog code: " + code);
			} else {
				codes.add(code);
			}

		}

		Collections.sort(codes);

		if (recordcount - codes.size() == 0) {
			log.debug("Passed!");
		} else {
			log.debug("Records with codes missing: "
					+ (recordcount - codes.size()));
			fail();
		}

	}

	public void testUTM() {

		log.info("******************************************");
		log.info("Running testUTM()");
		log.info("******************************************");

		List<RediscoveryExport> lst = RediscoveryExportEx
				.getICMSRecordsFromXMLFile(filename, false);
		Double count = 0.0;
		Double countall = 0.0;

		if (lst == null) {
			fail();
		}

		for (RediscoveryExport record : lst) {
			countall++;
			RediscoveryExportEx rec = (RediscoveryExportEx) record;

			if (rec.getUTMCoords() != null) {
				TridasLocation loc = rec
						.getTridasLocationFromUTM(SpatialUtils.UTMDatum.NAD27);

				if (loc != null) {
					// log.debug(rec.getUTMCoords()+"  --> NAD27 "+loc.getLocationGeometry().getPoint().getPos().getValues().toString());
					count++;
				}

				loc = rec.getTridasLocationFromUTM(SpatialUtils.UTMDatum.NAD83);

				/*
				 * if(loc!=null) {
				 * log.debug(rec.getUTMCoords()+"  --> NAD83 "+loc
				 * .getLocationGeometry
				 * ().getPoint().getPos().getValues().toString()); }
				 */
			}
		}

		log.debug("Total of " + count.intValue() + " records had UTM coords");
		log.debug(" = " + ((count / countall) * 100) + "%");

	}

}
