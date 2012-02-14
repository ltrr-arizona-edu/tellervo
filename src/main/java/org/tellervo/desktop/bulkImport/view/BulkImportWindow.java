/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
/**
 * Created on Jul 22, 2010, 5:20:52 PM
 */
package org.tellervo.desktop.bulkImport.view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.tellervo.desktop.bulkImport.control.BulkImportController;
import org.tellervo.desktop.bulkImport.model.BulkImportModel;
import org.tellervo.desktop.model.CorinaModelLocator;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

import com.dmurph.mvc.MVCEvent;


/**
 * @author Daniel Murphy
 *
 */
public class BulkImportWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabs;
	private ObjectView objects;
	private ElementView elements;
	private SampleView samples;
	
	public BulkImportWindow() {
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
		pack();
		this.setLocationRelativeTo(null);
	}
	
	public void initComponents() {
		tabs = new JTabbedPane();
		
		objects = new ObjectView(BulkImportModel.getInstance().getObjectModel());
		elements = new ElementView(BulkImportModel.getInstance().getElementModel());
		samples = new SampleView(BulkImportModel.getInstance().getSampleModel());
		add(tabs,"Center");
		tabs.addTab(I18n.getText("tridas.object")+"s", Builder.getIcon("object.png", 22), objects);
		tabs.addTab(I18n.getText("tridas.element")+"s", Builder.getIcon("element.png", 22), elements);
		tabs.addTab(I18n.getText("tridas.sample")+"s", Builder.getIcon("sample.png", 22), samples);
		
		setIconImage(Builder.getApplicationIcon());
	}
	
	public void linkModel() {

	}
	
	public void addListeners() {

	}
	
	public void populateLocale() {
		this.setTitle(I18n.getText("menus.file.bulkimport"));
	}
	
	public static void main()	
	{
		CorinaModelLocator.getInstance();
		MVCEvent event = new MVCEvent(BulkImportController.DISPLAY_BULK_IMPORT);
		event.dispatch();
	}
}
