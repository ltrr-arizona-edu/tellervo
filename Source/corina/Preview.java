/*
  so far, only sample and grid implement previewable
*/

package corina;

import java.util.List;

/*
   A preview consists of a title, and any number of bulleted items.
*/

public class Preview {

    // hmm, not thrilled about the "public" stuff

    public String title;
    // subtitle, too?  (grid could use it, for n=...)

    public List items; // of String

}
