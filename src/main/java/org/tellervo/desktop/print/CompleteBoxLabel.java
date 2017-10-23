/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSIBox;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.labels.LabBarcode;
import org.tellervo.desktop.util.pdf.PrintablePDF;
import org.tellervo.desktop.util.test.PrintReportFramework;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


/**
 * Class for producing labels to fix to lab storage boxes.  Designed for 
 * Avery 6579 system labels 5 x 8 1/8" labels 2 per letter sized sheet
 * 
 * @author peterbrewer
 *
 */
public class CompleteBoxLabel extends ReportBase{
	
	private ArrayList<WSIBox> boxlist = new ArrayList<WSIBox>();
	
	public CompleteBoxLabel(Sample s){
		
		if(s==null)
		{
			System.out.println("Error - sample is null");
			return;
		}
		
		if (s.getMeta(Metadata.BOX, WSIBox.class)==null)
		{
			System.out.println("Error - no box associated with this series");
			return;
		}
		boxlist.add(s.getMeta(Metadata.BOX, WSIBox.class));
	}
	
	public CompleteBoxLabel(WSIBox b){
		if (b==null)
		{
			System.out.println("Error - box is null");
			return;
		}
		boxlist.add(b);
	}
		
	public CompleteBoxLabel(ArrayList<WSIBox> bl)
	{
		boxlist = bl;
	}
	
	public void generateBoxLabel(OutputStream output) {
	
		try {
		
			PdfWriter writer = PdfWriter.getInstance(document, output);
			
			document.setPageSize(PageSize.LETTER);
			document.open();
		
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Peter Brewer"); 
		    document.addSubject("Box Label"); 
				
		    for(WSIBox b : this.boxlist)
		    {
			    
				// Title Left		
				ColumnText ct = new ColumnText(cb);
				ct.setSimpleColumn(document.left(), document.top(15)-210, 368, document.top(15), 20, Element.ALIGN_LEFT);
				ct.addText(getTitlePDF(b));
				ct.go();
				
				// Barcode
				ColumnText ct2 = new ColumnText(cb);
				ct2.setSimpleColumn(370, document.top(15)-100, document.right(0), document.top(0), 20, Element.ALIGN_RIGHT);
				ct2.addElement(getBarCode(b));
				ct2.go();			
					
				// Timestamp
				ColumnText ct3 = new ColumnText(cb);
				ct3.setSimpleColumn(document.left(), document.top(15)-223, 350, document.top(15)-60, 20, Element.ALIGN_LEFT);
				ct3.setLeading(0, 1.2f);
				ct3.addText(getTimestampPDF(b));
				ct3.go();		
				
				// Pad text
		        document.add(new Paragraph(" "));      
		        Paragraph p2 = new Paragraph();
		        p2.setSpacingBefore(70);
			    p2.setSpacingAfter(10);
			    p2.add(new Chunk(" ", bodyFontLarge));  
		        document.add(new Paragraph(p2));
		        
		        // Ring samples table
		        addTable(b);
		        document.add(getParagraphSpace());	
			    
		        document.add(getComments(b));
		        
		        document.newPage();

		    }
		    
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}

		// Close the document
		document.close();
	}
	
	
	/**
	 * Get an iText Paragraph for the Title 
	 * 
	 * @return Paragraph
	 */
	private Paragraph getTitlePDF(WSIBox b)
	{
		Paragraph p = new Paragraph();
		
		p.add(new Chunk(b.getTitle()+"\n", monsterFont));
		p.add(new Chunk(App.getLabName(), subTitleFont));
	
		//p.add(new Chunk(b.getCurationLocation(), bodyFontLarge));
				
		return p;		
	}
	
	
	/**
	 * Create a series bar code for this series
	 * 
	 * @return Image 
	 */
	private Image getBarCode(WSIBox b)
	{
		UUID uuid = UUID.fromString(b.getIdentifier().getValue());
		LabBarcode barcode = new LabBarcode(LabBarcode.Type.BOX, uuid);

		//barcode.setX(1f);
		//barcode.setN(0.5f);
		barcode.setSize(5f);
		barcode.setBaseline(-1f);
		barcode.setBarHeight(60f);
		
		Image image = barcode.createImageWithBarcode(cb, null, null);
	
		return image;
	
	}
	
	
		
