package org.tellervo.desktop.gui.seriesidentity;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.Icon;

import org.tellervo.desktop.gui.widgets.DescriptiveDialog;
import org.tridas.io.AbstractDendroFormat;

import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.BorderLayout;
import java.io.File;

public class IdentifySeriesDialog extends DescriptiveDialog implements TableModelListener {

	private static final long serialVersionUID = 1L;
	protected IdentifySeriesPanel panel;
	
	public IdentifySeriesDialog(Window parent, File[] files, AbstractDendroFormat filetype) {
		super(parent, "Import Data", "Below are the series extracted from the data file you selected.  To import them "
				+ "into the Tellervo database you need to identify which object, element, sample, radius and series they "
				+ "belong to.  Some of these entities may already be in the database, others may need to be generated. You "
				+ "can manually enter the names for each in the table below, or alternatively you can define them using "
				+ "patterns in the file name, folder, or series 'keycode' from the original data file.", null);
		
		panel = new IdentifySeriesPanel(this, files, filetype);
		getMainPanel().add(panel, BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(800, 600));
		this.setLocationRelativeTo(parent);
		panel.model.addTableModelListener(this);
		btnOK.setEnabled(false);
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("OK"))
		{
			panel.commit();
		}
		else if (evt.getActionCommand().equals("Cancel"))
		{
			dispose();
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		this.btnOK.setEnabled(panel.model.isTableComplete());
		
	}

}
