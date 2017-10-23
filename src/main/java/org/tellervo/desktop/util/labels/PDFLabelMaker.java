/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.util.labels;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.pdf.PrintablePDF;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasSample;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class PDFLabelMaker {
	private LabelPage margins;
	private int nAcross;
	private int nDown;
	private int curX;
	private Document document;
	private PdfContentByte contentb;
	private PdfPTable table;
	private Font labelfont = new Font(Font.FontFamily.HELVETICA, 15f, Font.BOLD);
	private Font tinyfont = new Font(Font.FontFamily.HELVETICA, 9f);
	private Integer borderwidth = 1;
    private final static Logger log = LoggerFactory.getLogger(PDFLabelMaker.class);

	
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
		document.addAuthor("Tellervo Label Generator");
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
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		

	}
	
	public void addCell(PdfPCell cell) {
		
		cell.setBorder(borderwidth);
		
		cell.setNoWrap(false);
		cell.setFixedHeight(margins.getLabelHeight());
			
		//cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		//cell.setVerticalAlignment(Element.ALIGN_TOP);
		
		if(curX != 0)
		{
			PdfPCell gapcell = new PdfPCell();
			gapcell.setBorder(borderwidth);
			table.addCell(gapcell);
		}

		curX++;
		table.addCell(cell);
		
		if(curX == nAcross)
			//table.completeRow();
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
				barcode.setX(1.8f);
				barcode.setN(10f);
				barcode.setSize(10f);
				barcode.setBaseline(10f);
				barcode.setBarHeight(50f);
				barcode.setFont(null);
			}
			else
			{
				barcode.setBarHeight(margins.getLabelHeight() * .45f);
				barcode.setX(0.6f);
				barcode.setSize(4.0f);

			}
			
			PdfPCell lbcell = new PdfPCell();
			
			lbcell.setVerticalAlignment(Element.ALIGN_TOP);
			lbcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			Phrase p = new Phrase();

			String labelText;
			TridasGenericField labcodeField = GenericFieldUtils.findField(s, "tellervo.internal.labcodeText");
			
			if(labcodeField==null)
			{
				log.warn("labcode missing from sample.  Can't print!");
				continue;
			}
			labelText = (labcodeField != null) ? labcodeField.getValue() : s.getTitle();
			
			p.add(new Chunk(labelText, labelfont));
			//p.add(new Chunk("bbb", labelfont));
			//p.add(new Chunk(s.getIdentifier().getValue().toString(), uuidfont));
			
			//barcode.setFont(null);
			Image img = barcode.createImageWithBarcode(contentb, BaseColor.BLACK, BaseColor.GRAY);
					
			PdfPCell labcell = new PdfPCell(); 
			
			if(App.getLabName()!=null)
			{
				labcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				labcell.setVerticalAlignment(Element.ALIGN_TOP);
				Phrase labPhrase = new Phrase(App.getLabName().toUpperCase(), tinyfont);				
				labcell.addElement(labPhrase);
			}
			addCell(labcell);
			
			
			PdfPCell bccell = new PdfPCell();
			bccell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			
			bccell.addElement(img);
			bccell.addElement(p);
			addCell(bccell);
			
			lbcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			lbcell.addElement(p);
			addCell(lbcell);
			

			//addCell(new PdfPCell());
			
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