	/**
	 * Get PdfPTable containing the samples per object
	 * 
	 * @return PdfPTable
	 * @throws DocumentException 
	 */
	private void addTable(WSIBox b) throws DocumentException
	{
		float[] widths = {0.15f, 0.75f, 0.2f};
		PdfPTable tbl = new PdfPTable(widths);
		PdfPCell headerCell = new PdfPCell();

		tbl.setWidthPercentage(100f);

		// Write header cells of table
		headerCell.setBorderWidthBottom(headerLineWidth);
		headerCell.setBorderWidthTop(headerLineWidth);
		headerCell.setBorderWidthLeft(0);
		headerCell.setBorderWidthRight(0);		
		headerCell.setPhrase(new Phrase("Object", tableHeaderFontLarge));
		headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		tbl.addCell(headerCell);
		headerCell.setPhrase(new Phrase("Elements", tableHeaderFontLarge));
		headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		tbl.addCell(headerCell);
		headerCell.setPhrase(new Phrase("# Samples", tableHeaderFontLarge));
		headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tbl.addCell(headerCell);
		
							
		// Find all objects associated with samples in this box
		SearchParameters objparam = new SearchParameters(SearchReturnObject.OBJECT);
		objparam.addSearchConstraint(SearchParameterName.SAMPLEBOXID, SearchOperator.EQUALS, b.getIdentifier().getValue().toString());
		EntitySearchResource<TridasObject> objresource = new EntitySearchResource<TridasObject>(objparam);
		objresource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(objresource);
		
		objresource.query();	
		dialog.setVisible(true);
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting objects");
			return;
		}
		List<TridasObject> obj = objresource.getAssociatedResult();

		// Check that there are not too many objects to fit on box label
		if(obj.size()>10) 
		{
			System.out.println("Warning this label has " + Integer.toString(obj.size()) + " objects associated with it so is unlikely to fit and may take some time to produce!");
		}
		
		if(obj.size()<4)
		{
			// Not many objects so add some space to the table for prettiness sake
			headerCell.setBorder(0);
			headerCell.setPhrase(new Phrase(" "));
			tbl.addCell(headerCell);
			tbl.addCell(headerCell);
			tbl.addCell(headerCell);
		}
		
		// Sort objects into alphabetically order based on labcode
		TridasComparator sorter = new TridasComparator();
		Collections.sort(obj, sorter);
		
		Integer sampleCountInBox = 0; 
		
		// Loop through objects
		List<TridasObject> objlist = new ArrayList<TridasObject>(); // Array of top level objects that have already been dealt with
		
		for(TridasObject myobj : obj)
		{
			objlist.add(myobj);
			if(myobj.isSetObjects())
			{
				for(TridasObject obj2 : myobj.getObjects())
				{
					objlist.add(obj2);
				}
			}
		}
		
		Collections.sort(objlist, sorter);
		
		mainobjloop:
		for(TridasObject myobj : objlist)
		{	
			// Need to check if this object has already been done as there will be duplicate top level objects if there are samples 
			// from more than one subobject in the box 
			/*if(objdone.size()>0)
			{
				try{for(TridasObject tlo : objdone){
					TridasObjectEx tloex = (TridasObjectEx) tlo;
					TridasObjectEx myobjex = (TridasObjectEx) myobj;
					
					//if (tloex.getLabCode().compareTo(myobjex.getLabCode())==0){
						// Object already been done so skip to next
					//	continue mainobjloop;
					//}
					//else {
						// Object has not been done so add to the done list and keep going
						objdone.add(myobj);
					//}
				}} catch (Exception e){}
				
			}
			else
			{
				objdone.add(myobj);
			}*/

			// Add object code to first column			
			PdfPCell dataCell = new PdfPCell();
			dataCell.setBorderWidthBottom(0);
			dataCell.setBorderWidthTop(0);
			dataCell.setBorderWidthLeft(0);
			dataCell.setBorderWidthRight(0);
			dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			String objCode = null;
			
		
			
			if(myobj instanceof TridasObjectEx) objCode = ((TridasObjectEx)myobj).getMultiLevelLabCode(); 	

			// Search for elements associated with this object
			System.out.println("Starting search for elements associated with " + myobj.getTitle().toString());
			SearchParameters sp = new SearchParameters(SearchReturnObject.ELEMENT);		
			sp.addSearchConstraint(SearchParameterName.SAMPLEBOXID, SearchOperator.EQUALS, b.getIdentifier().getValue());
			sp.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, myobj.getIdentifier().getValue());
			EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(sp);
			resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
			TellervoResourceAccessDialog dialog2 = new TellervoResourceAccessDialog(resource);
			resource.query();
			dialog2.setVisible(true);
			if(!dialog2.isSuccessful()) 
			{ 	
				System.out.println("oopsey doopsey.  Error getting elements");
				return;
			}
			//XMLDebugView.showDialog();
			List<TridasElement> elements = resource.getAssociatedResult();
			
