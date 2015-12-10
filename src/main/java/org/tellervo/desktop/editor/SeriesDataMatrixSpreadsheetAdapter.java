package org.tellervo.desktop.editor;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.desktop.gui.menus.actions.EditCopyAction;
import org.tellervo.desktop.gui.menus.actions.EditPasteAction;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.schema.WSIBoxDictionary;
import org.tellervo.schema.WSIElementTypeDictionary;
import org.tellervo.schema.WSIObjectTypeDictionary;
import org.tellervo.schema.WSISampleTypeDictionary;
import org.tellervo.schema.WSITaxonDictionary;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasUnit;
import org.tridas.util.TridasObjectEx;
/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables.
 * The clipboard data format used by the adapter is compatible with
 * the clipboard format used by Excel. This provides for clipboard
 * interoperability between enabled JTables and Excel.
 */
public class SeriesDataMatrixSpreadsheetAdapter implements ActionListener
   {
	private final static Logger log = LoggerFactory.getLogger(SeriesDataMatrixSpreadsheetAdapter.class);
   private String rowstring,value;
   private Clipboard system;
   private StringSelection stsel;
   private JTable mainTable ;
   private AbstractEditor editor;
   
   
   /**
    * The Excel Adapter is constructed with a
    * JTable on which it enables Copy-Paste and acts
    * as a Clipboard listener.
    */
   public SeriesDataMatrixSpreadsheetAdapter(SeriesDataMatrix sdm, AbstractEditor editor)
   {
	  this.editor = editor;
      
	  try{
		  mainTable = sdm.myTable;
	  } catch (Exception e)
	  {
		  log.error("Table is null");
	  }
	  
      KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
      // Identifying the copy KeyStroke user can modify this
      // to copy on some other Key combination.
      KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
      KeyStroke pasteappend = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK,false);
      // Identifying the Paste KeyStroke user can modify this
      //to copy on some other Key combination.
      mainTable.registerKeyboardAction(this,"Copy",copy,JComponent.WHEN_FOCUSED);
      mainTable.registerKeyboardAction(this,"Paste",paste,JComponent.WHEN_FOCUSED);
      system = Toolkit.getDefaultToolkit().getSystemClipboard();
   }
   
   
   /**
    * Public Accessor methods for the Table on which this adapter acts.
    */
	public JTable getJTable() 
	{
		return mainTable;
	}

	public void setJTable(JTable tbl) 
	{
		this.mainTable=tbl;
	}


	public void doCopy()
	{
		((EditCopyAction) editor.actions.editCopyAction).doCopy();
        
	}
	
	public void doPaste()
	{
		
		((EditPasteAction) editor.actions.editPasteAction).doPaste();

        
        
	}

	public void doPasteAppend()
	{
		
	}
	
	/**
    * This method is activated on the Keystrokes we are listening to
    * in this implementation. Here it listens for Copy and Paste ActionCommands.
    * Selections comprising non-adjacent cells result in invalid selection and
    * then copy action cannot be performed.
    * Paste is done by aligning the upper left corner of the selection with the
    * 1st element in the current selection of the JTable.
    */
	public void actionPerformed(ActionEvent e)
	{
		log.debug("Action command: "+e.getActionCommand());
		
      if (e.getActionCommand().compareTo("Copy")==0)
      {
    	  doCopy();
      }
      if (e.getActionCommand().compareTo("Paste")==0)
      {
    	  doPaste();
      }
      if (e.getActionCommand().compareTo("PasteAppend")==0)
      {
    	  doPaste();
      }
   }
}