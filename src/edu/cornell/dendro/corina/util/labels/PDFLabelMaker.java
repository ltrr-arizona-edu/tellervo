package edu.cornell.dendro.corina.util.labels;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFLabelMaker {
	private LabelPage margins;
	
	private int nAcross;
	private int nDown;

	private int curX;
	@SuppressWarnings("unused")
	private int curY;
	
	private Document document;
	private PdfContentByte contentb;
	private PdfWriter writer;
	
	private PdfPTable table;
	
	private int horizontalAlignment = Element.ALIGN_CENTER;
	private int verticalAlignment = Element.ALIGN_MIDDLE;
	
	public PDFLabelMaker(LabelPage margins, File file) throws IOException, DocumentException {
		this.margins = margins;
		
		float sides, page, h, w, labelGap;

		// calculate the number of labels wide this page is
		page = margins.getPageSize().getWidth();
		labelGap = margins.getLabelHorizontalGap();
		w = margins.getLabelWidth();
		sides = margins.getPageLeftMargin() + margins.getPageRightMargin();
		if(sides + w > page)
			throw new IllegalArgumentException("0 labels fit across");
		
		for(int i = 1; sides + (((float)i) * w) + (((float)(i - 1)) * labelGap) <= page; i++) {
			nAcross = i;
		}

		// the number of labels 'tall'
		page = margins.getPageSize().getHeight();
		labelGap = margins.getLabelVerticalGap();
		h = margins.getLabelHeight();
		sides = margins.getPageTopMargin() + margins.getPageBottomMargin();
		if(sides + h > page)
			throw new IllegalArgumentException("0 labels fit down");
		
		for(int i = 1; sides + (((float)i) * h) + (((float)(i - 1)) * labelGap) <= page; i++) {
			nDown = i;
		}

		System.out.println("LABELS: across: " + nAcross + ", down: " + nDown);
		
		document = new Document(margins.getPageSize());
		writer = PdfWriter.getInstance(document, new FileOutputStream(file));

		document.addAuthor("Corina Label Generator");
		document.addCreationDate();
		
		document.open();
		
		contentb = writer.getDirectContent();
		// first off, pdfs are weird and go from high y coordinates to low for pages (ie, highest y = top of page)
		// also, this sets the page margins
		new ColumnText(contentb).setSimpleColumn(
				margins.getPageLeftMargin(), 	// lower x margin
				margins.getPageBottomMargin(),	// bottom y margin
				margins.getPageSize().getWidth() - (margins.getPageRightMargin() + margins.getPageLeftMargin()),  // upper x margin
				margins.getPageSize().getHeight() - (margins.getPageTopMargin() + margins.getPageBottomMargin()), // upper y margin
				0, 						// no leading
				Element.ALIGN_MIDDLE	// align in the middle of the label
				);
		
		int realCols = nAcross + nAcross - 1; // number of labels + number of intra-label gaps
		
		float colwidth[] = new float[realCols];
		float totalWidth = 0;
		for(int i = 0; i < realCols; i++) {
			if((i & 1) == 1) {
				// odd, so it's a margin
				totalWidth += margins.getLabelHorizontalGap();
				colwidth[i] = margins.getLabelHorizontalGap();
			}
			else {
				// even, so it's an actual label
				totalWidth += margins.getLabelWidth();
				colwidth[i] = margins.getLabelWidth();
			}
		}
				
		table = new PdfPTable(realCols); 
		table.setTotalWidth(totalWidth);
		table.setWidths(colwidth);
		table.setLockedWidth(true);
		table.getDefaultCell().setPadding(0);
		
		curX = curY = 0;
	}
	
	public void addCell(PdfPCell cell) {
		cell.setNoWrap(true);
		cell.setFixedHeight(margins.getLabelHeight());
		cell.setHorizontalAlignment(horizontalAlignment);
		cell.setVerticalAlignment(verticalAlignment);
		
		if(curX != 0)
			table.addCell("");

		curX++;
		table.addCell(cell);
		
		if(curX == nAcross)
			curX = 0;
	}
		
	public void addUUIDBarcode(String name, LabBarcode.Type type, UUID uuid) {
		Barcode128 barcode = new LabBarcode(type, uuid);
		
		// if it's tiny, hide the label
		if(margins.getLabelHeight() * .80f < barcode.getBarHeight()) {
			barcode.setBarHeight(margins.getLabelHeight() * .55f);
			barcode.setSize(6.0f);
		}
		else
			barcode.setSize(6.0f);
		
		Image img = barcode.createImageWithBarcode(contentb, Color.black, Color.gray);
		addCell(new PdfPCell(img));
	}

	
	public void addTable(PdfPTable ntable) {
		
	}
	
	public void finish() throws DocumentException {
		while(curX != 0) {
			addCell(new PdfPCell());
		}
		document.add(table);
		document.close();
	}

	//private static Font barcodeFont = FontFactory.getFont(FontFactory.COURIER);
	//private static Font tinybarcodeFont = FontFactory.getFont(FontFactory.HELVETICA, 6.0f, Font.NORMAL);
	
	public static void main(String args[]) {
		try {
			PDFLabelMaker m = new PDFLabelMaker(new PetesCustomLabels(), new File("output.pdf"));
			
			for(int i = 10; i < 100; i++) {
				UUID uuid = UUID.randomUUID();
				m.addUUIDBarcode("Cell " + i, LabBarcode.Type.SAMPLE, uuid);
			}
			
			m.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
