package org.tellervo.desktop.util;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel;
import org.tellervo.desktop.bulkdataentry.view.DateEditor;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSIBoxDictionary;
import org.tellervo.schema.WSIElementTypeDictionary;
import org.tellervo.schema.WSIObjectTypeDictionary;
import org.tellervo.schema.WSIProjectTypeDictionary;
import org.tellervo.schema.WSISampleTypeDictionary;
import org.tellervo.schema.WSITaxonDictionary;
import org.tridas.io.util.DateUtils;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.Certainty;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasUnit;
import org.tridas.util.TridasObjectEx;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables. The
 * clipboard data format used by the adapter is compatible with the clipboard
 * format used by Excel. This provides for clipboard interoperability between
 * enabled JTables and Excel.
 */
public class JTableSpreadsheetAdapter implements ActionListener {
	private final static Logger log = LoggerFactory
			.getLogger(JTableSpreadsheetAdapter.class);
	private String rowstring, value;
	private Clipboard system;
	private StringSelection stsel;
	private JTable mainTable;
	private TellervoProgressDialog progDialog;

	  private static JProgressBar PROGRESS_BAR;
	  private static JLabel OUTPUT_LABEL;
	
	private List<ControlledVoc> taxonDictionary = Dictionary
			.getMutableDictionary("taxonDictionary");

	private DefaultTableModel pasteErrorTableModel;

