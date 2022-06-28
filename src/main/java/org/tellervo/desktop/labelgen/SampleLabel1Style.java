package org.tellervo.desktop.labelgen;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.WSIBox;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class SampleLabel1Style extends AbstractTellervoLabelStyle {

		

		
		
		Font labelTitleFont = new Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD);
		Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10f);
		Font titleFont = new Font(Font.FontFamily.HELVETICA, 20f, Font.BOLD);
		Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14f);
		Font subSubSectionFont = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD);
		Font bodyFontLarge = new Font(Font.FontFamily.HELVETICA, 14f);
		Font tableHeaderFontLarge = new Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD);		
		Float headerLineWidth = new Float(0.8);	
		private Font labelfont = new Font(Font.FontFamily.HELVETICA, 15f, Font.BOLD);

		private Font tinyfont = new Font(Font.FontFamily.HELVETICA, 9f);

	    private final static Logger log = LoggerFactory.getLogger(SampleLabel1Style.class);

	
	public SampleLabel1Style() {
		super("Sample Label Style 1", "Thin sample samples designed to be printed on a full letter sized sheet.  Thin labels are then manually cut for applying to core mounts", ItemType.SAMPLE);
		
	}

	@Override
	public void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception {
		try {
			
			document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, output);
			
			document.setPageSize(PageSize.LETTER);
			document.open();
		
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Tellervo"); 
		    document.addSubject("Sample Label"); 
				
		    PdfPTable table = new PdfPTable(3); 
			
		    float colwidth[] = new float[3];
		    colwidth[0] = 180;
		    colwidth[1] = 180;
		    colwidth[2] = 180;
		    table.setTotalWidth(colwidth);
			//table.setWidths(colwidth);
			table.setLockedWidth(true);
			table.getDefaultCell().setPadding(0);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			
		    
		    for(Object item : items)
		    {
		    	TridasSample s = (TridasSample) item;
			    
						
					PdfPCell lbcell = new PdfPCell();
					lbcell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);
					
					lbcell.setVerticalAlignment(Element.ALIGN_TOP);
					lbcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					Phrase p = new Phrase();

					String labelText;
					TridasGenericField labcodeField = GenericFieldUtils.findField(s, "tellervo.internal.labcodeText");
					
					if(labcodeField==null)
					{
						log.warn("labcode missing from sample.  Can't print!");
						continue;
					}
					labelText = (labcodeField != null) ? labcodeField.getValue() : s.getTitle();
					
					p.add(new Chunk(labelText, labelfont));
					//p.add(new Chunk("bbb", labelfont));
					//p.add(new Chunk(s.getIdentifier().getValue().toString(), uuidfont));
					
					//barcode.setFont(null);
					
					Image img = LabBarcode.getSampleBarcode(s, cb, 20, barcodeSize);
							
					PdfPCell labcell = new PdfPCell(); 
					
					if(App.getLabName()!=null)
					{
						labcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						labcell.setVerticalAlignment(Element.ALIGN_TOP);
						Phrase labPhrase = new Phrase(App.getLabName().toUpperCase(), tinyfont);
						labcell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);
						labcell.addElement(labPhrase);
					}
					
					table.addCell(labcell);
					
					
					PdfPCell bccell = new PdfPCell();
					bccell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
					
					bccell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);
					bccell.addElement(img);
					//bccell.addElement(p);
					table.addCell(bccell);
					
					lbcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					lbcell.addElement(p);
					table.addCell(lbcell);
					
		    }
		   
		    document.add(table);
			
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
		
		p.add(new Chunk(b.getTitle()+"\n", titleFont));
		p.add(new Chunk(App.getLabName(), subTitleFont));
	
		//p.add(new Chunk(b.getCurationLocation(), bodyFontLarge));
				
		return p;		
	}
	
	
	
		
	/**
	 * Get PdfPTable containing the samples per object
	 * 
	 * @return PdfPTable
	 * @throws DocumentException 
	 */
	private void addTable(WSIBox b) throws DocumentException
	{
		float[] widths = {0.25f, 0.65f, 0.2f};
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
		HashMap<TridasIdentifier, TridasObject> objmap = new HashMap<TridasIdentifier, TridasObject>(); // Array of top level objects that have already been dealt with
		
		for(TridasObject myobj : obj)
		{
			
			if(myobj.isSetObjects())
			{
				objmap.put(myobj.getIdentifier(), myobj);
				
				for(TridasObject obj2 : myobj.getObjects())
				{
					objmap.put(obj2.getIdentifier(), obj2);
				}
				
			}
		}
		
		Collection<TridasObject> col = objmap.values();
		ArrayList<TridasObject> objlist = new ArrayList<TridasObject>(col);
		
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
				System.out.println("Error getting elements");
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
	 * Blank iText paragraph used for padding 
	 * @return Paragraph
	 */
	protected Paragraph getParagraphSpace()
	{
		Paragraph p = new Paragraph();
		
		p.add(new Chunk(" "));
		return p;
	}


}
