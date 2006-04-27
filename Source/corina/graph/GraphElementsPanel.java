package corina.graph;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import corina.Element;
import corina.Sample;
import corina.gui.FileDialog;
import corina.gui.UserCancelledException;
import corina.ui.Alert;
import corina.ui.I18n;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class GraphElementsPanel extends JPanel {
	public GraphElementsPanel(List samples, GraphWindow gwindow) {
		super(new BorderLayout());
		
		this.window = gwindow;
		
		listmodel = new DefaultListModel();
		list = new JList(listmodel);
		list.setBorder(BorderFactory.createTitledBorder("Elements list"));
		add(list, BorderLayout.NORTH);
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				window.listSelectionChanged();
				removeButton.setEnabled((list.getSelectedIndex() < 0) ? false : true);
			}
		});
		
		loadSamples(samples);
		
	    JPanel addButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    add(addButtonContainer);
	    
	    addButton = new JButton("Add...");
	    addButtonContainer.add(addButton);
	    addButton.addActionListener(new AbstractAction() {
	    	public void actionPerformed(ActionEvent ae) {
	    		List ss = null;
	    		try {
	    			ss = FileDialog.showMulti(I18n.getText("plot"));
	    		} catch (UserCancelledException uce) {
	    			return;
	    		}

	    		window.add(ss);
	    	}
	    });

	    removeButton = new JButton("Remove");
	    removeButton.setEnabled(false);
	    addButtonContainer.add(removeButton);
	    removeButton.addActionListener(new AbstractAction() {
	    	public void actionPerformed(ActionEvent ae) {
	    		window.remove(getSelectedIndex());
	    	}
	    });
	}
	
	public void setSelectedIndex(int idx) {
		list.setSelectedIndex(idx);
	}
	
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	public void loadSamples(List samples) {
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
}
