package corina.browser;

import corina.Element;
import corina.Range;
import corina.Metadata;
import corina.files.ExportDialog; // for formatsize()

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import java.awt.Image;
import javax.swing.tree.DefaultTreeCellRenderer;

// 3 basic types of rows: elements, folders, and non-dendro files
// -- a "non-dendro file" is what an element is before it's loaded.
// -- don't keep reading a non-dendro file, but also don't completely ignore a file that might get fixed elsewhere.  solution: re-read a file if the moddate changes (so i'll need to keep a moddate field here.)

public class Row {

    private File file;
    private Element element=null;
    private Browser browser;
    
    public Row(File file, Browser browser) {
        this.file = file;
        this.browser = browser;
    }

    public String getName() { // in File
        return file.getName();
    }
    public String getPath() { //     "
        return file.getPath();
    }
    public boolean isDirectory() {
        return file.isDirectory();
    }

    private static Icon leafIcon = new DefaultTreeCellRenderer().getLeafIcon();
    private static Icon closedIcon = new DefaultTreeCellRenderer().getClosedIcon();
    private static Icon treeIcon;
    static {
        // tree.java does exactly the same thing ... refactor
        ImageIcon tmp = new ImageIcon(Row.class.getClassLoader().getResource("Images/Tree.png"));
        int height = new JTable().getFont().getSize() + 4;
        treeIcon = new ImageIcon(tmp.getImage().getScaledInstance(height, height, Image.SCALE_SMOOTH));
    }
    public Icon getIcon() {
        if (file.isDirectory())
            return closedIcon;
        if (element == null)
            return leafIcon;
        else
            return treeIcon;
    }

    private FileLength size=null;
    private FileLength getSize() { // in File -- but override
        if (size == null)
            size = new FileLength(file);
        return size;
    }

    public String getKind() {
        if (file.isDirectory())
            return "Folder";
        
        // WRITEME
        return "Document";
    }

    private RelativeDate modified = null;
    private RelativeDate getModified() {
        if (modified == null)
            modified = new RelativeDate(new Date(file.lastModified()));
        return modified;
    }

    public void load() {
        try {
            // create the element, and load its metadata
            element = new Element(file.getPath());
            element.loadMeta();

            // add other stuff to the meta-map
            // -- or make my own meta map?
            // -- or use an array?
            // WRITE ME
        } catch (IOException ioe) {
            // what to do?  better dim it or something.  oh, and kind="Unknown file" or "Not a sample" now.
            element = null;
        }
    }

    // valid fields are:
    // -- any metadata field
    // -- "name", "size", "kind", "modified"
    // -- "range", "start", "end", "length"
    // how about just making an array(list) of all of these?  the metadata fields exist in a fixed order,
    // and then this method is just a table lookup.  plus sorting is a snap.
    // -- IT COULD BE A LOT FASTER, TOO.
    public Object getField(String field) {
        // file metadata
        if (field.equals("name"))
            return getName();
        if (field.equals("size"))
            return getSize();
        if (field.equals("kind"))
            return getKind();
        if (field.equals("modified"))
            return getModified(); // AsString(); // should return the Date -- a RelativeDate, actually.

        // 

        if (element != null) {
            // range, in some form?
            Range range = element.range; // can't be null if element is loaded
            if (field.equals("range"))
                // i'd like this to use toStringWithSpan(), but how?  oh well.
                // (anonymous/inner class with tostring=tostringwithspan?
                return range;
            if (field.equals("start"))
                return range.getStart();
            if (field.equals("end"))
                return range.getEnd();
            if (field.equals("length"))
                return new Integer(range.span());
                //                return "n=" + range.span(); // BUG: if you return a String here, you can't sort by it.
                // anonymous/inner class wraps Integer, but with toString() { return "n=" + n; }?

            // if it's null, they get null
            Object value = element.details.get(field);
            if (value == null)
                return null;

            // maybe it has no translation
            boolean match = false;
            for (int i=0; i<Metadata.fields.length; i++) {
                if (Metadata.fields[i].variable.equals(field)) {
                    match = true;
                    break;
                }
            }
            if (!match)
                return value;

            // FIXME: if it's a species, try looking it up with the Species object, so codes show as names.
            
            // ok, it must be in there.  look it up
            try {
                return msg.getString(field + "." + value);
            } catch (MissingResourceException mre) {
                // maybe it's a '?' -- (this check is also in samplemetaview, i think -- refactor?)
                if (value.toString().equals("?"))
                    return null;

                // FIXME: the comments field will usually have newlines in it,
                // which render as empty boxes; replace them with | or something clever.
                
                // bad value, but they gave it before, so we'll give it back now.
                return value.toString();
            }
        }

        // uhhh...  this sucks, beavis.
        return null;
    }

    private static ResourceBundle msg = ResourceBundle.getBundle("MetadataBundle");
    
    // true if this file matches all of the words, with any of its visible fields.
    // (how am i to know what's visible here?)  (pass in the list of visible fields, too?)
    // no, better: if i pass a reference to the list-of-visible-fields with the constructor,
    // with the price of exactly one reference, i ... oh, wait, wrong concept.
    // i'm thinking i can change one field, implement compareTo() here, and it'll always work.
    // i'd need a reference back to the Browser, and Browser.getSortField().
    
    public boolean matches(String words[]) {
        // foreach field f in visiblefields[] -- element.meta.values()?

        // task: need to match across fields.
        // strategy: append all fields together, with spaces between them.
        
        StringBuffer buf = new StringBuffer();

        // use name, modified, kind.  (don't use size, or any range view.)
        buf.append(getName() + " ");
        buf.append(getModified() + " ");
        buf.append(getField("kind") + " ");

        // only look at visible fields
        // BUG: this lists all fields, not just metadata fields -- range, etc.
        // -- this is bad because: i already put in name,mod,kind, so they get searched twice, but
        // more importantly: this searches RANGE, etc., which i specifically don't want to search.
        List visible = browser.getVisibleFields();
        for (int i=0; i<visible.size(); i++) {
            Object value = getField((String) visible.get(i));
            if (value == null)
                continue;
            if (value instanceof Number || value instanceof Range) // skip all numbers and ranges
                continue;
            buf.append(value + " ");
        }

        return Browser.matchesAny(buf.toString(), words);
    }

    // why didn't i just overload File?
}
