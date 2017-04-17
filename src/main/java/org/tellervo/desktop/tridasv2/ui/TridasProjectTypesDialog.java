package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasFile;

import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import org.tellervo.desktop.tridasv2.ui.TridasProjectTypesPanel;


public class TridasProjectTypesDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(TridasProjectTypesDialog.class);

	private final TridasProjectTypesPanel contentPanel;

	
	/**
	 * Create the dialog.
	 */
	public TridasProjectTypesDialog(Component parent, ArrayList<ControlledVoc> lsst) {
		
		contentPanel = new TridasProjectTypesPanel(lsst);
		
		
		getContentPane().add(contentPanel);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPanel.add(panel, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton("OK");
		btnOk.setActionCommand("OK");
		btnOk.addActionListener(this);
		panel.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(this);
		panel.add(btnCancel);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 491, 368);
		
		this.setModal(true);
		this.setUndecorated(true);
		
		this.pack();
		this.setTitle("File references");
		this.setIconImage(Builder.getApplicationIcon());
		this.setBounds(parent.getLocationOnScreen().x, 
				parent.getLocationOnScreen().y, 
				300, 
				300);
		

		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getActionCommand()=="Cancel")
		{
			contentPanel.setHasResults(false);
			this.dispose();
		}
		if(ev.getActionCommand()=="OK")
		{	
			contentPanel.setHasResults(true);
			this.dispose();
		}
	}
	
	public ArrayList<ControlledVoc> getList()
	{
		return contentPanel.getList();
	}

	
	public Boolean hasResults()
	{
		return contentPanel.getHasResults();
	}
	

	
}

	