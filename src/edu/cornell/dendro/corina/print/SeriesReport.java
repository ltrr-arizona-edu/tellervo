
package edu.cornell.dendro.corina.print;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.tridas.schema.TridasObject;

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

	Font docTypeFont = new Font(Font.HELVETICA, 20, Font.BOLD);
	Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
	Font subTitleFont = new Font(Font.HELVETICA, 14);
	Font sectionFont = new Font(Font.HELVETICA, Font.BOLD, 14);
	Font subSectionFont = new Font(Font.HELVETICA, Font.ITALIC, 12);
	Font bodyFont = new Font(Font.HELVETICA, 10);
	Font tableHeaderFont = new Font(Font.HELVETICA, 10, Font.BOLD);
	
	
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
			
			// Title Right
			ColumnText ct3 = new ColumnText(cb);
			ct3.setSimpleColumn(284, document.top(10)-163, document.right(10), document.top(10), 20, Element.ALIGN_RIGHT);
			ct3.addElement(getBarCode());
			ct3.go();			
				
			// Pad text
	        document.add(new Paragraph(" "));      
	        Paragraph p2 = new Paragraph();
	        p2.setSpacingBefore(90);
		    p2.setSpacingAfter(10);
		    p2.add(new Chunk(" ", new Font(Font.TIMES_ROMAN, 12)));    
	        document.add(new Paragraph(p2));
	        
	        // Ring width table
	        document.add(getRingWidthTable());
	        
			
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
			
		barcode.setSize(6.0f);
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
		return null;
		
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