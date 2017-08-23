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
package org.tellervo.desktop.bulkdataentry.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.table.TableColumnExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tellervo.desktop.bulkdataentry.control.GPXBrowse;
import org.tellervo.desktop.bulkdataentry.control.ImportSelectedEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromDatabaseEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromGeonamesEvent;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ImportStatus;
import org.tellervo.desktop.bulkdataentry.model.ObjectModel;
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
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.tridasv2.ui.BooleanCellRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer.Behavior;
import org.tellervo.desktop.tridasv2.ui.TridasFileArrayRenderer;
import org.tellervo.desktop.tridasv2.ui.TridasProjectRenderer;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.schema.WSIObjectTypeDictionary;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.MVCArrayList;

import edu.emory.mathcs.backport.java.util.Arrays;


/**
 * @author Daniel Murphy
 *
 */
public class ObjectView extends AbstractBulkImportView{
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(ObjectView.class);

	private JButton browseGPX;
	
	public ObjectView(ObjectModel argModel){
		super(argModel);
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@SuppressWarnings("serial")
	@Override
	protected void setupTableCells(JTable argTable) {
		
		argTable.setDefaultEditor(String.class, new StringCellEditor());
		argTable.setDefaultEditor(WSIObjectTypeDictionary.class, new ComboBoxCellEditor(new ControlledVocDictionaryComboBox("objectTypeDictionary")));
		argTable.setDefaultRenderer(WSIObjectTypeDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
				
		argTable.setDefaultEditor(NormalTridasLocationType.class, new ComboBoxCellEditor(new LocationTypeComboBox()));
		argTable.setDefaultRenderer(NormalTridasLocationType.class, new LocationTypeRenderer());
		
		argTable.setDefaultEditor(TridasFileList.class, new TridasFileListEditor(new JTextField()));
		argTable.setDefaultRenderer(TridasFileList.class, new TridasFileArrayRenderer());
		
		//argTable.setDefaultEditor(TridasProject.class, new ComboBoxCellEditor(new ControlledVocDictionaryComboBox("projectDictionary")));
		argTable.setDefaultRenderer(TridasProject.class, new TridasProjectRenderer());
		
		argTable.setDefaultRenderer(ImportStatus.class, new ImportStatusRenderer());
		
		
		DynamicJComboBox<TridasProject> projectCombo = new DynamicJComboBox<TridasProject>(App.tridasProjects.getMutableObjectList(),new Comparator<TridasProject>() {
			/**
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(TridasProject argO1, TridasProject argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				return argO1.getTitle().compareTo(argO2.getTitle());
			}
		});
		
		projectCombo.setRenderer(new TridasProjectRenderer());
		argTable.setDefaultEditor(TridasProject.class, new ComboBoxCellEditor(projectCombo));
		
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
		
		MVCArrayList<TridasObjectEx> objlist = App.tridasObjects.getMutableObjectList();
		MVCArrayList<TridasObjectOrPlaceholder> toph = new MVCArrayList<TridasObjectOrPlaceholder>();
		for(TridasObjectEx o : objlist)
		{
			toph.add(new TridasObjectOrPlaceholder(o));
		}
		
		DynamicJComboBox<TridasObjectOrPlaceholder> box = new DynamicJComboBox<TridasObjectOrPlaceholder>(toph,
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
		box.setKeySelectionManager(new DynamicKeySelectionManager() {
			@Override
			public String convertToString(Object argO) {
				if(argO == null){
					return "";
				}
				TridasObjectOrPlaceholder o = (TridasObjectOrPlaceholder) argO;
				return o.getCode();
			}
		});

		box.setRenderer(new TridasObjectExRenderer());


		argTable.setDefaultEditor(TridasObject.class, new ComboBoxCellEditor(box));
		box.setEditable(true);	
		argTable.setDefaultEditor(TridasObjectOrPlaceholder.class, new ComboBoxCellEditor(box));
				
		
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView#importSelectedPressed()
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
	
	protected JToolBar setupToolbar(JButton argCopyButton, JButton argPasteButton, JButton argPasteAppendButton, JButton argAddRowButton, JButton argDeleteRowButton, 
				JButton argCopyRow, JButton argShowHideColumnButton, JButton argPopulateFromDB, JButton argPopulateFromGeonames, JButton argDeleteODKInstances, JButton argODKImport){
		/*Box box = Box.createHorizontalBox();
		box.add(argAddRowButton);
		box.add(argDeleteRowButton);
		box.add(argCopyRow);
		box.add( Box.createHorizontalGlue());
			return box;
		*/
		
		
		 JToolBar toolbar = new JToolBar();
		 
		 toolbar.add(argCopyButton);
		 toolbar.add(argPasteButton);
		 toolbar.add(argPasteAppendButton);
		 toolbar.add(selectAll);
		 toolbar.add(selectNone);
		 toolbar.add(argAddRowButton);
		 toolbar.add(argDeleteRowButton);
		 toolbar.add(argCopyRow);
  		 
		 toolbar.add(argODKImport);
		 toolbar.add(btnDeleteODKInstances);
			
		 toolbar.add(argPopulateFromDB);
		 toolbar.add(argPopulateFromGeonames);
		

		 
		 
		 
		browseGPX = new JButton();
		browseGPX.setIcon(Builder.getIcon("satellite.png", 22));
		browseGPX.setToolTipText(I18n.getText("bulkimport.browseGPXFile"));
		toolbar.add(browseGPX);

		
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
				ObjectModel model = BulkImportModel.getInstance().getObjectModel();
				GPXBrowse event = new GPXBrowse(model, parent);
				event.dispatch();
				
				// Show waypoint column
				//ColumnsModifiedEvent ev = new ColumnsModifiedEvent(ColumnChooserController.COLUMN_ADDED, "Waypoint", model.getColumnModel());
				//ev.dispatch();
			}
		});
						
	}

	@Override
	protected void populateFromDatabase() {
		ObjectModel model = BulkImportModel.getInstance().getObjectModel();
		PopulateFromDatabaseEvent event = new PopulateFromDatabaseEvent(model);
		
		event.dispatch();
		
	}

	@Override
	protected void populateFromGeonames() {
		ObjectModel model = BulkImportModel.getInstance().getObjectModel();
		PopulateFromGeonamesEvent event = new PopulateFromGeonamesEvent(model);
		
		event.dispatch();
		
	}

	@Override
	protected void saveColumnOrderToPrefs() {

			//log.debug("Saving column order to prefs");
			ArrayList<String> defaults = new ArrayList<String>();
					
			for(int i=0; i<table.getColumnCount(false); i++)
			{
				String s = (String) table.getColumnExt(i).getHeaderValue();
				//log.debug(" - "+s);
				defaults.add(s);
			}
			
			App.prefs.setArrayListPref(PrefKey.OBJECT_FIELD_VISIBILITY_ARRAY, defaults);
	}

	@Override
	protected void restoreColumnOrderFromPrefs() {
				
		ArrayList<String> prefs = App.prefs.getArrayListPref(PrefKey.OBJECT_FIELD_VISIBILITY_ARRAY, null);
		
		
		if(prefs==null){
			log.info("No prefs set for order of object columns, so using default order");
			prefs = new ArrayList<String>();
			prefs.add("Project");
			prefs.add("Object Code");
			prefs.add("Title");
			prefs.add("Type");
		}
		else
		{
			List<String> all = Arrays.asList(SingleObjectModel.TABLE_PROPERTIES);
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
		//log.debug("Saving column widths to preferences");
		
		//this.saveColumnOrderToPrefs();
		
		ArrayList<String> widths = new ArrayList<String>();
		
		for(int i=0; i<table.getColumnCount(); i++)
		{
			TableColumnExt col = table.getColumnExt(i);
			widths.add(col.getWidth()+"");
			
		}
		
		App.prefs.setArrayListPref(PrefKey.OBJECT_FIELD_COLUMN_WIDTH_ARRAY, widths);

		
	}

	@Override
	protected void restoreColumnWidthsFromPrefs() {
		
		//log.debug("Restoring column widths from preferences");
		
		table.setHorizontalScrollEnabled(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		ArrayList<String> widths = App.prefs.getArrayListPref(PrefKey.OBJECT_FIELD_COLUMN_WIDTH_ARRAY, null);
		
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
				
				//log.debug("Setting column "+i+" to width "+value);
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
		unhideableColumns.add("Object Code");
		unhideableColumns.add("Type");
		unhideableColumns.add("Title");
		
		
		
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
		
	}
}