	/**
	 * The Excel Adapter is constructed with a JTable on which it enables
	 * Copy-Paste and acts as a Clipboard listener.
	 */
	public JTableSpreadsheetAdapter(JTable myJTable) {
		mainTable = myJTable;
		// myJTable.getTableHeader().setReorderingAllowed(false);
		KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK, false);
		// Identifying the copy KeyStroke user can modify this
		// to copy on some other Key combination.
		KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,
				ActionEvent.CTRL_MASK, false);
		// Identifying the Paste KeyStroke user can modify this
		// to copy on some other Key combination.

		KeyStroke pasteAppend = KeyStroke.getKeyStroke(KeyEvent.VK_V,
				ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK, false);

		myJTable.registerKeyboardAction(this, "Copy", copy,
				JComponent.WHEN_FOCUSED);
		myJTable.registerKeyboardAction(this, "Paste", paste,
				JComponent.WHEN_FOCUSED);
		myJTable.registerKeyboardAction(this, "PasteAppend", pasteAppend,
				JComponent.WHEN_FOCUSED);

		system = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	/**
	 * Public Accessor methods for the Table on which this adapter acts.
	 */
	public JTable getJTable() {
		return mainTable;
	}

	public void setJTable(JTable tbl) {
		this.mainTable = tbl;
	}

	public void doCopy() {
		log.debug("doCopy() called");

		StringBuffer sbf = new StringBuffer();
		// Check to ensure we have selected only a contiguous block of
		// cells
		int numrows = mainTable.getSelectedRowCount();
		int[] rowsselected = mainTable.getSelectedRows();

		// Remove first two columns
		ArrayList<Integer> colsselected = new ArrayList();
		for (int c : mainTable.getSelectedColumns()) {
			if (c <= 1)
				continue;
			colsselected.add(c);
		}
		int numcols = colsselected.size();

		log.debug("selected rows: ");
		for (int i : rowsselected) {
			log.debug("    - " + i);
		}
		log.debug("selected cols: ");
		for (int i : colsselected) {
			log.debug("    - " + i);
		}

		/*
		 * if (!((numrows-1==rowsselected[rowsselected.length-1]-rowsselected[0]
		 * && numrows==rowsselected.length) &&
		 * (numcols-1==colsselected[colsselected.length-1]-colsselected[0] &&
		 * numcols==colsselected.length))) { JOptionPane.showMessageDialog(null,
		 * "Invalid Copy Selection", "Invalid Copy Selection",
		 * JOptionPane.ERROR_MESSAGE); return; }
		 */

		int colsdone = 0;
		
		// First add column headers - but only if multiple columns are selected
		if(colsselected.size()>1)
		{	
			for (int j : colsselected) {
				colsdone++;
	
				if (j <= 1)
					continue;
	
				sbf.append(mainTable.getColumnName(j));
	
				if (colsdone < colsselected.size())
					sbf.append("\t");
			}
			sbf.append("\n");
		}

		for (int i = 0; i < numrows; i++) {
			for (int j = 0; j < numcols; j++) {

				Object value = mainTable.getValueAt(rowsselected[i],
						colsselected.get(j));

				if (value == null) {
					sbf.append("");
				} else if (value instanceof TridasShape) {
					TridasShape shape = (TridasShape) value;
					sbf.append(shape.getNormalTridas().value());

				} else if (value instanceof TridasUnit) {
					TridasUnit unit = (TridasUnit) value;
					sbf.append(unit.getNormalTridas().value());

				}else if (value instanceof TridasProject) {
					TridasProject proj = (TridasProject) value;
					sbf.append(proj.getTitle());
				} 
				else if (value instanceof TridasObject) {
					TridasObjectEx obj = (TridasObjectEx) value;
					sbf.append(obj.getMultiLevelLabCode());
				} else if (value instanceof TridasElement) {
					TridasElement elem = (TridasElement) value;
					sbf.append(elem.getTitle());
				} else if (value instanceof ControlledVoc) {
					ControlledVoc cvoc = (ControlledVoc) value;
					if (cvoc.isSetNormal()) {
						sbf.append(cvoc.getNormal());
					}
				} else if (value instanceof NormalTridasLocationType){
					NormalTridasLocationType v = (NormalTridasLocationType) value;
					sbf.append(v.value());
				}
				else if (value instanceof WSIBox) {
					WSIBox box = (WSIBox) value;
					sbf.append(box.getTitle());
				} else if (value instanceof org.tridas.schema.Date) {
					org.tridas.schema.Date dt = (org.tridas.schema.Date) value;
					String strdt = DateUtils.getFormattedDate(dt,
							new SimpleDateFormat("YYYY-MM-dd"));
					sbf.append(strdt);
				} else {
					sbf.append(value);
				}
				if (j < numcols - 1)
					sbf.append("\t");
			}
			sbf.append("\n");
		}
		stsel = new StringSelection(sbf.toString());
		system = Toolkit.getDefaultToolkit().getSystemClipboard();
		system.setContents(stsel, stsel);

	}

	public Integer getRowCountFromClipboard() {
		log.debug("Clipboard contents: " + system.getName());
		try {
			String trstring = (String) (system.getContents(this)
					.getTransferData(DataFlavor.stringFlavor));

			String[] lines = StringUtils.splitByLines(trstring);

			if (lines.length == 0)
				return null;

			Integer lineCount = lines.length;

			if (mainTable.getSelectedColumns().length > 0) {
				String firstColName = mainTable.getColumnName(mainTable
						.getSelectedColumns()[0]);
				if (lines[0].startsWith(firstColName)) {
					lineCount--;
				}
			}

			for (String line : lines) {
				if (line.trim().length() == 0) {
					lineCount--;
				}
			}

			return lineCount;

		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private void initProgress(final int max)
	{
		/*SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    		progDialog = new TellervoProgressDialog();
		    		progDialog.initProgress(max);
		    		progDialog.setVisible(true);
		    	
		    }
		});*/
	}
	
	private void updateProgress(final int val) {
		  SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		      // Here, we can safely update the GUI
		      // because we'll be called from the
		      // event dispatch thread
		      //statusLabel.setText("Query: " + queryNo);
		    	//progDialog.setProgress(val);
		    }
		  });
	}
	
	private void finishProgress()
	{
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    		//progDialog.setVisible(false);
		    	
		    }
		});
	}
	
	
	public void doPaste() {
		this.doPaste(true, false);
	}
	
	private void doPaste(Boolean simulateFirst, Boolean pasteAppend) {
		

		//progDialog = new TellervoProgressDialog();
		
		
		
		
		if (simulateFirst == false && pasteAppend == true) {
			int previousRowCount = mainTable.getRowCount();
			int rows = getRowCountFromClipboard();

			addNRows(rows);

			mainTable.setRowSelectionInterval(previousRowCount,
					previousRowCount);
			mainTable.setColumnSelectionInterval(2, 2);
		}

		Boolean errorsEncountered = false;
		pasteErrorTableModel = new DefaultTableModel();
		pasteErrorTableModel.addColumn("Row");
		pasteErrorTableModel.addColumn("Column");
		pasteErrorTableModel.addColumn("Value");
		pasteErrorTableModel.addColumn("Error");

		int startRow;
		int startCol;
		try {
			startRow = (mainTable.getSelectedRows())[0];
			startCol = (mainTable.getSelectedColumns())[0];
		} catch (Exception e) {
			startRow = 0;
			startCol = 0;
		}

		// Make sure we skip the first two columns.
		if (startCol < 2)
			startCol = 2;

		// Check to see if we need to add rows
		int rowsavail = mainTable.getRowCount() - startRow;
		log.debug("Rows available to paste into = " + rowsavail);
		if (rowsavail < getRowCountFromClipboard()) {
			// We do, so add them.
			int rowsneeded = getRowCountFromClipboard() - rowsavail;
			log.debug("Rows needed = " + rowsneeded);

			if (!simulateFirst)
				addNRows(rowsneeded);
		}

		try {
			log.debug("Clipboard contents: " + system.getName()); 
			String trstring = (String) (system.getContents(this)
					.getTransferData(DataFlavor.stringFlavor));
			log.debug("Clipboard string is: " + trstring);
			String[] lines = trstring.split("\n");
			
			// Check we have the correct number of columns 
			int correctColCount = mainTable.getColumnCount()-2;
			
			for(String rowstring : lines)
			{
				String[] cells = rowstring.split("\t");
				
				if(cells.length>correctColCount)
				{
					Alert.error(
							"Incorrect column count",
							"There are more columns in the clipboard than there are columns in the table. Please check your columns and try again.");
					return;
				}
			}
			
			
			int rowsignored = 0;
			int lineindex = -1;
			
			
			if(lines.length==1 && 
					//lines[0].split("\t").length==1 && 
					mainTable.getSelectedColumnCount()==1 && 
					mainTable.getSelectedRowCount()>1)
			{
				String strline = lines[0];
				lines = new String[mainTable.getSelectedRowCount()];
				for(int p=0; p<mainTable.getSelectedRowCount(); p++)
				{
					lines[p] = strline;
				}
			}
						
			initProgress(lines.length);
			
			for (String rowstring : lines) {
				
				updateProgress(lineindex);
				
				lineindex++;
				// rowstring=st1.nextToken();
				String[] cells = rowstring.split("\t");

				if (cells.length == 0)
					continue;

				// Check to see if we have a header line that needs to be
				// ignored
				String colname = mainTable.getColumnName(0);
				if (cells[0].equals(mainTable.getColumnName(0))
						|| cells[0].equals(mainTable.getColumnName(2))) {
					// Header found so skip
					log.debug("Found header line.  Skipping");
					rowsignored++;
					continue;
				} else {
					log.debug("First cell is " + cells[0]
							+ " which doesn't match the first column name "
							+ colname);
					log.debug("No header to skip");
				}

				int colViewIndex = -1;
				for (String value : cells) {
					
					value = value.trim();
					
					colViewIndex++;
					// value=(String)st2.nextToken();

					log.debug("Value of this cell is = '" + value + "'");

					// Convert the row index to a model index
					int rowModelIndex = mainTable.convertRowIndexToModel(startRow
							+ lineindex - rowsignored);
					int colModelIndex = mainTable.convertColumnIndexToModel(colViewIndex+startCol);

					if ((rowModelIndex < mainTable.getRowCount() && startCol
							+ colViewIndex < mainTable.getColumnCount())
							|| (simulateFirst && pasteAppend)) {

						AbstractBulkImportTableModel tablemodel = ((AbstractBulkImportTableModel) mainTable
								.getModel());

						Class clazz = tablemodel.getColumnClass(colModelIndex);

						log.debug("Value for cell " + colViewIndex + " was '"
								+ value + "'");

						// Replace various representations of 'null' with a real
						// null
						if (value == null || value.equalsIgnoreCase("null")
								|| value.equals(" ") || value.equals("")
								|| value.replace(" ", "").length() == 0) {
							log.debug("Value for cell was '" + value
									+ "' so setting cell to null");

							if (!simulateFirst)
								tablemodel.setValueAt(null, rowModelIndex,colModelIndex);
						} else if (clazz.equals(WSIProjectTypeDictionary.class)) {
							List<ControlledVoc> types = Dictionary
									.getMutableDictionary("projectTypeDictionary");
							Boolean match = false;
							for (ControlledVoc cvoc : types) {
								if (cvoc.getNormal().equalsIgnoreCase(value)) {
									if (!simulateFirst)
										tablemodel.setValueAt(cvoc, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only items from the project type dictionary can be used.");
								errorsEncountered = true;
							}

						} else if (clazz.equals(WSIObjectTypeDictionary.class)) {
							List<ControlledVoc> types = Dictionary
									.getMutableDictionary("objectTypeDictionary");
							Boolean match = false;
							for (ControlledVoc cvoc : types) {
															
								if (cvoc.getNormal().equalsIgnoreCase(value)) {
									if (!simulateFirst)
										tablemodel.setValueAt(cvoc, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only items from the object type dictionary can be used.");
								errorsEncountered = true;
							}

						} else if (clazz.equals(WSIElementTypeDictionary.class)) {
							List<ControlledVoc> types = Dictionary
									.getMutableDictionary("elementTypeDictionary");
							Boolean match = false;
							for (ControlledVoc cvoc : types) {
								if (cvoc.getNormal().equalsIgnoreCase(value)) {
									if (!simulateFirst)
										tablemodel.setValueAt(cvoc, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only items from the element type dictionary can be used.");
								errorsEncountered = true;
							}
						} else if (clazz.equals(WSISampleTypeDictionary.class)) {
							List<ControlledVoc> types = Dictionary
									.getMutableDictionary("sampleTypeDictionary");
							Boolean match = false;
							for (ControlledVoc cvoc : types) {
								if (cvoc.getNormal().equals(value)) {
									if (!simulateFirst)
										tablemodel.setValueAt(cvoc, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only items from the sample type dictionary can be used.");
								errorsEncountered = true;
							}
						} else if (clazz.equals(WSIBoxDictionary.class)) {
							List<WSIBox> types = Dictionary
									.getMutableDictionary("boxDictionary");
							Boolean match = false;
							for (WSIBox bx : types) {
								if (bx.getTitle().equalsIgnoreCase(value)) {
									if (!simulateFirst)
										tablemodel.setValueAt(bx, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only items from the box dictionary can be used.");
								errorsEncountered = true;
							}
						} else if (clazz.equals(WSITaxonDictionary.class)) {

							Boolean match = false;
							for (ControlledVoc cvoc : taxonDictionary) {
								if (cvoc.getNormal().equalsIgnoreCase(value)) {
									if (!simulateFirst)
										tablemodel.setValueAt(cvoc, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only items from the taxon dictionary can be used.");

								errorsEncountered = true;
							}
						} else if (clazz.equals(TridasShape.class)) {
							NormalTridasShape[] types = NormalTridasShape
									.values();
							Boolean match = false;
							for (NormalTridasShape item : types) {
								if (item.value().equalsIgnoreCase(value)) {

									TridasShape shape = new TridasShape();
									shape.setNormalTridas(item);
									if (!simulateFirst)
										tablemodel.setValueAt(shape, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only items from the shape dictionary can be used.");
								errorsEncountered = true;
							}
							
						}else if (clazz.equals(NormalTridasLocationType.class)) {
							NormalTridasLocationType[] types = NormalTridasLocationType.values();
							Boolean match = false;
							for (NormalTridasLocationType item : types) {
								if (item.value().equalsIgnoreCase(value)) {

									if (!simulateFirst)
										tablemodel.setValueAt(item, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only standard TRiDaS location types can be used.");
								errorsEncountered = true;
							} 
						}						
						else if (clazz.equals(TridasUnit.class)) {
							NormalTridasUnit[] types = NormalTridasUnit
									.values();
							Boolean match = false;
							for (NormalTridasUnit item : types) {
								if (item.value().equalsIgnoreCase(value)) {

									TridasUnit unit = new TridasUnit();
									unit.setNormalTridas(item);
									if (!simulateFirst)
										tablemodel.setValueAt(unit, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only units defined by TRiDaS can be used.");
								errorsEncountered = true;
							}
						}
						else if (clazz.equals(TridasProject.class)) {
							List<TridasProject> types = App.tridasProjects.getMutableProjectList();
							Boolean match = false;
							for (TridasProject proj : types) {
								if (proj.getTitle().equals(value)) {

									if (!simulateFirst)
										tablemodel.setValueAt(proj, rowModelIndex,
												colModelIndex);
									match = true;
								}
							}
							if (match == false && value!=null && value.length()>0) {
								logPasteError(lineindex, colModelIndex, value,
										"Only the names of existing projects can be specified");
								errorsEncountered = true;
							}
						}
						else if (clazz.equals(TridasObject.class)) {
							List<TridasObjectEx> types = App.tridasObjects
									.getMutableObjectList();
							Boolean match = false;
							for (TridasObjectEx obj : types) {

								String multicode = TridasUtils.getObjectCodeMulti(obj);
		
								if (multicode.equals(value)) {

									if (!simulateFirst)
										tablemodel.setValueAt(obj, rowModelIndex,
												colModelIndex);
									match = true;
								}					
								
							}
							if (match == false) {
								logPasteError(lineindex, colModelIndex, value,
										"Only the names of existing objects can be specified");
								errorsEncountered = true;
							}
						} else if (clazz.equals(TridasElement.class)) {
							// TODO CHECK THISS!!!!!
							TridasElement tempElement = new TridasElement();
							tempElement.setTitle(value);

							if (!simulateFirst)
								tablemodel.setValueAt(tempElement, rowModelIndex,
										colModelIndex);
						} else if (clazz.equals(GPXWaypoint.class)) {
							
							if(value!=null && value.length()>0)
							{
								logPasteError(lineindex, colModelIndex, value,
										"Pasting in waypoint names not supported");
								errorsEncountered = true;
							}
							
						} else if (clazz.equals(Boolean.class)) {
							if (value.toLowerCase().equals("true")
									|| value.toLowerCase().equals("t")
									|| value.toLowerCase().equals("y")
									|| value.toLowerCase().equals("yes")) {
								if (!simulateFirst)
									tablemodel.setValueAt(true, rowModelIndex,
											colModelIndex);
							} else if (value == null
									|| value.toLowerCase().equals("null")
									|| value.toLowerCase().equals("false")
									|| value.toLowerCase().equals("f")
									|| value.toLowerCase().equals("n")
									|| value.toLowerCase().equals("no")) {
								if (!simulateFirst)
									tablemodel.setValueAt(false, rowModelIndex,
											colModelIndex);
							} else {
								logPasteError(
										lineindex,
										colViewIndex,
										value,
										"Invalid boolean value.  Must be one of: true; yes; t; y; false; no; f; n; or null.");
								errorsEncountered = true;
							}
						} else if (clazz.equals(Double.class)) {
							try {
								log.debug("Parsing '" + value.toString()
										+ "' to double");
								double dbl = Double.parseDouble(value
										.toString());
								log.debug("Value = " + dbl);

								if (!simulateFirst)
									tablemodel.setValueAt(dbl, rowModelIndex,
											colModelIndex);
							} catch (NumberFormatException e) {
								logPasteError(lineindex, colModelIndex, value,
										"Not a valid decimal number");
								errorsEncountered = true;
							}
						} else if (clazz.equals(org.tridas.schema.Date.class)) {
							try {
								log.debug("Parsing '" + value.toString()
										+ "' to org.tridas.schema.Date");

								Parser parser = new Parser();
								List<DateGroup> groups = parser.parse(value);

								log.debug("Natty found " + groups.size()
										+ " groups");

								org.tridas.schema.Date schemadate = null;
								if (groups.size() == 1) {
									log.debug("Natty found "
											+ groups.get(0).getDates().size()
											+ " dates");

									Date dt = null;
									if (groups.get(0).getDates().size() > 0) {
										dt = groups.get(0).getDates().get(0);
										schemadate = DateEditor
												.javaDateToSchemaDate(dt);
									}
									if (groups.get(0).getDates().size() > 1) {
										schemadate
												.setCertainty(Certainty.APPROXIMATELY);
									}
								}

								if (!simulateFirst)
									tablemodel.setValueAt(schemadate, rowModelIndex,
											colModelIndex);
							} catch (Exception e) {
								logPasteError(lineindex, colModelIndex, value,
										"Not a valid representation of a date");
								errorsEncountered = true;
							}
						} else if (clazz.equals(BigDecimal.class)) {
							try {
								log.debug("Parsing '" + value.toString()
										+ "' to double");
								double dbl = Double.parseDouble(value
										.toString());
								log.debug("Double value = " + dbl);
								BigDecimal bd = BigDecimal.valueOf(dbl);
								log.debug("Parsing '" + dbl + "' to BigDecimal");
								log.debug("BigDecimal value = " + bd);

								if (!simulateFirst)
									tablemodel.setValueAt(bd, rowModelIndex,
											colModelIndex);
							} catch (NumberFormatException e) {
								logPasteError(lineindex, colModelIndex, value,
										"Not a valid representation of a decimal number");
								errorsEncountered = true;
							}
						} else {
							// Assuming string, catch cast exception if not
							if (value.toLowerCase().equals("null")) {
								if (!simulateFirst)
									tablemodel.setValueAt(null, rowModelIndex,
											colModelIndex);
							} else {
								try {
									if (!simulateFirst)
										tablemodel.setValueAt(value, rowModelIndex,
												colModelIndex);
								} catch (ClassCastException e) {
									if (!simulateFirst)
										tablemodel.setValueAt(null, rowModelIndex,
												colModelIndex);
									logPasteError(lineindex, colModelIndex, value,
											"Not a valid entry");
									errorsEncountered = true;
								}
							}
						}
					} else {
						if (pasteAppend) {
							log.debug("Too much data but not worrying because we are simulating a pasteAppend");
						} else {
							Alert.error(
									"Too much data",
									"You attempted to paste more data into the table than there were cells. The remaining data has been discarded.");
							finishProgress();
							return;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			finishProgress();
		}
		
		finishProgress();
		mainTable.repaint();
		if (errorsEncountered && simulateFirst == true) {
			PasteErrorReportDialog dialog = new PasteErrorReportDialog(
					this.pasteErrorTableModel);
			dialog.setVisible(true);
			Boolean doContinue = dialog.getContinue();

			if (doContinue) {
				dialog.dispose();
				doPaste(false, pasteAppend);
			}

			// Alert.error("Error",
			// "One or more errors were encountered when pasting data.  Erroneous fields will be left blank."
			// +
			// "Check the data you are pasting is in the correct format " +
			// "and try again." );
		} else if (errorsEncountered == false && simulateFirst == true) {
			doPaste(false, pasteAppend);
		}
		
		
		


	}

	private void logPasteError(int row, int col, String value, String msg) {
		String colname = mainTable.getModel().getColumnName(col);

		Object[] error = { row + 1, colname, value, msg };
		pasteErrorTableModel.addRow(error);
	}

	public void doPasteAppend() {

		doPaste(true, true);

	}

	private void addNRows(int rows) {
		AbstractBulkImportTableModel tablemodel = ((AbstractBulkImportTableModel) mainTable
				.getModel());

		for (int i = 0; i < rows; i++) {
			tablemodel.addRow();
		}
	}

	/**
	 * This method is activated on the Keystrokes we are listening to in this
	 * implementation. Here it listens for Copy and Paste ActionCommands.
	 * Selections comprising non-adjacent cells result in invalid selection and
	 * then copy action cannot be performed. Paste is done by aligning the upper
	 * left corner of the selection with the 1st element in the current
	 * selection of the JTable.
	 */
	public void actionPerformed(ActionEvent e) {
		log.debug("Action command: " + e.getActionCommand());

		if (e.getActionCommand().compareTo("Copy") == 0) {
			doCopy();
		}
		if (e.getActionCommand().compareTo("Paste") == 0) {
			doPaste();
		}
		if (e.getActionCommand().compareTo("PasteAppend") == 0) {
			doPasteAppend();
		}
	}
	
}
