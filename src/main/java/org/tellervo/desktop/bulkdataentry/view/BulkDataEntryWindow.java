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
package org.tellervo.desktop.bulkdataentry.view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.model.TellervoModelLocator;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

import com.dmurph.mvc.MVCEvent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import net.miginfocom.swing.MigLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;


/**
 * @author Daniel Murphy
 *
 */
public class BulkDataEntryWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabs;
	private ObjectView objects;
	private ElementView elements;
	private SampleView samples;
	private JPanel panelTabs;
	private JPanel panelInfo;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnEdit;
	private JMenu mnTools;
	private JMenu mnView;
	
	public BulkDataEntryWindow() {
		initComponents();
		linkModel();
		addListeners();
		populateLocale();
		pack();
		this.setMinimumSize(new Dimension(640,480));
		setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH );
				
	}
	
	public void initComponents() {
		
		panelTabs = new JPanel();
		getContentPane().add(panelTabs, BorderLayout.CENTER);
		panelTabs.setLayout(new MigLayout("", "[240px,grow,fill]", "[112px,grow,fill]"));
		tabs = new JTabbedPane();
		tabs.setTabPlacement(JTabbedPane.BOTTOM);
		panelTabs.add(tabs, "cell 0 0,alignx left,aligny top");
		
		objects = new ObjectView(BulkImportModel.getInstance().getObjectModel());
		elements = new ElementView(BulkImportModel.getInstance().getElementModel());
		samples = new SampleView(BulkImportModel.getInstance().getSampleModel(), BulkImportModel.getInstance().getElementModel());
		tabs.addTab(I18n.getText("tridas.object")+"s", Builder.getIcon("object.png", 22), objects);
		tabs.addTab(I18n.getText("tridas.element")+"s", Builder.getIcon("element.png", 22), elements);
		tabs.addTab(I18n.getText("tridas.sample")+"s", Builder.getIcon("sample.png", 22), samples);
		
		panelInfo = new JPanel();
		getContentPane().add(panelInfo, BorderLayout.NORTH);
		panelInfo.setLayout(new MigLayout("", "[]", "[]"));
		
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
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
				TellervoModelLocator.getInstance();
				MVCEvent event = new MVCEvent(BulkImportController.DISPLAY_BULK_IMPORT);
				event.dispatch();
	        }
		});
	}
}
