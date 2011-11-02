/**
 * Created at Jan 19, 2011, 1:25:46 PM
 */
package edu.cornell.dendro.corina.io.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.miginfocom.swing.MigLayout;

import org.tridas.io.TridasIO;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.INamingConvention;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.naming.UUIDNamingConvention;

import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.gui.Help;
import edu.cornell.dendro.corina.io.command.ConvertCommand.StructWrapper;
import edu.cornell.dendro.corina.io.control.ExportEvent;
import edu.cornell.dendro.corina.io.control.SaveEvent;
import edu.cornell.dendro.corina.io.model.ExportModel;
import edu.cornell.dendro.corina.io.model.ConvertModel.WriterObject;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import javax.swing.DefaultComboBoxModel;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class ExportView extends JFrame {
	
	// probably change how this is populated
	private static final String[] groupingNames = new String[]{"Single packed file if possible",
			"Separate files for each series"};
	
	private static final String[] namings = new String[]{"Numerical", "Hierachical", "UUID"};
	
	private static final int iconSize = 16;
	
	private JComboBox charset;
	private JComboBox groupings;
	private JComboBox format;
	private JComboBox naming;
	private JComboBox selection;
	private JTree tree;
	
	private final ExportModel model;
	private JButton btnConvert;
	private JButton btnHelp;
	private JButton btnBrowse;
	private JButton btnSave;
	private JTextField destFolder;
	
	private final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Conversion Results");

	final JFileChooser fc = new JFileChooser();
	
	public ExportView(ExportModel argModel) {
		model = argModel;
		setIconImage(Builder.getApplicationIcon());
		setTitle(I18n.getText("export.dataFromDatabase"));
		
		initComponents();
		addListeners();
		linkModel();
		pack();
		this.setSize(564, 332);
		setLocationRelativeTo(null);
	}
	
	private void initComponents() {
		
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
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelMain = new JPanel();
		panelMain.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(panelMain, BorderLayout.CENTER);
		panelMain.setLayout(new BorderLayout(15, 15));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelMain.add(splitPane);
		splitPane.setResizeWeight(0.5);
		splitPane.setOneTouchExpandable(true);
		
		JPanel panelSummary = new JPanel();
		splitPane.setRightComponent(panelSummary);
		panelSummary.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelSummary.add(scrollPane, BorderLayout.CENTER);
		
		Icon sicon = Builder.getIcon("success.png", iconSize);
		Icon wicon = Builder.getIcon("warning.png", iconSize);
		Icon ficon = Builder.getIcon("fail.png", iconSize);
		Icon filesicon = Builder.getIcon("filesuccess.png", iconSize);
		Icon filewicon = Builder.getIcon("filewarning.png", iconSize);
		Icon infoicon = Builder.getIcon("info.png", iconSize);
		CustomTreeCellRenderer renderer = new CustomTreeCellRenderer(sicon, wicon, ficon, filesicon, filewicon,infoicon);
		
		tree = new JTree(rootNode, false);
		tree.setCellRenderer(renderer);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.expandRow(0);
		
		scrollPane.setViewportView(tree);
		
		JLabel lblNewLabel = new JLabel("Summary of export:");
		panelSummary.add(lblNewLabel, BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(tabbedPane);
		
		JPanel panelBasic = new JPanel();
		tabbedPane.addTab("Basic settings", null, panelBasic, null);
		panelBasic.setLayout(new MigLayout("", "[right][grow,fill][]", "[][]"));
		
		JLabel lblFormat = new JLabel(I18n.getText("io.convert.lblFormat"));
		panelBasic.add(lblFormat, "cell 0 0");
		
		format = new JComboBox(TridasIO.getSupportedWritingFormats());
		panelBasic.add(format, "cell 1 0 2 1");
		
		JLabel lblOutputFolder = new JLabel(I18n.getText("io.convert.lblOutput"));
		panelBasic.add(lblOutputFolder, "cell 0 1");
		
		destFolder = new JTextField();
		panelBasic.add(destFolder, "cell 1 1");
		
		btnBrowse = new JButton(I18n.getText("io.convert.btnBrowse"));
		panelBasic.add(btnBrowse, "cell 2 1,alignx left");
		
		JPanel panelAdv = new JPanel();
		tabbedPane.addTab("Advanced options", null, panelAdv, null);
		panelAdv.setLayout(new MigLayout("", "[right][grow,fill]", "[][][][]"));
		
		JLabel lblWhatToExport = new JLabel(I18n.getText("io.convert.whatExport"));
		panelAdv.add(lblWhatToExport, "cell 0 0");
		
		selection = new JComboBox(new String[] { model.getElements().size() <= 1 ? "The selected sample" : "The group of "+model.getElements().size()+" samples." });
		panelAdv.add(selection, "cell 1 0");
		
		JLabel lblGrouping = new JLabel(I18n.getText("io.convert.lblGrouping"));
		panelAdv.add(lblGrouping, "cell 0 1");
		
		groupings = new JComboBox(groupingNames);
		groupings.setModel(new DefaultComboBoxModel(new String[] {"Separate files for each series", "Single packed file if possible"}));
		panelAdv.add(groupings, "cell 1 1");
		
		JLabel lblNaming = new JLabel(I18n.getText("io.convert.lblNaming"));
		panelAdv.add(lblNaming, "cell 0 2");
		
		naming = new JComboBox(namings);
		naming.setModel(new DefaultComboBoxModel(new String[] {"Labcodes", "Hierachical", "UUID"}));
		panelAdv.add(naming, "cell 1 2");
		
		JLabel lblEncoding = new JLabel(I18n.getText("io.convert.lblEncoding"));
		panelAdv.add(lblEncoding, "cell 0 3");
		
		charset = new JComboBox(charsets.toArray(new String[0]));
		panelAdv.add(charset, "cell 1 3");
		
		JPanel panelButtons = new JPanel();
		panelMain.add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new MigLayout("", "[][grow,fill][][]", "[]"));
		
		btnHelp = new JButton(I18n.getText("io.convert.btnHelp"));
		panelButtons.add(btnHelp, "cell 0 0");
		
		btnConvert = new JButton(I18n.getText("io.convert.btnConvert"));
		panelButtons.add(btnConvert, "cell 2 0");
		
		btnSave = new JButton(I18n.getText("io.convert.btnSave"));
		panelButtons.add(btnSave, "cell 3 0");
		btnSave.setEnabled(false);

		
		pack();

	}
	
	private void addListeners() {
		
		btnConvert.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				INamingConvention name;
				if (naming.getSelectedIndex() == 0) {
					name = new NumericalNamingConvention();
				}
				else if (naming.getSelectedIndex() == 1) {
					name = new HierarchicalNamingConvention();
				}
				else {
					name = new UUIDNamingConvention();
				}
				ExportEvent evt = new ExportEvent(model, charset.getSelectedItem().toString(), format.getSelectedItem().toString(), name, groupings.getSelectedIndex() == 1);
				evt.dispatch();
			}
		});
		
		Help.assignHelpPageToButton(btnHelp, "File_formats");
		
		btnBrowse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				// Choose directory not file
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle(I18n.getText("export.selectAFolder"));
				
				// Open at last used directory
				if (model.getExportDirectory() != null) {
					fc.setCurrentDirectory(new File(model.getExportDirectory()));
				}
				
				// Show dialog
				int returnVal;
				returnVal = fc.showSaveDialog(ExportView.this);
				
				// Set file/folder in text box
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					destFolder.setText(file.getAbsoluteFile().toString());
					model.setExportDirectory(file.getAbsoluteFile().toString());
				}

				
				// If file/folder selected enable OK button
				if (destFolder.getText() == null || destFolder.getText().equals("")) {
					btnConvert.setEnabled(false);
				}
				else {
					btnConvert.setEnabled(true);
				}
			}
		});
		
		destFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				model.setExportDirectory(destFolder.getText());
			}
		});
		
		
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				ArrayList<WriterObject> writers = new ArrayList<WriterObject>();
				for(DefaultMutableTreeNode node : model.getNodes()){
					try{
						StructWrapper w = (StructWrapper) node.getUserObject();
						writers.add(w.struct);
					}catch(Exception e){}
				}
				if(writers.size() == 0){
					JOptionPane.showConfirmDialog(ExportView.this, "Could not find converted files.  Please report bug.", "Error", JOptionPane.OK_OPTION);
					return;
				}
				
				File folder = new File(model.getExportDirectory());
				if(!folder.exists()){
					JOptionPane.showConfirmDialog(ExportView.this, "Could not find the given export directory.", "Error", JOptionPane.OK_OPTION);
					return;
				}
				SaveEvent event = new SaveEvent(writers.toArray(new WriterObject[0]), folder, ExportView.this);
				event.dispatch();
			}
		});
	}
	
	private void linkModel(){
		model.getNodes().addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if(argEvt.getPropertyName().equals(MVCArrayList.ADDED_ALL)){
					DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
					rootNode.removeAllChildren();
					for (DefaultMutableTreeNode node : model.getNodes()) {
						rootNode.add(node);
					}
					treeModel.setRoot(rootNode);
					
					if(model.getExportDirectory() != null){
						btnSave.setEnabled(true);
					}
					expandToFiles();
				}
				else if(argEvt.getPropertyName().equals(MVCArrayList.REMOVED_ALL)){
					rootNode.removeAllChildren();
					DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
					treeModel.setRoot(rootNode);
				}
			}
		});
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if(argEvt.getPropertyName().equals("exportDirectory")){
					if(model.getExportDirectory() != null && model.getNodes().size() > 0){
						btnSave.setEnabled(true);
					}else{
						btnSave.setEnabled(false);
					}
				}
			}
		});
	}
	
	private void expandToFiles() {
		int row = 0;
		while (row < tree.getRowCount()) {
			if (tree.getPathForRow(row).getPathCount() < 3) {
				tree.expandRow(row);
			}
			row++;
		}
	}
}
