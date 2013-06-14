package org.tellervo.desktop.admin.curation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
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

public class LoanDialog extends JDialog implements ActionListener{


	private static final long serialVersionUID = 1L;

	private final static Logger log = LoggerFactory.getLogger(LoanDialog.class);

	private final JPanel contentPanel = new JPanel();
	private JTable tblLoans;
	private LoanPanel loanPanel;
	private LoanTableModel loanTableModel;
	private Component parent;
	private JSplitPane splitPane;
	
	
	/**
	 * Create the dialog.
	 */
	public LoanDialog(Component parent) {
		this.parent = parent;
		initGUI();
	}
	
	
	private void setLoan(WSILoan loan)
	{
		loanPanel.setLoan(loan);
		expandDetailsPanel();

	}
	
    private WSILoan getLoanFromWS(String idstr)
    {
    	
    	log.debug("Getting details of loan (id"+idstr+") from the webservice");

		TridasIdentifier id = new TridasIdentifier();
		id.setValue(idstr);
		
    	// we want a box returned here
		EntityResource<WSILoan> resource = new EntityResource<WSILoan>(id, EntityType.LOAN, TellervoRequestType.READ, WSILoan.class);

		// Query db 
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting loan information");
		}
		
		WSILoan loan = resource.getAssociatedResult();
		
		return loan;
    }

    private void loadAllLoans()
    {
		// Set return type to loan
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);
    	param.addSearchForAll();

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
		
		// Check to see if any samples were found
		if (loanList.size()==0) 
		{
			Alert.error("None found", "No loans were found");
			return;
		}
		
	/*	TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		*/
		
		loanTableModel.setLoans(loanList); 
		loanTableModel.fireTableDataChanged();
    }
    
    private void loadDelinquentLoans()
    {
		// Set return type to loan
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);
    	
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = new Date();
    	
    	param.addSearchConstraint(SearchParameterName.LOANDUEDATE, SearchOperator.LESS_THAN, dateFormat.format(date) );
    	param.addSearchConstraint(SearchParameterName.LOANRETURNDATE, SearchOperator.IS,  "NULL");

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
	
    }
	
    private void loadCurrentLoans()
    {
		// Set return type to loan
    	SearchParameters param = new SearchParameters(SearchReturnObject.LOAN);
    	
    	param.addSearchConstraint(SearchParameterName.LOANRETURNDATE, SearchOperator.IS,  "NULL");

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
	
    }
	
	private void initGUI(){
		setBounds(100, 100, 688, 520);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		{
			splitPane = new JSplitPane();
			splitPane.setOneTouchExpandable(true);
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			
			contentPanel.add(splitPane, "cell 0 0,grow");
			{
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setMinimumSize(new Dimension(100,100));
				splitPane.setLeftComponent(scrollPane);
				{
					tblLoans = new JTable();
					tblLoans.setMinimumSize(new Dimension(100,100));
					loanTableModel = new LoanTableModel();
					tblLoans.setModel(loanTableModel);
					
					tblLoans.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

						@Override
						public void valueChanged(ListSelectionEvent event) {
							WSILoan loan = ((LoanTableModel)tblLoans.getModel()).getRow(tblLoans.getSelectedRow());
							setLoan(loan);
							
						}
						
					});
					
					
					scrollPane.setViewportView(tblLoans);
				}
			}
			
			{
				loanPanel = new LoanPanel();
				splitPane.setRightComponent(loanPanel);
				loanPanel.setBorder(new TitledBorder(null, "Loan Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				loanPanel.setEditable(false);
			}
		}
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		{
			JButton btnNewLoan = new JButton("New");
			toolbar.add(btnNewLoan);
		}
		{
			JButton btnReturn = new JButton("Return");
			toolbar.add(btnReturn);
		}
		{
			JSeparator separator = new JSeparator();
			toolbar.add(separator);
		}
		{
			JButton btnCurrent = new JButton("Current");
			btnCurrent.setActionCommand("Current");
			btnCurrent.addActionListener(this);
			toolbar.add(btnCurrent);
		}
		{
			JButton btnDelinquent = new JButton("Delinquent");
			btnDelinquent.setActionCommand("Delinquent");
			btnDelinquent.addActionListener(this);
			toolbar.add(btnDelinquent);
		}
		{
			JButton btnAll = new JButton("All");
			btnAll.setActionCommand("All");
			btnAll.addActionListener(this);
			toolbar.add(btnAll);
		}
		{
			JButton btnNewButton_3 = new JButton("Search");
			toolbar.add(btnNewButton_3);
		}
		
		
		
		setIconImage(Builder.getApplicationIcon());
		setTitle("Loans");
		setLocationRelativeTo(parent);
		this.setVisible(true);
		splitPane.getRightComponent().setMinimumSize(new Dimension());
		splitPane.setDividerLocation(1.0d);
		
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
	}
	
	
	/**
	 * If the details panel is hidden or very small then expand
	 */
	private void expandDetailsPanel()
	{
		if(splitPane.getDividerLocation()>splitPane.getHeight()-100)
		{
			splitPane.setDividerLocation(0.2d);
		}
	}


}
