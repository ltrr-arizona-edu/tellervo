package edu.cornell.dendro.corina.util.labels;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.tridas.schema.TridasSample;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.XMLDebugView;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.print.ReportBase;
import edu.cornell.dendro.corina.print.SeriesReport;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.util.pdf.PrintablePDF;
import edu.cornell.dendro.corina.util.test.PrintReportFramework;

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
	
	private int horizontalAlignment = Element.ALIGN_LEFT;
	private int verticalAlignment = Element.ALIGN_MIDDLE;
	
	private Font labelfont = new Font(Font.HELVETICA, 15f, Font.BOLD);
	private Font uuidfont = new Font(Font.HELVETICA, 6f);
	
	private Integer borderwidth = 1;
	
	
	public PDFLabelMaker(LabelPage margins, OutputStream output) throws IOException, DocumentException {
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
		PdfWriter writer = PdfWriter.getInstance(document, output);
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
		
		cell.setBorder(borderwidth);
		
		cell.setNoWrap(false);
		cell.setFixedHeight(margins.getLabelHeight());
			
		//cell.setHorizontalAlignment(horizontalAlignment);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		
		if(curX != 0)
		{
			PdfPCell gapcell = new PdfPCell();
			gapcell.setBorder(borderwidth);
			table.addCell(gapcell);
		}

		curX++;
		table.addCell(cell);
		
		if(curX == nAcross)
			curX = 0;
	}
		
/*	public void addUUIDBarcode(String name, LabBarcode.Type type, UUID uuid) {
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
*/
	public void addLabelsForSamples(List<TridasSample> samples) {
		
		// Loop through samples in list
		for(TridasSample s : samples)
		{		
			Barcode128 barcode = new LabBarcode(LabBarcode.Type.SAMPLE, UUID.fromString(s.getIdentifier().getValue().toString()));
			
			// if it's tiny, hide the label
			if(margins.getLabelHeight() * .80f < barcode.getBarHeight()) {
				barcode.setBarHeight(margins.getLabelHeight() * .45f);
				barcode.setX(0.46f);
				barcode.setSize(4.0f);
				barcode.setFont(null);
			}
			else
			{
				barcode.setBarHeight(margins.getLabelHeight() * .45f);
				barcode.setX(0.6f);
				barcode.setSize(4.0f);

			}
			
			//barcode.setFont(null);
			Image img = barcode.createImageWithBarcode(contentb, Color.black, Color.gray);
						
			PdfPCell bccell = new PdfPCell();
			//bccell.setHorizontalAlignment(Element.ALIGN_CENTER);
			bccell.addElement(img);
			addCell(bccell);
			
			PdfPCell lbcell = new PdfPCell();
			
			lbcell.setVerticalAlignment(Element.ALIGN_TOP);
			lbcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			Paragraph p = new Paragraph();

			p.add(new Chunk(s.getTitle().toString(), labelfont));
			//p.add(new Chunk(s.getIdentifier().getValue().toString(), uuidfont));
		
			lbcell.addElement(p);
			addCell(lbcell);
			
		/**	PdfPTable tbl = new PdfPTable(2);
			
			tbl.addCell(bccell);
			tbl.addCell(lbcell);*/
	
		}
		

	}
	
	
	public void addTable(PdfPTable ntable) {
		
		PdfPCell cell = new PdfPCell();
		cell.addElement(ntable);
		cell.addElement(new Phrase("boo"));
		
		addCell(cell);
		
		
	}
	
	public void finish() throws DocumentException {
		while(curX != 0) {
			addCell(new PdfPCell());
		}
		document.add(table);
		document.close();
	}

	
	public static void print(List<TridasSample> samples) throws DocumentException, IOException
	{
	
		PDFLabelMaker lm = new PDFLabelMaker(new CornellSampleLabelPage(), new ByteArrayOutputStream());
		lm.getLabels(true, samples);
		
	}
	
	public static void preview(List<TridasSample> samples) throws DocumentException, IOException
	{
		PDFLabelMaker lm = new PDFLabelMaker(new CornellSampleLabelPage(), new ByteArrayOutputStream());
		lm.getLabels(false, samples);		
	}
	
	/**
	 * Function for printing or viewing labels 
	 * @param doPrint Boolean
	 * @param samples List<TridasSample>
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	private void getLabels(Boolean doPrint, List<TridasSample> samples) throws DocumentException, IOException
	{
		if(doPrint) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			PDFLabelMaker labelpage = new PDFLabelMaker(new CornellSampleLabelPage(), output);	
			
			labelpage.addLabelsForSamples(samples);
			labelpage.finish();

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
				File outputFile = File.createTempFile("seriesreport", ".pdf");
				FileOutputStream output = new FileOutputStream(outputFile);
				PDFLabelMaker labelpage = new PDFLabelMaker(new CornellSampleLabelPage(), output);	
				
				labelpage.addLabelsForSamples(samples);
				labelpage.finish();

				App.platform.openFile(outputFile);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				Alert.error("Error", "An error occurred while generating the labels.\n  See error log for further details.");
				return;
			}
		}	
	}
	

}
