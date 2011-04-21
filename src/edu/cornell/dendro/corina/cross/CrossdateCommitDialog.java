package edu.cornell.dendro.corina.cross;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.SeriesLink;
import org.tridas.schema.TridasDatingReference;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasInterpretation;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.io.Metadata;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.tridasv2.GenericFieldUtils;
import edu.cornell.dendro.corina.tridasv2.LabCode;
import edu.cornell.dendro.corina.tridasv2.LabCodeFormatter;
import edu.cornell.dendro.corina.tridasv2.SeriesLinkUtil;
import edu.cornell.dendro.corina.tridasv2.support.VersionUtil;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;
import edu.cornell.dendro.corina.wsi.corina.NewTridasIdentifier;



/**
 *
 * @author  peterbrewer
 */
public class CrossdateCommitDialog extends javax.swing.JDialog {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Sample primary;
	private Sample secondary;
	private Range range;
	private boolean saved;

    /** Creates new form CrossdateCommitDialog */
	public CrossdateCommitDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		initialize();
	}

    /** Creates new form CrossdateCommitDialog 
     * @wbp.parser.constructor*/
	public CrossdateCommitDialog(java.awt.Dialog parent) {
		super(parent, true);
		initComponents();
		initialize();
	}

	public void initialize() {
		cboCertainty.setRenderer(new StarRenderer());
		cboCertainty.setModel(new DefaultComboBoxModel(new Integer[] {0, 1, 2, 3, 4, 5}));
		cboCertainty.setSelectedItem(3);		
		
		// word wrap...
		txtJustification.setLineWrap(true);
		txtJustification.setWrapStyleWord(true);
		
		setTitle("Save crossdate...");
		
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
		
	}
	
	public boolean didSave() {
		return saved;
	}
	
	private boolean commit() {
		if(txtNewCrossdateName.getText().length() == 0 ||
				txtJustification.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "All form fields must be filled in.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		SampleLoader loader = secondary.getLoader();
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
		series.setIdentifier(NewTridasIdentifier.getInstance(secondary.getSeries().getIdentifier()));
		
		// Set version 
		if(txtVersion.getText()!=null) series.setVersion(txtVersion.getText());
		
		// this is a crossdate
		ControlledVoc voc = new ControlledVoc();
		voc.setValue(SampleType.CROSSDATE.toString());
		series.setType(voc);
		
		// set certainty and justification
		GenericFieldUtils.addField(series, "corina.crossdateConfidenceLevel", cboCertainty.getSelectedItem());		
		GenericFieldUtils.addField(series, "corina.justification", txtJustification.getText());
		
		// set the parent
		SeriesLinkUtil.addToSeries(series, secondary.getSeries().getIdentifier());

		//
		// TODO: Stop sending this in both startYear AND newStartYear!
		// (determine which one is correct)
		//
		
		// create an interpretation for master and first year
		TridasInterpretation interpretation = new TridasInterpretation();
		
		// set first year
		interpretation.setFirstYear(range.getStart().tridasYearValue());
		
		// set "newStartYear" generic field for first year of new crossdate
		GenericFieldUtils.addField(series, "corina.newStartYear", range.getStart().toString());

		// get linkseries for master
		SeriesLink linkMaster = SeriesLinkUtil.forIdentifier(primary.getSeries().getIdentifier());
		// make dating reference for master
		TridasDatingReference master = new TridasDatingReference();
		
		// tie together the hierarchy
		master.setLinkSeries(linkMaster);
		interpretation.setDatingReference(master);
		series.setInterpretation(interpretation);
		
		// make a new 'crossdate' dummy sample for saving
		Sample tmp = new Sample(series);		

		try {
			CorinaWsiTridasElement saver = new CorinaWsiTridasElement(series.getIdentifier());
			// here's where we do the "meat"
			if(saver.save(tmp)) {
				// put it in our menu
				OpenRecent.sampleOpened(new SeriesDescriptor(tmp));
				
				new Editor(tmp);
				
				// get out of here! :)
				return true;
			}
		} catch (IOException ioe) {
			Alert.error("Could not create crossdate", "Error: " + ioe.toString());
		}
		
		return false;
	}
	
	public void setup(Sample primary, Sample secondary, Range newRange) {	
		this.primary = primary;
		this.secondary = secondary;
		this.range = newRange;
		
		lblMasterSampleName.setText(primary.toString());
		lblCrossdateName.setText(secondary.toString());
		lblNewDateRange.setText(newRange.toString());
		
		//
		// BugID 90:
		//   The title field in the apply crossdate should be the same 
		//   as the displayTitle of the moving series that has been crossdated
		
		txtNewCrossdateName.setText(secondary.meta().getName());
		txtNewCrossdateName.setEditable(false);
		
		// make the prefix more relevant if we have a labcode
		if (secondary.hasMeta(Metadata.LABCODE)) {
			lblprefix.setText(LabCodeFormatter.getSeriesPrefixFormatter().format(
					secondary.getMeta(Metadata.LABCODE, LabCode.class))
					+ "- ");
		}
		else
		{
			lblprefix.setVisible(false);
		}
		
		//txtNewCrossdateName.setText("Cross " + primary.meta().getName() + 
		//		"/" + secondary.meta().getName());	
		// txtNewCrossdateName.requestFocus();
		
		// Version field
		if(secondary.getSeries() instanceof ITridasDerivedSeries) {
			String parentVersion = ((ITridasDerivedSeries) secondary.getSeries()).getVersion();
			
			txtVersion.setText(VersionUtil.nextVersion(parentVersion));
		}
		else {
			// default to v. 2
			txtVersion.setText("2");
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
        lblprefix = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        label = new JLabel("");
        getContentPane().add(label);
        
        label_1 = new JLabel("");
        getContentPane().add(label_1);
        
        label_2 = new JLabel("");
        getContentPane().add(label_2);
        
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("", "[][]", "[][][][][][][][]"));
        jLabel7 = new javax.swing.JLabel();
        panel.add(jLabel7, "cell 0 0");
        
                jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                jLabel7.setText("Series being crossdated:");
                lblCrossdateName = new javax.swing.JLabel();
                panel.add(lblCrossdateName, "cell 1 0");
                
                        lblCrossdateName.setText("C-XXX-XX-X-X-X (1234-2345)");
                jLabel6 = new javax.swing.JLabel();
                panel.add(jLabel6, "cell 0 1");
                
                        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                        jLabel6.setText("New date range:");
                        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                        lblNewDateRange = new javax.swing.JLabel();
                        panel.add(lblNewDateRange, "cell 1 1");
                        
                                lblNewDateRange.setText("2345-5678");
                        jLabel5 = new javax.swing.JLabel();
                        panel.add(jLabel5, "flowy,cell 0 2");
                        
                                jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                                jLabel5.setText("Master series:");
                                lblMasterSampleName = new javax.swing.JLabel();
                                panel.add(lblMasterSampleName, "cell 1 2");
                                
                                        lblMasterSampleName.setText("C-XXX-XX-X-X-X (9876-5432)");
                                        jSeparator1 = new javax.swing.JSeparator();
                                        panel.add(jSeparator1, "cell 0 7");
                                        
                                                jSeparator1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                                                jLabel4 = new javax.swing.JLabel();
                                                panel.add(jLabel4, "cell 0 3");
                                                
                                                        jLabel4.setText("Series:");
                                                        txtNewCrossdateName = new javax.swing.JTextField();
                                                        panel.add(txtNewCrossdateName, "cell 1 3,growx");
                                                        
                                                                txtNewCrossdateName.setEnabled(false);
                                                                jLabel3 = new javax.swing.JLabel();
                                                                panel.add(jLabel3, "cell 0 4");
                                                                
                                                                        jLabel3.setText("Version:");
                                                                        txtVersion = new javax.swing.JTextField();
                                                                        panel.add(txtVersion, "cell 1 4,growx");
                                                                        jLabel2 = new javax.swing.JLabel();
                                                                        panel.add(jLabel2, "cell 0 5");
                                                                        
                                                                                jLabel2.setText("Certainty:");
                                                                                cboCertainty = new javax.swing.JComboBox();
                                                                                panel.add(cboCertainty, "cell 1 5");
                                                                                jLabel1 = new javax.swing.JLabel();
                                                                                panel.add(jLabel1, "cell 0 6");
                                                                                
                                                                                        jLabel1.setText("Justification:");
                                                                                        jScrollPane1 = new javax.swing.JScrollPane();
                                                                                        panel.add(jScrollPane1, "cell 1 6");
                                                                                        txtJustification = new javax.swing.JTextArea();
                                                                                        
                                                                                                txtJustification.setColumns(20);
                                                                                                txtJustification.setRows(5);
                                                                                                jScrollPane1.setViewportView(txtJustification);
        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(lblprefix);
        
        panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new MigLayout("", "[grow][54px][81px]", "[25px]"));
        btnOk = new javax.swing.JButton();
        panel_1.add(btnOk, "cell 1 0,alignx left,aligny top");
        
                btnOk.setText("OK");
                
                        btnCancel = new javax.swing.JButton();
                        panel_1.add(btnCancel, "cell 2 0,alignx left,aligny top");
                        
                                btnCancel.setText("Cancel");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CrossdateCommitDialog dialog = new CrossdateCommitDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnCancel;
    protected javax.swing.JButton btnOk;
    protected javax.swing.JComboBox cboCertainty;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel2;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JLabel jLabel4;
    protected javax.swing.JLabel jLabel5;
    protected javax.swing.JLabel jLabel6;
    protected javax.swing.JLabel jLabel7;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JSeparator jSeparator1;
    protected javax.swing.JLabel lblCrossdateName;
    protected javax.swing.JLabel lblMasterSampleName;
    protected javax.swing.JLabel lblNewDateRange;
    protected javax.swing.JLabel lblprefix;
    protected javax.swing.JTextArea txtJustification;
    protected javax.swing.JTextField txtNewCrossdateName;
    protected javax.swing.JTextField txtVersion;
    private JLabel label;
    private JLabel label_1;
    private JLabel label_2;
    private JPanel panel_1;
}
