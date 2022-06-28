/**
 * 
 */
package org.tellervo.desktop.gui.dbbrowse;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoAssociatedResource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesSearchResource;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSIParam;
import org.tridas.interfaces.ITridas;


/**
 * A JPanel for building searches for TRiDaS entities based on one or more parameters
 * 
 * 
 * @author Lucas Madar
 *
 */
public class SearchPanel extends JPanel implements PropertyChangeListener, ResourceEventListener {
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(SearchPanel.class);
	private JScrollPane scroll;
	private final Window parent;
	
	/** Our actual panel */
	private final JPanel panel;
	
	/** A list of search panels we have */
	private final List<SearchParameterPanel> parameters;
	
	/** The search in progress, or null if no search is in progress */
	private TellervoAssociatedResource searchResource;
	
	/** A map for search results */
	private Map<SearchParameters, ElementList> searchCacheMap;
	
	private final SearchButtonPanel butPanel;
	
	/** The thing that cares about our results */
	private final SearchResultManager manager;
	private JPanel panelSearchFor;
	private JLabel lblSearchFor;
	private JComboBox<SearchReturnObject> cboSearchFor;
	private DefaultComboBoxModel searchForModel;
	
	public SearchPanel()
	{
		this.parent = null;
		this.manager = null;
		butPanel = new SearchButtonPanel();
		panel = new JPanel();
		// make our list of search parameters
		parameters = new ArrayList<SearchParameterPanel>();
		
		init();
	}
	
	public SearchPanel(SearchResultManager manager, Window parent) {
		
		this.parent = parent;
		this.manager = manager;
		butPanel = new SearchButtonPanel();
		panel = new JPanel();
		// make our list of search parameters
		parameters = new ArrayList<SearchParameterPanel>();
		
		init();
	}
	
