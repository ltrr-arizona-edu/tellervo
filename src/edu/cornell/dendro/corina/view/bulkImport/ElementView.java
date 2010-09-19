/**
 * Created on Aug 18, 2010, 1:10:49 PM
 */
package edu.cornell.dendro.corina.view.bulkImport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
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
import edu.cornell.dendro.corina.control.bulkImport.GPXBrowse;
import edu.cornell.dendro.corina.control.bulkImport.ImportSelectedEvent;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gis.GPXParser.GPXWaypoint;
import edu.cornell.dendro.corina.model.bulkImport.BulkImportModel;
import edu.cornell.dendro.corina.model.bulkImport.ElementModel;
import edu.cornell.dendro.corina.schema.WSIElementTypeDictionary;
import edu.cornell.dendro.corina.schema.WSITaxonDictionary;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer;
import edu.cornell.dendro.corina.tridasv2.ui.ControlledVocRenderer.Behavior;
import edu.cornell.dendro.corina.ui.Builder;

/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("serial")
public class ElementView extends AbstractBulkImportView{
	
	private JButton browseGPX;

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
		DynamicJComboBox<TridasObjectEx> box = new DynamicJComboBox<TridasObjectEx>(App.tridasObjects.getMutableObjectList(), new IDynamicJComboBoxInterpretter<TridasObjectEx>() {
			@Override
			public String getStringValue(TridasObjectEx argComponent) {
				if(argComponent == null){
					return "";
				}
				if(!argComponent.isTopLevelObject()){
					return null;
				}
				return argComponent.getLabCode();
			}
		});
		argTable.setDefaultEditor(TridasObject.class, new DefaultCellEditor(box));
		
		ElementModel model = BulkImportModel.getInstance().getElementModel();
		DynamicJComboBox<GPXWaypoint> waypointBox = new DynamicJComboBox<GPXWaypoint>(model.getWaypointList());
		argTable.setDefaultEditor(GPXWaypoint.class, new DefaultCellEditor(waypointBox));
	}
	
	@Override
	protected Box setupHeaderElements(JButton argAddRowButton, JButton argDeleteRowButton, 
			JButton argCopyRowButton, JButton argShowHideColumnButton){
		Box box = Box.createHorizontalBox();
		box.add(argAddRowButton);
		box.add(argDeleteRowButton);
		box.add(argCopyRowButton);
		box.add( Box.createHorizontalGlue());
		browseGPX = new JButton();
		browseGPX.setIcon(Builder.getIcon("satellite.png", 22));
		browseGPX.setToolTipText("Provide GPS data");
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
				ElementModel model = BulkImportModel.getInstance().getElementModel();
				GPXBrowse event = new GPXBrowse(model);
				event.dispatch();
				
			}
		});
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