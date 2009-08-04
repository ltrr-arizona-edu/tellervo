
package edu.cornell.dendro.corina.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
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
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.util.pdf.PrintablePDF;
import edu.cornell.dendro.corina.util.test.PrintReportFramework;

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
		
	public void generateSeriesReport(OutputStream output) {
	
		// Create a document-object
		Document document = new Document(PageSize.LETTER);
		try {

			PdfWriter writer = PdfWriter.getInstance(document, output);
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
	        document.add(getParagraphSpace());	
	        document.add(getElementAndSampleInfo());
			
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
			p.add(new Chunk("Measured by: ", subSectionFont));
			p.add(new Chunk(mseries.getAnalyst(), bodyFont));
			p.add(new Chunk("\nSupervised by: ", subSectionFont));
			p.add(new Chunk(mseries.getDendrochronologist(), bodyFont));			
		}
		else
		{
			dseries = (TridasDerivedSeries) sss;
			p.add(new Chunk("Created by: ", subSectionFont));
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
		String heartwoodPresence = null;
		String missingHeartwoodRings = null;
		String missingHeartwoodFoundation = null;
		String sapwoodPresence = null;
		String missingSapwoodRings = null;
		String missingSapwoodFoundation = null;
		
		p.add(new Chunk("Wood Completeness:\n", subSectionFont));
		
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
		
		// Extract Heartwood info
		if(woodCompleteness.getHeartwood()!=null){
			
			if(woodCompleteness.getHeartwood().getPresence()!=null){
				heartwoodPresence = woodCompleteness.getHeartwood().getPresence().value();
			}
			else{
				heartwoodPresence = "not specified";
			}
			
			if(woodCompleteness.getHeartwood().getMissingHeartwoodRingsToPith()!=null){
				missingHeartwoodRings = woodCompleteness.getHeartwood().getMissingHeartwoodRingsToPith().toString();
			}
			else{
				missingHeartwoodRings = "unknown number of";
			}
			
			if(woodCompleteness.getHeartwood().getMissingHeartwoodRingsToPithFoundation()!=null){
				missingHeartwoodFoundation = woodCompleteness.getHeartwood().getMissingHeartwoodRingsToPithFoundation().toString();
			}
			else{
				missingHeartwoodFoundation = "unspecified reasons";
			}
			
			p.add(new Chunk("- Heartwood is " + heartwoodPresence, bodyFont));
			if(woodCompleteness.getHeartwood().getMissingHeartwoodRingsToPith()!=null){
				p.add(new Chunk(" ("+ missingHeartwoodRings + " rings are missing based upon " + missingHeartwoodFoundation + ").\n", bodyFont));
			}
			else
			{
				p.add(new Chunk(" (Details on missing heartwood rings was not recorded.)\n", bodyFont));
			}
		}
		else{
			p.add(new Chunk("- No heartwood details recorded."));
		}
		
		// Extract Sapwood info
		if(woodCompleteness.getSapwood()!=null){
			
			if(woodCompleteness.getSapwood().getPresence()!=null){
				sapwoodPresence = woodCompleteness.getSapwood().getPresence().value();
			}
			else{
				sapwoodPresence = "not specified";
			}
			
			if(woodCompleteness.getSapwood().getMissingSapwoodRingsToBark()!=null){
				missingSapwoodRings = woodCompleteness.getSapwood().getMissingSapwoodRingsToBark().toString();
			}
			else{
				missingSapwoodRings = "unknown number of";
			}
			
			if(woodCompleteness.getSapwood().getMissingSapwoodRingsToBarkFoundation()!=null){
				missingSapwoodFoundation = woodCompleteness.getSapwood().getMissingSapwoodRingsToBarkFoundation().toString();
			}
			else{
				missingSapwoodFoundation = "unspecified reasons";
			}
						
			p.add(new Chunk("- Sapwood is " + sapwoodPresence, bodyFont ));
			if(woodCompleteness.getSapwood().getMissingSapwoodRingsToBark()!=null){
				p.add(new Chunk(" ("+ missingSapwoodRings + " rings are missing based upon " + missingSapwoodFoundation + ").\n", bodyFont));
			}
			else
			{
				p.add(new Chunk(" (Details on missing sapwood rings was not recorded.)\n", bodyFont));
			}
		}
		else{
			p.add(new Chunk("- No sapwood details recorded.\n"));
		}	
	
		// Extract bark info
		if(woodCompleteness.getBark()!=null){
			
			if(woodCompleteness.getBark().getPresence().toString().toLowerCase()!=null){
				barkPresence = woodCompleteness.getBark().getPresence().value();
			}
			else{
				barkPresence = "not specified";
			}
		
			p.add(new Chunk("- Bark is "+ barkPresence +".\n", bodyFont));
		}
		else{
			p.add(new Chunk("- No bark details were recorded.\n", bodyFont));
		}
		
		// Last ring info
		if(woodCompleteness.getSapwood().getLastRingUnderBark()!=null){
			p.add(new Chunk("- Last ring "+woodCompleteness.getSapwood().getLastRingUnderBark().toString(), bodyFont));
		}
		else
		{
			p.add(new Chunk("- No details about the last ring were recorded.\n", bodyFont));
		}
		return p;
	}
	
	/**
	 * Blank iText paragraph used for padding 
	 * @return Paragraph
	 */
	private Paragraph getParagraphSpace()
	{
		Paragraph p = new Paragraph();
		
		p.add(new Chunk(" "));
		return p;
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
		
		p.add(new Chunk("Element and sample details:\n", subSectionFont));
		
		p.add(new Chunk("- Taxon - "+ telem.getTaxon().getNormal()+"\n", bodyFont));
		p.add(new Chunk("- Element type - "+ telem.getType().getNormal()+"\n", bodyFont));
		p.add(new Chunk("- Sample type - "+ tsamp.getType().getNormal()+"\n", bodyFont));
		return p;
		
	}	
	
	public static void getReport(Boolean printReport, String vmid)
	{
		
		String domain = "dendro.cornell.edu/dev/";
	
		Sample samp = null;
		
		try {
			samp = PrintReportFramework.getSampleForID(domain, vmid);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

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
				return;
			}
		}
		
	}
	
	public static void printReport(String vmid)
	{
		getReport(true, vmid);	
	}
	
	public static void viewReport(String vmid)
	{
		getReport(false, vmid);
	}
	
	public static void main(String[] args)
	{
		String measurementID = "02189be5-b19c-5dbd-9035-73ae8827dc7a";
		viewReport(measurementID);
		
	}
	
}