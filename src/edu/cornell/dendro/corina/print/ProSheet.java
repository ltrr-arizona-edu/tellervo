
package edu.cornell.dendro.corina.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.table.AbstractTableModel;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.BaseSeries;
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
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.util.ArrayListModel;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.util.pdf.PrintablePDF;
import edu.cornell.dendro.corina.util.test.PrintReportFramework;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntitySearchResource;
import edu.cornell.dendro.corina.wsi.corina.resources.SeriesSearchResource;


public class ProSheet extends ReportBase {
	
	private TridasObject o = new TridasObject();
	private ArrayListModel<edu.cornell.dendro.corina.sample.Element> elements;
	
	public ProSheet(TridasObject o, TridasDerivedSeries master, ArrayListModel<edu.cornell.dendro.corina.sample.Element> elements){
		
		// Find all series for an object 
    	SearchParameters sampparam = new SearchParameters(SearchReturnObject.OBJECT);
    	sampparam.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, o.getIdentifier().getValue().toString());

    	// we want a series returned here    	
		EntitySearchResource<TridasObject> searchResource = new EntitySearchResource<TridasObject>(sampparam);
		searchResource.query();	
	
		List<TridasObject> objlist = searchResource.getAssociatedResult();

		if(objlist.size()>0)
		{
			this.o = objlist.get(0);
		}
		this.elements = elements;
	}
		
	private void generateProSheet(OutputStream output) {
	
		
		try {
		
			PdfWriter writer = PdfWriter.getInstance(document, output);
			document.setPageSize(PageSize.LETTER);
			document.open();
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Peter Brewer"); 
		    document.addSubject("Corina Provenience Sheet for " + o.getTitle()); 
				   
			// Title Left		
			ColumnText ct = new ColumnText(cb);
			ct.setSimpleColumn(document.left(), document.top()-163, 400, document.top(), 20, Element.ALIGN_LEFT);
			ct.addText(getTitlePDF());
			ct.go();
						
				
			// Timestamp
			ColumnText ct3 = new ColumnText(cb);
			ct3.setSimpleColumn(document.left(), document.top()-223, 283, document.top()-60, 20, Element.ALIGN_LEFT);
			ct3.setLeading(0, 1.2f);
			ct3.addText(getTimestampPDF());
			ct3.go();
			
		
			
			
			// Pad text
	        document.add(new Paragraph(" "));      
	        Paragraph p2 = new Paragraph();
	        p2.setSpacingBefore(50);
		    p2.setSpacingAfter(10);
		    p2.add(new Chunk(" ", bodyFont));  
	        document.add(new Paragraph(p2));

	        document.add(getObjectDescription());
	        document.add(getObjectComments());
		    
	        document.add(new Paragraph(" ")); 
	        p2.setSpacingBefore(10);
		    p2.setSpacingAfter(10);
		    p2.add(new Chunk(" ", bodyFont));  
	        document.add(new Paragraph(p2));
	        
	        getElementTable();

			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}

		// Close the document
		document.close();
	}
	
	/**
	 * Get PdfPTable containing the ring width data for this series
	 * 
	 * @return PdfPTable
	 * @throws DocumentException 
	 */
	private void getElementTable() throws DocumentException
	{
		
		PdfPTable tbl = new PdfPTable(4);
		PdfPCell headerCell = new PdfPCell();

		tbl.setWidthPercentage(100f);
		float[] widths = {0.15f, 0.55f, 0.1f, 0.2f};
		
		tbl.setWidths(widths);

		// Set up header
		headerCell.setPhrase(new Phrase("Element", tableHeaderFont));
		headerCell.setBorderWidthBottom(headerLineWidth);
		headerCell.setBorderWidthTop(headerLineWidth);
		headerCell.setBorderWidthLeft(0);
		headerCell.setBorderWidthRight(0);
		tbl.addCell(headerCell);
		
		headerCell.setPhrase(new Phrase("Comments", tableHeaderFont));
		tbl.addCell(headerCell);
		
		headerCell.setPhrase(new Phrase("# Rings", tableHeaderFont));
		tbl.addCell(headerCell);
		
		headerCell.setPhrase(new Phrase("Dates", tableHeaderFont));
		tbl.addCell(headerCell);		
				
		
		// Loop through rows
		for(edu.cornell.dendro.corina.sample.Element e: this.elements)
		{	
			PdfPCell dataCell = new PdfPCell();
			dataCell.setBorderWidthBottom(0);
			dataCell.setBorderWidthTop(0);
			dataCell.setBorderWidthLeft(0);
			dataCell.setBorderWidthRight(0);
			dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
							
			dataCell.setPhrase(new Phrase("ABC-1", tableBodyFont));		
            tbl.addCell(dataCell);
            
			dataCell.setPhrase(new Phrase("Some info about sample", tableBodyFont));		
            tbl.addCell(dataCell);
            
            dataCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataCell.setPhrase(new Phrase("210", tableBodyFont));		
            tbl.addCell(dataCell);
            
            dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataCell.setPhrase(new Phrase("930BC - 101AD", tableBodyFont));		
            tbl.addCell(dataCell);
            
	
	        
			
		
		}
		
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

		Date now = new Date();
		
		DateFormat df1 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
		
		Paragraph p = new Paragraph();

		p.add(new Chunk("Printed: ", subSubSectionFont));
		p.add(new Chunk(df1.format(now), bodyFont));
		
		return p;
		
	}
	
	/**
	 * Get an iText Paragraph for the Title 
	 * 
	 * @return Paragraph
	 */
	private Paragraph getTitlePDF()
	{
		Paragraph p = new Paragraph();
		
		p.add(new Chunk(o.getTitle()+"\n", titleFont));

		//p.add(new Chunk(i.getCode(), subTitleFont));
				
		return p;		
	}
	
	
	
	
	private Paragraph getObjectComments() 
	{
	
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		
		p.add(new Chunk("Notes: ", bodyFontItalic));
		if(o.getComments()!=null){
			p.add(new Chunk(o.getComments(), bodyFontItalic));
		}
		else{
			p.add(new Chunk("No comments recorded", bodyFont));
		}
		
		return p;
	}
	
	private Paragraph getObjectDescription() 
	{
	
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		
		
		if(o.getDescription()!=null){
			p.add(new Chunk(o.getDescription(), bodyFont));
		}
		else{
			p.add(new Chunk("No description recorded", bodyFont));
		}
		
		return p;
	}

	
	/**
	 * Function for printing or viewing series report
	 * @param printReport Boolean
	 * @param vmid String
	 */
	private static void getReport(Boolean printReport, String objid)
	{
	/*	String domain = "dendro.cornell.edu/dev/";
		TridasObject obj = null;
		
		obj = PrintReportFramework.getCorinaObjectFromID(domain, objid);
		
		getReport(printReport, obj);
		*/
		
	}
	
	/**
	 * Function for printing or viewing series report
	 * @param printReport Boolean
	 * @param vmid String
	 */
	private static void getReport(Boolean printReport, TridasObject obj, TridasDerivedSeries master, ArrayListModel<edu.cornell.dendro.corina.sample.Element> elements)
	{
		// create the series report
		ProSheet report = new ProSheet(obj, master, elements);		
		
		if(printReport) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			report.generateProSheet(output);
			
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
				File outputFile = File.createTempFile("pro-", ".pdf");
				FileOutputStream output = new FileOutputStream(outputFile);
				
				report.generateProSheet(output);

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
	public static void printReport(TridasObject obj, ArrayListModel<edu.cornell.dendro.corina.sample.Element> series)
	{
		TridasDerivedSeries master = new TridasDerivedSeries();
		getReport(true, obj, master, series);	
	}
	
	/**
	 * Wrapper function to view report
	 * @param vmid
	 */
	public static void viewReport(TridasObject obj, ArrayListModel<edu.cornell.dendro.corina.sample.Element> series)
	{
		
		TridasDerivedSeries master = new TridasDerivedSeries();
		
		getReport(false, obj, master, series);
	}
	
	
}