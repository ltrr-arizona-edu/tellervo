package org.tellervo.desktop.util.labels.v2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.labelgen.LabBarcode;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class SampleLabelCompiler {

	private final SampleLabelLayout layout;
	private Font oneLineFont = FontFactory.getFont(FontFactory.HELVETICA, 14f);
	private Font twoLineFont = FontFactory.getFont(FontFactory.HELVETICA, 8f);
	private Font threeLineFont = FontFactory.getFont(FontFactory.HELVETICA, 6f);
	
	private PdfWriter writer;
	private final static Logger log = LoggerFactory.getLogger(SampleLabelCompiler.class);

	
	public SampleLabelCompiler(SampleLabelLayout layout)
	{
		this.layout = layout;
	}
	


	
    public static final String DEST = "/tmp/simple_table.pdf";
    
    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        SampleLabelLayout layout = new SampleLabelLayout();
        layout.setTucsonStandardLayout();
        layout.setPageSize(PageSize.LETTER.rotate());
        
        new SampleLabelCompiler(layout).createPdf(DEST);
    }
    
    
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(layout.getPageSize(), 
        		mmToPoint(layout.getLeftMargin()),
        		mmToPoint(layout.getRightMargin()), 
        		mmToPoint(layout.getTopMargin()), 
        		mmToPoint(layout.getBottomMargin()));
        writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
      
        document.open();
        PdfPTable table = new PdfPTable(3);
        
        
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("ABC-1-A");
        arr.add("ABC-10-A");
        arr.add("My ProjectName");
        
        float docwidth = document.getPageSize().getWidth()-mmToPoint(10);
        // Barcode width is static
        float barcodewidth = mmToPoint(76);
        
        // Left col width 
        float leftColWidth = this.calcMaxWidth(arr, twoLineFont);
        
        // Right col gets whatever is left
        float rightColWidth = docwidth - barcodewidth - leftColWidth;
               
        table.setTotalWidth(new float[]{ leftColWidth, barcodewidth, rightColWidth  });
        table.setLockedWidth(true);
        
        

        

    
        table.addCell(getCell("ABC-1-A", "My ProjectName", rightColWidth, Element.ALIGN_RIGHT));
        table.addCell(getBarcodeCell(UUID.fromString("c991328e-1987-11e7-819b-cf39ddfd4a34")));
        table.addCell(getCell("P.W.Brewer 2016", "The name of my site ", rightColWidth, Element.ALIGN_LEFT));        

        table.addCell(getCell("ABC-10-A", "My ProjectName", rightColWidth, Element.ALIGN_RIGHT));
        table.addCell(getBarcodeCell(UUID.fromString("c991328e-1987-11e7-819b-cf39ddfd4a34")));
        table.addCell(getCell("P.W.Brewer 2016", "The name of my site sdafh asdhfj kasdkjfahsdkf hasdhf kjashdkj haksjdh fkjashdk haskdjh fkjasdh fkjahsdjk fhaksjdh fkjashdk", rightColWidth, Element.ALIGN_LEFT));
    
        table.addCell(getCell("ABC-10-A", Element.ALIGN_RIGHT));
        table.addCell(getBarcodeCell(UUID.fromString("c991328e-1987-11e7-819b-cf39ddfd4a34")));
        table.addCell(getCell("P.W.Brewer 2016", "The name of my site sdafh asdhfj kasdkjfahsdkf hasdhf kjashdkj haksjdh fkjashdk haskdjh fkjasdh fkjahsdjk fhaksjdh fkjashdk", "My project", rightColWidth, Element.ALIGN_LEFT));        
    
        
        document.add(table);
        document.close();
    }
    
    /**
     * Calculate the maximum width required to display the strings in an array with the specified font
     * 
     * @param array
     * @param font
     * @return
     */
    private float calcMaxWidth(ArrayList<String> array, Font font)
    {
    	float maxwidth = 0;
    	
    	for(String s: array)
    	{
    		float thiswidth = font.getCalculatedBaseFont(true).getWidthPoint(s, font.getCalculatedSize())+5;
    		if(thiswidth>maxwidth) maxwidth = thiswidth;
    	}
    	
    	return maxwidth;
    }
    
    private PdfPCell getBarcodeCell(UUID uuid)
    {
     	Barcode128 barcode = new LabBarcode(LabBarcode.Type.SAMPLE, uuid);
				
		barcode.setBarHeight(mmToPoint(9));
		barcode.setFont(null);
		
        PdfPCell cell = new PdfPCell(barcode.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY), true);
    	cell.setFixedHeight(mmToPoint(8));
    	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    	cell.setBorderWidthLeft(0);
    	cell.setBorderWidthRight(0);
    	return cell;
    }
    
    private PdfPCell getCell(String str, String str2, float maxwidth, int align)
    {
    	Font font = twoLineFont;
    	
    	// Calculate if we can get away with 2 lines of text
    	ArrayList<String> array = new ArrayList<String>();
    	array.add(str);
    	array.add(str2);
    	float twolinewidth = this.calcMaxWidth(array, twoLineFont);
    	
    	if(twolinewidth>maxwidth)
    	{
    		// Text won't fit on two lines so shrink font and do three lines instead
    		// If it won't fit on three lines then it will truncate
    		font = threeLineFont;
    	}
    	
    	Phrase p = new Phrase();
    	p.setFont(font);
    	p.add(str);
    	p.add(Chunk.NEWLINE);
    	p.add(str2);
    	 	
 
    	PdfPCell cell = new PdfPCell(p);
    	
    	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    	cell.setHorizontalAlignment(align);
    	cell.setUseAscender(true);
    	//cell.setUseDescender(true);
    	cell.setPadding(1);
    	cell.setFixedHeight(mmToPoint(8));
    	cell.setBorderWidthLeft(0);
    	cell.setBorderWidthRight(0);
    	//cell.setBorderWidthBottom(0);
    	
    	
    	
    	return cell;
    }
    
    private PdfPCell getCell(String str, String str2, String str3, float maxwidth, int align)
    {
    	Font font =threeLineFont;
      	
    	Phrase p = new Phrase();
    	p.setFont(font);
    	
    	
    	p.add(truncateString(str, font, maxwidth));
    	p.add(Chunk.NEWLINE);
    	p.add(truncateString(str2, font, maxwidth));
    	p.add(Chunk.NEWLINE);
    	p.add(truncateString(str3, font, maxwidth));
 
    	PdfPCell cell = new PdfPCell(p);
    	
    	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    	cell.setHorizontalAlignment(align);
    	cell.setUseAscender(true);
    	//cell.setUseDescender(true);
    	cell.setPadding(1);
    	cell.setFixedHeight(mmToPoint(8));
    	cell.setBorderWidthLeft(0);
    	cell.setBorderWidthRight(0);
    	//cell.setBorderWidthBottom(0);
    	
    	
    	
    	return cell;
    }
    
    /**
     * Calculate the width of the string when displayed with the specified font and truncate if it doesn't fit in the availableWidth
     * 
     * @param content
     * @param font
     * @param availableWidth
     * @return
     */
    private String truncateString(String content, Font font, float availableWidth )
    {      
        
        float fullwidth = font.getCalculatedBaseFont(true).getWidthPoint(content, font.getCalculatedSize());
        
        if(fullwidth>availableWidth-5)
        {
        	return truncateString(content.substring(0, content.length()-1), font, availableWidth);
        }
        else
        {
        	return content;
        }
        
    }
    
    private PdfPCell getCell(String str, int align)
    {
    	Phrase p = new Phrase();
    	
    	p.setFont(oneLineFont);
    	p.add(str);
    	
    	
    	PdfPCell cell = new PdfPCell(p);
    	cell.setFixedHeight(mmToPoint(8));
    	cell.setBorderWidthLeft(0);
    	cell.setBorderWidthRight(0);
    	//cell.setBorderWidthBottom(0);
    	cell.setHorizontalAlignment(align);
    	
    	return cell;
    }
    
    public static int mmToPoint(Double mm)
    {
    	return ((int) (mm * 2.8346439));
    }
    
    public static int mmToPoint(int mm)
    {
    	return ((int) (mm * 2.8346439));
    }
}
