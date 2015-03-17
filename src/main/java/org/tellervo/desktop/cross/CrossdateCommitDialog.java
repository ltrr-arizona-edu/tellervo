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
package org.tellervo.desktop.cross;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.view.FullEditor;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.manip.Redate;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleLoader;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.SeriesLinkUtil;
import org.tellervo.desktop.tridasv2.support.VersionUtil;
import org.tellervo.desktop.tridasv2.ui.ComboBoxFilterable;
import org.tellervo.desktop.tridasv2.ui.EnumComboBoxItemRenderer;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;
import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.SeriesLink;
import org.tridas.schema.TridasDating;
import org.tridas.schema.TridasDatingReference;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasInterpretation;




/**
 *
 * @author  peterbrewer
 */
public class CrossdateCommitDialog extends javax.swing.JDialog {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Sample sampleAsReference;
	private Sample sampleToDate;
	private Range range;
	private boolean saved;
	private NormalTridasDatingType originalDatingType;

    /** Creates new form CrossdateCommitDialog */
	public CrossdateCommitDialog(java.awt.Frame parent, boolean modal, 
			Sample sampleToDate, Sample sampleAsReference, Range range ) {
		super(parent, modal);
		initComponents();
		initialize();
		setSamples(sampleToDate, sampleAsReference, range);
		pack();
	}

    /** Creates new form CrossdateCommitDialog 
     * @wbp.parser.constructor*/
	public CrossdateCommitDialog(java.awt.Dialog parent, 
			Sample sampleToDate, Sample sampleAsReference, Range range) {
		super(parent, true);
		initComponents();
		initialize();
		setSamples(sampleToDate, sampleAsReference, range);
		pack();
	}

