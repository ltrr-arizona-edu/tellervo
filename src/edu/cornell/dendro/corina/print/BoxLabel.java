
package edu.cornell.dendro.corina.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasRemark;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasWoodCompleteness;
import org.tridas.schema.Year;

import sun.text.CompactShortArray.Iterator;

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
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.remarks.Remark;
import edu.cornell.dendro.corina.remarks.Remarks;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.util.pdf.PrintablePDF;
import edu.cornell.dendro.corina.util.test.PrintReportFramework;

public class BoxLabel extends ReportBase{
	
	private Sample s = new Sample();

	
	public BoxLabel(Sample s){
		this.s = s;

	}
		
	public void generateBoxLabel(OutputStream output) {
	
		
		try {
		
			PdfWriter writer = PdfWriter.getInstance(document, output);
			
			document.setPageSize(PageSize.A5.rotate());
			document.open();
		
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Peter Brewer"); 
		    document.addSubject("Box Label"); 
				   
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
			
			// Pad text
	        document.add(new Paragraph(" "));      
	        Paragraph p2 = new Paragraph();
	        p2.setSpacingBefore(130);
		    p2.setSpacingAfter(10);
		    p2.add(new Chunk(" ", bodyFont));  
	        document.add(new Paragraph(p2));
	        
	        // Ring samples table
	        getTable();
	        document.add(getParagraphSpace());	
		    
	        getSeriesComments();	       		       
				
		    
			
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
		
		p.add(new Chunk("TU-123\n", monsterFont));
	
		p.add(new Chunk("Stack3, shelf 2, column 1", subTitleFont));
				
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
	
	
	/**
	 * Get PdfPTable containing the samples per object
	 * 
	 * @return PdfPTable
	 * @throws DocumentException 
	 */
	private void getTable() throws DocumentException
	{
		float[] widths = {0.1f, 0.75f, 0.1f};
		PdfPTable tbl = new PdfPTable(widths);
		PdfPCell headerCell = new PdfPCell();
		Integer totalSampleCount = 0;

		tbl.setWidthPercentage(100f);
				
		headerCell.setBorderWidthBottom(headerLineWidth);
		headerCell.setBorderWidthTop(headerLineWidth);
		headerCell.setBorderWidthLeft(0);
		headerCell.setBorderWidthRight(0);
		
		headerCell.setPhrase(new Phrase("Object", tableHeaderFont));
		headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		tbl.addCell(headerCell);
		headerCell.setPhrase(new Phrase("Samples", tableHeaderFont));
		headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		tbl.addCell(headerCell);
		headerCell.setPhrase(new Phrase("Total", tableHeaderFont));
		headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		tbl.addCell(headerCell);
		
		int rows = 2; // count of objects
		
		// Loop through rows
		for(int row =0; row < rows; row++)
		{	
			PdfPCell dataCell = new PdfPCell();
			
			dataCell.setBorderWidthBottom(0);
			dataCell.setBorderWidthTop(0);
			dataCell.setBorderWidthLeft(0);
			dataCell.setBorderWidthRight(0);
			dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			
			dataCell.setPhrase(new Phrase("ABC", bodyFont));
			tbl.addCell(dataCell);
		
			int sampleCount = 7; // count of samples
				
			String cellStr = "";
			for(int sampNum = 0; sampNum < sampleCount; sampNum++) {				
				cellStr += sampNum + ", ";
			}
			
			dataCell.setPhrase(new Phrase(cellStr, bodyFont));
			tbl.addCell(dataCell);
			
			dataCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataCell.setPhrase(new Phrase(Integer.toString(sampleCount), bodyFont));
			tbl.addCell(dataCell);
			totalSampleCount = totalSampleCount+sampleCount;
		}		
		
		headerCell.setBorderWidthBottom(headerLineWidth);
		headerCell.setBorderWidthTop(headerLineWidth);
		headerCell.setBorderWidthLeft(0);
		headerCell.setBorderWidthRight(0);
		
		headerCell.setPhrase(new Phrase(" ", bodyFont));
		tbl.addCell(headerCell);	
		headerCell.setPhrase(new Phrase("Grand Total", bodyFont));
		headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tbl.addCell(headerCell);	
		headerCell.setPhrase(new Phrase(Integer.toString(totalSampleCount), bodyFont));
		tbl.addCell(headerCell);
		
		// Add table to document
		document.add(tbl);		
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

	
		p.add(new Chunk("Created: ", subSectionFont));
		p.add(new Chunk(df1.format(createdTimestamp), bodyFont));
		p.add(new Chunk("\nLast Modified: ", subSectionFont));
		p.add(new Chunk(df1.format(lastModifiedTimestamp), bodyFont));

		
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
			return bodyFont;
		}
				
	}
	
	private void getSeriesComments() throws DocumentException
	{
	
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		
		p.add(new Chunk("Comments: \n", subSectionFont));
		if(s.getSeries().getComments()!=null){
			p.add(new Chunk(s.getSeries().getComments(), bodyFont));
		}
		else{
			p.add(new Chunk("No comments recorded", bodyFont));
		}
		
		document.add(p);
	}
	

	/**
	 * Function for printing or viewing series report
	 * @param printReport Boolean
	 * @param vmid String
	 */
	public static void getLabel(Boolean printReport, String vmid)
	{
		
		String domain = "dendro.cornell.edu/dev/";
		Sample samp = null;
		
		try {
			samp = PrintReportFramework.getSampleForID(domain, vmid);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// create the box label
		BoxLabel label = new BoxLabel(samp);		
		
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
				File outputFile = File.createTempFile("seriesreport", ".pdf");
				FileOutputStream output = new FileOutputStream(outputFile);
				
				label.generateBoxLabel(output);

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
	public static void printLabel(String vmid)
	{
		getLabel(true, vmid);	
	}
	
	/**
	 * Wrapper function to view report
	 * @param vmid
	 */
	public static void viewLabel(String vmid)
	{
		getLabel(false, vmid);
	}
	
	
	public static void main(String[] args) {
		String domain = "dendro.cornell.edu/dev/";
		String measurementID = "02189be5-b19c-5dbd-9035-73ae8827dc7a";
		String filename = "output.pdf";
		
	    App.platform = new Platform();
	    App.platform.init();	    
		App.init(null, null);
		Sample samp = null;
		
		BoxLabel label = new BoxLabel(samp);
		label.getLabel(false, measurementID);
		
	}
	
}