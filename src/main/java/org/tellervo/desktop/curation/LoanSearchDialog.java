package org.tellervo.desktop.curation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import javax.swing.DefaultComboBoxModel;
import org.tellervo.desktop.curation.LoanSearchDialog.TextSearchOperator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSILoan;

public class LoanSearchDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(LoanSearchDialog.class);

	private List<WSILoan> loanList;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtLoanID;
	private JCheckBox chkLoanID;
	private JComboBox cbxLoanID;
	
	private JTextField txtFirstName;
	private JCheckBox chkFirstName;
	private JComboBox cbxFirstName;
	
	private JTextField txtLastName;
	private JCheckBox chkLastName;
	private JComboBox cbxLastName;
	
	private JTextField txtOrganisation;
	private JCheckBox chkOrganisation;
	private JComboBox cbxOrganisation;

	private JTextField txtIssueDate;
	private JCheckBox chkIssueDate;
	private JComboBox cbxIssueDate;
	
	private JTextField txtDueDate;
	private JCheckBox chkDueDate;
	private JComboBox cbxDueDate;

	private JTextField txtReturnDate;
	private JCheckBox chkReturnDate;
	private JComboBox cbxReturnDate;

	
	
	public enum TextSearchOperator{
		
		EQUALS("="),
		NOT_EQUALS("!="),
		LIKE("LIKE");

		private String humanreadable;
		
		private TextSearchOperator(String humanreadable)
		{
			this.humanreadable = humanreadable;
		}
		
		public String getHumanReadable()
		{
			return humanreadable;
		}
		
		public SearchOperator getSearchOperator()
		{
			if(this.equals(TextSearchOperator.EQUALS))
			{
				return SearchOperator.EQUALS;
			}
			else if(this.equals(TextSearchOperator.NOT_EQUALS))
			{
				return SearchOperator.NOT_EQUALS;
			}
			else if(this.equals(TextSearchOperator.LIKE))
			{
				return SearchOperator.LIKE;
			}
			
			return null;
		}
		
	}
	
	public enum DateSearchOperator{
		
		LESS("<"),
		MORE(">");

		private String humanreadable;
		
		private DateSearchOperator(String humanreadable)
		{
			this.humanreadable = humanreadable;
		}
		
		public String getHumanReadable()
		{
			return humanreadable;
		}
		
		public SearchOperator getSearchOperator()
		{
			if(this.equals(DateSearchOperator.LESS))
			{
				return SearchOperator.LESS_THAN;
			}
			else if(this.equals(DateSearchOperator.MORE))
			{
				return SearchOperator.GREATER_THAN;
			}
		
			
			return null;
		}
		
	}
	
	
	/**
	 * Create the dialog.
	 */
	public LoanSearchDialog() {

		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow][grow]", "[][][][][][][][grow]"));
		{
			chkLoanID = new JCheckBox("");
			chkLoanID.setActionCommand("EnableLoanID");
			chkLoanID.addActionListener(this);
			contentPanel.add(chkLoanID, "cell 0 0");
		}
		{
			JLabel lblLoanId = new JLabel("Loan ID:");
			contentPanel.add(lblLoanId, "cell 1 0,alignx trailing");
		}
		{
			cbxLoanID = new JComboBox();
			cbxLoanID.setModel(new DefaultComboBoxModel(TextSearchOperator.values()));
			contentPanel.add(cbxLoanID, "cell 2 0,growx");
		}
		{
			txtLoanID = new JTextField();
			contentPanel.add(txtLoanID, "cell 3 0,growx");
			txtLoanID.setColumns(10);
		}
		
		{
			chkFirstName = new JCheckBox("");
			chkFirstName.setActionCommand("EnableFirstName");
			chkFirstName.addActionListener(this);
			contentPanel.add(chkFirstName, "cell 0 1");
		}
		{
			JLabel lblFirstName = new JLabel("First name:");
			contentPanel.add(lblFirstName, "cell 1 1,alignx trailing");
		}
		{
			cbxFirstName = new JComboBox();
			cbxFirstName.setModel(new DefaultComboBoxModel(TextSearchOperator.values()));
			contentPanel.add(cbxFirstName, "cell 2 1,growx");
		}
		{
			txtFirstName = new JTextField();
			txtFirstName.setColumns(10);
			contentPanel.add(txtFirstName, "cell 3 1,growx");
		}
		
		{
			chkLastName = new JCheckBox("");
			chkLastName.setSelected(true);
			chkLastName.setActionCommand("EnableLastName");
			chkLastName.addActionListener(this);
			contentPanel.add(chkLastName, "cell 0 2");
		}
		{
			JLabel lblLastName = new JLabel("Last name:");
			contentPanel.add(lblLastName, "cell 1 2,alignx trailing");
		}
		{
			cbxLastName = new JComboBox();
			cbxLastName.setModel(new DefaultComboBoxModel(TextSearchOperator.values()));
			contentPanel.add(cbxLastName, "cell 2 2,growx");
		}
		{
			txtLastName = new JTextField();
			txtLastName.setColumns(10);
			contentPanel.add(txtLastName, "cell 3 2,growx");
		}
		
		{
			chkOrganisation = new JCheckBox("");
			chkOrganisation.setActionCommand("EnableOrganisation");
			chkOrganisation.addActionListener(this);
			contentPanel.add(chkOrganisation, "cell 0 3");
		}
		{
			JLabel lblOrganisation = new JLabel("Organisation:");
			contentPanel.add(lblOrganisation, "cell 1 3,alignx trailing");
		}
		{
			cbxOrganisation = new JComboBox();
			cbxOrganisation.setModel(new DefaultComboBoxModel(TextSearchOperator.values()));
			contentPanel.add(cbxOrganisation, "cell 2 3,growx");
		}
		{
			txtOrganisation = new JTextField();
			txtOrganisation.setColumns(10);
			contentPanel.add(txtOrganisation, "cell 3 3,growx");
		}
		
		
		{
			chkIssueDate = new JCheckBox("");
			chkIssueDate.setActionCommand("EnableIssueDate");
			chkIssueDate.addActionListener(this);
			contentPanel.add(chkIssueDate, "cell 0 4");
		}
		{
			JLabel lblIssueDate = new JLabel("Issue date:");
			contentPanel.add(lblIssueDate, "cell 1 4,alignx trailing");
		}
		{
			cbxIssueDate = new JComboBox();
			cbxIssueDate.setModel(new DefaultComboBoxModel(DateSearchOperator.values()));
			contentPanel.add(cbxIssueDate, "cell 2 4,growx");
		}
		{
			txtIssueDate = new JTextField();
			txtIssueDate.setColumns(10);
			contentPanel.add(txtIssueDate, "cell 3 4,growx");
		}
		
		{
			chkDueDate = new JCheckBox("");
			chkDueDate.setActionCommand("EnableDueDate");
			chkDueDate.addActionListener(this);
			contentPanel.add(chkDueDate, "cell 0 5");
		}
		{
			JLabel lblDueDate = new JLabel("Due date:");
			contentPanel.add(lblDueDate, "cell 1 5,alignx trailing");
		}
		{
			cbxDueDate = new JComboBox();
			cbxDueDate.setModel(new DefaultComboBoxModel(DateSearchOperator.values()));
			contentPanel.add(cbxDueDate, "cell 2 5,growx");
		}
		{
			txtDueDate = new JTextField();
			txtDueDate.setColumns(10);
			contentPanel.add(txtDueDate, "cell 3 5,growx");
		}
		
		
		{
			chkReturnDate = new JCheckBox("");
			chkReturnDate.setActionCommand("EnableReturnDate");
			chkReturnDate.addActionListener(this);
			contentPanel.add(chkReturnDate, "cell 0 6");
		}
		{
			JLabel lblReturnDate = new JLabel("Return date:");
			contentPanel.add(lblReturnDate, "cell 1 6,alignx trailing");
		}
		{
			cbxReturnDate = new JComboBox();
			cbxReturnDate.setModel(new DefaultComboBoxModel(DateSearchOperator.values()));
			contentPanel.add(cbxReturnDate, "cell 2 6,growx");
		}
		{
			txtReturnDate = new JTextField();
			txtReturnDate.setColumns(10);
			contentPanel.add(txtReturnDate, "cell 3 6,growx");
		}
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnSearch = new JButton("Search");
				btnSearch.setActionCommand("Search");
				btnSearch.addActionListener(this);
				buttonPane.add(btnSearch);
				getRootPane().setDefaultButton(btnSearch);
			}
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				btnCancel.addActionListener(this);
				buttonPane.add(btnCancel);
			}
		}
		
		
		this.setIconImage(Builder.getApplicationIcon());
		this.setTitle("Loan search");
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		enableDisableGUI();
	}


	/**
	 * Show hide GUI components depending on user choice
	 */
	private void enableDisableGUI()
	{
		cbxLoanID.setEnabled(chkLoanID.isSelected());
		txtLoanID.setEnabled(chkLoanID.isSelected());
		
		cbxFirstName.setEnabled(chkFirstName.isSelected());
		txtFirstName.setEnabled(chkFirstName.isSelected());
		
		cbxLastName.setEnabled(chkLastName.isSelected());
		txtLastName.setEnabled(chkLastName.isSelected());
		
		cbxOrganisation.setEnabled(chkOrganisation.isSelected());
		txtOrganisation.setEnabled(chkOrganisation.isSelected());
		
		cbxDueDate.setEnabled(chkDueDate.isSelected());
		txtDueDate.setEnabled(chkDueDate.isSelected());
		
		cbxIssueDate.setEnabled(chkIssueDate.isSelected());
		txtIssueDate.setEnabled(chkIssueDate.isSelected());
		
		cbxReturnDate.setEnabled(chkReturnDate.isSelected());
		txtReturnDate.setEnabled(chkReturnDate.isSelected());
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if((e.getActionCommand().equals("EnableLoanID"))        || 
		   (e.getActionCommand().equals("EnableFirstName"))     ||
		   (e.getActionCommand().equals("EnableLastName"))      ||
		   (e.getActionCommand().equals("EnableOrganisation"))  ||
		   (e.getActionCommand().equals("EnableIssueDate"))     ||
		   (e.getActionCommand().equals("EnableDueDate"))       ||
		   (e.getActionCommand().equals("EnableReturnDate"))    
		  )
		{
			enableDisableGUI();
		}

		else if(e.getActionCommand().equals("Search"))
		{
			if(performSearch())
			{
				setVisible(false);
			}
		}
		else if(e.getActionCommand().equals("Cancel"))
		{
			setVisible(false);
		}
	}
	
	/**
	 * Get the result of the search
	 * 
	 * @return
	 */
	public List<WSILoan> getSearchResult()
	{
		return loanList;
	}
	
	/**
	 * Actually run the requested search
	 * @return
	 */
	private boolean performSearch()
	{
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);    	
    	
    	if(chkLoanID.isSelected())
    	{
    		param.addSearchConstraint(SearchParameterName.LOANID, ((TextSearchOperator)cbxLoanID.getSelectedItem()).getSearchOperator(),  txtLoanID.getText());
    	}
    	if(chkFirstName.isSelected())
    	{
    		param.addSearchConstraint(SearchParameterName.LOANFIRSTNAME, ((TextSearchOperator)cbxFirstName.getSelectedItem()).getSearchOperator(),  txtFirstName.getText());
    	}
    	if(chkLastName.isSelected())
    	{
    		param.addSearchConstraint(SearchParameterName.LOANLASTNAME, ((TextSearchOperator)cbxLastName.getSelectedItem()).getSearchOperator(),  txtLastName.getText());
    	}
    	if(chkOrganisation.isSelected())
    	{
    		param.addSearchConstraint(SearchParameterName.LOANORGANISATION, ((TextSearchOperator)cbxOrganisation.getSelectedItem()).getSearchOperator(),  txtOrganisation.getText());
    	}

    	if(chkIssueDate.isSelected())
    	{
    		param.addSearchConstraint(SearchParameterName.LOANISSUEDATE, ((DateSearchOperator)cbxIssueDate.getSelectedItem()).getSearchOperator(),  txtIssueDate.getText());
    	}
    	if(chkDueDate.isSelected())
    	{
    		param.addSearchConstraint(SearchParameterName.LOANDUEDATE, ((DateSearchOperator)cbxDueDate.getSelectedItem()).getSearchOperator(),  txtDueDate.getText());
    	}
    	if(chkReturnDate.isSelected())
    	{
    		param.addSearchConstraint(SearchParameterName.LOANRETURNDATE, ((DateSearchOperator)cbxReturnDate.getSelectedItem()).getSearchOperator(),  txtReturnDate.getText());
    	}
    	
		// we want a loan returned here
		EntitySearchResource<WSILoan> resource = new EntitySearchResource<WSILoan>(param, WSILoan.class);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting loans");
			return false;
		}
		
		loanList = resource.getAssociatedResult();
		
		if(loanList.size()>0) return true;
		
		Alert.message("No records", "No records match your search requset");
		return false;
		
	}
}
