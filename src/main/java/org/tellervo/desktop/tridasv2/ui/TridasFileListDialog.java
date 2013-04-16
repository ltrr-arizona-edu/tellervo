package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasFile;

public class TridasFileListDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(TridasFileListDialog.class);

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNewFile;

	private JRadioButton radFile;
	private JRadioButton radWebpage;
	private JRadioButton radURN;
	private ButtonGroup radioButtons;
	private JButton btnBrowse;
	private JButton btnOpen;
	private JButton btnRemove;
	private JTable tblFileList;
	private Boolean hasResults=false;
	private ImagePreviewPanel previewPanel;
	
	public static FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());			
	public static FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF documents", "pdf");
	public static FileNameExtensionFilter wordDocs = new FileNameExtensionFilter("Text documents", new String[] {"doc", "docx", "wps", "rtf", "txt", "odt", "wps", "wpd", "xml"});
	public static FileNameExtensionFilter spreadsheetDocs = new FileNameExtensionFilter("Spreadsheet documents", new String[] {"xls", "xlsx", "ods", "csv", "wks", "wk1", "123"});
	public static FileNameExtensionFilter zipFilter = new FileNameExtensionFilter("Archives", new String[] {"zip", "gz", "tar", "rar"});	

	
	
	/**
	 * Create the dialog.
	 */
	public TridasFileListDialog(Component parent, ArrayList<TridasFile> fileList) {
		

		
		radioButtons = new ButtonGroup();
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 619, 331);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[285.00px,grow,left][][]", "[grow]"));
		{
			JSplitPane splitPane = new JSplitPane();
			contentPanel.add(splitPane, "cell 0 0 3 1,grow");
			{
				JPanel panel = new JPanel();
				splitPane.setLeftComponent(panel);
				panel.setLayout(new MigLayout("", "[][grow]", "[][][grow,fill][]"));
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, "cell 1 0");
				FlowLayout fl_panel_1 = (FlowLayout) panel_1.getLayout();
				fl_panel_1.setAlignment(FlowLayout.LEFT);
				{
					radFile = new JRadioButton("File from file system");
					radFile.setFont(new Font("Dialog", Font.PLAIN, 12));
					radFile.setSelected(true);
					radFile.setActionCommand("RadioFile");
					radFile.addActionListener(this);
					panel_1.add(radFile);
				}
				{
					radWebpage = new JRadioButton("Webpage");
					radWebpage.setActionCommand("RadioWebpage");
					radWebpage.addActionListener(this);
					radWebpage.setFont(new Font("Dialog", Font.PLAIN, 12));
					panel_1.add(radWebpage);
				}
				{
					radURN = new JRadioButton("URN");
					radURN.setActionCommand("RadioURN");
					radURN.addActionListener(this);
					radURN.setFont(new Font("Dialog", Font.PLAIN, 12));
					panel_1.add(radURN);
				}
				radioButtons.add(radFile);
				radioButtons.add(radWebpage);
				radioButtons.add(radURN);
				{
					JLabel lblAddANew = new JLabel("Add:");
					panel.add(lblAddANew, "cell 0 1");
					lblAddANew.setFont(new Font("Dialog", Font.PLAIN, 12));
				}
				{
					txtNewFile = new JTextField("");
					panel.add(txtNewFile, "flowx,cell 1 1,growx");
					txtNewFile.setColumns(10);
					txtNewFile.setActionCommand("AddToList");
					txtNewFile.addActionListener(this);
					JScrollPane scrollPane = new JScrollPane();
					panel.add(scrollPane, "cell 0 2 2 1,growx");
					tblFileList = new JTable();
					tblFileList.setModel(new URIListTableModel());
					tblFileList.setRowSelectionAllowed(true);
					tblFileList.setColumnSelectionAllowed(false);
					tblFileList.setShowVerticalLines(false);
					tblFileList.setShowHorizontalLines(false);
					tblFileList.setBackground(Color.WHITE);
					tblFileList.setOpaque(true);
					tblFileList.getColumnModel().getColumn(0).setCellRenderer(new TableCellIconRenderer());
					tblFileList.setTableHeader(null);
					tblFileList.getColumnModel().getColumn(0).setMinWidth(18);
					tblFileList.getColumnModel().getColumn(0).setMaxWidth(18);
					tblFileList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
					
					tblFileList.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

						@Override
						public void valueChanged(ListSelectionEvent arg0) {
							if(tblFileList.getSelectedRows().length>0)
							{
								btnOpen.setEnabled(true);
								btnRemove.setEnabled(true);
							}
							else
							{
								btnOpen.setEnabled(false);
								btnRemove.setEnabled(false);
							}

							
						}
					});
					
					tblFileList.addMouseListener(new MouseListener(){

						@Override
						public void mouseClicked(MouseEvent ev) {
							int row = tblFileList.rowAtPoint(ev.getPoint());
							URI uri = ((URIListTableModel)tblFileList.getModel()).getURI(row);
							
							if(ev.getClickCount()>1)
							{
								TridasFileListDialog.openURI(uri);
							}
							else
							{
								previewURI();
							}
						}

						@Override
						public void mouseEntered(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseExited(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mousePressed(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseReleased(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
						
					});
							
							scrollPane.setViewportView(tblFileList);
							scrollPane.getViewport().setBackground(Color.WHITE);
							scrollPane.getViewport().setOpaque(true);
							tblFileList.setFont(new Font("Dialog", Font.PLAIN, 12));
							tblFileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
							
							{
								btnBrowse = new JButton();
								panel.add(btnBrowse, "cell 1 1");
								btnBrowse.setActionCommand("Browse");
								btnBrowse.addActionListener(this);
								btnBrowse.setIcon(Builder.getIcon("open.png", 16));
							}
							JButton btnAdd = new JButton();
							panel.add(btnAdd, "cell 1 1");
							btnAdd.setIcon(Builder.getIcon("edit_add.png", 16));
							btnAdd.setActionCommand("AddToList");
							btnRemove = new JButton();
							panel.add(btnRemove, "flowx,cell 1 3,alignx right");
							btnRemove.setIcon(Builder.getIcon("cancel.png", 16));
							btnRemove.setActionCommand("DeleteFromList");
							btnRemove.setEnabled(false);
							{
								btnOpen = new JButton();
								panel.add(btnOpen, "cell 1 3,alignx right");
								btnOpen.setIcon(Builder.getIcon("document_preview.png", 16));
								btnOpen.setToolTipText("View document");
								btnOpen.setActionCommand("Open");
								btnOpen.addActionListener(this);
								btnOpen.setEnabled(false);
							}
							btnRemove.addActionListener(this);
							btnAdd.addActionListener(this);
							{
								JPanel previewPanelHolder = new JPanel();
								splitPane.setRightComponent(previewPanelHolder);
								splitPane.setResizeWeight(1.0);
								previewPanelHolder.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
								{
									previewPanelHolder.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), "Image preview", TitledBorder.LEADING, TitledBorder.TOP, null, null));
								}
								previewPanelHolder.setMinimumSize(new Dimension(128,128));
																
								previewPanel = new ImagePreviewPanel();
								previewPanel.setLayout(new MigLayout("", "[grow]", "[grow,fill]"));
								
								previewPanelHolder.add(previewPanel, "cell 0 0,grow");
								
								previewPanelHolder.addComponentListener(new ComponentListener(){

									@Override
									public void componentHidden(
											ComponentEvent arg0) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void componentMoved(
											ComponentEvent arg0) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void componentResized(
											ComponentEvent arg0) {
											previewURI();
										
									}

									@Override
									public void componentShown(
											ComponentEvent arg0) {
										// TODO Auto-generated method stub
										
									}
									
								});
								

							}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.setLayout(new MigLayout("", "[149px][grow][54px][81px]", "[25px]"));
				buttonPane.add(okButton, "cell 2 0,alignx left,aligny top");
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton, "cell 3 0,alignx left,aligny top");
				cancelButton.addActionListener(this);
			}
		}
		
		setFileList(fileList);		
		this.setTitle("References Files");
		this.setLocationRelativeTo(parent);
	}
	
	private void previewURI()
	{
		int row = tblFileList.getSelectedRow();
		
		URI uri = ((URIListTableModel)tblFileList.getModel()).getURI(row);
		previewPanel.loadImage(uri);
		previewPanel.repaint();
	}
	
	public static void openURI(URI uri)
	{
		Desktop desktop = Desktop.getDesktop();
		
		try{
			if(uri.getScheme().equals("urn"))
			{
				URI uri2 = URI.create("http://wm-urn.org/"+uri.toString());
				desktop.browse(uri2);
			}
			else
			{
				desktop.browse(uri);
			}
		}
		catch (IOException ex)
		{
			log.debug("Unable to open URI");
			Alert.error("Error", "Unable to open URI.\n\n"+ex.getLocalizedMessage());
		}
	}
	
	private void setFileList(ArrayList<TridasFile> fileList)
	{
		for(TridasFile f : fileList)
		{
			try{
			URI uri = URI.create(f.getHref());
					
			((URIListTableModel)tblFileList.getModel()).addURI(uri);
			} catch (IllegalArgumentException ex)
			{
				Alert.error("Illegal Link", "The file link '"+f.getHref()+"' stored in the database is not a valid URI");
			}
			
		}
	}
	

	public ArrayList<TridasFile> getFileList()
	{
		ArrayList<TridasFile> fileList = new ArrayList<TridasFile>();
		
		for(int i=0; i<tblFileList.getModel().getRowCount(); i++)
		{
			URI uri = ((URIListTableModel)tblFileList.getModel()).getURI(i);
			
			TridasFile f = new TridasFile();
			f.setHref(uri.toString());
			
			fileList.add(f);
			
		}
		
		return fileList;
	}
	
	public Boolean hasResults()
	{
		return this.hasResults;
	}
	
	private void addToList()
	{
		log.debug("Adding reference to list");
		try{
		URI uri = URI.create(txtNewFile.getText());
		
		log.debug("URI created");
		log.debug("Host: "+ uri.getHost());
		log.debug("Path: "+uri.getPath());
		
		((URIListTableModel) tblFileList.getModel()).addURI(uri);
		
	
		
		} catch (IllegalArgumentException e)
		{
			Alert.error("Invalid", "The reference you supplied is not valid");
			return;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		
		if(ev.getActionCommand()=="Cancel")
		{
			hasResults=false;
			this.dispose();
		}
		if(ev.getActionCommand()=="OK")
		{
			
			if(tblFileList.getModel().getRowCount()>0) 
			{
				hasResults=true;
			}
			else
			{
				hasResults=false;
			}
			
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
			txtNewFile.setText("");
			btnBrowse.setEnabled(true);
		}
		else if (ev.getActionCommand()=="AddToList")
		{
			addToList();
		}
		else if (ev.getActionCommand()=="DeleteFromList")
		{
			log.debug("Deleting reference(s) from list");
			int[] indices = tblFileList.getSelectedRows();
			for(int i : indices)
			{
				log.debug("Deleting index "+i);
				((URIListTableModel)tblFileList.getModel()).removeURI(i);
			}

		}
		else if (ev.getActionCommand()=="Open")
		{
			log.debug("Open selected reference(s) ");

			int[] indices = tblFileList.getSelectedRows();
			for(int i : indices)
			{

				URI uri = ((URIListTableModel)tblFileList.getModel()).getURI(i);
				openURI(uri);				
				
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
			

			
			
			fc.addChoosableFileFilter(imageFilter);
			fc.addChoosableFileFilter(pdfFilter);
			fc.addChoosableFileFilter(wordDocs);
			fc.addChoosableFileFilter(spreadsheetDocs);
			fc.addChoosableFileFilter(zipFilter);
			
			int returnVal = fc.showOpenDialog(null);
				
			// Get details from user
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		        File[] files = fc.getSelectedFiles();
		        
		        for(File file : files)
		        {
		        	((URIListTableModel)tblFileList.getModel()).addURI(file.toURI());
		        }
		       
		        
				// Remember this folder for next time
				App.prefs.setPref(PrefKey.FOLDER_LAST_READ, files[0].getPath());
			    
		    } else {
		    	return;
		    }
		}

		
	}

	class TableCellIconRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		public TableCellIconRenderer() {
		  }

		  public Component getTableCellRendererComponent(JTable table, Object value,
		      boolean isSelected, boolean hasFocus, int row, int column) {
		   
			  JLabel label = new JLabel();
			 		    
		    
		    try{
		    	label.setIcon((Icon) value);
		    } catch (Exception e){}
		    
		    return label;
		  }
		}
	
}

	