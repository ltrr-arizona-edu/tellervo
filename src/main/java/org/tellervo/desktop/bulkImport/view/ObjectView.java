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
 * Created on Jul 22, 2010, 2:15:56 AM
 */
package org.tellervo.desktop.bulkImport.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.tellervo.desktop.bulkImport.control.BulkImportController;
import org.tellervo.desktop.bulkImport.control.ColumnChooserController;
import org.tellervo.desktop.bulkImport.control.ColumnsModifiedEvent;
import org.tellervo.desktop.bulkImport.control.GPXBrowse;
import org.tellervo.desktop.bulkImport.control.ImportSelectedEvent;
import org.tellervo.desktop.bulkImport.model.BulkImportModel;
import org.tellervo.desktop.bulkImport.model.ObjectModel;
import org.tellervo.desktop.components.table.ComboBoxCellEditor;
import org.tellervo.desktop.components.table.ControlledVocDictionaryComboBox;
import org.tellervo.desktop.components.table.DynamicJComboBox;
import org.tellervo.desktop.components.table.DynamicKeySelectionManager;
import org.tellervo.desktop.components.table.TridasObjectExRenderer;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.schema.WSIObjectTypeDictionary;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer.Behavior;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;


/**
 * @author Daniel Murphy
 *
 */
public class ObjectView extends AbstractBulkImportView{
	private static final long serialVersionUID = 1L;
	
	private JButton browseGPX;
	
	public ObjectView(ObjectModel argModel){
		super(argModel);
	}
	
	/**
	 * @see org.tellervo.desktop.bulkImport.view.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@SuppressWarnings("serial")
	@Override
	protected void setupTableCells(JTable argTable) {
		argTable.setDefaultEditor(WSIObjectTypeDictionary.class, new ComboBoxCellEditor(new ControlledVocDictionaryComboBox("objectTypeDictionary")));
		argTable.setDefaultRenderer(WSIObjectTypeDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
		
		ObjectModel model = BulkImportModel.getInstance().getObjectModel();
		DynamicJComboBox<GPXWaypoint> waypointBox = new DynamicJComboBox<GPXWaypoint>(model.getWaypointList(),new Comparator<GPXWaypoint>() {
			/**
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(GPXWaypoint argO1, GPXWaypoint argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				return argO1.compareTo(argO2);
			}
		});
		argTable.setDefaultEditor(GPXWaypoint.class, new ComboBoxCellEditor(waypointBox));
		
		DynamicJComboBox<TridasObjectEx> box = new DynamicJComboBox<TridasObjectEx>(App.tridasObjects.getMutableObjectList(),
				new Comparator<TridasObjectEx>() {
			public int compare(TridasObjectEx argO1, TridasObjectEx argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				return argO1.getLabCode().compareToIgnoreCase(argO2.getLabCode());
			}
		});
		box.setRenderer(new TridasObjectExRenderer());
		box.setKeySelectionManager(new DynamicKeySelectionManager() {
			@Override
			public String convertToString(Object argO) {
				if(argO == null){
					return "";
				}
				TridasObjectEx o = (TridasObjectEx) argO;
				return o.getLabCode();
			}
		});
		
		argTable.setDefaultEditor(TridasObject.class, new ComboBoxCellEditor(box));
		argTable.setDefaultRenderer(TridasObject.class, new DefaultTableCellRenderer(){
			/**
			 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
			 */
			@Override
			protected void setValue(Object argValue) {
				if(argValue == null){
					super.setValue(argValue);
					return;
				}
				TridasObjectEx object = (TridasObjectEx) argValue;
				super.setValue(object.getLabCode());
			}
		});
	}
	
	/**
	 * @see org.tellervo.desktop.bulkImport.view.AbstractBulkImportView#importSelectedPressed()
	 */
	@Override
	protected void importSelectedPressed() {
		ImportSelectedEvent event = new ImportSelectedEvent(BulkImportController.IMPORT_SELECTED_OBJECTS);
		event.dispatch();
	}
	
	
	@Override
	/*protected Box setupToolbar(JButton argShowHideColumnButton, JButton selectAll, JButton selectNone)
	{
		Box box = Box.createVerticalBox();
		box.add(argShowHideColumnButton);
		box.add(selectAll);
		box.add(selectNone);
		browseGPX = new JButton();
		browseGPX.setIcon(Builder.getIcon("satellite.png", 22));
		browseGPX.setToolTipText("Provide GPS data");
		box.add(browseGPX);
		return box;
	}*/
	
	protected Box setupHeaderElements(JButton argAddRowButton, JButton argDeleteRowButton, 
				JButton argCopyRow, JButton argShowHideColumnButton){
		Box box = Box.createHorizontalBox();
		box.add(argAddRowButton);
		box.add(argDeleteRowButton);
		box.add(argCopyRow);
		box.add( Box.createHorizontalGlue());
		browseGPX = new JButton();
		browseGPX.setIcon(Builder.getIcon("satellite.png", 22));
		browseGPX.setToolTipText(I18n.getText("bulkimport.browseGPXFile"));
		box.add(browseGPX);
		box.add(argShowHideColumnButton);
		
		return box;
	}
	
	@Override
	protected void addListeners() {
		super.addListeners();
		final JPanel parent = (JPanel) this;
		
		browseGPX.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ObjectModel model = BulkImportModel.getInstance().getObjectModel();
				GPXBrowse event = new GPXBrowse(model, parent);
				event.dispatch();
				
				// Show waypoint column
				//ColumnsModifiedEvent ev = new ColumnsModifiedEvent(ColumnChooserController.COLUMN_ADDED, "Waypoint", model.getColumnModel());
				//ev.dispatch();
			}
		});
	}
}