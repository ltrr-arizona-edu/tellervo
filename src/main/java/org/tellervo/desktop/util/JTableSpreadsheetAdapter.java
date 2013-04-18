package org.tellervo.desktop.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel;
import org.tellervo.schema.WSIObjectTypeDictionary;
import org.tridas.schema.ControlledVoc;

import java.awt.datatransfer.*;
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
        StringBuffer sbf=new StringBuffer();
        // Check to ensure we have selected only a contiguous block of
        // cells
        int numcols=mainTable.getSelectedColumnCount();
        int numrows=mainTable.getSelectedRowCount();
        int[] rowsselected=mainTable.getSelectedRows();
        int[] colsselected=mainTable.getSelectedColumns();
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
        for (int i=0;i<numrows;i++)
        {
           for (int j=0;j<numcols;j++)
           {
        	   
        	Object value = mainTable.getValueAt(rowsselected[i],colsselected[j]);
        	
        	if(value instanceof ControlledVoc)
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
	
	public void doPaste()
	{
        System.out.println("Trying to Paste");
        int startRow=(mainTable.getSelectedRows())[0];
        int startCol=(mainTable.getSelectedColumns())[0];
        try
        {
           String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
           System.out.println("String is:"+trstring);
           StringTokenizer st1=new StringTokenizer(trstring,"\n");
           for(int i=0;st1.hasMoreTokens();i++)
           {
              rowstring=st1.nextToken();
              StringTokenizer st2=new StringTokenizer(rowstring,"\t");
              for(int j=0;st2.hasMoreTokens();j++)
              {
                 value=(String)st2.nextToken();
                  
				if (startRow+i< mainTable.getRowCount()  &&
                     startCol+j< mainTable.getColumnCount())
				{
                    
					Object currentValue = mainTable.getValueAt(startRow+i,startCol+j);
					AbstractBulkImportTableModel tablemodel = ((AbstractBulkImportTableModel)mainTable.getModel());
					
					Class clazz = tablemodel.getColumnClass(startCol+j);
					
					/*if(clazz.equals(WSIObjectTypeDictionary.class))
					{
						WSIObjectTypeDictionary dictionary = new WSIObjectTypeDictionary();
						List<ControlledVoc> types = dictionary.getObjectTypes();
						
						for(ControlledVoc cvoc : types)
						{
							if(cvoc.getNormal().equals(value)) {
				                 System.out.println("Putting controlled voc "+ cvoc.getNormal()+" at row="+startRow+i+"column="+startCol+j);
								tablemodel.setValueAt(cvoc,startRow+i,startCol+j);
							}
						}
						
					}*/
					if (clazz.equals(Boolean.class))
					{
						if(value.toLowerCase().equals("true") || value.toLowerCase().equals("t")|| value.toLowerCase().equals("y") || value.toLowerCase().equals("yes"))
						{
							tablemodel.setValueAt(true,startRow+i,startCol+j);
						}
						else if(value.toLowerCase().equals("false") || value.toLowerCase().equals("f")|| value.toLowerCase().equals("n") || value.toLowerCase().equals("no"))
						{
							tablemodel.setValueAt(false,startRow+i,startCol+j);
						}
						else
						{
							throw new Exception("Invalid data");
						}
					}
					else
					{
						tablemodel.setValueAt(value,startRow+i,startCol+j);
					}
	                 System.out.println("Putting "+ value+"at row="+startRow+i+"column="+startCol+j);
				}
             }
          }
       }
       catch(Exception ex){ex.printStackTrace();}

        mainTable.repaint();
        
        
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