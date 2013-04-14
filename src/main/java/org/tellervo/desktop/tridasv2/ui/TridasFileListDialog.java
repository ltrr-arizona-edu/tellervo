package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JList;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.io.view.ImportView;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasFile;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

public class TridasFileListDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(TridasFileListDialog.class);

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNewFile;
	private ArrayList<TridasFile> fileList;

	private JRadioButton radFile;
	private JRadioButton radWebpage;
	private JRadioButton radURN;
	private JButton btnBrowse;
	private JList<URI> lstFileList;
	private DefaultListModel<URI> listModel;
	
	
	/**
	 * Create the dialog.
	 */
	public TridasFileListDialog(Component parent, ArrayList<TridasFile> fileList) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[285.00px,grow,left][][]", "[][][grow,top]"));
		{
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panel, "cell 0 0 3 1,grow");
			{
				JLabel lblAddANew = new JLabel("Add a new:");
				lblAddANew.setFont(new Font("Dialog", Font.PLAIN, 12));
				panel.add(lblAddANew);
			}
			{
				radFile = new JRadioButton("File from file system");
				radFile.setFont(new Font("Dialog", Font.PLAIN, 12));
				radFile.setSelected(true);
				radFile.setActionCommand("RadioFile");
				radFile.addActionListener(this);
				panel.add(radFile);
			}
			{
				radWebpage = new JRadioButton("Webpage");
				radWebpage.setActionCommand("RadioWebpage");
				radWebpage.addActionListener(this);
				radWebpage.setFont(new Font("Dialog", Font.PLAIN, 12));
				panel.add(radWebpage);
			}
			{
				radURN = new JRadioButton("URN");
				radURN.setActionCommand("RadioURN");
				radURN.addActionListener(this);
				radURN.setFont(new Font("Dialog", Font.PLAIN, 12));
				panel.add(radURN);
			}
			
			ButtonGroup radioButtons = new ButtonGroup();
			radioButtons.add(radFile);
			radioButtons.add(radWebpage);
			radioButtons.add(radURN);

		}
		{
			txtNewFile = new JTextField("file:/");
			contentPanel.add(txtNewFile, "cell 0 1,growx");
			txtNewFile.setColumns(10);
		}
		{
			btnBrowse = new JButton();
			btnBrowse.setActionCommand("Browse");
			btnBrowse.addActionListener(this);
			btnBrowse.setIcon(Builder.getIcon("open.png", 16));
			contentPanel.add(btnBrowse, "flowx,cell 1 1");
		}
		{
			JButton btnAdd = new JButton();
			btnAdd.setIcon(Builder.getIcon("edit_add.png", 16));
			btnAdd.setActionCommand("AddToList");
			btnAdd.addActionListener(this);
			contentPanel.add(btnAdd, "cell 2 1,alignx left");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 0 2,grow");
			{
				lstFileList = new JList<URI>();
				DefaultListModel<URI> listModel = new DefaultListModel<URI>();
		
				
				scrollPane.setViewportView(lstFileList);
				lstFileList.setFont(new Font("Dialog", Font.PLAIN, 12));
				lstFileList.setModel(listModel);
				lstFileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			}
		}
		{
			JButton btnRemove = new JButton();
			btnRemove.setIcon(Builder.getIcon("cancel.png", 16));
			btnRemove.setActionCommand("DeleteFromList");
			btnRemove.addActionListener(this);
			contentPanel.add(btnRemove, "flowx,cell 1 2,aligny top");
		}
		{
			JButton btnOpen = new JButton();
			btnOpen.setIcon(Builder.getIcon("document_preview.png", 16));
			btnOpen.setToolTipText("View document");
			btnOpen.setActionCommand("Open");
			btnOpen.addActionListener(this);
			contentPanel.add(btnOpen, "cell 2 2");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(this);
			}
		}
	}

	public ArrayList<TridasFile> getFileList()
	{
		return this.fileList;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		
		if(ev.getActionCommand()=="Cancel")
		{
			this.dispose();
		}
		else if (ev.getActionCommand()=="RadioWebpage")
		{
			log.debug("Webpage");
			txtNewFile.setText("http://");
			btnBrowse.setEnabled(false);
			
		}
		else if (ev.getActionCommand()=="RadioURN")
		{
			log.debug("URN");
			txtNewFile.setText("urn:");
			btnBrowse.setEnabled(false);
		}
		else if (ev.getActionCommand()=="RadioFile")
		{
			log.debug("File");
			txtNewFile.setText("file:/");
			btnBrowse.setEnabled(true);
		}
		else if (ev.getActionCommand()=="AddToList")
		{
			log.debug("Adding reference to list");
			try{
			URI uri = URI.create(txtNewFile.getText());
			
			log.debug("URI created");
			log.debug("Host: "+ uri.getHost());
			log.debug("Path: "+uri.getPath());
			
			((DefaultListModel<URI>) lstFileList.getModel()).addElement(uri);
			
		
			
			} catch (IllegalArgumentException e)
			{
				Alert.error("Invalid", "The reference you supplied is not valid");
				return;
			}
		}
		else if (ev.getActionCommand()=="DeleteFromList")
		{
			log.debug("Deleting reference(s) from list");
			int[] indices = lstFileList.getSelectedIndices();
			for(int i : indices)
			{
				log.debug("Deleting index "+i);
				((DefaultListModel<URI>)lstFileList.getModel()).remove(i);
			}

		}
		else if (ev.getActionCommand()=="Open")
		{
			log.debug("Open selected reference(s) ");

			int[] indices = lstFileList.getSelectedIndices();
			for(int i : indices)
			{
				log.debug("Deleting index "+i);

				Desktop desktop = Desktop.getDesktop();

				try {
					URI uri = lstFileList.getModel().getElementAt(i);

					
					if(uri.getScheme().equals("urn"))
					{
						URI uri2 = URI.create("http://wm-urn.org/"+uri.toString());
						desktop.browse(uri2);
					}
					else
					{
						desktop.browse(uri);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
			}
		}
		else if (ev.getActionCommand()=="Browse")
		{
			File lastFolder = null;
			try{
				lastFolder = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
			} catch (Exception e){}
			
			JFileChooser fc = new JFileChooser(lastFolder);
			fc.setMultiSelectionEnabled(true);
			
			FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
				    "Image files", ImageIO.getReaderFileSuffixes());			
			FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Document (*.pdf)", "pdf");
			String[] ext = {"doc", "docx", "wps", "rtf", "txt", "odt", "wps", "wpd", "xml"};
			FileNameExtensionFilter wordDocs = new FileNameExtensionFilter("Text documents", ext);
			String[] ext2 = {"xls", "xlsx", "ods", "csv", "wks", "wk1", "123"};
			FileNameExtensionFilter spreadsheetDocs = new FileNameExtensionFilter("Spreadsheet documents", ext2);	
			
			
			fc.addChoosableFileFilter(imageFilter);
			fc.addChoosableFileFilter(pdfFilter);
			fc.addChoosableFileFilter(wordDocs);
			fc.addChoosableFileFilter(spreadsheetDocs);
			
			int returnVal = fc.showOpenDialog(null);
				
			// Get details from user
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		        File[] files = fc.getSelectedFiles();
		        
		        for(File file : files)
		        {
		        	((DefaultListModel<URI>) lstFileList.getModel()).addElement(file.toURI());
		        }
		       
		        
				// Remember this folder for next time
				App.prefs.setPref(PrefKey.FOLDER_LAST_READ, files[0].getPath());
			    
		    } else {
		    	return;
		    }
		}
		
	}

}

	