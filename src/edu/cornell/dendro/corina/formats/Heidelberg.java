//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.formats;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.ArrayList;
import java.util.List;

import java.io.OutputStreamWriter;
import java.io.StreamTokenizer;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.BaseSeries;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

/**
   Frank Rinn's "Heidelberg" format.  I believe this format is the
   default format for his TSAP program.

   <p>This class is unfinished.  It doesn't load or save metadata yet,
   and assumes all files are summed.</p>

   <h2>Left to do</h2>
   <ul>
     <li>doesn't load or save metadata yet
     <li>assumes all files are summed
     <li>doesn't allow 0's in the data stream; either handle them
         properly, or don't allow them to be saved
	 (change unit test to add zeros)
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Heidelberg implements Filetype {

	@Override
	public String toString() {
		return I18n.getText("format.heidelberg") + " (*"+ getDefaultExtension()+")";
	}

	public String getDefaultExtension() {
		return ".fh";
	}

	public Sample load(BufferedReader r) throws IOException {
		// make sure it's a heidelberg file
		String line = r.readLine();
		if (line == null || !line.startsWith("HEADER:"))
			throw new WrongFiletypeException(); // no HEADER: found

		// new sample, with given filename
		Sample s = new Sample();

		// don't know end, yet
		Year end = null;
		int length = -1;

		// metadata
		for (;;) {
			// read a line
			line = r.readLine();

			// got to data, stop
			if (line.startsWith("DATA:"))
				break;

			// parse line as "variable = value", and put into s.meta
			int i = line.indexOf("=");
			if (i == -1)
				throw new WrongFiletypeException();
			String tag = line.substring(0, i);
			String value = line.substring(i + 1);

			// Attempt to map metadata into TRiDaS fields
			if (tag.equals("DateEnd"))
				end = new Year(value);

			if (tag.equals("Length"))
				length = Integer.parseInt(value);

			if(tag.equals("Species"))
				s.setMeta("species", value);

			
			
		}

		// if we got here, line starts with DATA:...
		String dataFormat = line.substring(5);
				
		// no end?  die.
		if (end == null)
			throw new WrongFiletypeException();
		
		StreamTokenizer t = new StreamTokenizer(r);
		
		if(dataFormat.compareToIgnoreCase("single") == 0 || dataFormat.compareToIgnoreCase("tree") == 0) {
			parseSingle(s, t, length);
		}
		else if(dataFormat.compareToIgnoreCase("double") == 0) {
			parseDouble(s, t, length);
		}
		else if(dataFormat.compareToIgnoreCase("chrono") == 0) {
			parseFull(s, t, length);
		}
		else {
			// hmm. what should we do here?
			// let's call this a bug, so we get better user feedback.

			IOException ioe = new IOException("Couldn't open Heidelberg data format: " + dataFormat);
			new edu.cornell.dendro.corina.gui.Bug(ioe);
			throw ioe;
		}

		// set range, and return
		s.setRange(new Range(end.add(1 - s.getData().size()), end));
		return s;
	}

	private void parseFull(Sample s, StreamTokenizer t, int length) throws IOException {
		int idx = 0;

		// data -- i'll assume all data is (width,count,up,down)

		s.setCount(new ArrayList());
		s.setWJIncr(new ArrayList());
		s.setWJDecr(new ArrayList());
		
		for (;;) {
			// parse (datum, count, up, dn)
			int datum, count, up, dn;
			try {
				t.nextToken();
				datum = (int) t.nval;
				t.nextToken();
				count = (int) t.nval;
				t.nextToken();
				up = (int) t.nval;
				t.nextToken();
				dn = (int) t.nval;
			} catch (IOException ioe) {
				throw new WrongFiletypeException();
			}

			// (0,0,0,0) seems to mean end-of-sample
			if (datum == 0)
				break;

			// add to lists
			s.getData().add(new Integer(datum));
			s.getCount().add(new Integer(count));
			s.getWJIncr().add(new Integer(up));
			s.getWJDecr().add(new Integer(dn));

			idx++;

			// break out if we have 'length' samples
			if (idx == length)
				break;
		}
	}
	
	private void parseSingle(Sample s, StreamTokenizer t, int length) throws IOException {
		int idx = 0;

		// data -- i'll assume all data is just a width
		
		for (;;) {
			// parse (datum)
			int datum;
			try {
				t.nextToken();
				datum = (int) t.nval;
			} catch (IOException ioe) {
				throw new WrongFiletypeException();
			}

			// (0) seems to mean end-of-sample
			if (datum == 0)
				break;

			// add to lists
			s.getData().add(new Integer(datum));

			idx++;

			// break out if we have 'length' samples
			if (idx == length)
				break;
		}
		
	}

	private void parseDouble(Sample s, StreamTokenizer t, int length) throws IOException {
		int idx = 0;

		// data -- i'll assume all data is (width,count)

		s.setCount(new ArrayList());
		
		for (;;) {
			// parse (datum, count)
			int datum, count;
			try {
				t.nextToken();
				datum = (int) t.nval;
				t.nextToken();
				count = (int) t.nval;
			} catch (IOException ioe) {
				throw new WrongFiletypeException();
			}

			// (0,0) seems to mean end-of-sample
			if (datum == 0)
				break;

			// add to lists
			s.getData().add(new Integer(datum));
			s.getCount().add(new Integer(count));

			idx++;

			// break out if we have 'length' samples
			if (idx == length)
				break;
		}
		
	}


	public void save(Sample s, BufferedWriter w) throws IOException {
		
		TridasObject tobj = s.getMeta(Metadata.OBJECT, TridasObject.class);
		TridasElement telem = s.getMeta(Metadata.ELEMENT, TridasElement.class);
		TridasSample tsamp = s.getMeta(Metadata.SAMPLE, TridasSample.class);
		TridasRadius trad = s.getMeta(Metadata.RADIUS, TridasRadius.class);
		ITridasSeries tseries = s.getSeries();
		
		
		// header
		w.write("HEADER:");
		w.newLine();
		
		// TSAP AcceptDate - order accept date
		// Not implemented - tridas.project.requestdate
		
		// TSAP Age - tree age
		// Is this the same as ring count?  
		
		// TSAP AutoCorrelation (used for Tucson file format)
		// Unknown not implemented
		
		// TSAP Bark -  'B' = bark available, '-' = bark not available
		try{
			
			if (trad.getWoodCompleteness().getBark().getPresence().toString().compareTo("PRESENT")==0){
				w.write("Bark=B");
			}
			else if (trad.getWoodCompleteness().getBark().getPresence().toString().compareTo("ABSENT")==0){
				w.write("Bark=-");
			}
			w.newLine();
		}	
		catch (NullPointerException e){}
		
		// TSAP BHD - Breast height diameter
		try{
			w.write("BHD=" + telem.getDimensions().getDiameter().toString());
			w.newLine();
		}
		catch (NullPointerException e){}
		
		// TSAP Bibliography[n] and BibliographyCount
		// Not implemented
		
		// TSAP Bundle - Timber bundle
		// Unknown not implemented
		
		// TSAP CardinalPoint
		// Unknown not implemented
		
		// TSAP Chronology Type - used for sheffield file format
		// Unknown not implemented
		
		// TSAP ChronoMemberCount - Chrono members
		// Unknown not implemented
		
		// TSAP ChronoMemberKeycodes - Chrono members
		// Unknown not implemented
		
		// TSAP Circumference
		// Not implemented
		
		// TSAP Client
		// Not implemented
		
		// TSAP ClientNo
		// Not implemented
		
		// TSAP Collector - Sampling personal ID
		// Not implemented
		
		// TSAP Comment - Single line comment
		// Not implemented used multi line syntax instead
		
		// TSAP Comment[n] and CommentCount
		int commentint = 0;
		try{
			if(tobj.getComments()!=null && tobj.getComments().length()>0)
			{
				commentint++;
				w.write("Comment[" + Integer.toString(commentint) + "]=Object comments: " + tobj.getComments().toString());
				w.newLine();
			}
		}
		catch (NullPointerException e){}
		try{
			if(telem.getComments()!=null && telem.getComments().length()>0)
			{
				commentint++;
				w.write("Comment[" + Integer.toString(commentint) + "]=Element comments: " + telem.getComments().toString());
				w.newLine();
			}
		}
		catch (NullPointerException e){}
		try{
			if(tsamp.getComments()!=null && tsamp.getComments().length()>0)
			{
				commentint++;
				w.write("Comment[" + Integer.toString(commentint) + "]=Sample comments: " + tsamp.getComments().toString());
				w.newLine();
			}
		}	
		catch (NullPointerException e){}
		try{
			if(trad.getComments()!=null && trad.getComments().length()>0)
			{
				commentint++;
				w.write("Comment[" + Integer.toString(commentint) + "]=Radius comments: " + trad.getComments().toString());
				w.newLine();
			}
		}	
		catch (NullPointerException e){}
		if(commentint>0)
		{
			w.write("CommentCount=" + Integer.toString(commentint));
			w.newLine();				
		}
		
		// TSAP Continent
		// Not implemented
		
		// TSAP CoreNo 
		try{
			w.write("CoreNo="+tsamp.getTitle().toString());
			w.newLine();
		}catch (NullPointerException e){}
		
		// TSAP Country
		// Not implemented
		
		// TSAP DataFormat
		// Unknown not implemented
		
		// TSAP DataType - one of Ringwidth, Earlywood, Latewood, EarlyLateWood, Min density, Max density
		// Earlywood density, Latewood density. Pith age, Weight of ringwidth
		// NB - Hardcoded for now.
		try{
			w.write("DataType=Ringwidth");
			w.newLine();
		} catch (NullPointerException e){}
		
		// TSAP DateBegin
		try{
			w.write("DateBegin=" + s.getRange().getStart());
			w.newLine();
		} catch (NullPointerException e){}
		
		// TSAP Dated
		try{
			if(tseries.getInterpretation().getDating().getType().compareTo(NormalTridasDatingType.RELATIVE)==0)
			{
				w.write("RelDated");
			}
			else if (tseries.getInterpretation().getDating().getType().compareTo(NormalTridasDatingType.ABSOLUTE)==0)
			{
				w.write("Dated");
			}
			else if (tseries.getInterpretation().getDating().getType().compareTo(NormalTridasDatingType.DATED___WITH___UNCERTAINTY)==0)
			{
				w.write("Dated");
			}
			else if (tseries.getInterpretation().getDating().getType().compareTo(NormalTridasDatingType.RADIOCARBON)==0)
			{
				w.write("Dated");
			}			
			else{
				w.write("Undated");
			}
			w.newLine();
		} catch (NullPointerException e){}
		
		// TSAP DateEnd	
		try{
			w.write("DateEnd=" + s.getRange().getEnd());
			w.newLine();
		} catch (NullPointerException e){}
		
		// TSAP DateOfSampling
		try{
			w.write("DateOfSampling="+tsamp.getSamplingDate().getValue().toString());
			w.newLine();
		} catch (NullPointerException e){}
		
		// TSAP
		//DateRelBegin[n]
		//DateRelEnd[n]
		//DateRelReferenceKey[n]
		//DateRelCount
		//DeltaMissingRingsAfter
		//DeltaMissingRingsBefore
		//DeltaRingsFromSeedToPith
		//Disk
		//District
		//EdgeInformation
		//EffectiveAutoCorrelation
		//EffectiveMean
		//EffectiveMeanSensitivity
		//EffectiveNORFAC
		//EffectiveNORFM
		//EffectiveStandardDeviation
		//Eigenvalue

		// TSAP Elevation
		try{
			w.write("Elevation="+telem.getAltitude().toString());
			w.newLine();
		} catch (NullPointerException e){}
		
		// TSAP EstimatedTimePeriod
		try{
			w.write("EstimatedTimePeriod=" + tobj.getCoverage().getCoverageTemporal().toString());
			w.newLine();
		} catch (NullPointerException e){}
		
		// TSAP Exposition
		// Unknown not implemented
		
		// TSAP FieldNo
		// Unknown not implemented
		
		// TSAP FirstMeasurementDate
		try{
			w.write("FirstMeasurementDate="
					+Integer.toString(tseries.getCreatedTimestamp().getValue().getDay())+"/"
					+Integer.toString(tseries.getCreatedTimestamp().getValue().getMonth())+"/"
					+Integer.toString(tseries.getCreatedTimestamp().getValue().getYear())+" at "
					+Integer.toString(tseries.getCreatedTimestamp().getValue().getHour())+":"
					+Integer.toString(tseries.getCreatedTimestamp().getValue().getMinute())
					);
			w.newLine();			
		} catch (NullPointerException e){}

		// TSAP FirstMeasurementPersID
		try{
			if(tseries instanceof TridasMeasurementSeries){
				TridasMeasurementSeries tmseries = (TridasMeasurementSeries) tseries;
				w.write("FirstMeasurementPersID="+tmseries.getAnalyst().toString());
				w.newLine();
			}
		} catch (NullPointerException e){}
			
		// TSAP GlobalMathComment[n]
		// TSAP GlobalMathCommentCount
		// Unknown not implemented
		
		// TSAP Group
		// Only used for Sheffield file format so not implemented
		
		// TSAP HouseName
		// TSAP HouseNo
		// TSAP ImageCellRow
		// TSAP ImageComment[n]
		// TSAP ImageFile[n]
		// TSAP ImageCount
		// TSAP ImageFile
		// TSAP Interpretation
		// TSAP InvalidRingsAfter
		// TSAP InvalidRingsBefore
		// TSAP JuvenileWood
		// All not implemented
		
		// TSAP KeyCode
		try{
			w.write("KeyCode=" + s.getDisplayTitle().replace("-", ""));
			w.newLine();
		} catch (NullPointerException e){}

		// TSAP KeyNo
		// Unknown not implemented
		
		// TSAP LaboratoryCode
		// Unknown not implemented
		
		// TSAP LastRevisionDate
		try{
			w.write("LastRevisionDate="
					+Integer.toString(tseries.getLastModifiedTimestamp().getValue().getDay())+"/"
					+Integer.toString(tseries.getLastModifiedTimestamp().getValue().getMonth())+"/"
					+Integer.toString(tseries.getLastModifiedTimestamp().getValue().getYear())+" at "
					+Integer.toString(tseries.getCreatedTimestamp().getValue().getHour())+":"
					+Integer.toString(tseries.getCreatedTimestamp().getValue().getMinute())
					);
			w.newLine();
		} catch (NullPointerException e){}		
		
		// TSAP LastRevisionPersID
		// Not implemented
		
		// TSAP Latitude and Longitude
		// Need to make this dynamic between TRiDaS entities so that it uses more detailed info if possible
		try{		
			List<Double> poslist = tobj.getLocation().getLocationGeometry().getPoint().getPos().getValues();		
			if(poslist.size()==2)
			{
				w.write("Latitude="+ poslist.get(0).toString());
				w.newLine();
				w.write("Longitude="+poslist.get(1).toString());
				w.newLine();
			}
		} catch (NullPointerException e){}		

		// TSAP Location
		try{
			w.write("Location=" + tobj.getTitle());
			w.newLine();			
		} catch (NullPointerException e){}
		
		// TSAP LeaveLoss
		// Not implemented
		
		// TSAP Length 
		try{
			w.write("Length=" + s.getData().size());
			w.newLine();   
		} catch (NullPointerException e){}
			
		// TSAP LocationCharacteristics
		try{
			w.write("LocationCharacteristics="+tobj.getLocation().getLocationType().value().toString());
		} catch (NullPointerException e){}
		
		// TSAP MajorDimension
		// TSAP MathComment
		// TSAP MathComment[n]
		// TSAP MathCommentCount
		// TSAP MeanSensitivity
		// TSAP MinorDimension
		// TSAP MissingRingsAfter
		// TSAP MissingRingsBefore		
		// TSAP NumberOfSamplesInChrono
		// TSAP NumberOfTreesInChrono
		// All not implemented
		
		// TSAP PersId
		// Not implemented
		
		// TSAP Pith, P=present -=absent
		try{
			String pith = trad.getWoodCompleteness().getPith().getPresence().toString();
			if (pith.compareTo("COMPLETE")==0){
				w.write("Pith=P");
				w.newLine();
			}
			else if (pith.compareTo("INCOMPLETE")==0){
				w.write("Pith=P");
				w.newLine();
			}
			else if (pith.compareTo("ABSENT")==0)
			{
				w.write("Pith=-");
				w.newLine();
			}			
		} catch (NullPointerException e){}
		
		// TSAP Project
		// Not implemented
		
		// TSAP ProtectionCode - used for CATRAS
		// Not implemented
		
		// TSAP Province
		// Not implemented
		
		// TSAP QualityCode
		// Not implemented
		
		// TSAP Radius used for INRA file format
		// Not implemented
		
		// TSAP RadiusNo
		try{
			w.write("RadiusNo=" + trad.getTitle());
			w.newLine();		
		} catch (NullPointerException e){}
		
		// TSAP RelGroundWaterLevel
		// Not implemented
		
		// TSAP RingsFromSeedToPith
		// Not implemented
		
		// TSAP SampleType - used for Sheffield format
		// Not implemented
		
		// TSAP SamplingHeight
		
		// TSAP SamplingPoint
		try{
			w.write("SamplingPoint=" + tsamp.getPosition().toString());
			w.newLine();		
		} catch (NullPointerException e){}		
		
		// TSAP SapWoodRings
		try{
			w.write("SapWoodRings=" + trad.getWoodCompleteness().getSapwood().getNrOfSapwoodRings().toString());
			w.newLine();		
		} catch (NullPointerException e){}	
		
		// TSAP Sequence
		// Not implemented
		
		// TSAP SeriesEnd - series ends with ring width, earlywood, latewood
		// TSAP SeriesStart - series starts with ring width, earlywood, latewood
		// TSAP SeriesType - single curve, mean curve, radius, chronology, autocorrelation
		// Not implemented
		
		// TSAP ShapeOfSample
		try{
			w.write("ShapeOfSample=" + telem.getShape().getNormalTridas().toString());
			w.newLine();		
		} catch (NullPointerException e){}			
		
		// TSAP Site - used for INRA file format
		// Not implemented
		
		// TSAP SiteCode
		try{
			w.write("SiteCode=" + tobj.getIdentifier().getValue().toString());
			w.newLine();		
		} catch (NullPointerException e){}			
		
		// TSAP SocialStand
		// Not implemented
		
		// TSAP SoilType
		try{
			w.write("SoilType=" + telem.getSoil().getDescription().toString());
			w.newLine();		
		} catch (NullPointerException e){}	
	
		// TSAP Species - ITRDB Species code
		// Not implemented

		// TSAP SpeciesName
		try{
			w.write("SpeciesName=" + telem.getTaxon().getNormal().toString());
			w.newLine();	
		} catch (NullPointerException e){}		
		
		// TSAP StandardDeviation used for Tucson file format
		// Not implemented
		
		// TSAP State
		// Not implemented
		
		// TSAP StemDiskNo
		try{
			w.write("StemDiskNo=" + tsamp.getTitle().toString());
			w.newLine();	
		} catch (NullPointerException e){}				
		
		// TSAP Street
		// Not implemented
		
		// TSAP Timber
		// Not implemented
		
		// TSAP TimberHeight
		try{
			w.write("TimberHeight=" + telem.getDimensions().getHeight().toString());
			w.newLine();	
		} catch (NullPointerException e){}
		
		// TSAP TimberType
		// Not implemented
		
		// TSAP TimberWidth
		try{
			w.write("TimberWidth=" + telem.getDimensions().getWidth().toString());
			w.newLine();	
		} catch (NullPointerException e){}
		
		// TSAP TotalAutoCorrelation
		// TSAP TotalMean
		// TSAP TotalMeanSensitivity
		// TSAP TotalNORFAC
		// TSAP TotalNORFM
		// TSAP TotalStandardDeviation
		// Not implemented
		
		// TSAP Town
		// TSAP TownZipCode
		// TSAP Tree - for INRA and Sheffield format
		// Not implemented
		
		// TSAP TreeHeight
		try{
			if (telem.getType().getNormal().compareTo("Tree")==0){
				w.write("TreeHeight=" + telem.getDimensions().getHeight().toString());
				w.newLine();
			}
		} catch (NullPointerException e){}
		
		// TSAP TreeNo
		try{
			if (telem.getType().getNormal().compareTo("Tree")==0){
				w.write("TreeNo=" + telem.getTitle().toString());
				w.newLine();
			}
		} catch (NullPointerException e){}		
		
		// TSAP Unit
		// Not implemented
		
		// TSAP UnmeasuredInnerRings
		// TSAP UnmeasuredOuterRings
		
		// TSAP WaldKante - WKE = Earlywood, WKL = Latewood, WKX = Unknown, WK? = Indistinct, --- = None
		// Difficult to implement as we don't structure type of wood in last ring so using 
		// term 'WK' when last ring is complete.
		try{
			String lastring = trad.getWoodCompleteness().getSapwood().getLastRingUnderBark().getPresence().toString();
			if (lastring.compareTo("PRESENT")==0){
				w.write("WaldKante=WK");
				w.newLine();
			}		
			else if (lastring.compareTo("ABSENT")==0)
			{
				w.write("WaldKante=---");
				w.newLine();
			}				
		} catch (NullPointerException e){}
		
		
		// TSAP WoodMaterialType
		// Not implemented
		
		// TSAP WorkTraces
		try{
			w.write("WorkTraces=" + telem.getProcessing().toString());
			w.newLine();	
		} catch (NullPointerException e){}		
		

		// NOTE
		// We convert all values of '0' to '1' here.
		// This is heidelberg's signal for end. Hmm.
		// This should be better documented
		// (BUG: what if it's negative?)
		// (ICK: if it changes the data, it's harder to test)
		
		// We're writing out a 'tree' or a 'single'...
		// Data is for a single sample, not a sum or anything.
		if(!s.hasCount()) {
			w.write("DATA:Tree");
			w.newLine();
			int column = 0;

			for (int i = 0; i < s.getData().size(); i++) {
				String datum = s.getData().get(i).toString();
				if (datum.equals("0"))
					datum = "1"; 
				
				// six space padding in this type of file
				writeDatum6(w, datum);
				
				// newline every 10
				if (column % 10 == 9)
					w.newLine();
				
				column++;
			}
			
			// extra 0's to pad
			while (column % 10 != 0) {
				w.write("     0");
				column++;
			}
			w.newLine();
		}
		// Data is for a sum without Weiserjahre data
		else if(!s.hasWeiserjahre()) {
			w.write("DATA:Double");
			w.newLine();
			int column = 0;

			for (int i = 0; i < s.getData().size(); i++) {
				String datum = s.getData().get(i).toString();
				if (datum.equals("0"))
					datum = "1"; 
				
				// six space padding in this type of file
				writeDatum6(w, datum);
				writeDatum6(w, s.getCount().get(i));
				
				// newline every 5
				if (column % 5 == 4)
					w.newLine();
				
				column++;
			}
			
			// extra 0's to pad
			while (column % 5 != 0) {
				w.write("     0     0");
				column++;
			}
			w.newLine();
			
		}
		else {
			boolean countIsNull = (s.getCount() == null);
			boolean wjIsNull = !s.hasWeiserjahre();
			
			// data
			w.write("DATA:Chrono"); // what's "chrono" mean?
			w.newLine();
			int column = 0;
			for (int i = 0; i < s.getData().size(); i++) {
				String datum = s.getData().get(i).toString();
				if (datum.equals("0"))
					datum = "1"; // DOCUMENT me!
				// (BUG: what if it's negative?)
				// (ICK: if it changes the data, it's harder to test)
				String count = (countIsNull ? "0" : s.getCount().get(i).toString());
				String up = (wjIsNull ? "0" : s.getWJIncr().get(i).toString());
				String dn = (wjIsNull ? "0" : s.getWJDecr().get(i).toString());

				// output ("%-5d%-5d%-5d%-5d", datum, count, up, dn)
				writeDatum5(w, datum);
				// WAS: w.write(StringUtils.leftPad(s.data.get(i).toString(), 5));
				writeDatum5(w, (countIsNull ? "0" : s.getCount().get(i)));
				// WAS: w.write(StringUtils.leftPad(countIsNull ? "0" : s.count.get(i).toString(), 5));
				w.write(StringUtils.leftPad(wjIsNull ? "0" : s.getWJIncr().get(i)
						.toString(), 5));
				w.write(StringUtils.leftPad(wjIsNull ? "0" : s.getWJDecr().get(i)
						.toString(), 5));

				// newline every 4
				if (column % 4 == 3)
					w.newLine();

				column++;
			}

			// COMBINE these: newline-every-4, and extra-0's-to-pad both
			// care about exactly one value: position (column).

			// extra 0's to pad
			while (column % 4 != 0) {
				w.write("    0    0    0    0");
				column++;
			}
			w.newLine();
		}
	}

	private void writeDatum5(BufferedWriter w, Object o) throws IOException {
		w.write(StringUtils.leftPad(o.toString(), 5));
	}

	private void writeDatum6(BufferedWriter w, Object o) throws IOException {
		w.write(StringUtils.leftPad(o.toString(), 6));
	}

	public Boolean isPackedFileCapable() {
		return false;
	}

	public String getDeficiencyDescription() {
		return this.toString() + " file format uses unstructured keyword-value pairs for metadata with no data type " +
				"constraints.  Only the metadata that can be mapped to standard " +
				"Heidelberg fields will be exported so the additional Corina/TRiDaS "+
				"metadata fields will not be available.";
	}

	public Boolean isLossless() {
		return false;
	}

}
