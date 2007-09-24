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

package edu.cornell.dendro.corina.formats;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.Element;
import edu.cornell.dendro.corina.Weiserjahre;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.List;
import java.util.ArrayList;

import java.io.StreamTokenizer;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

/**
   Mecki Pohl's original (MS-DOS) Corina format.

   <p>A Corina file contains yearly data (ring width and number of
   samples for that year), some fixed metadata, and optionally
   weiserjahre data and a listing of element samples (for summed
   samples).</p>

   <p>The title comes first, on a line by itself, followed by a blank
   line.  The title is repeated later, so this is only to make it
   easier for people or external programs (like `head -1`) to read the
   title.</p>

   <p>The metadata section comes next.  The syntax is <code>;TAG
   value</code>.  Tags are all uppercase.  Their order is fixed.  Some
   values are terminated by a newline, others by the next
   semicolon.</p>

   <p>Valid tags, and their internal names, are ID, NAME, DATING,
   UNMEAS_PRE, UNMEAS_POST, COMMENTS, COMMENTS2, TYPE, SPECIES,
   SAPWOOD, PITH, TERMINAL, CONTINUOUS, QUALITY, FORMAT, INDEX_TYPE,
   RECONCILED</p>

   On disk, they're stored all-caps; in memory, they're lower-case.
   There also exists one special case: "NAME" on disk is "title" in
   memory.

   <p>Actually, I lied; there's a second special case now.  Originally
   there was both COMMENTS and COMMENTS2 on-disk and in-memory, but
   eventually people wanted to type more than 2 lines of comments.
   The new semi-backwards-compatible way is: any number of lines of
   comments are stored in memory under simply COMMENTS (with embedded
   newlines), and on disk as COMMENTS, COMMENTS2, COMMENTS3, ... as
   needed.</p>

   The order of the tags and position of newlines is as follows:

   <pre>
   // -- sample meta section (WRITEME)
   </pre>

   // -- description of tags: index_type, ...

   <h3>Data Section</h3>

   <p>Every sample has a data section.  It starts with the line:</p>

   <pre>
   ;DATA         
   </pre>

   <p>(There are 9 spaces after the A.  Don't ask.)</p>

   WRITE ME

   <p>The data section has a few quirks:</p>

   <ul>

     <li>All files have count data, even raw samples.  Unfortunately
     the only way to tell if it's a summed sample is to check for the
     presense of Weiserjahre, Elements, or a count value greater than
     1.  When loading a Corina file, therefore, it has to load all the
     count values, and then throw them out if it turns out to not be
     summed.

     <li>The last line of count data are shifted to the left by 4
     characters.  (Again, don't ask.)

     <li>The count value for the terminating 9990 value is the same as
     the count value for the last measured year, though this has no
     real meaning.

   </ul>

   <h3>Elements Section</h3>

   <p>The Elements section is also optional; only summed samples have
   it.  It starts with the line ";ELEMENTS ".  Each line consists of a
   complete filename, preceded by a "*" if the sample is <i>not</i>
   enabled.  It ends when the next section, Weiserjahre, begins.</p>

   <h3>Weiserjahre Section</h3>

   <p>The Weiserjahre section is optional; only summed samples have it.  FINISH ME.</p>

   <h3>Author Section</h3>

   <p>The final tag in a file is the author's name.  The format is:</p>

   <pre>
   ~ author's name
   </pre>

   <p>Everything after this line is ignored, hence, the <code>~</code>
   character is not allowed anywhere in the Corina file.</p>

   <h3>Modifications</h3>

   <p>There are a couple base features that the Corina filetype can't
   handle.  I actually implement a superset of the original Corina
   filetype.  It should be 100% backwards-compatible with the original
   Corina program, though of course Corina can't take advantage of
   these features, and the extra data will be lost if it is loaded and
   re-saved in Corina.</p>

   <p>The additional features are:

   <ul>

     <li>Any number of comment lines.  The Sample class stores
     comments as one long String, possibly with newlines, and this
     class saves them as <code>;COMMENTS</code>,
     <code>;COMMENTS2</code>, <code>;COMMENTS3</code>, etc.  MS-DOS
     Corina will only recognize the first two such lines.

   </ul>

   <h3>Notes</h3>

   <p>This loader is more forgiving than (MS-DOS) Corina's.  When
   reading the metadata tags, the case of the ;-tags doesn't matter,
   nor does their order or position of newlines.</p>

   <p>This is one of the longest files in Corina, by quite a bit (40%
   bigger than all of the crossdating algorithms combined right now),
   though it's significantly shorter than it once was
   (StreamTokenizers can be more trouble than they're worth).  I'm not
   concerned about the length alone, except for the fact that it's
   this long and complex and it still doesn't do everything we
   want, hence the experiments with XML.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
 */
