package org.tellervo.desktop.testing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.TellervoRequestType;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLaboratory;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

public class WSTester extends JDialog implements ActionListener{

	private final JPanel contentPanel = new JPanel();
	private JTable tbltest;
	private DefaultTableModel tablemodel;

	private TridasProject project;
	private TridasObject object;
	private TridasObject subobject;
	private TridasElement element;
	private TridasSample sample;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WSTester dialog = new WSTester();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WSTester() {
		
		init();
	}
	
	private void init() {
		setTitle("Webservice Tester");
		this.setIconImage(Builder.getApplicationIcon());
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				tbltest = new JTable();
						
						
				tablemodel=	new DefaultTableModel(
					new Object[][] {
						{"Create project", null, null},
						{"Create object", null, null},
						{"Create subobject", null, null},
						{"Create element", null, null},
						{"Create box", null, null},
						{"Create sample", null, null},
						{"Create radius", null, null},
						{"Create measurement", null, null},
						{"Create index", null, null},
						{"Update project", null, null},
						{"Update object", null, null},
						{"Update element", null, null},
						{"Update box", null, null},
						{"Update sample", null, null},
						{"Update radius", null, null},
						{"Update measurement", null, null},
						{"Delete measurement", null, null},
						{"Delete radius", null, null},
						{"Delete sample", null, null},
						{"Delete box", null, null},
						{"Delete element", null, null},
						{"Delete subobject", null, null},
						{"Delete object", null, null},
						{"Delete project", null, null},
					},
					new String[] {
						"Test", "Result", "Information"
					}
				);
				
				
				tbltest.setModel(tablemodel);
		
				tbltest.getColumnModel().getColumn(0).setPreferredWidth(165);
				scrollPane.setViewportView(tbltest);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnRun = new JButton("Run");
				btnRun.setActionCommand("Run");
				btnRun.addActionListener(this);
				buttonPane.add(btnRun);
				getRootPane().setDefaultButton(btnRun);
			}
			{
				JButton btnClose = new JButton("Close");
				btnClose.setActionCommand("Close");
				btnClose.addActionListener(this);
				buttonPane.add(btnClose);
			}
		}
	}

	private void runTests() {

		for(int i=0; i<tablemodel.getRowCount(); i++) {
			
			String testname = (String) tablemodel.getValueAt(i, 0);
			
			if(testname=="Create project")
			{
				project = new TridasProject();
				project.setTitle("WEBSERVICE TEST");
				project.setInvestigator("TEST");
				project.setPeriod("TEST");
				List<ControlledVoc> types = Dictionary
						.getMutableDictionary("projectTypeDictionary");
				project.setTypes(types);
				
				List<ControlledVoc> categories = Dictionary
						.getMutableDictionary("projectCategoryDictionary");
				project.setCategory(categories.get(0));
				
				ArrayList<TridasLaboratory> laboratories = new ArrayList<TridasLaboratory>();
				TridasLaboratory lab = new TridasLaboratory();
				TridasLaboratory.Name labName = new TridasLaboratory.Name();
				labName.setAcronym(App.getLabAcronym());
				labName.setValue(App.getLabName());
				lab.setName(labName);
				TridasAddress address = new TridasAddress();
				lab.setAddress(address);	
				laboratories.add(lab);
				project.setLaboratories(laboratories);
								

				EntityResource<TridasProject> resource = new EntityResource<TridasProject>(project, TellervoRequestType.CREATE, TridasProject.class);

				TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(this, resource, i, tablemodel.getRowCount());
				
				resource.query();
				dialog.setVisible(true);
				
				if(!dialog.isSuccessful()) {
					tablemodel.setValueAt("Fail", i, 1);
					tablemodel.setValueAt(dialog.getFailException().getLocalizedMessage(), i, 2);
				}
				else
				{
					tablemodel.setValueAt("Pass", i, 1);
					tablemodel.setValueAt("", i, 2);
					project = resource.getAssociatedResult();
				}
			}
			if(testname=="Delete project")
			{
				EntityResource<TridasProject> resource = new EntityResource<TridasProject>(project, TellervoRequestType.DELETE, TridasProject.class);

				TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(this, resource, i, tablemodel.getRowCount());
				
				resource.query();
				dialog.setVisible(true);
				
				if(!dialog.isSuccessful()) {
					tablemodel.setValueAt("Fail", i, 1);
					tablemodel.setValueAt(dialog.getFailException().getLocalizedMessage(), i, 2);
				}
				else
				{
					tablemodel.setValueAt("Pass", i, 1);
					tablemodel.setValueAt("", i, 2);
					project = null;
				}
			}
			if(testname=="Create object")
			{
				object = new TridasObject();
				object.setTitle("TEST");
				
				EntityResource<TridasObject> resource = new EntityResource<TridasObject>(object, TellervoRequestType.CREATE, TridasObject.class);
				
				TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(this, resource, i, tablemodel.getRowCount());
				
				resource.query();
				dialog.setVisible(true);
				
				if(!dialog.isSuccessful()) {
					tablemodel.setValueAt("Fail", i, 1);
					tablemodel.setValueAt(dialog.getFailException().getLocalizedMessage(), i, 2);
				}
				else
				{
					tablemodel.setValueAt("Pass", i, 1);
					tablemodel.setValueAt("", i, 2);
					object = resource.getAssociatedResult();
				}
			}
			if(testname=="Delete object")
			{
				EntityResource<TridasObject> resource = new EntityResource<TridasObject>(object, TellervoRequestType.DELETE, TridasObject.class);

				TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(this, resource, i, tablemodel.getRowCount());
				
				resource.query();
				dialog.setVisible(true);
				
				if(!dialog.isSuccessful()) {
					tablemodel.setValueAt("Fail", i, 1);
					tablemodel.setValueAt(dialog.getFailException().getLocalizedMessage(), i, 2);
				}
				else
				{
					tablemodel.setValueAt("Pass", i, 1);
					tablemodel.setValueAt("", i, 2);
					o = null;
				}
			}			
			
			
		}
		
	}
	
	
	public static void showDialog() {
		try {
			WSTester dialog = new WSTester();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="Run")
		{
			this.runTests();
		}
		else if (e.getActionCommand()=="Close")
		{
			this.setVisible(false);
		}
		
	}

}
