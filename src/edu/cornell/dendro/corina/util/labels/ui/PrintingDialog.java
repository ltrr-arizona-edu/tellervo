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

import edu.cornell.dendro.corina.admin.UserGroupAdminOld;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.platform.Platform;

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;

public class PrintingDialog extends JDialog{

	private static final long serialVersionUID = 1428573868641877953L;
	private JFrame dialog;
	private PrintSettings lp;
		
    public PrintingDialog(java.awt.Frame parent, boolean modal, PrintSettings.PrintType lt) {
        super(parent, modal);
        lp = new PrintSettings(lt, this);
        
        this.setTitle("Print " + lt.toString().toLowerCase() );
        setIconImage(Builder.getApplicationIcon());
		this.setContentPane(lp);
		this.pack();
    }
		
	public static void boxLabelDialog()
	{
		PrintingDialog.main(PrintSettings.PrintType.BOX);
	}
	
	public static void sampleLabelDialog()
	{
		PrintingDialog.main(PrintSettings.PrintType.SAMPLE);
	}
	
	public static void proSheetPrintingDialog()
	{
		PrintingDialog.main(PrintSettings.PrintType.PROSHEET);
		
	}
	
    public static void main(final PrintSettings.PrintType lt) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	PrintingDialog dialog = new PrintingDialog(null, true, lt);
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
