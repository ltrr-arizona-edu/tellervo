package edu.cornell.dendro.corina.print;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;

public class ReportBase {
	Font docTypeFont = new Font(Font.HELVETICA, 20f, Font.BOLD);
	Font titleFont = new Font(Font.HELVETICA, 20f, Font.BOLD);
	Font monsterFont = new Font(Font.HELVETICA, 38f, Font.BOLD);
	Font subTitleFont = new Font(Font.HELVETICA, 14f);
	Font sectionFont = new Font(Font.HELVETICA, 14f, Font.BOLD);
	Font subSectionFont = new Font(Font.HELVETICA, 10f, Font.BOLD);
	Font bodyFont = new Font(Font.HELVETICA, 10f);
	Font superBodyFont = new Font(Font.HELVETICA, 6f);
	Font bodyFontItalic = new Font(Font.HELVETICA, 10f, Font.ITALIC);
	Font tableHeaderFont = new Font(Font.HELVETICA, 10f, Font.BOLD);

	
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