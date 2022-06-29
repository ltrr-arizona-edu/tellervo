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
import org.tellervo.desktop.testing.WSTest.WSTestKey;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIBox;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLaboratory;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

public class WSTester extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable tbltest;
	private WSTestTableModel tablemodel;

	private TridasProject project;
	private TridasObject object;
	private TridasObject subobject;
	private TridasElement element;
	private WSIBox box;
	private TridasSample sample;
	private TridasRadius radius;
	private TridasMeasurementSeries series;
	private TridasDerivedSeries dseries;
	
	
	
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
				tablemodel = new WSTestTableModel();
				tbltest.setModel(tablemodel);
				tbltest.getColumnModel().getColumn(0).setPreferredWidth(200);
				tbltest.getColumnModel().getColumn(0).setMaxWidth(300);
				tbltest.getColumnModel().getColumn(1).setPreferredWidth(70);
				tbltest.getColumnModel().getColumn(1).setMaxWidth(100);
				tbltest.getColumnModel().getColumn(2).setPreferredWidth(500);
				tbltest.getColumnModel().getColumn(2).setMaxWidth(5000);
				tbltest.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

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
			
			WSTest test = tablemodel.getTest(i);
			WSTestKey testname = test.getKey();

			try {
				if(testname.equals(WSTestKey.CREATE_PROJECT))
				{
					boolean pass = test.runTestCreateProject(i, tablemodel.getRowCount());
					tablemodel.setTest(i, test);
					if(pass)
					{
						project = (TridasProject) test.getReturnObject();
					}
				}
				if(testname.equals(WSTestKey.DELETE_PROJECT))
				{
					boolean pass = test.runTestDeleteProject(i, tablemodel.getRowCount(), project);
					tablemodel.setTest(i, test);
					if(pass)
					{
						project = null;
					}
				}
				if(testname.equals(WSTestKey.CREATE_OBJECT))
				{
					boolean pass = test.runTestCreateObject(i, tablemodel.getRowCount(), project);
					tablemodel.setTest(i, test);
					if(pass)
					{
						object = (TridasObject) test.getReturnObject();
					}
				}
				if(testname.equals(WSTestKey.DELETE_OBJECT))
				{
					boolean pass = test.runTestDeleteObject(i, tablemodel.getRowCount(), object);
					tablemodel.setTest(i, test);
					if(pass)
					{
						object = null;
					}
				}		
				if(testname.equals(WSTestKey.CREATE_SUBOBJECT))
				{
					boolean pass = test.runTestCreateSubobject(i, tablemodel.getRowCount(), object);
					tablemodel.setTest(i, test);
					if(pass)
					{
						subobject = (TridasObject) test.getReturnObject();
					}
				}
				if(testname.equals(WSTestKey.DELETE_SUBOBJECT))
				{
					boolean pass = test.runTestDeleteSubobject(i, tablemodel.getRowCount(), subobject);
					tablemodel.setTest(i, test);
					if(pass)
					{
						subobject = null;
					}
				}	
				if(testname.equals(WSTestKey.CREATE_ELEMENT))
				{
					boolean pass = test.runTestCreateElement(i, tablemodel.getRowCount(), object);
					tablemodel.setTest(i, test);
					if(pass)
					{
						element = (TridasElement) test.getReturnObject();
					}
				}
				if(testname.equals(WSTestKey.DELETE_ELEMENT))
				{
					boolean pass = test.runTestDeleteElement(i, tablemodel.getRowCount(), element);
					tablemodel.setTest(i, test);
					if(pass)
					{
						element = null;
					}
				}	
				if(testname.equals(WSTestKey.CREATE_BOX))
				{
					boolean pass = test.runTestCreateBox(i, tablemodel.getRowCount());
					tablemodel.setTest(i, test);
					if(pass)
					{
						box = (WSIBox) test.getReturnObject();
					}
				}
				if(testname.equals(WSTestKey.DELETE_BOX))
				{
					boolean pass = test.runTestDeleteBox(i, tablemodel.getRowCount(), box);
					tablemodel.setTest(i, test);
					if(pass)
					{
						box = null;
					}
				}
				if(testname.equals(WSTestKey.CREATE_SAMPLE))
				{
					boolean pass = test.runTestCreateSample(i, tablemodel.getRowCount(), element);
					tablemodel.setTest(i, test);
					if(pass)
					{
						sample = (TridasSample) test.getReturnObject();
					}
				}
				if(testname.equals(WSTestKey.DELETE_SAMPLE))
				{
					boolean pass = test.runTestDeleteSample(i, tablemodel.getRowCount(), sample);
					tablemodel.setTest(i, test);
					if(pass)
					{
						sample = null;
					}
				}				
				if(testname.equals(WSTestKey.CREATE_RADIUS))
				{
					boolean pass = test.runTestCreateRadius(i, tablemodel.getRowCount(), sample);
					tablemodel.setTest(i, test);
					if(pass)
					{
						radius = (TridasRadius) test.getReturnObject();
					}
				}
				if(testname.equals(WSTestKey.DELETE_RADIUS))
				{
					boolean pass = test.runTestDeleteRadius(i, tablemodel.getRowCount(), radius);
					tablemodel.setTest(i, test);
					if(pass)
					{
						radius = null;
					}
				}	
				if(testname.equals(WSTestKey.CREATE_SERIES))
				{
					boolean pass = test.runTestCreateSeries(i, tablemodel.getRowCount(), radius);
					tablemodel.setTest(i, test);
					if(pass)
					{
						series = (TridasMeasurementSeries) test.getReturnObject();
					}
				}
				if(testname.equals(WSTestKey.DELETE_SERIES))
				{
					boolean pass = test.runTestDeleteSeries(i, tablemodel.getRowCount(), series);
					tablemodel.setTest(i, test);
					if(pass)
					{
						series = null;
					}
				}	
				if(testname.equals(WSTestKey.TAXON_DICTIONARY))
				{
					test.runTestTaxonDictionary(i, tablemodel.getRowCount());
					tablemodel.setTest(i, test);
				}	
				if(testname.equals(WSTestKey.REMARK_DICTIONARY))
				{
					test.runTestRemarkDictionary(i, tablemodel.getRowCount());
					tablemodel.setTest(i, test);
				}	
				if(testname.equals(WSTestKey.CREATE_INDEX))
				{
					boolean pass = test.runTestCreateIndex(i, tablemodel.getRowCount(), series);
					tablemodel.setTest(i, test);
					if(pass)
					{
						dseries = (TridasDerivedSeries) test.getReturnObject();
					}
				}	
				if(testname.equals(WSTestKey.DELETE_INDEX))
				{
					boolean pass = test.runTestDeleteIndex(i, tablemodel.getRowCount(), dseries);
					tablemodel.setTest(i, test);
					if(pass)
					{
						dseries = null;
					}
				}					
				
			} catch (Exception ex)
			{
				// Set test
				test.setErrorMessage(ex.getLocalizedMessage());
				test.setTestResult(false);
				tablemodel.setTest(i, test);
			}
		}
				
	}
	
	public static void showDialog() {
		try {
			WSTester dialog = new WSTester();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModal(true);
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