			if(elements==null || elements.size()==0) continue;
			
			dataCell.setPhrase(new Phrase(objCode, bodyFontLarge));
			tbl.addCell(dataCell);
			
			
			TridasComparator numSorter = new TridasComparator(TridasComparator.Type.TITLES, 
					TridasComparator.NullBehavior.NULLS_LAST, 
					TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
			Collections.sort(elements, numSorter);	
			
			// Loop through elements 
			Integer smpCnt = 0;
			ArrayList<String> numlist = new ArrayList<String>();
			for(TridasElement myelem : elements)
			{
				// Add element title to string
				if(myelem.getTitle()!=null) 
				{
					String mytitle = myelem.getTitle();
					numlist.add(mytitle);
				}

				// Grab associated samples and add count to running total
				List<TridasSample> samples = myelem.getSamples(); 
				smpCnt += samples.size();
			}
						
			
			// Add element names to second column
			dataCell.setPhrase(new Phrase(hyphenSummarize(numlist), bodyFontLarge));
			tbl.addCell(dataCell);
			
			// Add sample count to third column
			dataCell.setPhrase(new Phrase(smpCnt.toString(), bodyFontLarge));
			dataCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tbl.addCell(dataCell);
			
			sampleCountInBox += smpCnt;
			
		}
		
		if(obj.size()<4)
		{
			// Not many objects so add some space to the table for prettiness sake
			headerCell.setBorder(0);
			headerCell.setPhrase(new Phrase(" "));
			tbl.addCell(headerCell);
			tbl.addCell(headerCell);
			tbl.addCell(headerCell);
		}
				
		
		headerCell.setBorderWidthBottom(headerLineWidth);
		headerCell.setBorderWidthTop(headerLineWidth);
		headerCell.setBorderWidthLeft(0);
		headerCell.setBorderWidthRight(0);
		
		headerCell.setPhrase(new Phrase(" ", bodyFontLarge));
		tbl.addCell(headerCell);	
		headerCell.setPhrase(new Phrase("Grand Total", tableHeaderFontLarge));
		headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tbl.addCell(headerCell);	
		headerCell.setPhrase(new Phrase(sampleCountInBox.toString(), bodyFontLarge));
		tbl.addCell(headerCell);
		
