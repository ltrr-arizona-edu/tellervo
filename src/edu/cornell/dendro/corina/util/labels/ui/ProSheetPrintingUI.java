package edu.cornell.dendro.corina.util.labels.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import org.tridas.schema.BaseSeries;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.dbbrowse.SiteRenderer;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.tridasv2.TridasComparator;
import edu.cornell.dendro.corina.util.ArrayListModel;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntitySearchResource;
import edu.cornell.dendro.corina.wsi.corina.resources.SeriesSearchResource;


/**
 *
 * @author  peterbrewer
 */
public class ProSheetPrintingUI extends javax.swing.JPanel implements ActionListener{
    
	
	protected ArrayListModel<Element> selModel = new ArrayListModel<Element>();
	protected ArrayListModel<Element> availModel = new ArrayListModel<Element>();
	protected ArrayListModel<TridasObject> objModel = new ArrayListModel<TridasObject>();
	
    /** Creates new form ProSheetPrintingUI */
    public ProSheetPrintingUI() {
        initComponents();
        setupDialog();
        populateObjectList();
        cboObject.addActionListener(this);
        btnSelectObject.addActionListener(this);
        btnAdd.addActionListener(this);
        btnRemove.addActionListener(this);
    }
    
    private void setupDialog()
    {
		lstAvailable.setModel(availModel);
		lstAvailable.setCellRenderer(new TridasListCellRenderer());
		
		lstSelected.setModel(selModel);
		lstSelected.setCellRenderer(new TridasListCellRenderer());
		
		cboMaster.setModel(availModel);
		
		cboObject.setRenderer(new SiteRenderer());	
		cboObject.setModel(objModel);
    }
    
    private void populateObjectList()
    {
    	objModel.replaceContents(App.tridasObjects.getObjectList());
    	objModel.setSelectedItem(null);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboObject = new javax.swing.JComboBox();
        lblObject = new javax.swing.JLabel();
        btnSelectObject = new javax.swing.JButton();
        panelSeries = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnRemove = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstAvailable = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstSelected = new javax.swing.JList();
        lblAvailable = new javax.swing.JLabel();
        lblSelected = new javax.swing.JLabel();
        cboMaster = new javax.swing.JComboBox();
        lblMaster = new javax.swing.JLabel();
        panelComponents = new javax.swing.JPanel();
        chkMap = new javax.swing.JCheckBox();
        chkCrossdates = new javax.swing.JCheckBox();
        chkOutstanding = new javax.swing.JCheckBox();
        chkGantt = new javax.swing.JCheckBox();

        cboObject.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblObject.setText("Object:");

        btnSelectObject.setText("Select");

        panelSeries.setBorder(javax.swing.BorderFactory.createTitledBorder("Series"));

        btnRemove.setText("<");

        btnAdd.setText(">");

        lstAvailable.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstAvailable);

