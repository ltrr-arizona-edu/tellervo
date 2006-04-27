/**
 * 
 */
package corina.graph;

import corina.gui.menus.EditMenu;

class GraphEditMenu extends EditMenu {
	GraphEditMenu(GraphWindow window) {
		this.window = window;
	}

	private GraphWindow window;

	/*
	 * -- DISABLED until i'm sure it works: copy svg to clipboard protected
	 * void addCopy() { JMenuItem copy = Builder.makeMenuItem("copy");
	 * copy.addActionListener(new AbstractAction() { public void
	 * actionPerformed(ActionEvent e) { window.copyToClipboard(); } });
	 * this.add(copy); }
	 */
}