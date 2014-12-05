package org.tellervo.desktop.gis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.fhaes.util.Builder;

public class GISPointDisplay extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtLat;
	private JTextField txtLon;
	private JButton btnCopyLat;
	private JButton btnCopyLon;


	/**
	 * Create the dialog.
	 */
	public GISPointDisplay(Double lat, Double lon) {
		
		this.setTitle("Coordinates at point");
		this.setIconImage(Builder.getApplicationIcon());
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][]"));
		{
			JLabel lblLatitude = new JLabel("Latitude:");
			contentPanel.add(lblLatitude, "cell 0 0,alignx trailing");
		}
		{
			txtLat = new JTextField();
			contentPanel.add(txtLat, "flowx,cell 1 0,growx");
			txtLat.setColumns(10);
		}
		{
			JLabel lblLongitude = new JLabel("Longitude:");
			contentPanel.add(lblLongitude, "cell 0 1,alignx trailing");
		}
		{
			txtLon = new JTextField();
			contentPanel.add(txtLon, "flowx,cell 1 1,growx");
			txtLon.setColumns(10);
		}
		{
			btnCopyLat = new JButton("Copy");
			btnCopyLat.setActionCommand("CopyLat");
			btnCopyLat.addActionListener(this);
			contentPanel.add(btnCopyLat, "cell 1 0");
		}
		{
			btnCopyLon = new JButton("Copy");
			btnCopyLon.setActionCommand("CopyLon");
			btnCopyLon.addActionListener(this);
			
			contentPanel.add(btnCopyLon, "cell 1 1");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		txtLat.setText(lat.toString());
		txtLon.setText(lon.toString());
		
	}


	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("CopyLat"))
		{
			StringSelection stringSelection = new StringSelection (txtLat.getText());
			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents (stringSelection, null);
		}
		else if(evt.getActionCommand().equals("CopyLon"))
		{
			StringSelection stringSelection = new StringSelection (txtLon.getText());
			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents (stringSelection, null);
		}
	}



}
