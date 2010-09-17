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

import edu.cornell.dendro.corina.components.table.ControlledVocDictionaryEditor;
import edu.cornell.dendro.corina.components.table.DynamicJComboBox;
import edu.cornell.dendro.corina.components.table.IDynamicJComboBoxInterpretter;
import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;
import edu.cornell.dendro.corina.control.bulkImport.GPXBrowse;
import edu.cornell.dendro.corina.control.bulkImport.ImportSelectedEvent;
import edu.cornell.dendro.corina.gis.GPXParser.GPXWaypoint;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ObjectModel;
import edu.cornell.dendro.corina.schema.WSIObjectTypeDictionary;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer.Behavior;
import edu.cornell.dendro.corina.ui.Builder;

/**
 * @author Daniel Murphy
 *
 */
public class ObjectView extends AbstractBulkImportView{
	private static final long serialVersionUID = 1L;
	protected JButton browseGPX;
	
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
		argTable.setDefaultEditor(GPXWaypoint.class, new DefaultCellEditor(new DynamicJComboBox(model.getWaypointList(), new IDynamicJComboBoxInterpretter() {
			
			@Override
			public String getStringValue(Object argComponent) {
	
				return argComponent.toString();
			}
		})));
		
		
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
	
	protected Box setupHeaderElements(JButton argAddRowButton, JButton argDeleteRowButton, JButton argShowHideColumnButton, 
			JButton argSelectAll, JButton argSelectNone) {
	
		Box box = Box.createHorizontalBox();
		box.add(argAddRowButton);
		box.add(argDeleteRowButton);
		box.add( Box.createHorizontalGlue());
		browseGPX = new JButton();
		browseGPX.setIcon(Builder.getIcon("satellite.png", 22));
		browseGPX.setToolTipText("Provide GPS data");
		box.add(browseGPX);
		box.add(argShowHideColumnButton);
		box.add(argSelectAll);
		box.add(argSelectNone);
		return box;
		
	};
	
	@Override
	protected void addListeners() {
		super.addListeners();
		
		browseGPX.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GPXBrowse event = new GPXBrowse(BulkImportModel.getInstance().getObjectModel());
				event.dispatch();
				
			}
		});
	}
}