package corina.files;

import corina.Year;
import corina.Sample;
import corina.Element;

import java.io.IOException;

public class Spreadsheet extends Filetype {

    // can't load a spreadsheet file.  this should never get called,
    // but in case it does...
    public Sample load() throws IOException {
        throw new WrongFiletypeException();
    }

    // save
    public void save(Sample sample) throws IOException {
        // verify it's a master
        if (sample.elements == null)
            throw new IOException("Spreadsheet format is only available for summed samples with Elements");

        // number of elements
        int n = sample.elements.size();

        // load all the elements into a buffer
        Sample buf[] = new Sample[n];
        for (int i=0; i<n; i++)
            try {
                buf[i] = ((Element) sample.elements.get(i)).load();
            } catch (IOException ioe) {
                throw new IOException("Can't load element " + ((Element) sample.elements.get(i)).getFilename());
            }

        // write header
        String eoln = System.getProperty("line.separator");
        w.write("Year");
        for (int i=0; i<n; i++)
            w.write("\t" + buf[i].meta.get("title"));
        w.write(eoln);

        // save line-at-a-time
        for (Year y=sample.range.getStart(); y.compareTo(sample.range.getEnd())<=0; y=y.add(1)) {
            // write year
            w.write(y.toString());

            // write each datum for this year
            for (int i=0; i<n; i++) {
                // tab
                w.write("\t");

                // data[y]
                if (buf[i].range.contains(y)) {
                    Year start = buf[i].range.getStart();
                    int index = y.diff(start);
                    w.write(buf[i].data.get(index).toString());
                }
            }

            // end-of-line
            w.write(eoln);
        }
        
        w.close();
    }

    public String toString() {
        return "Spreadsheet"; // i18n!
    }
}
