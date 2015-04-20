package org.tellervo.desktop.gis2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import javax.swing.JComboBox;

public class Properties extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Properties dialog = new Properties();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Properties() {
		setTitle("Properties");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[76.00px,right][grow]", "[][][]"));
		{
			JLabel lblNewLabel = new JLabel("Color:");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "cell 0 0,alignx right,aligny top");
		}
		{
			JComboBox comboBox = new JComboBox();
			int[] values = new int[] { 0, 128, 192, 255 };
		    for (int r = 0; r < values.length; r++)
		      for (int g = 0; g < values.length; g++)
		        for (int b = 0; b < values.length; b++) {
		          Color c = new Color(values[r], values[g], values[b]);
		          comboBox.addItem(c);
		        }
		    comboBox.setRenderer(new ColorComboRenderer());
			contentPanel.add(comboBox, "cell 1 0,growx");
		}
		{
			JLabel lblShape = new JLabel("Shape:");
			contentPanel.add(lblShape, "cell 0 1,alignx trailing");
		}
		{
			JComboBox comboBox = new JComboBox();
			contentPanel.add(comboBox, "cell 1 1,growx");
		}
		{
			JLabel lblOpacity = new JLabel("Opacity:");
			contentPanel.add(lblOpacity, "cell 0 2,alignx trailing");
		}
		{
			JComboBox comboBox = new JComboBox();
			contentPanel.add(comboBox, "cell 1 2,growx");
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
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
