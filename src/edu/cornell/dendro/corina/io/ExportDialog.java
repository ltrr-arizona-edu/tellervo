package edu.cornell.dendro.corina.io;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.cornell.dendro.corina.formats.Filetype;
import edu.cornell.dendro.corina.formats.PackedFileType;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.Help;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.layouts.DialogLayout;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.util.PureStringWriter;
import edu.cornell.dendro.corina.util.TextClipboard;

public class ExportDialog extends JDialog {


	ExportUI mainPanel;
	
	/**
	 Create and display a sample-export dialog.

	 @param samples the list of samples to export
	 @param parent window
	 */
	public ExportDialog(ElementList elements){
		//super(parent, I18n.getText("export"), true);
			
		setupGui(elements);

	}

	public ExportDialog(Element element){
		ElementList elements = ElementList.singletonList(element);
		setupGui(elements);
	}
	
	public ExportDialog(Sample s){
		
		ElementList elements = ElementList.singletonList(new Element((BaseSample) s));
		setupGui(elements);
	}
	

	public void setupGui(ElementList elements)
	{
		mainPanel = new ExportUI(this, elements);
		this.setContentPane(mainPanel);
		mainPanel.panelPreview.setVisible(false);
		
		this.setTitle("Export data from database...");
		
		this.setMinimumSize(new java.awt.Dimension(525, 270));
		
		this.pack();
		Center.center(this);
		
		
		this.setVisible(true);
	}
	
	

	


}
