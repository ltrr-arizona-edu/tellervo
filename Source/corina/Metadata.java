//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
   <p>A list of metadata fields for a Sample.</p>

   <p>This class is never instantiated; only
   <code>Metadata.getFields()</code> is ever called, to get a List of
   Metadata.Fields.  A Field is a mapping of:</p>

   <ul>
     <li>variable (Map key)
     <li>description
     <li>help (tooltip)
     <li>suggested values
     <li>read-only flag
   </ul>

   <p>Currently, this class is used by <code>gui.MetaPanel</code> and
   <code>format.HTML</code>.  It should be user wherever the entire
   list of metadata for a Sample are listed for editing or
   display.</p>

   <p>This class gets the information from a properties file.  Such a
   file might look similar to this:</p>

   <pre>
   id.name = ID

   title.name = Title

   dating.name = Dating
   dating.help = Is the sample relatively (R) or absolutely (A) dated?
   dating.options = R,A

   author.name = Author
   author.readonly = true
   </pre>

   <p>The metadata variables are: id, title, dating, unmeas_pre,
   unmeas_post, type, species, format, index_type, sapwood, pith,
   terminal, continuous, quality, reconciled, author, comments.</p>

   <p>This file may be localized by changing the .name values in a
   copy of the MetadataBundle file.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Metadata {

    // metadata fields -- order matters!
    private static final String FIELD_LIST[] = {
	"title",
	"id",
	"dating",
	"unmeas_pre",
	"unmeas_post",
	"type",
	"species",
	"format",
	"index_type",
	"sapwood",
	"pith",
	"terminal",
	"continuous",
	"quality",
	"reconciled",
	"author",
	"comments",
    };

    /* this class is to be used for *all* user-visible (meta)data
       fields; Element loadMeta() details list only *some
       user-selected* fields, hence this class cannot be used for
       that.  (what did i mean by that?  was i high?) */

    // list of fields
    public static Field fields[];

    // list of preview fields
    public static Field preview[];

    /** A variable record, which holds a (variable => description,
        values, readonly) mapping. */
    public static class Field {
        public String variable; // variable name -- hashtable key
        public String description; // user-readable description
        public String help; // help sentence (tooltip).
        public String values[]; // suggested values, if choice, else null
        public boolean readonly; // is this field read-only?
        public boolean preview; // is this field one of the preview fields?
        public int lines; // number of lines to display

        // store type information?

        public String toString() { // for GUI elements to use directly
            return description;
        }
    }

    // load fields from resource bundle, on class load
    static {
        // place to store fields
        fields = new Field[FIELD_LIST.length];

        // we'll count how many to put into preview
        int numPreview = 0;

        // open bundle, and read vars/order key
        ResourceBundle m = ResourceBundle.getBundle("MetadataBundle");

        for (int i=0; i<FIELD_LIST.length; i++) {
            // for the key, construct a field = (name, help, ...) tuple
            String key = FIELD_LIST[i];
            Field f = new Field();
            f.variable = key;
            f.description = m.getString(key + ".name");
            try {
                f.help = m.getString(key + ".help");
            } catch (MissingResourceException mre) {
                f.help = null;
            }
            try {
                f.readonly = Boolean.valueOf(m.getString(key + ".readonly")).booleanValue();
            } catch (MissingResourceException mre) {
                f.readonly = false;
            }
            try {
                f.preview = Boolean.valueOf(m.getString(key + ".preview")).booleanValue();
            } catch (MissingResourceException mre) {
                f.preview = false;
            }
            try {
                f.lines = Integer.valueOf(m.getString(key + ".lines")).intValue();
            } catch (MissingResourceException mre) {
                f.lines = 1;
            }

            // values are going to be of the form "A,B,C" -- parse this
            try {
                List tmp = new ArrayList();
                String v = m.getString(key + ".options");
                int start = 0;
                for (;;) {
                    int nextComma = v.indexOf(',', start);
                    if (nextComma == -1)
                        break;
                    tmp.add(v.substring(start, nextComma));
                    start = nextComma + 1;
                }
                tmp.add(v.substring(start));
                f.values = (String[]) tmp.toArray(new String[0]);
            } catch (MissingResourceException mre) {
                f.values = null;
            }

            // add field to list(s)
            fields[i] = f;
            if (f.preview)
                numPreview++;
        }

        // add some to preview list
        preview = new Field[numPreview];
        int n=0;
        for (int i=0; i<fields.length; i++)
            if (fields[i].preview)
                preview[n++] = fields[i];
    }
}
