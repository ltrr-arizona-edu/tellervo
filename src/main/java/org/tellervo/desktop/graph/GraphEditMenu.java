/**
 * 
 */
package org.tellervo.desktop.graph;

import javax.swing.JFrame;

import org.tellervo.desktop.gui.menus.EditMenu;


@SuppressWarnings("serial")
class GraphEditMenu extends EditMenu {
	
	public GraphEditMenu(JFrame frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}


	/*
	 * -- DISABLED until i'm sure it works: copy svg to clipboard protected
	 * void addCopy() { JMenuItem copy = Builder.makeMenuItem("copy");
	 * copy.addActionListener(new AbstractAction() { public void
	 * actionPerformed(ActionEvent e) { window.copyToClipboard(); } });
	 * this.add(copy); }
	 */
}