public class Corina implements Filetype {

	public String toString() {
		return I18n.getText("format.corina");
	}

	// read chars until a space char, or eof.
	// if EOF, throw a WFTE; otherwise, return those chars as a string.
	// (enters when the reader is just beyond the ';', leaves the
	// reader just beyond the ' ' on return)
	private String loadTag(BufferedReader r) throws IOException {
		StringBuffer name = new StringBuffer();
		int c = r.read();
		while (c != ' ') {
			if (c == -1)
				// end-of-file before ' ' occurs (otherwise no termination)
				throw new WrongFiletypeException();
			name.append((char) c);
			c = r.read();
		}
		return name.toString();
	}

	// enters with the reader just beyond the ' ' (on the first char
	// of the value), exits with the reader beyond the value (looking
	// at a ';', or EOLN, or some such);
	private void loadMeta(Sample s, BufferedReader r, String tag)
			throws IOException {
		StringBuffer buf = new StringBuffer();

		// snarf up tag name
		r.mark(1);
		char c = (char) r.read();
		while (c != '\n' && c != '\r' && c != ';') {
			buf.append(c);
			r.mark(1);
			int cc = r.read();
			if (cc == -1)
				throw new IOException(
						"end-of-file reached in middle of metadata!");
			c = (char) cc;
		}
		String value = buf.toString();

		// if read a ';', push back
		if (c == ';')
			r.reset();

		// if read a '\n' or '\r', keep reading EOLNs
		while (c == '\n' || c == '\r') {
			r.mark(1);
			c = (char) r.read();
		}
		r.reset();

		// if empty, discard
		if (value.length() == 0)
			return;

		// special cases: "PITH +" => "PITH P", "TYPE U" => "TYPE ?"
		if (tag.equals("PITH") && value.equals("+"))
			value = "P";
		if (tag.equals("TYPE") && value.equals("U"))
			value = "?";

		// try to squeeze into an Integer
		Object o;
		try {
			o = new Integer(value);
		} catch (NumberFormatException nfe) {
			o = value;
		}

		// store (tag,value) in hash
		if (tag.equals("NAME")) // ...Mecki likes "TITLE"...
			tag = "TITLE";
		if (tag.startsWith("COMMENTS")) { // append comments
			String soFar = (String) s.meta.get("comments");
			s.meta
					.put("comments", soFar == null ? value : soFar + '\n'
							+ value);
		} else { // store value
			s.meta.put(tag.toLowerCase(), o);
		}
	}

	private void loadData(Sample s, StreamTokenizer t) throws IOException {
		Year start;

		t.whitespaceChars(' ', ' '); // we can ignore whitespace here

		// assume for now that we'll need to save the count, because
		// we don't know yet.  we'll delete this when the whole load
		// is done if it turns out to be a raw sample.
		s.count = new ArrayList();

		// read start
		int x = t.nextToken();
		start = new Year((int) t.nval);
		t.pushBack();

		loop: while (true) {
			boolean stop = false;
			int decade, years;

			// read a decade of data
			x = t.nextToken();
			if (x == StreamTokenizer.TT_EOF)
				throw new IOException("End-of-file reached in data section"); // this is weird
			decade = (int) t.nval;
			years = 0;
			int indexStart = (decade % 10 < 0 ? decade % 10 + 10 : decade % 10);
			int indexStop = 10;

			for (int j = indexStart; j < indexStop; j++) {
				x = t.nextToken();
				int width = (int) t.nval;
				years++;
				if (width == 9990) {
					stop = true;
					break;
				}

				s.data.add(new Integer(width));
			}

			// read a decade of counts
			for (int j = 0; j < years; j++) { // go to previously counted amount -- ignore 9990's count?
				x = t.nextToken(); // [
				x = t.nextToken(); // count
				int count = (int) t.nval;

				if (stop && j == years - 1)
					break loop;
				x = t.nextToken(); // ]

				s.count.add(new Integer(count));
			}
		}

		t.wordChars(' ', ' '); // done

		s.range = new Range(start, s.data.size());
	}

	// in the original version of loadElements, i called r.mark(120),
	// readLine(), check for startsWith(";") -> break, then parse.
	// that assumes no filename is longer than 120 characters, which
	// is pretty long, but i'd rather not have any static
	// limitations. so i mark()/reset() every time.  it might be a
	// little slower, but correctness takes precedence!

