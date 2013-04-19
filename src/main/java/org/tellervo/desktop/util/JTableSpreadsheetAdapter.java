package org.tellervo.desktop.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel;
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
   
   
   /**
    * The Excel Adapter is constructed with a
    * JTable on which it enables Copy-Paste and acts
    * as a Clipboard listener.
    */
   public JTableSpreadsheetAdapter(JTable myJTable)
   {
      mainTable = myJTable;
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
		log.debug("doCopy() called");
		
        StringBuffer sbf=new StringBuffer();
        // Check to ensure we have selected only a contiguous block of
        // cells
        int numcols=mainTable.getSelectedColumnCount();
        int numrows=mainTable.getSelectedRowCount();
        int[] rowsselected=mainTable.getSelectedRows();
        int[] colsselected=mainTable.getSelectedColumns();
        
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
        
        if (!((numrows-1==rowsselected[rowsselected.length-1]-rowsselected[0] &&
               numrows==rowsselected.length) &&
               (numcols-1==colsselected[colsselected.length-1]-colsselected[0] &&
               numcols==colsselected.length)))
        {
           JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
                                         "Invalid Copy Selection",
                                         JOptionPane.ERROR_MESSAGE);
           return;
        }
        
        
        // First add column headers
        for(int j=0;j<numcols;j++)
        {
    		sbf.append(mainTable.getColumnName(j));
    	
    		if (j<numcols-1) sbf.append("\t");
        }
        sbf.append("\n");
        
        
        for (int i=0;i<numrows;i++)
        {
           for (int j=0;j<numcols;j++)
           {
        	   
        	Object value = mainTable.getValueAt(rowsselected[i],colsselected[j]);
        	
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
			
			String firstColName = mainTable.getColumnName(mainTable.getSelectedColumns()[0]);
			
			if(lines[0].startsWith(firstColName))
			{
				lineCount--;
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
                	 String colname = mainTable.getColumnName(j);
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
					
					if(clazz.equals(WSIObjectTypeDictionary.class))
					{
						if(value!=null && value.toLowerCase()!="null")
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
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
						
					}
					else if(clazz.equals(WSIElementTypeDictionary.class))
					{
						if(value!=null && value.toLowerCase()!="null")
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
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
						
					}
					else if(clazz.equals(WSISampleTypeDictionary.class))
					{
						if(value!=null && value.toLowerCase()!="null")
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
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
						
					}
					else if(clazz.equals(WSIBoxDictionary.class))
					{
						if(value!=null && value.toLowerCase()!="null")
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
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
						
					}
					else if(clazz.equals(WSITaxonDictionary.class))
					{
						if(value!=null && value.toLowerCase()!="null")
						{
							List<ControlledVoc> types = Dictionary.getMutableDictionary("taxonDictionary");
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
								System.out.println("Error in WSITaxonDictionary");
								errorsEncountered = true;
							}
						}
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
						
					}
					else if(clazz.equals(TridasShape.class))
					{
						if(value!=null && value.toLowerCase()!="null")
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
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
						
					}
					else if(clazz.equals(TridasUnit.class))
					{
						if(value!=null && value.toLowerCase()!="null")
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
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
						
					}
					
					else if (clazz.equals(TridasObject.class))
					{
						if(value!=null && value.toLowerCase()!="null")
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
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
					}
					else if (clazz.equals(TridasElement.class))
					{
						
						/*if(value!=null && value.toLowerCase()!="null")
						{
							List<TridasElement> types = ??
							Boolean match = false;
							for(TridasElement obj : types)
							{
			
									tablemodel.setValueAt(obj,rowIndex,startCol+j);
									match = true;
								
							}
							if(match==false) {
								System.out.println("Error in TridasElement");
								errorsEncountered = true;
							}
						}
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}*/
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
						if(value!=null && !value.toLowerCase().equals("null"))
						{
							try{
								tablemodel.setValueAt(Double.parseDouble(value),rowIndex,startCol+j);
							} catch (NumberFormatException e)
							{
								System.out.println("Error in Double");
								errorsEncountered = true;
							}
						}
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
						}
					}
					else if (clazz.equals(BigDecimal.class))
					{
						if(value!=null && !value.toLowerCase().equals("null"))
						{
							try{
								tablemodel.setValueAt(new BigDecimal(Double.parseDouble(value)),rowIndex,startCol+j);
							} catch (NumberFormatException e)
							{
								System.out.println("Error in Double");
								errorsEncountered = true;
							}
						}
						else
						{
							tablemodel.setValueAt(null,rowIndex,startCol+j);
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
	                 System.out.println("Putting "+ value+" at row="+rowIndex+"column="+startCol+j);
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