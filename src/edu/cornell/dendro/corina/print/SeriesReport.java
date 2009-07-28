
package edu.cornell.dendro.corina.print;

import java.io.FileOutputStream;
import java.io.IOException;

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
import com.lowagie.text.pdf.Barcode39;

import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.test.PrintReportFramework;

/**
 * Generates a simple 'Hello World' PDF file.
 * 
 * @author blowagie
 */

public class SeriesReport {

	static Font docTypeFont = new Font(Font.HELVETICA, 20, Font.BOLD);
	static Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
	static Font subTitleFont = new Font(Font.HELVETICA, 16);
	static Font sectionFont = new Font(Font.HELVETICA, Font.BOLD, 14);
	static Font subSectionFont = new Font(Font.HELVETICA, Font.ITALIC, 12);
	static Font bodyFont = new Font(Font.HELVETICA, 10);
	static Font tableHeaderFont = new Font(Font.HELVETICA, 10, Font.BOLD);
	static Sample testSample = new Sample();
	
	
	/**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {
				
		String domain = "dendro.cornell.edu/dev/";
		String measurementID = "02189be5-b19c-5dbd-9035-73ae8827dc7a";
		
		try {
			testSample = PrintReportFramework.getSampleForID(domain, measurementID);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		// Create a document-object
		Document document = new Document(PageSize.LETTER);
		try {

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("HelloWorld.pdf"));

			// Open doc and set basic metadata
			document.open();
		    document.addAuthor("Peter Brewer"); 
		    document.addSubject("Corina Test Report"); 
			HeaderFooter header = new HeaderFooter(new Phrase("This is a header."), false);
			document.setHeader(header);
			
			//Title Left
			PdfContentByte cb = writer.getDirectContent();
			ColumnText ct = new ColumnText(cb);
			ct.setSimpleColumn(document.left(), document.top(10)-163, 283, document.top(10), 20, Element.ALIGN_LEFT);
			ct.addText(getTitlePDF());
			ct.go();
			
			//TITLE Right
			ColumnText ct3 = new ColumnText(cb);
			ct3.setSimpleColumn(284, document.top(10)-163, document.right(10), document.top(10), 20, Element.ALIGN_RIGHT);
			ct3.addElement(getBarCode(writer));
			ct3.go();			
				
			// Dummy text
	        document.add(new Paragraph(" "));      
	        Paragraph p2 = new Paragraph();
	        p2.setSpacingBefore(90);
		    p2.setSpacingAfter(10);
		    p2.add(new Chunk("This text is in Times Roman. This is ZapfDingbats: ", new Font(Font.TIMES_ROMAN, 12)));    
	        p2.add(new Chunk("abcdefghijklmnopqrstuvwxyz", new Font(Font.ZAPFDINGBATS, 12)));
	        p2.add(new Chunk(". This is font Symbol: ", new Font(Font.TIMES_ROMAN, 12)));
	        p2.add(new Chunk("abcdefghijklmnopqrstuvwxyz", new Font(Font.SYMBOL, 12)));
	        document.add(new Paragraph(p2));
	        
	        // Ring width table
	        document.add(getRingWidthTable());
	        
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
	
	public static Paragraph getTitlePDF()
	{
		Paragraph p = new Paragraph();
		
		p.add(new Chunk(testSample.getDisplayTitle()+"\n", titleFont));

		p.add(new Chunk("Corinth, Acrocorinth", subTitleFont));
		return p;
		
	}
	
	public static Image getBarCode(PdfWriter writer)
	{
	
		PdfContentByte cb = writer.getDirectContent();
		Barcode39 code = new Barcode39();
		code.setCode("9780201615883");
		code.setX(2);
		Image image = code.createImageWithBarcode(cb, null, null);
	
		return image;
	
	}
	
	public static Paragraph getDocTypePDF()
	{
		Paragraph p = new Paragraph();
				
		p.add(new Chunk("Measurement Series Report \n", docTypeFont));
		return p;
	
	}
	
	public static PdfPTable getRingWidthTable()
	{
		PdfPTable tbl = new PdfPTable(11);
		PdfPCell cell = new PdfPCell();
		
		// Do column headers
		cell.setPhrase(new Phrase("1/100th mm", tableHeaderFont));
		cell.setBorderWidthBottom(1);
		cell.setBorderWidthTop(1);
		cell.setBorderWidthLeft(1);
		cell.setBorderWidthRight(1);
		tbl.addCell(cell);
		for(int i=0; i<10; i++){

			cell.setPhrase(new Phrase(Integer.toString(i), tableHeaderFont));   
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidthBottom(1);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(0.2f);
			cell.setBorderWidthRight(0.2f);
			
			if(i==0) cell.setBorderWidthLeft(1);
			if(i==9) cell.setBorderWidthRight(1);
            tbl.addCell(cell);          
        }
		
		// Loop through rows
		for(int r=1001; r<1060; r = r+10)
		{	
			cell.setPhrase(new Phrase(Integer.toString(r), tableHeaderFont));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorderWidthBottom(0.2f);
			cell.setBorderWidthTop(1);
			cell.setBorderWidthLeft(1);
			cell.setBorderWidthRight(1);
			if(r==1051) cell.setBorderWidthBottom(1);
			tbl.addCell(cell);	
			
			// Loop through columns
			for(int i=0; i<10; i++){
				cell.setBorderWidthBottom(0.2f);
				cell.setBorderWidthTop(0.2f);
				cell.setBorderWidthLeft(0.2f);
				cell.setBorderWidthRight(0.2f);
				cell.setPhrase(new Phrase(Integer.toString(i), bodyFont));   
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				if(i==0) cell.setBorderWidthLeft(1);
				if(i==9) cell.setBorderWidthRight(1);
				if(r==1051) cell.setBorderWidthBottom(1);
	            tbl.addCell(cell);            
	        }
			
		
		}
		
		return tbl;
		
	}
}