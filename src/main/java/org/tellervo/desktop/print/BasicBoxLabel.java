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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.labelgen.LabBarcode;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.pdf.PrintablePDF;
import org.tellervo.desktop.util.test.PrintReportFramework;
import org.tellervo.schema.WSIBox;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class BasicBoxLabel extends ReportBase{
	
	private ArrayList<WSIBox> boxlist = new ArrayList<WSIBox>();

	public BasicBoxLabel(Sample s){
		
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
	
	public BasicBoxLabel(WSIBox b){
		if (b==null)
		{
			System.out.println("Error - box is null");
			return;
		}
		boxlist.add(b);
	}
		
	public BasicBoxLabel(ArrayList<WSIBox> bl)
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
		    document.addAuthor("Tellervo"); 
		    document.addSubject("Tellervo Box Labels"); 
				
		    PdfPTable table = new PdfPTable(2);
	        table.setTotalWidth(495f);
	        table.setLockedWidth(true);
	        
	        for(WSIBox b : boxlist)
	        {
		        Paragraph p = new Paragraph();
		        
		        p.add(new Chunk(b.getTitle()+Chunk.NEWLINE, labelTitleFont));
		        
		        p.add(new Chunk(Chunk.NEWLINE+" ", bodyFont));

		        //p.add(new Chunk(Chunk.NEWLINE+b.getComments()+Chunk.NEWLINE, bodyFont));
		        p.add(new Chunk(App.getLabName()+Chunk.NEWLINE+Chunk.NEWLINE, bodyFont));
		        p.add(new Chunk(this.getBarCode(b), 0, 0, true));
		        
		        PdfPCell cell = new PdfPCell(p);
		        cell.setPaddingLeft(15f);
		        cell.setPaddingRight(15f);
		        cell.setBorderColor(BaseColor.LIGHT_GRAY);

		        table.addCell(cell);

	        }
	
	        PdfPCell cell = new PdfPCell(new Paragraph());
	        cell.setBorderColor(BaseColor.LIGHT_GRAY);
	        
	        table.addCell(cell);
	        document.add(table);
	        document.close();
	        
	        
		    
		    
		    
		    
		    /*float top = document.top(15);
		    int row = 1;
		    
		    for(int i = 0; i< boxlist.size(); i = i+2)
		    {
		    	
		    	log.debug("Document left : "+document.left());
		    	log.debug("Document right: "+document.right());
		    	log.debug("Top           : "+top);
		    	
		    	
		    	
		    	
		    	
		    	
				// Column 1		
				ColumnText ct1a = new ColumnText(cb);
				ct1a.setSimpleColumn(document.left(), 
								   top-210, 
								   368, 
								   top, 
								   20, 
								   Element.ALIGN_LEFT);
				
				ColumnText ct1b = new ColumnText(cb);
				ct1b.setSimpleColumn(document.left(), 
						   top-70, 
						   document.left()+206, 
						   top-150, 
						   20, 
						   Element.ALIGN_LEFT);
				
				try{
			    	WSIBox b1 = boxlist.get(i);
			    	ct1a.addText(getTitlePDF(b1));
			    	ct1a.go();
					
			
			    	ct1b.addElement(getBarCode(b1));
			    	ct1b.go();

				} catch (Exception e)
				{
					log.debug("Failed writing box label in left column where i="+i);
				}
				
				
				// Column 2		
				ColumnText ct2a = new ColumnText(cb);
				ct2a.setSimpleColumn(306, 
								   top-210, 
								   document.right(), 
								   top, 
								   20, 
								   Element.ALIGN_LEFT);
				
				ColumnText ct2b = new ColumnText(cb);
				ct2b.setSimpleColumn(306, 
						   top-70, 
						   512,  
						   top-80, 
						   20, 
						   Element.ALIGN_LEFT);
				
				try{
			    	WSIBox b2 = boxlist.get(i+1);
			    	ct2a.addText(getTitlePDF(b2));
			    	ct2a.go();
					
			
			    	ct2b.addElement(getBarCode(b2));
			    	ct2b.go();

				} catch (Exception e)
				{
					log.debug("Failed writing box label in right column where i="+i);
					//e.printStackTrace();
				}
				
				
				// Column 2
			/*	ColumnText ct2 = new ColumnText(cb);
				ct2.setSimpleColumn(370,     //llx 
						top-100, 		     //lly	
						document.right(0),   //urx
						top+15, 		     //ury
						20, 			     //leading
						Element.ALIGN_RIGHT  //alignment
						);
				
				try{
				WSIBox b2 = boxlist.get(i+1);
				ct2.addText(getTitlePDF(b2));
				ct2.addElement(getBarCode(b2));
				ct2.go();
				} catch (Exception e)
				{
					log.debug("Failed writing box label where i="+i+1);
				}
				*/
				/*
				
				top = top-160;
				
				if(row==5)
				{
					top = document.top(15);
				    document.newPage();
				    row=1;
				}
				else
				{
					row++;
				}
				
				
				
				

		    }*/
		    
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}

		// Close the document
		document.close();
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

		barcode.setX(0.7f);
		//barcode.setN(0.5f);
		barcode.setSize(6f);
		barcode.setBaseline(8f);
		barcode.setBarHeight(50f);
		
		Image image = barcode.createImageWithBarcode(cb, null, null);
	
		return image;
	
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
				File outputFile = File.createTempFile("tellervolabel", ".pdf");
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
			samp = PrintReportFramework.getTellervoSampleFromVMID(domain, vmid);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// create the box label
		BasicBoxLabel label = new BasicBoxLabel(samp);		
		
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
				File outputFile = File.createTempFile("tellervolabel", ".pdf");
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