	public void initialize() {
		cboCertainty.setRenderer(new StarRenderer());
		cboCertainty.setModel(new DefaultComboBoxModel(new Integer[] {0, 1, 2, 3, 4, 5}));
		cboCertainty.setSelectedItem(5);		
		
		// word wrap...
		txtJustification.setLineWrap(true);
		txtJustification.setWrapStyleWord(true);
		
		setTitle("Save crossdate...");
		
		radNewCrossdate.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGuiByType();
			}
		});
		
		radRedateInPlace.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGuiByType();
			}
		});
		
		// buh-bye
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saved = false;
				dispose();
			}
		});
		
		// select all on focus! :)
		txtNewCrossdateName.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				txtNewCrossdateName.selectAll();
			}

			public void focusLost(FocusEvent e) {
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(commit()) {
					saved = true;
					dispose();
				}
			}
		});
		
		setGuiByType();
		

				
		this.setSize(new Dimension(808, 417));
		pack();
		
		
	}
	
	private void setGuiByType()
	{
		if(this.radNewCrossdate.isSelected())
		{
			this.cboCertainty.setEnabled(true);
			this.txtVersion.setEnabled(true);
			this.txtJustification.setEnabled(true);
			this.txtNewCrossdateName.setEnabled(true);
			this.cboDatingType.setEnabled(false);
		}
		else
		{
			this.cboCertainty.setEnabled(false);
			this.txtVersion.setEnabled(false);
			this.txtJustification.setEnabled(false);
			this.txtNewCrossdateName.setEnabled(false);
			this.cboDatingType.setEnabled(true);
		}
	}
	
	public boolean didSave() {
		return saved;
	}
	
	/**
	 * Go ahead and commit the crossdate
	 * 
	 * @return
	 */
	private boolean commit() {
		
		if(this.radNewCrossdate.isSelected())
		{
			return commitAsCrossdate();
		}
		else if (this.radNewRedate.isSelected())
		{
			return Redate.performTellervoWsiRedate(sampleToDate, txtNewCrossdateName.getText(), 
					txtVersion.getText(), txtJustification.getText(), (NormalTridasDatingType) this.cboDatingType.getSelectedItem(), 
					originalDatingType, range);
		}
		else if (this.radRedateInPlace.isSelected())
		{
			TridasDating dating = new TridasDating();
			dating.setType((NormalTridasDatingType) this.cboDatingType.getSelectedItem());
			sampleToDate.postEdit(Redate.redate(sampleToDate, range, dating));
			return true;
		}
		
		
		return false;
	}
		
	
	/**
	 * Commit the change as a true crossdate - linking to the reference series to maintain integrity
	 * 
	 * @return
	 */
	private boolean commitAsCrossdate()
	{
		if(txtNewCrossdateName.getText().length() == 0 ||
				txtJustification.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "All form fields must be filled in.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		SampleLoader loader = sampleToDate.getLoader();
		if(loader == null) {
			new Bug(new Exception("Attempting to apply an crossdate to a sample without a loader. Shouldn't be possible!"));
			return false;
		}
		
		/*
		 * a crossdate has
		 * 	- parent - the secondary sample
		 * 	- master - the master sample we crossdated against
		 *  - start year - a dating range
		 * 	- name - easy -> title
		 * 	- certainty - this is a number from 1 to 5, I think
		 * 	- justification - freeform text
		*/

		TridasDerivedSeries series = new TridasDerivedSeries();
		series.setTitle(txtNewCrossdateName.getText());
		// the identifier is based on the domain from the secondary
		series.setIdentifier(NewTridasIdentifier.getInstance(sampleToDate.getSeries().getIdentifier()));
		
		// Set version 
		if(txtVersion.getText()!=null) series.setVersion(txtVersion.getText());
		
		// this is a crossdate
		ControlledVoc voc = new ControlledVoc();
		voc.setValue(SampleType.CROSSDATE.toString());
		series.setType(voc);
		
		// set certainty and justification
		GenericFieldUtils.addField(series, "tellervo.crossdateConfidenceLevel", cboCertainty.getSelectedItem());		
		GenericFieldUtils.addField(series, "tellervo.justification", txtJustification.getText());
		
		// set the parent
		SeriesLinkUtil.addToSeries(series, sampleToDate.getSeries().getIdentifier());

		//
		// TODO: Stop sending this in both startYear AND newStartYear!
		// (determine which one is correct)
		//
		
		// create an interpretation for master and first year
		TridasInterpretation interpretation = new TridasInterpretation();
		
		// set first year
		interpretation.setFirstYear(range.getStart().tridasYearValue());
		
		// set "newStartYear" generic field for first year of new crossdate
		GenericFieldUtils.addField(series, "tellervo.newStartYear", range.getStart().toString());

		// get linkseries for master
		SeriesLink linkMaster = SeriesLinkUtil.forIdentifier(sampleAsReference.getSeries().getIdentifier());
		// make dating reference for master
		TridasDatingReference master = new TridasDatingReference();
		
		// tie together the hierarchy
		master.setLinkSeries(linkMaster);
		interpretation.setDatingReference(master);
		series.setInterpretation(interpretation);
		
		// make a new 'crossdate' dummy sample for saving
		Sample tmp = new Sample(series);		

		try {
			TellervoWsiTridasElement saver = new TellervoWsiTridasElement(series.getIdentifier());
			// here's where we do the "meat"
			if(saver.save(tmp)) {
				// put it in our menu
				OpenRecent.sampleOpened(new SeriesDescriptor(tmp));
				
				new FullEditor(tmp);
				
				// get out of here! :)
				return true;
			}
		} catch (IOException ioe) {
			Alert.error("Could not create crossdate", "Error: " + ioe.toString());
		}
		
		return false;
	}
	
	private void setSamples(Sample sampleAsReference, Sample sampleToDate, Range newRange) {	
		this.sampleAsReference = sampleAsReference;
		this.sampleToDate = sampleToDate;
		this.range = newRange;
		
		lblMasterSampleName.setText(sampleAsReference.toString());
		lblCrossdateName.setText(sampleToDate.toString());
		lblNewDateRange.setText(newRange.toString());
		
		//
		// BugID 90:
		//   The title field in the apply crossdate should be the same 
		//   as the displayTitle of the moving series that has been crossdated
		
		txtNewCrossdateName.setText(sampleToDate.meta().getName());
		txtNewCrossdateName.setEditable(false);
		
		// make the prefix more relevant if we have a labcode
		/*if (secondary.hasMeta(Metadata.LABCODE)) {
			lblprefix.setText(LabCodeFormatter.getSeriesPrefixFormatter().format(
					secondary.getMeta(Metadata.LABCODE, LabCode.class))
					+ "- ");
		}
		else
		{
			lblprefix.setVisible(false);
		}*/
		
		//txtNewCrossdateName.setText("Cross " + primary.meta().getName() + 
		//		"/" + secondary.meta().getName());	
		// txtNewCrossdateName.requestFocus();
		
		// Version field
		if(sampleToDate.getSeries() instanceof ITridasDerivedSeries) {
			String parentVersion = ((ITridasDerivedSeries) sampleToDate.getSeries()).getVersion();
			
			txtVersion.setText(VersionUtil.nextVersion(parentVersion));
		}
		else {
			// default to v. 2
			txtVersion.setText("2");
		}
		
		// Select dating type of original series
		try{
			originalDatingType = sampleToDate.getSeries().getInterpretation().getDating().getType();
		}
		catch (Exception e)
		{
			originalDatingType = null;
		}
		
		for(int i=0; i<cboDatingType.getItemCount(); i++)
		{
				NormalTridasDatingType dt = (NormalTridasDatingType) cboDatingType.getItemAt(i);			
				if(dt.equals(originalDatingType))
				{
					cboDatingType.setSelectedIndex(i);
				}
		}
		
	}

	/**
	 * A quick and dirty class to render stars in a combo box
	 */
	private class StarRenderer implements ListCellRenderer {
		private Icon image;

		public StarRenderer() {
		    image = Builder.getIcon("star.png", 16);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			if(value instanceof Number) {
				Integer val = ((Number)value).intValue();
				
				if(val == 0) {
					JLabel tmp = new JLabel("Zero");

					tmp.setHorizontalAlignment(SwingConstants.CENTER);
					tmp.setOpaque(true);
					tmp.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
					
					return tmp;
				}
				
				JPanel panel = new JPanel(new FlowLayout());
				
				panel.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
				
				for(int i = 0; i < val; i++) 
					panel.add(new JLabel(image));

				return panel;
			}
			
			return null;
		}
	}
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new MigLayout("", "[70:70px:70px][244.00px,grow,fill]", "[88.00px][241px,grow,fill][37px]"));
        
        lblIcon = new JLabel("");
        lblIcon.setIcon(Builder.getIcon("crossdate.png", 64));
        getContentPane().add(lblIcon, "cell 0 0,alignx center,aligny center");
                
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new MigLayout("", "[][149.00,fill][grow,fill]", "[][][][][][][][grow,top][]"));
        lblFloatingSeries = new javax.swing.JLabel();
        panelMain.add(lblFloatingSeries, "cell 0 0");
        
                lblFloatingSeries.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                lblFloatingSeries.setText("Series being crossdated:");
                lblCrossdateName = new javax.swing.JLabel();
                panelMain.add(lblCrossdateName, "cell 1 0 2 1");
                
                        lblCrossdateName.setText(App.getLabCodePrefix()+"XXX-XX-X-X-X (1234-2345)");
                lblNewRange = new javax.swing.JLabel();
                panelMain.add(lblNewRange, "cell 0 1,alignx right");
                
                        lblNewRange.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                        lblNewRange.setText("New date range:");
                        lblNewRange.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                        lblNewDateRange = new javax.swing.JLabel();
                        panelMain.add(lblNewDateRange, "cell 1 1 2 1");
                        
                                lblNewDateRange.setText("2345-5678");
                        lblReferenceSeries = new javax.swing.JLabel();
                        panelMain.add(lblReferenceSeries, "flowy,cell 0 2,alignx right");
                        
                                lblReferenceSeries.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                                lblReferenceSeries.setText("Reference series:");
                                lblMasterSampleName = new javax.swing.JLabel();
                                panelMain.add(lblMasterSampleName, "cell 1 2 2 1");
                                
                                        lblMasterSampleName.setText(App.getLabCodePrefix()+"XXX-XX-X-X-X (9876-5432)");
                                        
                                        lblDatingType = new JLabel("Dating type:");
                                        panelMain.add(lblDatingType, "cell 0 3,alignx trailing");
                                        
                                        cboDatingType = getDatingTypeComboBox();
                                        panelMain.add(cboDatingType, "cell 1 3,growx");
                                                lblNewCrossdateName = new javax.swing.JLabel();
                                                panelMain.add(lblNewCrossdateName, "cell 0 4,alignx right");
                                                
                                                        lblNewCrossdateName.setText("Series:");
                                                        txtNewCrossdateName = new javax.swing.JTextField();
                                                        panelMain.add(txtNewCrossdateName, "cell 1 4,growx");
                                                        
                                                                txtNewCrossdateName.setEnabled(false);
                                                                lblVersion = new javax.swing.JLabel();
                                                                panelMain.add(lblVersion, "cell 0 5,alignx right");
                                                                
                                                                        lblVersion.setText("Version:");
                                                                        txtVersion = new javax.swing.JTextField();
                                                                        panelMain.add(txtVersion, "cell 1 5,growx");
                                                                        lblCertainty = new javax.swing.JLabel();
                                                                        panelMain.add(lblCertainty, "cell 0 6,alignx right");
                                                                        
                                                                                lblCertainty.setText("Certainty:");
                                                                                cboCertainty = new javax.swing.JComboBox();
                                                                                panelMain.add(cboCertainty, "cell 1 6,growx");
                                                                                lblJustification = new javax.swing.JLabel();
                                                                                panelMain.add(lblJustification, "cell 0 7,alignx right,aligny top");
                                                                                
                                                                                        lblJustification.setText("Justification:");
                                                                                        jScrollPane1 = new javax.swing.JScrollPane();
                                                                                        panelMain.add(jScrollPane1, "cell 1 7 2 1,grow");
                                                                                        txtJustification = new javax.swing.JTextArea();
                                                                                        
                                                                                                txtJustification.setColumns(20);
                                                                                                txtJustification.setRows(5);
                                                                                                jScrollPane1.setViewportView(txtJustification);
        getContentPane().add(panelMain, "cell 1 1,alignx left,aligny top");
        
        panelButtons = new JPanel();
        getContentPane().add(panelButtons, "cell 1 2,growx,aligny top");
        panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        
                btnCancel = new javax.swing.JButton();
                panelButtons.add(btnCancel);
                
                        btnCancel.setText("Cancel");
        btnOk = new javax.swing.JButton();
        panelButtons.add(btnOk);
        
                btnOk.setText("OK");
                                
                                panelApplyType = new JPanel();
                                getContentPane().add(panelApplyType, "cell 1 0,growx,aligny top");
                                panelApplyType.setLayout(new MigLayout("", "[grow]", "[][][][]"));
                                
                                lblApplyCrossdateBy = new JLabel("Apply crossdate by:");
                                panelApplyType.add(lblApplyCrossdateBy, "cell 0 0");
                                
                                radNewCrossdate = new JRadioButton("Creating a new series linked to the reference series");
                                radNewCrossdate.setSelected(true);
                                panelApplyType.add(radNewCrossdate, "cell 0 1");
                                
                                radNewRedate = new JRadioButton("Creating a new redated series with no link to the reference series ");
                                panelApplyType.add(radNewRedate, "cell 0 2");
                                
                                radRedateInPlace = new JRadioButton("Redating the existing series in place");
                                panelApplyType.add(radRedateInPlace, "cell 0 3");

                                
                                ButtonGroup rbgroup = new ButtonGroup();
                                rbgroup.add(radNewCrossdate);
                                rbgroup.add(radNewRedate);
                                rbgroup.add(radRedateInPlace);
                                
                                
        pack();
        
    }// </editor-fold>//GEN-END:initComponents
    
    
    /**
     * Build a combo box for the dating type
     * 
     * @return
     */
	private JComboBox getDatingTypeComboBox() 
	{
		final JComboBox cboDatingType = new ComboBoxFilterable(NormalTridasDatingType.values());
		
		cboDatingType.setRenderer(new EnumComboBoxItemRenderer());
		
		return cboDatingType;
	}
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnCancel;
    protected javax.swing.JButton btnOk;
    protected javax.swing.JComboBox cboCertainty;
    protected javax.swing.JLabel lblJustification;
    protected javax.swing.JLabel lblCertainty;
    protected javax.swing.JLabel lblVersion;
    protected javax.swing.JLabel lblNewCrossdateName;
    protected javax.swing.JLabel lblReferenceSeries;
    protected javax.swing.JLabel lblNewRange;
    protected javax.swing.JLabel lblFloatingSeries;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JLabel lblCrossdateName;
    protected javax.swing.JLabel lblMasterSampleName;
    protected javax.swing.JLabel lblNewDateRange;
    protected javax.swing.JTextArea txtJustification;
    protected javax.swing.JTextField txtNewCrossdateName;
    protected javax.swing.JTextField txtVersion;
    private JPanel panelButtons;
    private JPanel panelApplyType;
    private JRadioButton radNewCrossdate;
    private JRadioButton radRedateInPlace;
    private JLabel lblApplyCrossdateBy;
    private JLabel lblIcon;
    private JLabel lblDatingType;
    private JComboBox cboDatingType;
    private JRadioButton radNewRedate;
}
