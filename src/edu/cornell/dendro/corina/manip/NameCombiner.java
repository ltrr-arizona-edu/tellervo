package edu.cornell.dendro.corina.manip;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
   Combines a group of strings intelligently.

   <h2>Left to do</h2>
   <ul>
     <li>Write!
     <li>GPL header
     <li>Javadoc
     <li>Unit tests -- put in main() now, then move to UnitTests.java (e.g., ("ZKB 1" "ZKB 2") =&gt; "ZKB 1,2")
     <li>Integrate with browser, etc.
   </ul>
*/
public class NameCombiner {

    private static class Name {
        String name; // e.g., "ZKB"
        List numbers; // Integer(1), Integer(2), etc.

        // e.g., "ZKB 1,2"
        public String toString() {
            // sort numbers
            Collections.sort(numbers);

            // for each run of 3 or more, write it as "3-5".
            // for single numbers, or just 2 in a row, separate by commas ("9,10,15").
            int i = 0;
            return null; // WRITEME
        }
    }

    // once Name.toString() is done, what needs doing?
    // -- sort by names
    // -- for each name, extract all numbers
    // -- basically, assemble a list of Names -- need Name(String) c'tor?
    // -- then toString() each one, with ", " in between them.
    // in other words, not much.

    public static String combine(List names) {
        return "--";
    }

    // RFE: pick an intelligent name
    // -- if the files are "xyz1.ext", "xyz2.ext", and "xyz3.ext", call it "xyz 1-3".  (duh.)
    // -- first, ignore extension.  result extension will be ".sum" (for now)
    // -- next, sort them (natural ordering, ignore case)
    // -- look for common prefix; if complete (i.e., "zkb111"+"zkb222", but not "zkb111"+"zyb111")
    // -- for each block of common prefixes, then look at the numbers
    // -- for each block of consecutive numbers, replace with "x-y"
    // -- smash all remaining number blocks together with ","
    // -- smash all remaining prefix blocks together with ","
    // -- result: something like "ZKB 1-3,4, ZYB 5,7,9-13" [.SUM]

    public static void main(String args[]) {
        List names = new ArrayList();
        for (int i=0; i<args.length; i++)
            names.add(args[i]);

        System.out.println(combine(names));
    }
}
