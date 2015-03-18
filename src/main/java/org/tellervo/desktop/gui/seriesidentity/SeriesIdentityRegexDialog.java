package org.tellervo.desktop.gui.seriesidentity;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.LineBorder;

import org.tellervo.desktop.gui.widgets.DescriptiveDialog;
import org.tellervo.desktop.ui.Builder;

public class SeriesIdentityRegexDialog extends DescriptiveDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txtObjectPattern;
	private JTextField txtElementPattern;
	private JTextField txtSamplePattern;
	private JTextField txtRadiusPattern;
	private JTextField txtSeriesPattern;
	
	String[] fieldOptions = {"Series name", "File name", "Full path", "Final folder"};
	String[] methodOptions = {"None", "Fixed width", "Regex"};
	private JTextField txtObjectTest;
	private JTextField txtElementTest;
	private JTextField txtSampleTest;
	private JTextField txtRadiusTest;
	private JTextField txtSeriesTest;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SeriesIdentityRegexDialog dialog = new SeriesIdentityRegexDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SeriesIdentityRegexDialog() {
		super("Define naming convention patterns", "Use this dialog to automatically assign names to entities based on patterns within the file or series names extracted from the input files.", null);
		setTitle("Define Patterns");
		setModal(true);
		setBounds(100, 100, 676, 379);
		
		getMainPanel().setLayout(new MigLayout("", "[grow][grow][grow][grow][grow]", "[][fill][fill][fill][fill][fill]"));
		{
			JLabel lblField = new JLabel("Field");
			getMainPanel().add(lblField, "cell 1 0,alignx center");
		}
		{
			JLabel lblMethod = new JLabel("Method");
			getMainPanel().add(lblMethod, "cell 2 0,alignx center");
		}
		{
			JLabel lblPattern = new JLabel("Pattern");
			getMainPanel().add(lblPattern, "cell 3 0,alignx center");
		}
		{
			JLabel lblTest = new JLabel("Test");
			getMainPanel().add(lblTest, "cell 4 0,alignx center");
		}
		{
			JLabel lblObject = new JLabel("Object");
			getMainPanel().add(lblObject, "cell 0 1,alignx trailing");
		}
		{
			JComboBox cboObjectField = new JComboBox();
			cboObjectField.setModel(new DefaultComboBoxModel(fieldOptions));
			getMainPanel().add(cboObjectField, "cell 1 1,growx");
		}
		{
			JComboBox cboObjectMethod = new JComboBox();
			cboObjectMethod.setModel(new DefaultComboBoxModel(methodOptions));
			getMainPanel().add(cboObjectMethod, "cell 2 1,growx");
		}
		{
			txtObjectPattern = new JTextField();
			getMainPanel().add(txtObjectPattern, "cell 3 1,growx,aligny top");
			txtObjectPattern.setColumns(10);
		}
		{
			txtObjectTest = new JTextField();
			txtObjectTest.setEditable(false);
			getMainPanel().add(txtObjectTest, "cell 4 1,growx,aligny top");
			txtObjectTest.setColumns(10);
		}
		{
			JLabel lblElement = new JLabel("Element");
			getMainPanel().add(lblElement, "cell 0 2,alignx trailing");
		}
		{
			JComboBox cboElementField = new JComboBox();
			cboElementField.setModel(new DefaultComboBoxModel(fieldOptions));
			getMainPanel().add(cboElementField, "cell 1 2,growx");
		}
		{
			JComboBox cboElementMethod = new JComboBox();
			cboElementMethod.setModel(new DefaultComboBoxModel(methodOptions));
			getMainPanel().add(cboElementMethod, "cell 2 2,grow");
		}
		{
			txtElementPattern = new JTextField();
			txtElementPattern.setColumns(10);
			getMainPanel().add(txtElementPattern, "cell 3 2,growx");
		}
		{
			txtElementTest = new JTextField();
			txtElementTest.setEditable(false);
			txtElementTest.setColumns(10);
			getMainPanel().add(txtElementTest, "cell 4 2,growx");
		}
		{
			JLabel lblSample = new JLabel("Sample");
			getMainPanel().add(lblSample, "cell 0 3,alignx trailing");
		}
		{
			JComboBox cboSampleField = new JComboBox();
			cboSampleField.setModel(new DefaultComboBoxModel(fieldOptions));
			getMainPanel().add(cboSampleField, "cell 1 3,growx");
		}
		{
			JComboBox cboSampleMethod = new JComboBox();
			cboSampleMethod.setModel(new DefaultComboBoxModel(methodOptions));
			getMainPanel().add(cboSampleMethod, "cell 2 3,growx");
		}
		{
			txtSamplePattern = new JTextField();
			txtSamplePattern.setColumns(10);
			getMainPanel().add(txtSamplePattern, "cell 3 3,growx");
		}
		{
			txtSampleTest = new JTextField();
			txtSampleTest.setEditable(false);
			txtSampleTest.setColumns(10);
			getMainPanel().add(txtSampleTest, "cell 4 3,growx");
		}
		{
			JLabel lblRadius = new JLabel("Radius");
			getMainPanel().add(lblRadius, "cell 0 4,alignx trailing");
		}
		{
			JComboBox cboRadiusField = new JComboBox();
			cboRadiusField.setModel(new DefaultComboBoxModel(fieldOptions));
			getMainPanel().add(cboRadiusField, "cell 1 4,growx");
		}
		{
			JComboBox cboRadiusMethod = new JComboBox();
			cboRadiusMethod.setModel(new DefaultComboBoxModel(methodOptions));
			getMainPanel().add(cboRadiusMethod, "cell 2 4,growx");
		}
		{
			txtRadiusPattern = new JTextField();
			txtRadiusPattern.setColumns(10);
			getMainPanel().add(txtRadiusPattern, "cell 3 4,growx");
		}
		{
			txtRadiusTest = new JTextField();
			txtRadiusTest.setEditable(false);
			txtRadiusTest.setColumns(10);
			getMainPanel().add(txtRadiusTest, "cell 4 4,growx");
		}
		{
			JLabel lblSeries = new JLabel("Series");
			getMainPanel().add(lblSeries, "cell 0 5,alignx trailing");
		}
		{
			JComboBox cboSeriesField = new JComboBox();
			cboSeriesField.setModel(new DefaultComboBoxModel(fieldOptions));
			getMainPanel().add(cboSeriesField, "cell 1 5,growx");
		}
		{
			JComboBox cboSeriesMethod = new JComboBox();
			cboSeriesMethod.setModel(new DefaultComboBoxModel(methodOptions));
			getMainPanel().add(cboSeriesMethod, "cell 2 5,growx");
		}
		{
			txtSeriesPattern = new JTextField();
			txtSeriesPattern.setColumns(10);
			getMainPanel().add(txtSeriesPattern, "cell 3 5,growx");
		}
		{
			txtSeriesTest = new JTextField();
			txtSeriesTest.setEditable(false);
			txtSeriesTest.setColumns(10);
			getMainPanel().add(txtSeriesTest, "cell 4 5,growx");
		}
	}
	
	public void validatePatterns()
	{
		
	}

}
