/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.print;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;

public class ReportBase {
	
	// Title fonts
	Font docTypeFont = new Font(Font.FontFamily.HELVETICA, 20f, Font.BOLD);
	Font titleFont = new Font(Font.FontFamily.HELVETICA, 20f, Font.BOLD);
	Font labelTitleFont = new Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD);
	Font monsterFont = new Font(Font.FontFamily.HELVETICA, 38f, Font.BOLD);
	Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14f);
	
	// Section fonts
	Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD);
	Font subSectionFont = new Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD);
	Font subSubSectionFont = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD);
	
	// General body fonts
	Font bodyFontLarge = new Font(Font.FontFamily.HELVETICA, 14f);
	Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10f);
	Font bodyFontItalic = new Font(Font.FontFamily.HELVETICA, 10f, Font.ITALIC);
	Font commentFont = new Font(Font.FontFamily.HELVETICA, 9f, Font.ITALIC);
	Font superBodyFont = new Font(Font.FontFamily.HELVETICA, 6f);
	
	// Table fonts
	Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD);
	Font tableHeaderFontLarge = new Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD);
	Font tableBodyFont = new Font(Font.FontFamily.HELVETICA, 8f);
	
	// Line widths
	Float lineWidth = new Float(0.05);
	Float headerLineWidth = new Float(0.8);	
	
	
	protected Document document = new Document();
	protected PdfContentByte cb;


	/**
	 * Blank iText paragraph used for padding 
	 * @return Paragraph
	 */
	protected Paragraph getParagraphSpace()
	{
		Paragraph p = new Paragraph();
		
		p.add(new Chunk(" "));
		return p;
	}

}
