package org.tellervo.desktop.labelgen;

import java.util.ArrayList;
import java.util.UUID;

import org.tellervo.desktop.labelgen.AbstractTellervoLabelStyle.ItemType;
import org.tellervo.schema.WSIBox;
import org.tridas.schema.TridasSample;

import com.itextpdf.text.BaseColor;
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

public class ShelfEndLabelStyle extends AbstractTellervoLabelStyle {

	private Font sectionFont = new Font(Font.FontFamily.HELVETICA, 36f, Font.BOLD);
	private Font detailFont = new Font(Font.FontFamily.HELVETICA, 12f);

	//private Rectangle pageSize = new Rectangle(288, 54);
	private Rectangle pageSize = new Rectangle(355, 212);
	//private Rectangle pageSize = new Rectangle(3252, 2108);
	
	
	public ShelfEndLabelStyle() {
		super("Box barcodes 3.5 x 1.5\"", "Simple box barcode labels for printing on a roll - 3.5 x 1.5\"", ItemType.GENERIC);
		
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
		    	String[] pairs = item.toString().split(";");
		    	
	        	
	        	ColumnText ct = new ColumnText(cb);
	        	ct.setSimpleColumn(15, 0, this.document.getPageSize().getWidth() - 5, this.document.getPageSize().getHeight()-25, 16, Element.ALIGN_LEFT);
	        	ct.addText(new Phrase(pairs[0], sectionFont));
	        	ct.go();
	        	
	        	//System.out.println("Yline = "+ct.getYLine());
	        	//float barheight = ct.getYLine();
	        	float barheight = 51;
	        	
	        	ct = new ColumnText(cb);
	        	
	        	
	        	if(barheight>50)
	        	{
	          		ct.setSimpleColumn(10, 5, this.document.getPageSize().getWidth() - 10, barheight - 10, 16, Element.ALIGN_RIGHT);
	        		ct.addText(new Chunk(ShelfEndLabelStyle.getBarcode(pairs[0], cb, barheight-15), 0, 0, true));
	        	}
	        	else
	        	{
	        		ct.setSimpleColumn(10, 5, this.document.getPageSize().getWidth() - 10, barheight - 10, 16, Element.ALIGN_RIGHT);
	        		ct.addText(new Chunk(ShelfEndLabelStyle.getBarcode(pairs[0], cb, barheight-15), 0, 0, true));

	        	}
	        	ct.go();
	        
	        	Rectangle rect= new Rectangle(pageSize.getWidth(), pageSize.getHeight());
	        	rect.enableBorderSide(1);
	        	rect.enableBorderSide(2);
	        	rect.enableBorderSide(4);
	        	rect.enableBorderSide(8);
	        	rect.setBorder(Rectangle.BOX);
	        	rect.setBorderWidth(1);
	        	rect.setBorderColor(BaseColor.BLACK);
	        	document.add(rect);
	        	
	        	// NFC Logo
	            String filename = "/home/pbrewer/git/tellervo/src/main/resources/Icons/512x512/nfc.png";
	            Image image = Image.getInstance(filename);
	            float scaler = 9f;
	            image.scalePercent(scaler);
	            image.setAbsolutePosition(15f, 15f);
	            document.add(image);
	            
	            ct = new ColumnText(cb);
	        	ct.setSimpleColumn(15, 0, this.document.getPageSize().getWidth() - 5, this.document.getPageSize().getHeight()-70, 16, Element.ALIGN_LEFT);
	        	if(pairs.length>1)
	        	{
	        		ct.addText(new Phrase(pairs[1].trim(), detailFont));

	        	}
	        	
	        	if(pairs.length>2)
	        	{
	        		ct.addText(new Phrase("\n"+pairs[2].trim(), detailFont));
	        	}
	        	if(pairs.length>3)
	        	{
	        		ct.addText(new Phrase("\n"+pairs[3].trim(), detailFont));
	        	}
	        	if(pairs.length>4)
	        	{
	        		ct.addText(new Phrase("\n"+pairs[4].trim(), detailFont));
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
