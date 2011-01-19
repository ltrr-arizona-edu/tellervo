/**
 * Created at Jan 19, 2011, 1:25:46 PM
 */
package edu.cornell.dendro.corina.io.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTree;

import org.tridas.io.TridasIO;

import edu.cornell.dendro.corina.io.control.ExportEvent;
import edu.cornell.dendro.corina.io.model.ExportModel;

/**
 * @author Daniel
 *
 */
public class ExportView extends JFrame {
	
	// probably change how this is populated
	private static final String[] groupingNames = new String[]{
		"Single packed file if possible", "Separate files for each series"
	};
	
	private JComboBox charset;
	private JComboBox groupings;
	private JComboBox format;
	private JTree tree;
	
	private final ExportModel model;
	private JButton exportButton;
	
	public ExportView(ExportModel argModel){
		model = argModel;
		initComponents();
		addListeners();
	}
	
	private void initComponents(){
		
		ArrayList<String> charsets = new ArrayList<String>();
		charsets.add(Charset.defaultCharset().displayName());
		for (String key : Charset.availableCharsets().keySet()) {
			Charset cs = Charset.availableCharsets().get(key);
			if (cs.canEncode()) {
				if (key.equals(Charset.defaultCharset().displayName())) {
					continue;
				}
				charsets.add(key);
			}
		}
		charset = new JComboBox(charsets.toArray(new String[0]));
		groupings = new JComboBox(groupingNames);
		format = new JComboBox(TridasIO.getSupportedWritingFormats());
		
		tree = new JTree(model.getRootNode());
		
		exportButton = new JButton("Export");
		
		
		setLayout(new BorderLayout());
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(new JLabel("Format:"));
		box.add(format);
		box.add(new JLabel("Charset:"));
		box.add(charset);
		box.add(new JLabel("Options:"));
		box.add(groupings);
		
		add(box, "West");
		add(tree, "Center");
	}
	
	private void addListeners(){
		
		exportButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				ExportEvent evt = new ExportEvent(model, charset.getSelectedItem().toString(),
						format.getSelectedItem().toString(), groupings.getSelectedIndex() == 0);
				evt.dispatch();
			}
		});
	}
}
