package corina.files;

import corina.Sample;
import corina.Element;
import corina.ui.I18n;

import java.io.IOException;

// TODO: i18n me

/** For a sum, output the name and range of each element, one per line,
    with columns separated by tabs, and elements separated by newlines.
    For example:

<pre>
Sample	Start	End	Span
ZKB-1   1001    1236    236
ZKB-2   1003    1072    70
ZKB-3   1011    1099    89
</pre>

   (Useful for dumping into spreadsheets, or generating bar graphs.)

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/
public class RangesOnly extends Filetype {

    /** Throws a WrongFiletypeException - RangesOnly files can't be loaded.
	(This should never get called, but just in case.)
	@exception WrongFiletypeException always thrown
    */
    public Sample load() throws IOException {
        throw new WrongFiletypeException();
    }

    /** Save the sample in range-only format.
	@param sample the sample to save
	@exception IOException if there's any problem writing it, or reading an element
    */
    public void save(Sample sample) throws IOException {
        // verify it's a master
        if (sample.elements == null)
            throw new IOException("Ranges-only format is only available for summed samples with Elements");

        String eoln = System.getProperty("line.separator");

	// write a header
	w.write("Sample");
	w.write("\t");
	w.write("Start");
	w.write("\t");
	w.write("End");
	w.write("\t");
	w.write("Span");
	w.write(eoln);

        int n = sample.elements.size();
        for (int i=0; i<n; i++) {
            Element e = (Element) sample.elements.get(i);
	    // OBSOLETE: getRange(), etc., now load the file automatically.
	    // but BUG: they don't throw anything if it fails.
	    // (and i don't have a getMeta() yet, so it's not unnecessary.)
            try {
                e.loadMeta(); // this aborts if element can't be loaded.
            } catch (IOException ioe) {
                throw new IOException("Can't load element " + e.getFilename());
            }

            // output (name, start, end)
            if (e.details.containsKey("title"))
                w.write(e.details.get("title").toString());
            else
                w.write(e.details.get("filename").toString());
            w.write("\t");
            w.write(e.getRange().getStart().toString());
            w.write("\t");
            w.write(e.getRange().getEnd().toString());
            w.write("\t");
            w.write(String.valueOf(e.getRange().span()));
            w.write(eoln);
        }

        w.close();
    }

    /** Return the name of this format: "Ranges Only" (in your favorite language).
	(Internationalization property is "ranges_only".)
	@return the name of this format
    */
    public String toString() {
        return I18n.getText("ranges_only");
    }
}
