package org.tellervo.desktop.maventests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import com.rediscov.util.ICMSImporter;
import com.rediscov.util.RediscoveryExportEx;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

public class ICMSTest extends TestCase {
	private static final Logger log = LoggerFactory.getLogger(ICMSTest.class);

	private String filename = "/home/pbrewer/Dropbox/export4.xml";
	private boolean skipAllTests= true;

	
	public void testImporter()
	{
		if(skipAllTests) return;
		
		ICMSImporter importer = new ICMSImporter(filename);
		importer.doImport();
		
	}
	
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
		if(skipAllTests) return;
		
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
				ltrrcode = rec.getSubObjectCode() + "-" + rec.getElementCode();
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
		if(skipAllTests) return;
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
		if(skipAllTests) return;
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
		if(skipAllTests) return;
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
		if(skipAllTests) return;
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
		if(skipAllTests) return;
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
		if(skipAllTests) return;
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
				code = rec.getSubObjectCode() + "-" + rec.getElementCode();
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
		if(skipAllTests) return;
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
	
	public void testHistCultPer() {
		if(skipAllTests) return;
		String[] dict = {
				"16TH C",
				"16TH C, EARLY",
				"16TH C, MIDDLE",
				"16TH C, LATE",
				
				"17TH C",
				"17TH C, EARLY",
				"17TH C, MIDDLE",
				"17TH C, LATE",
				
				"18TH C",
				"18TH C, EARLY",
				"18TH C, MIDDLE",
				"18TH C, LATE",
				
				"19TH C",
				"19TH C, EARLY",
				"19TH C, MIDDLE",
				"19TH C, LATE",
				"19TH C, SECOND HALF",
				
				"20TH C",
				"20TH C, EARLY",
				"20TH C, MIDDLE",
				"20TH C, LATE",
				"ACKMEN",
				"AMARGOSA",
				"AMARGOSA I",
				"AMARGOSA II",
				"AMERICAN",
				"ANGELL",
				"ANIMAS",
				"APACHE WARS",
				"APISHAPA PHASE",
				"ARCHAIC",
				"ARCHAIC, EARLY",
				"ARCHAIC, LATE",
				"ARCHAIC, MIDDLE",
				"BAKER",
				"BASKETMAKER",
				"BASKETMAKER II",
				"BASKETMAKER III",
				"BASKETMAKER II-III",
				"BLACK ROCK",
				"BONITO",
				"BONNEVILLE",
				"CAMP VERDE",
				"CAMP VERDE, EARLY",
				"CAMP VERDE, LATE",
				"CANYON DEL ORO",
				"CERAMIC, EARLY",
				"CERROS",
				"CHIRICAHUA",
				"CINDER PARK",
				"CIVANO",
				"CIVIL WAR",
				"CLASSIC",
				"CLASSIC, EARLY",
				"CLASSIC, LATE",
				"CLASSIC, MIDDLE",
				"CLOVERDALE",
				"CLOVIS",
				"COLONIAL",
				"COLONIAL, LATE",
				"COWHORN",
				"DE CHELLY",
				"DEATH VALLEY I",
				"DEATH VALLEY II",
				"DEATH VALLEY II, LATE",
				"DEATH VALLEY III",
				"DEATH VALLEY IV",
				"DEATH VALLEY V",
				"DEL MUERTO",
				"DEPRESSION ERA",
				"EARLY HISTORIC",
				"EL PASO",
				"EL TOVAR",
				"EL TOVAR, POST",
				"EL TOVAR, PRE",
				"ELDEN",
				"EN MEDIO",
				"ENCINAS",
				"ESTRELLA",
				"FOLSOM",
				"FORMATIVE",
				"GALIURO",
				"GILA",
				"GILA BUTTE",
				"GOBERNADOR, EARLY",
				"HARDT",
				"HISTORIC",
				"HISTORIC, EARLY",
				"HOMESTEADING, INDIAN WARS",
				"HOMESTEADING, RANCHING",
				"HONANKI",
				"HOSTA BUTTE",
				"INDIAN WARS",
				"KLONDIKE",
				"LA PLATA",
				"LATE PREHISTORIC",
				"LLANO",
				"MANCOS",
				"MANGAS",
				"MARANA",
				"MCELMO",
				"MEDIO",
				"MESA VERDE",
				"MIAMI",
				"MISSION",
				"MISSION, EARLY",
				"MISSION, LATE",
				"MODERN",
				"NAVAJO",
				"NAVAJO, EARLY",
				"NAVAJO, LATE",
				"PADRE",
				"PATAYAN I",
				"PATAYAN II",
				"PATAYAN III",
				"PATAYAN III, LATE",
				"PIEDRA",
				"PIEDRA, LATE",
				"PINTO",
				"PIONEER",
				"PIONEER, LATE",
				"PRECLASSIC",
				"PROTOHISTORIC",
				"PUEBLO",
				"PUEBLO I",
				"PUEBLO I, EARLY",
				"PUEBLO I, LATE",
				"PUEBLO I, MIDDLE",
				"PUEBLO II",
				"PUEBLO II, EARLY",
				"PUEBLO II, LATE",
				"PUEBLO II, MIDDLE",
				"PUEBLO III",
				"PUEBLO III, EARLY",
				"PUEBLO III, LATE",
				"PUEBLO III, MIDDLE",
				"PUEBLO IV",
				"PUEBLO V",
				"RESERVE",
				"RILLITO",
				"RINCON",
				"RINCON, EARLY",
				"RINCON, LATE",
				"RIO DE FLAG",
				"ROOSEVELT",
				"ROSA",
				"RUSSIAN",
				"SACATON",
				"SACATON, EARLY",
				"SACATON, LATE",
				"SAN DIEGUITO",
				"SAN DIEGUITO I",
				"SAN DIEGUITO II",
				"SAN FRANCISCO",
				"SAN JOSE",
				"SAN PEDRO",
				"SANTA CRUZ",
				"SANTA CRUZ, LATE",
				"SANTAN",
				"SEDENTARY",
				"SEDENTARY, EARLY",
				"SEDENTARY, LATE",
				"SELLS",
				"SELLS, EARLY",
				"SNAKETOWN",
				"SOHO",
				"SQUAW PEAK",
				"SUNSET",
				"SWEETWATER",
				"TANQUE VERDE",
				"TANQUE VERDE, EARLY",
				"TRADING POST/TOURIST",
				"TUCSON",
				"TULAROSA",
				"TURKEY HILL",
				"TUZIGOOT",
				"VAHKI",
				"VAMORI",
				"VENTANA",
				"WENDOVER",
				"WORLD WAR I",
				"WORLD WAR II",
				"WORLD WAR II INTERNMENT",
				"YUMAN II",
				"YUMAN III"};
		
		ArrayList<String> standard = new ArrayList<String>(Arrays.asList(dict));
		
		List<RediscoveryExport> lst = RediscoveryExportEx
				.getICMSRecordsFromXMLFile(filename, false);

		Double recordcount = 0.0;
		Double goodcount = 0.0;
		HashSet<String> nonstandard = new HashSet<String>();
		

		if (lst == null) {
			fail();
		}

		for (RediscoveryExport record : lst) {
			

			RediscoveryExportEx rec = (RediscoveryExportEx) record;
			String code = rec.getHistCultPer().trim();
			
			String[] codes = code.split("--");
			
			
			for(String c : codes)
			{
				recordcount++;
				
				c = c.trim();
				if(standard.contains(c))
				{
					goodcount++;
				}
				else
				{
					nonstandard.add(c);
				}
			}
		}

		if(nonstandard.size()==0)
		{
			log.debug("Passed!");
			return;
		}
		else
		{
			Iterator iter = nonstandard.iterator();
			while (iter.hasNext()) {
			    log.debug(iter.next().toString());
			}
			log.debug("Total number of non-standard codes: "+ nonstandard.size());
			log.debug("Percentage non-standard codes     : "+(goodcount/recordcount)*100);
			fail();
		}

	}

	public void testUTM() {
		if(skipAllTests) return;
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
