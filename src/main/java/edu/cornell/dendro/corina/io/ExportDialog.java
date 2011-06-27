/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.io;

import javax.swing.JDialog;

import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;

public class ExportDialog extends JDialog {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ExportUI mainPanel;
	
	/**
	 Create and display a sample-export dialog.

	 @param samples the list of samples to export
	 @param parent window
	 */
	public ExportDialog(ElementList elements){
		//super(parent, I18n.getText("export"), true);
			
		setupGui(elements);
		setIconImage(Builder.getApplicationIcon());

	}

	public ExportDialog(Element element){
		ElementList elements = ElementList.singletonList(element);
		setupGui(elements);
	}
	
	public ExportDialog(Sample s){
		
		ElementList elements = ElementList.singletonList(new Element((BaseSample) s));
		setupGui(elements);
	}
	

	public void setupGui(ElementList elements)
	{
		
		mainPanel = new ExportUI(this, elements);
		this.setContentPane(mainPanel);
		
		this.setTitle(I18n.getText("export.dataFromDatabase"));
		
		this.setMinimumSize(new java.awt.Dimension(525, 270));

		
		
		this.pack();
		Center.center(this);
		
		
		this.setVisible(true);
	}


}
