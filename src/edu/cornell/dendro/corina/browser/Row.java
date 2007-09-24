package corina.browser;

import corina.Element;
import corina.Range;
import corina.MetadataTemplate;
import corina.Species;
import corina.UnknownSpeciesException;
import corina.ui.I18n;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.MissingResourceException;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.tree.DefaultTreeCellRenderer;

// 3 basic types of rows: samples, folders, and other files
// -- "other file" is what an element is before it's loaded.
// -- don't keep reading a non-dendro file, but also don't completely ignore a file that might get fixed elsewhere.  solution: re-read a file if the moddate changes (so i'll need to keep a moddate field here.)

public class Row {

    private File file;
    private Element element=null;
    private Browser browser;

    // use browser for (1) visible fields, and (2) matchesAny() method.
    public Row(File file, Browser browser) {
        this.file = file;
        this.browser = browser;
    }

    // new interface: Summary is responsible for loading
    public Row(Element e, Browser browser) {
	this.file = new File(e.getFilename());
	this.element = e; // don't load again!
	this.browser = browser;
    }

    public String getName() { // in File
        return file.getName();
    }
    public String getPath() { //     "
        return file.getPath();
    }
    public boolean isDirectory() { //     "
        return file.isDirectory();
    }

    // this makes bargraphs from the browser, for example, *much* more efficient
    public Element getElement() {
	return element;
    }

    private static Icon leafIcon, closedIcon, treeIcon;
    static {
	DefaultTreeCellRenderer tcr = new DefaultTreeCellRenderer();
	leafIcon = tcr.getLeafIcon();
	closedIcon = tcr.getClosedIcon();

        // tree.java does exactly the same thing ... REFACTOR
        ImageIcon tmp = new ImageIcon(Row.class.getClassLoader().getResource("Images/Tree.png"));
        int height = new JTable().getFont().getSize() + 4; // EXTRACT CONST!
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

        // WRITEME?
        return "Document";
    }

    private RelativeDate modified = null;
    private RelativeDate getModified() {
        if (modified == null)
            modified = new RelativeDate(new Date(file.lastModified()));
        return modified;
    }

    // FIXME: it'd be better to do nothing here if it's a folder, than to
    // say if (!r.isDirectory()) r.load();.
    /*
    public void load() {
        try {
            // create the element, and load its metadata
            element = new Element(file.getPath());
	    //            element.loadMeta();

	    // WORKING HERE:
	    // -- i already have a pointer to my Browser, which is good.
	    // -- the Browser, before calling any Row.load(), should have loaded a Summary
	    // -- so, my first step here is to check that summary
	    // -- if the moddate is ok, just read the meta from that
	    if (browser.summary != null) {
		for (int i=0; i<browser.summary.elements.size(); i++) {
		    Element e = (Element) browser.summary.elements.get(i);
		    if (e.filename.equals(file.getName())) {
			if (file.lastModified() <= e.lastModified) {
			    element = e;
			    return;
			} else {
			    break; // need to load newer version -- break to loadMeta() below
			}
		    }
		}
	    }

	    // if it's a non-dendro file, skip out here
	    if (browser.summary != null) {
		for (int i=0; i<browser.summary.others.size(); i++) {
		    String f = (String) browser.summary.others.get(i);
		    System.out.println("f=" + f + ", file=" + file.getName());
		    if (file.getName().equals(f)) {
			System.out.println("not loading, not a dendro file");
			return;
		    }
		}
	    }

	    // FIXME: summary should include which files aren't dendro files.
	    // right now, it has to fall back to trying .xls files again, for example.

	    // System.out.println("falling back to loadMeta() for " + file.getName());
	    element.loadMeta();

	    // -- (also add/rem'd files)
	    // -- if not, load sample normally, and tell Browser to update Summary
	    // -- when done, Browser should tell the Summary to re-save itself

            // add other stuff to the meta-map
            // -- or make my own meta map?
            // -- or use an array?
            // WRITE ME
        } catch (IOException ioe) {
            // what to do?  better dim it or something.  oh, and kind="Unknown file" or "Not a sample" now.
	    System.out.println("in row.load (" + file.getName() + "), got ioe, so it must not be a dendro file");
            element = null;
        }
    }
*/

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

        if (element != null) {
            // range, in some form?
            Range range = element.getRange(); // can't be null if element is loaded
            if (field.equals("range"))
                // i'd like this to use toStringWithSpan(), but how?  oh well.
                // (anonymous/inner class with tostring=tostringwithspan?
                return range;
            if (field.equals("start"))
                return range.getStart(); // PERF: year.tostring isn't memoed, so this involves new(), too!
            if (field.equals("end"))
                return range.getEnd(); // PERF: year.tostring isn't memoed, so this involves new(), too!
            if (field.equals("length"))
                return new Integer(range.span()); // PERF: new() called on each view!

            // if it's null, they get null
            Object value = element.details.get(field);
            if (value == null)
                return null;

            // maybe it has no translation
            boolean match = false;
	    Iterator i = MetadataTemplate.getFields();
	    while (i.hasNext()) {
		MetadataTemplate.Field f = (MetadataTemplate.Field) i.next();
                if (f.getVariable().equals(field)) {
		    // REFACTOR: looks familiar -- isField() or something?
                    match = true;
                    break;
                }
            }
            if (match)
                return value;

            // if it's a species, try looking it up with the Species object,
            // so 4-letter codes show as names.  if that doesn't work, just show the value.
            if (field.equals("species")) {
		// try {
		// return Species.getName(value.toString());
		// } catch (UnknownSpeciesException use) {
		return value.toString();
		// }
            }

	    // the comments field will often have newlines in it,
	    // which render as empty boxes; replace them with spaces
	    // or something.
	    // TODO: why only the comments field?
	    if (field.equals("comments"))
		return value.toString().replace('\n', ' ');

	    // ms-dos corina allowed "?" to mean "unspecified"
	    if (value.toString().equals("?"))
		return null;

	    // it's a valid field -- find it, and look up the string
	    MetadataTemplate.Field f = MetadataTemplate.getField(field);
	    if (f.isValidValue(value.toString()))
	        return I18n.getText("meta." + field + "." + value);

	    // it's an unknown value -- let's just spit it out
	    return value.toString();
        }

        // element = null ... something's wrong, i think.
        return null;
    }

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
