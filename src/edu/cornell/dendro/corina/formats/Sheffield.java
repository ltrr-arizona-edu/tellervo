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
// Copyright 2009 Peter Brewer
//

package edu.cornell.dendro.corina.formats;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.ArrayList;
import java.util.List;

import java.io.StreamTokenizer;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.BaseSeries;
import org.tridas.schema.ComplexPresenceAbsence;
import org.tridas.schema.DatingSuffix;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.PresenceAbsence;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasShape;

/**
  Standard format for Ian Tyer's Dendro for Windows program
*/
public class Sheffield implements Filetype {

	@Override
	public String toString() {
		return I18n.getText("format.sheffield") + " (*"+ getDefaultExtension()+")";
	}

	public String getDefaultExtension() {
		return ".d";
	}

	public Sample load(BufferedReader r)  {
		return null;


	}

	public void save(Sample s, BufferedWriter w) throws IOException {
		
		TridasObject tobj = s.getMeta(Metadata.OBJECT, TridasObject.class);
		TridasElement telem = s.getMeta(Metadata.ELEMENT, TridasElement.class);
		TridasSample tsamp = s.getMeta(Metadata.SAMPLE, TridasSample.class);
		TridasRadius trad = s.getMeta(Metadata.RADIUS, TridasRadius.class);
		ITridasSeries tseries = s.getSeries();
		
		// ******
		// HEADER
		// ******
		
		// Line 1 - Site name/sample number - free form text not including , " ( ) up to 64 characters	
		try{
			String str = (s.getDisplayTitle() + " " + tobj.getTitle().replace(",", ";").replace("\"", "'"));
			if(str.length()>64) str = str.substring(0,63);
			w.write(str);
			w.newLine();	
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}

		// Line 2 - Number of rings - whole positive number
		try{
			w.write(Integer.toString(tseries.getValues().size()));
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
		
		// Line 3 - Date type - single character; A = absolute date, R = relative date 
		NormalTridasDatingType dating = null;
		try{
			dating = tseries.getInterpretation().getDating().getType();
			
			if(dating.compareTo(NormalTridasDatingType.RELATIVE)==0)
			{
				w.write("R");
			}
			else if (dating.compareTo(NormalTridasDatingType.ABSOLUTE)==0)
			{
				w.write("A");
			}
			else if (dating.compareTo(NormalTridasDatingType.DATED___WITH___UNCERTAINTY)==0)
			{
				w.write("A");
			}
			else if (dating.compareTo(NormalTridasDatingType.RADIOCARBON)==0)
			{
				w.write("A");
			}			
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
		
		// Line 4 -Start date - whole number (can be negative) (if absolute 10001 = 1AD) 
		try{			
			BigInteger offset = BigInteger.valueOf(0);   // For 10000 offset if necessary
			BigInteger value = null;					 // Actual year value
			
			// Add 10000 to all 'absolute' years
			if((dating.compareTo(NormalTridasDatingType.ABSOLUTE)==0) || 
					(dating.compareTo(NormalTridasDatingType.DATED___WITH___UNCERTAINTY)==0) || 
					(dating.compareTo(NormalTridasDatingType.RADIOCARBON)==0))
			{
				offset = BigInteger.valueOf(10000);
			}
			
			// Handle depending on suffix
			DatingSuffix ds = tseries.getInterpretation().getFirstYear().getSuffix();		
			if (ds.compareTo(DatingSuffix.AD)==0)
			{
				// Add offset
				value = tseries.getInterpretation().getFirstYear().getValue().add(offset);
			}
			else if (ds.compareTo(DatingSuffix.BC)==0)
			{
				// Subtract offset
				value = BigInteger.valueOf(0).subtract(tseries.getInterpretation().getFirstYear().getValue().add(offset));
			}
			else if (ds.compareTo(DatingSuffix.BP)==0)
			{
				// Subtract BP value from 1950 to get value into AD/BC calender 
				value = BigInteger.valueOf(1950).subtract(tseries.getInterpretation().getFirstYear().getValue());
			
				int val = value.intValue();
				if (val>0){
					// Value is AD so *add* offset
					value = value.add(offset);
				}
				else if (val <= 0){
					// Value is BC so *subtract* offset+1 (to handle non-existent year 0BC/AD)
					value = value.subtract(offset.add(BigInteger.valueOf(1)));
				}
			}
			
			// Write the value out
			w.write(value.toString());
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
		
		// Line 5 either...
		// - Raw - Data type - single character; R = annual raw ring width data (NB earlier versions 
		//         used some other codes here for species e.g. ABEFPSU these are all interpreted as 
		//         equivalent to R)
		// - Mean - Data type - single character; W=timber mean with signatures, X=chron mean with 
		//          signatures, T = timber mean, C = chron mean, M = un-weighted master sequence 
		try{
			if (tseries instanceof TridasMeasurementSeries){
				w.write("R");
			}
			else if (tseries instanceof TridasDerivedSeries){
				/*
				 * TODO should we be using the other mean data types?
				 */
				w.write("C");
			}			
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}		
		
		// Line 6 either...
		// - Raw - Sap number - while positive number or 0
		// - Mean - Number of timbers/chronologies - whole positive number
		try{
			if (tseries instanceof TridasMeasurementSeries){
				// Raw - Sap number 
				if (trad.getWoodCompleteness().getSapwood().getNrOfSapwoodRings()!=null)
				{
					w.write(trad.getWoodCompleteness().getSapwood().getNrOfSapwoodRings().toString());
				}
				else
				{
					w.write("0");
				}
			}
			else if (tseries instanceof TridasDerivedSeries){
				// Mean - Number of timbers/chronologies
				w.write(s.getCount().toString());
			}				
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
		
		// Line 7 either...
		// Raw -  Edges inf. - single character; Y = has bark, ! = has ?bark, W = terminal ring probably complete 
		//        (i.e. possibly Winter Felled), S = terminal ring probably incomplete (i.e. possibly Summer Felled), 
		//        B = has h/s boundary, ? = has ?h/s boundary, N = has no specific edge, (NB but may have sap), 
		//        U = sap/bark unknown, C = charred outer edge, P = possibly charred outer edge 	
		// Mean - Chronology Type - single character; R = raw unfiltered data, 5 = 5 year running mean, 
		//        I = indexed data, U = unknown mean type
		try{
			if (tseries instanceof TridasMeasurementSeries){
				// Edge info
				if (trad.getWoodCompleteness().getBark().getPresence().compareTo(PresenceAbsence.PRESENT)==0)
				{
					w.write("Y");
				}
				else if (trad.getWoodCompleteness().getSapwood().getLastRingUnderBark().getPresence().compareTo(PresenceAbsence.PRESENT)==0)
				{
					// A little fuzzy here as could also be "S" = Terminal ring probably incomplete
					w.write("W");
				}
				else
				{
					w.write("U");
				}
			}
			else if (tseries instanceof TridasDerivedSeries){
				// Mean - Default to unknown chronology type
				w.write("U");
			}
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
			
		// Line 8 - Author and comment - free form text not including , " ( ) up to 64 characters e.g. IGT 4/2/1995
		try{
			String date = tseries.getCreatedTimestamp().getValue().toString();
			if (tseries instanceof TridasMeasurementSeries){
				TridasMeasurementSeries ser = (TridasMeasurementSeries) tseries;
				w.write(ser.getAnalyst().replace(",", ";").replace("\"", "'") + " " + date);
			}
			else if (tseries instanceof TridasDerivedSeries){
				TridasDerivedSeries ser = (TridasDerivedSeries) tseries;
				w.write(ser.getAuthor().replace(",", ";").replace("\"", "'") + " " + date);
			}
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
				
		// Line 9 - UK National grid reference 	2 characters +even no of digits up to 14 characters in all, 
		// ? = not known e.g.	TQ67848675
		w.write("?");
		w.newLine();
				
		// Line 10 - Latitude and longitude 
		//         - NEW e.g. 53.382457;-1.513623 
		//         - previously e.g. N54^50 W003^50, 
		//         - ? = not known 	
		try{
			List<Double> poslist = tobj.getLocation().getLocationGeometry().getPoint().getPos().getValues();		
			if(poslist.size()==2)
			{
				w.write(poslist.get(0).toString()+";"+poslist.get(1).toString());
			}	
			else	
			{
				w.write("?");
			}
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
				
		// Line 11 - Pith single character; C = centre of tree, V = within 5 years of centre, F = 5-10 years of 
		//         centre, G = greater than 10, ? = unknown 
		try{
			if (trad.getWoodCompleteness().getPith().getPresence().equals(ComplexPresenceAbsence.COMPLETE)){
				w.write("C");
			}
			else if (trad.getWoodCompleteness().getPith().getPresence().equals(ComplexPresenceAbsence.INCOMPLETE)){
				w.write("V");
			}
			else{
				w.write("?");
			}
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
				
		// Line 12 - Cross-section code - Two character code; first character, A = whole roundwood, B = half round, 
		//   C quartered, D radial/split plank, E tangential/sawn plank. second character, 1 untrimmed, 2 trimmed, 
		//   X irregularly trimmed. or, X = core /unclassifiable, ? unknown/unrecorded 	
		try{
			NormalTridasShape shape = telem.getShape().getNormalTridas();
			
			if(shape.equals(NormalTridasShape.WHOLE___SECTION)){
				w.write("A1");
			} else if(shape.equals(NormalTridasShape.HALF___SECTION)){
				w.write("B1");
			} else if(shape.equals(NormalTridasShape.QUARTER___SECTION)){
				w.write("C1");
			} else if(shape.equals(NormalTridasShape.BEAM___STRAIGHTENED___ON___ONE___SIDE)){
				w.write("A2");
			} else if(shape.equals(NormalTridasShape.PART___OF___UNDETERMINED___SECTION)){
				w.write("?");
			} else if(shape.equals(NormalTridasShape.PLANK___CUT___ON___ONE___SIDE)){				
				w.write("E1");
			} else if(shape.equals(NormalTridasShape.PLANK___NOT___INCLUDING___PITH___WITH___BREADTH___SMALLER___THAN___A___QUARTER___SECTION)){				
				w.write("E2");
			} else if(shape.equals(NormalTridasShape.RADIAL___PLANK___THROUGH___PITH)){
				w.write("D2");
			} else if(shape.equals(NormalTridasShape.RADIAL___PLANK___UP___TO___PITH)){
				w.write("D2");
			} else if(shape.equals(NormalTridasShape.SMALL___PART___OF___SECTION)){
				w.write("?");
			} else if(shape.equals(NormalTridasShape.SQUARED___BEAM___FROM___HALF___SECTION)){
				w.write("B2");
			} else if(shape.equals(NormalTridasShape.SQUARED___BEAM___FROM___QUARTER___SECTION)){
				w.write("C2");
			} else if(shape.equals(NormalTridasShape.SQUARED___BEAM___FROM___WHOLE___SECTION)){
				w.write("A2");
			} else if(shape.equals(NormalTridasShape.TANGENTIAL___PLANK___NOT___INCLUDING___PITH___WITH___BREADTH___LARGER___THAN___A___QUARTER___SECTION)){
				w.write("E2");
			} else if(shape.equals(NormalTridasShape.THIRD___SECTION)){
				// Bit fuzzy
				w.write("CX");
			} else if(shape.equals(NormalTridasShape.UNKNOWN)){
				w.write("?");
			} else if(shape.equals(NormalTridasShape.WEDGE___WHERE___RADIUS___EQUALS___THE___CIRCUMFERENCE)){
				w.write("CX");
			} else if(shape.equals(NormalTridasShape.WEDGE___WHERE___RADIUS___IS___BIGGER___THAN___THE___CIRCUMFERENCE)){				
				w.write("CX");
			} else if(shape.equals(NormalTridasShape.WEDGE___WHERE___RADIUS___IS___SMALLER___THAN___CIRCUMFERENCE)){
				w.write("CX");
			}else {
				w.write("?");
			}	
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("?"); 
			w.newLine();
		}
				
		// Line 13 - major dimension - whole number in mm, 0 if unrecorded or mean
		try{
			BigDecimal dim = BigDecimal.valueOf(0);
			
			// Find the largest value
			if(telem.getDimensions()!=null)
			{
				if(telem.getDimensions().getHeight()!=null){
					if (telem.getDimensions().getHeight().compareTo(dim) > 0) dim = telem.getDimensions().getHeight();
				}
				if(telem.getDimensions().getWidth()!=null){
					if (telem.getDimensions().getWidth().compareTo(dim) > 0) dim = telem.getDimensions().getWidth();
				}
				if(telem.getDimensions().getDepth()!=null){
					if (telem.getDimensions().getDepth().compareTo(dim) > 0) dim = telem.getDimensions().getDepth();
				}					
			}
			w.write(dim.toPlainString());
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
				
		// Line 14 - minor dimension - whole number in mm, 0 if unrecorded or mean
		try{
			BigDecimal dim = BigDecimal.valueOf(99999999);
			
			// Find the smallest value
			if(telem.getDimensions()!=null)
			{
				if(telem.getDimensions().getHeight()!=null){
					if (telem.getDimensions().getHeight().compareTo(dim) < 0) dim = telem.getDimensions().getHeight();
				}
				if(telem.getDimensions().getWidth()!=null){
					if (telem.getDimensions().getWidth().compareTo(dim) < 0) dim = telem.getDimensions().getWidth();
				}
				if(telem.getDimensions().getDepth()!=null){
					if (telem.getDimensions().getDepth().compareTo(dim) < 0) dim = telem.getDimensions().getDepth();
				}					
			}
			
			// Write value
			if (dim.equals(BigDecimal.valueOf(99999999))){ w.write("0"); } else { w.write(dim.toPlainString());}
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
				
		// Line 15 - unmeasured inner rings - single character+whole number; use pith codes + number of rings or, 
		//           H = heartwood, N = none
		// TODO not implemented yet
		w.write("N");
		w.newLine();
				
		// Line 16 - unmeasured outer rings - single character+whole number; use edges code + number of rings except 
		//           that S = sapwood with no edge and V is the spring felling equivalent other codes are, 
		//           H = heartwood with no edge, N = none 	
		// TODO not implemented yet
		w.write("N");
		w.newLine();
				
		// Line 17 - group/phase - free form text not including , " ( ) up to 14 characters 
		try{
			String str = tobj.getTitle().replace(",", ";").replace("\"", "'");
			if (str.length()>14) str = str.substring(0,13);
			w.write(str);
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
				
		// Line 18 - short title - free form text not including , " ( ) up to 8 characters 
		try{
			String str = s.getDisplayTitle().replace(",", ";").replace("\"", "'");
			if (str.length()>8) str = str.substring(0,7);
			w.write(str);
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
				
		// Line 19 - period - single character; C = modern, P = post medieval, M = medieval, S = Saxon, R = Roman, 
		// A = pre Roman, 2 = duplicate e.g. repeat measure, B = multiperiod e.g. long master, ? = unknown 	M
		try{
			w.write("?");
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
				
		// Line 20 - ITRDB species code - 4 character code refer to ITRDB species codes
		// TODO Cross map required to implement
		try{
			w.write("?");
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
						
		// Line 21 - Interpretation and anatomical notes - up to 64 characters - ? =no interpretation/notes
		//	       the interpretation and the anatomical notes can be in any order but each must consist of three parts, 
		//         a single character A or I for anatomy or interpretation, a separator ~, for interpretations the date 
		//         of the start, for anatomy the ringno, a separator ~, for anatomy the anatomical code for interpretations 
		//         P for plus, 0 for felled and a number for the length of the range, where more than one record is present 
		//         these are separated by ~, there must not be a terminal separator and each record must consist of the tree 
		//         parts. The anatomical codings can be anything of a single character but supported usage is based on 
		//         Hans-Hubert Leuschners anatomical codes; D = Density Band, R = Reaction Wood, L = Light Latewood, 
		//         H = Dense Latewood, F = Frost Ring, K = Small Earlywood Vessels - oak, G = Great Latewood Vessels - oak, 
		//         T = Wound Tissue, N = Narrow Latewood, A = Light Latewood End, P = Narrow and Light Latewood, 
		//         Q = Narrow and Dense Latewood
		// TODO Not implemented yet
		try{
			w.write("?");
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
		
		// Line 22 - data type - single character; D = ring widths, E = early-wood widths only, L = late-wood widths only, 
		//         R = late+early wood widths (i.e. reverse of normal rings), I = minimum density, A = maximum density, 
		//         S = early, late; (i.e. sequentially & separately), M = mixed (?means of others) 	D
		// TODO Other data types not supported yet
		try{
			w.write("D");
			w.newLine();
		} catch (NullPointerException e){ 
			w.write("ERROR"); 
			w.newLine();
		}
				
		
		// ****
		// DATA
		// ****
		
		// Line 23+ - for each width (equivalent to the value of length) the individual increments etc. no negatives or 0's
		// TODO Handle units
		for (int i = 0; i < s.getData().size(); i++) {
			String datum = s.getData().get(i).toString();
			
			// Error check		
			if (datum.equals("0"))
				datum = "?"; // DOCUMENT me!
			// (BUG: what if it's negative?)
			
			w.write(datum);
			w.newLine();
		}		
	
	}

	public Boolean isPackedFileCapable() {
		return false;
	}

	public String getDeficiencyDescription() {
		return "Support for the " + this.toString() + " file format is experimental so use at your own risk!  " +
	    "It uses unstructured keyword-value pairs for metadata with no data type " +
		"constraints.  Only the metadata that can be mapped to standard " +
		"fields will be exported so the additional Corina/TRiDaS metadata "+
		"fields will not be available.";
	}

	public Boolean isLossless() {
		return false;
	}
}
