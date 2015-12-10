package org.tellervo.desktop.gui.menus.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.CopyDialog;
import org.tellervo.desktop.editor.DecadalModel;
import org.tellervo.desktop.editor.TableCoords;
import org.tellervo.desktop.editor.UnitAwareDecadalModel;
import org.tellervo.desktop.io.TwoColumn;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.PureStringWriter;
import org.tellervo.desktop.util.TextClipboard;

public class EditPasteAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	private final static Logger log = LoggerFactory.getLogger(EditPasteAction.class);
	private Clipboard system;
	
	public EditPasteAction(AbstractEditor editor) {
        super("Paste", Builder.getIcon("editpaste.png", 22));
		putValue(SHORT_DESCRIPTION, "Paste");
        putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.edit.paste")); 
        putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.edit.paste"));
		system = Toolkit.getDefaultToolkit().getSystemClipboard();

		this.editor = editor;
		
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		doPaste();
		
	}
	
	public void doPaste()
	{
		JTable table = editor.getSeriesDataMatrix().myTable;
		
		int col = table.getSelectedColumn();
		int row = table.getSelectedRow();
		
		ArrayList<Integer> values = getValuesFromClipboard();
		
		if(values==null || values.size()==0) return;
		
		
		for(Integer value : values)
		{
			if(table.isCellEditable(row, col))
			{
				table.setValueAt(value, row, col);
			}
			else
			{
				log.error("Failed");
				return;
			}
			
			TableCoords nextCell = ((DecadalModel)table.getModel()).getCoordsOfNextCell(row, col);
			row = nextCell.getRow();
			col = nextCell.getCol();
		}
		
		
		
	}

	private ArrayList<Integer> getValuesFromClipboard()
	{
		ArrayList<Integer> dataValues = new ArrayList<Integer>();
		
		 try
	        {
	           log.debug("Clipboard contents: "+system.getName());
	           String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
	           System.out.println("String is:"+trstring);
	           StringTokenizer st1=new StringTokenizer(trstring,"\n");

	           String rowstring;
	           for(int i=0;st1.hasMoreTokens();i++)
	           { 
	              rowstring = st1.nextToken();
	             
	              String[] values = rowstring.split("\t");
	              if(values.length==0)
	              {
	            	  values = rowstring.split(",");
	              }
	              if(values.length==0)
	              {
	            	  values = rowstring.split(" ");
	              }
	              
	              if(values.length==1)
	              {
	            	  Integer v = Integer.valueOf(values[0]);
	            	  dataValues.add(v);
	              }
	              else if (values.length==2)
	              {
	            	  Integer v = Integer.valueOf(values[1]);
	            	  dataValues.add(v);
	              }
	              else
	              {
	            	  log.error("Unable to extract data from clipboard");
	            	  return null;
	              }
	            	
	           }
	        }
		 	catch (Exception e)
		 {
		 		e.printStackTrace();
		 		log.error("Unable to extract data from clipboard");
		 		
		 }
		 
		 return dataValues;
		
	}
	
	

	
}
