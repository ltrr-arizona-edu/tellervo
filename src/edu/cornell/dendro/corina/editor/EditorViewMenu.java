package edu.cornell.dendro.corina.editor;

import javax.swing.JMenu;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;

public class EditorViewMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	/*
      this should be:

      View
      ----
      Data        ^1
      Metadata    ^2
      Weiserjahre ^3
      Elements    ^4
      ---
      Elements by Filenames
      Elements by Summary Fields
      Elements by All Fields
      ---
      * Show Count as Histogram
      Show Count as Numbers
      ---
      Font >
      Size >
      (Style >)
      Text Color>
      Background Color>
      Show/Hide Gridlines

      (wj,elements dimmed if not present)
      (text/*, gridlines, and which meta view are global, and get saved!)

      (global means i'll need more references like open-recent.
      abstract that out somehow?  GlobalMenu?  GlobalMenuItem?)
    */

    public EditorViewMenu(Sample sample) {
    	super(I18n.getText("view")); // TODO: mnemonic!
    }
}
