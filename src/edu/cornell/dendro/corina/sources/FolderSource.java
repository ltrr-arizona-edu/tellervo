package edu.cornell.dendro.corina.sources;

import javax.swing.Icon;
import javax.swing.tree.DefaultTreeCellRenderer;

/*
  notes:
  -- this class will be responsible for caching files
  ---- (ftpsource even more so -- though differently)
*/

public class FolderSource implements Source {

    private String name="Library"; // like "Library"

    public String getName() {
	return name;
    }

    private static Icon closedIcon;
    static {
        DefaultTreeCellRenderer dtcr = new DefaultTreeCellRenderer();
        closedIcon = dtcr.getClosedIcon();
    }
    public Icon getIcon() {
	return closedIcon;
    }

    public boolean canAcceptDrop() {
        return true;
    }
    public boolean canBeDragged() {
        return true;
    }
    public boolean canElementsBeDragged() {
        return true;
    }
}
