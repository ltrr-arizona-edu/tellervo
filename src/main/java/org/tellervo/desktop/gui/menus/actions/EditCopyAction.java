package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.CopyDialog;
import org.tellervo.desktop.io.TwoColumn;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.PureStringWriter;
import org.tellervo.desktop.util.TextClipboard;

public class EditCopyAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	
	public EditCopyAction(AbstractEditor editor) {
        super("Copy", Builder.getIcon("editcopy.png", 22));
		putValue(SHORT_DESCRIPTION, "Copy");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

		this.editor = editor;
		
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		CopyDialog d = new CopyDialog(editor, editor.getSample().getRange());
		if(d.isOk())
			TextClipboard.copy(asTwoColumn(d.getChosenRange(), editor.getSample()));
		
	}

	
	public static String asTwoColumn(Range range, Sample sample) {
		try {
			int inindex = range.getStart().compareTo(sample.getRange().getStart());
			
			List tmpData = sample.getRingWidthData().subList(inindex, inindex + range.getSpan());
			List tmpCount = sample.hasCount() ? null : sample.getCount().subList(inindex, inindex + range.getSpan());
			Sample tmpSample = new Sample();
			
			tmpSample.setRange(range);
			tmpSample.setRingWidthData(tmpData);
			tmpSample.setCount(tmpCount);
			
			int estimatedLength = 10 * tmpSample.getRingWidthData().size();
			PureStringWriter w = new PureStringWriter(estimatedLength);
			BufferedWriter b = new BufferedWriter(w);
			new TwoColumn().save(tmpSample, b);
			b.close();
			return w.toString();
		} catch (IOException ioe) {
			// can't happen: i'm just writing to a buffer
			return "";
		}
	}
	
}
