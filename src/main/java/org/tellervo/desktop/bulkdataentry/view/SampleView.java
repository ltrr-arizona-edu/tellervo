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
 * Created at Aug 24, 2010, 1:32:02 PM
 */
package org.tellervo.desktop.bulkdataentry.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tellervo.desktop.bulkdataentry.control.ImportSelectedEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromODKFileEvent;
import org.tellervo.desktop.bulkdataentry.control.PrintSampleBarcodesEvent;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.SampleModel;
import org.tellervo.desktop.bulkdataentry.model.TridasFileList;
import org.tellervo.desktop.components.table.ComboBoxCellEditor;
import org.tellervo.desktop.components.table.ControlledVocDictionaryComboBox;
import org.tellervo.desktop.components.table.DynamicJComboBox;
import org.tellervo.desktop.components.table.DynamicKeySelectionManager;
import org.tellervo.desktop.components.table.NattyDateEditor;
import org.tellervo.desktop.components.table.StringCellEditor;
import org.tellervo.desktop.components.table.TridasElementRenderer;
import org.tellervo.desktop.components.table.TridasFileListEditor;
import org.tellervo.desktop.components.table.TridasObjectExRenderer;
import org.tellervo.desktop.components.table.WSIBoxRenderer;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.tridasv2.NumberThenStringComparator;
import org.tellervo.desktop.tridasv2.ui.BooleanCellRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer;
import org.tellervo.desktop.tridasv2.ui.TridasFileArrayRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer.Behavior;
import org.tellervo.desktop.tridasv2.ui.TridasDatingCellRenderer;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSIBoxDictionary;
import org.tellervo.schema.WSISampleTypeDictionary;
import org.tridas.schema.Date;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.gui.combo.MVCJComboBox;
import com.dmurph.mvc.model.HashModel;
import com.michaelbaranov.microba.calendar.DatePicker;
import com.michaelbaranov.microba.calendar.DatePickerCellEditor;



/**
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class SampleView  extends AbstractBulkImportView{

	private JButton printBarcodes;
	private JButton quickFill;
	private JButton browseODK;

	private ElementModel elementModel;

	
	public SampleView(SampleModel argModel, ElementModel elementModel) {
		super(argModel);
		this.elementModel = elementModel;
		table.getColumnModel().getColumn(1).setCellRenderer(new BooleanCellRenderer(true));

	}

	/**
	 * @see org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setupTableCells(JTable argTable) {
		
		argTable.setDefaultEditor(String.class, new StringCellEditor());

		argTable.setDefaultEditor(WSISampleTypeDictionary.class, new ComboBoxCellEditor(new ControlledVocDictionaryComboBox("sampleTypeDictionary")));
		argTable.setDefaultRenderer(WSISampleTypeDictionary.class, new ControlledVocRenderer(Behavior.NORMAL_ONLY));
		
		argTable.setDefaultEditor(TridasFileList.class, new TridasFileListEditor(new JTextField()));
		argTable.setDefaultRenderer(TridasFileList.class, new TridasFileArrayRenderer());

		/*MVCJComboBox<TridasElement> cboElement = new MVCJComboBox<TridasElement>(null, new Comparator<TridasElement>(){
			public int compare(TridasElement argO1, TridasElement argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				return argO1.getTitle().compareToIgnoreCase(argO2.getTitle());
			}
		});*/
		Comparator comparator = new NumberThenStringComparator();
		MVCJComboBox<TridasElement> cboElement = new MVCJComboBox<TridasElement>(null, comparator);
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
		
		
		MVCJComboBox<TridasObjectEx> obj = new MVCJComboBox<TridasObjectEx>(App.tridasObjects.getMutableObjectList(),
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
		obj.setKeySelectionManager(new DynamicKeySelectionManager() {
			@Override
			public String convertToString(Object argO) {
				if(argO == null){
					return "";
				}
				TridasObjectEx o = (TridasObjectEx) argO;
				return o.getLabCode();
			}
		});
		
		obj.setRenderer(new TridasObjectExRenderer());
	
		
		argTable.setDefaultEditor(TridasObject.class, new ComboBoxCellEditor(obj));
		argTable.setDefaultRenderer(TridasObject.class, new TridasObjectExRenderer());
		
		//DatePicker datePicker = new DatePicker();
		//datePicker.setFocusable(false);
		//argTable.setDefaultEditor(Date.class, new DateEditor(datePicker));
		argTable.setDefaultEditor(Date.class, new NattyDateEditor(new JTextField()));
		argTable.setDefaultRenderer(Date.class, new TridasDatingCellRenderer());

	}

	@Override
	protected JToolBar setupToolbar(JButton argCopyButton, JButton argPasteButton, JButton argPasteAppendButton, JButton argAddRowButton, JButton argDeleteRowButton, 
			JButton argCopyRow, JButton argShowHideColumnButton, JButton argPopulateFromDB, JButton argPopulateFromGeonames){
	
		 JToolBar toolbar = new JToolBar();
		 
		 
		 toolbar.add(argCopyButton);
		 toolbar.add(argPasteButton);
		 toolbar.add(argPasteAppendButton);
		 toolbar.add(selectAll);
		 toolbar.add(selectNone);
		 toolbar.add(argAddRowButton);
		 toolbar.add(argDeleteRowButton);
		 toolbar.add(argCopyRow);
		 
		 quickFill = new JButton();
		 quickFill.setIcon(Builder.getIcon("quickfill.png", 22));
		 quickFill.setToolTipText("Open sample quick fill dialog");
		 toolbar.add(quickFill);
		 
			browseODK = new JButton("");
			browseODK.setIcon(Builder.getIcon("odk.png", 22));
			browseODK.setToolTipText("Browse for ODK file");
			toolbar.add(browseODK);
		 
			printBarcodes = new JButton();
			printBarcodes.setIcon(Builder.getIcon("barcode.png", 22));
			printBarcodes.setToolTipText(I18n.getText("bulkimport.printBarcodes"));
			toolbar.add(printBarcodes);
		 toolbar.add(argShowHideColumnButton);
		
		

		
		return toolbar;
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
		
		
		final SampleView glue = this;
		quickFill.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				QuickEntrySample dialog = new QuickEntrySample(glue, model, elementModel);
				dialog.setVisible(true);
				
			}
		});
		
		browseODK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				HashModel model = BulkImportModel.getInstance().getSampleModel();
				PopulateFromODKFileEvent event = new PopulateFromODKFileEvent(model);
				event.dispatch();
			}
		});
	}
	
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView#importSelectedPressed()
	 */
	@Override
	protected void importSelectedPressed() {
		ImportSelectedEvent event = new ImportSelectedEvent(BulkImportController.IMPORT_SELECTED_SAMPLES);
		event.dispatch();
	}
	
	@Override
	protected void populateFromDatabase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void populateFromGeonames() {
		// Not relevant
		
	}
	
}
