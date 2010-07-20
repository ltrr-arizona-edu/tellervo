package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasDerivedSeries;

import edu.cornell.dendro.corina.cross.CrossdateDialog;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;

public class ElementListPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	public ElementListPopupMenu(final Element element, final ElementListManager browser) {
		JMenuItem item;
		

		
		item = Builder.makeMenuItem("menus.file.open", true, "fileopen.png");
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
			bs = element.load();
		} catch (IOException ioe) {
			// shouldn't happen?
			return;
		}
		
		ITridasSeries series = bs.getSeries();
		if( series instanceof TridasDerivedSeries)
		{
			final TridasDerivedSeries ds = (TridasDerivedSeries) bs.getSeries();
			if(ds.getType().getValue().equalsIgnoreCase("Crossdate"))
			{
				item = Builder.makeMenuItem("crossdate.reviewCrossdate", true, "crossdate.png");
				add(item);
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {			
					
						new CrossdateDialog(new JFrame(), element);

					}
				});
			}
		}
		
		// direct number of children
		Integer directChildCount = bs.getMeta(Metadata.CHILD_COUNT, Integer.class);
		boolean canDelete = (element.isDeletable() && (directChildCount == null || directChildCount == 0));
		
		// Delete 
		addSeparator();
		item = Builder.makeMenuItem("general.delete", canDelete, "delete.png");
		add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				
					Object[] options = {"OK",
					                    "Cancel"};
					int ret = JOptionPane.showOptionDialog(getParent(), "Are you sure you want to delete this series?", "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						
					if(ret == JOptionPane.YES_OPTION)
					{
						element.delete();
						browser.deleteElement(element);
					}
					
				} catch (IOException ioe) {
					Alert.error("Cannot delete", "Unable to delete: " + ioe.getMessage());
				}
			}
		});
		

	}
}
