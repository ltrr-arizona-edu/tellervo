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

	JFrame dialog;
	LabelPrinting.LabelType labeltype;
	
	LabelPrintingUI mainPanel = new LabelPrintingUI();
	LabelLayoutUI layoutPanel = new LabelLayoutUI();
	JPanel labelpanel;
		
    public LabelPrintingDialog(java.awt.Frame parent, boolean modal, LabelPrinting.LabelType lt) {
        super(parent, modal);
		labeltype = lt;	
		this.setupDialog();
    }
		
	public void setupDialog(){
				
		// Add layout panel to tab
		mainPanel.tabLayout.setLayout(new BorderLayout());
		mainPanel.tabLayout.add(layoutPanel, BorderLayout.CENTER);
		
		// Add Label panel to tab
		mainPanel.tabLabels.setLayout(new BorderLayout());
		if(labeltype==LabelPrinting.LabelType.BOX)
		{
			labelpanel = new BoxLabelPrintingUI();
		}
		else if (labeltype==LabelPrinting.LabelType.SAMPLE)
		{
			labelpanel = new SampleLabelPrintingUI();
		}
		mainPanel.tabLabels.add(labelpanel, BorderLayout.CENTER);
		this.setContentPane(mainPanel);
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
            	LabelPrintingDialog dialog = new LabelPrintingDialog(new javax.swing.JFrame(), true, lt);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {

                    }
                });

                dialog.setVisible(true);
               
            }
        });
    }



}
