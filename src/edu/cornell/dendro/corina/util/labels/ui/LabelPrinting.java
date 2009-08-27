package edu.cornell.dendro.corina.util.labels.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import edu.cornell.dendro.corina.print.BoxLabel;

public class LabelPrinting extends LabelPrintingUI implements ActionListener{

	private static final long serialVersionUID = -5470599627007265539L;

	LabelPrinting.LabelType labeltype;
	
	LabelLayoutUI layoutPanel = new LabelLayoutUI();
	BoxLabelPrintingUI boxlabelpanel = new BoxLabelPrintingUI();
	SampleLabelPrintingUI samplelabelpanel = new SampleLabelPrintingUI();
	
	
	public LabelPrinting(LabelPrinting.LabelType lt)
	{
		labeltype = lt;
		setupDialog();
		btnPrint.addActionListener(this);
		btnPreview.addActionListener(this);

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
		
		}
		
		
		if (evt.getSource() == btnPrint){
			
			if(labeltype == LabelType.BOX)
			{
				if(boxlabelpanel.selModel.getSize()>0)
				{
					System.out.println("Print box label");
					BoxLabel label = new BoxLabel(boxlabelpanel.selModel);
					
					label.getLabel(true);
				}
			}
			else if(labeltype == LabelType.SAMPLE)
			{

			}
		}
		
		
		if (evt.getSource() == btnPreview){

			if(labeltype == LabelType.BOX)
			{			
				if(boxlabelpanel.selModel.getSize()>0)
				{
					System.out.println("Preview box label");
					BoxLabel label = new BoxLabel(boxlabelpanel.selModel);
					
					label.getLabel(false);
				}	
			}
			else if(labeltype == LabelType.SAMPLE)
			{

			}			

			
			
		}
	}	
	
	
	
	public enum LabelType {
		BOX, SAMPLE
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
