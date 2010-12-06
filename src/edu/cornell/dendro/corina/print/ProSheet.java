
package edu.cornell.dendro.corina.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.tridasv2.LabCode;
import edu.cornell.dendro.corina.tridasv2.LabCodeFormatter;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.util.ArrayListModel;
import edu.cornell.dendro.corina.util.pdf.PrintablePDF;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntitySearchResource;


public class ProSheet extends ReportBase {
	
	private TridasObjectEx o = new TridasObjectEx();
	private ArrayListModel<edu.cornell.dendro.corina.sample.Element> elements;
	
	public ProSheet(TridasObject o, TridasDerivedSeries master, ArrayListModel<edu.cornell.dendro.corina.sample.Element> elements){
		
		// Find all series for an object 
    	SearchParameters sampparam = new SearchParameters(SearchReturnObject.OBJECT);
    	sampparam.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, o.getIdentifier().getValue().toString());
    	
    	
    	// we want a series returned here    	
		EntitySearchResource<TridasObject> searchResource = new EntitySearchResource<TridasObject>(sampparam);
		searchResource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.COMPREHENSIVE);
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(searchResource);
		searchResource.query();	
		dialog.setVisible(true);
	
		List<TridasObject> objlist = searchResource.getAssociatedResult();

		if(objlist.size()>0)
		{
			this.o = (TridasObjectEx) objlist.get(0);
		}
		this.elements = elements;
	}
		
	private void generateProSheet(OutputStream output) {
	
        Paragraph spacingPara = new Paragraph();
        spacingPara.setSpacingBefore(10);
	    spacingPara.add(new Chunk(" ", bodyFont)); 
		
		try {
		
			PdfWriter writer = PdfWriter.getInstance(document, output);
			document.setPageSize(PageSize.LETTER);

			// Set basic metadata
		    document.addAuthor("Peter Brewer"); 
		    document.addSubject("Corina Provenience Sheet for " + o.getTitle()); 
		    
		    HeaderFooter footer = new HeaderFooter(new Phrase(""), new Phrase(""));
		    footer.setAlignment(Element.ALIGN_RIGHT);
		    footer.setBorder(0);  
		    document.setFooter(footer);
		    
		    HeaderFooter header = new HeaderFooter(new Phrase(o.getLabCode()+ " - "+o.getTitle(), bodyFont), false);
		    header.setAlignment(Element.ALIGN_RIGHT);
		    header.setBorder(0);
		    document.setHeader(header);
		    			
		    document.open();
			cb = writer.getDirectContent();	
			
			// Title Left		
			ColumnText ct = new ColumnText(cb);
			ct.setSimpleColumn(document.left(), document.top()-193, document.right(), document.top()-20, 20, Element.ALIGN_LEFT);
			ct.addText(getTitlePDF());
			ct.go();
						
				
			// Timestamp
			ColumnText ct3 = new ColumnText(cb);
			ct3.setSimpleColumn(document.left(), document.top()-223, 283, document.top()-60, 20, Element.ALIGN_LEFT);
			ct3.setLeading(0, 1.2f);
			ct3.addText(getTimestampPDF());
			ct3.go();
			
		
			
			
			// Pad text
			document.add(spacingPara);   
	        document.add(getObjectDescription());   
	        document.add(getObjectComments());
		     
	        document.add(spacingPara);
	        
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
		
		PdfPTable tbl = new PdfPTable(5);
		
		PdfPCell headerCell = new PdfPCell();

		tbl.setWidthPercentage(100f);
		float[] widths = {0.1f, 0.4f, 0.2f, 0.1f, 0.2f};
		
		tbl.setWidths(widths);

		// Set up header
		headerCell.setPhrase(new Phrase("Element", tableHeaderFont));
		headerCell.setBorderWidthBottom(headerLineWidth);
		headerCell.setBorderWidthTop(headerLineWidth);
		headerCell.setBorderWidthLeft(0);
		headerCell.setBorderWidthRight(0);
		headerCell.setPaddingTop(5);
		headerCell.setPaddingBottom(5);
		tbl.addCell(headerCell);
		
		headerCell.setPhrase(new Phrase("Comments", tableHeaderFont));
		tbl.addCell(headerCell);
	
		headerCell.setPhrase(new Phrase("Taxon", tableHeaderFont));
		tbl.addCell(headerCell);
		
		headerCell.setPhrase(new Phrase("# Rings", tableHeaderFont));
		tbl.addCell(headerCell);
		
		headerCell.setPhrase(new Phrase("Dates", tableHeaderFont));
		tbl.addCell(headerCell);		
				
		
		// Loop through rows
		for(edu.cornell.dendro.corina.sample.Element e: this.elements)
		{	
			Sample s = null;
			
			try {
				s = e.load();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				continue;
			}
			
			
			// Find element details for this series 
	    	SearchParameters param = new SearchParameters(SearchReturnObject.DERIVED_SERIES);
			param.addSearchConstraint(SearchParameterName.SERIESDBID, SearchOperator.EQUALS, s.getIdentifier().getValue().toString());
	    		
			EntitySearchResource<TridasObject> searchResource = new EntitySearchResource<TridasObject>(param, TridasObject.class);
			searchResource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.COMPREHENSIVE);
			CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(searchResource);
			searchResource.query();	
			dialog.setVisible(true);
		
			List<TridasObject> oblist = searchResource.getAssociatedResult();
			
			if(oblist.size()!=1)
			{
				System.out.println(e.getName()+ " has more than one (or no) associated objects so skipping");
				continue;				
			}
			TridasObject obj = oblist.get(0);
			
			List<TridasElement> ellist = obj.getElements();
			if(ellist.size()!=1)
			{
				System.out.println(e.getName()+ " has more than one (or no) associated element so skipping");
				continue;
			}
			TridasElement el = ellist.get(0);
			
			// make lab code
			LabCode labcode = new LabCode();
			labcode.appendSiteCode(((TridasObjectEx) obj).getLabCode());
			labcode.setElementCode(el.getTitle());
					
			
			PdfPCell dataCell = new PdfPCell();
			dataCell.setBorderWidthBottom(0);
			dataCell.setBorderWidthTop(0);
			dataCell.setBorderWidthLeft(0);
			dataCell.setBorderWidthRight(0);
			dataCell.setPaddingTop(5);
			dataCell.setPaddingBottom(5);			
			dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			
			// Title Column
			dataCell.setPhrase(new Phrase(LabCodeFormatter.getSamplePrefixFormatter().format(labcode).toString(), tableBodyFont));		
            tbl.addCell(dataCell);
                        
            // Comments Column
            if(el.getComments()!=null)
            	dataCell.setPhrase(new Phrase(el.getComments(), tableBodyFont));		
            else
            	dataCell.setPhrase(new Phrase(" ", tableBodyFont));		
        	tbl.addCell(dataCell);
        	
        	// Taxon Column
            dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataCell.setPhrase(new Phrase(el.getTaxon().getNormal().toString(), tableBodyFont));		
            tbl.addCell(dataCell);        	
        	
        	// Rings Column
            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataCell.setPhrase(new Phrase(String.valueOf(s.countRings()), tableBodyFont));		
            tbl.addCell(dataCell);
            
            // Dates column
            String datingLabel;
            String datingType = s.getSeries().getInterpretation().getDating().getType().value().toString();  
            datingLabel = s.getSeries().getInterpretation().getFirstYear().getValue().toString();
            if(datingType=="Absolute")
            {
            	datingLabel += s.getSeries().getInterpretation().getFirstYear().getSuffix().toString();
            }
            datingLabel += " - "+ String.valueOf(s.getSeries().getInterpretation().getFirstYear().getValue().intValue() + s.countRings()-1);
            if(datingType=="Relative")
            {
            	datingLabel += " (Rel. Date)";
            }
 
            dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataCell.setPhrase(new Phrase(datingLabel, tableBodyFont));		
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
		
		p.add(new Chunk(o.getLabCode()+" - "+o.getTitle()+"\n", titleFont));

		//p.add(new Chunk(i.getCode(), subTitleFont));
				
		return p;		
	}
	
	
	
	private Paragraph getObjectDescription() 
	{
	
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setSpacingAfter(10);
        p.setSpacingBefore(50);
        
		if(o.getDescription()!=null){
			p.add(new Chunk(o.getDescription(), bodyFont));
		}
		else{
			p.add(new Chunk("No description recorded", bodyFont));
		}
		
		return p;
	}

	private Paragraph getObjectComments() 
	{
	
		Paragraph p = new Paragraph();
		p.setLeading(0, 1.2f);
		p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setSpacingAfter(10);
		
		if(o.getComments()!=null){
			p.add(new Chunk("Notes: ", commentFont));
			p.add(new Chunk(o.getComments(), commentFont));
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
	/*	String domain = App.domain;
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