	// -- but ken, what about the FOUS's?
	// -- Files Of Unusual Size?  i don't think they exist...

	private void loadElements(Sample s, BufferedReader r) throws IOException {
		// create elements
		s.elements = new ArrayList();

		for (;;) {
			// check first char for ';'-ness
			r.mark(2);
			char firstChar = (char) r.read();
			r.reset();
			if (firstChar == ';')
				break;

			// read line, ignoring if empty
			String line = r.readLine();
			if (line.length() == 0)
				continue;

			// add to elements
			if (line.charAt(0) == '*') // disabled element, if first char is '*'
				s.elements.add(new Element(line.substring(1), false));
			else
				s.elements.add(new Element(line));
		}
	}

	private void loadWeiserjahre(Sample s, BufferedReader r) throws IOException {
		// create wj; add 0/0
		s.incr = new ArrayList();
		s.decr = new ArrayList();
		s.incr.add(new Integer(0));
		s.decr.add(new Integer(0));

		// ";weiserjahre" is followed by some spaces -- eat them
		r.readLine();

		// read 2 lines to figure out the spacing
		r.mark(80 * 2 + 20);
		String line1 = r.readLine();
		String line2 = r.readLine();
		r.reset();

		// easy case: at least 2 years on line1, just diff 'em.
		int slash1 = line1.indexOf('/');
		int slash2 = line1.indexOf('/', slash1 + 1);
		int numDigits = 0;
		if (slash2 != -1) {
			numDigits = (slash2 - slash1 - 1) / 2;
		}

		// ok, maybe there's only 1 slash on the first line, so try the second line
		if (slash2 == -1) {
			slash1 = line2.indexOf('/');
			slash2 = line2.indexOf('/', slash1 + 1);
			if (slash2 != -1) {
				numDigits = (slash2 - slash1 - 1) / 2;
			}

			// ok, maybe there's exactly 1 slash in each of the first 2 lines.  *groan*
			if (slash2 == -1) {
				// super-special case: read inc/dec from line1, read inc/dec from line2, return.
				new Bug(
						new IllegalArgumentException(
								"d'oh, i never bothered to implement that.  go hit ken with a stick."));
			}
		}

		// number of values left to read
		int left = s.data.size() - 1;

		// now just read data until i'm done
		String line = r.readLine();
		int col = slash1;
		while (left > 0) {
			// end of line, start a new one
			if (col > line.length()) {
				line = r.readLine();
				col = slash1;
			}

			// read inc/dec
			try {
				String inc = line.substring(col - numDigits, col).trim();
				String dec = line.substring(col + 1, col + 1 + numDigits)
						.trim();
				s.incr.add(new Integer(inc));
				s.decr.add(new Integer(dec));
			} catch (StringIndexOutOfBoundsException sioobe) {
				System.out.println("line=" + line);
				System.out.println("numDigits=" + numDigits);
				System.out.println("col=" + col);
				throw new IOException("BUG in Corina.java, loadWeiserjahre()");
			}
			left--;

			// next column
			col += (slash2 - slash1);
		}
	}

	// the reader is just past the '~' character, so just trim what's
	// left of the line
	private void loadAuthor(Sample s, BufferedReader r) throws IOException {
		String author = r.readLine();
		if (author == null)
			return; // is to just end with a ~?
		s.meta.put("author", author.trim());
	}

