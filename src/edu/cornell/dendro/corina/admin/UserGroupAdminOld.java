package edu.cornell.dendro.corina.admin;

/**
 *
 * @author  peterbrewer
 */

import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.User;


public class UserGroupAdminOld extends javax.swing.JDialog {
    
	private static final long serialVersionUID = -1241531356865887567L;
	private UserTableModel utm;
	
	
    /** Creates new form Admin */
    public UserGroupAdminOld(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
	private void initComponents() {

        accountsTabPane = new javax.swing.JTabbedPane();
        userPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        btnEditUser = new javax.swing.JButton();
        btnNewUser = new javax.swing.JButton();
        btnDeleteUser = new javax.swing.JButton();
        chkShowDisabledUsers = new javax.swing.JCheckBox();
        groupPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGroups = new javax.swing.JTable();
        chkShowDisabledGroups = new javax.swing.JCheckBox();
        btnEditGroup = new javax.swing.JButton();
        btnNewGroup = new javax.swing.JButton();
        btnDeleteGroup = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblUsers1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Account Management");

        /*tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "peter", "Peter", "Brewer", "Admin, Staff", new Boolean(true)},
                {"2", "lucas", "Lucas", "Madar", "Admin, Staff", new Boolean(true)}
            },
            new String [] {
                "ID", "User", "First name", "Last name", "Groups", "Enabled"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        */
        
        List<User> lstofUsers = (List<User>) App.dictionary.getDictionary("securityUserDictionary");
        
        utm = new UserTableModel(lstofUsers);
        
        tblUsers.setModel(utm);
        
                
        tblUsers.setShowVerticalLines(false);
        
        jScrollPane1.setViewportView(tblUsers);
        tblUsers.getColumnModel().getColumn(4).setHeaderValue("Groups");
        tblUsers.getColumnModel().getColumn(5).setResizable(false);

        btnEditUser.setText("Edit");

        btnNewUser.setText("New");

        btnDeleteUser.setText("Delete");

        chkShowDisabledUsers.setText("Show disabled accounts");
        chkShowDisabledUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowDisabledUsersActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout userPanelLayout = new org.jdesktop.layout.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(userPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .add(userPanelLayout.createSequentialGroup()
                        .add(chkShowDisabledUsers)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 190, Short.MAX_VALUE)
                        .add(btnEditUser)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(btnNewUser)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(btnDeleteUser)))
                .addContainerGap())
        );
        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(userPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnDeleteUser)
                    .add(chkShowDisabledUsers)
                    .add(btnNewUser)
                    .add(btnEditUser))
                .addContainerGap())
        );

        accountsTabPane.addTab("Users", userPanel);

        tblGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Admin", new Boolean(true)},
                {"2", "Staff", new Boolean(true)},
                {"3", "Students", new Boolean(true)},
                {"4", "Guests", new Boolean(true)}
            },
            new String [] {
                "ID", "Group", "Enabled"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblGroups);

        chkShowDisabledGroups.setText("Show disabled accounts");
        chkShowDisabledGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowDisabledGroupsActionPerformed(evt);
            }
        });

        btnEditGroup.setText("Edit");

        btnNewGroup.setText("New");

        btnDeleteGroup.setText("Delete");

        tblUsers1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "peter", "Peter", "Brewer", "Admin, Staff", new Boolean(true)},
                {"2", "lucas", "Lucas", "Madar", "Admin, Staff", new Boolean(true)}
            },
            new String [] {
                "ID", "User", "First name", "Last name", "Groups", "Enabled"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblUsers1);
        tblUsers1.getColumnModel().getColumn(4).setHeaderValue("Groups");
        tblUsers1.getColumnModel().getColumn(5).setResizable(false);

        jLabel1.setText("Group members:");

        org.jdesktop.layout.GroupLayout groupPanelLayout = new org.jdesktop.layout.GroupLayout(groupPanel);
        groupPanel.setLayout(groupPanelLayout);
        groupPanelLayout.setHorizontalGroup(
            groupPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(groupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(groupPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, groupPanelLayout.createSequentialGroup()
                        .add(chkShowDisabledGroups)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 190, Short.MAX_VALUE)
                        .add(btnEditGroup)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(btnNewGroup)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(btnDeleteGroup))
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .add(jLabel1))
                .addContainerGap())
        );
        groupPanelLayout.setVerticalGroup(
            groupPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, groupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(groupPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkShowDisabledGroups)
                    .add(btnDeleteGroup)
                    .add(btnNewGroup)
                    .add(btnEditGroup))
                .addContainerGap())
        );

        accountsTabPane.addTab("Groups", groupPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(accountsTabPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 653, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(accountsTabPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 591, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
        
    }
        
   
    private void chkShowDisabledUsersActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        // TODO add your handling code here:
}
    private void chkShowDisabledGroupsActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        // TODO add your handling code here:
    }
    
    public class UserTableModel extends AbstractTableModel {
        	
    	private List<User> userList;
    	    	
        private final String[] columnNames = {
                "#",
                "User",
                "First name",
                "Last name",
                "Groups",
                "Enabled",
            };
    	
    	public UserTableModel(List<User> usrLst){
    		userList = usrLst;
    	}
        
        public void setUsers(List<User> usrList){
        	userList = usrList;        	
        }
        
		public int getColumnCount() {
			return columnNames.length;
		};
		
		public int getRowCount() {
			return userList.size();
		};		
		
        public Class<?> getColumnClass(int c) {
        	if (c==5){
        		return Boolean.class;
        	} else {
        		return String.class;
        	}
        }
        
		public String getColumnName(int index) {
			return columnNames[index];
		}
		
		public User getUserAt(int rowIndex) {
			return userList.get(rowIndex);						
		}		
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			User usr = getUserAt(rowIndex);

			switch (columnIndex) {
				case 0: return usr.getId();
				case 1: return usr.getUsername();
				case 2: return usr.getFirstname();
				case 3: return usr.getLastname();
				case 4: return "n/a";
				case 5: return usr.isEnabled();
				default: return null;
			}
		}
    
    
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UserGroupAdminOld dialog = new UserGroupAdminOld(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {

                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration
    private javax.swing.JTabbedPane accountsTabPane;
    private javax.swing.JButton btnDeleteGroup;
    private javax.swing.JButton btnDeleteUser;
    private javax.swing.JButton btnEditGroup;
    private javax.swing.JButton btnEditUser;
    private javax.swing.JButton btnNewGroup;
    private javax.swing.JButton btnNewUser;
    private javax.swing.JCheckBox chkShowDisabledGroups;
    private javax.swing.JCheckBox chkShowDisabledUsers;
    private javax.swing.JPanel groupPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblGroups;
    private javax.swing.JTable tblUsers;
    private javax.swing.JTable tblUsers1;
    private javax.swing.JPanel userPanel;
    
}
