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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.table.TableColumnExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tellervo.desktop.bulkdataentry.control.ColumnChooserController;
import org.tellervo.desktop.bulkdataentry.control.ColumnsModifiedEvent;
import org.tellervo.desktop.bulkdataentry.control.DeleteODKInstancesEvent;
import org.tellervo.desktop.bulkdataentry.control.GPXBrowse;
import org.tellervo.desktop.bulkdataentry.control.ImportSelectedEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromDatabaseEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromGeonamesEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromODKFileEvent;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.ObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SampleModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.bulkdataentry.model.TridasFileList;
import org.tellervo.desktop.bulkdataentry.model.TridasObjectOrPlaceholder;
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
import org.tellervo.desktop.prefs.Prefs.PrefKey;
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
import com.dmurph.mvc.model.MVCArrayList;

import edu.emory.mathcs.backport.java.util.Arrays;


/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("serial")
public class ElementView extends AbstractBulkImportView{

	private final static Logger log = LoggerFactory.getLogger(ElementView.class);

	private JButton browseGPX;
	private JButton quickFill;
    private MVCArrayList<TridasObjectEx> objlist;
    
    
	public ElementView(ElementModel argModel){
		super(argModel);
		
		table.getColumn("Imported").setCellRenderer(new BooleanCellRenderer(true));
		
	}

	/**
	 * @see org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@Override
	protected void setupTableCells(final JTable argTable) {
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
		/*DynamicJComboBox<TridasObjectEx> box = new DynamicJComboBox<TridasObjectEx>(App.tridasObjects.getMutableObjectList(),
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

		box.setRenderer(new TridasObjectExRenderer());*/
		
		objlist = App.tridasObjects.getMutableObjectList();
		