	public Sample load(BufferedReader r) throws IOException {
		// new empty sample
		Sample s = new Sample();
		s.meta.clear();

		// make sure the first line is ok.  i don't strictly need
		// this, but it's great for checking the filetype (binary
		// files usually fail this).
		String firstLine = r.readLine();
		if (firstLine == null || firstLine.length() > 200)
			throw new WrongFiletypeException();

		// top-level tokenizer
		StreamTokenizer t = new StreamTokenizer(r);
		t.ordinaryChar(';');
		t.slashSlashComments(false);
		t.slashStarComments(false);

		/*
		 using a streamtokenizer here to read in characters is just
		 plain wrong.  just read the chars!  FIXME.

		 (loadData(t) takes the tokenizer -- but can't it be created there?)

		 - kbh, 9-jan-2002
		 */

		// top-level parse of the file
		boolean dataLoaded = false;
		for (;;) {
			int x = t.nextToken();
			if (x == ';') {
				String tag = loadTag(r);
				if (tag.equals("DATA")) {
					loadData(s, t);
					dataLoaded = true;
				} else if (tag.equals("weiserjahre"))
					loadWeiserjahre(s, r);
				else if (tag.equals("ELEMENTS"))
					loadElements(s, r);
				else
					loadMeta(s, r, tag);
			} else if (x == '~') {
				loadAuthor(s, r);
				break; // author is always last
			} else if (x != StreamTokenizer.TT_EOF) { // no metadata found in 4 lines -> problem
				if (s.meta.isEmpty() && t.lineno() > 4)
					throw new WrongFiletypeException();
			} else { // (x == StreamTokenizer.TT_EOF)
				if (t.lineno() >= 3 && s.meta.containsKey("title")) // if 3 lines + ;TITLE, it's Corina-format
					throw new IOException(
							"Early end-of-file detected (at line " + t.lineno()
									+ ")");
				else
					throw new WrongFiletypeException();
			}
		}

		// never got to a ;DATA section?  then it's not a corina file.
		if (!dataLoaded)
			throw new WrongFiletypeException();

		// if this is a raw sample (not summed), delete s.count, which
		// serves no purpose (it's a List of Integer(1)'s).  first,
		// make sure there's no WJ or elements.
		if (!s.hasWeiserjahre() && s.elements == null) {
			boolean delete = true; // ok to delete s.count?

			// then make sure the count really is all [1]'s (or [0]'s, i suppose)
			for (int i = 0; i < s.count.size(); i++)
				if (((Integer) s.count.get(i)).intValue() > 1) {
					delete = false;
					break;
				}

			// okay?  now we can delete it.
			if (delete)
				s.count = null;
		}

		// return
		return s;
	}

	// ****************************************

	// given a tag name "tag" (lower-case, as it's stored in meta), if
	// it exists in the metadata, save it as ";TAG value", else do
	// nothing.
	private void saveTag(Sample s, BufferedWriter w, String tag)
			throws IOException {
		// get value, and print it, as long as it's not null
		Object o = s.meta.get(tag);
		if (o != null)
			w.write(";" + tag.toUpperCase() + " " + o);
	}

	private void saveMeta(Sample s, BufferedWriter w) throws IOException {
		if (s.meta.containsKey("title"))
			w.write(s.meta.get("title").toString());
		w.newLine();

		w.newLine();

		// REFACTOR: except for title/name special case, this would be a lot simpler
		// as just a list: in lisp, ("id" "title" "dating" "unmeas_pre" "unmeas_post" 'newline ...)
		// (so move the special case out to saveTag?)

		saveTag(s, w, "id");
		if (s.meta.containsKey("title")) // special case (!)
			w.write(";NAME " + s.meta.get("title"));
		saveTag(s, w, "dating");
		saveTag(s, w, "unmeas_pre");
		saveTag(s, w, "unmeas_post");
		w.newLine();

		saveTag(s, w, "filename");
		w.newLine();

		saveComments(s, w);

		saveTag(s, w, "type");
		saveTag(s, w, "species");
		saveTag(s, w, "format");
		saveTag(s, w, "sapwood");
		saveTag(s, w, "pith");
		w.newLine();

		saveTag(s, w, "terminal");
		saveTag(s, w, "continuous");
		saveTag(s, w, "quality");
		w.newLine();

		saveTag(s, w, "index_type");
		saveTag(s, w, "reconciled");
		w.newLine();
	}

	// save any number of ;COMMENTS fields, for each line of text
	private void saveComments(Sample s, BufferedWriter w) throws IOException {
		if (s.meta.containsKey("comments")) {
			// split comments by lines
			// BUG: won't this fail if the comments line is a Number?
			String comments[] = StringUtils.splitByLines((String) s.meta
					.get("comments"));
			int n = 1; // the comment number, starting at 1
			for (int i = 0; i < comments.length; i++) {
				String line = comments[i];
				// if empty, skip it: old programs can't handle ";COMMENTS \n"
				if (line.length() > 0) {
					// ";COMMENTS(n)"
					String tag = ";COMMENTS";
					if (n > 1)
						tag += n;

					// write line, replacing ';' with ','
					w.write(tag + " " + line.replace(';', ','));
					w.newLine();

					// next line
					n++;
				}
			}
			/*
			 eventually, old programs will no longer be in use, so
			 ";COMMENTS \n" will be ok.  and taking out that oddity
			 makes the code simpler.  i should do that...
			 */
		}
	}

