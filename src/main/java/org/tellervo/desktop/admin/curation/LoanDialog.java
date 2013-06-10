package org.tellervo.desktop.admin.curation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSplitPane;
import javax.swing.JSeparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.admin.BoxCuration.BoxCurationType;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSILoan;
import org.tridas.schema.TridasIdentifier;

public class LoanDialog extends JDialog implements ActionListener{


	private static final long serialVersionUID = 1L;

	private final static Logger log = LoggerFactory.getLogger(LoanDialog.class);

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private LoanPanel loanPanel;
	private LoanTableModel loanTableModel;

	/**
	 * Create the dialog.
	 */
	public LoanDialog() {
		initGUI();
		setLoan(getLoanFromWS("4b9bdb67-6c4e-445c-87df-f5a8fd5926b4"));
	}
	
	
	private void setLoan(WSILoan loan)
	{
		loanPanel.setLoan(loan);
		
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

	
	
	
	private void initGUI(){
		setBounds(100, 100, 498, 520);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		{
			JSplitPane splitPane = new JSplitPane();
			splitPane.setOneTouchExpandable(true);
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			contentPanel.add(splitPane, "cell 0 0,grow");
			{
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setMinimumSize(new Dimension(100,100));
				splitPane.setLeftComponent(scrollPane);
				{
					table = new JTable();
					table.setMinimumSize(new Dimension(100,100));
					loanTableModel = new LoanTableModel();
					table.setModel(loanTableModel);
					scrollPane.setViewportView(table);
				}
			}
			
			{
				loanPanel = new LoanPanel();
				splitPane.setRightComponent(loanPanel);
				loanPanel.setBorder(null);
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
			JButton btnNewButton = new JButton("Current");
			toolbar.add(btnNewButton);
		}
		{
			JButton btnNewButton_1 = new JButton("Delinquent");
			toolbar.add(btnNewButton_1);
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
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("All"))
		{
			
		}
		
	}


}
