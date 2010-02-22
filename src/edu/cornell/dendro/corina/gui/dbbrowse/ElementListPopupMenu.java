package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.BaseSeries;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.cross.CrossdateDialog;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.SeriesSearchResource;

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
			if(ds.getType().getValue().equals("Crossdate"));
			{
				item = Builder.makeMenuItem("crossdate.reviewCrossdate", true, "crossdate.png");
				add(item);
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {			
						
						Element floatingSeries = element;
						Element referenceSeries = null;
						
						// set up our query...
						SearchParameters search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
						search.addSearchConstraint(SearchParameterName.SERIESDBID, 
								SearchOperator.EQUALS, ds.getInterpretation().getDatingReference().getLinkSeries().getIdentifier().getValue());
					

						SeriesSearchResource searchResource = new SeriesSearchResource(search);
						CorinaResourceAccessDialog dlg = new CorinaResourceAccessDialog(new JDialog(), searchResource);
						
						// start our query (remotely)
						searchResource.query();
						dlg.setVisible(true);
						
						if(!dlg.isSuccessful()) {
							new Bug(dlg.getFailException());
						} else {
							referenceSeries = searchResource.getAssociatedResult().get(0);

						}
						
						
						new CrossdateDialog(new JFrame(), referenceSeries, floatingSeries);

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
