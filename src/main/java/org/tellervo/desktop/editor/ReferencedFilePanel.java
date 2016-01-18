package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.TridasFileList;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.tridasv2.ui.ImagePreviewPanel;
import org.tellervo.desktop.tridasv2.ui.TridasFileListPanel;
import org.tellervo.desktop.tridasv2.ui.URIListTableModel;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

public class ReferencedFilePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Sample s;
	private TridasFileList tfl;

	protected final static Logger log = LoggerFactory.getLogger(ReferencedFilePanel.class);

	private JTable tblFileList;
	private JButton btnOpen;
	private ImagePreviewPanel panelPreview ;
	private JSplitPane splitPane;
	
	
	public ReferencedFilePanel()
	{
		initGUI();
	}
	
	public ReferencedFilePanel(Sample s)
	{
		initGUI();
		this.setSample(s);
	}

	public void setSample(Sample sample) {
		
		this.s = sample;
		
		
		TridasObject obj = s.getMeta(Metadata.OBJECT, TridasObject.class);
		TridasElement elm = s.getMeta(Metadata.ELEMENT, TridasElement.class);
		TridasSample samp = s.getMeta(Metadata.SAMPLE, TridasSample.class);
		
		tfl = new TridasFileList();
		if(obj.isSetFiles())
		{
			tfl.addAll(obj.getFiles());
		}
		
		if(elm.isSetFiles())
		{
			tfl.addAll(elm.getFiles());
		}
		if(samp.isSetFiles())
		{
			tfl.addAll(samp.getFiles());
		}
		
		log.debug("Tridas file list contains... "+tfl.size()+" records");
		
		for(TridasFile file : tfl)
		{
			try {
				URI uri = new URI(file.getHref());
				addToList(uri);
				
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void initGUI()
	{
		setLayout(new MigLayout("", "[grow]", "[grow,fill][]"));
		
		splitPane = new JSplitPane();
		add(splitPane, "cell 0 0,grow");
		
		panelPreview= new ImagePreviewPanel();
		splitPane.setRightComponent(panelPreview);
		panelPreview.setBorder(new TitledBorder(UIManager.getBorder("SplitPane.border"), "Preview", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		
		JPanel panelList = new JPanel();
		splitPane.setLeftComponent(panelList);
		panelList.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelList.add(scrollPane);
		
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
				}
				else
				{
					btnOpen.setEnabled(false);
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
					TridasFileListPanel.openURI(uri);
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
		
		
		
		btnOpen = new JButton("View");
		btnOpen.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] indices = tblFileList.getSelectedRows();
				for(int i : indices)
				{

					URI uri = ((URIListTableModel)tblFileList.getModel()).getURI(i);
					TridasFileListPanel.openURI(uri);				
					
				}
				
			}
			
			
			
		});
		btnOpen.setIcon(Builder.getIcon("document_preview.png", 16));
		add(btnOpen, "cell 0 1");
	}
	
	private void previewURI()
	{
		int row = tblFileList.getSelectedRow();
		
		URI uri = ((URIListTableModel)tblFileList.getModel()).getURI(row);
		if(panelPreview.loadImage(uri))
		{
			panelPreview.repaint();
			expandPreviewPanel();
		}
		else
		{
			hidePreviewPanel();
		}
	}
	

	/**
	 * If the preview panel is hidden or very small then expand
	 */
	public void expandPreviewPanel()
	{
		if(splitPane.getDividerLocation()>splitPane.getWidth()-100)
		{
			splitPane.setDividerLocation(0.7d);
		}
	}
	
	public void hidePreviewPanel()
	{
		splitPane.setDividerLocation(1.0d);
	}
	
	private void addToList(URI uri)
	{
		log.debug("Adding reference to list");
		try{
		
		
			log.debug("URI created");
			log.debug("Host: "+ uri.getHost());
			log.debug("Path: "+uri.getPath());
			
			// If URI is in list already just silently return
			for(int i=0; i<tblFileList.getModel().getRowCount(); i++)
			{
				URI u = ((URIListTableModel) tblFileList.getModel()).getURI(i);
				if(u.toString().equals(uri.toString()))
				{
					return;
				}
			}
			
			((URIListTableModel) tblFileList.getModel()).addURI(uri);
			
		
		
		} catch (IllegalArgumentException e)
		{
			log.error("The reference you supplied is not valid");
			return;
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
