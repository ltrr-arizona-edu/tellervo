package org.tellervo.desktop.tridasv2.ui;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.ui.Builder;

import edu.emory.mathcs.backport.java.util.Arrays;


public class URIListTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(URIListTableModel.class);

	private ArrayList<URI> uris = new ArrayList<URI>();
	private final Icon fileIcon;
	private final Icon pdfIcon;
	private final Icon textIcon;
	private final Icon imageIcon;
	private final Icon spreadsheetIcon;
	private final Icon webIcon;
	private final Icon urnIcon;
	private final Icon zipIcon;
	
	
	public URIListTableModel()
	{
		fileIcon = Builder.getIcon("file.png", 16);
		pdfIcon = Builder.getIcon("pdf.png", 16);
		textIcon = Builder.getIcon("text.png", 16);
		imageIcon = Builder.getIcon("image.png", 16);
		spreadsheetIcon = Builder.getIcon("spreadsheet.png", 16);
		webIcon = Builder.getIcon("internet.png", 16);
		urnIcon = Builder.getIcon("link.png", 16);
		zipIcon = Builder.getIcon("zip.png", 16);
	}
	
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return uris.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		URI uri = getURI(rowIndex);
		if(uri==null) return null;
		if(uri.getScheme()==null) return null;
		String scheme = uri.getScheme();

		if(columnIndex==0)
		{

			if(scheme.equals("urn"))
			{
				return urnIcon;
			}
			else if (scheme.equals("http") || scheme.equals("https") || scheme.equals("ftp"))
			{
				return webIcon;
			}
			else if(scheme.equals("file"))
			{
				String extension = null;

				int i = uri.getPath().lastIndexOf('.');
				if (i > 0) {
				    extension = uri.getPath().substring(i+1);
				}
				
				if(extension!=null)
				{
					if(Arrays.asList(TridasFileListDialog.imageFilter.getExtensions()).contains(extension.toLowerCase()))
					{
						return imageIcon;
					}
					else if(Arrays.asList(TridasFileListDialog.wordDocs.getExtensions()).contains(extension.toLowerCase()))
					{
						return textIcon;
					}
					else if(Arrays.asList(TridasFileListDialog.pdfFilter.getExtensions()).contains(extension.toLowerCase()))
					{
						return pdfIcon;
					}
					else if(Arrays.asList(TridasFileListDialog.spreadsheetDocs.getExtensions()).contains(extension.toLowerCase()))
					{
						return spreadsheetIcon;
					}
					else if(Arrays.asList(TridasFileListDialog.zipFilter.getExtensions()).contains(extension.toLowerCase()))
					{
						return zipIcon;
					}
				}
			}

			
			return fileIcon;
			
		}
		else 
		{
			if(scheme.equals("file"))
			{
				return uri.getPath();
			}
			return uri.toString();
		}	
		
	}
	
	public URI getURI(int index)
	{
		try{
			return uris.get(index);
		} catch (IndexOutOfBoundsException e)
		{
			
		}
		return null;
	}
	
	public ArrayList<URI> getURIs()
	{
		return uris;
	}
	
	public void addURI(URI uri)
	{
		uris.add(uri);
		fireTableDataChanged();
	}
	
	public void setURI(ArrayList<URI> uris)
	{
		this.uris = uris;
		fireTableDataChanged();
	}
	
	public void removeURI(int index)
	{
		uris.remove(index);
		fireTableDataChanged();
	}

}