	private void init()
	{
		scroll = new JScrollPane();
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


		butPanel.setBorder(null);
		
		butPanel.btnAddAnotherCriteria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					addSearchCriteria();
			}
		});
		
		butPanel.btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					rebuildQuery();
			}
		});
		
		butPanel.btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					log.debug("Removing existing criteria");
					parameters.clear();
					parameters.add(new SearchParameterPanel());
					rebuild();
			}
		});
		
		
		// create a synchronized map for our results
		searchCacheMap = Collections.synchronizedMap(new HashMap<SearchParameters, ElementList>());		
		
		
		// make our 'add' button...
	/*	btnAddSearchCriteria = new JButton("Add another criteria");
		btnAddSearchCriteria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSearchCriteria();
			}
		});*/
				
		// justify our content to the top of our panel...
		
		scroll.add(panel);
		
		this.setLayout(new BorderLayout());
		this.add(scroll, BorderLayout.CENTER);
		this.add(butPanel, BorderLayout.SOUTH);
		scroll.setViewportView(panel);
		
		panelSearchFor = new JPanel();
		add(panelSearchFor, BorderLayout.NORTH);
		panelSearchFor.setLayout(new MigLayout("", "[78px][grow]", "[15px,center]"));
		panelSearchFor.setVisible(false);
		
		lblSearchFor = new JLabel("Search for:");
		panelSearchFor.add(lblSearchFor, "cell 0 0,alignx trailing,aligny center");
		
		cboSearchFor = new JComboBox<SearchReturnObject>();
		searchForModel = new DefaultComboBoxModel();
		cboSearchFor.setModel(searchForModel);
		panelSearchFor.add(cboSearchFor, "cell 1 0,growx");
		
		addSearchCriteria();
	}
		
	private SearchParameterPanel addSearchCriteria() {		
		// create a new panel and add it to our list
		SearchParameterPanel newParameter = new SearchParameterPanel();
		parameters.add(newParameter);
		newParameter.addSearchParameterPropertyChangeListener(this);

		rebuild();
		
		return newParameter;
	}

	/**
	 * Rebuild our panel: relayout everything
	 */
	protected void rebuild() {
		// delete the layout
		panel.removeAll();

		/*GridBagConstraints c = new GridBagConstraints();
		
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
		*/
		
		panel.setLayout(new MigLayout("", "[grow,fill]", "[]"));
		
		// now, add everything to our layout
		for(int i=0; i<parameters.size(); i++)
		{
			String operator = "AND";
			if(i==0) operator = "";
			SearchParameterPanel searchPanel = parameters.get(i);
			searchPanel.setParameterOperator(operator);
			panel.add(searchPanel, "cell 0 "+i);
		}
				
		
					
		// revalidate our panel, we changed the layout
		panel.revalidate();		
		panel.repaint();
	}
	
	public void setSearchForItems(ArrayList<SearchReturnObject> items)
	{
		panelSearchFor.setVisible(true);
		searchForModel.removeAllElements();
		for(SearchReturnObject item : items)
		{
			searchForModel.addElement(item);
		}
	}
	
	/**
	 * Rebuild a search parameter list, and search if need be
	 */
	private void rebuildQuery() {
		
		SearchReturnObject searchfor = (SearchReturnObject) cboSearchFor.getSelectedItem();
		
		if(searchfor==null) searchfor = SearchReturnObject.MEASUREMENT_SERIES;
		SearchParameters search = new SearchParameters(searchfor);

		// Set paging parameters
		search.setLimit((Integer) butPanel.spnLimit.getValue());
		search.setSkip((Integer) butPanel.spnSkip.getValue());
		
		// don't include children
		search.setIncludeChildren(false);
		
		for(SearchParameterPanel searchPanel : parameters) {
			if(!searchPanel.isDataValid())
				continue;
			
			search.addSearchConstraint(searchPanel.getParameterName(), searchPanel.getOperator(), 
					searchPanel.getValue());
		}
		
		// empty search? don't change anything...
		if(!search.isSetParams())
			return;
		
		
		if(searchfor.equals(SearchReturnObject.MEASUREMENT_SERIES))
		{
			startSeriesSearch(search);
		}
		else
		{
			startEntitySearch(search);
		}
	}
	
	public void runQuery(SearchParameters params)
	{
		parameters.clear();
		
		if(params.isSetLimit()) butPanel.spnLimit.setValue(params.getLimit());
		if(params.isSetSkip()) butPanel.spnSkip.setValue(params.getSkip());
		
		for(WSIParam p : params.getParams())
		{
			SearchParameterPanel newParameter = new SearchParameterPanel();			
			newParameter.populateFromWSIParam(p);
			newParameter.addSearchParameterPropertyChangeListener(this);
			parameters.add(newParameter);
			
		}
		
		rebuild();
		doSearch();
	}
	
	/**
	 * Build the query from the GUI panels and perform search
	 * 
	 */
	public void doSearch()
	{
		rebuildQuery();
		
	}
	
	
	private void startEntitySearch(SearchParameters params)
	{
		log.debug("Start search");
		
		// we already have a search resource?
		if(searchResource != null) {
			// the parameters are the same? don't search again
			if(searchResource instanceof EntitySearchResource && ((EntitySearchResource) searchResource).getSearchParameters().equals(params))
				return;
			
			// abort the current query, being careful to not do this if we're in the process of getting results
			log.debug("Query already underway, so aborting");
			synchronized(this) {
				searchResource.removeResourceEventListener(this);
				searchResource.abortQuery();
				searchResource = null;
			}
		}

		// notify we have a new search
		log.debug("Notifying manager that search is starting");
		manager.notifySearchStarting();
		
		// check to see if we already have this search
		/*ElementList elements;
		if((elements = searchCacheMap.get(params)) != null) {
			manager.notifySearchFinished(elements);
			return;
		}*/
		
		// lock again, to make sure (not necessary?) 
		synchronized(this) {
			searchResource = new EntitySearchResource(params);
		}
		
		// start the query...
		searchResource.addResourceEventListener(this);
		searchResource.query();
		
		TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(parent, searchResource);
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful()) 
		{
			log.debug("Search failed");
			// Search failed
			return;
		}
		else
		{
			log.debug("Search successful");
		}
	}
	
	/**
	 * Start a search on the given parameters
	 * If one already exists, it is canceled (unless it's the same search)
	 * @param params
	 */
	private void startSeriesSearch(SearchParameters params) {
		log.debug("Start search");
		
		// we already have a search resource?
		if(searchResource != null) {
			// the parameters are the same? don't search again
			if(searchResource instanceof SeriesSearchResource && ((SeriesSearchResource) searchResource).getSearchParameters().equals(params))
				return;
			
			// abort the current query, being careful to not do this if we're in the process of getting results
			log.debug("Query already underway, so aborting");
			synchronized(this) {
				searchResource.removeResourceEventListener(this);
				searchResource.abortQuery();
				searchResource = null;
			}
		}
		

		// notify we have a new search
		log.debug("Notifying manager that search is starting");
		manager.notifySearchStarting();
		
		// check to see if we already have this search
		ElementList elements;
		if((elements = searchCacheMap.get(params)) != null) {
			manager.notifySeriesSearchFinished(elements);
			return;
		}
		
		// lock again, to make sure (not necessary?) 
		synchronized(this) {
			searchResource = new SeriesSearchResource(params);
		}
		
		// start the query...
		searchResource.addResourceEventListener(this);
		searchResource.query();
		
		TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(parent, searchResource);
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful()) 
		{
			log.debug("Search failed");
			// Search failed
			return;
		}
		else
		{
			log.debug("Search successful");
		}
		
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
			
			//rebuildQuery();
			return;
		}
		
		// rebuild when params get invalid
		if(SearchParameterPanel.PARAMETER_VALID_PROPERTY.equals(evt.getPropertyName())) {
			if((Boolean) evt.getNewValue() == false)
				//rebuildQuery();
			return;
		}
		
		// rebuild if properties change!
		//rebuildQuery();
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
		
		log.debug("Resource changed");
		
		if(re.getSource() instanceof SeriesSearchResource)
		{
		
			SeriesSearchResource resource = (SeriesSearchResource) re.getSource();
			
			// ignore if this info comes from a resource we don't care about
			synchronized(this) {
				if(resource != searchResource)
					return;
			}
			
			if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_FAILED) {
				
				log.debug("RESOURCE_QUERY_FAILED");
				
				// get rid of the search resource
				synchronized(this) {
					searchResource = null;
				}
				
				// run this in the UI thread
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						manager.notifySeriesSearchFinished(null);
						
						JOptionPane.showMessageDialog(SearchPanel.this, "Search failed: " + 
								re.getAttachedException().getLocalizedMessage(), 
									"Search results", JOptionPane.ERROR_MESSAGE);					
					}
					
				});
				
				return;
			}
			
			if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
				
				log.debug("RESOURCE_QUERY_COMPLETE");
				
				// get rid of the search resource
				synchronized(this) {
					searchResource = null;
				}
				
				final ElementList elements = resource.getAssociatedResult();
				
				// store the result (getAssociatedResult must return a value)
	
				synchronized (searchCacheMap){
					searchCacheMap.put(resource.getSearchParameters(), elements);	
				}
					
				// run this in the UI thread
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						manager.notifySeriesSearchFinished(elements);
					}				
				});		
			}
		}
		else if(re.getSource() instanceof EntitySearchResource)
		{
			EntitySearchResource resource = (EntitySearchResource) re.getSource();
			
			// ignore if this info comes from a resource we don't care about
			synchronized(this) {
				if(resource != searchResource)
					return;
			}
			
			if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_FAILED) {
				
				log.debug("RESOURCE_QUERY_FAILED");
				
				// get rid of the search resource
				synchronized(this) {
					searchResource = null;
				}
				
				// run this in the UI thread
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						manager.notifyEntitySearchFinished(null);
						
						JOptionPane.showMessageDialog(SearchPanel.this, "Search failed: " + 
								re.getAttachedException().getLocalizedMessage(), 
									"Search results", JOptionPane.ERROR_MESSAGE);					
					}
					
				});
				
				return;
			}
			
			if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
				
				log.debug("RESOURCE_QUERY_COMPLETE");
				
				// get rid of the search resource
				synchronized(this) {
					searchResource = null;
				}
				
				final List<ITridas> elements = (List<ITridas>) resource.getAssociatedResult();

				// run this in the UI thread
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						manager.notifyEntitySearchFinished(elements);
					}				
				});		
			}
		}
		
	}	
}
