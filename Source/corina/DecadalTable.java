package corina;

import java.util.List;

// the view of a table
public class DecadalTable {

    private List data; // List or array?  crap, arrays aren't objects, or ... :P
    private Range range;
    
    DecadalTable(List data, Range range) {
        this.data = data;
        this.range = range;
    }
    
    public Object getValue(int x, int y) {
        if (x == 0) {
          // TODO: WRITEME
        }
        return null;
    }

    public Year getYear(int x, int y) {
        // WRITE ME
        return null;
    }

    private int minRow=0, maxRow=-1;
    public int getRowCount() {
        return (maxRow - minRow + 1);
    }

    // for empty cells; default is null (gasp)
    private Object voidObject = null;
    public void setVoidObject(Object o) {
        voidObject = o;
    }
    public Object getVoidObject() {
        return voidObject;
    }
}
