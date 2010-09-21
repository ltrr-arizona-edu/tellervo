/**
 * Created on Jul 22, 2010, 2:15:56 AM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.components.table.ComboBoxCellEditor;
import edu.cornell.dendro.corina.components.table.ControlledVocDictionaryEditor;
import edu.cornell.dendro.corina.components.table.DynamicJComboBox;
import edu.cornell.dendro.corina.components.table.IDynamicJComboBoxInterpreter;
import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;
import edu.cornell.dendro.corina.control.bulkImport.ColumnChooserController;
import edu.cornell.dendro.corina.control.bulkImport.ColumnsModifiedEvent;
import edu.cornell.dendro.corina.control.bulkImport.GPXBrowse;
import edu.cornell.dendro.corina.control.bulkImport.ImportSelectedEvent;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gis.GPXParser.GPXWaypoint;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectModel;
import edu.cornell.dendro.corina.schema.WSIObjectTypeDictionary;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer.Behavior;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

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
	 * @see edu.cornell.dendro.corina.view.bulkImport.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@Override
	protected void setupTableCells(JTable argTable) {
		argTable.setDefaultEditor(WSIObjectTypeDictionary.class, new ControlledVocDictionaryEditor("objectTypeDictionary"));
		argTable.setDefaultRenderer(WSIObjectTypeDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
		
		ObjectModel model = BulkImportModel.getInstance().getObjectModel();
		argTable.setDefaultEditor(GPXWaypoint.class, new ComboBoxCellEditor(new DynamicJComboBox<GPXWaypoint>(model.getWaypointList())));
		
		DynamicJComboBox<TridasObjectEx> box = new DynamicJComboBox<TridasObjectEx>(App.tridasObjects.getMutableObjectList(), new IDynamicJComboBoxInterpreter<TridasObjectEx>() {
			@Override
			public String getStringValue(TridasObjectEx argComponent) {
				if(argComponent == null){
					return "";
				}
				return argComponent.getLabCode();
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
	 * @see edu.cornell.dendro.corina.view.bulkImport.AbstractBulkImportView#importSelectedPressed()
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
		
		browseGPX.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ObjectModel model = BulkImportModel.getInstance().getObjectModel();
				GPXBrowse event = new GPXBrowse(model);
				event.dispatch();
				
				// Show waypoint column
				ColumnsModifiedEvent ev = new ColumnsModifiedEvent(ColumnChooserController.COLUMN_ADDED, "Waypoint", model.getColumnModel());
				ev.dispatch();
			}
		});
	}
}