package edu.cornell.dendro.corina.util.labels.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.AdminPanel;
import edu.cornell.dendro.corina.platform.Platform;

import edu.cornell.dendro.corina.util.Center;

public class LabelPrintingDialog extends JDialog{

	private static final long serialVersionUID = 1428573868641877953L;
	private JFrame dialog;
	private LabelPrinting lp;
		
    public LabelPrintingDialog(java.awt.Frame parent, boolean modal, LabelPrinting.LabelType lt) {
        super(parent, modal);
        lp = new LabelPrinting(lt, this);
        
        this.setTitle("Print " + lt.toString().toLowerCase() + " labels" );
		this.setContentPane(lp);
		this.pack();
    }
		
	public static void boxLabelDialog()
	{
		LabelPrintingDialog.main(LabelPrinting.LabelType.BOX);
	}
	
	public static void sampleLabelDialog()
	{
		LabelPrintingDialog.main(LabelPrinting.LabelType.SAMPLE);
	}
	
    public static void main(final LabelPrinting.LabelType lt) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	LabelPrintingDialog dialog = new LabelPrintingDialog(null, true, lt);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {

                    }
                });
                
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
               
            }
        });
    }

}
