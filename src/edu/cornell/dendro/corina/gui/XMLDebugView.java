package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.cornell.dendro.corina.util.Center;

import jsyntaxpane.DefaultSyntaxKit;

public class XMLDebugView extends JDialog {
	public static XMLDebugView singleton = new XMLDebugView(null);
	/** if not enabled, we ignore any add events */
	private static final boolean enabled = true;
	/** The maximum number of documents to hold */
	private static final int maxDocuments = 10;

	private JList docsView;
	private JEditorPane sourceView;
	
	private static class DocumentHolder {
		public String name;
		public Object doc;
		public Boolean direction;
		
		public DocumentHolder(Object doc, String name, boolean isIncoming) {
			this.doc = doc;
			this.name = name;
			this.direction = isIncoming;
		}
		
		@Override
		public String toString() {
			return (direction ? "[I] " : "[O] ") + name;
		}
	}
	
	public XMLDebugView(Frame owner) {
		// don't be modal
		super(owner, false);
		
		if(!enabled) {
			dispose();
			singleton = null;
			return;
		}
				
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		// initialize java syntax editor
		DefaultSyntaxKit.initKit();

		sourceView = new JEditorPane();		
		sourceView.setEditable(false);
		
		docsView = new JList(new DefaultListModel());
		docsView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		docsView.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				// wait until we're all done
				if(e.getValueIsAdjusting())
					return;
				
				DocumentHolder doc = (DocumentHolder) docsView.getSelectedValue();
				if(doc == null) {
					sourceView.setText("Select a document to view");
					return;
				}
				
				if(doc.doc instanceof Document) {		
					XMLOutputter outputter = new XMLOutputter();
					outputter.setFormat(Format.getPrettyFormat());
					sourceView.setText(outputter.outputString((Document)doc.doc));
				}
				else {
					sourceView.setText(doc.doc.toString());
				}
			}
			
		});
		
		docsView.setPreferredSize(new Dimension(200, 600));
		sourceView.setPreferredSize(new Dimension(824, 600));
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		JScrollPane scroller;
		
		scroller = new JScrollPane(docsView);
		c.add(scroller, BorderLayout.WEST);
		
		scroller = new JScrollPane(sourceView);
		c.add(scroller, BorderLayout.CENTER);
				
		setSize(new Dimension(1024, 768));
		pack();
		
		// set the sourceView to display the xml
		// apparently, has to be done here
		sourceView.setContentType("text/xml");

		Center.center(this);
	}
	
	public static void addDocument(Object doc, String title, boolean isIncoming) {
		if(!enabled)
			return;
		
		((DefaultListModel)singleton.docsView.getModel()).addElement(new DocumentHolder(doc, title, isIncoming));
		
		if(singleton.docsView.getModel().getSize() > maxDocuments)
			singleton.docsView.remove(0);
	}
	
	public static void showDialog() {
		if(enabled)
			singleton.setVisible(true);
	}
}
