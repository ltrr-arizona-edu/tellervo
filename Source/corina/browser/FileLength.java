package corina.browser;

import java.io.File;

import java.text.DecimalFormat;

// used for sorting and displaying file lengths.
// -- interface: stores a long, toString()s it as a file length (like "34.5K")
// (i'd extend Long, if they'd let me.)
// the length of a folder is displayed as "--", and sorts as the smallest-sized file.
// javadoc me!
public class FileLength implements Comparable {
    private long length;
    public FileLength(long length) {
        this.length = length;
    }
    public FileLength(File file) {
        this.length = (file.isDirectory() ? -1 : file.length());
    }
    public int compareTo(Object o2) {
        FileLength f2 = (FileLength) o2;
        return (length < f2.length ? -1 : (length == f2.length ? 0 : +1));
    }

    // format a file's size for users, like "34.5K"
    private static final char PREFIXES[] = new char[] { 'K', 'M', 'G', 'T', 'P', 'E', };
    private static final DecimalFormat fmt = new DecimalFormat("#.#"); // 1 sig fig is plenty

    // memoized!
    private String string=null;

    private static String format(long length) {
        if (length < 0) {
            return "--"; // directories, now, have negative length
        }

        if (length < 1024) {
            return length + " bytes";
        }

        double b = (float) length;
        for (int i=0; i<PREFIXES.length; i++) {
            b /= 1024;
            if (b < 1024) {
                return fmt.format(b) + " " + PREFIXES[i] + "B";
            }
        }

        // can't happen.
        return "really, really big!";
    }

    public String toString() {
        // never called before => compute and store
        if (string == null)
            string = format(length);

        return string;
    }
}
