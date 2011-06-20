package edu.cornell.dendro.corina.io;

import java.awt.Color;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;


/*
* Usage: textComp.addCaretListener(new CurrentLineHighlighter());
*/
public class LineHighlighter implements CaretListener
{
	static final Color DEFAULT_COLOR = new Color(230, 230, 210);
	
	private Highlighter.HighlightPainter painter;
	private Object highlight;

	
	public LineHighlighter(int line)
	{
		this(null, line);
	}
	
	public LineHighlighter(Color highlightColor, int line)
	{
		Color c = highlightColor != null ? highlightColor : DEFAULT_COLOR;
		painter = new DefaultHighlighter.DefaultHighlightPainter(c);
	}
	
	public void caretUpdate(CaretEvent evt)
	{
		JTextComponent comp = (JTextComponent)evt.getSource();
		if (comp != null && highlight != null)
		{
			comp.getHighlighter().removeHighlight(highlight);
			highlight = null;
		}
		
		int pos = comp.getCaretPosition();
		Element elem = Utilities.getParagraphElement(comp, pos);
		int start = elem.getStartOffset();
		int end = elem.getEndOffset();
		
		try
		{
			highlight = comp.getHighlighter().addHighlight(start, end,
			painter);
		}
		catch (BadLocationException ex)
		{
			ex.printStackTrace();
		}
	}
}
