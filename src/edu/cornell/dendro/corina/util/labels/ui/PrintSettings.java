package edu.cornell.dendro.corina.util.labels.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.tridas.schema.TridasObject;

import com.lowagie.text.DocumentException;

import edu.cornell.dendro.corina.print.BoxLabel;
import edu.cornell.dendro.corina.print.ProSheet;
import edu.cornell.dendro.corina.util.labels.CornellSampleLabelPage;
import edu.cornell.dendro.corina.util.labels.PDFLabelMaker;

public class PrintSettings extends PrintSettingsUI implements ActionListener{

	private static final long serialVersionUID = -5470599627007265539L;

	PrintSettings.PrintType printtype;
	
	LabelLayoutUI layoutPanel = new LabelLayoutUI();
	BoxLabelPrintingUI boxlabelpanel = new BoxLabelPrintingUI();
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
		if(printtype==PrintSettings.PrintType.BOX)
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

	}
	
	
	
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		
		if (evt.getSource() == btnCancel){
		
			this.parent.dispose();
		}
		
		
		if (evt.getSource() == btnPrint){
			
			this.parent.setVisible(false);
			
			if(printtype == PrintType.BOX)
			{		
				if(boxlabelpanel.selModel.getSize()>0)
				{
					System.out.println("Print box label");
					BoxLabel label = new BoxLabel(boxlabelpanel.selModel);
					
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
			
		}
		
		
		if (evt.getSource() == btnPreview){

			if(printtype == PrintType.BOX)
			{	
				this.parent.setVisible(false);
				if(boxlabelpanel.selModel.getSize()>0)
				{
					
					System.out.println("Preview box label");
					BoxLabel label = new BoxLabel(boxlabelpanel.selModel);
					
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

			
			
		}
	}	
	
	
	
	public enum PrintType {
		BOX, SAMPLE, PROSHEET
	}
	

}
