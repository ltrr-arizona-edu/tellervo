package org.tellervo.desktop.gui.seriesidentity;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.widgets.DescriptiveDialog;
import org.tellervo.desktop.ui.Alert;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

public class SeriesIdentityRegexDialog extends DescriptiveDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(SeriesIdentityRegexDialog.class);

	private boolean cancelled = false;
	private SeriesIdentity testcase = null;
	
	public enum FieldOptions{
		SERIES_NAME("Series name"),
		FILE_NAME("File name"),
		FULL_PATH("Full path"),
		FINAL_FOLDER("Final folder");
		
		String humanName;
		
		FieldOptions(String humanName)
		{
			this.humanName = humanName;
		}
		
		public String getHumanName()
		{
			return this.humanName;
		}
		
		public String toString()
		{
			return this.humanName;
		}
	}
	
	public enum MethodOptions{
		NONE("None"),
		FIXED_WIDTH("Fixed width"),
		REGEX("Regex");
		
		String humanName;
		
		MethodOptions(String humanName)
		{
			this.humanName = humanName;
		}
		
		public String getHumanName()
		{
			return this.humanName;
		}
	}
	
	
	private JTextField txtObjectTest;
	private JTextField txtElementTest;
	private JTextField txtSampleTest;
	private JTextField txtRadiusTest;
	private JTextField txtSeriesTest;
	
	private JTextField txtObjectPattern;
	private JTextField txtElementPattern;
	private JTextField txtSamplePattern;
	private JTextField txtRadiusPattern;
	private JTextField txtSeriesPattern;
	private JComboBox<MethodOptions> cboSeriesMethod;
	private JComboBox<FieldOptions>  cboSeriesField;
	private JComboBox<MethodOptions> cboRadiusMethod;
	private JComboBox<FieldOptions>  cboRadiusField;
	private JComboBox<MethodOptions> cboSampleMethod;
	private JComboBox<FieldOptions>  cboSampleField;
	private JComboBox<MethodOptions> cboElementMethod;
	private JComboBox<FieldOptions>  cboElementField;
	private JComboBox<MethodOptions> cboObjectMethod;
	private JComboBox<FieldOptions>  cboObjectField;
	private JButton btnRunTests;
	
	
	/**
	 * Create the dialog.
	 */
	public SeriesIdentityRegexDialog(Window parent, SeriesIdentity testcase) {
		super(parent, "Define naming convention patterns", "Use this dialog to automatically assign names to entities based " +
				"on patterns within the file or series names extracted from the input files. You can test your patterns by " +
				"clicking the 'run tests' button which will use the values from the first series in your table. " +
				"The fixed width pattern simply requires the range of characters you would like from the field (e.g. '1-3' would extract " +
				"the first, second and third characters, whereas * returns all characters).  The regex pattern enables you to use standard regular expression notation " +
				"to extract the information you want.", null);
		setTitle("Define Patterns");
		setModal(true);
		setBounds(100, 100, 676, 379);
		this.testcase = testcase;
		
		getMainPanel().setLayout(new MigLayout("", "[][grow][grow][grow][grow]", "[][fill][fill][fill][fill][fill][]"));
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
			JLabel lblObject = new JLabel("Object code:");
			getMainPanel().add(lblObject, "cell 0 1,alignx trailing");
		}
		{
			cboObjectField = new JComboBox<FieldOptions>();
			cboObjectField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboObjectField, "cell 1 1,growx");
		}
		{
			cboObjectMethod = new JComboBox<MethodOptions>();
			cboObjectMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboObjectMethod.addActionListener(this);
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
			JLabel lblElement = new JLabel("Element code:");
			getMainPanel().add(lblElement, "cell 0 2,alignx trailing");
		}
		{
			cboElementField = new JComboBox<FieldOptions>();
			cboElementField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboElementField, "cell 1 2,growx");
		}
		{
			cboElementMethod = new JComboBox<MethodOptions>();
			cboElementMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboElementMethod.addActionListener(this);
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
			JLabel lblSample = new JLabel("Sample code:");
			getMainPanel().add(lblSample, "cell 0 3,alignx trailing");
		}
		{
			cboSampleField = new JComboBox<FieldOptions>();
			cboSampleField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboSampleField, "cell 1 3,growx");
		}
		{
			cboSampleMethod = new JComboBox<MethodOptions>();
			cboSampleMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboSampleMethod.addActionListener(this);
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
			JLabel lblRadius = new JLabel("Radius code:");
			getMainPanel().add(lblRadius, "cell 0 4,alignx trailing");
		}
		{
			cboRadiusField = new JComboBox<FieldOptions>();
			cboRadiusField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboRadiusField, "cell 1 4,growx");
		}
		{
			cboRadiusMethod = new JComboBox<MethodOptions>();
			cboRadiusMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboRadiusMethod.addActionListener(this);
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
			JLabel lblSeries = new JLabel("Series code:");
			getMainPanel().add(lblSeries, "cell 0 5,alignx trailing");
		}
		{
			cboSeriesField = new JComboBox<FieldOptions>();
			cboSeriesField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboSeriesField, "cell 1 5,growx");
		}
		{
			cboSeriesMethod = new JComboBox<MethodOptions>();
			cboSeriesMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboSeriesMethod.addActionListener(this);
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
		{
			btnRunTests = new JButton("Run tests");
			btnRunTests.setActionCommand("RunTests");
			btnRunTests.addActionListener(this);
			
			getMainPanel().add(btnRunTests, "cell 4 6,alignx right");
		}
		
		setGUIForSelections();
	}
	
	private void setGUIForSelections()
	{
		txtObjectPattern.setEnabled(cboObjectMethod.getSelectedIndex()>0);
		txtElementPattern.setEnabled(cboElementMethod.getSelectedIndex()>0);
		txtSamplePattern.setEnabled(cboSampleMethod.getSelectedIndex()>0);
		txtRadiusPattern.setEnabled(cboRadiusMethod.getSelectedIndex()>0);
		txtSeriesPattern.setEnabled(cboSeriesMethod.getSelectedIndex()>0);	
		
		this.btnOK.setEnabled(validatePatterns());
	}
	
	public boolean validatePatterns()
	{
		boolean pass = true;
		
		if(!validPattern(txtObjectPattern.getText(),  (MethodOptions) cboObjectMethod.getSelectedItem()))  return false;
		if(!validPattern(txtElementPattern.getText(), (MethodOptions) cboElementMethod.getSelectedItem())) return false;
		if(!validPattern(txtSamplePattern.getText(),  (MethodOptions) cboSampleMethod.getSelectedItem()))  return false;
		if(!validPattern(txtRadiusPattern.getText(),  (MethodOptions) cboRadiusMethod.getSelectedItem()))  return false;
		if(!validPattern(txtSeriesPattern.getText(),  (MethodOptions) cboSeriesMethod.getSelectedItem()))  return false;
		
		return pass;
	}
	
	private boolean validPattern(String pattern, MethodOptions method)
	{
		if(method.equals(MethodOptions.FIXED_WIDTH))
		{
			return validFixedWidthPattern(pattern);
		}
		else if (method.equals(MethodOptions.REGEX))
		{
			return validRegexPattern(pattern);
		}
		else if (method.equals(MethodOptions.NONE))
		{
			return true;
		}
		return false;
	}
	
	private boolean validFixedWidthPattern(String pattern)
	{
		log.debug("Validating fixed width pattern: "+pattern);
		
		if (pattern.matches("^[0-9]+-[0-9]+$"))
		{
			return true;
		}
		else if (pattern.trim().equals("*"))
		{
			return true;
		}
		return false;
		
	}
	
	private boolean validRegexPattern(String pattern)
	{
		log.debug("Validating regex pattern: "+pattern);
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource().equals(cboObjectMethod) || 
			(evt.getSource().equals(cboElementMethod)) || 
			(evt.getSource().equals(cboSampleMethod)) || 
			(evt.getSource().equals(cboRadiusMethod)) || 
			(evt.getSource().equals(cboSeriesMethod))
			)
		{
			setGUIForSelections();
		}
		else if (evt.getActionCommand().equals("OK"))
		{
			okClick();
		}
		else if (evt.getActionCommand().equals("Cancel"))
		{
			cancelClick();
		}
		else if (evt.getActionCommand().equals("RunTests"))
		{
			runTests();
		}

	}

	
	public static String getStringToTest(SeriesIdentity id, FieldOptions field)
	{
		if(field.equals(FieldOptions.SERIES_NAME))
		{
			return id.getSample().getDisplayTitle();
		}
		else if (field.equals(FieldOptions.FILE_NAME))
		{
			return id.getFile().getName();
		}
		else if (field.equals(FieldOptions.FULL_PATH))
		{
			return id.getFile().getAbsolutePath();
		}
		else if (field.equals(FieldOptions.FINAL_FOLDER))
		{
			//TODO Just final folder
			return id.getFile().getAbsolutePath();
		}
		
		return "";
		
	}
	
	public MethodOptions getSelectedMethodOption(Class<? extends ITridas> clazz)
	{
		if(clazz.equals(TridasObject.class))
		{
			return (MethodOptions) this.cboObjectMethod.getSelectedItem();
		}
		else if(clazz.equals(TridasElement.class))
		{
			return (MethodOptions) this.cboElementMethod.getSelectedItem();
		}
		else if(clazz.equals(TridasSample.class))
		{
			return (MethodOptions) this.cboSampleMethod.getSelectedItem();
		}
		if(clazz.equals(TridasRadius.class))
		{
			return (MethodOptions) this.cboRadiusMethod.getSelectedItem();
		}
		if(clazz.equals(TridasMeasurementSeries.class))
		{
			return (MethodOptions) this.cboSeriesMethod.getSelectedItem();
		}
		return null;
	}
	
	public FieldOptions getSelectedFieldOption(Class<? extends ITridas> clazz)
	{
		if(clazz.equals(TridasObject.class))
		{
			return (FieldOptions) this.cboObjectField.getSelectedItem();
		}
		else if(clazz.equals(TridasElement.class))
		{
			return (FieldOptions) this.cboElementField.getSelectedItem();
		}
		else if(clazz.equals(TridasSample.class))
		{
			return (FieldOptions) this.cboSampleField.getSelectedItem();
		}
		if(clazz.equals(TridasRadius.class))
		{
			return (FieldOptions) this.cboRadiusField.getSelectedItem();
		}
		if(clazz.equals(TridasMeasurementSeries.class))
		{
			return (FieldOptions) this.cboSeriesField.getSelectedItem();
		}
		return null;
	}
	
	public String getPattern(Class<? extends ITridas> clazz)
	{
		if(clazz.equals(TridasObject.class))
		{
			return this.txtObjectPattern.getText();
		}
		else if(clazz.equals(TridasElement.class))
		{
			return this.txtElementPattern.getText();
		}
		else if(clazz.equals(TridasSample.class))
		{
			return this.txtSamplePattern.getText();
		}
		if(clazz.equals(TridasRadius.class))
		{
			return this.txtRadiusPattern.getText();
		}
		if(clazz.equals(TridasMeasurementSeries.class))
		{
			return this.txtSeriesPattern.getText();
		}
		return null;
	}
	
	private String getTestCaseText(FieldOptions field)
	{
		
		return getStringToTest(testcase, field);
	}
	
	private void runTests()
	{

		this.txtObjectTest.setText(getPatternMatch(
				this.getTestCaseText((FieldOptions) cboObjectField.getSelectedItem()), 
				(MethodOptions) cboObjectMethod.getSelectedItem(),
				txtObjectPattern.getText()));
		
		this.txtElementTest.setText(getPatternMatch(
				this.getTestCaseText((FieldOptions) cboElementField.getSelectedItem()), 
				(MethodOptions) cboElementMethod.getSelectedItem(),
				txtElementPattern.getText()));		
		
		this.txtSampleTest.setText(getPatternMatch(
				this.getTestCaseText((FieldOptions) cboSampleField.getSelectedItem()), 
				(MethodOptions) cboSampleMethod.getSelectedItem(),
				txtSamplePattern.getText()));		
		
		this.txtRadiusTest.setText(getPatternMatch(
				this.getTestCaseText((FieldOptions) cboRadiusField.getSelectedItem()), 
				(MethodOptions) cboRadiusMethod.getSelectedItem(),
				txtRadiusPattern.getText()));	
		
		this.txtSeriesTest.setText(getPatternMatch(
				this.getTestCaseText((FieldOptions) cboSeriesField.getSelectedItem()), 
				(MethodOptions) cboSeriesMethod.getSelectedItem(),
				txtSeriesPattern.getText()));

	}

	public static String getPatternMatch(String teststring, MethodOptions method, String pattern)
	{
		if(method.equals(MethodOptions.FIXED_WIDTH))
		{
			if(pattern.trim().equals("*"))
			{
				return teststring;
			}
			
			try{
				String[] rangevalues = pattern.split("-");
				return teststring.substring(Integer.valueOf(rangevalues[0])-1, Integer.valueOf(rangevalues[1]));
			} catch (Exception e)
			{				
				e.printStackTrace();
			}
		}
		else if (method.equals(MethodOptions.REGEX))
		{
			//TODO
		}
		
		return "";
	}
	
	protected void okClick() {
		log.debug("Confirming OK...");
		
		if(!this.validatePatterns())
		{
			Alert.error(this, "Error", "Invalid patterns.  Check and try again");
			return;
			
		}
		
		this.setVisible(false);
		
	}


	protected void cancelClick() {
		log.debug("Cancelling...");
		cancelled = true;
		this.setVisible(false);
		
	}
	
	/**
	 * Did the user click cancel?
	 * 
	 * @return
	 */
	public boolean userCancelled()
	{
		return cancelled;
	}

}
