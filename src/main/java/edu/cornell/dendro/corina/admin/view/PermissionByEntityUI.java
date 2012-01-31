package edu.cornell.dendro.corina.admin.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.cornell.dendro.corina.admin.model.GroupsWithPermissionsTableModel;
import edu.cornell.dendro.corina.admin.model.UsersWithPermissionsTableModel;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.PermissionsEntityType;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.schema.WSIBox;
import edu.cornell.dendro.corina.schema.WSIPermission;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;
import edu.cornell.dendro.corina.wsi.corina.resources.PermissionsResource;

import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import javax.swing.JLabel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTabbedPane;

public class PermissionByEntityUI extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;
	private UsersWithPermissionsTableModel userTableModel;
	private GroupsWithPermissionsTableModel groupTableModel;
	private final static Logger log = LoggerFactory.getLogger(PermissionByEntityUI.class);
	private JTable tblUserPerms;
	private ArrayList<WSIPermission> permsList;
	private JTable tblGroupPerms;
	private JTabbedPane tabbedPane;
	private JPanel panel_1;
	private JPanel panel_2;

	/**
	 * Create the panel.
	 */
	public PermissionByEntityUI(ITridas entity) {
		
		
		
		setEntity(entity);



	}
	

	
	private void lookupUsersAndGroups(ITridas entity)
	{

		PermissionsEntityType pEntityType;

		if(entity instanceof TridasObject)
		{
			pEntityType = PermissionsEntityType.OBJECT;
		}
		else if(entity instanceof TridasElement)
		{
			pEntityType = PermissionsEntityType.ELEMENT;
		}
		else if(entity instanceof TridasMeasurementSeries)
		{
			pEntityType = PermissionsEntityType.MEASUREMENT_SERIES;
		}		
		else if(entity instanceof TridasDerivedSeries)
		{
			pEntityType = PermissionsEntityType.DERIVED_SERIES;
		}	
		else
		{
			Alert.error("Error", "Error searching for permissions info.  Entity provided is not valid.");
			log.error("Error searching for permissions info.  Entity provided is not valid.");
			return;
		}
		
		PermissionsResource resource = new PermissionsResource();
		
		for (WSISecurityUser user : (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary"))
		{
			if(!user.isIsActive()) continue;
			
			resource.addPermission(pEntityType, entity.getIdentifier().getValue(), user);
		}
		
		for (WSISecurityGroup grp : (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary"))
		{
			resource.addPermission(pEntityType, entity.getIdentifier().getValue(), grp);
		}
		
		// Query db 
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting permissions info");
			Alert.error("Error", "Error getting permissions info");
			return;
		}
		
		permsList = resource.getAssociatedResult();
		
		if(tblGroupPerms!=null) tblGroupPerms.repaint();
		if(tblUserPerms!=null) tblUserPerms.repaint();
		
		if(permsList.size()==0)
		{
			Alert.error("Error", "No records found");
			return;
		}
	}
	
	public void setEntity(ITridas entity)
	{
		lookupUsersAndGroups(entity);
		
		userTableModel = new UsersWithPermissionsTableModel(permsList);
		groupTableModel = new GroupsWithPermissionsTableModel(permsList);
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);
		
		panel_2 = new JPanel();
		tabbedPane.addTab("Group permissions", Builder.getIcon("edit_group.png", 22), panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollGroup = new JScrollPane();
		panel_2.add(scrollGroup);
		
		tblGroupPerms = new JTable(groupTableModel);
		scrollGroup.setViewportView(tblGroupPerms);
		
		tblGroupPerms.getColumnModel().getColumn(0).setPreferredWidth(15);
		tblGroupPerms.getColumnModel().getColumn(2).setPreferredWidth(45);
		tblGroupPerms.getColumnModel().getColumn(3).setPreferredWidth(45);
		tblGroupPerms.getColumnModel().getColumn(4).setPreferredWidth(45);
		tblGroupPerms.getColumnModel().getColumn(5).setPreferredWidth(45);
		tblGroupPerms.getColumnModel().getColumn(6).setPreferredWidth(300);
		
		panel_1 = new JPanel();
		tabbedPane.addTab("User permissions", Builder.getIcon("edit_user.png", 22), panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollUser = new JScrollPane();
		panel_1.add(scrollUser);
		
		tblUserPerms = new JTable(userTableModel);
		scrollUser.setViewportView(tblUserPerms);
		
		tblUserPerms.getColumnModel().getColumn(0).setPreferredWidth(15);
		tblUserPerms.getColumnModel().getColumn(4).setPreferredWidth(45);
		tblUserPerms.getColumnModel().getColumn(5).setPreferredWidth(45);
		tblUserPerms.getColumnModel().getColumn(6).setPreferredWidth(45);
		tblUserPerms.getColumnModel().getColumn(7).setPreferredWidth(45);
		tblUserPerms.getColumnModel().getColumn(8).setPreferredWidth(300);
	
		tblGroupPerms.addMouseListener(this);
		tblUserPerms.addMouseListener(this);


		
	}
	
	public ArrayList<WSIPermission> getUserPermissionsList()
	{
		return permsList;
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		
		// Only worry about double clicks
		if(e.getClickCount()<2) return;
			
		
		Component comp = e.getComponent();
		
		if(comp instanceof JTable)
		{
			if(((JTable) comp).getModel() instanceof GroupsWithPermissionsTableModel)
			{
				WSISecurityGroup group = ((GroupsWithPermissionsTableModel)((JTable) comp).getModel()).getSecurityGroupAtRow(tblGroupPerms.getSelectedRow());
				
				for (WSISecurityGroup grp : (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary"))
				{
					if(grp.getId().equals(group.getId()))
					{
						group = grp;
						break;
					}
				}

				GroupUIView groupDialog = new GroupUIView(null, true, group);
				groupDialog.setVisible(true);
				
			}
			else if (((JTable) comp).getModel() instanceof UsersWithPermissionsTableModel)
			{
				WSISecurityUser user = ((UsersWithPermissionsTableModel)((JTable) comp).getModel()).getSecurityUserAtRow(tblUserPerms.getSelectedRow());
				
				for (WSISecurityUser usr : (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary"))
				{
					if(usr.getId().equals(user.getId()))
					{
						user = usr;
						break;
					}
				}

				UserUIView userDialog = new UserUIView(null, true, user);
				userDialog.setVisible(true);
			}
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
