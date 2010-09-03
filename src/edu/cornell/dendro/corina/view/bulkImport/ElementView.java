/**
 * Created on Aug 18, 2010, 1:10:49 PM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasUnit;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.components.table.ControlledVocDictionaryEditor;
import edu.cornell.dendro.corina.components.table.DynamicJComboBox;
import edu.cornell.dendro.corina.components.table.IDynamicJComboBoxInterpretter;
import edu.cornell.dendro.corina.components.table.TridasShapeEditor;
import edu.cornell.dendro.corina.components.table.TridasShapeRenderer;
import edu.cornell.dendro.corina.components.table.TridasUnitEditor;
import edu.cornell.dendro.corina.components.table.TridasUnitRenderer;
import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;
import edu.cornell.dendro.corina.control.bulkImport.ImportSelectedEvent;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.model.bulkImport.ElementModel;
import edu.cornell.dendro.corina.schema.WSIElementTypeDictionary;
import edu.cornell.dendro.corina.schema.WSITaxonDictionary;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer.Behavior;

/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("serial")
public class ElementView extends AbstractBulkImportView{
	
	public ElementView(ElementModel argModel){
		super(argModel);
	}

	/**
	 * @see edu.cornell.dendro.corina.view.bulkImport.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@Override
	protected void setupTableCells(JTable argTable) {
		argTable.setDefaultEditor(WSIElementTypeDictionary.class, new ControlledVocDictionaryEditor("elementTypeDictionary"));
		argTable.setDefaultRenderer(WSIElementTypeDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
		argTable.setDefaultEditor(TridasShape.class, new TridasShapeEditor());
		argTable.setDefaultRenderer(TridasShape.class, new TridasShapeRenderer());
		argTable.setDefaultEditor(TridasUnit.class, new TridasUnitEditor());
		argTable.setDefaultRenderer(TridasUnit.class, new TridasUnitRenderer());
		argTable.setDefaultEditor(WSITaxonDictionary.class, new ControlledVocDictionaryEditor("taxonDictionary"));
		argTable.setDefaultRenderer(WSITaxonDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
		
		// this combo box should update from mvc events
		DynamicJComboBox box = new DynamicJComboBox(App.tridasObjects.getMutableObjectList(), new IDynamicJComboBoxInterpretter() {
			@Override
			public String getStringValue(Object argComponent) {
				if(argComponent == null){
					return "";
				}
				TridasObjectEx o = (TridasObjectEx) argComponent;
				if(!o.isTopLevelObject()){
					return null;
				}
				return o.getLabCode();
			}
		});
		
		argTable.setDefaultEditor(TridasObject.class, new DefaultCellEditor(box));
	}
	
	/**
	 * @see edu.cornell.dendro.corina.view.bulkImport.AbstractBulkImportView#importSelectedPressed()
	 */
	@Override
	protected void importSelectedPressed() {
		ImportSelectedEvent event = new ImportSelectedEvent(BulkImportController.IMPORT_SELECTED_ELEMENTS);
		event.dispatch();
	}
}