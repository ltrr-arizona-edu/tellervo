package edu.cornell.dendro.corina.util.labels.ui;

import javax.swing.JDialog;

import edu.cornell.dendro.corina.ui.Builder;

public class PrintingDialog extends JDialog{

	private static final long serialVersionUID = 1428573868641877953L;
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
