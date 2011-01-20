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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.tridas.io.TridasIO;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.INamingConvention;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.naming.UUIDNamingConvention;
import org.tridas.io.util.IOUtils;

import edu.cornell.dendro.corina.components.table.CustomTreeCellRenderer;
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
	
	private static final String[] namings = new String[]{
		"Numerical", "Hierachical", "UUID"
	};
	
	private static final String iconSize = "16x16";

	private JComboBox charset;
	private JComboBox groupings;
	private JComboBox format;
	private JComboBox naming;
	private JTree tree;
	
	private final ExportModel model;
	private JButton exportButton;
	private JButton browseButton;
	
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
		naming = new JComboBox(namings);
		
		ImageIcon ficon     = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/fail.png"));
		ImageIcon wicon     = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/warning.png"));
		ImageIcon sicon     = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/success.png"));
		ImageIcon infoicon  = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/info.png"));
		ImageIcon filewicon = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/filewarning.png"));
		ImageIcon filesicon = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/filesuccess.png"));

		CustomTreeCellRenderer renderer = new CustomTreeCellRenderer(sicon, wicon, ficon, filesicon, filewicon, infoicon);
		
		tree = new JTree(model.getRootNode(), false);
		tree.setCellRenderer(renderer);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.expandRow(0);
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(tree);
		
		exportButton = new JButton("Export");
		browseButton = new JButton("Choose Directory");
		
		setLayout(new BorderLayout());
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(new JLabel("Format:"));
		box.add(format);
		box.add(new JLabel("Charset:"));
		box.add(charset);
		box.add(new JLabel("Options:"));
		box.add(groupings);
		
		add(box, "West");
		add(pane, "Center");
	}
	
	private void addListeners(){
		
		exportButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				INamingConvention name;
				if(naming.getSelectedIndex() == 0){
					name = new NumericalNamingConvention();
				}
				else if(naming.getSelectedIndex() == 1){
					name = new HierarchicalNamingConvention();
				}
				else {
					name = new UUIDNamingConvention();
				}
				ExportEvent evt = new ExportEvent(model, charset.getSelectedItem().toString(),
						format.getSelectedItem().toString(), name, groupings.getSelectedIndex() == 0);
				evt.dispatch();
			}
		});
	}
}
