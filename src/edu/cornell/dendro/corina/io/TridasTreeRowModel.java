package edu.cornell.dendro.corina.io;

import java.io.File;
import java.util.Date;

import org.netbeans.swing.outline.RowModel;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;

public class TridasTreeRowModel implements RowModel {

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            default:
                assert false;
        }
        return null;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "Action" : "Status";
    }

    @Override
    public Object getValueFor(Object node, int column) {
        ITridas f = (ITridas) node;
        switch (column) {
            case 0:
            	if(f instanceof TridasProject)
            	{
            		return "Ignore";
            	}
            	else if (f instanceof TridasObject)
            	{
            		return "Stored in database";
            	}
                return "Pending";
            case 1:
                return "OK";
            default:
                assert false;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    @Override
    public void setValueFor(Object node, int column, Object value) {
        //do nothing for now
    }
    
    
    
}