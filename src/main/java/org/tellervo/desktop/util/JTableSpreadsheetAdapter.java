package org.tellervo.desktop.util;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.view.AbstractBulkImportView;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.schema.WSIBoxDictionary;
import org.tellervo.schema.WSIElementTypeDictionary;
import org.tellervo.schema.WSIObjectTypeDictionary;
import org.tellervo.schema.WSISampleTypeDictionary;
import org.tellervo.schema.WSITaxonDictionary;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasShape;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.MVCArrayList;

import java.awt.datatransfer.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables.
 * The clipboard data format used by the adapter is compatible with
 * the clipboard format used by Excel. This provides for clipboard
 * interoperability between enabled JTables and Excel.
 */
public class JTableSpreadsheetAdapter implements ActionListener
   {
	private final static Logger log = LoggerFactory.getLogger(JTableSpreadsheetAdapter.class);
   private String rowstring,value;
   private Clipboard system;
   private StringSelection stsel;
   private JTable mainTable ;
   
   private List<ControlledVoc> taxonDictionary = Dictionary.getMutableDictionary("taxonDictionary");
   
   
   
   /**
    * The Excel Adapter is constructed with a
    * JTable on which it enables Copy-Paste and acts
    * as a Clipboard listener.
    */
   public JTableSpreadsheetAdapter(JTable myJTable)
   {
      mainTable = myJTable;
      myJTable.getTableHeader().setReorderingAllowed(false);
      KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
      // Identifying the copy KeyStroke user can modify this
      // to copy on some other Key combination.
      KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
      // Identifying the Paste KeyStroke user can modify this
      //to copy on some other Key combination.
      
      KeyStroke pasteAppend = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK,false);
      
      myJTable.registerKeyboardAction(this,"Copy",copy,JComponent.WHEN_FOCUSED);
      myJTable.registerKeyboardAction(this,"Paste",paste,JComponent.WHEN_FOCUSED);
      myJTable.registerKeyboardAction(this,"PasteAppend",pasteAppend,JComponent.WHEN_FOCUSED);

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
		log.debug("doCopy() called");
		
        StringBuffer sbf=new StringBuffer();
        // Check to ensure we have selected only a contiguous block of
        // cells
        int numrows=mainTable.getSelectedRowCount();
        int[] rowsselected=mainTable.getSelectedRows();
        
        // Remove first two columns
        ArrayList<Integer> colsselected = new ArrayList();
        for(int c : mainTable.getSelectedColumns())
        {
        	if(c<=1) continue;
        	colsselected.add(c);
        }
        int numcols=colsselected.size();

        
        log.debug("selected rows: ");
        for(int i : rowsselected)
        {
        	log.debug("    - "+i);
        }
        log.debug("selected cols: ");
        for(int i : colsselected)
        {
        	log.debug("    - "+i);
        }
        
        /*if (!((numrows-1==rowsselected[rowsselected.length-1]-rowsselected[0] &&
               numrows==rowsselected.length) &&
               (numcols-1==colsselected[colsselected.length-1]-colsselected[0] &&
               numcols==colsselected.length)))
        {
           JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
                                         "Invalid Copy Selection",
                                         JOptionPane.ERROR_MESSAGE);
           return;
        }*/
        
        
        // First add column headers
        int colsdone=0;
        for(int j : colsselected)
        {
        	colsdone++;
        	
        	if(j<=1) continue;
        	
    		sbf.append(mainTable.getColumnName(j));
    		
    		if (colsdone<colsselected.size()) sbf.append("\t");
        }
        sbf.append("\n");
        
        
        for (int i=0;i<numrows;i++)
        {
           for (int j=0;j<numcols;j++)
           {
        	   
        	Object value = mainTable.getValueAt(rowsselected[i],colsselected.get(j));
        	
        	if (value instanceof TridasShape)
        	{
        		TridasShape shape = (TridasShape) value;
        		sbf.append(shape.getNormalTridas().value());
        				
        	}
        	if (value instanceof TridasUnit)
        	{
        		TridasUnit unit = (TridasUnit) value;
        		sbf.append(unit.getNormalTridas().value());
        				
        	}
        	else if (value instanceof TridasObject )
        	{
        		TridasObjectEx obj = (TridasObjectEx) value;
        		sbf.append(obj.getLabCode());
        	}
        	else if (value instanceof TridasElement)
        	{
        		TridasElement elem = (TridasElement) value;
        		sbf.append(elem.getTitle());
        	}
        	else if(value instanceof ControlledVoc)
        	{
        		ControlledVoc cvoc = (ControlledVoc)value;
        		if(cvoc.isSetNormal())
        		{
        			sbf.append(cvoc.getNormal());
        		}
        	}
           	else
        	{
        		sbf.append(value);
        	}
           	if (j<numcols-1) sbf.append("\t");
           }
           sbf.append("\n");
        }
        stsel  = new StringSelection(sbf.toString());
        system = Toolkit.getDefaultToolkit().getSystemClipboard();
        system.setContents(stsel,stsel);

	}
	
	public Integer getRowCountFromClipboard()
	{
    	log.debug("Clipboard contents: "+system.getName());
        try {
			String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
			
			String[] lines = StringUtils.splitByLines(trstring);
			
			if(lines.length==0) return null;
			
			Integer lineCount = lines.length;
			
			
			if(mainTable.getSelectedColumns().length>0)
			{
				String firstColName = mainTable.getColumnName(mainTable.getSelectedColumns()[0]);
				if(lines[0].startsWith(firstColName))
				{
					lineCount--;
				}
			}
			
			for(String line : lines)
			{
				if(line.trim().length()==0)
				{
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
	
	public void doPaste()
	{
		Boolean errorsEncountered = false;
		
		
        int startRow=(mainTable.getSelectedRows())[0];
        int startCol=(mainTable.getSelectedColumns())[0];
        
        // Make sure we skip the first two columns.
        if(startCol<2) startCol=2;
        
        
        // Check to see if we need to add rows
        int rowsavail = mainTable.getRowCount()-startRow;
        log.debug("Rows available to paste into = "+rowsavail);
        if( rowsavail < getRowCountFromClipboard())
        {
        	// We do, so add them.
        	int rowsneeded = getRowCountFromClipboard() - rowsavail;
        	log.debug("Rows needed = "+rowsneeded);
        	addNRows(rowsneeded);
        }
        
        
        try
        {
           log.debug("Clipboard contents: "+system.getName());
           String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
           System.out.println("String is:"+trstring);
           StringTokenizer st1=new StringTokenizer(trstring,"\n");
                
           Boolean firstRun = true;
           int rowsignored = 0;
           for(int i=0;st1.hasMoreTokens();i++)
           { 
              rowstring=st1.nextToken();
              StringTokenizer st2=new StringTokenizer(rowstring,"\t");
              for(int j=0;st2.hasMoreTokens();j++)
              {
                 value=(String)st2.nextToken();
               
                 if(firstRun)
                 {
                	 String colname = mainTable.getColumnName(startCol);
                	 if(value.equals(colname))
                	 {
                		 // Clipboard has header line which needs to be skipped
                		 for(int k=0;st2.hasMoreTokens();k++)
                		 {
                			 st2.nextToken();
                		 }
                		 rowsignored=1;
                		 firstRun = false;
                		 continue;
                	 }
                	 firstRun = false;
                 }
                 
				if (startRow+i-rowsignored< mainTable.getRowCount()  &&
                     startCol+j< mainTable.getColumnCount())
				{
                    int rowIndex = startRow+i-rowsignored;
					Object currentValue = mainTable.getValueAt(rowIndex,startCol+j);
					AbstractBulkImportTableModel tablemodel = ((AbstractBulkImportTableModel)mainTable.getModel());
					
					Class clazz = tablemodel.getColumnClass(startCol+j);
					
					if(value==null || value.toLowerCase().equals("null") || value.equals(" ") || value.equals(""))
					{
						log.debug("Value was '"+value+"' so setting cell to null");
						
						tablemodel.setValueAt(null,rowIndex,startCol+j);
					}
					else if(clazz.equals(WSIObjectTypeDictionary.class))
					{
						List<ControlledVoc> types = Dictionary.getMutableDictionary("objectTypeDictionary");
						Boolean match = false;
						for(ControlledVoc cvoc : types)
						{
							if(cvoc.getNormal().equals(value)) {
				                 System.out.println("Putting controlled voc "+ cvoc.getNormal()+" at row="+startRow+i+"column="+startCol+j);
								tablemodel.setValueAt(cvoc,rowIndex,startCol+j);
								match = true;
							}
						}
						if(match==false) {
							System.out.println("Error in WSIObjectTypeDictionary");
							errorsEncountered = true;
						}
											
					}
					else if(clazz.equals(WSIElementTypeDictionary.class))
					{
						List<ControlledVoc> types = Dictionary.getMutableDictionary("elementTypeDictionary");
						Boolean match = false;
						for(ControlledVoc cvoc : types)
						{
							if(cvoc.getNormal().equals(value)) {
				                 System.out.println("Putting controlled voc "+ cvoc.getNormal()+" at row="+startRow+i+"column="+startCol+j);
								tablemodel.setValueAt(cvoc,rowIndex,startCol+j);
								match = true;
							}
						}
						if(match==false) {
							System.out.println("Error in WSIElementTypeDictionary");
							errorsEncountered = true;
						}						
					}
					else if(clazz.equals(WSISampleTypeDictionary.class))
					{
						List<ControlledVoc> types = Dictionary.getMutableDictionary("sampleTypeDictionary");
						Boolean match = false;
						for(ControlledVoc cvoc : types)
						{
							if(cvoc.getNormal().equals(value)) {
				                 System.out.println("Putting controlled voc "+ cvoc.getNormal()+" at row="+startRow+i+"column="+startCol+j);
								tablemodel.setValueAt(cvoc,rowIndex,startCol+j);
								match = true;
							}
						}
						if(match==false) {
							System.out.println("Error in WSISampleTypeDictionary");
							errorsEncountered = true;
						}						
					}
					else if(clazz.equals(WSIBoxDictionary.class))
					{
						List<ControlledVoc> types = Dictionary.getMutableDictionary("boxDictionary");
						Boolean match = false;
						for(ControlledVoc cvoc : types)
						{
							if(cvoc.getNormal().equals(value)) {
				                 System.out.println("Putting controlled voc "+ cvoc.getNormal()+" at row="+startRow+i+"column="+startCol+j);
								tablemodel.setValueAt(cvoc,rowIndex,startCol+j);
								match = true;
							}
						}
						if(match==false) {
							System.out.println("Error in WSIBoxDictionary");
							errorsEncountered = true;
						}						
					}
					else if(clazz.equals(WSITaxonDictionary.class))
					{
						
						Boolean match = false;
						for(ControlledVoc cvoc : taxonDictionary)
						{
							if(cvoc.getNormal().equals(value)) {
				                 System.out.println("Putting controlled voc "+ cvoc.getNormal()+" at row="+startRow+i+"column="+startCol+j);
								tablemodel.setValueAt(cvoc,rowIndex,startCol+j);
								match = true;
							}
						}
						if(match==false) {
							System.out.println("Error in WSITaxonDictionary");
							log.debug("Cannont find entry for '"+value+"'");
							log.debug("Number of taxa in dictionary = "+taxonDictionary.size());
							errorsEncountered = true;
						}						
					}
					else if(clazz.equals(TridasShape.class))
					{
						NormalTridasShape[] types = NormalTridasShape.values();
						Boolean match = false;
						for(NormalTridasShape item : types)
						{
							if(item.value().equals(value)) {
				                 
								TridasShape shape = new TridasShape();
								shape.setNormalTridas(item);
								tablemodel.setValueAt(shape,rowIndex,startCol+j);
								match = true;
							}
						}
						if(match==false) {
							System.out.println("Error in TridasShape");
							errorsEncountered = true;
						}						
					}
					else if(clazz.equals(TridasUnit.class))
					{
						NormalTridasUnit[] types = NormalTridasUnit.values();
						Boolean match = false;
						for(NormalTridasUnit item : types)
						{
							if(item.value().equals(value)) {
				                 
								TridasUnit unit = new TridasUnit();
								unit.setNormalTridas(item);
								tablemodel.setValueAt(unit,rowIndex,startCol+j);
								match = true;
							}
						}
						if(match==false) {
							System.out.println("Error in TridasShape");
							errorsEncountered = true;
						}						
					}
					
					else if (clazz.equals(TridasObject.class))
					{
						List<TridasObjectEx> types = App.tridasObjects.getMutableObjectList();
						Boolean match = false;
						for(TridasObjectEx obj : types)
						{
							if(obj.getLabCode().equals(value)) {
				     
								tablemodel.setValueAt(obj,rowIndex,startCol+j);
								match = true;
							}
						}
						if(match==false) {
							System.out.println("Error in TridasObject");
							errorsEncountered = true;
						}
					}
					else if (clazz.equals(TridasElement.class))
					{
						TridasElement tempElement = new TridasElement();
						tempElement.setTitle(value);
		
						tablemodel.setValueAt(tempElement,rowIndex,startCol+j);
					}
					else if (clazz.equals(GPXWaypoint.class))
					{
						// ignore
					}
					else if (clazz.equals(Boolean.class))
					{
						if(value.toLowerCase().equals("true") || value.toLowerCase().equals("t")|| value.toLowerCase().equals("y") || value.toLowerCase().equals("yes"))
						{
							tablemodel.setValueAt(true,rowIndex,startCol+j);
						}
						else if(value==null || value.toLowerCase().equals("null") || value.toLowerCase().equals("false") || value.toLowerCase().equals("f")|| value.toLowerCase().equals("n") || value.toLowerCase().equals("no"))
						{
							tablemodel.setValueAt(false,rowIndex,startCol+j);
						}
						else
						{
							System.out.println("Error in Boolean");
							errorsEncountered = true;
						}
					}
					else if (clazz.equals(Double.class))
					{
						try{
							log.debug("Parsing '"+value.toString()+"' to double");
							double dbl = Double.parseDouble(value.toString());
							log.debug("Value = "+dbl);
							
							tablemodel.setValueAt(dbl, rowIndex,startCol+j);
						} catch (NumberFormatException e)
						{
							System.out.println("Error in Double");
							errorsEncountered = true;
						}
					}
					else if (clazz.equals(BigDecimal.class))
					{
						try{
							log.debug("Parsing '"+value.toString()+"' to double");
							double dbl = Double.parseDouble(value.toString());
							log.debug("Double value = "+dbl);
							BigDecimal bd = BigDecimal.valueOf(dbl);
							log.debug("Parsing '"+dbl+"' to BigDecimal");
							log.debug("BigDecimal value = "+bd);
															
							tablemodel.setValueAt(bd,rowIndex,startCol+j);
						} catch (NumberFormatException e)
						{
							System.out.println("Error in Double");
							errorsEncountered = true;
						}
					}
					else
					{
						// Assuming string, catch cast exception if not
						if(value.toLowerCase().equals("null"))
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
						else
						{
							try{
								tablemodel.setValueAt(value,rowIndex,startCol+j);
							} catch (ClassCastException e)
							{
								tablemodel.setValueAt(null,rowIndex,startCol+j);
								errorsEncountered=true;
							}
						}
					}
	                 log.debug("Putting "+ value+" at row="+rowIndex+"column="+startCol+j);
				}
				else 
				{
					Alert.error("Too much data", "You attempted to paste more data into the table than there were cells. The remaining data has been discarded.");
					return;
				}
             }
          }
       }
       catch(Exception ex){ex.printStackTrace();}

        mainTable.repaint();
        if (errorsEncountered)
        {
        	Alert.error("Error", "One or more errors were encountered when pasting data.  Erroneous fields will be left blank." +
        						 "Check the data you are pasting is in the correct format " +
        						 "and try again." );
        }
        
        
	}
	
	
	
	

	public void doPasteAppend()
	{
		int previousRowCount = mainTable.getRowCount();
		int rows= getRowCountFromClipboard();
		
		addNRows(rows);
		
		mainTable.setRowSelectionInterval(previousRowCount, previousRowCount);
		mainTable.setColumnSelectionInterval(2, 2);		
		doPaste();
		
		
	}
	
	private void addNRows(int rows)
	{
		AbstractBulkImportTableModel tablemodel = ((AbstractBulkImportTableModel)mainTable.getModel());

		for(int i=0; i<rows; i++)
		{
			tablemodel.addRow();
		}
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
    	  doPasteAppend();
      }
   }
}