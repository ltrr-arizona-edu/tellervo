
package edu.cornell.dendro.corina.print;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.Year;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
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
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.test.PrintReportFramework;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.editor.DecadalModel;

public class SeriesReport {

	Font docTypeFont = new Font(Font.HELVETICA, 20f, Font.BOLD);
	Font titleFont = new Font(Font.HELVETICA, 20f, Font.BOLD);
	Font subTitleFont = new Font(Font.HELVETICA, 14f);
	Font sectionFont = new Font(Font.HELVETICA, 14f, Font.BOLD);
	Font subSectionFont = new Font(Font.HELVETICA, 10f, Font.BOLD);
	Font bodyFont = new Font(Font.HELVETICA, 10f);
	Font tableHeaderFont = new Font(Font.HELVETICA, 10f, Font.BOLD);
	
	private Sample s = new Sample();
	private PdfContentByte cb;
	
	public SeriesReport(Sample s){
		this.s = s;
	}
		
	public void generateSeriesReport(String filename) {
	
		// Create a document-object
		Document document = new Document(PageSize.LETTER);
		try {

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(filename));
			document.open();
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Peter Brewer"); 
		    document.addSubject("Corina Series Report for " + s.getDisplayTitle()); 
		
			// Title Left		
			ColumnText ct = new ColumnText(cb);
			ct.setSimpleColumn(document.left(), document.top(10)-163, 283, document.top(10), 20, Element.ALIGN_LEFT);
			ct.addText(getTitlePDF());
			ct.go();
			
			// Barcode
			ColumnText ct2 = new ColumnText(cb);
			ct2.setSimpleColumn(284, document.top(10)-163, document.right(10), document.top(10), 20, Element.ALIGN_RIGHT);
			ct2.addElement(getBarCode());
			ct2.go();			
				
			// Timestamp
			ColumnText ct3 = new ColumnText(cb);
			ct3.setSimpleColumn(document.left(), document.top(10)-223, 283, document.top(10)-60, 20, Element.ALIGN_LEFT);
			ct3.setLeading(0, 1.2f);
			ct3.addText(getTimestampPDF());
			ct3.go();
			
			// Authorship
			ColumnText ct4 = new ColumnText(cb);
			ct4.setSimpleColumn(284, document.top(10)-223, document.right(10), document.top(10)-60, 20, Element.ALIGN_RIGHT);
			ct4.setLeading(0, 1.2f);
			ct4.addText(getAuthorshipPDF());
			ct4.go();			
			
			
			// Pad text
	        document.add(new Paragraph(" "));      
	        Paragraph p2 = new Paragraph();
	        p2.setSpacingBefore(100);
		    p2.setSpacingAfter(10);
		    p2.add(new Chunk(" ", bodyFont));  
	        document.add(new Paragraph(p2));
	        
	        // Ring width table
	        document.add(getRingWidthTable());
	        document.add(getParagraphSpace());	        
	        document.add(getSeriesComments());
	        document.add(getParagraphSpace());
	        document.add(getInterpretationPDF());
	        document.add(getParagraphSpace());	        
	        document.add(getWoodCompletenessPDF());
	        
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
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

		TridasObject tobj = s.getMeta(Metadata.OBJECT, TridasObject.class);
		
		p.add(new Chunk(tobj.getTitle(), subTitleFont));
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

		barcode.setX(0.8f);
		barcode.setSize(5f);
		barcode.setBarHeight(20f);
		barcode.setBaseline(-1f);	
		
		Image image = barcode.createImageWithBarcode(cb, null, null);
	
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
	 */
	private PdfPTable getRingWidthTable()
	{
		PdfPTable tbl = new PdfPTable(11);
		PdfPCell headerCell = new PdfPCell();
		DecadalModel model = new DecadalModel(s);
		Float lineWidth = new Float(0.05);
		Float headerLineWidth = new Float(0.8);
		
		tbl.setWidthPercentage(100f);
		
		int rows = model.getRowCount();
			
		// Do column headers
		headerCell.setPhrase(new Phrase("1/100th mm", tableHeaderFont));
		headerCell.setBorderWidthBottom(headerLineWidth);
		headerCell.setBorderWidthTop(headerLineWidth);
		headerCell.setBorderWidthLeft(headerLineWidth);
		headerCell.setBorderWidthRight(headerLineWidth);
		tbl.addCell(headerCell);
		for(int i=0; i<10; i++){

			headerCell.setPhrase(new Phrase(Integer.toString(i), tableHeaderFont));   
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setBorderWidthBottom(headerLineWidth);
			headerCell.setBorderWidthTop(headerLineWidth);
			headerCell.setBorderWidthLeft(lineWidth);
			headerCell.setBorderWidthRight(lineWidth);
			
			if(i==0) headerCell.setBorderWidthLeft(headerLineWidth);
			if(i==9) headerCell.setBorderWidthRight(headerLineWidth);
            tbl.addCell(headerCell);          
        }
		
		// Loop through rows
		for(int row =0; row < rows; row++)
		{	
			PdfPCell dataCell = new PdfPCell();
			// Loop through columns
			for(int col = 0; col < 11; col++) {
				Object value = model.getValueAt(row, col);
				Phrase cellPhrase;
				
				// Set value of cell
				if(value!=null){
					cellPhrase = new Phrase(value.toString(), getTableFont(col));
				}
				else{
					cellPhrase = new Phrase("");
				}					
				
				
				// Set border styles depending on where we are in the table
				
				// Defaults
				dataCell.setBorderWidthBottom(lineWidth);
				dataCell.setBorderWidthTop(lineWidth);
				dataCell.setBorderWidthLeft(lineWidth);
				dataCell.setBorderWidthRight(lineWidth);
				dataCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				
				// Row headers
				if(col==0)
				{	
					dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					dataCell.setBorderWidthLeft(headerLineWidth);
					dataCell.setBorderWidthRight(headerLineWidth);
				}				
				
				// First data column
				if(col==1)
				{
					dataCell.setBorderWidthLeft(headerLineWidth);
				}
				
				// Last data column
				if(col==10)
				{
					dataCell.setBorderWidthRight(headerLineWidth);
				}

				// Last row
				if(row==model.getRowCount()-1)
				{
					dataCell.setBorderWidthBottom(headerLineWidth);				
				}
				

				// Finally write phrase to cell and cell to table
				dataCell.setPhrase(cellPhrase);
	            tbl.addCell(dataCell);

	        }
			
		
		}
		
		return tbl;
		
	}
	
	private Paragraph getTimestampPDF()
	{
		// Set up calendar
		GregorianCalendar cal1 = new GregorianCalendar(
				s.getSeries().getCreatedTimestamp().getValue().getYear(),
				s.getSeries().getCreatedTimestamp().getValue().getMonth()-1,
				s.getSeries().getCreatedTimestamp().getValue().getDay(),
				s.getSeries().getCreatedTimestamp().getValue().getHour(),
				s.getSeries().getCreatedTimestamp().getValue().getMinute(),
				s.getSeries().getCreatedTimestamp().getValue().getSecond());	
		Date createdTimestamp = cal1.getTime(); 
		GregorianCalendar cal2 = new GregorianCalendar(
				s.getSeries().getLastModifiedTimestamp().getValue().getYear(),
				s.getSeries().getLastModifiedTimestamp().getValue().getMonth()-1,
				s.getSeries().getLastModifiedTimestamp().getValue().getDay(),
				s.getSeries().getLastModifiedTimestamp().getValue().getHour(),
				s.getSeries().getLastModifiedTimestamp().getValue().getMinute(),
				s.getSeries().getLastModifiedTimestamp().getValue().getSecond());	
		Date lastModifiedTimestamp = cal2.getTime(); 		
		cal1.setTime(cal1.getTime());
		cal2.setTime(cal2.getTime());		
		DateFormat df1 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
		
		Paragraph p = new Paragraph();

	
		p.add(new Chunk("Created: ", subSectionFont));
		p.add(new Chunk(df1.format(createdTimestamp), bodyFont));
		p.add(new Chunk("\nLast Modified: ", subSectionFont));
		p.add(new Chunk(df1.format(lastModifiedTimestamp), bodyFont));

		
		return p;
		
	}
	
	
	private Paragraph getAuthorshipPDF()
	{
		Paragraph p = new Paragraph();
	
		
		p.add(new Chunk("Measured by: ", subSectionFont));
		p.add(new Chunk("Peter Brewer", bodyFont));
		p.add(new Chunk("\nSupervised by: ", subSectionFont));
		p.add(new Chunk("Peter Kuniholm", bodyFont));

		
		return p;		
		
	}
	
	private  Font getTableFont(int col)
	{
		
		if (col==0)	{
			return tableHeaderFont;
		}
		else {
			return bodyFont;
		}
				
	}
	
	private Paragraph getSeriesComments()
	{
	
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		
		p.add(new Chunk("Comments: \n", subSectionFont));
		p.add(new Chunk(s.getSeries().getComments(), bodyFont));
	
		return p;		
	}
	
	private Paragraph getInterpretationPDF()
	{
	
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		Year firstyear = s.getSeries().getInterpretation().getFirstYear();
		Year sproutyear = s.getSeries().getInterpretation().getSproutYear();
		Year deathyear = s.getSeries().getInterpretation().getDeathYear();
		
		p.add(new Chunk("Interpretation:", subSectionFont));
		
		if(firstyear!=null){
			p.add(new Chunk("\n- The first ring of this series begins in ", bodyFont));
			if (firstyear.getCertainty()!=null){
				p.add(new Chunk(firstyear.getCertainty().toString().toLowerCase() + " ", bodyFont));
			}
			p.add(new Chunk(firstyear.getValue().toString() + firstyear.getSuffix().toString() + ". ", bodyFont));
		}
		
		if(sproutyear!=null && deathyear!=null){
			p.add(new Chunk("\n- This tree sprouted in ", bodyFont));
			if (sproutyear.getCertainty()!=null){
				p.add(new Chunk(sproutyear.getCertainty().toString().toLowerCase() + " ", bodyFont));
			}
			p.add(new Chunk(sproutyear.getValue().toString() + sproutyear.getSuffix().toString() +
				" and died in ", bodyFont));
			if (deathyear.getCertainty()!=null){
				p.add(new Chunk(deathyear.getCertainty().toString().toLowerCase() + " ", bodyFont));
			}
			p.add(new Chunk(deathyear.getValue().toString() + deathyear.getSuffix().toString() + ".\n", bodyFont));
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
	
	private Paragraph getWoodCompletenessPDF()
	{
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		
		p.add(new Chunk("Wood Completeness:\n", subSectionFont));
		p.add(new Chunk("- Pith is present\n", bodyFont));
		p.add(new Chunk("- Heartwood is incomplete (4 rings are missing based upon ring geometry)\n", bodyFont));
		p.add(new Chunk("- Sapwood is incomplete.  A total of 9 sapwood rings are present (3 are missing based on species average)\n", bodyFont));
		p.add(new Chunk("- Bark is absent\n", bodyFont));
		return p;
	}
	
	private Paragraph getParagraphSpace()
	{
		Paragraph p = new Paragraph();
		
		p.add(new Chunk(" "));
		return p;
	}
	
	public static void main(String[] args)
	{
		String domain = "dendro.cornell.edu/dev/";
		String measurementID = "02189be5-b19c-5dbd-9035-73ae8827dc7a";
		String filename = "output.pdf";
		
	    App.platform = new Platform();
	    App.platform.init();	    
		App.init(null, null);
		Sample samp = null;
		
		try {
			samp = PrintReportFramework.getSampleForID(domain, measurementID);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		
		SeriesReport report = new SeriesReport(samp); 
		report.generateSeriesReport(filename);
	}
	
}