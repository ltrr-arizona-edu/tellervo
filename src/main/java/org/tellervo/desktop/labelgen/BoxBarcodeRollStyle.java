package org.tellervo.desktop.labelgen;

import java.util.ArrayList;

import org.tellervo.schema.WSIBox;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

public class BoxBarcodeRollStyle extends AbstractTellervoLabelStyle {


		private Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD);
		private Font tinyFont = new Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC);
		private Rectangle pageSize = new Rectangle(252, 108);

	
	public BoxBarcodeRollStyle() {
		super("Box barcodes 3.5 x 1.5\"", "Simple box barcode labels for printing on a roll - 3.5 x 1.5\"", ItemType.BOX);
		
	}
	
	public BoxBarcodeRollStyle(Rectangle pageSize, String title, String description, ItemType itemType) {
		super(title, description, ItemType.BOX);
		this.pageSize = pageSize;
		
	}
	


	@Override
	public void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception {
		try {
			document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, output);
			
			document.setPageSize(this.pageSize);
			document.setMargins(2, 2, 0 ,0);

			document.open();
		
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Tellervo"); 
		    document.addSubject("Tellervo Box Barcodes"); 
				        
		    for(Object item : items)
	        {
	        	if(item instanceof WSIBox)
	        	{
	        		
	        	}
	        	else
	        	{
	        		throw new Exception ("Label type not valid for this label style");
	        	}
	        	
	        	WSIBox b = (WSIBox) item;
	        	
	        	ColumnText ct = new ColumnText(cb);
	        	ct.setSimpleColumn(5, this.document.getPageSize().getHeight()/2, this.document.getPageSize().getWidth() - 5, this.document.getPageSize().getHeight() - 5, 16, Element.ALIGN_CENTER);
	        	ct.addText(new Phrase(b.getTitle(), sectionFont));
	        	//ct.addText(new Phrase(b.getCurationLocation(), tinyFont));
	        	ct.go();
	        	
	        	//System.out.println("Yline = "+ct.getYLine());
	        	float barheight = ct.getYLine();
	        	
	        	ct = new ColumnText(cb);
	        	
	        	
	        	if(barheight>50)
	        	{
	        		ct.setSimpleColumn(10, 5, this.document.getPageSize().getWidth() - 10, barheight - 10, 16, Element.ALIGN_CENTER);

	        		ct.addText(new Chunk(LabBarcode.getBoxBarcode(b, cb, barheight-15, barcodeSize), 0, 0, true));
	        	}
	        	else
	        	{
	        		ct.setSimpleColumn(10, 5, this.document.getPageSize().getWidth() - 5, barheight - 10, 16, Element.ALIGN_CENTER);
	        		ct.addText(new Chunk(LabBarcode.getBoxBarcode(b, cb, barheight-15, barcodeSize), 0, 0, true));

	        	}
	        	ct.go();
	        
	        	document.newPage();
	        }


		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}

		// Close the document
		document.close();
	}


	
	

}
