package org.tellervo.desktop.admin.curation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSILoan;
import org.tridas.schema.TridasIdentifier;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.FlowLayout;

public class LoanDialog extends JDialog implements ActionListener{


	private static final long serialVersionUID = 1L;

	private final static Logger log = LoggerFactory.getLogger(LoanDialog.class);

	private final JPanel contentPanel = new JPanel();
	private JTable tblLoans;
	private LoanPanel loanPanel;
	private LoanTableModel loanTableModel;
	private Component parent;
	private JSplitPane splitPane;
	private JComboBox cboLoanFilter;
	private JPanel buttonPanel;
	private JButton btnOk;
	private JButton btnCancel;
	
	/**
	 * Create the dialog.
	 */
	public LoanDialog(Component parent) {
		this.parent = parent;
		initGUI();
		loadCurrentLoans();
	}
	
	
	public void setLoan(WSILoan loan)
	{
		loanPanel.setLoan(loan);
		expandDetailsPanel();

	}
	
	private void loadLoansBySearchParam(SearchParameters param)
	{
		if(!param.getReturnObject().equals(SearchReturnObject.LOAN))
		{
			log.error("Return object type passed to loadLoansBySearchParam() must be a Loan");
			return;
		}
		
		// we want a loan returned here
		EntitySearchResource<WSILoan> resource = new EntitySearchResource<WSILoan>(param, WSILoan.class);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting loans");
			return;
		}
		
