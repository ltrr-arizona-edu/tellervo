package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.webdbi.Authenticate;
import edu.cornell.dendro.corina.webdbi.DummyResource;
import edu.cornell.dendro.corina.webdbi.Resource;
import edu.cornell.dendro.corina.webdbi.ResourceEvent;
import edu.cornell.dendro.corina.webdbi.ResourceEventListener;
import edu.cornell.dendro.corina.webdbi.ResourceObject;

public class DebugInstantiator extends JDialog implements ResourceEventListener {
	private static DebugInstantiator singleton = null;

	private void do_it() {
		// an example
		if(true) {
			Sample s = new Sample();
			ResourceObject<Sample> r = new DummyResource();
		
			// create a dumb sample
			s.setMeta("id", "1");
		 
			// associate our DummyResource with it
			r.setObject(s);
		
			// make sure we're listening to status information
			r.addResourceEventListener(this);
		
			// start the query (query method runs in another thread)
			r.query();		
		}
		
		// another example
		if(false) {
			Resource r = new Authenticate("kit", "yourmom", "123412903481239nonce");
		
			// make sure we're listening to status information
			r.addResourceEventListener(this);
		
			// start the query (query method runs in another thread)
			r.query();					
		}
	}
	
	private DebugInstantiator() {
		setTitle("Debug Instantiator");
		text = new JTextArea();

		text.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
		text.setEditable(false);
		text.setFont(Font.decode("Courier New-Plain-11"));
		text.setLineWrap(true);
		text.setWrapStyleWord(true);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().add(
				new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), 
				BorderLayout.CENTER);
		pack();
		setSize(600, 500);
		
		text.append("Starting debug...:\n");

		do_it();
	}
	
	JTextArea text;
	
	public void resourceChanged(ResourceEvent re) {
		Document doc;
		
		switch (re.getEventType()) {
		case ResourceEvent.RESOURCE_DEBUG_IN:
			text.append("\n[INCOMING]\n");
			doc = re.getAttachedDocument();
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_OUT:
			text.append("\n[OUTGOING]\n");
			doc = re.getAttachedDocument();
			break;
			
		case ResourceEvent.RESOURCE_QUERY_COMPLETE:
			text.append("-- Resource query complete --\n");
			return;

		case ResourceEvent.RESOURCE_QUERY_FAILED:
			Exception ex = re.getAttachedException();
			if(ex == null)
				text.append("-- Resource query failed --\n");
			else
				text.append("-- Resource query failed: " + ex + " --\n");
			return;
			
		default:
			text.append("Unknown event!\n");
			return;
		}
		
		XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());
		text.append(outp.outputString(doc));
		
		text.repaint();
	}

	public static synchronized void showMe() {
		if (singleton == null) {
			singleton = new DebugInstantiator();
		}
		Center.center(singleton);
		singleton.show();
	}
}