		objlist.addPropertyChangeListener(new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateObjectCombo(argTable);
				
			}
			
			
		});
		
		updateObjectCombo(argTable);
		
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

	
	private void updateObjectCombo(JTable argTable)
	{
		MVCArrayList<TridasObjectOrPlaceholder> toph = new MVCArrayList<TridasObjectOrPlaceholder>();
		for(TridasObjectEx o : objlist)
		{
			toph.add(new TridasObjectOrPlaceholder(o));
		}
		
		DynamicJComboBox<TridasObjectOrPlaceholder> combobox = new DynamicJComboBox<TridasObjectOrPlaceholder>(toph,
				new Comparator<TridasObjectOrPlaceholder>() {
			public int compare(TridasObjectOrPlaceholder argO1, TridasObjectOrPlaceholder argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				return argO1.getCode().compareToIgnoreCase(argO2.getCode());
			}
		});
		combobox.setKeySelectionManager(new DynamicKeySelectionManager() {
			@Override
			public String convertToString(Object argO) {
				if(argO == null){
					return "";
				}
				TridasObjectOrPlaceholder o = (TridasObjectOrPlaceholder) argO;
				return o.getCode();
			}
		});

		combobox.setRenderer(new TridasObjectExRenderer());


		argTable.setDefaultEditor(TridasObject.class, new ComboBoxCellEditor(combobox));
		combobox.setEditable(true);	
		argTable.setDefaultEditor(TridasObjectOrPlaceholder.class, new ComboBoxCellEditor(combobox));
		
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
		
		argTable.setDefaultRenderer(TridasObjectOrPlaceholder.class, new DefaultTableCellRenderer(){
			/**
			 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
			 */
			@Override
			protected void setValue(Object argValue) {
				if(argValue == null){
					super.setValue(argValue);
					return;
				}
				
				TridasObjectOrPlaceholder object = null;
				if(argValue instanceof TridasObjectOrPlaceholder)
				{
					object = (TridasObjectOrPlaceholder) argValue;
				}
				else if (argValue instanceof TridasObjectEx)
				{
					object = new TridasObjectOrPlaceholder((TridasObjectEx)argValue);;
				}
				else if (argValue instanceof String)
				{
					object = new TridasObjectOrPlaceholder((String)argValue);
				}

				super.setValue(object.getCode());
				if(object.getTridasObject()==null) {
					super.setForeground(Color.GRAY);
				}
				else
				{
					super.setForeground(Color.BLACK);
				}

			}
		});
	}
	
	@Override
	protected JToolBar setupToolbar(JButton argCopyButton, JButton argPasteButton, JButton argPasteAppendButton, JButton argAddRowButton, JButton argDeleteRowButton, 
			JButton argCopyRowButton, JButton argShowHideColumnButton, JButton argPopulateFromDB, JButton argPopulateFromGeonames, JButton argDeleteODKInstances, JButton argODKImport){


		JToolBar toolbar = new JToolBar();
		toolbar.add(argCopyButton);
		toolbar.add(argPasteButton);
		toolbar.add(argPasteAppendButton);
		toolbar.add(selectAll);
		toolbar.add(selectNone);
		toolbar.add(argAddRowButton);
		toolbar.add(argDeleteRowButton);
		toolbar.add(argCopyRowButton);
		
		toolbar.add(argODKImport);
	    toolbar.add(argDeleteODKInstances);

		toolbar.add(argPopulateFromDB);
		toolbar.add(argPopulateFromGeonames);

		browseGPX = new JButton();
		browseGPX.setIcon(Builder.getIcon("satellite.png", 22));
		browseGPX.setToolTipText(I18n.getText("bulkimport.browseGPXFile"));
		toolbar.add(browseGPX);


		quickFill = new JButton();
		quickFill.setIcon(Builder.getIcon("quickfill.png", 22));
		quickFill.setToolTipText("Open quick fill dialog");


		toolbar.add(quickFill);
		
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
	
	@Override
	protected void saveColumnOrderToPrefs() {

			log.debug("Saving column order to prefs");
			ArrayList<String> defaults = new ArrayList<String>();
					
			for(int i=0; i<table.getColumnCount(false); i++)
			{
				String s = (String) table.getColumnExt(i).getHeaderValue();
				log.debug(" - "+s);
				defaults.add(s);
			}
			
			App.prefs.setArrayListPref(PrefKey.ELEMENT_FIELD_VISIBILITY_ARRAY, defaults);
	}

	@Override
	protected void restoreColumnOrderFromPrefs() {
				
		ArrayList<String> prefs = App.prefs.getArrayListPref(PrefKey.ELEMENT_FIELD_VISIBILITY_ARRAY, null);
		
		if(prefs==null){
			log.info("No prefs set for order of element columns, so using default order");
			prefs = new ArrayList<String>();
			prefs.add("Object code");
			prefs.add("Element code");
			prefs.add("Type");
			prefs.add("Taxon");
		}
		else
		{
			List<String> all = Arrays.asList(SingleElementModel.TABLE_PROPERTIES);
			Iterator<String> iterator = prefs.iterator();
			while (iterator.hasNext()) {
				String item = iterator.next();
				if(!all.contains(item))
				{
					log.debug("Removing unknown field from list: "+item);
					iterator.remove();
				}
	
			}
		}


		restoreColumnOrderFromArray(prefs);

		
	}

	@Override
	protected void saveColumnWidthsToPrefs() {
		log.debug("Saving column widths to preferences");
		
		//this.saveColumnOrderToPrefs();
		
		ArrayList<String> widths = new ArrayList<String>();
		
		for(int i=0; i<table.getColumnCount(); i++)
		{
			TableColumnExt col = table.getColumnExt(i);
			widths.add(col.getWidth()+"");
			
		}
		
		App.prefs.setArrayListPref(PrefKey.SAMPLE_FIELD_COLUMN_WIDTH_ARRAY, widths);
		
	}

	@Override
	protected void restoreColumnWidthsFromPrefs() {
		
		log.debug("Restoring column widths from preferences");
		
		table.setHorizontalScrollEnabled(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		ArrayList<String> widths = App.prefs.getArrayListPref(PrefKey.ELEMENT_FIELD_COLUMN_WIDTH_ARRAY, null);
		
		if(widths==null) {
			
			table.packAll();
			return;
		}
		
		if(widths.size()!=table.getColumnCount())
		{
			return;
		}
		
		int i=0;
		for(String width : widths)
		{
			try{
				Integer value = Integer.valueOf(width);
				
				log.debug("Setting column "+i+" to width "+value);
				table.getColumnExt(i).setPreferredWidth(value);

				
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
				return;
			}
			
			i++;
		}
		
	}
	
	@Override
	public void setUnhideableColumns() {
		
		ArrayList<String> unhideableColumns = new ArrayList<String>();
		unhideableColumns.add("Selected");
		unhideableColumns.add("Imported");
		unhideableColumns.add("Object code");
		unhideableColumns.add("Element code");
		unhideableColumns.add("Type");
		unhideableColumns.add("Taxon");
		
		
		for(int i=0; i<table.getColumnCount(true); i++)
		{
			TableColumn col = table.getColumns(true).get(i);
			TableColumnExt colext = table.getColumnExt(col.getIdentifier());
			
			String colname = colext.getHeaderValue().toString();
			
			if(unhideableColumns.contains(colname))
			{
				colext.setHideable(false);
			}
			else
			{
				colext.setHideable(true);
			}
			
		}
		
		/*if(table.getColumnCount(true)>1)
		{
		
			// Ensure the first two columns cannot be hidden
			table.getColumnExt(0).setHideable(false);
			table.getColumnExt(1).setHideable(false);
			
			
		}*/
		
	}
}
