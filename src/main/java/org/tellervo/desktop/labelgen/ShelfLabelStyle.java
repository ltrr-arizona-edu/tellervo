package org.tellervo.desktop.labelgen;

import java.util.ArrayList;
import java.util.UUID;

import org.tellervo.desktop.labelgen.AbstractTellervoLabelStyle.ItemType;
import org.tellervo.schema.WSIBox;
import org.tridas.schema.TridasSample;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class ShelfLabelStyle extends AbstractTellervoLabelStyle {

	private Font sectionFont = new Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD);
	//private Rectangle pageSize = new Rectangle(288, 54);
	private Rectangle pageSize = new Rectangle(234, 27);
	//private Rectangle pageSize = new Rectangle(3252, 2108);
	
	
	public ShelfLabelStyle() {
		super("Box barcodes 3.25 x 0.375\"", "Simple box barcode labels for printing on a roll - 3.25 x 1.5\"", ItemType.GENERIC);
		
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
		    document.addSubject("Tellervo Shelf Labels"); 
				        
		    for(Object item : items)
	        {
	        
	        	
	        	ColumnText ct = new ColumnText(cb);
	        	ct.setSimpleColumn(6, 0, this.document.getPageSize().getWidth() - 5, this.document.getPageSize().getHeight()-4, 16, Element.ALIGN_LEFT);
	        	ct.addText(new Phrase(item.toString(), sectionFont));
	        	ct.go();
	        	
	        	//System.out.println("Yline = "+ct.getYLine());
	        	//float barheight = ct.getYLine();
	        	float barheight = 33;
	        	
	        	ct = new ColumnText(cb);
	        	
	        	
	        	if(barheight>50)
	        	{
	        		ct.setSimpleColumn(15, 5, this.document.getPageSize().getWidth() - 5, barheight - 10, 16, Element.ALIGN_RIGHT);

	        		ct.addText(new Chunk(ShelfLabelStyle.getBarcode(item.toString(), cb, barheight), 0, 0, true));
	        	}
	        	else
	        	{
	        		ct.setSimpleColumn(15, 5, this.document.getPageSize().getWidth() - 5, barheight - 10, 16, Element.ALIGN_RIGHT);
	        		ct.addText(new Chunk(ShelfLabelStyle.getBarcode(item.toString(), cb, barheight-15), 0, 0, true));

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
	
	public static Image getBarcode(String code, PdfContentByte cb, float barheight)
	{
		Barcode128 barcode = new Barcode128();
		barcode.setCode(code);
		
		barcode.setFont(null);
		barcode.setX(0.7f);
		barcode.setSize(6f);
		barcode.setBaseline(8f);
		barcode.setBarHeight(barheight);
		
		Image image = barcode.createImageWithBarcode(cb, null, null);
	
		return image;
	
	}

}
