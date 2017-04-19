package org.tellervo.desktop.print;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.platform.Platform;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.FontUtil;
import org.tellervo.desktop.util.labels.LabBarcode;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class RecordCard extends ReportBase {

	private Font hand;
	private Font[] fieldFonts = new Font[4];
	private Font[] titleFonts = new Font[6];
	private Font[] headerFonts = new Font[5];
	int baseWidth = 0;
	private float padding = 1f;
	private int border = Rectangle.NO_BORDER;
	
	public static void main(String[] args) {
	    App.platform = new Platform();
	    App.platform.init();
	    
		App.init(null, null);
		
		//XMLDebugView.showDialog();
		

		RecordCard.getRecordCards(null);
	}
	
	public static TridasObject getTestTridasObject(String code) {
		
		List<TridasObjectEx> objects = App.tridasObjects.getObjectList();
		
		for(TridasObjectEx ob : objects)
		{
			if(TridasUtils.getGenericFieldByName(ob, "tellervo.objectLabCode").getValue().equals("code"))
			{
				return ob;
			}
			
		}
		
		
		
		return null;
		
	}
	
	public RecordCard()
	{
		writePDF();
	}
	
	public static void getRecordCards(TridasObject entity)
	{
		RecordCard rc = new RecordCard();
		
	}
	
	
	public void writePDF()
	{

		BaseFont handfont = null;
		try {
			handfont = BaseFont.createFont(FontUtil.getFontFilenameFromResources("OpenSans-LightItalic.ttf"), BaseFont.CP1252, BaseFont.EMBEDDED);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		fieldFonts[0] = new Font(Font.HELVETICA, 9, Font.NORMAL);
		fieldFonts[1] = new Font(Font.HELVETICA, 8, Font.NORMAL);
		fieldFonts[2] = new Font(Font.HELVETICA, 7, Font.NORMAL);
		fieldFonts[3] = new Font(Font.HELVETICA, 6, Font.NORMAL);

		
		titleFonts[0] = new Font(Font.HELVETICA, 20, Font.BOLD);
		titleFonts[1] = new Font(Font.HELVETICA, 18, Font.BOLD);
		titleFonts[2] = new Font(Font.HELVETICA, 16, Font.BOLD);
		titleFonts[3] = new Font(Font.HELVETICA, 14, Font.BOLD);
		titleFonts[4] = new Font(Font.HELVETICA, 12, Font.BOLD);
		titleFonts[5] = new Font(Font.HELVETICA, 10, Font.BOLD);
		
		headerFonts[0] = new Font(handfont, 4, Font.NORMAL);
		headerFonts[1] = new Font(handfont, 3, Font.NORMAL);
		headerFonts[2] = new Font(handfont, 2, Font.NORMAL);
		
	
		Document document = new Document(new Rectangle(360, 216), 10, 10, 5, 5);
		PdfContentByte cb;

		File outputFile;
		FileOutputStream output = null;
		try {
			outputFile = new File("/tmp/recordcard.pdf");
			output = new FileOutputStream(outputFile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, output);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		document.open();
	
		cb = writer.getDirectContent();			
		
		// Set basic metadata
	    document.addAuthor("Tellervo"); 
	    document.addSubject("Tellervo Record Cards");
	    
	    float[] widths = {6f, 1f, 5f};
	    PdfPTable table = new PdfPTable(widths);
	    float width = 350f;
	    this.baseWidth = (int) (width/12);
	    table.setTotalWidth(width);
	    table.setLockedWidth(true);
	   
	    Paragraph p;
	    PdfPCell cell;
	    
	   
		TridasSample s = new TridasSample();
		
		TridasIdentifier id = new TridasIdentifier();
		id.setValue("5fe8cc2e-e4db-11e6-819b-f72940f6ee61");
		s.setIdentifier(id);
	    
	    
	    
        try {
        	// LINE 1
        	p = new Paragraph();        	
        	cell = getCellWithFitText("ABC-1234-A", 0, baseWidth*6, titleFonts, true);
        	cell.setBorder(border);
        	
        	cell.setRowspan(2);
        	cell.setPadding(padding); 
        	table.addCell(cell); 
        	
        	cell = getCellWithFitText("Box", 0, baseWidth, headerFonts, true);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("1234A", 0, baseWidth*5, fieldFonts, false);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("Shelfmark", 0, baseWidth, headerFonts, true);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("LTRR Archive, Room X, Shelf B", 0, baseWidth*5, fieldFonts, false);
        	cell.setPadding(padding); table.addCell(cell); 
        	
        	
        	// LINE 2
        	cell = getCellWithFitText("NPS-XXXXX; Blah blah bblah another code", 0, baseWidth*7, fieldFonts, false);
        	cell.setColspan(2);
        	cell.setBorderWidthTop(0.5f);
        	cell.setPadding(padding);
        	cell.setPaddingTop(4);
        	table.addCell(cell); 
        	
        	cell = getCellWithFitText("Core", 0, baseWidth*5, fieldFonts, false);
        	cell.setBorderWidthTop(0.5f);
        	cell.setPadding(padding);
        	cell.setPaddingTop(4); 
       	 	table.addCell(cell);         	
        	
        	cell = getCellWithFitText("External specimen code(s)", 0, baseWidth*7, headerFonts, true);
        	cell.setColspan(2);
        	cell.setPadding(padding); 
        	table.addCell(cell);
        	
        	cell = getCellWithFitText("Sample type", 0, baseWidth*5, headerFonts, true);
        	cell.setPadding(padding); 
        	table.addCell(cell); 
        	
        	      	
        	
        	// LINE 3
        	cell = getCellWithFitText("Pinus nigra subsp. pallasiana (Lamb.) Holmboe", 0, baseWidth*7, fieldFonts, false);
        	cell.setColspan(2);
        	cell.setPadding(padding);
        	table.addCell(cell); 
        	
        	cell = getCellWithFitText("1200AD - 1940AD", 0, baseWidth*4, fieldFonts, false);
        	cell.setPadding(padding);
       	 	table.addCell(cell);         	
        	
        	cell = getCellWithFitText("Taxon", 0, baseWidth*7, headerFonts, true);
        	cell.setColspan(2);
        	cell.setPadding(padding); 
        	table.addCell(cell);
        	
        	cell = getCellWithFitText("Dating", 0, baseWidth*5, headerFonts, true);
        	cell.setPadding(padding); 
        	table.addCell(cell); 
        	
        	
        	// LINE 4
        	cell = getCellWithFitText("20th January 2016", 0, baseWidth*7, fieldFonts, false);
        	cell.setColspan(2);
        	cell.setPadding(padding);
        	table.addCell(cell); 
        	
        	cell = getCellWithFitText("Lat: 34.23  Lon: -120.33", 0, baseWidth*4, fieldFonts, false);
        	cell.setPadding(padding);
       	 	table.addCell(cell);         	
        	
        	cell = getCellWithFitText("Sampling date", 0, baseWidth*7, headerFonts, true);
        	cell.setColspan(2);
        	cell.setPadding(padding); 
        	table.addCell(cell);
        	
        	cell = getCellWithFitText("Tree coordinates (WGS84)", 0, baseWidth*5, headerFonts, true);
        	cell.setPadding(padding); 
        	table.addCell(cell); 
        	
        	
        	// LINE 5
        	cell = getCellWithFitText("My site name", 0, baseWidth*7, fieldFonts, false);
        	cell.setColspan(2);
        	cell.setPadding(padding);
        	table.addCell(cell); 
        	
        	cell = getCellWithFitText("Lat: 34.23  Lon: -120.33", 0, baseWidth*4, fieldFonts, false);
        	cell.setPadding(padding);
       	 	table.addCell(cell);         	
        	
        	cell = getCellWithFitText("Site or object name", 0, baseWidth*7, headerFonts, true);
        	cell.setColspan(2);
        	cell.setPadding(padding); 
        	table.addCell(cell);
        	
        	cell = getCellWithFitText("Site coordinates (WGS84)", 0, baseWidth*5, headerFonts, true);
        	cell.setPadding(padding); 
        	table.addCell(cell); 
        	
        	// LINE 6
        	cell = getCellWithFitText("Arizona, United States", 0, baseWidth*7, fieldFonts, false);
        	cell.setColspan(2);
        	cell.setPadding(padding);
        	table.addCell(cell); 
        	
        	cell = getCellWithFitText("Peter W Brewer", 0, baseWidth*4, fieldFonts, false);
        	cell.setPadding(padding);
       	 	table.addCell(cell);         	
        	
        	cell = getCellWithFitText("Site or object location", 0, baseWidth*7, headerFonts, true);
        	cell.setColspan(2);
        	cell.setPadding(padding); 
        	table.addCell(cell);
        	
        	cell = getCellWithFitText("Sampled by", 0, baseWidth*5, headerFonts, true);
        	cell.setPadding(padding); 
        	table.addCell(cell); 
        	
        	
        	// LINE 7
        	cell = getCellWithFitText("The name of the project if it is very long it then wraps around to two lines.  If it is reaaaaaallly long then the font size will shrink.", 0, baseWidth*14, fieldFonts, false);
        	cell.setColspan(2);
        	cell.setPadding(padding);
        	table.addCell(cell); 

        	cell = getCellWithFitText("Peter W Brewer", 0, baseWidth*4, fieldFonts, false);
        	cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        	cell.setPadding(padding);
       	 	table.addCell(cell);     
        	
        	cell = getCellWithFitText("Project name", 0, baseWidth*7, headerFonts, true);
        	cell.setColspan(2);
        	cell.setPadding(padding); 
        	table.addCell(cell);
        	
        	cell = getCellWithFitText("Principal investigator", 0, baseWidth*5, headerFonts, true);
        	cell.setPadding(padding); 
        	table.addCell(cell); 
        	
        	// LINE 8
        	cell = getCellWithFitText("Brewer P.W. 2016 Blah blah blah bbfadfsb fds skfd jdfj lkdsf kl Dendrochronologia 35, 100-200", 0, baseWidth*12, fieldFonts, false);
        	cell.setColspan(3);
        	cell.setPadding(padding);
        	table.addCell(cell); 

        	cell = getCellWithFitText("Primary project citation", 0, baseWidth*24, headerFonts, true);
        	cell.setColspan(3);
        	cell.setPadding(padding); 
        	table.addCell(cell);

        	// LINE 9
        	cell = getCellWithFitText("Lots of comments can go here.  The font size is adjusted to fit.  If the comments are too long with the smallest font size then they will be truncated.  Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis scelerisque erat vel tempus placerat. Duis eget leo lorem. Curabitur posuere turpis nibh, vel ultrices justo tincidunt vehicula. Phasellus id neque vitae nibh finibus placerat. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Aliquam congue arcu sit amet neque pretium, sit amet dapibus velit vehicula. Aliquam posuere odio leo, porta tempus eros molestie eget. Pellentesque viverra mi feugiat eros semper iaculis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis scelerisque erat vel tempus placerat. Duis eget leo lorem. Curabitur posuere turpis nibh, vel ultrices justo tincidunt vehicula. Phasellus id neque vitae nibh finibus placerat. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Aliquam congue arcu sit amet neque pretium, sit amet dapibus velit vehicula. Aliquam posuere odio leo, porta tempus eros molestie eget. Pellentesque viverra mi feugiat eros semper iaculis.",
        			0, baseWidth*59, fieldFonts, false);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
        	cell.setColspan(3);
        	cell.setPadding(padding);
        	table.addCell(cell); 

        	/*
        	
        	
        	
       	 	
       	 	
        
		     
        	// LINE 5
        	cell = getCellWithFitText("Sampled:", 0, baseWidth, headerFonts);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("2016-04-20", 0, fieldFonts);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("Tree coords:", 0, baseWidth, headerFonts);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("Lat: 34.23  Lon: -120.33", 0, fieldFonts);
        	cell.setPadding(padding); table.addCell(cell); 
        	
        	// LINE 6
        	cell = getCellWithFitText("Site/Object:", 0, baseWidth, headerFonts);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("My site name", 0, baseWidth*5, fieldFonts);
       	 	cell.setColspan(3);
       	 	cell.setPadding(padding); table.addCell(cell); 
        	
        	// LINE 7
       	 	cell = getCellWithFitText("Location:", 0, baseWidth, headerFonts);
       	 	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
       	 	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("Arizona, United States", 0, fieldFonts);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("Site coords:", 0, baseWidth, headerFonts);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("Lat: 34.23  Lon: -120.33", 0, fieldFonts);
        	cell.setPadding(padding); table.addCell(cell); 
       	 	
        	// LINE 8
        	cell = getCellWithFitText("PI:", 0, baseWidth, headerFonts);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("Peter Brewer", 0, fieldFonts);
       	 	cell.setColspan(3);
        	cell.setPadding(padding); table.addCell(cell); 
        	
        	// LINE 9
        	cell = getCellWithFitText("Project:", 0, baseWidth, headerFonts);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        	cell.setPadding(padding); table.addCell(cell); 
        	cell = getCellWithFitText("The name of my project", 0, baseWidth*5, fieldFonts);
       	 	cell.setColspan(3);
        	cell.setPadding(padding); table.addCell(cell); 
        	
        	// LINE 10
        	cell = getCellWithFitText("Citation:", 0, baseWidth, headerFonts);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        	cell.setPadding(padding); table.addCell(cell); 
       	 	cell = getCellWithFitText("Brewer P.W. 2016 Blah blah blah bbfadfsb fds skfd jdfj lkdsf kl Dendrochronologia 35, 100-200.", 0, 1080, fieldFonts);
 
       	 	cell.setColspan(3);
       	 	cell.setPadding(padding); table.addCell(cell); 

        	// LINE 10
       	 	cell = getCellWithFitText("Comments:", 0, baseWidth, headerFonts);
       	 	cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
       	 	cell.setPadding(padding); table.addCell(cell); 
       	 	cell = getCellWithFitText("Lots of comments go here...", 0, 1080, fieldFonts);
       	 	cell.setColspan(3);
       	 	cell.setPadding(padding); table.addCell(cell); 
        	*/
       	 	// LINE 11
            p.add(new Chunk(getBarCode(cb, s), 0, 0, true));
        	cell = new PdfPCell(p);
        	cell.setBorder(border);
        	cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        	cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        	cell.setColspan(3);
        	cell.setPadding(padding); table.addCell(cell); 
       	 	
       	 	table.setExtendLastRow(true);
		    document.add(table);
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		document.close();
		
		System.out.println("Finished");
		System.exit(0);
	}
	
	public PdfPCell getCell(String text, int alignment) {
	    PdfPCell cell = new PdfPCell(new Phrase(text));
	    cell.setPadding(0);
	    cell.setHorizontalAlignment(alignment);
	    //cell.setBorder(PdfPCell.NO_BORDER);
	    return cell;
	}
	
    public static float millimetersToPoints(float value) {
        return (value / 25.4f) * 72f;
    }
	/**
	 * Create a series bar code for this series
	 * 
	 * @return Image 
	 */
	private static Image getBarCode(PdfContentByte cb, TridasSample s)
	{
		UUID uuid = UUID.fromString(s.getIdentifier().getValue());
		LabBarcode barcode = new LabBarcode(LabBarcode.Type.SAMPLE, uuid);

		barcode.setX(1.0f);
		//barcode.setN(0.5f);
		//barcode.setSize(6f);
		barcode.setFont(null);
		//barcode.setBaseline(8f);
		barcode.setBarHeight(20f);
		
		
		Image image = barcode.createImageWithBarcode(cb, null, null);
	
		return image;
	
	}
	
	private PdfPCell getCellWithFitText(String text, int fontIndex, Font[] fontArray, boolean upperCase)
	{
		int width =baseWidth *12; // 5 inches
		
		return getCellWithFitText(text, fontIndex, width, fontArray, upperCase);
		
	}
	
	private PdfPCell getCellWithFitText(String text, int fontIndex, int width, Font[] fontArray, boolean upperCase)
	{
		
		if(upperCase)
		{
			text = text.toUpperCase();
		}
		
		if(fontIndex< fontArray.length)
		{
			if(getStringWidth(text, fontArray[fontIndex])>width)
			{
				return getCellWithFitText(text, fontIndex+1, width, fontArray, upperCase);
			}
			else
			{
				Paragraph p = new Paragraph(text, fontArray[fontIndex]);
		    	PdfPCell cell = new PdfPCell(p);
		    	cell.setBorder(border);
		    	cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		    	return cell;
			}
		}
		else
		{
			// Font size set to smallest, yet text is still too big.  
			text =  text.substring(0, text.length()-2);
			if(getStringWidth(text, fontArray[fontArray.length-1])>width)
			{
				return getCellWithFitText(text, fontArray.length-1, width, fontArray, upperCase);
			}
			else
			{
				text = text+"...";
				Paragraph p = new Paragraph(text, fontArray[fontArray.length-1]);
		    	PdfPCell cell = new PdfPCell(p);
		    	cell.setBorder(border);
		    	cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		    	return cell;
			}
		}
	}
	
	
	/**
	 * Calculate the width of a string written in the specified font
	 * 
	 * @param text
	 * @param font
	 * @return
	 */
	private float getStringWidth(String text, Font font)
	{
		return font.getCalculatedBaseFont(true).getWidthPoint(text, font.getCalculatedSize());
	}
	
	
}	
