package org.tellervo.desktop.labelgen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.JoinOperator;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.WSIBox;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class BoxLabel2Style extends AbstractTellervoLabelStyle {

		
    private final static Logger log = LoggerFactory.getLogger(BoxLabel2Style.class);

		
		
		Font labelTitleFont = new Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD);
		Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10f);
		Font titleFont = new Font(Font.FontFamily.HELVETICA, 40f, Font.BOLD);
		Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14f);
		Font subSubSectionFont = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD);
		Font bodyFontLarge = new Font(Font.FontFamily.HELVETICA, 14f);
		Font tableHeaderFontLarge = new Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD);		
		Float headerLineWidth = new Float(0.8);	
		
	
	public BoxLabel2Style() {
		super("Box label - full summary style 2", "Box label with full summary of contents.  One landscape label per letter sized sheet", ItemType.BOX);
		
	}

	@Override
	public void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception {
		try {
			document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, output);
	       
			document.setPageSize(PageSize.LETTER.rotate());			
			document.open();
		
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Peter Brewer"); 
		    document.addSubject("Box Label"); 
				
		    for(Object item : items)
		    {
		    	WSIBox b = (WSIBox) item;
			    
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
					System.out.println("Error getting objects");
					continue;
				}
				List<TridasObject> objList = objresource.getAssociatedResult();

				log.debug("Object found count: "+objList.size());
				
				for(TridasObject o : objList)
				{
					log.debug("  - "+o.getTitle() + " - " +TridasUtils.getGenericFieldValueByName(o, "tellervo.objectLabCode"));
				}
				
		    	
		    	
				// Title Left		
				ColumnText ct = new ColumnText(cb);
				ct.setSimpleColumn(document.left(), document.top(15)-210, 368, document.top(15), 45f, Element.ALIGN_LEFT);
				ct.addText(getTitleParagraph(b));
				
				ct.go();
				
		        // Table
				ColumnText ct2 = new ColumnText(cb);
				ct2.setSimpleColumn(370, document.top(15)-100, document.right(0), document.top(0), 20, Element.ALIGN_RIGHT);
				ct2.addElement( getTable(b, objList));
				ct2.go();			
					
				ColumnText ct3 = new ColumnText(cb);
				ct3.setSimpleColumn(500, document.bottom(0), document.right(0), document.bottom(0)+80, 20, Element.ALIGN_RIGHT);
				ct3.addElement(LabBarcode.getBoxBarcode(b, cb));
				ct3.go();
				
	
				
				// Pad text
		        document.add(new Paragraph(" "));      
		        Paragraph p2 = new Paragraph();
		        p2.setSpacingBefore(70);
			    p2.setSpacingAfter(10);
			    p2.add(new Chunk(" ", bodyFontLarge));  
		        document.add(new Paragraph(p2));

		        
		        ColumnText ct4 = new ColumnText(cb);
				ct4.setSimpleColumn(500, document.bottom(0), document.left(0), document.bottom(0)+80, 20, Element.ALIGN_RIGHT);
				ct4.addElement(getProjectParagraph(objList));
				ct4.go();

		        
		        
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
	private Paragraph getTitleParagraph(WSIBox b)
	{
		Paragraph p = new Paragraph(b.getTitle(), titleFont);
		p.setLeading(95f);
		//p.add(new Chunk(b.getTitle()+"\n", titleFont));
		//p.add(new Chunk(App.getLabName(), subTitleFont));
	
		//p.add(new Chunk(b.getCurationLocation(), bodyFontLarge));
				
		return p;		
	}
	
	
	
		
	/**
	 * Get PdfPTable containing the samples per object
	 * 
	 * @return PdfPTable
	 * @throws DocumentException 
	 */
	private PdfPTable getTable(WSIBox b, List<TridasObject> objList) throws DocumentException
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
		
							
	
		
		// Check that there are not too many objects to fit on box label
		if(objList.size()>10) 
		{
			System.out.println("Warning this label has " + Integer.toString(objList.size()) + " objects associated with it so is unlikely to fit and may take some time to produce!");
		}
		
		if(objList.size()==0)
		{
			Alert.error("No items", "This style of label includes details of contents but this box is empty!");
		}
		
		if(objList.size()<4)
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
		Collections.sort(objList, sorter);
		
		Integer sampleCountInBox = 0; 
		
		// Loop through objects
		HashMap<TridasIdentifier, TridasObject> objmap = new HashMap<TridasIdentifier, TridasObject>(); // Array of top level objects that have already been dealt with
		
		for(TridasObject myobj : objList)
		{
			
			if(myobj.isSetObjects())
			{
				objmap.put(myobj.getIdentifier(), myobj);
				
				for(TridasObject obj2 : myobj.getObjects())
				{
					objmap.put(obj2.getIdentifier(), obj2);
				}
				
			}
			else
			{
				objmap.put(myobj.getIdentifier(), myobj);
			}
		}
		
		Collection<TridasObject> col = objmap.values();
		ArrayList<TridasObject> objlist = new ArrayList<TridasObject>(col);
		
		Collections.sort(objlist, sorter);
		
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
				return null;
			}
			//XMLDebugView.showDialog();
			List<TridasElement> elements = resource.getAssociatedResult();
			
			if(elements==null || elements.size()==0)
			{
				log.error("No elements found for object "+objCode);
			}
			else
			{
				
			
				dataCell.setColspan(3);
				dataCell.setPhrase(new Phrase(myobj.getTitle(), bodyFont));
				tbl.addCell(dataCell);
				
				
				dataCell = new PdfPCell();
				dataCell.setBorderWidthBottom(0);
				dataCell.setBorderWidthTop(0);
				dataCell.setBorderWidthLeft(0);
				dataCell.setBorderWidthRight(0);
				dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
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
			
		}
		
		if(objList.size()<4)
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
		return tbl;		
	}
	
	
	/**
	 * Get a PDF paragraph summerizing the project information 
	 * 
	 * @param objList
	 * @return
	 */
	private Paragraph getProjectParagraph(List<TridasObject> objList)
	{
		if(objList==null || objList.size()==0) return null;
		
		SearchParameters sp = new SearchParameters(SearchReturnObject.PROJECT);
		sp.setJoinOperator(JoinOperator.OR);
		
		for(TridasObject o : objList)
		{
			sp.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, o.getIdentifier().getValue());	
		}
				
		EntitySearchResource<TridasProject> resource = new EntitySearchResource<TridasProject>(sp);
		
		//resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
		TellervoResourceAccessDialog dialog2 = new TellervoResourceAccessDialog(resource);
		resource.query();
		dialog2.setVisible(true);
		if(!dialog2.isSuccessful()) 
		{ 	
			System.out.println("Error getting project");
			return null;
		}
		//XMLDebugView.showDialog();
		List<TridasProject> projects = resource.getAssociatedResult();
		
		if(projects.size()>1)
		{
			log.debug("Box has more than one project in it");
		}
		else if (projects.size()==0)
		{
			log.debug("No project for box");
		}
		else
		{
			log.debug("Box has just one project in it");
		}
		
		
		
		Paragraph p = new Paragraph();
		p.setLeading(0.0f, 1.2f);
		
		p.add(new Chunk(projects.get(0).getTitle()+"\n", subTitleFont));
		p.add(new Chunk(projects.get(0).getInvestigator(), subTitleFont));
	
		//p.add(new Chunk(b.getCurationLocation(), bodyFontLarge));
				
		return p;		
	
		
		
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
				// Contains some chars so just add as comma delimited string to return string
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
