/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.gui.dbbrowse.BooleanCellRenderer;
import org.tellervo.desktop.gui.dbbrowse.ElementListCellRenderer;
import org.tellervo.desktop.gui.dbbrowse.ElementListManager;
import org.tellervo.desktop.gui.dbbrowse.ElementListPopupMenu;
import org.tellervo.desktop.gui.dbbrowse.ElementListTableModel;
import org.tellervo.desktop.gui.dbbrowse.ElementListTableSorter;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
import org.tellervo.desktop.util.PopupListener;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesResource;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesSearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tridas.schema.TridasIdentifier;
import java.awt.Component;


/**
 * @author Lucas Madar
 *
 */
public class DependentsViewer extends JPanel implements ResourceEventListener, ElementListManager {
	private static final long serialVersionUID = 1L;
	
	/** The sample we're viewing */
	private Sample sample;
	/** True if we've loaded the comprehensive series */
	private boolean loadedComprehensive;
	
	/** Status bar text */
	private JLabel txtStatus;
	/** Progress bar */
	private JProgressBar pbStatus;
	
	private JPanel tablePanel;
	private JTable table;
	
	private ElementListTableSorter tableSorter;
	private ElementListTableModel tableModel;
	
	private final static String TABLEPANEL = "Series Table View";
	
	public DependentsViewer(Sample sample) {
		this.sample = sample;
		this.loadedComprehensive = false;
		
		initComponents();
		updateContent(null);
	}
	
	private void initComponents() {
			
		tableModel = new ElementListTableModel();
		table = new JTable(tableModel);

		// create status bar
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		txtStatus = new JLabel("");
		pbStatus = new JProgressBar();
		pbStatus.setVisible(false);
		setLayout(new MigLayout("", "[450px,grow,fill]", "[3px,grow,fill][18px]"));
		
		tablePanel = new JPanel(new BorderLayout());
		add(tablePanel, "cell 0 0,growx,aligny top");
		JScrollPane scrollPane = new JScrollPane(table);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		
		tableSorter = new ElementListTableSorter(tableModel, table);
		table.getTableHeader().addMouseListener(tableSorter); // add sorter & header renderer
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
    	// set our column widths
    	ElementListTableModel.setupColumnWidths(table);
    	
    	table.setDefaultRenderer(Object.class, new ElementListCellRenderer(this, false));
    	table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer(this, false));
    	
    	// popup menu
    	table.addMouseListener(new PopupListener() {
    		@Override
    		public void showPopup(MouseEvent e) {
    			// only clicks on tables
    			if(!(e.getSource() instanceof JTable))
    				return;
    			
    			JTable table = (JTable) e.getSource();
    			ElementListTableModel model = (ElementListTableModel) table.getModel();
    			
    			// get the row and sanity check
    			int row = table.rowAtPoint(e.getPoint());
    			if(row < 0 || row >= model.getRowCount())
    				return;
    			
    			// select it?
    			table.setRowSelectionInterval(row, row);
    			
    			// get the element
    			Element element = model.getElementAt(row);
    			
    			// create and show the menu
    			JPopupMenu popup = new ElementListPopupMenu(element, DependentsViewer.this);
    			popup.show(table, e.getX(), e.getY());
    		}
    	});
		statusPanel.add(txtStatus);
		Component horizontalStrut = Box.createHorizontalStrut(8);
		statusPanel.add(horizontalStrut);
		statusPanel.add(pbStatus);
		statusPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		add(statusPanel, "cell 0 1,growx,aligny top");
	
	}
	
	

	/*private void recurseAddElementsToList(ElementList elements, ElementList flat, 
			DefaultMutableTreeNode parent, int depth) {
		for(Element e : elements) {
			if(e instanceof CachedElement) {
				CachedElement ce = (CachedElement) e;
				
				// need the basic...
				if(!ce.hasBasic()) {
					System.err.println("Cached, but not even basic loaded!");
					continue;
				}
				
				// add to list
				flat.add(ce);
				
				// add to tree
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(ce);
				parent.add(node);
				
				// can't go any deeper if there's no series...
				if(!ce.hasFull())
					continue;
				
				try {
					Sample s = ce.load();
					ElementList sampleElements = s.getElements();
					if(sampleElements != null)
						recurseAddElementsToList(sampleElements, flat, node, depth + 1);
				} catch (IOException ioe) {
					// shouldn't happen
				} 
			}
			else
				System.err.println("Non-cached element: " + e);
		}
	}*/
	
	private void updateContent(ElementList el) {

		ElementList displayElements = el;
		
		// if we don't have any, use an empty list instead...
		if(displayElements == null)
			displayElements = new ElementList();
		
		
		// should be unsorted - default order is what we want
		tableModel.setElements(displayElements);
		//treeModel.setRoot(rootNode);
		
		// expand all nodes in tree
		// note: tree.getRowCount() changes as we expand each node!
		/*for(int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}*/
	}
	
	private void setStatus(String status, boolean inProgress) {
		txtStatus.setText(status);
		pbStatus.setVisible(inProgress);
		pbStatus.setIndeterminate(inProgress);
	}
	
	/**
	 * Called when someone has made this panel visible
	 */
	public void notifyPanelVisible() {
		if(loadedComprehensive)
			return;
		
		loadedComprehensive = true;
		
		if(sample.getLoader() instanceof TellervoWsiTridasElement) {
			
					
			TridasIdentifier identifier = ((TellervoWsiTridasElement) sample.getLoader()).getTridasIdentifier();
			// create a new resource
			SearchParameters search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
			search.addSearchConstraint(SearchParameterName.DEPENDENTSERIESID, SearchOperator.EQUALS, identifier.getValue());
		
			SeriesSearchResource resource = new SeriesSearchResource(search);

			// flag it as comprehensive
			resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
			resource.setOwnerWindow(SwingUtilities.getWindowAncestor(this));
			
			resource.addResourceEventListener(this);
			resource.query();
			
			setStatus("Loading comprehensive series...", true);
			
			return;
		}
		
		setStatus("Cannot load comprehensive view for this series.", false);
	}

	/**
	 * Called from loading a resource
	 */
	public void resourceChanged(ResourceEvent re) {
		switch(re.getEventType()) {
		case ResourceEvent.RESOURCE_DEBUG_OUT:
			setStatus("Loading components please wait...", true);
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_IN:
			setStatus("Receiving reply...", true);
			break;
			
		case ResourceEvent.RESOURCE_QUERY_COMPLETE: {
			setStatus("Done", false);
			
			SeriesSearchResource resource = (SeriesSearchResource) re.getSource();
			
			ElementList el = resource.getAssociatedResult();
			
			
			
			if(el == null ) {
				setStatus("Error: Series was not found.", false);
				return;
			}
			
			if(el.size()==0)
			{
				setStatus("There are no series dependent on this series", false);
				return;
			}
			else if (el.size()==1)
			{
				setStatus("One series is dependent on the current series", false);
			}
			else
			{
				setStatus("These "+el.size()+" series are dependent on the current series", false);
			}
			
			updateContent(el);
			break;
		}
			
		case ResourceEvent.RESOURCE_QUERY_FAILED:
			setStatus("Error loading view: " + re.getAttachedException().getLocalizedMessage(), false);
			break;
		}
	}

	public void deleteElement(Element e) {
		
		loadedComprehensive = false;
		notifyPanelVisible();
		
		
	}

	public boolean isElementDisabled(Element e) {
		// none of our elements are ever disabled
		return false;
	}
}
