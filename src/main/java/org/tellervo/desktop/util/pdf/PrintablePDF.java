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
package org.tellervo.desktop.util.pdf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

public class PrintablePDF implements Printable, Pageable {
	private final PDFFile pdf;
	private final RandomAccessFile associatedFile;
	
	private PageFormat pageFormat;
	private PrinterJob printJob;
	
	/**
	 * Create a new Printable pdf
	 * @param pdf The PDFFile to print
	 * @param associatedFile A randomAccessFile to close when this class is finalized (or null)
	 * @param a page format (or null for default)
	 */
	public PrintablePDF(PDFFile pdf, RandomAccessFile associatedFile, PageFormat pageFormat) {
		this.pdf = pdf;
		this.associatedFile = associatedFile;

		// create a new print job
		this.printJob = PrinterJob.getPrinterJob();
		// use the default page if we have a null format
		this.pageFormat = (pageFormat == null) ? printJob.defaultPage() : pageFormat;
		
		printJob.setPageable(this);
		printJob.setJobName("Tellervo Report");
	}
	
	/**
	 * @see #PrintablePDF(PDFFile, RandomAccessFile, PageFormat)
	 * @param pdf
	 * @param associatedFile
	 */
	public PrintablePDF(PDFFile pdf, RandomAccessFile associatedFile) {
		this(pdf, associatedFile, null);
	}

	/**
	 * Get the current pageFormat
	 * @return the pageFormat
	 */
	public PageFormat getPageFormat() {
		return pageFormat;
	}
	
	/**
	 * Change the associated page format
	 * @param pageFormat
	 */
	public void setPageFormat(PageFormat pageFormat) {
		this.pageFormat = pageFormat;
	}
	
	/**
	 * Shortcut method for changing page orientation
	 * Works the same as calling getPageFormat().setOrientation(orientation)
	 * @see PageFormat#setOrientation(int)
	 * @param orientation one of PageFormat.LANDSCAPE, PageFormat.PORTRIAT, PageFormat.REVERSE_LANDSCAPE, etc
	 */
	public void setPageOrientation(int orientation) {
		pageFormat.setOrientation(orientation);
	}
	
	/**
	 * @return A page format that has the minimum acceptable margins for the current printer
	 */
	private PageFormat minimumMarginsFormat(PageFormat format) {
		// get the paper
		Paper paper = format.getPaper();
		
		// set the paper to no margins
		paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
		
		// re-set with the marginless paper
		format.setPaper(paper);

		// validate for the chosen printer in this printjob
		return printJob.validatePage(format);
	}
	
	/**
	 * Actually print the PDF
	 * @throws PrinterException
	 */
	public void print(boolean showDialog) throws PrinterException {
		if(!showDialog || printJob.printDialog()) {
			// set the minimum margins for this paper (java defaults to 1"... wtf?)
			pageFormat = minimumMarginsFormat(pageFormat);
			
			printJob.print();
		}
	}

	public int getNumberOfPages() {
		return pdf.getNumPages();
	}

	public PageFormat getPageFormat(int pageIndex)
			throws IndexOutOfBoundsException {	
		return pageFormat;
	}

	public Printable getPrintable(int pageIndex)
			throws IndexOutOfBoundsException {
		return this;
	}

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		
		// Check for a page
		if(pageIndex >= pdf.getNumPages())
			return NO_SUCH_PAGE;
	
		// is this necessary? 
		graphics.translate(0, 0);
		
		PDFPage page = pdf.getPage(pageIndex + 1);
		PDFRenderer renderer = new PDFRenderer(page, (Graphics2D) graphics,
				getImageableRectangle(pageFormat), null, Color.white);
		
		try {
			// umm... wait for finish what? Apparently this is required and not well documented!
			page.waitForFinish();
			renderer.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return PAGE_EXISTS;
	}

	private Rectangle getImageableRectangle(PageFormat format) {
		return new Rectangle(
				(int) format.getImageableX(), (int) format.getImageableY(),
				(int) format.getImageableWidth(), (int) format.getImageableHeight());
	}
	
	@Override
	protected void finalize() {
		// on GC, make sure my file closes
		if(associatedFile != null) {
			try {
				associatedFile.close();
			} catch (Throwable e) {
			}
		}
	}
	
	/**
	 * Create a new printable pdf from the given file
	 * @param pdfFile
	 * @return a PrintablePDF object
	 * @throws IOException
	 */
	public static PrintablePDF fromFile(File pdfFile) throws IOException {
		// open the file as a random access file
		RandomAccessFile file = new RandomAccessFile(pdfFile, "r");
		// get the file as a read-only byte array
		ByteBuffer pdfBytes = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 
				0, file.getChannel().size());
		
		return new PrintablePDF(new PDFFile(pdfBytes), file);
	}
	
	/**
	 * @param pdfBytes a ByteBuffer
	 * @return a PrintablePDF object
	 * @throws IOException
	 */
	public static PrintablePDF fromByteBuffer(ByteBuffer pdfBytes) throws IOException {
		return new PrintablePDF(new PDFFile(pdfBytes), null);
	}
	
	/**
	 * @param pdfBytes an array of byte[]
	 * @return a PrintablePDF object
	 * @throws IOException
	 */
	public static PrintablePDF fromByteArray(byte[] pdfBytes) throws IOException {
		return fromByteBuffer(ByteBuffer.wrap(pdfBytes));
	}
}
