package corina.files;

import corina.Sample;
import corina.Element;

import java.io.IOException;

public class RangesOnly extends Filetype {

    // can't load a ranges-only file.  this should never get called,
    // but in case it does...
    public Sample load() throws IOException {
        throw new WrongFiletypeException();
    }

    // save
    public void save(Sample sample) throws IOException {
        // verify it's a master
        if (sample.elements == null)
            throw new IOException("Ranges-only format is only available for summed samples with Elements");

        int n = sample.elements.size();
        String eoln = System.getProperty("line.separator");
        for (int i=0; i<n; i++) {
            Element e = (Element) sample.elements.get(i);
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
            w.write(e.range.getStart().toString());
            w.write("\t");
            w.write(e.range.getEnd().toString());
            w.write(eoln);
        }

        w.close();
    }

    public String toString() {
        return "Ranges Only";
    }
}
