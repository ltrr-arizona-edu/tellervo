package edu.cornell.dendro.corina.util.labels.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lowagie.text.DocumentException;

import edu.cornell.dendro.corina.print.BoxLabel;
import edu.cornell.dendro.corina.util.labels.CornellSampleLabelPage;
import edu.cornell.dendro.corina.util.labels.PDFLabelMaker;

public class LabelPrinting extends LabelPrintingUI implements ActionListener{

	private static final long serialVersionUID = -5470599627007265539L;

	LabelPrinting.LabelType labeltype;
	
	LabelLayoutUI layoutPanel = new LabelLayoutUI();
	BoxLabelPrintingUI boxlabelpanel = new BoxLabelPrintingUI();
	SampleLabelPrintingUI samplelabelpanel = new SampleLabelPrintingUI();
	JDialog parent;
	
	public LabelPrinting(LabelPrinting.LabelType lt, JDialog p)
	{
		labeltype = lt;
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
		
		// Add Label panel to tab
		this.tabLabels.setLayout(new BorderLayout());
		if(labeltype==LabelPrinting.LabelType.BOX)
		{
			boxlabelpanel = new BoxLabelPrintingUI();
			this.tabLabels.add(boxlabelpanel, BorderLayout.CENTER);
		}
		else if (labeltype==LabelPrinting.LabelType.SAMPLE)
		{
			samplelabelpanel = new SampleLabelPrintingUI();
			this.tabLabels.add(samplelabelpanel, BorderLayout.CENTER);
		}

	}
	
	
	
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		
		if (evt.getSource() == btnCancel){
		
			this.parent.dispose();
		}
		
		
		if (evt.getSource() == btnPrint){
			
			if(labeltype == LabelType.BOX)
			{
				this.parent.setVisible(false);
				if(boxlabelpanel.selModel.getSize()>0)
				{
					System.out.println("Print box label");
					BoxLabel label = new BoxLabel(boxlabelpanel.selModel);
					
					label.getLabel(true);
					this.parent.dispose();
				}
			}
			else if(labeltype == LabelType.SAMPLE)
			{
				this.parent.setVisible(false);
				System.out.println("Print sample labels");				
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
		}
		
		
		if (evt.getSource() == btnPreview){

			if(labeltype == LabelType.BOX)
			{		
				if(boxlabelpanel.selModel.getSize()>0)
				{
					this.parent.setVisible(false);
					System.out.println("Preview box label");
					BoxLabel label = new BoxLabel(boxlabelpanel.selModel);
					
					label.getLabel(false);
					this.parent.dispose();
				}	
			}
			else if(labeltype == LabelType.SAMPLE)
			{
				if(samplelabelpanel.selModel.getSize()>0)
				{
					this.parent.setVisible(false);
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

			
			
		}
	}	
	
	
	
	public enum LabelType {
		BOX, SAMPLE
	}
	

}
