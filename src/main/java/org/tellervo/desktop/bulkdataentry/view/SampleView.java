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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.table.TableColumnExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tellervo.desktop.bulkdataentry.control.ImportSelectedEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromDatabaseEvent;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromODKFileEvent;
import org.tellervo.desktop.bulkdataentry.control.PrintSampleBarcodesEvent;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.ImportStatus;
import org.tellervo.desktop.bulkdataentry.model.ObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SampleModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.bulkdataentry.model.TridasElementOrPlaceholder;
import org.tellervo.desktop.bulkdataentry.model.TridasFileList;
import org.tellervo.desktop.bulkdataentry.model.TridasObjectOrPlaceholder;
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
import org.tellervo.desktop.components.table.WSISampleStatusRenderer;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.tridasv2.NumberThenStringComparator;
import org.tellervo.desktop.tridasv2.ui.BooleanCellRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer;
import org.tellervo.desktop.tridasv2.ui.TridasFileArrayRenderer;
import org.tellervo.desktop.tridasv2.ui.ControlledVocRenderer.Behavior;
import org.tellervo.desktop.tridasv2.ui.TridasDatingCellRenderer;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.schema.SampleStatus;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSIBoxDictionary;
import org.tellervo.schema.WSISampleStatusDictionary;
import org.tellervo.schema.WSISampleTypeDictionary;
import org.tridas.io.util.DateUtils;
import org.tridas.schema.Date;
import org.tridas.schema.DateTime;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.gui.combo.MVCJComboBox;
import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;
import com.michaelbaranov.microba.calendar.DatePicker;
import com.michaelbaranov.microba.calendar.DatePickerCellEditor;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class SampleView extends AbstractBulkImportView {

	private final static Logger log = LoggerFactory.getLogger(SampleView.class);

	private JButton printBarcodes;
	private JButton quickFill;

	private ElementModel elementModel;

	public SampleView(SampleModel argModel, ElementModel elementModel) {
		super(argModel);
		this.elementModel = elementModel;

		table.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				
				int col = table.getColumnModel().getColumnIndexAtX(e.getX());
				int row = table.rowAtPoint(e.getPoint());
				//int row = table.getEditingRow();
				//int row = 1;
				
				try{
					log.debug("Is "+row+","+col+", editable? "+table.isCellEditable(row, col));
					
					if( !table.isCellEditable(row, col))
					{
						
					}
				} catch (Exception ex)
				{
					log.debug(ex.getLocalizedMessage());
				}
				
				
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}

	/**
	 * @see org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView#setupTableCells(javax.swing.JTable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setupTableCells(JTable argTable) {

		argTable.setDefaultEditor(String.class, new StringCellEditor());

		argTable.setDefaultEditor(WSISampleTypeDictionary.class,
				new ComboBoxCellEditor(new ControlledVocDictionaryComboBox(
						"sampleTypeDictionary")));
		argTable.setDefaultRenderer(WSISampleTypeDictionary.class,
				new ControlledVocRenderer(Behavior.NORMAL_ONLY));

		argTable.setDefaultEditor(TridasFileList.class,
				new TridasFileListEditor(new JTextField()));
		argTable.setDefaultRenderer(TridasFileList.class,
				new TridasFileArrayRenderer());
		
		argTable.setDefaultRenderer(ImportStatus.class, new ImportStatusRenderer());


		/*
		 * MVCJComboBox<TridasElement> cboElement = new
		 * MVCJComboBox<TridasElement>(null, new Comparator<TridasElement>(){
		 * public int compare(TridasElement argO1, TridasElement argO2) {
		 * if(argO1 == null){ return -1; } if(argO2 == null){ return 1; } return
		 * argO1.getTitle().compareToIgnoreCase(argO2.getTitle()); } });
		 */
		Comparator comparator = new NumberThenStringComparator();
		MVCJComboBox<TridasElementOrPlaceholder> cboElement = new MVCJComboBox<TridasElementOrPlaceholder>(
				null, comparator);
		cboElement.setRenderer(new TridasElementRenderer());
		cboElement.setKeySelectionManager(new DynamicKeySelectionManager() {
			@Override
			public String convertToString(Object argO) {
				if (argO == null) {
					return "";
				}
				return ((TridasElement) argO).getTitle();
			}
		});
		cboElement.setEditable(true);

		// specific editor for this, options have to be unique per column
		argTable.setDefaultEditor(TridasElementOrPlaceholder.class,
				new ChosenElementEditor(cboElement));
		argTable.setDefaultRenderer(TridasElement.class,
				new TridasElementRenderer());

		argTable.setDefaultRenderer(TridasElementOrPlaceholder.class,
				new DefaultTableCellRenderer() {
					/**
					 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
					 */
					@Override
					protected void setValue(Object argValue) {
						if (argValue == null) {
							super.setValue(argValue);
							return;
						}

						TridasElementOrPlaceholder element = null;
						if (argValue instanceof TridasElementOrPlaceholder) {
							element = (TridasElementOrPlaceholder) argValue;
						} else if (argValue instanceof TridasElement) {
							element = new TridasElementOrPlaceholder(
									(TridasElement) argValue);
							;
						} else if (argValue instanceof String) {
							element = new TridasElementOrPlaceholder(
									(String) argValue);
						}

						super.setValue(element.getCode());
						if (element.getTridasElement() == null) {
							super.setForeground(Color.GRAY);
						} else {
							super.setForeground(Color.BLACK);
						}

					}
				});

		DynamicJComboBox<WSIBox> cboBox = new DynamicJComboBox<WSIBox>(
				Dictionary.getMutableDictionary("boxDictionary"),
				new Comparator<WSIBox>() {
					/**
					 * @see java.util.Comparator#compare(java.lang.Object,
					 *      java.lang.Object)
					 */
					@Override
					public int compare(WSIBox argO1, WSIBox argO2) {
						if (argO1 == null) {
							return -1;
						}
						if (argO2 == null) {
							return 1;
						}
						return argO1.getTitle().compareToIgnoreCase(
								argO2.getTitle());
					}
				});
		cboBox.setRenderer(new WSIBoxRenderer());
		cboBox.setKeySelectionManager(new DynamicKeySelectionManager() {

			@Override
			public String convertToString(Object argO) {
				if (argO == null) {
					return "";
				}
				return ((WSIBox) argO).getTitle();
			}
		});

		argTable.setDefaultEditor(WSIBoxDictionary.class,
				new ComboBoxCellEditor(cboBox));
		argTable.setDefaultRenderer(WSIBoxDictionary.class,
				new WSIBoxRenderer());
		
		JComboBox<SampleStatus> cboSampleStatus = new JComboBox<SampleStatus>(SampleStatus.values());
		cboSampleStatus.setRenderer(new WSISampleStatusRenderer());
		cboSampleStatus.setKeySelectionManager(new DynamicKeySelectionManager() {

			@Override
			public String convertToString(Object argO) {
				if (argO == null) {
					return "";
				}
				return ((SampleStatus) argO).value();
			}
		});

		argTable.setDefaultEditor(WSISampleStatusDictionary.class,
				new ComboBoxCellEditor(cboSampleStatus));
		argTable.setDefaultRenderer(WSISampleStatusDictionary.class,
				new WSISampleStatusRenderer());
		
		

		/*
		 * MVCJComboBox<TridasObjectEx> obj = new
		 * MVCJComboBox<TridasObjectEx>(App
		 * .tridasObjects.getMutableObjectList(), new
		 * Comparator<TridasObjectEx>() { public int compare(TridasObjectEx
		 * argO1, TridasObjectEx argO2) { if(argO1 == null){ return -1; }
		 * if(argO2 == null){ return 1; } return
		 * argO1.getLabCode().compareToIgnoreCase(argO2.getLabCode()); } });
		 * obj.setKeySelectionManager(new DynamicKeySelectionManager() {
		 * 
		 * @Override public String convertToString(Object argO) { if(argO ==
		 * null){ return ""; } TridasObjectEx o = (TridasObjectEx) argO; return
		 * o.getLabCode(); } });
		 * 
		 * obj.setRenderer(new TridasObjectExRenderer());
		 * 
		 * 
		 * argTable.setDefaultEditor(TridasObject.class, new
		 * ComboBoxCellEditor(obj));
		 * argTable.setDefaultRenderer(TridasObject.class, new
		 * TridasObjectExRenderer());
		 */

		MVCArrayList<TridasObjectEx> objlist = App.tridasObjects
				.getMutableObjectList();
		MVCArrayList<TridasObjectOrPlaceholder> toph = new MVCArrayList<TridasObjectOrPlaceholder>();
		for (TridasObjectEx o : objlist) {
			toph.add(new TridasObjectOrPlaceholder(o));
		}

		DynamicJComboBox<TridasObjectOrPlaceholder> cboObject = new DynamicJComboBox<TridasObjectOrPlaceholder>(
				toph, new Comparator<TridasObjectOrPlaceholder>() {
					public int compare(TridasObjectOrPlaceholder argO1,
							TridasObjectOrPlaceholder argO2) {
						if (argO1 == null) {
							return -1;
						}
						if (argO2 == null) {
							return 1;
						}
						return argO1.getCode().compareToIgnoreCase(
								argO2.getCode());
					}
				});
		cboObject.setKeySelectionManager(new DynamicKeySelectionManager() {
			@Override
			public String convertToString(Object argO) {
				if (argO == null) {
					return "";
				}
				TridasObjectOrPlaceholder o = (TridasObjectOrPlaceholder) argO;
				return o.getCode();
			}
		});

		cboObject.setRenderer(new TridasObjectExRenderer());
		argTable.setDefaultEditor(TridasObject.class, new ComboBoxCellEditor(
				cboObject));
		cboObject.setEditable(true);
		argTable.setDefaultEditor(TridasObjectOrPlaceholder.class,
				new ComboBoxCellEditor(cboObject));

		argTable.setDefaultRenderer(TridasObject.class,
				new DefaultTableCellRenderer() {
					/**
					 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
					 */
					@Override
					protected void setValue(Object argValue) {
						if (argValue == null) {
							super.setValue(argValue);
							return;
						}
						TridasObjectEx object = (TridasObjectEx) argValue;
						super.setValue(object.getLabCode());
					}
				});

		argTable.setDefaultRenderer(TridasObjectOrPlaceholder.class,
				new DefaultTableCellRenderer() {
					/**
					 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
					 */
					@Override
					protected void setValue(Object argValue) {
						if (argValue == null) {
							super.setValue(argValue);
							return;
						}

						TridasObjectOrPlaceholder object = null;
						if (argValue instanceof TridasObjectOrPlaceholder) {
							object = (TridasObjectOrPlaceholder) argValue;
						} else if (argValue instanceof TridasObjectEx) {
							object = new TridasObjectOrPlaceholder(
									(TridasObjectEx) argValue);
							;
						} else if (argValue instanceof String) {
							object = new TridasObjectOrPlaceholder(
									(String) argValue);
						}

						super.setValue(object.getCode());
						if (object.getTridasObject() == null) {
							super.setForeground(Color.GRAY);
						} else {
							super.setForeground(Color.BLACK);
						}

					}
				});
		
		/*argTable.setDefaultRenderer(Date.class,
				new DefaultTableCellRenderer() {
				
					@Override
					protected void setValue(Object argValue) {
						if (argValue == null) {
							super.setValue(argValue);
							return;
						}

						Date object = null;
						if (argValue instanceof Date) {
							object = (Date) argValue;
						}  else if (argValue instanceof String) {
							object = new Date();
							DateTime datetime = DateUtils.parseDateTimeFromNaturalString(argValue.toString());
							object.setValue(datetime.getValue());
						}

					}
				});*/
				

		// DatePicker datePicker = new DatePicker();
		// datePicker.setFocusable(false);
		// argTable.setDefaultEditor(Date.class, new DateEditor(datePicker));
		argTable.setDefaultRenderer(org.tridas.schema.Date.class, new TridasDatingCellRenderer());
		argTable.setDefaultEditor(org.tridas.schema.Date.class, new NattyDateEditor(new JTextField()));
		

	}

	@Override
	protected JToolBar setupToolbar(JButton argCopyButton,
			JButton argPasteButton, JButton argPasteAppendButton,
			JButton argAddRowButton, JButton argDeleteRowButton,
			JButton argCopyRow, JButton argShowHideColumnButton,
			JButton argPopulateFromDB, JButton argPopulateFromGeonames,
			JButton argDeleteODKInstances, JButton argODKImport) {

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
		toolbar.add(argDeleteODKInstances);
		
		
		toolbar.add(argPopulateFromDB);


		quickFill = new JButton();
		quickFill.setIcon(Builder.getIcon("quickfill.png", 22));
		quickFill.setToolTipText("Open sample quick fill dialog");
		toolbar.add(quickFill);

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
				SampleModel model = BulkImportModel.getInstance()
						.getSampleModel();
				PrintSampleBarcodesEvent event = new PrintSampleBarcodesEvent(
						model);
				event.dispatch();
			}
		});

		final SampleView glue = this;
		quickFill.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				QuickEntrySample dialog = new QuickEntrySample(glue, model,
						elementModel);
				dialog.setVisible(true);

			}
		});

	}

	/**
	 * @see org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView#importSelectedPressed()
	 */
	@Override
	protected void importSelectedPressed() {
		ImportSelectedEvent event = new ImportSelectedEvent(
				BulkImportController.IMPORT_SELECTED_SAMPLES);
		event.dispatch();
	}

	@Override
	protected void populateFromDatabase() {
		SampleModel model = BulkImportModel.getInstance().getSampleModel();
		PopulateFromDatabaseEvent event = new PopulateFromDatabaseEvent(model);

		event.dispatch();


	}

	@Override
	protected void populateFromGeonames() {
		// Not relevant

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
			
			App.prefs.setArrayListPref(PrefKey.SAMPLE_FIELD_VISIBILITY_ARRAY, defaults);
	}

	@Override
	protected void restoreColumnOrderFromPrefs() {
			
		ArrayList<String> prefs = App.prefs.getArrayListPref(PrefKey.SAMPLE_FIELD_VISIBILITY_ARRAY, null);
		
		if(prefs==null){
			log.info("No prefs set for order of sample columns, so using default order");
			prefs = new ArrayList<String>();
			prefs.add("Project");
			prefs.add("Object Code");
			prefs.add("Title");
			prefs.add("Type");
		}
		
		List<String> all = Arrays.asList(SingleSampleModel.TABLE_PROPERTIES);
		Iterator<String> iterator = prefs.iterator();
		while (iterator.hasNext()) {
			String item = iterator.next();
			if(!all.contains(item))
			{
				log.debug("Removing unknown field from list: "+item);
				iterator.remove();
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
		ArrayList<String> widths = App.prefs.getArrayListPref(PrefKey.SAMPLE_FIELD_COLUMN_WIDTH_ARRAY, null);
		
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
		unhideableColumns.add("Sample code");
		unhideableColumns.add("Type");
		
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
