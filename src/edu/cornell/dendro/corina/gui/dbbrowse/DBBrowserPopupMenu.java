package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;

public class DBBrowserPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	public DBBrowserPopupMenu(final Element element, final DBBrowser browser) {
		JMenuItem item;
		
		item = Builder.makeMenuItem("open", true, "fileopen.png");
		add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Sample s = element.load();
					new Editor(s);
				} catch (IOException e1) {
					Alert.errorLoading(element.getShortName(), e1);
				} 
			}
		});
		
		item = Builder.makeMenuItem("graph", true, "graph.png");
		add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Sample s = element.load();
					new GraphWindow(s);
				} catch (IOException e1) {
					Alert.errorLoading(element.getShortName(), e1);
				} 
			}
		});

		BaseSample bs;
		try {
			bs = element.loadBasic();
		} catch (IOException ioe) {
			// shouldn't happen?
			return;
		}

		// direct number of children
		Integer directChildCount = bs.getMeta(Metadata.CHILD_COUNT, Integer.class);
		boolean canDelete = (element.isDeletable() && (directChildCount == null || directChildCount == 0));
		
		item = Builder.makeMenuItem("delete", canDelete, "delete.png");
		add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					element.delete();
					browser.deleteElement(element);
				} catch (IOException ioe) {
					Alert.error("Cannot delete", "Unable to delete: " + ioe.getMessage());
				}
			}
		});

	}
}
