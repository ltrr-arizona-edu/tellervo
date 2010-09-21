/**
 * Created at Aug 24, 2010, 1:32:02 PM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.tridas.schema.TridasElement;

import edu.cornell.dendro.corina.components.table.ComboBoxCellEditor;
import edu.cornell.dendro.corina.components.table.ControlledVocDictionaryEditor;
import edu.cornell.dendro.corina.components.table.DynamicJComboBox;
import edu.cornell.dendro.corina.components.table.IDynamicJComboBoxInterpreter;
import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;
import edu.cornell.dendro.corina.control.bulkImport.ImportSelectedEvent;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ElementModel;
import edu.cornell.dendro.corina.model.bulkImport.SampleModel;
import edu.cornell.dendro.corina.schema.WSISampleTypeDictionary;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer.Behavior;


/**
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class SampleView  extends AbstractBulkImportView{


	public SampleView(SampleModel argModel) {
		super(argModel);
	}

	/**
	 * @see edu.cornell.dendro.corina.view.bulkImport.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@Override
	protected void setupTableCells(JTable argTable) {
		argTable.setDefaultEditor(WSISampleTypeDictionary.class, new ControlledVocDictionaryEditor("sampleTypeDictionary"));
		argTable.setDefaultRenderer(WSISampleTypeDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
		

		ElementModel m = BulkImportModel.getInstance().getElementModel();
		DynamicJComboBox<TridasElement> box = new DynamicJComboBox<TridasElement>(m.getImportedList(), new IDynamicJComboBoxInterpreter<TridasElement>() {
			@Override
			public String getStringValue(TridasElement argComponent) {
				if(argComponent == null){
					return null;
				}
				return argComponent.getTitle();
			}
		});
		argTable.setDefaultEditor(TridasElement.class, new ComboBoxCellEditor(box));
		argTable.setDefaultRenderer(TridasElement.class, new DefaultTableCellRenderer(){
			/**
			 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
			 */
			@Override
			protected void setValue(Object argValue) {
				if(argValue == null){
					super.setValue(argValue);
					return;
				}
				TridasElement object = (TridasElement) argValue;
				super.setValue(object.getTitle());
			}
		});
	}

	/**
	 * @see edu.cornell.dendro.corina.view.bulkImport.AbstractBulkImportView#importSelectedPressed()
	 */
	@Override
	protected void importSelectedPressed() {
		ImportSelectedEvent event = new ImportSelectedEvent(BulkImportController.IMPORT_SELECTED_SAMPLES);
		event.dispatch();
	}
	
}
