package org.tellervo.desktop.curation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.curation.LoanPanel.LoanListener;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSILoan;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

public class LoanDialog extends JDialog implements ActionListener, LoanListener, WindowListener{


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
	private LoanDialogMode mode;
	
	
	public enum LoanDialogMode{		
			NEW,
			BROWSE_EDIT;		
	};
	
	
	public static void showNewLoanDialog(Component parent)
	{
		LoanDialog dialog = new LoanDialog(parent, LoanDialogMode.NEW);
		dialog.setVisible(true);
		dialog.expandDetailsPanel(0.0d);
	}
	
	/**
	 * Create the dialog in standard 'browse' mode
	 */
	public LoanDialog(Component parent) {
		this.parent = parent;
		mode = LoanDialogMode.BROWSE_EDIT;
		initGUI();
		loadCurrentLoans();
	}
	
	
	/**
	 * Set the display mode for this dialog
	 * 
	 * @param mode
	 */
	public void setLoanDialogMode(LoanDialogMode mode)
	{
		this.mode = mode;
		setGUIForMode();
	}
	
	
	/**
	 * Create dialog in the specified mode
	 * 
	 * @param parent
	 * @param mode
	 */
	public LoanDialog(Component parent, LoanDialogMode mode)
	{
		this.parent = parent;
		this.mode= mode;
		initGUI();
		loadCurrentLoans();
	}
	
	
	/**
	 * Set the loan to display in the loan panel.  
	 * 
	 * @param loan
	 */
	public void setLoan(WSILoan loan)
	{
		loanPanel.setLoan(loan);
		
		if(loan==null)
		{
			loanPanel.setEditable(false);	
		}
		else
		{
			expandDetailsPanel();
		}
	}

	
	private void setGUIForMode()
	{
		this.loanPanel.setGUIForMode(mode);
	}
	
	/**
	 * Set the loans list according to the SearchParameters passed.  A WS query
	 * is performed at the results displayed
	 * 
	 * @param param
	 */
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
		//tblLoans.clearSelection();
	}
	
	/**
	 * Load all the loans that have been returned into the loan list
	 */
    private void loadReturnedLoans()
    {
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);    	
    	param.addSearchConstraint(SearchParameterName.LOANRETURNDATE, SearchOperator.IS,  "NOT NULL");
    	loadLoansBySearchParam(param);
    }
    
    /**
     * Load all loans into the loan list
     */
    private void loadAllLoans()
    {
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);
    	param.addSearchForAll();
    	loadLoansBySearchParam(param);
    }
    
    /**
     * Load all delinquent loans into the loan list
     */
    private void loadDelinquentLoans()
    {
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = new Date();
    	param.addSearchConstraint(SearchParameterName.LOANDUEDATE, SearchOperator.LESS_THAN, dateFormat.format(date) );
    	param.addSearchConstraint(SearchParameterName.LOANRETURNDATE, SearchOperator.IS,  "NULL");
    	loadLoansBySearchParam(param);
    }
	
    /**
     * Populate the loan list with all currently active loans
     */
    private void loadCurrentLoans()
    {
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);
    	param.addSearchConstraint(SearchParameterName.LOANRETURNDATE, SearchOperator.IS,  "NULL");
    	loadLoansBySearchParam(param);
    }
	
    
    /**
     * Initialise the GUI 
     */
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
							updateLoanTable();
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
					tblLoans.setAutoCreateRowSorter(true);
					tblLoans.setMinimumSize(new Dimension(100,100));
					loanTableModel = new LoanTableModel();
					tblLoans.setModel(loanTableModel);
					
					tblLoans.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

						@Override
						public void valueChanged(ListSelectionEvent event) {
							
							
							if(loanPanel.isModified())
							{
								if(!confirmOKToChangeLoan()) return;
							}
							
							if(tblLoans.getSelectedRowCount()==0) 
							{
								setLoan(null);	
							}
							else
							{
								WSILoan loan = ((LoanTableModel)tblLoans.getModel()).getRow(tblLoans.getSelectedRow());
								setLoan(loan);
								setLoanDialogMode(LoanDialogMode.BROWSE_EDIT);
							}
							
							loanPanel.setEditable(false);
							
						}
						
					});
					
					
					scrollPane.setViewportView(tblLoans);
				}
				{
					JButton btnSearch = new JButton("Search");
					btnSearch.setEnabled(false);
					topPanel.add(btnSearch, "cell 1 0");
				}
			}
			
			{
				loanPanel = new LoanPanel();
				loanPanel.addListener(this);
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
		
		setGUIForMode();
		loanPanel.pack();
		
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
	}



	private void updateLoanTable()
	{

		if(!confirmOKToChangeLoan()) return;
				
		
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
	
	/**
	 * Check whether there are unsaved changes, if so confirm with the user
	 * whether they should be saved. 
	 * 
	 * Returns boolean to indicate whether user is happy to continue
	 * 
	 * @return
	 */
	public Boolean confirmOKToChangeLoan()
	{
		if(loanPanel.isModified())
		{
			Object[] options = {I18n.getText("general.save"),
					I18n.getText("general.discard"),
	                I18n.getText("general.cancel")};

			int n = JOptionPane.showOptionDialog(this, 
				"This loan has been modified.\nDo you want to save your changes?",
				"Save changes?",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
			
			if(n==JOptionPane.CANCEL_OPTION)
			{
				return false;
			}
			
			if(n==JOptionPane.NO_OPTION)
			{
				return true;
			}
			
			if(n==JOptionPane.YES_OPTION)
			{
				if (loanPanel.saveChanges())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			
			return false;

		}
		else
		{
			return true;
		}
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
			if(loanPanel.isModified())
			{
				if (loanPanel.saveChanges())
				{
					dispose();
				}
			}
		}
		else if (event.getActionCommand().equals("Cancel"))
		{
			
			dispose();
		}
	}
	
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
			splitPane.setDividerLocation(0.3d);
		}
	}

	@Override
	public void changesMadeToLoan() {
		log.debug("LoanPanel notified LoanDialog that the loan was changed");
		int ind = tblLoans.getSelectedRow();
		
		updateLoanTable();
		
		// Try and reselect the same row
		try{
			tblLoans.setRowSelectionInterval(ind, ind);
		} catch (IllegalArgumentException e )
		{
			tblLoans.clearSelection();
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0)  { }

	@Override
	public void windowClosed(WindowEvent arg0)  { }

	@Override
	public void windowClosing(WindowEvent event) {
		
		if(confirmOKToChangeLoan())
		{
			dispose();
		}		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)  { }

	@Override
	public void windowDeiconified(WindowEvent arg0)  { }

	@Override
	public void windowIconified(WindowEvent arg0)  { }

	@Override
	public void windowOpened(WindowEvent arg0) { }
	


}
