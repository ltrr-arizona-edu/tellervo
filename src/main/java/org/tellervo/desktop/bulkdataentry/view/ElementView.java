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
 * Created on Aug 18, 2010, 1:10:49 PM
 */
package org.tellervo.desktop.bulkdataentry.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;

import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tellervo.desktop.bulkdataentry.control.ColumnChooserController;
import org.tellervo.desktop.bulkdataentry.control.ColumnsModifiedEvent;
import org.tellervo.desktop.bulkdataentry.control.GPXBrowse;
import org.tellervo.desktop.bulkdataentry.control.ImportSelectedEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromDatabaseEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromGeonamesEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromODKFileEvent;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.TridasFileList;
import org.tellervo.desktop.components.table.ComboBoxCellEditor;
import org.tellervo.desktop.components.table.ControlledVocDictionaryComboBox;
import org.tellervo.desktop.components.table.DynamicJComboBox;
import org.tellervo.desktop.components.table.DynamicKeySelectionManager;
import org.tellervo.desktop.components.table.LocationTypeComboBox;
import org.tellervo.desktop.components.table.LocationTypeRenderer;
import org.tellervo.desktop.components.table.StringCellEditor;
import org.tellervo.desktop.components.table.TridasFileListEditor;
import org.tellervo.desktop.components.table.TridasObjectExRenderer;
import org.tellervo.desktop.components.table.TridasShapeComboBox;
import org.tellervo.desktop.components.table.TridasShapeRenderer;
import org.tellervo.desktop.components.table.TridasUnitComboBox;
import org.tellervo.desktop.components.table.TridasUnitRenderer;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.desktop.tridasv2.ui.BooleanCellRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer;
import org.tellervo.desktop.tridasv2.ui.TridasFileArrayRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer.Behavior;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.schema.WSIElementTypeDictionary;
import org.tellervo.schema.WSITaxonDictionary;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasUnit;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.HashModel;


/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("serial")
public class ElementView extends AbstractBulkImportView{

	private JButton browseGPX;
	private JButton quickFill;
	private JButton browseODK;

	public ElementView(ElementModel argModel){
		super(argModel);
		table.getColumnModel().getColumn(1).setCellRenderer(new BooleanCellRenderer(true));

	}

	/**
	 * @see org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@Override
	protected void setupTableCells(JTable argTable) {
		argTable.setDefaultEditor(String.class, new StringCellEditor());
		argTable.setDefaultEditor(WSIElementTypeDictionary.class, new ComboBoxCellEditor(new ControlledVocDictionaryComboBox("elementTypeDictionary")));
		argTable.setDefaultRenderer(WSIElementTypeDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
		argTable.setDefaultEditor(TridasShape.class, new ComboBoxCellEditor(new TridasShapeComboBox()));
		argTable.setDefaultRenderer(TridasShape.class, new TridasShapeRenderer());
		
		argTable.setDefaultEditor(TridasFileList.class, new TridasFileListEditor(new JTextField()));
		argTable.setDefaultRenderer(TridasFileList.class, new TridasFileArrayRenderer());
		
		argTable.setDefaultEditor(TridasUnit.class, new ComboBoxCellEditor(new TridasUnitComboBox()));
		argTable.setDefaultRenderer(TridasUnit.class, new TridasUnitRenderer());
		argTable.setDefaultEditor(WSITaxonDictionary.class, new ComboBoxCellEditor(new ControlledVocDictionaryComboBox("taxonDictionary")));
		argTable.setDefaultRenderer(WSITaxonDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
		argTable.setDefaultEditor(NormalTridasLocationType.class, new ComboBoxCellEditor(new LocationTypeComboBox()));
		argTable.setDefaultRenderer(NormalTridasLocationType.class, new LocationTypeRenderer());
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

		box.setRenderer(new TridasObjectExRenderer());

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

		ElementModel model = BulkImportModel.getInstance().getElementModel();
		DynamicJComboBox<GPXWaypoint> waypointBox = new DynamicJComboBox<GPXWaypoint>(model.getWaypointList(), new Comparator<GPXWaypoint>() {
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
	}

	@Override
	protected JToolBar setupToolbar(JButton argCopyButton, JButton argPasteButton, JButton argPasteAppendButton, JButton argAddRowButton, JButton argDeleteRowButton, 
			JButton argCopyRowButton, JButton argShowHideColumnButton, JButton argPopulateFromDB, JButton argPopulateFromGeonames){


		JToolBar toolbar = new JToolBar();
		 toolbar.add(argCopyButton);
		 toolbar.add(argPasteButton);
		 toolbar.add(argPasteAppendButton);
		toolbar.add(selectAll);
		toolbar.add(selectNone);
		toolbar.add(argAddRowButton);
		toolbar.add(argDeleteRowButton);
		toolbar.add(argCopyRowButton);
		toolbar.add(argPopulateFromDB);
		toolbar.add(argPopulateFromGeonames);

		quickFill = new JButton();
		quickFill.setIcon(Builder.getIcon("quickfill.png", 22));
		quickFill.setToolTipText("Open quick fill dialog");


		toolbar.add(quickFill);


		browseGPX = new JButton();
		browseGPX.setIcon(Builder.getIcon("satellite.png", 22));
		browseGPX.setToolTipText(I18n.getText("bulkimport.browseGPXFile"));
		toolbar.add(browseGPX);
		
		browseODK = new JButton("");
		browseODK.setIcon(Builder.getIcon("odk.png", 22));
		browseODK.setToolTipText("ODK Import");
		toolbar.add(browseODK);
		
		
		toolbar.add(argShowHideColumnButton);

		return toolbar;
	}

	@Override
	protected void addListeners() {
		super.addListeners();
		final JPanel parent = (JPanel) this;


		browseGPX.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ElementModel model = BulkImportModel.getInstance().getElementModel();
				GPXBrowse event = new GPXBrowse(model, parent);
				event.dispatch();

				// Show waypoint column
				ColumnsModifiedEvent ev = new ColumnsModifiedEvent(ColumnChooserController.COLUMN_ADDED, "Waypoint", model.getColumnModel());
				ev.dispatch();

			}
		});

		browseODK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				HashModel model = BulkImportModel.getInstance().getElementModel();
				PopulateFromODKFileEvent event = new PopulateFromODKFileEvent(model);
				event.dispatch();
			}
			
			
			
		});
		
		final ElementView glue = this;
		quickFill.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				QuickEntryElement dialog = new QuickEntryElement(glue, model);

				dialog.setVisible(true);

			}



		});


	}

	/**
	 * @see org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView#importSelectedPressed()
	 */
	@Override
	protected void importSelectedPressed() {
		ImportSelectedEvent event = new ImportSelectedEvent(BulkImportController.IMPORT_SELECTED_ELEMENTS);
		event.dispatch();
	}

	@Override
	protected void populateFromDatabase() {
		ElementModel model = BulkImportModel.getInstance().getElementModel();
		PopulateFromDatabaseEvent event = new PopulateFromDatabaseEvent(model);

		event.dispatch();

	}

	@Override
	protected void populateFromGeonames() {
		ElementModel model = BulkImportModel.getInstance().getElementModel();
		PopulateFromGeonamesEvent event = new PopulateFromGeonamesEvent(model);
		
		event.dispatch();
		
	}
}
