package org.tellervo.desktop.labelgen;

import java.util.ArrayList;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.schema.WSIBox;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Image;

import javax.swing.Icon;
 

public class BoxBarcode12UpLabelStyle extends AbstractTellervoLabelStyle {

		Font labelTitleFont = new Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD);
		Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10f);

	
	public BoxBarcode12UpLabelStyle() {
		super("Box barcodes 12-up", "Simple box barcode labels for printing on letter-size labels, 12 to a page.  Includes box and laboratory name as well as barcode.", ItemType.BOX);
		
	}

	@Override
	public Icon getPageImage()
	{
		return Builder.getImageAsIcon("/LabelStyles/BoxBarcode12UpLabelStyle-page.png");
	}
	
	@Override
	public Icon getLabelImage()
	{
		return Builder.getImageAsIcon("/LabelStyles/BoxBarcode12UpLabelStyle-label.png");
	}
	
	@Override
	public void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception {
		try {
			document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, output);
			
			document.setPageSize(PageSize.LETTER);

			document.setMargins(10, 10, 40, 40);
			document.open();
		
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Tellervo"); 
		    document.addSubject("Tellervo Box Labels"); 
				
		    PdfPTable table = new PdfPTable(2);
	        table.setTotalWidth(495f);
	        table.setLockedWidth(true);
	        
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
	        	
		        Paragraph p = new Paragraph();
		        
		        p.add(new Chunk(b.getTitle()+Chunk.NEWLINE, labelTitleFont));
		        
		        p.add(new Chunk(Chunk.NEWLINE+" ", bodyFont));

		        //p.add(new Chunk(Chunk.NEWLINE+b.getComments()+Chunk.NEWLINE, bodyFont));
		        p.add(new Chunk(App.getLabName()+Chunk.NEWLINE+Chunk.NEWLINE, bodyFont));
		        p.add(new Chunk(LabBarcode.getBoxBarcode(b, cb), 0, 0, true));
		        
		        PdfPCell cell = new PdfPCell(p);
		        cell.setPaddingLeft(15f);
		        cell.setPaddingRight(15f);
		        cell.setBorderColor(BaseColor.LIGHT_GRAY);

		        table.addCell(cell);

	        }
	
	        PdfPCell cell = new PdfPCell(new Paragraph());
	        cell.setBorderColor(BaseColor.LIGHT_GRAY);
	        
	        table.addCell(cell);
	        document.add(table);
	        document.close();

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}

		// Close the document
		document.close();
	}

	
	

}