		List<WSILoan> loanList = resource.getAssociatedResult();
		
		
		loanTableModel.setLoans(loanList); 
		loanTableModel.fireTableDataChanged();
		tblLoans.clearSelection();
	}
	
    private void loadReturnedLoans()
    {
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);    	
    	param.addSearchConstraint(SearchParameterName.LOANRETURNDATE, SearchOperator.IS,  "NOT NULL");
    	loadLoansBySearchParam(param);
    }
    
    private void loadAllLoans()
    {
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);
    	param.addSearchForAll();
    	loadLoansBySearchParam(param);
    }
    
    private void loadDelinquentLoans()
    {
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = new Date();
    	param.addSearchConstraint(SearchParameterName.LOANDUEDATE, SearchOperator.LESS_THAN, dateFormat.format(date) );
    	param.addSearchConstraint(SearchParameterName.LOANRETURNDATE, SearchOperator.IS,  "NULL");
    	loadLoansBySearchParam(param);
    }
	
    private void loadCurrentLoans()
    {
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);
    	param.addSearchConstraint(SearchParameterName.LOANRETURNDATE, SearchOperator.IS,  "NULL");
    	loadLoansBySearchParam(param);
    }
	
	private void initGUI(){
		setBounds(100, 100, 688, 614);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			splitPane = new JSplitPane();
			splitPane.setOneTouchExpandable(true);
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			
			contentPanel.add(splitPane);
			{
				JPanel topPanel = new JPanel();
				topPanel.setBorder(new TitledBorder(null, "Loan list", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				splitPane.setLeftComponent(topPanel);
				topPanel.setLayout(new MigLayout("", "[64.00px][grow]", "[][100px,grow,fill]"));
				{
					JLabel lblFilterLoans = new JLabel("Filter loan list:");
					topPanel.add(lblFilterLoans, "cell 0 0,alignx trailing");
				}
				{
					cboLoanFilter = new JComboBox();
					cboLoanFilter.setModel(new DefaultComboBoxModel(new String[] {"Current loans", "Delinquent loans", "Returned loans", "All loans"}));
					
					cboLoanFilter.addItemListener(new ItemListener(){

						@Override
						public void itemStateChanged(ItemEvent arg0) {
							if(cboLoanFilter.getSelectedItem().equals("Current loans"))
							{
								loadCurrentLoans();
							}
							else if(cboLoanFilter.getSelectedItem().equals("All loans"))
							{
								loadAllLoans();
							}
							else if(cboLoanFilter.getSelectedItem().equals("Returned loans"))
							{
								loadReturnedLoans();
							}
							else if(cboLoanFilter.getSelectedItem().equals("Delinquent loans"))
							{
								loadDelinquentLoans();
							}
							
						}
						
						
					});
					
					cboLoanFilter.setSelectedItem("Current loans");
					
					topPanel.add(cboLoanFilter, "flowx,cell 1 0,growx");
				}
				
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setMinimumSize(new Dimension(100,100));
				topPanel.add(scrollPane, "cell 0 1 2 1,grow");
				{
					tblLoans = new JTable();
					tblLoans.setMinimumSize(new Dimension(100,100));
					loanTableModel = new LoanTableModel();
					tblLoans.setModel(loanTableModel);
					
					tblLoans.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

						@Override
						public void valueChanged(ListSelectionEvent event) {
							
							if(tblLoans.getSelectedRowCount()==0) 
							{
								setLoan(null);
							}
							else
							{
								WSILoan loan = ((LoanTableModel)tblLoans.getModel()).getRow(tblLoans.getSelectedRow());
								setLoan(loan);
							}
							
						}
						
					});
					
					
					scrollPane.setViewportView(tblLoans);
				}
				{
					JButton btnNewButton_3 = new JButton("Search");
					topPanel.add(btnNewButton_3, "cell 1 0");
				}
			}
			
			{
				loanPanel = new LoanPanel();
				splitPane.setRightComponent(loanPanel);
				loanPanel.setBorder(new TitledBorder(null, "Loan Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				loanPanel.setEditable(false);
			}
		}
		
		
		
		setIconImage(Builder.getApplicationIcon());
		setTitle("Loans");
		setLocationRelativeTo(parent);
		this.setVisible(true);
		splitPane.getRightComponent().setMinimumSize(new Dimension());
		splitPane.setDividerLocation(1.0d);
		{
			buttonPanel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			contentPanel.add(buttonPanel, BorderLayout.SOUTH);
			{
				btnOk = new JButton("OK");
				btnOk.setActionCommand("OK");
				btnOk.addActionListener(this);
				buttonPanel.add(btnOk);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				btnCancel.addActionListener(this);
				buttonPanel.add(btnCancel);
			}
		}
		
		loanPanel.pack();
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("All"))
		{
			loadAllLoans();
		}
		else if(event.getActionCommand().equals("Current"))
		{
			loadCurrentLoans();
		}
		else if(event.getActionCommand().equals("Delinquent"))
		{
			loadDelinquentLoans();
		}
		else if (event.getActionCommand().equals("OK"))
		{
			if(loanPanel.hasUnsavedEdits())
			{
				loanPanel.saveChanges();
			}
			
			dispose();
		}
		else if (event.getActionCommand().equals("Cancel"))
		{
			if(loanPanel.hasUnsavedEdits())
			{
				Object[] options = {I18n.getText("general.save"),
						I18n.getText("general.discard"),
		                I18n.getText("general.cancel")};

				int n = JOptionPane.showOptionDialog(parent, 
					"This loan has been modified.\nDo you want to save your changes?",
					"Save changes?",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
				
				if(n==JOptionPane.CANCEL_OPTION)
				{
					return;
				}
				
				if(n==JOptionPane.NO_OPTION)
				{
					dispose();
				}
				
				if(n==JOptionPane.YES_OPTION)
				{
					loanPanel.saveChanges();
				}

			}
			else
			{
				dispose();
			}
		}
	}
	
	
	/**
	 * If the details panel is hidden or very small then expand
	 */
	public void expandDetailsPanel(Double overridepos)
	{
		splitPane.setDividerLocation(overridepos);
	}
	
	/**
	 * If the details panel is hidden or very small then expand
	 */
	public void expandDetailsPanel()
	{
		if(splitPane.getDividerLocation()>splitPane.getHeight()-100)
		{
			splitPane.setDividerLocation(0.4d);
		}
	}
	


}
