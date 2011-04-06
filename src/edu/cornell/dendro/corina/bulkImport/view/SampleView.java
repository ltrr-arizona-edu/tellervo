/**
 * Created at Aug 24, 2010, 1:32:02 PM
 */
package edu.cornell.dendro.corina.bulkImport.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JTable;

import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.gui.combo.MVCJComboBox;

import edu.cornell.dendro.corina.bulkImport.control.BulkImportController;
import edu.cornell.dendro.corina.bulkImport.control.ImportSelectedEvent;
import edu.cornell.dendro.corina.bulkImport.control.PrintSampleBarcodesEvent;
import edu.cornell.dendro.corina.bulkImport.model.BulkImportModel;
import edu.cornell.dendro.corina.bulkImport.model.SampleModel;
import edu.cornell.dendro.corina.components.table.ComboBoxCellEditor;
import edu.cornell.dendro.corina.components.table.ControlledVocDictionaryComboBox;
import edu.cornell.dendro.corina.components.table.DynamicJComboBox;
import edu.cornell.dendro.corina.components.table.DynamicKeySelectionManager;
import edu.cornell.dendro.corina.components.table.TridasElementRenderer;
import edu.cornell.dendro.corina.components.table.TridasObjectExRenderer;
import edu.cornell.dendro.corina.components.table.WSIBoxRenderer;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.WSIBox;
import edu.cornell.dendro.corina.schema.WSIBoxDictionary;
import edu.cornell.dendro.corina.schema.WSISampleTypeDictionary;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer.Behavior;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;


/**
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class SampleView  extends AbstractBulkImportView{

	private JButton printBarcodes;
	
	public SampleView(SampleModel argModel) {
		super(argModel);
	}

	/**
	 * @see edu.cornell.dendro.corina.bulkImport.view.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setupTableCells(JTable argTable) {
		argTable.setDefaultEditor(WSISampleTypeDictionary.class, new ComboBoxCellEditor(new ControlledVocDictionaryComboBox("sampleTypeDictionary")));
		argTable.setDefaultRenderer(WSISampleTypeDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
		

		MVCJComboBox<TridasElement> cboElement = new MVCJComboBox<TridasElement>(null, new Comparator<TridasElement>(){
			public int compare(TridasElement argO1, TridasElement argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				return argO1.getTitle().compareToIgnoreCase(argO2.getTitle());
			}
		});
		cboElement.setRenderer(new TridasElementRenderer());
		cboElement.setKeySelectionManager(new DynamicKeySelectionManager() {
			@Override
			public String convertToString(Object argO) {
				if(argO == null){
					return "";
				}
				return ((TridasElement)argO).getTitle();
			}
		});
		// specific editor for this, options have to be unique per column
		argTable.setDefaultEditor(TridasElement.class, new ChosenElementEditor(cboElement));
		argTable.setDefaultRenderer(TridasElement.class, new TridasElementRenderer());
		
		DynamicJComboBox<WSIBox> cboBox = new DynamicJComboBox<WSIBox>(Dictionary.getMutableDictionary("boxDictionary"), 
			new Comparator<WSIBox>() {
			/**
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(WSIBox argO1, WSIBox argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				return argO1.getTitle().compareToIgnoreCase(argO2.getTitle());
			}
		});
		cboBox.setRenderer(new WSIBoxRenderer());
		cboBox.setKeySelectionManager(new DynamicKeySelectionManager() {
			
			@Override
			public String convertToString(Object argO) {
				if(argO == null){
					return "";
				}
				return ((WSIBox)argO).getTitle();
			}
		});
		
		argTable.setDefaultEditor(WSIBoxDictionary.class, new ComboBoxCellEditor(cboBox));
		argTable.setDefaultRenderer(WSIBoxDictionary.class, new WSIBoxRenderer());
		
		
		MVCJComboBox<TridasObjectEx> box = new MVCJComboBox<TridasObjectEx>(App.tridasObjects.getMutableObjectList(),
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
		argTable.setDefaultRenderer(TridasObject.class, new TridasObjectExRenderer());
	}

	@Override
	protected Box setupHeaderElements(JButton argAddRowButton, JButton argDeleteRowButton, 
			JButton argCopyRow, JButton argShowHideColumnButton){
		Box box = Box.createHorizontalBox();
		box.add(argAddRowButton);
		box.add(argDeleteRowButton);
		box.add(argCopyRow);
		box.add( Box.createHorizontalGlue());
		printBarcodes = new JButton();
		printBarcodes.setIcon(Builder.getIcon("barcode.png", 22));
		printBarcodes.setToolTipText(I18n.getText("bulkimport.printBarcodes"));
		box.add(printBarcodes);
		box.add(argShowHideColumnButton);
		
		return box;
	}
	
	@Override
	protected void addListeners() {
		super.addListeners();
		
		printBarcodes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SampleModel model = BulkImportModel.getInstance().getSampleModel();
				PrintSampleBarcodesEvent event = new PrintSampleBarcodesEvent(model);
				event.dispatch();
			}
		});
	}
	
	
	/**
	 * @see edu.cornell.dendro.corina.bulkImport.view.AbstractBulkImportView#importSelectedPressed()
	 */
	@Override
	protected void importSelectedPressed() {
		ImportSelectedEvent event = new ImportSelectedEvent(BulkImportController.IMPORT_SELECTED_SAMPLES);
		event.dispatch();
	}
	
}
