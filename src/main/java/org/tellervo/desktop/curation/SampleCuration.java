/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.curation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.admin.SampleListTableModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.AddGISDataDialog;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.gui.dbbrowse.TridasObjectRenderer;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.SoundUtil;
import org.tellervo.desktop.util.SoundUtil.SystemSound;
import org.tellervo.desktop.util.labels.LabBarcode;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tridas.interfaces.ITridas;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;


/**
 * GUI class to allow users to find the physical location of the sample they are
 * interested in.   
 *
 * @author  peterbrewer
 */
public class SampleCuration extends javax.swing.JDialog implements ActionListener{
    
	private final static Logger log = LoggerFactory.getLogger(SampleCuration.class);

	private static final long serialVersionUID = 7814293298626840188L;
	ButtonGroup group = new ButtonGroup();
	protected ArrayListModel<TridasObject> objModel = new ArrayListModel<TridasObject>();
	protected ArrayListModel<TridasElement> elModel = new ArrayListModel<TridasElement>();
	protected ArrayListModel<TridasSample> sampModel = new ArrayListModel<TridasSample>();
	protected SampleListTableModel resultModel = new SampleListTableModel();
	
	
    /** Creates new form SampleCuration */
    public SampleCuration(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();   


    }
    

    /**
     * Show the actual dialog
     */
    public static void showDialog() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final SampleCuration dialog = new SampleCuration(new javax.swing.JFrame(), false);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.dispose();
                    }
                });
        
                dialog.setupGui();
                dialog.internationalizeComponents();
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

            }
        });
    }

    private void internationalizeComponents()
    {
    	
    
    }
    
    /**
     * Setup the GUI.  Called from showDialog()
     */
    private void setupGui()
    {    	
        setIconImage(Builder.getApplicationIcon());
       	

        btnOk.addActionListener(this);
      
         
        // Set models 
        tblResults.setModel(resultModel);
                    
        // Rmove the checklist column
        tblResults.removeColumn(tblResults.getColumn("Temporary Checklist"));
        
    }
    

    
    
  

   
    
	private void doSearchFromEntityPicker(ArrayList<ITridas> entitylist) {
		
		ITridas entity = entitylist.get(0);
		
		// Set return type to samples
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
    	
		
		if(entity instanceof TridasObject)
		{
			 param.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, SearchOperator.EQUALS, ((TridasObject)entity).getIdentifier().getValue());
		}
		else if (entity instanceof TridasElement)
		{
			param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, entity.getIdentifier().getValue());;
		}
		else if (entity instanceof TridasSample)
		{
			param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, entity.getIdentifier().getValue());;
		}
		else
		{
			log.error("Unknonw entity type");
			return;
		}
    	


    	// we want a sample returned here
		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting samples");
			return;
		}
		
		List<TridasSample> sampList = resource.getAssociatedResult();
		
		// Check to see if any samples were found
		if (sampList.size()==0) 
		{
			Alert.error("None found", "No samples were found");
			return;
		}
		
	/*	TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		*/
		
		resultModel.setSamples(sampList); 
		resultModel.fireTableDataChanged();
    	
    	
		
	}
    
    
    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    @SuppressWarnings("serial")
	private void initComponents() {

        setTitle("Find a sample...");
        
        panel = new JPanel();
        panel.setLayout(new MigLayout("", "[grow]", "[][grow,fill][]"));
                
                TridasEntityPickerPanel pickerPanel = new TridasEntityPickerPanel(this, TridasSample.class, EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT );
                //pickerPanel.setMinimalGui(true);
                pickerPanel.setShutdownOnSelect(false);
                
                ((TridasEntityPickerPanel) pickerPanel).addTridasSelectListener(new TridasSelectListener(){

					@Override
					public void entitySelected(TridasSelectEvent event) {
						ArrayList<ITridas> entitylist = event.getEntityList();

						// Nothing found so return 
						if(entitylist==null) {
							
							log.debug("No sampels selected");
							return;
						}
						
						
						doSearchFromEntityPicker(entitylist);
						
						
						
					}


                	
                });
                
                panel.add(pickerPanel, "cell 0 0,grow");
                                                                                                                panelResults = new javax.swing.JPanel();
                                                                                                                panel.add(panelResults, "cell 0 1,growx");
                                                                                                                scrollResults = new javax.swing.JScrollPane();
                                                                                                                tblResults = new JXTable();
                                                                                                                tblResults.setAutoCreateRowSorter(true);
                                                                                                                
                                                                                                                        tblResults.setModel(new javax.swing.table.DefaultTableModel(
                                                                                                                            new Object [][] {
                                                                                                                                {null, null, null}
                                                                                                                            },
                                                                                                                            new String [] {
                                                                                                                                "Sample", "Box", "Location"
                                                                                                                            }
                                                                                                                        ) {
                                                                                                                            boolean[] canEdit = new boolean [] {
                                                                                                                                false, false, false
                                                                                                                            };
                                                                                                                
                                                                                                                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                                                                                                                return canEdit [columnIndex];
                                                                                                                            }
                                                                                                                        });
                                                                                                                        scrollResults.setViewportView(tblResults);
                                                                                                                        panelResults.setLayout(new MigLayout("", "[314.00px,grow]", "[150px,grow]"));
                                                                                                                        panelResults.add(scrollResults, "cell 0 0,grow");
                                                                                                                        panelButtons = new javax.swing.JPanel();
                                                                                                                        panel.add(panelButtons, "cell 0 2,growx");
                                                                                                                        btnOk = new javax.swing.JButton();
                                                                                                                        sep = new javax.swing.JSeparator();
                                                                                                                        
                                                                                                                                btnOk.setText("Ok");
                                                                                                                                
                                                                                                                                        sep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                                                                                                                                        sep.setMaximumSize(new java.awt.Dimension(9999, 2));
                                                                                                                                        sep.setMinimumSize(new java.awt.Dimension(30, 2));
                                                                                                                                        sep.setPreferredSize(new java.awt.Dimension(50, 2));
                                                                                                                                        panelButtons.setLayout(new MigLayout("", "[410px,grow]", "[2px][25px]"));
                                                                                                                                        panelButtons.add(sep, "cell 0 0,growx,aligny top");
                                                                                                                                        panelButtons.add(btnOk, "cell 0 1,alignx right,aligny top");
        getContentPane().setLayout(new BorderLayout(0, 0));
        getContentPane().add(panel);
    }// </editor-fold>//GEN-END:initComponents

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnOk;
    protected javax.swing.JPanel panelButtons;
    protected javax.swing.JPanel panelResults;
    protected javax.swing.JScrollPane scrollResults;
    protected javax.swing.JSeparator sep;
    protected JXTable tblResults;
    private JPanel panel;
    // End of variables declaration//GEN-END:variables

    
    /**
     * Perform actions
     */
	public void actionPerformed(ActionEvent e) {

		Object btn = e.getSource();
		
	
		if (btn==btnOk)
		{
			this.dispose();
		}
		
		
	}
    
}