		// Add table to document
		document.add(tbl);		
	}
			
	
	public String hyphenSummarize(List<String> lst)
	{

		String returnStr = "";
		
		/*
		if (none==true)
		{
			for(String item : lst)
			{
				returnStr += item + ", ";
				
			}
			
			if (returnStr.length()>2) returnStr = returnStr.substring(0, returnStr.length()-2);
			return returnStr;
		}*/


		Integer lastnum = null; 
		Boolean inSeq = false;
		Integer numOfElements = lst.size();
		Integer cnt = 0;
		for(String item : lst)
		{
			cnt++;
			if (containsOnlyNumbers(item))
			{
				// Contains only numbers
				if(lastnum==null)
				{
					// Lastnum is null so just add item to return string and continue to the next in list
					returnStr += ", " + item;
					lastnum = Integer.valueOf(item);
					continue;
				}
				else
				{
					if(inSeq==true)
					{
						if(cnt==numOfElements)
						{
							// This is the last one in the list!
							returnStr += "-" + item;
							continue;
						}
						else if(isNextInSeq(lastnum, Integer.valueOf(item)))
						{
							// Keep going!
							inSeq = true;
							lastnum = Integer.valueOf(item);
							continue;
						}
						else
						{
							// 
							returnStr += "-" + lastnum.toString() + ", " + item;
							inSeq = false;
							lastnum = Integer.valueOf(item);
							continue;
							
						}
					}
					else
					{
						// Not in sequence yet
						
						if(isNextInSeq(lastnum, Integer.valueOf(item)))
						{
							
							if(cnt==numOfElements)
							{
								// This is the last one in the list!
								returnStr += "-" + item;
								continue;
							}
							else
							{
								// Keep going!
								inSeq = true;
								lastnum = Integer.valueOf(item);
								continue;
							}
						}
						else
						{
							returnStr += ", " + item;
							lastnum = Integer.valueOf(item);
							continue;
						}
					}
						
					
				}
			}
			else
			{
				// Contains some chars so just add as comma delimated string to return string
				returnStr += ", " + item;
				lastnum = null;
				inSeq = null;
			}
			
			
			
			
		}
		
		returnStr = returnStr.substring(2, returnStr.length());
		return returnStr;
	}
	
	private Boolean isNextInSeq(Integer seqnum, Integer curnum)
	{
		if(seqnum+1==curnum)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
		
    /**
     * This method checks if a String contains only numbers
     */
    public boolean containsOnlyNumbers(String str) {
        
        //It can't contain only numbers if it's null or empty...
        if (str == null || str.length() == 0)
            return false;
        
        for (int i = 0; i < str.length(); i++) {

            //If we find a non-digit character we return false.
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        
        return true;
    }
	
	
	/**
	 * iText paragraph containing created and lastmodified timestamps
	 * 
	 * @return Paragraph
	 */
	private Paragraph getTimestampPDF(WSIBox b)
	{
		// Set up calendar
		Date createdTimestamp = b.getCreatedTimestamp().getValue()
				.toGregorianCalendar().getTime();
		Date nowTimestamp = new Date();
		
		DateFormat df1 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
		
		Paragraph p = new Paragraph();

	
		p.add(new Chunk("Created: ", subSubSectionFont));
		p.add(new Chunk(df1.format(createdTimestamp), bodyFont));
		//p.add(new Chunk("\nLast Modified: ", subSubSectionFont));
		//p.add(new Chunk(df1.format(lastModifiedTimestamp), bodyFontLarge));
		p.add(new Chunk("\nLabel updated: ", subSubSectionFont));
		p.add(new Chunk(df1.format(nowTimestamp), bodyFont));

		
		return p;
		
	}
	

	
	private Paragraph getComments(WSIBox b) throws DocumentException
	{
	
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		
		p.add(new Chunk("Comments: \n", subSubSectionFont));
		if(b.getComments()!=null){
			p.add(new Chunk(b.getComments(), bodyFont));
		}
		else{
			p.add(new Chunk("No comments recorded", bodyFont));
		}
		
		return(p);
	}

	/**
	 * Function for printing or viewing series report
	 * @param printReport Boolean
	 * @param vmid String
	 */
	public void getLabel(Boolean printReport)
	{
			
		if(printReport) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			this.generateBoxLabel(output);
			
			try {
				PrintablePDF pdf = PrintablePDF.fromByteArray(output.toByteArray());

				// true means show printer dialog, false means just print using the default printer
				pdf.print(true);
			} catch (Exception e) {
				e.printStackTrace();
				Alert.error("Printing error", "An error occured during printing.\n  See error log for further details.");
			}
		}
		else {
			// probably better to use a chooser dialog here...
			try {
				File outputFile = File.createTempFile("boxlabel", ".pdf");
				FileOutputStream output = new FileOutputStream(outputFile);
				
				this.generateBoxLabel(output);

				App.platform.openFile(outputFile);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				Alert.error("Error", "An error occurred while generating the box label.\n  See error log for further details.");
				return;
			}
		}
		
	}
	
	/**
	 * Function for printing or viewing series report
	 * @param printReport Boolean
	 * @param vmid String
	 */
	public static void getLabel(Boolean printReport, String vmid)
	{
		
		String domain = App.domain;
		Sample samp = null;
		
		try {
			samp = PrintReportFramework.getCorinaSampleFromVMID(domain, vmid);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// create the box label
		CompleteBoxLabel label = new CompleteBoxLabel(samp);		
		
		if(printReport) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			label.generateBoxLabel(output);
			
			try {
				PrintablePDF pdf = PrintablePDF.fromByteArray(output.toByteArray());

				// true means show printer dialog, false means just print using the default printer
				pdf.print(true);
			} catch (Exception e) {
				e.printStackTrace();
				Alert.error("Printing error", "An error occured during printing.\n  See error log for further details.");
			}
		}
		else {
			// probably better to use a chooser dialog here...
			try {
				File outputFile = File.createTempFile("boxlabel", ".pdf");
				FileOutputStream output = new FileOutputStream(outputFile);
				
				label.generateBoxLabel(output);

				App.platform.openFile(outputFile);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				Alert.error("Error", "An error occurred while generating the box label.\n  See error log for further details.");
				return;
			}
		}
		
	}
	

	
	
}
