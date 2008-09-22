package edu.cornell.dendro.corina.cross;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.menus.OpenRecent;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.ui.Alert;


/**
 *
 * @author  Lucas Madar
 */
public class CrossdateCommitDialog extends javax.swing.JDialog {
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
			new Bug(new Exception("Attempting to apply an index to a sample without a loader. Shouldn't be possible!"));
			return false;
		}
		
		
		// make a new 'crossdate' dummy sample for saving
		Sample tmp = new Sample();
		
		tmp.setMeta("name", txtNewCrossdateName.getText());
		tmp.setMeta("title", txtNewCrossdateName.getText()); // unnecessary, but consistent
		tmp.setRange(range);
		tmp.setMeta("::saveoperation", SampleType.CROSSDATE);

		// the new sample's parent is our secondary sample
		tmp.setMeta("::dbparent", secondary.getMeta("::dbrid"));		
		tmp.setMeta("::crossdatemaster", primary.getMeta("::dbrid"));
		tmp.setMeta("::crossdatecertainty", cboCertainty.getSelectedItem());
		tmp.setMeta("::crossdatejustification", txtJustification.getText());

		try {
			// here's where we do the "meat"
			if(loader.save(tmp)) {
				// put it in our menu
				OpenRecent.sampleOpened(tmp.getLoader());
				
				// get out of here! :)
				return true;
			}
		} catch (IOException ioe) {
			Alert.error("Could not create index", "Error: " + ioe.toString());
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
		
		txtNewCrossdateName.setText("Cross " + primary.getMeta("name").toString() + 
				"/" + secondary.getMeta("name").toString());
		
		txtNewCrossdateName.requestFocus();
	}

	/**
	 * A quick and dirty class to render stars in a combo box
	 */
	private class StarRenderer implements ListCellRenderer {
		private ImageIcon image;

		public StarRenderer() {
			ClassLoader cl = this.getClass().getClassLoader();
		    URL url = cl.getResource("edu/cornell/dendro/corina_resources/Icons/star-16x16.png");
		    
		    image = new ImageIcon(url);			
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
    @SuppressWarnings("unchecked")
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

        jLabel1.setText("Sample being crossdated:");

        jLabel2.setText("New date range:");

        jLabel3.setText("Master sample:");

        lblCrossdateName.setText("C-XXX-XX-XX-X (1234-2345)");

        lblNewDateRange.setText("(2345-3456)");

        lblMasterSampleName.setText("C-XXX-XX-XX-X (1432-5431)");

        jLabel7.setText("Crossdate name:");

        jLabel8.setText("Certainty:");

        cboCertainty.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Justification:");

        txtJustification.setColumns(20);
        txtJustification.setRows(5);
        jScrollPane1.setViewportView(txtJustification);

        btnOk.setText("OK");

        btnCancel.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNewDateRange)
                            .addComponent(lblCrossdateName)
                            .addComponent(lblMasterSampleName)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                            .addComponent(cboCertainty, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNewCrossdateName, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblCrossdateName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNewDateRange)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblMasterSampleName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNewCrossdateName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cboCertainty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOk))
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
