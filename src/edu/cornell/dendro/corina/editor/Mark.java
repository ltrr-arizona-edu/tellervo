package corina.editor;

import corina.ui.Builder;

import javax.swing.Icon;

public class Mark {

    /*
      WRITEME!

      a Mark should have:
      -- (class) a way of listing all the marks
      -- (class) a way of constructing a mark, given its serialization

      -- an icon
      -- a serialization, probably a unicode character (or string)
      -- a way to draw itself on a cell, probably either above, to the side, or around the number
    */

    public Mark(char unicode, Icon icon) {
	this.unicode = unicode;
	this.icon = icon;
    }

    Icon icon;
    char unicode;

    public static Mark defaults[] = new Mark[] {
	new Mark('\u2191', Builder.getIcon("up-arrow.png")),
	new Mark('\u2192', Builder.getIcon("down-arrow.png")),
	new Mark('\u2219', Builder.getIcon("1-dot.png")),
	new Mark('\u2236', Builder.getIcon("2-dots.png")),
	new Mark('\u22EE', Builder.getIcon("3-dots.png")),
	new Mark('\u25CB', Builder.getIcon("outline.png")),
	new Mark('\u25A2', Builder.getIcon("box.png")),
	new Mark('_', Builder.getIcon("underline.png")),
    };
}
