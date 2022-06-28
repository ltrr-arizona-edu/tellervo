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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JDialog;

import org.tellervo.desktop.print.BasicBoxLabel;
import org.tellervo.desktop.print.BoxBarcodeLabel;
import org.tellervo.desktop.print.CompleteBoxLabel;
import org.tellervo.desktop.print.ProSheet;
import org.tellervo.desktop.util.labels.PDFLabelMaker;
import org.tridas.schema.TridasObject;

import com.itextpdf.text.DocumentException;


public class PrintSettings extends PrintSettingsUI implements ActionListener{

	private static final long serialVersionUID = -5470599627007265539L;

	PrintSettings.PrintType printtype;
	
	LabelLayoutUI layoutPanel = new LabelLayoutUI();
	BoxLabelPrintingUI boxlabelpanel;
	SampleLabelPrintingUI samplelabelpanel = new SampleLabelPrintingUI();
	ProSheetPrintingUI prosheetpanel = new ProSheetPrintingUI();
	JDialog parent;
	
	public PrintSettings(PrintSettings.PrintType lt, JDialog p)
	{
		printtype = lt;
		parent = p;
		
		setupDialog();
		btnPrint.addActionListener(this);
		btnPreview.addActionListener(this);
		btnCancel.addActionListener(this);

	}
	
	public void setupDialog(){
		
		// Add layout panel to tab
		this.tabLayout.setLayout(new BorderLayout());
		this.tabLayout.add(layoutPanel, BorderLayout.CENTER);
		
		// Add content panel to tab
		this.tabContent.setLayout(new BorderLayout());
		if(printtype==PrintSettings.PrintType.BOX_WITH_CONTENTS || printtype==PrintSettings.PrintType.BOX_BASIC || printtype==PrintSettings.PrintType.BOX_BARCODE)
		{
			boxlabelpanel = new BoxLabelPrintingUI();
			this.tabContent.add(boxlabelpanel, BorderLayout.CENTER);
		}
		else if (printtype==PrintSettings.PrintType.SAMPLE)
		{
			samplelabelpanel = new SampleLabelPrintingUI();
			this.tabContent.add(samplelabelpanel, BorderLayout.CENTER);
		}
		else if (printtype==PrintSettings.PrintType.PROSHEET)
		{
			prosheetpanel = new ProSheetPrintingUI();
			this.tabContent.add(prosheetpanel, BorderLayout.CENTER);
			
		}
		else
		{
			System.err.println("Unknown label type");
		}

	}
	
	
	
	public void actionPerformed(ActionEvent evt) {
		
		if (evt.getSource() == btnCancel){
		
			this.parent.dispose();
		}
		
		
		if (evt.getSource() == btnPrint){
			
			this.parent.setVisible(false);
			
			if(printtype == PrintType.BOX_WITH_CONTENTS)
			{		
				if(boxlabelpanel.selModel.getSize()>0)
				{
					System.out.println("Print box label");
					CompleteBoxLabel label = new CompleteBoxLabel(boxlabelpanel.selModel);
					
					label.getLabel(true);
					this.parent.dispose();
				}
			}
			else if(printtype == PrintType.BOX_BASIC)
			{		
				if(boxlabelpanel.selModel.getSize()>0)
				{
					System.out.println("Print box label");
					BasicBoxLabel label = new BasicBoxLabel(boxlabelpanel.selModel);
					
					label.getLabel(true);
					this.parent.dispose();
				}
			}
			else if(printtype == PrintType.BOX_BARCODE)
			{		
				if(boxlabelpanel.selModel.getSize()>0)
				{
					System.out.println("Print box label");
					BoxBarcodeLabel label = new BoxBarcodeLabel(boxlabelpanel.selModel);
					
					label.getLabel(true);
					this.parent.dispose();
				}
			}
			else if(printtype == PrintType.SAMPLE)
			{			
				try {
					PDFLabelMaker.print(samplelabelpanel.selModel);
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(printtype == PrintType.PROSHEET)
			{
				
			}
			else 
			{
				System.err.println("Unknown label type");
			}
			
		}
		
		
		if (evt.getSource() == btnPreview){

			if(printtype == PrintType.BOX_WITH_CONTENTS)
			{	
				if(boxlabelpanel.selModel.getSize()>0)
				{
					
					System.out.println("Preview box label");
					CompleteBoxLabel label = new CompleteBoxLabel(boxlabelpanel.selModel);
					
					label.getLabel(false);
				}	
			}
			else if(printtype == PrintType.BOX_BASIC)
			{	
				if(boxlabelpanel.selModel.getSize()>0)
				{
					
					System.out.println("Preview box label");
					BasicBoxLabel label = new BasicBoxLabel(boxlabelpanel.selModel);
					
					label.getLabel(false);
				}	
			}
			else if(printtype == PrintType.BOX_BARCODE)
			{	
				if(boxlabelpanel.selModel.getSize()>0)
				{
					
					System.out.println("Preview box label");
					BoxBarcodeLabel label = new BoxBarcodeLabel(boxlabelpanel.selModel);
					
					label.getLabel(false);
				}	
			}			
			else if(printtype == PrintType.SAMPLE)
			{
				if(samplelabelpanel.selModel.getSize()>0)
				{
					System.out.println("Preview sample labels");
					try {
						PDFLabelMaker.preview(samplelabelpanel.selModel);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				}	
			}	
			else if (printtype == PrintType.PROSHEET)
			{
				if(prosheetpanel.selModel.getSize()>0)
				{
					System.out.println("Preview provenience sheet");
					
					ProSheet.viewReport((TridasObject) prosheetpanel.cboObject.getSelectedItem(), prosheetpanel.selModel);


				}					
				
			}
			else
			{
				System.out.println("Unknown label type");
			}

			
			
		}
	}	
	
	
	
	public enum PrintType {
		BOX_WITH_CONTENTS, SAMPLE, PROSHEET, BOX_BASIC, BOX_BARCODE
	}
	

}
