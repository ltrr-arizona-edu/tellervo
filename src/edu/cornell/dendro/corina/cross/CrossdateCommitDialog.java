package edu.cornell.dendro.corina.cross;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.SeriesLink;
import org.tridas.schema.TridasDatingReference;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasInterpretation;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.tridasv2.GenericFieldUtils;
import edu.cornell.dendro.corina.tridasv2.SeriesLinkUtil;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;
import edu.cornell.dendro.corina.wsi.corina.NewTridasIdentifier;

/**
 *
 * @author  Lucas Madar
 */
@SuppressWarnings("serial")
public class CrossdateCommitDialog extends JDialog {
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

    /** Creates new form CrossdateCommitDialog */
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
		
		//txtNewCrossdateName.setText("Cross " + primary.meta().getName() + 
		//		"/" + secondary.meta().getName());	
		// txtNewCrossdateName.requestFocus();
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblCrossdateName = new javax.swing.JLabel();
        lblNewDateRange = new javax.swing.JLabel();
        lblMasterSampleName = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        txtNewCrossdateName = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cboCertainty = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtJustification = new javax.swing.JTextArea();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Series being crossdated:");

        jLabel2.setText("New date range:");

        jLabel3.setText("Master series:");

        lblCrossdateName.setText("C-XXX-XX-XX-X (1234-2345)");

        lblNewDateRange.setText("(2345-3456)");

        lblMasterSampleName.setText("C-XXX-XX-XX-X (1432-5431)");

        jLabel7.setText("New series name:");

        jLabel8.setText("Certainty:");

        cboCertainty.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Justification:");

        txtJustification.setColumns(20);
        txtJustification.setRows(5);
        jScrollPane1.setViewportView(txtJustification);

        btnOk.setText("OK");

        btnCancel.setText("Cancel");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel3)
                            .add(jLabel1)
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblNewDateRange)
                            .add(lblCrossdateName)
                            .add(lblMasterSampleName)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel8)
                            .add(jLabel7)
                            .add(jLabel9))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                            .add(cboCertainty, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtNewCrossdateName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(btnOk)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(lblCrossdateName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblNewDateRange)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(lblMasterSampleName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(txtNewCrossdateName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(cboCertainty, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel9)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 23, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancel)
                    .add(btnOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>

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

    // Variables declaration - do not modify
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox cboCertainty;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblCrossdateName;
    private javax.swing.JLabel lblMasterSampleName;
    private javax.swing.JLabel lblNewDateRange;
    private javax.swing.JTextArea txtJustification;
    private javax.swing.JTextField txtNewCrossdateName;
    // End of variables declaration

}
