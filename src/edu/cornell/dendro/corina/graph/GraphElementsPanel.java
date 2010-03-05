package edu.cornell.dendro.corina.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;

public class GraphElementsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public GraphElementsPanel(List<Graph> samples, GraphWindow gwindow) {
		super(new BorderLayout());
		
		this.window = gwindow;
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		// Create panel to hold legend
		JPanel legendPanel = new JPanel();
		legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
		legendPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Components"));
		topPanel.add(legendPanel);
		
		// Create list of series
		listmodel = new DefaultListModel();
		list = new JList(listmodel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		legendPanel.add(list);
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				window.listSelectionChanged();

				boolean enabled = list.getSelectedIndex() < 0 ? false : true;

				removeButton.setEnabled(enabled);
				colorselect.setEnabled(enabled);
			}
		});
		
		JPanel colorSelectorPanel = new JPanel();
		colorSelectorPanel.setLayout(new BoxLayout(colorSelectorPanel, BoxLayout.X_AXIS));
		JLabel lblColor = new JLabel();
		lblColor.setText("Selected color:");
		colorselect = new ColorComboBox();
		colorSelectorPanel.add(lblColor);
		colorSelectorPanel.add(colorselect);
		legendPanel.add(colorSelectorPanel);
		
		colorselect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				window.setActiveColor(colorselect.getSelectedColor());
			}
		});
		
		add(topPanel, BorderLayout.NORTH);
		loadSamples(samples);
				
	    JPanel addButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    add(addButtonContainer, BorderLayout.CENTER);
	    
	    addButton = new JButton("Add...");
	    addButtonContainer.add(addButton);
	    addButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
	    		Window ancestor = SwingUtilities.getWindowAncestor(GraphElementsPanel.this);
	    		
				DBBrowser browser; 
				
				if(ancestor instanceof Dialog)
					browser = new DBBrowser((Dialog) ancestor, true, true);
				else if(ancestor == null || window instanceof Frame)
					browser = new DBBrowser((Frame) ancestor, true, true);
				else
					throw new IllegalStateException("GraphElementsPanel has no real parents!");
	    		
	    		browser.setVisible(true);
	    		
	    		if(browser.getReturnStatus() == DBBrowser.RET_OK) {
	    			ElementList ss = browser.getSelectedElements();
	    			
	    			for(Element e : ss) {
	    				// load it
	    				Sample s;
	    				try {
	    					s = e.load();
	    				} catch (IOException ioe) {
	    					Alert.error("Error Loading Sample",
	    							"Can't open this file: " + ioe.getMessage());
	    					continue;
	    				}

	    				OpenRecent.sampleOpened(new SeriesDescriptor(s));
	    				
	    				// add it to graph
	    				window.add(s);
	    			}
	    		}	
	    	}
	    });

	    removeButton = new JButton("Remove");
	    removeButton.setEnabled(false);
	    addButtonContainer.add(removeButton);
	    removeButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
	    		window.remove(getSelectedIndex());
	    	}
	    });
	}
	
	public void setSelectedIndex(int idx) {
		list.setSelectedIndex(idx);
	}
	
	public void setColor(Color c) {
		colorselect.setColor(c);
	}
	
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	public void loadSamples(List<Graph> samples) {
		listmodel.clear();
		for(int i = 0; i < samples.size(); i++) {
			Graph cg = (Graph) samples.get(i);
			
			listmodel.addElement(cg.graph.toString());
		}
		list.revalidate();
	}
	
	private DefaultListModel listmodel;
	private JList list;
	private GraphWindow window;
	private JButton addButton;
	private JButton removeButton;
	private ColorComboBox colorselect;
}
