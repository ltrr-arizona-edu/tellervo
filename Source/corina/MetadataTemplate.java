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

import corina.util.StringUtils;
import corina.ui.I18n;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
   A list of metadata fields for a Sample.

   <p>This class is never instantiated; only
   <code>MetadataTemplate.getFields()</code> is ever called, to get a
   List of MetadataTemplate.Fields.  A metadata Field is a:</p>

   <ul>
     <li>key (variable name), like "dating"
     <li>description, localized, like "Dating" or "Datierung"
     <li>suggested values, like { "R" or "A" }
     <li>read-only flag
   </ul>

   <p>This class should be user wherever the entire list of metadata
   fields for a Sample are listed for editing or display.</p>

   <p>Fields get their localized strings from the I18n class.
   The keys take the form
<pre>
   meta.&lt;variable&gt; = &lt;name&gt;
</pre>
   and
<pre>
   meta.&lt;variable&gt;.&lt;value&gt; = &lt;name&gt;
</pre>
   for example, the "dating" field in English would be:
<pre>
   meta.dating = Dating
   meta.dating.R = Relative
   meta.dating.A = Absolute
</pre>
   (Free-form fields which don't have a fixed number of possible
   values, of course, only need the name string.)</p>

   <p>The metadata variables are: id, title, dating, unmeas_pre,
   unmeas_post, type, species, format, index_type, sapwood, pith,
   terminal, continuous, quality, reconciled, author, comments.</p>

   <h2>Left to do</h2>
   <ul>
     <li>add feature: localize(field, value) => string.
         for example, localize(dating, "R") => "Relative" (in english)

     <li>no: make all i18n the job of the user.  remove description
         altogether.  the user can say I18n.getText("meta." + var)
	 or I18n.getText("meta." + var + "." + value) -- actually,
	 these should have shortcuts here.
	 - MetadataTemplate.getDescription(var)
	 - MetadataTemplate.getValueDescription(var, value)

     <li>why's it not instantiable?  sometimes i think
         mt = new MetadataTemplate();
	 // iterate over mt.fields...
	 would be really nice.

     <li>refactor metadata-field-popup component?  (used twice:
         MetadataPanel, SearchDialog)

     <li>make Field immutable: need read-only interface for |values|

     <li>document Field fully
     <li>move documentation of Field's fields to Metadata?
         (if i make it instantiable, this becomes a non-issue)

     <li>change public interface from "readonly" to "editable"?

     <li>"Filetype" is a hack -- make it a real field

     <li>document: list the keys+values here (currently in Sample?)
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class MetadataTemplate {
    // don't instantiate me
    private MetadataTemplate() { }

    // metadata fields -- order matters!
    private static final Field FIELDS[] = {
	// variable, editable?, [values | lines]
	// -- (description, help-text, and value-names are in
	// bundle)
	new Field("title",       true),
	new Field("id",          true),
	new Field("dating",      true, "R,A"),
	new Field("unmeas_pre",  true),
	new Field("unmeas_post", true),
	new Field("type",        true, "C,H,S"),
	new Field("species",     true),
	new Field("format",      true, "R,I"),
	new Field("index_type",  false), // why not "-1,1,2,3,..."?  BUG with that + readonly=true
	new Field("sapwood",     true),
	new Field("pith",        true, "P,*,N"),
	new Field("terminal",    true, "B,W,v,vv"),
	new Field("continuous",  true, "C,R,N"),
	new Field("quality",     true, "+,++"),
	new Field("reconciled",  true, "Y,N"),
	new Field("author",      false),
	new Field("comments",    true, 4),
    };

    /**
       Return all of the fields, in order.

       @return an Iterator which lists all of the metadata fields
    */
    public static Iterator getFields() {
	return new Iterator() {
		private int i = 0;
		public boolean hasNext() {
		    return (i < FIELDS.length);
		}
		public Object next() {
		    if (hasNext())
			return FIELDS[i++];
		    else
			throw new NoSuchElementException();
		}
		public void remove() {
		    throw new UnsupportedOperationException();
		}
	    };
    }

    /**
        A variable record, which holds a (variable => description,
        values, readonly) mapping.
        
        <p>A metadata field is defined by:</p>
        
        <dl>
            <dt>variable (like "dating")
            <dd>the key to store this type of data in the metadata hash table

            <dt>description (like "Dating")
            <dd>a short human-readable description for displaying
                in forms and table headers

            <dt>values (like ["R","A"])
            <dd>WRITEME ("suggested values, if choice, else null")

            <dt>readonly (like <i>false</i>)
            <dd>WRITEME ("is this field read-only?")

            <dt>lines (like <i>1</i>)
            <dd>WRITEME ("number of lines to display")
        </dl>
    */
    public static class Field {

	public Field(String variable,
		     boolean editable) {
	    this.variable = variable;
	    // TODO: this'll get refactored into Field(...)! -- why?
	    this.readonly = !editable;

	    // set description, from |m|
            this.description = I18n.getText("meta." + variable);
	}
        
	public Field(String variable,
		     boolean editable,
		     int lines) {
	    this(variable, editable);
	    this.lines = lines;
	    this.values = null;
	}

	public Field(String variable,
		     boolean editable,
		     String values) {
	    this(variable, editable);
	    this.lines = 1;

	    this.values = StringUtils.splitBy(values, ',');
	}

        private String variable="";
        public String getVariable() {
            return variable;
        }

        private String description="";
	public String getDescription() {
	    return description;
	}
        
	// document: null means it's free-form!  (do i need a 'type', too?)
	// TYPES: string, one-of, string (multiple lines), label (ro!).
	// subclass?
	// -- StringField
	// -- ChoiceField (getChoices(), etc.)
	// -- TextField (just StringField with lines != 1?)
	// -- LabelField (just like StringField, but users can't edit)
        public String values[]=null;
	// TODO: give this a nice public interface
	// used by: DataComponent, MetadataPanel, ElementsPanel
	public boolean isValidValue(String value) {
	    for (int i=0; i<values.length; i++)
		if (values[i].equals(value))
		    return true;
	    return false;
	}
        
        private boolean readonly=false;
        public boolean isReadOnly() {
            return readonly;
        }
        
        private int lines=1;
	public int getLines() {
	    return lines;
	}

        // TO ADD: "columns".  ("sapwood count" doesn't need to be as
        // wide as "title" or "species")

        // TO ADD: numbers-only? (types)

        // store type information?

	// awt/swing sometimes lets you use objects directly,
	// and use the toString() value as their label.
	// so let's provide that.
        public String toString() {
            return description;
        }
    }

    /**
        Return true if the given string is the name of a field, like
        "species".

	@param f a string to check
	@return true, iff it's the name of a metadata field
    */
    public static boolean isField(String field) {
	if (field == null)
            return false;

	Iterator i = getFields();
	while (i.hasNext()) {
	    Field f = (Field) i.next();
	    if (f.variable.equals(field))
		return true;
	}

	return false;
    }

    /**
       Get a Field, given its key.

       @param key the key name to look up
       @return the Field which has that key
       @exception ???
    */
    public static Field getField(String field) {
	Iterator i = getFields();
	while (i.hasNext()) {
	    Field f = (Field) i.next();
	    if (f.variable.equals(field))
		return f;
	}

	throw new NullPointerException(); // ???
    }
}
