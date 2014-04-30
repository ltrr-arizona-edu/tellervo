/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.util.labels.ui;

import javax.swing.JDialog;

import org.tellervo.desktop.ui.Builder;


public class LabelPrintingDialog extends JDialog{

	private static final long serialVersionUID = 1428573868641877953L;
	private PrintSettings lp;
		
    public LabelPrintingDialog(java.awt.Frame parent, boolean modal, PrintSettings.PrintType lt) {
        super(parent, modal);
        lp = new PrintSettings(lt, this);
        
        this.setTitle("Print " + lt.toString().toLowerCase() );
        setIconImage(Builder.getApplicationIcon());
		this.setContentPane(lp);
		this.pack();
    }
		
	public static void boxLabelDialog()
	{
		LabelPrintingDialog.main(PrintSettings.PrintType.BOX_WITH_CONTENTS);
	}
	
	public static void boxBasicLabelDialog()
	{
		LabelPrintingDialog.main(PrintSettings.PrintType.BOX_BASIC);
	}
	
	public static void sampleLabelDialog()
	{
		LabelPrintingDialog.main(PrintSettings.PrintType.SAMPLE);
	}
	
	public static void proSheetPrintingDialog()
	{
		LabelPrintingDialog.main(PrintSettings.PrintType.PROSHEET);
		
	}
	
    public static void main(final PrintSettings.PrintType lt) {
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
