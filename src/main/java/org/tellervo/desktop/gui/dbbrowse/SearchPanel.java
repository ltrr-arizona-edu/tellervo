/**
 * 
 */
package org.tellervo.desktop.gui.dbbrowse;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.tellervo.desktop.sample.ElementList;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesSearchResource;


/**
 * @author Lucas Madar
 *
 */
public class SearchPanel extends JScrollPane implements PropertyChangeListener, ResourceEventListener {
	private static final long serialVersionUID = 1L;
	
	/** Our actual panel */
	private final JPanel panel;
	
	/** A list of search panels we have */
	private final List<SearchParameterPanel> parameters;
	
	/** The vertical glue we stick at the bottom of our box */
	private final JButton addButton;

	/** The search in progress, or null if no search is in progress */
	private SeriesSearchResource searchResource;
	
	/** A map for search results */
	private Map<SearchParameters, ElementList> searchCacheMap;
	
	/** The thing that cares about our results */
	private final SearchResultManager manager;
	
	public SearchPanel(SearchResultManager manager) {
		super(null, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

		this.manager = manager;

		// create a synchronized map for our results
		searchCacheMap = Collections.synchronizedMap(new HashMap<SearchParameters, ElementList>());		
		
		panel = new JPanel(new GridBagLayout());
		
		// make our list of search parameters
		parameters = new ArrayList<SearchParameterPanel>();
		
		// make our 'add' button...
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSearch();
			}
		});
		
		// justify our content to the top of our panel...
		JPanel dummy = new JPanel(new BorderLayout());
		dummy.add(panel, BorderLayout.NORTH);
		
		setViewportView(dummy);
		
		addSearch();
	}
	
	private void addSearch() {		
		// create a new panel and add it to our list
		SearchParameterPanel newParameter = new SearchParameterPanel();
		parameters.add(newParameter);
		newParameter.addSearchParameterPropertyChangeListener(this);

		rebuild();
	}

	/**
	 * Rebuild our panel: relayout everything
	 */
	private void rebuild() {
		// delete the layout
		panel.removeAll();

		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 4;
		
		// now, add everything to our layout
		for(SearchParameterPanel searchPanel : parameters) {
			panel.add(searchPanel, c);
			c.gridy++;
		}
		
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(addButton, c);
		
		// revalidate our panel, we changed the layout
		panel.revalidate();		
	}
	
	/**
	 * Rebuild a search parameter list, and search if need be
	 */
	private void rebuildQuery() {
		SearchParameters search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);

		// don't include children
		search.setIncludeChildren(false);
		
		for(SearchParameterPanel searchPanel : parameters) {
			if(!searchPanel.isDataValid())
				return;
			
			search.addSearchConstraint(searchPanel.getParameterName(), searchPanel.getOperator(), 
					searchPanel.getValue());
		}
		
		// empty search? don't change anything...
		if(!search.isSetParams())
			return;
		
		startSearch(search);
	}
	
	/**
	 * Start a search on the given parameters
	 * If one already exists, it is cancelled (unless it's the same search)
	 * @param params
	 */
	private void startSearch(SearchParameters params) {
		// we already have a search resource?
		if(searchResource != null) {
			// the parameters are the same? don't search again
			if(searchResource.getSearchParameters().equals(params))
				return;
			
			// abort the current query, being careful to not do this if we're in the process of getting results
			synchronized(this) {
				searchResource.removeResourceEventListener(this);
				searchResource.abortQuery();
				searchResource = null;
			}
		}

		// notify we have a new search
		manager.notifySearchStarting();
		
		// check to see if we already have this search
		ElementList elements;
		if((elements = searchCacheMap.get(params)) != null) {
			manager.notifySearchFinished(elements);
			return;
		}
		
		// lock again, to make sure (not necessary?) 
		synchronized(this) {
			searchResource = new SeriesSearchResource(params);
		}
		
		// start the query...
		searchResource.addResourceEventListener(this);
		searchResource.query();
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		SearchParameterPanel searchPanel;
		
		if(evt.getSource() instanceof SearchParameterPanel)
			searchPanel = (SearchParameterPanel) evt.getSource();
		else
			return;
		
		// removing it from the list?
		if(SearchParameterPanel.PARAMETER_REMOVED_PROPERTY.equals(evt.getPropertyName())) {
			if((Boolean) evt.getNewValue() == true) {
				parameters.remove(searchPanel);
				rebuild();
			}
			
			rebuildQuery();
			return;
		}
		
		// rebuild when params get invalid
		if(SearchParameterPanel.PARAMETER_VALID_PROPERTY.equals(evt.getPropertyName())) {
			if((Boolean) evt.getNewValue() == false)
				rebuildQuery();
			return;
		}
		
		// rebuild if properties change!
		rebuildQuery();
	}

	public void cancelSearch() {
		// abort the current query, being careful to not do this if we're in the process of getting results
		synchronized(this) {
			if(searchResource == null)
				return;
			
			searchResource.removeResourceEventListener(this);
			searchResource.abortQuery();
			searchResource = null;
		}		
	}
	
	public void resourceChanged(final ResourceEvent re) {
		SeriesSearchResource resource = (SeriesSearchResource) re.getSource();
		
		// ignore if this info comes from a resource we don't care about
		synchronized(this) {
			if(resource != searchResource)
				return;
		}
		
		if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_FAILED) {
			// get rid of the search resource
			synchronized(this) {
				searchResource = null;
			}
			
			// run this in the UI thread
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					manager.notifySearchFinished(null);
					
					JOptionPane.showMessageDialog(SearchPanel.this, "Search failed: " + 
							re.getAttachedException().getLocalizedMessage(), 
								"Search results", JOptionPane.ERROR_MESSAGE);					
				}
				
			});
			
			return;
		}
		
		if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
			// get rid of the search resource
			synchronized(this) {
				searchResource = null;
			}
			
			final ElementList elements = resource.getAssociatedResult();
			
			// store the result (getAssociatedResult must return a value)
			searchCacheMap.put(resource.getSearchParameters(), elements);
			
			// run this in the UI thread
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					manager.notifySearchFinished(elements);
				}				
			});		
		}
	}	
}
