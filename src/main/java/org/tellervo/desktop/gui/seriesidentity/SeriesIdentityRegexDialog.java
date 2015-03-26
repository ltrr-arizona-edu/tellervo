package org.tellervo.desktop.gui.seriesidentity;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.DescriptiveDialog;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import javax.swing.JPanel;
import java.awt.Font;

public class SeriesIdentityRegexDialog extends DescriptiveDialog implements ActionListener, DocumentListener{

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
		
		public String toString()
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
	private JButton btnSaveConfiguration;
	private JButton btnLoadDefinition;
	private JPanel panel;
	
	
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
		setBounds(100, 100, 676, 450);
		this.testcase = testcase;
		
		getMainPanel().setLayout(new MigLayout("", "[grow][grow][grow][grow][grow]", "[][][fill][fill][fill][fill][fill][]"));
		{
			panel = new JPanel();
			getMainPanel().add(panel, "cell 0 0 5 1,alignx center,growy");
			panel.setLayout(new MigLayout("", "[][][]", "[32px,fill]"));
			{
				btnLoadDefinition = new JButton("Load definition");
				btnLoadDefinition.setFont(new Font("Dialog", Font.BOLD, 9));
				panel.add(btnLoadDefinition, "flowx,cell 0 0");
				btnLoadDefinition.setIcon(Builder.getIcon("fileopen.png", 22));
				btnLoadDefinition.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						loadDefinition();
						
					}
					
				});
			}
			{
				btnSaveConfiguration = new JButton("Save definition");
				btnSaveConfiguration.setFont(new Font("Dialog", Font.BOLD, 9));
				panel.add(btnSaveConfiguration, "cell 1 0,alignx left,aligny top");
				btnSaveConfiguration.setIcon(Builder.getIcon("filesave.png", 22));
				btnSaveConfiguration.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						saveDefinition();
						
					}
					
				});
				{
					btnRunTests = new JButton("Run tests");
					btnRunTests.setFont(new Font("Dialog", Font.BOLD, 9));
					btnRunTests.setIcon(Builder.getIcon("button_ok.png", 22));
					panel.add(btnRunTests, "cell 2 0");
					btnRunTests.setActionCommand("RunTests");
					btnRunTests.addActionListener(this);
				}
			}
		}
		{
			JLabel lblField = new JLabel("Field");
			getMainPanel().add(lblField, "cell 1 1,alignx center");
		}
		{
			JLabel lblMethod = new JLabel("Method");
			getMainPanel().add(lblMethod, "cell 2 1,alignx center");
		}
		{
			JLabel lblPattern = new JLabel("Pattern");
			getMainPanel().add(lblPattern, "cell 3 1,alignx center");
		}
		{
			JLabel lblTest = new JLabel("Test");
			getMainPanel().add(lblTest, "cell 4 1,alignx center");
		}
		{
			JLabel lblObject = new JLabel("Object code:");
			getMainPanel().add(lblObject, "cell 0 2,alignx trailing");
		}
		{
			cboObjectField = new JComboBox<FieldOptions>();
			cboObjectField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboObjectField, "cell 1 2,growx");
		}
		{
			cboObjectMethod = new JComboBox<MethodOptions>();
			cboObjectMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboObjectMethod.addActionListener(this);
			getMainPanel().add(cboObjectMethod, "cell 2 2,growx");
		}
		{
			txtObjectPattern = new JTextField();
			getMainPanel().add(txtObjectPattern, "cell 3 2,growx,aligny top");
			txtObjectPattern.setColumns(10);
			txtObjectPattern.getDocument().addDocumentListener(this);
		}
		{
			txtObjectTest = new JTextField();
			txtObjectTest.setEditable(false);
			getMainPanel().add(txtObjectTest, "cell 4 2,growx,aligny top");
			txtObjectTest.setColumns(10);
		}
		{
			JLabel lblElement = new JLabel("Element code:");
			getMainPanel().add(lblElement, "cell 0 3,alignx trailing");
		}
		{
			cboElementField = new JComboBox<FieldOptions>();
			cboElementField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboElementField, "cell 1 3,growx");
		}
		{
			cboElementMethod = new JComboBox<MethodOptions>();
			cboElementMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboElementMethod.addActionListener(this);
			getMainPanel().add(cboElementMethod, "cell 2 3,grow");
		}
		{
			txtElementPattern = new JTextField();
			txtElementPattern.setColumns(10);
			getMainPanel().add(txtElementPattern, "cell 3 3,growx");
			txtElementPattern.getDocument().addDocumentListener(this);

		}
		{
			txtElementTest = new JTextField();
			txtElementTest.setEditable(false);
			txtElementTest.setColumns(10);
			getMainPanel().add(txtElementTest, "cell 4 3,growx");
		}
		{
			JLabel lblSample = new JLabel("Sample code:");
			getMainPanel().add(lblSample, "cell 0 4,alignx trailing");
		}
		{
			cboSampleField = new JComboBox<FieldOptions>();
			cboSampleField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboSampleField, "cell 1 4,growx");
		}
		{
			cboSampleMethod = new JComboBox<MethodOptions>();
			cboSampleMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboSampleMethod.addActionListener(this);
			getMainPanel().add(cboSampleMethod, "cell 2 4,growx");
		}
		{
			txtSamplePattern = new JTextField();
			txtSamplePattern.setColumns(10);
			getMainPanel().add(txtSamplePattern, "cell 3 4,growx");
			txtSamplePattern.getDocument().addDocumentListener(this);

		}
		{
			txtSampleTest = new JTextField();
			txtSampleTest.setEditable(false);
			txtSampleTest.setColumns(10);
			getMainPanel().add(txtSampleTest, "cell 4 4,growx");
		}
		{
			JLabel lblRadius = new JLabel("Radius code:");
			getMainPanel().add(lblRadius, "cell 0 5,alignx trailing");
		}
		{
			cboRadiusField = new JComboBox<FieldOptions>();
			cboRadiusField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboRadiusField, "cell 1 5,growx");
		}
		{
			cboRadiusMethod = new JComboBox<MethodOptions>();
			cboRadiusMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboRadiusMethod.addActionListener(this);
			getMainPanel().add(cboRadiusMethod, "cell 2 5,growx");
		}
		{
			txtRadiusPattern = new JTextField();
			txtRadiusPattern.setColumns(10);
			getMainPanel().add(txtRadiusPattern, "cell 3 5,growx");
			txtRadiusPattern.getDocument().addDocumentListener(this);

		}
		{
			txtRadiusTest = new JTextField();
			txtRadiusTest.setEditable(false);
			txtRadiusTest.setColumns(10);
			getMainPanel().add(txtRadiusTest, "cell 4 5,growx");
		}
		{
			JLabel lblSeries = new JLabel("Series code:");
			getMainPanel().add(lblSeries, "cell 0 6,alignx trailing");
		}
		{
			cboSeriesField = new JComboBox<FieldOptions>();
			cboSeriesField.setModel(new DefaultComboBoxModel<FieldOptions>(FieldOptions.values()));
			getMainPanel().add(cboSeriesField, "cell 1 6,growx");
		}
		{
			cboSeriesMethod = new JComboBox<MethodOptions>();
			cboSeriesMethod.setModel(new DefaultComboBoxModel<MethodOptions>(MethodOptions.values()));
			cboSeriesMethod.addActionListener(this);
			getMainPanel().add(cboSeriesMethod, "cell 2 6,growx");
		}
		{
			txtSeriesPattern = new JTextField();
			txtSeriesPattern.setColumns(10);
			getMainPanel().add(txtSeriesPattern, "cell 3 6,growx");
			txtSeriesPattern.getDocument().addDocumentListener(this);

		}
		{
			txtSeriesTest = new JTextField();
			txtSeriesTest.setEditable(false);
			txtSeriesTest.setColumns(10);
			getMainPanel().add(txtSeriesTest, "cell 4 6,growx");
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
		if(!validPattern(txtObjectPattern.getText(),  (MethodOptions) cboObjectMethod.getSelectedItem()))  {
			txtObjectPattern.setBorder(getBorderInvalid());
			pass = false;
		}
		else
		{
			txtObjectPattern.setBorder(getBorderValid());
		}
		
		if(!validPattern(txtElementPattern.getText(), (MethodOptions) cboElementMethod.getSelectedItem())) {
			txtElementPattern.setBorder(getBorderInvalid());
			pass = false;
		}
		else
		{
			txtElementPattern.setBorder(getBorderValid());
		}
		
		
		if(!validPattern(txtSamplePattern.getText(),  (MethodOptions) cboSampleMethod.getSelectedItem())) {
			txtSamplePattern.setBorder(getBorderInvalid());
			pass = false;
		}
		else
		{
			txtSamplePattern.setBorder(getBorderValid());
		}
		
		if(!validPattern(txtRadiusPattern.getText(),  (MethodOptions) cboRadiusMethod.getSelectedItem())){
			txtRadiusPattern.setBorder(getBorderInvalid());
			pass = false;
		}
		else
		{
			txtRadiusPattern.setBorder(getBorderValid());
		}
		
		if(!validPattern(txtSeriesPattern.getText(),  (MethodOptions) cboSeriesMethod.getSelectedItem())){
			txtSeriesPattern.setBorder(getBorderInvalid());
			pass = false;
		}
		else
		{
			txtSeriesPattern.setBorder(getBorderValid());
		}
		
		return pass;
	}
	
	private Border getBorderInvalid()
	{
		return new LineBorder(Color.RED);
		
	}
	
	private Border getBorderValid()
	{
		JTextField tf = new JTextField();
		return tf.getBorder();
		
	}
	
	private boolean validPattern(String pattern, MethodOptions method)
	{
		if(method.equals(MethodOptions.FIXED_WIDTH))
		{
			if (pattern.matches("^[0-9]+-[0-9]+$"))
			{
				return true;
			}
			else if (pattern.trim().equals("*"))
			{
				return true;
			}
			else if (pattern.matches("^[0-9]+"))
			{
				return true;
			}
			return false;
			
		}
		else if (method.equals(MethodOptions.REGEX))
		{
			
			try{
				Pattern.compile(pattern);
				return true;
			} catch (PatternSyntaxException ex )
			{
				return false;
			}
		}
		else if (method.equals(MethodOptions.NONE))
		{
			return true;
		}
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
				// Get range values as integer positions
				String[] rangevalues = pattern.split("-");
				Integer[] rangeints = new Integer[rangevalues.length];
				for(int i=0; i<rangevalues.length; i++)
				{
						rangeints[i] = Integer.valueOf(rangevalues[i]);
				}
				
				if(rangevalues.length==2)
				{
					// Set end position to end of string if necessary
					if(rangeints[1]>=teststring.length()) rangeints[1]=teststring.length()-1;
					return teststring.substring(rangeints[0]-1, rangeints[1]);
				}
				else if (rangevalues.length==1)
				{
					return teststring.substring(rangeints[0]-1, rangeints[0]);
				}
			} catch (Exception e)
			{				
				log.error("Failed to get fixed width pattern. "+ e.getMessage());
			}
		}
		else if (method.equals(MethodOptions.REGEX))
		{

			try {
				Pattern regexpattern = Pattern.compile(pattern);
				Matcher matcher = regexpattern.matcher(teststring);

				while (matcher.find()) {
					return matcher.group();
				}
			} catch (Exception e) {
				log.error("Failed to perform regex pattern match. "
						+ e.getMessage());

			}
  
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
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		setGUIForSelections();
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		setGUIForSelections();
		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		setGUIForSelections();
		
	}
	
	private void loadDefinition()
	{
		File thisFile = null;
		JFileChooser fc = new JFileChooser();
	
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new CFGFileFilter());
		
		// Pick the last used directory by default
		try{
			File lastDirectory = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
			if(lastDirectory != null){
				fc.setCurrentDirectory(lastDirectory);
			}
		} catch (Exception e)
		{
		}
		
		int retValue = fc.showOpenDialog(this);
		if (retValue == JFileChooser.APPROVE_OPTION) {
			thisFile = fc.getSelectedFile();
			// Remember this folder for next time
			App.prefs.setPref(PrefKey.FOLDER_LAST_READ, thisFile.getPath());

		}
		
		if (thisFile == null) {
			return;
		}
		
		
	    BufferedReader br = null;
	    try {
	    	br = new BufferedReader(new FileReader(thisFile));
	        
	    	String line = br.readLine();
	        this.cboObjectField.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.cboObjectMethod.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.txtObjectPattern.setText(line);
	        
	    	line = br.readLine();
	        this.cboElementField.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.cboElementMethod.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.txtElementPattern.setText(line);
	        
	    	line = br.readLine();
	        this.cboSampleField.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.cboSampleMethod.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.txtSamplePattern.setText(line);
	        
	    	line = br.readLine();
	        this.cboRadiusField.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.cboRadiusMethod.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.txtRadiusPattern.setText(line);
	        
	    	line = br.readLine();
	        this.cboSeriesField.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.cboSeriesMethod.setSelectedIndex(Integer.parseInt(line));
	        line = br.readLine();
	        this.txtSeriesPattern.setText(line);
	    
	    } catch (FileNotFoundException e) {
	    	
			// TODO Auto-generated catch block
			e.printStackTrace();
	    } catch (NumberFormatException e)
	    {
	    	
	    }
	    catch (IOException ex)
	    {
	    	
		} finally {
	        try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	private void saveDefinition()
	{
		BufferedWriter writer = null;
		
		File thisFile = null;
		JFileChooser fc = new JFileChooser();
	
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new CFGFileFilter());
		
		// Pick the last used directory by default
		try{
			File lastDirectory = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
			if(lastDirectory != null){
				fc.setCurrentDirectory(lastDirectory);
			}
		} catch (Exception e)
		{
		}
		
		int retValue = fc.showSaveDialog(this);
		if (retValue == JFileChooser.APPROVE_OPTION) {
			thisFile = fc.getSelectedFile();
			// Remember this folder for next time
			App.prefs.setPref(PrefKey.FOLDER_LAST_READ, thisFile.getPath());

		}
		
		if (thisFile == null) {
			return;
		}
		

        try {
        	

			writer = new BufferedWriter(new FileWriter(thisFile));
        	
			String str = cboObjectField.getSelectedIndex() + System.lineSeparator() + cboObjectMethod.getSelectedIndex() + System.lineSeparator() + txtObjectPattern.getText();
			writer.write(str);
			
			str = cboElementField.getSelectedIndex() + System.lineSeparator() + cboElementMethod.getSelectedIndex() + System.lineSeparator() + txtElementPattern.getText();
			writer.write(str);
			
			str = cboSampleField.getSelectedIndex() + System.lineSeparator() + cboSampleMethod.getSelectedIndex() + System.lineSeparator() + txtSamplePattern.getText();
			writer.write(str);
			
			str = cboRadiusField.getSelectedIndex() + System.lineSeparator() + cboRadiusMethod.getSelectedIndex() + System.lineSeparator() + txtRadiusPattern.getText();
			writer.write(str);
			
			str = cboSeriesField.getSelectedIndex() + System.lineSeparator() + cboSeriesMethod.getSelectedIndex() + System.lineSeparator() + txtSeriesPattern.getText();
			writer.write(str);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
	}
	
    protected static class CFGFileFilter extends javax.swing.filechooser.FileFilter
    {
        public CFGFileFilter()
        {
        }

        public boolean accept(File file)
        {
            if (file == null || file.isDirectory())
                return true;

            if(FileUtils.getExtension(file.getName()).toLowerCase().equals("cfg"))
            	return true;


            return false;
        }

        public String getDescription()
        {
            return "Tellervo Configuration File (*.cfg)";
        }
    }

}