	// save the ;DATA section
	protected void saveData(Sample s, BufferedWriter w) throws IOException {
		w.write(";DATA         ");
		w.newLine();

		// clone the data, so i can add the infamous "9990".  (i can't
		// modify the data in-place, because that could cause all
		// sorts of problems, and it would be a huge mess to handle it
		// in a special case.)
		List data = new ArrayList();
		data.addAll(s.data);
		data.add(new Integer(9990));

		// clone and update the range, too
		Range range = new Range(s.range.getStart(), s.range.getEnd().add(1));

		// count for 9990 is the same as the last count, for reasons i
		// don't claim to understand.
		List count = null;
		if (s.count != null) {
			count = new ArrayList();
			count.addAll(s.count);
			count.add(count.get(count.size() - 1));
		}

		// row ends, for count info
		Year rleft = null, rright;

		// loop through years
		for (Year y = range.getStart(); y.compareTo(range.getEnd()) <= 0; y = y
				.add(+1)) {

			// year
			if (range.startOfRow(y)) {
				w.write(StringUtils.leftPad(y.toString(), 5));
				rleft = y;
			}

			// pad the first row
			if (y.equals(range.getStart()) || y.isYearOne())
				for (int i = 0; i < y.column(); i++)
					w.write("      ");

			// data: pad to 6 ("%-6d")
			w.write(StringUtils.leftPad(data.get(y.diff(range.getStart()))
					.toString(), 6));
			// (FIXME: dies if data[i] = null -- so don't let nulls get here!)

			// newline
			if (range.endOfRow(y)) {
				w.newLine();
				rright = y;

				// last row is 4 cols to the left.  don't ask why.
				w.write(rright.equals(range.getEnd()) ? "   " : "       "); // (3,7)

				// count (in brackets), for the line of data i just wrote
				if (rleft.equals(range.getStart()) || rleft.isYearOne())
					for (int i = 0; i < rleft.column(); i++)
						w.write("      "); // (6)

				if (count == null) {
					String c = StringUtils.leftPad("[1]", 6);
					for (Year y1 = rleft; y1.compareTo(rright) <= 0; y1 = y1
							.add(+1)) {
						w.write(c);
					}
				} else {
					for (Year y1 = rleft; y1.compareTo(rright) <= 0; y1 = y1
							.add(+1)) {
						String c = count.get(y1.diff(range.getStart()))
								.toString();
						c = StringUtils.leftPad("[" + c + "]", 6);
						w.write(c);
					}
				}

				w.newLine();
			}
		}

		// blank line after s.data
		w.newLine();
	}

	private void saveElements(Sample s, BufferedWriter w) throws IOException {
		if (s.elements == null)
			return;
				
		// if relative paths are on, save elements with relative paths!
		boolean relativepath = Boolean.valueOf(App.prefs.getPref("corina.dir.relativepaths")).booleanValue();

		w.write(";ELEMENTS ");
		w.newLine();
		for (int i = 0; i < s.elements.size(); i++) {
			// if disabled, write '*' before filename
			Element el = (Element) s.elements.get(i);
			w.write((el.isActive() ? "" : "*") + (relativepath ? el.getRelativeFilename() : el.getFilename()));
			w.newLine();
		}
	}

	private void saveWeiserjahre(Sample s, BufferedWriter w) throws IOException {
		// if no wj, do nothing
		if (!s.hasWeiserjahre())
			return;

		w.write(";weiserjahre   ");
		w.newLine();

		for (Year y = s.range.getStart(); y.compareTo(s.range.getEnd()) < 0; y = y
				.add(1)) {

			// year: "%5d"
			if (s.range.startOfRow(y))
				w.write(StringUtils.leftPad(y.toString(), 5));

			int i = y.diff(s.range.getStart()) + 1; // first is 0/0, so add 1 to index

			// always use '/' in corina files
			w.write(Weiserjahre.toStringFixed(s, i, 9, "/"));

			// newline
			if (y.column() == 9 || y.equals(s.range.getEnd().add(-1)))
				w.newLine();
		}
	}

	private void saveAuthor(Sample s, BufferedWriter w) throws IOException {
		w.write("~");
		String author = (String) s.meta.get("author");
		if (author != null)
			w.write(" " + author);
		w.newLine();
	}

	public void save(Sample s, BufferedWriter w) throws IOException {
		saveMeta(s, w);
		saveData(s, w);
		saveElements(s, w);
		saveWeiserjahre(s, w);
		saveAuthor(s, w);
	}

	// default extension -- well, there isn't really one...
	public String getDefaultExtension() {
		return ".RAW";
	}
}
