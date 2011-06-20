package edu.cornell.dendro.corina.sources;

import edu.cornell.dendro.corina.ui.Builder;
import javax.swing.Icon;

public class FavoritesSource implements Source {

    private String name="Favorites";

    // testing
    FavoritesSource(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public Icon getIcon() {
	return Builder.getIcon("bookmark-toolbar.png", 22);
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
