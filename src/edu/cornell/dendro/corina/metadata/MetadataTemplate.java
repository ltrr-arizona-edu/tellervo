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

package edu.cornell.dendro.corina.metadata;

import java.util.HashMap;
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
	private MetadataTemplate() {
	}

	// metadata fields -- order matters!
	// Order matters because we generate our metadata editing dialog in this order...
	private static final MetadataField FIELDS[] = {
		
		// variable, editable?, [values | lines]
		// -- (description, help-text, and value-names are in
		// bundle)
		new MetadataField("title", true),
		new MetadataField("id", true),
		new MetadataField("dating", true, "R,A"),
		new MetadataField("unmeas_pre", true),
		new MetadataField("unmeas_post", true),
		new DictionaryMetadataField("type", "SpecimenType", true), //new MetadataField("type", true, "C,H,S"),
		new MetadataField("species", true),
		new MetadataField("format", true, "R,I"),
		new MetadataField("index_type", false), // why not "-1,1,2,3,..."?  BUG with that + readonly=true
		new MetadataField("sapwood", true), 
		new DictionaryMetadataField("pith", "Pith", true), //new MetadataField("pith", true, "P,*,N"),
		new DictionaryMetadataField("terminal", "TerminalRing", true), //new MetadataField("terminal", true, "B,W,v,vv"),
		new DictionaryMetadataField("continuous", "SpecimenContinuity", true), //new MetadataField("continuous", true, "C,R,N"),
		new DictionaryMetadataField("quality", "SpecimenQuality", true), //new MetadataField("quality", true, "+,++"),
		new MetadataField("reconciled", true, "Y,N"), 
		new MetadataField("author", false),
		new MetadataField("comments", true, 4), 
	};
	
	private static HashMap<String, MetadataField> fieldMap;
	
	// set up our fieldmap
	static {
		fieldMap = new HashMap<String, MetadataField>();
		
		Iterator<MetadataField> i = getFields();
		while (i.hasNext()) {
			MetadataField f = i.next();
			fieldMap.put(f.getVariable(), f);
		}		
	}

	/**
	 Return all of the fields, in order.

	 @return an Iterator which lists all of the metadata fields
	 */
	public static Iterator<MetadataField> getFields() {
		return new Iterator<MetadataField>() {
			private int i = 0;

			public boolean hasNext() {
				return (i < FIELDS.length);
			}

			public MetadataField next() {
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
	 * Return true if the given string is the name of a field, like "species".
	 * 
	 * @param f a string to check
	 * @return true, iff it's the name of a metadata field
	 */
	public static boolean isField(String field) {
		/*
		if (field == null)
			return false;

		Iterator i = getFields();
		while (i.hasNext()) {
			MetadataField f = (MetadataField) i.next();
			if (f.getVariable().equals(field))
				return true;
		}

		return false;
		*/
		
		return fieldMap.containsKey(field);
	}

	/**
	 Get a Field, given its key.

	 @param key the key name to look up
	 @return the Field which has that key
	 @exception ???
	 */
	public static MetadataField getField(String field) {
		MetadataField f = fieldMap.get(field);
		
		if(f == null)
			throw new NullPointerException(); // apparently, this shouldn't happen
		
		return f;
		
		/*
		Iterator i = getFields();
		while (i.hasNext()) {
			MetadataField f = (MetadataField) i.next();
			if (f.getVariable().equals(field))
				return f;
		}
				throw new NullPointerException(); // ???

		*/

	}
}