        lstSelected.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstSelected);

        lblAvailable.setText("Available:");

        lblSelected.setText("Selected:");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(lblAvailable)
                        .add(145, 145, 145)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(btnRemove, 0, 0, Short.MAX_VALUE)
                    .add(btnAdd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblSelected)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblAvailable)
                    .add(lblSelected))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(btnAdd)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 101, Short.MAX_VALUE)
                        .add(btnRemove))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                .addContainerGap())
        );

        cboMaster.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblMaster.setText("Master chronology:");

        org.jdesktop.layout.GroupLayout panelSeriesLayout = new org.jdesktop.layout.GroupLayout(panelSeries);
        panelSeries.setLayout(panelSeriesLayout);
        panelSeriesLayout.setHorizontalGroup(
            panelSeriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSeriesLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelSeriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelSeriesLayout.createSequentialGroup()
                        .add(lblMaster, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cboMaster, 0, 380, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelSeriesLayout.setVerticalGroup(
            panelSeriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSeriesLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelSeriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cboMaster, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblMaster))
                .add(21, 21, 21)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelComponents.setBorder(javax.swing.BorderFactory.createTitledBorder("Include components"));

        chkMap.setText("Map");
        chkMap.setEnabled(false);
        chkMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMapActionPerformed(evt);
            }
        });

        chkCrossdates.setText("Regional crossdates");
        chkCrossdates.setEnabled(false);

        chkOutstanding.setSelected(true);
        chkOutstanding.setText("Outstanding samples");

        chkGantt.setText("Gantt chart");
        chkGantt.setEnabled(false);

        org.jdesktop.layout.GroupLayout panelComponentsLayout = new org.jdesktop.layout.GroupLayout(panelComponents);
        panelComponents.setLayout(panelComponentsLayout);
        panelComponentsLayout.setHorizontalGroup(
            panelComponentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelComponentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelComponentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkMap)
                    .add(chkCrossdates))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 74, Short.MAX_VALUE)
                .add(panelComponentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(chkGantt)
                    .add(chkOutstanding))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        panelComponentsLayout.setVerticalGroup(
            panelComponentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelComponentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelComponentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkMap)
                    .add(chkGantt))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelComponentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkCrossdates)
                    .add(chkOutstanding))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(lblObject, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cboObject, 0, 306, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSelectObject))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(panelComponents, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(4, 4, 4)
                        .add(panelSeries, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnSelectObject)
                    .add(cboObject, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblObject))
                .add(18, 18, 18)
                .add(panelComponents, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(panelSeries, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chkMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMapActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_chkMapActionPerformed
    
    private void populateAvailableSeries()
    {
    	
    	TridasObject obj = (TridasObject) cboObject.getSelectedItem();
    	
		// Find all series for an object 
    	SearchParameters sampparam = new SearchParameters(SearchReturnObject.DERIVED_SERIES);
    	sampparam.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, obj.getIdentifier().getValue().toString());

    	// we want a series returned here
		SeriesSearchResource searchResource = new SeriesSearchResource(sampparam);
		
		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(searchResource);
		searchResource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting series");
			return;
		}
		
		
		ElementList seriesList = searchResource.getAssociatedResult();
		
		availModel.replaceContents(seriesList);	
		availModel.setSelectedItem(null);
		sortAvailableSeries();
    }
    
    private void sortAvailableSeries(){
		// Sort list intelligently
		/*TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		
		Collections.sort(availModel, numSorter);*/
    }
    
    
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		
		
		if (evt.getSource()==cboObject)
		{
			
		}
		if (evt.getSource()==btnSelectObject)
		{
			populateAvailableSeries();
			
		}
		
		
		if(evt.getSource() == btnAdd){

			for (Object obj : lstAvailable.getSelectedValues())
			{
				Element myobj = (Element) obj;
				selModel.add(myobj);
				availModel.remove(myobj);
				sortAvailableSeries();
			}
			
			
		}
			
		if(evt.getSource() == btnRemove)
		{
			for (Object obj : lstSelected.getSelectedValues())
			{
				Element myobj = (Element) obj;
				availModel.add(myobj);
				selModel.remove(myobj);
				sortAvailableSeries();

			}
		}
		
		
	}
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnAdd;
    protected javax.swing.JButton btnRemove;
    protected javax.swing.JButton btnSelectObject;
    protected javax.swing.JComboBox cboMaster;
    protected javax.swing.JComboBox cboObject;
    protected javax.swing.JCheckBox chkCrossdates;
    protected javax.swing.JCheckBox chkGantt;
    protected javax.swing.JCheckBox chkMap;
    protected javax.swing.JCheckBox chkOutstanding;
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JLabel lblAvailable;
    protected javax.swing.JLabel lblMaster;
    protected javax.swing.JLabel lblObject;
    protected javax.swing.JLabel lblSelected;
    protected javax.swing.JList lstAvailable;
    protected javax.swing.JList lstSelected;
    protected javax.swing.JPanel panelComponents;
    protected javax.swing.JPanel panelSeries;
    // End of variables declaration//GEN-END:variables
    
}
