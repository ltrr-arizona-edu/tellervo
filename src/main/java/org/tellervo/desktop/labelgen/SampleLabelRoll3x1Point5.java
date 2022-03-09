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
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
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
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Label style designed for 3x1.5" non-adhesive plastic roll labels from EIM Inc.
 *  
 * @author pbrewer
 *
 */
public class SampleLabelRoll3x1Point5 extends AbstractTellervoLabelStyle {

		Font labelTitleFont = new Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD);
		Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10f);
		Font titleFont = new Font(Font.FontFamily.HELVETICA, 20f, Font.BOLD);
		Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14f);
		Font subSubSectionFont = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD);
		Font bodyFontLarge = new Font(Font.FontFamily.HELVETICA, 14f);
		Font tableHeaderFontLarge = new Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD);		
		Float headerLineWidth = new Float(0.8);	
		private Font labelfont = new Font(Font.FontFamily.HELVETICA, 15f, Font.BOLD);

		private Font tinyfont = new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD);
		private Font teenyfont = new Font(Font.FontFamily.HELVETICA, 6f);
		

	    private final static Logger log = LoggerFactory.getLogger(SampleLabelRoll3x1Point5.class);

	
	public SampleLabelRoll3x1Point5() {
		super("Sample Tag - Oversize", "Designed for 3x1.5\" non-adhesive plastic roll labels from EIM Inc.", ItemType.SAMPLE);
		
	}

	@Override
	public void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception {
		try {
			
			document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, output);
			
			document.setPageSize(new Rectangle(216, 108));
			document.open();
			float margin = 2;
			float fontheight = 7;
			document.setMargins(margin, margin, margin ,margin);
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Tellervo"); 
		    document.addSubject("Sample Label"); 
						
		    for(Object item : items)
	        {
	        	if(item instanceof TridasSample)
	        	{
	        		
	        	}
	        	else
	        	{
	        		throw new Exception ("Label type not valid for this label style");
	        	}
			
	        	// Compile data to go on label
	        	String sampleLabelText;
	        	TridasProject project = App.dictionary.getTridasProjectByID(TridasUtils.getGenericFieldValueByName((TridasSample) item, "tellervo.internal.projectID"));
	        	TridasObject object = App.dictionary.getTridasObjectByID(TridasUtils.getGenericFieldValueByName((TridasSample) item, "tellervo.internal.objectID"));
	        	WSIBox box =  App.dictionary.getBoxByID(TridasUtils.getGenericFieldValueByName((TridasSample) item, "tellervo.boxID"));
				String siteNameText = object.getTitle();
	        			
	        	
	        	//String projectText  = "Peter Brewer : The name of the project af asdfasd fasd fsad fasd fads fasd fasd fasd fasd fasd fdsa fas dfads fas dfasdf asdf asd fads fasd fads fasd ";
	        	String projectTitle  = project.getTitle();

	        	TridasSample s = (TridasSample) item;
				TridasGenericField labcodeField = GenericFieldUtils.findField(s, "tellervo.internal.labcodeText");
				sampleLabelText = labcodeField.getValue();
	        	
				
				
				// Top left text
				
	        	ColumnText ct = new ColumnText(cb);
	        	ct.setUseAscender(true);
	        		        	
	        	float llx = 36;
	        	float lly = 36;
	        	float urx = this.document.getPageSize().getWidth() - margin;
	        	float ury = this.document.getPageSize().getHeight() - margin - fontheight;
	        	
	        	System.out.println("llx = "+llx);
	        	System.out.println("lly = "+lly);
	        	System.out.println("urx = "+urx);
	        	System.out.println("ury = "+ury);
	        	
	        	// PdfContentByte cb = writer.getDirectContentUnder();
	            // cb.rectangle(llx, lly,   urx-llx , ury - lly);
	            //cb.stroke();
	        	
	        	
	        	ct.setSimpleColumn(llx,lly ,urx , ury , 5f, Element.ALIGN_LEFT);
	        	ct.addElement(new Phrase(project.getTitle(), teenyfont));
	        	ct.addElement(new Phrase(project.getInvestigator(), teenyfont));
	        	ct.addElement(new Phrase(object.getTitle(), tinyfont));

	        	ct.go();
	        	
	        	
	        	// Sample Code right
	        	ct = new ColumnText(cb);
	        	int status = ColumnText.START_COLUMN;
	        	ct.setSimpleColumn(36,28 ,this.document.getPageSize().getWidth() - margin - margin , 64 , 8f, Element.ALIGN_LEFT);
	        	ct.addText(new Phrase("LTRR #:"+sampleLabelText, tinyfont));
	        	status = ct.go();
	        	
	        	if(ColumnText.hasMoreText(status))
	        	{
	        		log.error("OVERFULL sample code!!!");
	        		ct = new ColumnText(cb);
	        		ct.setSimpleColumn(280,2 ,this.document.getPageSize().getWidth() - margin - margin , 15 , 8f, Element.ALIGN_LEFT);
		        	ct.addElement(new Paragraph("!", teenyfont));
		        	ct.go();
	        	}
	        	if(box!=null)
	        	{
	        		ct.addText(new Phrase("Box:"+box.getTitle(), teenyfont));
	        		ct.go();
	        	}
	        	
	        	
	        	        	
	        	// Barcode bottom left 
	        	
	        	//System.out.println("Yline = "+ct.getYLine());
	        	//float barheight = ct.getYLine();
	        	float barheight = 22;
	        	
	        	ct = new ColumnText(cb);
	        	
	        	System.out.println("Bar height = "+barheight);
	       
        		llx = margin+9;
	        	lly = margin;
	        	urx = this.document.getPageSize().getWidth() -margin;
	        	ury = barheight+margin;
	        	int leading = 1;
	        	
	        	System.out.println("llx = "+llx);
	        	System.out.println("lly = "+lly);
	        	System.out.println("urx = "+urx);
	        	System.out.println("ury = "+ury);
        		ct.setSimpleColumn(llx,lly ,urx , ury , leading,  Element.ALIGN_LEFT);
        		ct.setUseAscender(true);
        		ct.addText(new Chunk(LabBarcode.getSampleBarcode(s, cb, barheight, barcodeSize), 0, 0, true));
	        	ct.go();
	        	
	        	
	        	// Sample Code right
	        	/*int status = ColumnText.START_COLUMN;
	        	ct.setSimpleColumn(215,2 ,this.document.getPageSize().getWidth() - margin - margin , this.document.getPageSize().getHeight() - margin , 8f, Element.ALIGN_LEFT);
	        	ct.addText(new Phrase(sampleLabelText, tinyfont));
	        	status = ct.go();
	        	
	        	if(ColumnText.hasMoreText(status))
	        	{
	        		log.error("OVERFULL sample code!!!");
	        		ct = new ColumnText(cb);
	        		ct.setSimpleColumn(280,2 ,this.document.getPageSize().getWidth() - margin - margin , 15 , 8f, Element.ALIGN_LEFT);
		        	ct.addElement(new Paragraph("!", teenyfont));
		        	ct.go();
	        	}*/
	      	        
	        	document.newPage();
	        	
	        	
	        	
	        }
		    

			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}

		// Close the document
		document.close();
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
