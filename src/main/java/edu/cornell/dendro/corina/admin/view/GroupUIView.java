/*******************************************************************************
 * Copyright (C) 2011 Dan Girshovich and Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Dan Girshovich
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.admin.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.TableRowSorter;

import edu.cornell.dendro.corina.admin.model.SecurityGroupTableModelA;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityGroupEntityResource;




/**
 * GUI Class for viewing/editing a groups details.
 * 
 * @author  Dan Girshovich
 */
public class GroupUIView extends javax.swing.JDialog implements ActionListener, MouseListener{
    
	private static final long serialVersionUID = 1L;
	WSISecurityGroup group = new WSISecurityGroup();
	Boolean isNewGroup = true;
	private SecurityGroupTableModelA groupsModel;
	private TableRowSorter<SecurityGroupTableModelA> groupsSorter;
	
    /** Creates new form GroupUI 
     * @wbp.parser.constructor*/
    public GroupUIView(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    	isNewGroup = true;
        setupGUI();

    }
    
    public GroupUIView(JDialog parent, boolean modal, WSISecurityGroup group) {
        super(parent, modal);
        this.group = group;
        initComponents();
    	isNewGroup = false;
    	setupGUI();

    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    @SuppressWarnings("serial")
	private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblGroup = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        chkEnabled = new javax.swing.JCheckBox();
        scrollPane = new javax.swing.JScrollPane();
        tblGroups = new javax.swing.JTable();
        btnDoIt = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblId.setText("ID:");

        txtId.setEditable(false);

        lblGroup.setText("Group name:");

        chkEnabled.setText("Enabled");

        tblGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Group", "Description", "Member"
            }
        ) {
			@SuppressWarnings("rawtypes")
			Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };


			public Class<?> getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane.setViewportView(tblGroups);

        btnDoIt.setText("Apply");

        btnClose.setText("OK");

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(btnDoIt)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(btnClose))
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(lblId)
        							.addGap(64)
        							.addComponent(txtId, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)))
        					.addContainerGap())
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(lblGroup)
        					.addGap(18)
        					.addComponent(txtName, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))))
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(chkEnabled)
        			.addContainerGap(336, Short.MAX_VALUE))
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblId)
        				.addComponent(txtId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(chkEnabled)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblGroup))
        			.addGap(18)
        			.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnClose)
        				.addComponent(btnDoIt))
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnClose;
    protected javax.swing.JButton btnDoIt;
    protected javax.swing.JCheckBox chkEnabled;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JLabel lblId;
    protected javax.swing.JLabel lblGroup;
    protected javax.swing.JScrollPane scrollPane;
    protected javax.swing.JTable tblGroups;
    protected javax.swing.JTextField txtId;
    protected javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
 
    @SuppressWarnings("unchecked")
	private void setupGUI()
    {
    	this.setLocationRelativeTo(getRootPane());
    	this.lblId.setVisible(false);
    	this.txtId.setVisible(false);
    	    	
    	this.tblGroups.addMouseListener(this);

    	if(isNewGroup)
    	{
        	this.setTitle("Create group");
        	btnDoIt.setText("Create");
        	btnClose.setText("Close");
        	chkEnabled.setSelected(true);
    	}
    	else
    	{
        	this.setTitle("Edit group");
        	btnDoIt.setText("Apply");
        	btnClose.setText("Close");
	    	if(group.isSetName()) txtName.setText(group.getName());
	    	if(group.isSetId()) 		  txtId.setText(group.getId());
	    	if(group.isSetName())  txtName.setText(group.getName());
	    	if(group.isSetIsActive())  chkEnabled.setSelected(group.isIsActive());
    	}
    	
        // Populate groups list
        ArrayList<WSISecurityGroup> lstofGroups = (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary");  
        groupsModel = new SecurityGroupTableModelA(lstofGroups);
        tblGroups.setModel(groupsModel);
        groupsSorter = new TableRowSorter<SecurityGroupTableModelA>(groupsModel);
        tblGroups.setRowSorter(groupsSorter);
        tblGroups.setEditingColumn(3);
    	
    	this.btnDoIt.addActionListener(this);
    	this.btnClose.addActionListener(this);
    	
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.btnClose) 
		{
			this.dispose();
		}	
		else if (e.getSource()==this.btnDoIt)
		{
			saveChangesToGroup();
		}
	}
	
	private void saveChangesToGroup()
	{
		group.setName(txtName.getText());
		group.setIsActive(this.chkEnabled.isSelected());
		
		
		if(isNewGroup)
		{
			// associate a resource
	    	SecurityGroupEntityResource rsrc = new SecurityGroupEntityResource(CorinaRequestType.CREATE, group);
	    	
			CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
			{
				rsrc.getAssociatedResult();
				dispose();
			}
			
			JOptionPane.showMessageDialog(this, "Error creating group.  Make sure the groupname is unique." + accdialog.getFailException().
					getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}
		else 
		{
			// Editing existing group
			
			// associate a resource
	    	SecurityGroupEntityResource rsrc = new SecurityGroupEntityResource(CorinaRequestType.UPDATE, group);
	    	
			CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
			{
				rsrc.getAssociatedResult();
			}
			
			JOptionPane.showMessageDialog(this, "Error updating group: " + accdialog.getFailException().
					getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()>1)
		{
			// Double clicked on groups table - change groups membership accordingly
			Boolean isMember = (Boolean) this.groupsModel.getValueAt(this.tblGroups.getSelectedRow(), 3);
			
//			if(isMember)
//			{
//				groupsModel.setMembershipAt(this.tblGroups.getSelectedRow(), false);
//			}
//			else
//			{
//				groupsModel.setMembershipAt(this.tblGroups.getSelectedRow(), true);
//
//			}
			this.tblGroups.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
    
}
