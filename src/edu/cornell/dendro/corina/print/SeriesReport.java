
package edu.cornell.dendro.corina.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang.WordUtils;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.ComplexPresenceAbsence;
import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.PresenceAbsence;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLastRingUnderBark;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasRemark;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasWoodCompleteness;
import org.tridas.schema.Year;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.editor.DecadalModel;
import edu.cornell.dendro.corina.editor.WJTableModel;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.remarks.Remarks;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.util.pdf.PrintablePDF;
import edu.cornell.dendro.corina.util.test.PrintReportFramework;

public class SeriesReport extends ReportBase {
	
	/** A map for the lazy-loading of icons */
	private HashMap<String, Image> lazyIconMap;
	
	/** For showing an icon we don't have */
	private final String missingIconURL = Builder.getBodgeMissingIconURL(48);
	
	/** The list of icons to draw */
	private List<Icon> icons;

	
	private Sample s = new Sample();

	public SeriesReport(Sample s){
		this.s = s;
		
		// lazily load icons
	    lazyIconMap = new HashMap<String, Image>();
	}
		
	private void generateSeriesReport(OutputStream output) {
	
		
		try {
		
			PdfWriter writer = PdfWriter.getInstance(document, output);
			document.setPageSize(PageSize.LETTER);
			document.open();
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Peter Brewer"); 
		    document.addSubject("Corina Series Report for " + s.getDisplayTitle()); 
				   
			// Title Left		
			ColumnText ct = new ColumnText(cb);
			ct.setSimpleColumn(document.left(), document.top()-163, 283, document.top(), 20, Element.ALIGN_LEFT);
			ct.addText(getTitlePDF());
			ct.go();
			
			// Barcode
			ColumnText ct2 = new ColumnText(cb);
			ct2.setAlignment(Element.ALIGN_RIGHT);
			ct2.setSimpleColumn(324, document.top()-163, document.right(10), document.top(), 20, Element.ALIGN_RIGHT);
			ct2.addElement(getBarCode());
			ct2.go();			
				
			// Timestamp
			ColumnText ct3 = new ColumnText(cb);
			ct3.setSimpleColumn(document.left(), document.top()-223, 283, document.top()-60, 20, Element.ALIGN_LEFT);
			ct3.setLeading(0, 1.2f);
			ct3.addText(getTimestampPDF());
			ct3.go();
			
			// Authorship
			ColumnText ct4 = new ColumnText(cb);
			ct4.setSimpleColumn(284, document.top()-223, document.right(10), document.top()-60, 20, Element.ALIGN_RIGHT);
			ct4.setLeading(0, 1.2f);
			ct4.addText(getAuthorshipPDF());
			ct4.go();			
			
			
			// Pad text
	        document.add(new Paragraph(" "));      
	        Paragraph p2 = new Paragraph();
	        p2.setSpacingBefore(50);
		    p2.setSpacingAfter(10);
		    p2.add(new Chunk(" ", bodyFont));  
	        document.add(new Paragraph(p2));
	        
	        // Ring width table
	        getRingWidthTable();
	        document.add(getParagraphSpace());	
		    
		    if(s.getSeries() instanceof TridasMeasurementSeries)
		    {
		    	// MEASUREMENT SERIES
  
		    	//document.add(getRingRemarks());
		        document.add(getWoodCompletenessPDF());
		        document.add(getParagraphSpace());	
		    	document.add(getSeriesComments());
		        document.add(getParagraphSpace());
		        document.add(getInterpretationPDF());
		        document.add(getParagraphSpace());	        
		        document.add(getElementAndSampleInfo());
		    }
		    else
		    {
		    	// DERIVED SERIES
		        getWJTable();
		        document.add(getParagraphSpace());
		        document.add(getSeriesComments());
		        document.add(getParagraphSpace());
		        //document.add(getRingRemarks());
		       		       
				
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
	private Paragraph getTitlePDF()
	{
		Paragraph p = new Paragraph();
		
		p.add(new Chunk(s.getDisplayTitle()+"\n", titleFont));
	
		// Add object name if this is a mSeries
		if(s.getSeries() instanceof TridasMeasurementSeries)
		{
			TridasObject tobj = s.getMeta(Metadata.OBJECT, TridasObject.class);
		
			p.add(new Chunk(tobj.getTitle(), subTitleFont));
		}		
		return p;		
	}
	
	
	/**
	 * Create a series bar code for this series
	 * 
	 * @return Image 
	 */
	private Image getBarCode()
	{
		UUID uuid = UUID.fromString(s.getSeries().getIdentifier().getValue());
		LabBarcode barcode = new LabBarcode(LabBarcode.Type.SERIES, uuid);

		//barcode.setX(0.4f);
		//barcode.setSize(1f);
		//barcode.setBarHeight(10f);
		barcode.setBaseline(-1f);	
		//barcode.setFont(null);

		
		Image image = barcode.createImageWithBarcode(cb, null, null);
	
		image.setWidthPercentage(95);
		
		return image;
	
	}
	
	private Paragraph getDocTypePDF()
	{
		Paragraph p = new Paragraph();
				
		p.add(new Chunk("Measurement Series Report \n", docTypeFont));
		return p;
	
	}
	
		
	/**
	 * Get PdfPTable containing the ring width data for this series
	 * 
	 * @return PdfPTable
	 * @throws DocumentException 
	 */
	private void getRingWidthTable() throws DocumentException
	{
		
		try {
			getDataTable(false);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get PdfPTable containing weiserjahre data for this series
	 * 
	 * @return
	 * @throws DocumentException 
	 */
	private void getWJTable() throws DocumentException
	{
		try {
			getDataTable(true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private PdfPTable getTableKey() throws DocumentException, IOException, IOException
	{
		PdfPTable mainTable = new PdfPTable(12);
		float[] widths = {0.1f, 0.1f, 0.8f, 0.1f, 0.1f, 0.8f, 0.1f, 0.1f, 0.8f, 0.1f, 0.1f, 0.8f};
		mainTable.setWidths(widths);
		mainTable.setWidthPercentage(100);		
		
		DecadalModel model;
		model = new DecadalModel(s);
		int rows = model.getRowCount();
		List<TridasRemark> masterList = null;
	
		// Loop through rows
		for(int row =0; row < rows; row++)
		{				
			// Loop through columns
			for(int col = 0; col < 11; col++) 
			{
				edu.cornell.dendro.corina.Year year = model.getYear(row, col);
				List<TridasRemark> remarksList = null;
				remarksList = s.getRemarksForYear(year);
				
				// If masterlist is still null initialize it with this remarks list
				if(remarksList.size()>0 && masterList==null) masterList = remarksList;
				
				for(TridasRemark remark : remarksList)
				{
					if(!masterList.contains(remark)) masterList.add(remark);
				}
			}
		}
		
		for(TridasRemark remark: masterList)	
		{
			PdfPCell iconCell = new PdfPCell();
			PdfPCell equalsCell = new PdfPCell();
			PdfPCell descriptionCell = new PdfPCell();

			iconCell.setBorder(0);
			equalsCell.setBorder(0);
			descriptionCell.setBorder(0);


			iconCell.setVerticalAlignment(Element.ALIGN_TOP);
			equalsCell.setVerticalAlignment(Element.ALIGN_TOP);
			descriptionCell.setVerticalAlignment(Element.ALIGN_TOP);
			
			Image icon = null;
			String remarkStr = null;
			
			// Get actual icon (either tridas or corina)
			if(remark.isSetNormalTridas()) {
				remarkStr = remark.getNormalTridas().toString().toLowerCase();
				remarkStr = remarkStr.replace("_", " ");
				icon = getTridasIcon(remark.getNormalTridas());	
				if(icon==null) icon = Image.getInstance(missingIconURL);
			}
			else if(CORINA.equals(remark.getNormalStd())) {
				remarkStr = remark.getNormal();
				icon = getCorinaIcon(remark.getNormal());	
				if(icon==null) icon = Image.getInstance(missingIconURL);
			}
			
			
			iconCell.addElement(icon);
			equalsCell.addElement(new Phrase("=", tableBodyFont));
			descriptionCell.addElement(new Phrase(WordUtils.capitalize(remarkStr), tableBodyFont));
			
			mainTable.addCell(iconCell);
			mainTable.addCell(equalsCell);
			mainTable.addCell(descriptionCell);
		}

		// Pad out empty cells
		PdfPCell blankCell = new PdfPCell();
		blankCell.addElement(new Phrase(""));	
		blankCell.setBorder(0);
		for(int i=0; i<12; i++)
		{
			mainTable.addCell(blankCell);
		}
		
		
		return mainTable;
	}
	
	/**
	 * Get PdfPTable containing the ring width data for this series
	 * 
	 * @return PdfPTable
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private void getDataTable(Boolean wj) throws DocumentException, MalformedURLException, IOException
	{
		// THE actual table
		PdfPTable mainTable = new PdfPTable(11);
		// Cell for column headers
		PdfPCell colHeadCell = new PdfPCell();
		// Model for data
		DecadalModel model;
		// Flag to show if there are *any* ring remarks
		Boolean hasRemarks = false;

		mainTable.setWidthPercentage(100f);
		
		if(wj==true)
		{
			if(s.hasWeiserjahre()==true){
				model = new WJTableModel(s);
				document.add(new Chunk("Weiserjahre:", subSubSectionFont));
			}
			else{
				return;					
			}
		}
		else
		{
			model = new DecadalModel(s);
			document.add(new Chunk("Ring widths:", subSubSectionFont));
		}
		
		int rows = model.getRowCount();
				
		// Do column headers
		if(wj==true)
		{
			colHeadCell.setPhrase(new Phrase("inc/dec", tableHeaderFont));
		}
		else
		{
			colHeadCell.setPhrase(new Phrase("1/100th mm", tableHeaderFont));
		}
		colHeadCell.setBorderWidthBottom(headerLineWidth);
		colHeadCell.setBorderWidthTop(headerLineWidth);
		colHeadCell.setBorderWidthLeft(headerLineWidth);
		colHeadCell.setBorderWidthRight(headerLineWidth);
		mainTable.addCell(colHeadCell);
		for(int i=0; i<10; i++)
		{
			colHeadCell.setPhrase(new Phrase(Integer.toString(i), tableHeaderFont));   
			colHeadCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			colHeadCell.setBorderWidthBottom(headerLineWidth);
			colHeadCell.setBorderWidthTop(headerLineWidth);
			colHeadCell.setBorderWidthLeft(lineWidth);
			colHeadCell.setBorderWidthRight(lineWidth);
			
			if(i==0) colHeadCell.setBorderWidthLeft(headerLineWidth);
			if(i==9) colHeadCell.setBorderWidthRight(headerLineWidth);
            mainTable.addCell(colHeadCell);          
        }
		
		
		// Loop through rows
		for(int row =0; row < rows; row++)
		{				
			// Loop through columns
			for(int col = 0; col < 11; col++) 
			{
				// Mini table to hold remark icons
				PdfPTable remarksMiniTable = new PdfPTable(3);
				float[] widths = {0.3f, 0.3f, 0.6f};
				remarksMiniTable.setWidths(widths);
				remarksMiniTable.setWidthPercentage(100);			
				
				// Get ring value or year number for first column
				Phrase cellValuePhrase;
				Object value = model.getValueAt(row, col);
				if(value==null){
					cellValuePhrase = new Phrase("");
				}
				else
				{
					cellValuePhrase = new Phrase(value.toString(), getTableFont(col));
				}	
				
				
				// Get any remarks and compile them into a mini table
				edu.cornell.dendro.corina.Year year = model.getYear(row, col);
				List<TridasRemark> remarksList = null;
				remarksList = s.getRemarksForYear(year);
								
				// If there are remarks, cycle through them adding cells to the mini table
				if(col!=0 && remarksList.size()>0)
				{
					hasRemarks=true;
					// Get icons for remarks
					int cellnum = 1;
					int remarknum = 0;
					for(TridasRemark remark : remarksList)
					{
						// Keep track of which remark we are on.
						remarknum++;
						// String for holding remark name for debugging
						String remstr = "?";						
						// The actual remark icon
						Image icon = null;
						// A table cell for the remark
						PdfPCell remarkCell = new PdfPCell();

						// Set default attributes for remark and value cells
						remarkCell.setBorderWidthBottom(0);
						remarkCell.setBorderWidthTop(0);
						remarkCell.setBorderWidthLeft(0);
						remarkCell.setBorderWidthRight(0);
						remarkCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						remarkCell.setPadding(0);
						remarkCell.setUseBorderPadding(true);
						
						// A table cell for the ring width value
						PdfPCell valueCell = new PdfPCell();	
						valueCell = remarkCell;	

						// Get actual icon (either tridas or corina)
						if(remark.isSetNormalTridas()) {
							remstr = remark.getNormalTridas().toString();
							icon = getTridasIcon(remark.getNormalTridas());	
							if(icon==null) icon = Image.getInstance(missingIconURL);
						}
						else if(CORINA.equals(remark.getNormalStd())) {
							remstr = remark.getNormal();
							icon = getCorinaIcon(remark.getNormal());	
							if(icon==null) icon = Image.getInstance(missingIconURL);
						}

						// Print debug info for this remark
						String errStr = "Getting icon for "+remstr+" for year "+year.toString() + "(cell value = "+cellnum+")";
						System.out.print(errStr);
						
						// Shrink the icon a bit
						icon.scalePercent(20);

						// Add icon to minitable
						remarkCell.addElement(icon);
						remarksMiniTable.addCell(remarkCell);
						cellnum++;
						
						if (cellnum==1 && remarksList.size()<cellnum)
						{
							// First cell and no remark so print blank
							valueCell.setPhrase(new Phrase(""));
							remarksMiniTable.addCell(valueCell);
							cellnum++;
						}						
						if (cellnum==2 && remarksList.size()<cellnum)
						{
							// Second cell and no remark so print blank
							valueCell.setPhrase(new Phrase(""));
							remarksMiniTable.addCell(valueCell);
							cellnum++;
						}
						if(cellnum==3)
						{
							// In third cell so print value
							valueCell.setPhrase(cellValuePhrase);
							remarksMiniTable.addCell(valueCell);
							cellnum++;
						}
						else if (cellnum % 3 == 0)
						{
							// In third column so print blank
							valueCell.setPhrase(new Phrase(""));
							remarksMiniTable.addCell(valueCell);
							cellnum++;
						}
						
						if(remarknum==remarksList.size())
						{
							valueCell.setPhrase(new Phrase(""));
							remarksMiniTable.addCell(valueCell);
							remarksMiniTable.addCell(valueCell);
						}
						
						remarkCell = null;
						valueCell = null;
					}	
				}
				else
				{
					// No remarks so make mini table have blank, blank, value
					
					// Create blank and value cells
					PdfPCell blankCell = new PdfPCell();
					PdfPCell valueCell = new PdfPCell();
					
					// Set up style
					blankCell.setBorderWidthBottom(0);
					blankCell.setBorderWidthTop(0);
					blankCell.setBorderWidthLeft(0);
					blankCell.setBorderWidthRight(0);
					blankCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					blankCell.setPadding(0);
					blankCell.setUseBorderPadding(true);
					valueCell = blankCell;
					
					// Add cells to mini table
					remarksMiniTable.addCell(blankCell);
					remarksMiniTable.addCell(blankCell);
					valueCell.setPhrase(cellValuePhrase);
					remarksMiniTable.addCell(valueCell);
				}
				
				
				
				// Set border styles depending on where we are in the table
				
				// Defaults
				PdfPCell mainTableCell = new PdfPCell();
				mainTableCell.setBorderWidthBottom(lineWidth);
				mainTableCell.setBorderWidthTop(lineWidth);
				mainTableCell.setBorderWidthLeft(lineWidth);
				mainTableCell.setBorderWidthRight(lineWidth);
				mainTableCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				
				// Row headers
				if(col==0)
				{	
					mainTableCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					mainTableCell.setBorderWidthLeft(headerLineWidth);
					mainTableCell.setBorderWidthRight(headerLineWidth);
				}				
				
				// First data column
				if(col==1)
				{
					mainTableCell.setBorderWidthLeft(headerLineWidth);
				}
				
				// Last data column
				if(col==10)
				{
					mainTableCell.setBorderWidthRight(headerLineWidth);
				}

				// Last row
				if(row==model.getRowCount()-1)
				{
					mainTableCell.setBorderWidthBottom(headerLineWidth);				
				}
	
				// Write mini table to cell 		
				mainTableCell.addElement(remarksMiniTable);
				
				// Write cell to main table
	            mainTable.addCell(mainTableCell);

	        }
		}
		
		// Add table to document
		document.add(mainTable);
		
		if(!wj && hasRemarks)	document.add(getTableKey());
	}
		
	
	private Paragraph getRingRemarks(){
		
		Paragraph p = new Paragraph();
		//p.setLeading(0, 1.2f);
		
		DecadalModel model = new DecadalModel(s);
		float[] widths = {0.1f, 0.75f};
		PdfPTable tbl = new PdfPTable(widths);
		Boolean hasRemarks = false;	
		tbl.setWidthPercentage(100f);
	
		// Loop through rows
		int rows = model.getRowCount();
		for(int row =0; row < rows; row++)
		{	
			// Loop through columns
			for(int col = 0; col < 11; col++) {
				
				edu.cornell.dendro.corina.Year year = model.getYear(row, col);
				List<TridasRemark> remarks = s.getRemarksForYear(year);
				
				PdfPCell yearCell = new PdfPCell();
				PdfPCell remarkCell = new PdfPCell();
								
				// Extract remarks and compile into a string
				String remarkStr = "";
				for(TridasRemark remark : remarks) 
				{
					
					if (remark.getNormalTridas()!=null)
					{
						remarkStr += remark.getNormalTridas().value() + "\n";		
					}
					else if(remark.getNormal()!=null)
					{
						remarkStr += remark.getNormal().toString() + "\n";
					}
					else
					{
						remarkStr += remark.getValue().toString() + "\n";
					}
				}
				
				// Write to table
				if(remarks.isEmpty())
				{
					// Nothing to write
				}
				else
				{
					yearCell.setPhrase(new Phrase(year.toString(), tableHeaderFont));   
					yearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					yearCell.setBorderWidthBottom(0);
					yearCell.setBorderWidthTop(0);
					yearCell.setBorderWidthLeft(0);
					yearCell.setBorderWidthRight(lineWidth);
					tbl.addCell(yearCell);
					
					remarkCell.setPhrase(new Phrase(remarkStr, bodyFont));
					remarkCell.setPaddingLeft(5f);
					remarkCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					remarkCell.setBorderWidthBottom(0);
					remarkCell.setBorderWidthTop(0);
					remarkCell.setBorderWidthLeft(lineWidth);
					remarkCell.setBorderWidthRight(0);
					tbl.addCell(remarkCell);
					hasRemarks = true;
				}
			}
		}
		
		// Add to document
		if(hasRemarks){
			p.add(new Chunk("Ring remarks:", subSubSectionFont));
			p.add(tbl);
		}
		
		
		return p;
		
	}
	
	/**
	 * iText paragraph containing created and lastmodified timestamps
	 * 
	 * @return Paragraph
	 */
	private Paragraph getTimestampPDF()
	{
		// Set up calendar
		Date createdTimestamp = s.getSeries().getCreatedTimestamp().getValue()
				.toGregorianCalendar().getTime();
		Date lastModifiedTimestamp = s.getSeries().getLastModifiedTimestamp()
				.getValue().toGregorianCalendar().getTime();
		DateFormat df1 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
		
		Paragraph p = new Paragraph();

	
		p.add(new Chunk("Created: ", subSubSectionFont));
		p.add(new Chunk(df1.format(createdTimestamp), bodyFont));
		p.add(new Chunk("\nLast Modified: ", subSubSectionFont));
		p.add(new Chunk(df1.format(lastModifiedTimestamp), bodyFont));

		
		return p;
		
	}
	
	/**
	 * iText Paragraph containing the various authorship fields
	 * @return Paragraph
	 */
	private Paragraph getAuthorshipPDF()
	{
		Paragraph p = new Paragraph();
	
		ITridasSeries sss = s.getSeries();
		TridasMeasurementSeries mseries = null;
		TridasDerivedSeries dseries = null;
		
		if(sss instanceof TridasMeasurementSeries) 
		{ 
			mseries = (TridasMeasurementSeries) sss; 
			if(mseries.getAnalyst()!=null) {
			p.add(new Chunk("Measured by: ", subSubSectionFont));
			p.add(new Chunk(mseries.getAnalyst(), bodyFont));
			}
			
			if(mseries.getDendrochronologist()!=null){
			p.add(new Chunk("\nSupervised by: ", subSubSectionFont));
			p.add(new Chunk(mseries.getDendrochronologist(), bodyFont));
			}
		}
		else
		{
			dseries = (TridasDerivedSeries) sss;
			p.add(new Chunk("Created by: ", subSubSectionFont));
			p.add(new Chunk(dseries.getAuthor(), bodyFont));
			
		}
		
		return p;		
		
	}
	
	/**
	 * Get the font style to use in the table based on column number
	 * @param col column number
	 * @return Font
	 */
	private  Font getTableFont(int col)
	{
		if (col==0)	{
			return tableHeaderFont;
		}
		else {
			return tableBodyFont;
		}
				
	}
	
	private Paragraph getSeriesComments() 
	{
	

		Paragraph p = new Paragraph();
		
		if(s.getSeries().getComments()!=null){
			
			p.setLeading(0, 1.2f);
			p.add(new Chunk("Comments: \n", subSubSectionFont));
			p.add(new Chunk(s.getSeries().getComments(), bodyFont));
			return p;
		}
		else
		{
			return p;
		}
		
		
	}
	
	private Paragraph getInterpretationPDF()
	{
		
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		Year firstyear = s.getSeries().getInterpretation().getFirstYear();
		Year pithYear = s.getSeries().getInterpretation().getPithYear();
		Year deathyear = s.getSeries().getInterpretation().getDeathYear();
		Boolean isRelativelyDated = false;
		p.add(new Chunk("Interpretation:", subSubSectionFont));
		
		String datingType = s.getSeries().getInterpretation().getDating().getType().toString();
		
		if(datingType=="RELATIVE") isRelativelyDated = true;
		
		if(firstyear!=null){
			p.add(new Chunk("\n- The first ring of this series begins in ", bodyFont));
			if(isRelativelyDated) p.add(new Chunk("relative year ", bodyFont));
			
			if (firstyear.getCertainty()!=null){
				p.add(new Chunk(firstyear.getCertainty().toString().toLowerCase() + " ", bodyFont));
			}
			p.add(new Chunk(firstyear.getValue().toString(), bodyFont));
			if(isRelativelyDated==false) p.add(new Chunk(firstyear.getSuffix().toString(), bodyFont));
			p.add(new Chunk(".\n", bodyFont));
		}
		
		if(pithYear!=null && deathyear!=null){
			p.add(new Chunk("- The pith of this radius was laid down ", bodyFont));
			if (pithYear.getCertainty()!=null){
				p.add(certaintyToNaturalString(pithYear.getCertainty().toString()));
			}
			if(isRelativelyDated) p.add(new Chunk("relative year ", bodyFont));
			p.add(new Chunk(pithYear.getValue().toString(), bodyFont));
			if(isRelativelyDated==false) p.add(new Chunk(pithYear.getSuffix().toString(), bodyFont));
			p.add(new Chunk(" and died ", bodyFont));
			if (deathyear.getCertainty()!=null){
				p.add(certaintyToNaturalString(deathyear.getCertainty().toString()));
			}
			if(isRelativelyDated) p.add(new Chunk("relative year ", bodyFont));
			p.add(new Chunk(deathyear.getValue().toString(), bodyFont));
			if(isRelativelyDated==false) p.add(new Chunk(deathyear.getSuffix().toString(), bodyFont));
			p.add(new Chunk(".\n", bodyFont));
			
		}
		
		
		// Dated with...
		if(s.getSeries().getInterpretation().getDatingReference()!=null)
		{
			p.add(new Chunk("\n- This series was dated using series: " + s.getSeries().getInterpretation().getDatingReference().getLinkSeries().getIdentifier().getValue().toString(), bodyFont));		
		}
		
		// Provence...
		if(s.getSeries().getInterpretation().getProvenance()!=null)
		{	
			p.add(new Chunk("\n- Provenance: " + s.getSeries().getInterpretation().getProvenance().toString(), bodyFont));
		}
				
	
		return p;		
	}
	
	
	private Chunk certaintyToNaturalString(String certainty)
	{
		Chunk c = new Chunk();
		
		if(certainty.equalsIgnoreCase("exact")){
			c.append("in exactly ");
		}
		else if (certainty.equalsIgnoreCase("after")){
			c.append("after ");
		}
		else if (certainty.equalsIgnoreCase("before")){
			c.append("before ");
		}		
		else{
			c.append("in "+certainty.toLowerCase() + " ");
		}
		
		c.setFont(bodyFont);
		return c;
	}
	
	/**
	 * iText paragraph for the TRiDaS wood completeness fields
	 * 
	 * @return Paragraph
	 */
	private Paragraph getWoodCompletenessPDF()
	{
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		TridasRadius trad = s.getMeta(Metadata.RADIUS, TridasRadius.class);
		TridasWoodCompleteness woodCompleteness = trad.getWoodCompleteness();
		
		String pithPresence = null;
		String barkPresence = null;
		
		p.add(new Chunk("Wood Completeness:\n", subSubSectionFont));
		
		// Extract pith info
		if(woodCompleteness.getPith()!=null){
			
			if(woodCompleteness.getPith().getPresence()!=null){
				pithPresence = woodCompleteness.getPith().getPresence().value();
			}
			else{
				pithPresence = "not specified";
			}
		
			p.add(new Chunk("- Pith is "+ pithPresence +".\n", bodyFont));
		}
		else{
			p.add(new Chunk("- No pith details were recorded.\n", bodyFont));
		}
		
		// Ring count 
		p.add(new Chunk("- A total of "+ String.valueOf(s.countRings()) + " rings were measured.",bodyFont));
		
		// Unmeasured rings
		p.add(new Chunk(" Additional rings were observed but not measured ("+String.valueOf(woodCompleteness.getNrOfUnmeasuredInnerRings())+
				" towards the pith and " + String.valueOf(woodCompleteness.getNrOfUnmeasuredOuterRings()) + " towards the bark.",bodyFont));
		p.add(new Chunk("\n"));
				
		// Extract Heartwood and sapwood info
		p.add(getHeartSapwoodDetails(woodCompleteness, WoodType.HEARTWOOD));
		p.add(getHeartSapwoodDetails(woodCompleteness, WoodType.SAPWOOD));
				
		// Extract last ring under bark info
		if(woodCompleteness.getSapwood().getLastRingUnderBark()!=null)
		{
			TridasLastRingUnderBark lastRing = woodCompleteness.getSapwood().getLastRingUnderBark();			
			if(lastRing.getPresence()!=null){
				if(lastRing.getPresence().equals(PresenceAbsence.PRESENT))
				{
					p.add(new Chunk("- Last ring under bark is present",  bodyFont));
					
					if(lastRing.getContent()!=null)
					{
						p.add(new Chunk(" and the user has noted that it is: " + lastRing.getContent().toString() + ".\n", bodyFont));
					}
					else
					{
						p.add(new Chunk(".\n", bodyFont));
					}
								
				}
				else if (lastRing.getPresence().equals(PresenceAbsence.ABSENT))
				{
					p.add(new Chunk("- Last ring under bark is absent.\n",  bodyFont));
				}
			}
				
		}
		
		// Extract bark info
		if(woodCompleteness.getBark()!=null){			
			if(woodCompleteness.getBark().getPresence().toString().toLowerCase()!=null){			
				if(woodCompleteness.getBark().getPresence().value()=="present")
				{
					p.add(new Chunk("- Bark is present ", bodyFont));
					// Last ring info
					if(woodCompleteness.getSapwood().getLastRingUnderBark()!=null){
						p.add(new Chunk("and the last ring before the bark is noted as: \""+woodCompleteness.getSapwood().getLastRingUnderBark().getContent().toString() + "\"", bodyFont));
					}
					else
					{
						p.add(new Chunk("but no details about the last ring under the bark were recorded.\n", bodyFont));
					}
				}
				else if (woodCompleteness.getBark().getPresence().value()=="absent")
				{
					
					// Calculate if we have waney edge
					if(woodCompleteness.getSapwood().getPresence().equals(ComplexPresenceAbsence.COMPLETE))
					{
						p.add(new Chunk("- Waney edge present\n", bodyFont));
					}
					else
					{
						p.add(new Chunk("- Bark is absent.\n", bodyFont));
					}
				}
				else
				{
					barkPresence = woodCompleteness.getBark().getPresence().value();
					p.add(new Chunk("- Bark is " + barkPresence+ "\n", bodyFont));
				}	
			}
			else{
				p.add(new Chunk("- Bark information was not recorded.\n", bodyFont));
			}
		}
		

		return p;
	}
	
	
	private Paragraph getHeartSapwoodDetails(TridasWoodCompleteness woodCompleteness, WoodType type)
	{
		Paragraph p = new Paragraph();	

		String presence = null;
		String presenceStr = null;
		String missingRings = null;
		String missingRingsStr = null;
		String foundationStr = null;
		String woodTypeStr = null;
		String nrSapwoodRingsStr = null;
		Integer nrSapwoodRings = null;
		
		// Extract data from woodcompleteness based on type
		if(type==WoodType.HEARTWOOD)
		{
			// Extract Heartwood details
			woodTypeStr = "heartwood";
			if(woodCompleteness.getHeartwood()!=null){

				// Presence / Absence
				if(woodCompleteness.getHeartwood().getPresence()!=null){				
					if(woodCompleteness.getHeartwood().getPresence().equals(ComplexPresenceAbsence.UNKNOWN)){
						// if heartwood presence is unknown all other details are irrelevant
						p.add(new Chunk("- No " + woodTypeStr + " details recorded.", bodyFont));
						return p;
					}
					else{
						presence = woodCompleteness.getHeartwood().getPresence().value();
					}
				}

				// Missing rings
				if(woodCompleteness.getHeartwood().getMissingHeartwoodRingsToPith()!=null){
					missingRingsStr = woodCompleteness.getHeartwood().getMissingHeartwoodRingsToPith().toString();
					missingRings = missingRingsStr;
				}
				else{
					missingRingsStr = "an unknown number of";
				}
				
				// Missing rings foundation
				if(woodCompleteness.getHeartwood().getMissingHeartwoodRingsToPithFoundation()!=null){
					foundationStr = woodCompleteness.getHeartwood().getMissingHeartwoodRingsToPithFoundation().toString().toLowerCase();
				}
				else{
					foundationStr = "unspecified reasons";
				}
			
			}
			else{
				p.add(new Chunk("- No " + woodTypeStr + " details recorded.", bodyFont));
				return p;
			}	
		}
		else if (type==WoodType.SAPWOOD)
		{
			// Extract Sapwood details
			woodTypeStr = "sapwood";
			if(woodCompleteness.getSapwood()!=null){

				// Presence / Absence
				if(woodCompleteness.getSapwood().getPresence()!=null){
					if(woodCompleteness.getSapwood().getPresence().equals(ComplexPresenceAbsence.UNKNOWN)){
						// if sapwood presence is unknown all other details are irrelevant
						p.add(new Chunk("- No " + woodTypeStr + " details recorded.", bodyFont));
						return p;
					}			
					else {
						presence = woodCompleteness.getSapwood().getPresence().value();
					}
				}
				
				// Missing rings
				if(woodCompleteness.getSapwood().getMissingSapwoodRingsToBark()!=null){
					missingRingsStr = woodCompleteness.getSapwood().getMissingSapwoodRingsToBark().toString();
					missingRings = missingRingsStr;
				}
				else{
					missingRingsStr = "an unknown number of";
				}
				
				// No. of rings present
				if(woodCompleteness.getSapwood().getNrOfSapwoodRings()!=null){
					nrSapwoodRingsStr = woodCompleteness.getSapwood().getNrOfSapwoodRings().toString();
					nrSapwoodRings = woodCompleteness.getSapwood().getNrOfSapwoodRings();
				}
					
				// Missing rings foundation
				if(woodCompleteness.getSapwood().getMissingSapwoodRingsToBarkFoundation()!=null){
					foundationStr = woodCompleteness.getSapwood().getMissingSapwoodRingsToBarkFoundation().toString().toLowerCase();
				}
				else{
					foundationStr = "unspecified reasons";
				}		
			}
			else{
				p.add(new Chunk("- No " + woodTypeStr + " details recorded.", bodyFont));
				return p;
			}	
						
		}
		else
		{
			return null;
		}
		
		// Set output strings for presence/absence
		if(presence=="unknown"){
			//presenceStr = "- Whether " + woodTypeStr + " is present or not is unknown";
			presenceStr = "";
		}
		else if (presence == null)
		{
			//presenceStr = "- Presence of " + woodTypeStr + " has not been specified";
			presenceStr = "";
		}
		else{
			presenceStr = "- " + woodTypeStr.substring(0,1).toUpperCase() + woodTypeStr.substring(1) + " is " + presence;
			if (woodTypeStr == "sapwood" && nrSapwoodRings!=null)
			{
				presenceStr += ". A total of " + nrSapwoodRings + " sapwood rings were identified";
			}
		}
		
	
		// Compile paragraph	
		p.add(new Chunk(presenceStr, bodyFont));
		if(missingRings!=null){
			p.add(new Chunk(". The analyst records that "+ missingRingsStr, bodyFont));
			if (missingRingsStr.equals("1"))
			{
				p.add(new Chunk(" ring is ", bodyFont)); 
			}
			else
			{
				p.add(new Chunk(" rings are ", bodyFont));
			}
			p.add(new Chunk("missing, the justification of which is noted as: \"" + foundationStr + "\".", bodyFont));
		}
		else if (presence=="complete")
		{
			// Wood is complete so no mention required about missing rings
			p.add(new Chunk(". ", bodyFont));
		}
		else
		{
			//p.add(new Chunk(". Details about missing " + woodTypeStr + " rings was not recorded.", bodyFont));
			p.add(new Chunk("", bodyFont));
		}
		
		return p;
			
	}
	
	private enum WoodType {
	    HEARTWOOD, SAPWOOD 
	}
	
	/**
	 * iText paragraph of element and sample info
	 * @return Paragraph
	 */
	private Paragraph getElementAndSampleInfo()
	{
		Paragraph p = new Paragraph();
		
		TridasElement telem = s.getMeta(Metadata.ELEMENT, TridasElement.class);
		TridasSample tsamp = s.getMeta(Metadata.SAMPLE, TridasSample.class);
		
		p.add(new Chunk("Element and sample details:\n", subSubSectionFont));
		
		p.add(new Chunk("- Taxon:  ", bodyFont));
		p.add(new Chunk(telem.getTaxon().getNormal()+"\n", bodyFontItalic));
		p.add(new Chunk("- Element type: "+ telem.getType().getNormal()+"\n", bodyFont));
		p.add(new Chunk("- Sample type: "+ tsamp.getType().getNormal()+"\n", bodyFont));
		return p;
		
	}	
	
	/**
	 * Lazily-load this icon
	 * 
	 * @param iconName
	 * @return the icon, or null if iconName was null
	 */
	private Image lazyLoadIcon(String iconName) {
		if(iconName == null)
			return null;
		
		Image icon = lazyIconMap.get(iconName);
		if(icon == null) {
			// lazy-load the icon
			icon = Builder.getITextImageIcon(iconName);

			lazyIconMap.put(iconName, icon);
		}
		
		return icon;		
	}
	
	/**
	 * Get an icon for this tridas remark
	 * @param remark
	 * @return the icon, lazily loaded, or null
	 */
	private Image getTridasIcon(NormalTridasRemark remark) {
		return lazyLoadIcon(Remarks.getTridasRemarkIcons().get(remark));
	}
	
	/**
	 * Get an icon for this Corina remark (text)
	 * @param remark
	 * @return the icon, lazily loaded, or null
	 */
	private Image getCorinaIcon(String remark) {
		
		String iconName = Remarks.getCorinaRemarkIcons().get(remark);
		
		return lazyLoadIcon(iconName);
	}
	
	
	private final static String CORINA = "Corina";
	

	
	
	/**
	 * Function for printing or viewing series report
	 * @param printReport Boolean
	 * @param vmid String
	 */
	private static void getReport(Boolean printReport, String vmid)
	{
		String domain = "dendro.cornell.edu/dev/";
		Sample samp = null;
		
		try {
			samp = PrintReportFramework.getCorinaSampleFromVMID(domain, vmid);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		getReport(printReport, samp);
		
	}
	
	/**
	 * Function for printing or viewing series report
	 * @param printReport Boolean
	 * @param vmid String
	 */
	private static void getReport(Boolean printReport, Sample samp)
	{
		// create the series report
		SeriesReport report = new SeriesReport(samp);		
		
		if(printReport) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			report.generateSeriesReport(output);
			
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
				File outputFile = File.createTempFile("seriesreport", ".pdf");
				FileOutputStream output = new FileOutputStream(outputFile);
				
				report.generateSeriesReport(output);

				App.platform.openFile(outputFile);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				Alert.error("Error", "An error occurred while generating the series report.\n  See error log for further details.");
				return;
			}
		}	
	}
	

	/**
	 * Wrapper function to print report
	 *  
	 * @param vmid
	 */
	public static void printReport(Sample s)
	{
		getReport(true, s);	
	}
	
	/**
	 * Wrapper function to view report
	 * @param vmid
	 */
	public static void viewReport(Sample s)
	{
		getReport(false, s);
	}
	
	
}