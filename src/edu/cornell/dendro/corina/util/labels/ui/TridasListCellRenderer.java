package edu.cornell.dendro.corina.util.labels.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.schema.WSIBox;
import edu.cornell.dendro.corina.tridasv2.LabCode;

public class TridasListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 5452092569212454680L;

	public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        if(value instanceof WSIBox) {
            value = ((WSIBox) value).getTitle();
        }
        
        if(value instanceof TridasSample){
        	TridasSample s = (TridasSample) value;
                	
        	value = ((TridasSample) value).getTitle();
        }
        
        else
        {
        	System.out.println("unsupported object type in TridasListCellRenderer");
        }


        return super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
    } 
	
	
}
