package edu.cornell.dendro.corina.io.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.tridas.interfaces.ITridas;
import org.tridas.io.exceptions.ConversionWarning;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow.ImportAction;

public class ImportModel extends HashModel {

	private static final long serialVersionUID = 1L;
	public static final String TREE_MODEL = "treeModel";
	public static final String CONVERSION_WARNINGS = "conversionWarnings";
	public static final String SELECTED_NODE = "selectedNode";
	public static final String ORIGINAL_FILE = "originalFile";
			
	public ImportModel()
	{
		registerProperty(ImportModel.TREE_MODEL, PropertyType.FINAL, new TridasRepresentationTreeModel());
		registerProperty(ImportModel.CONVERSION_WARNINGS, PropertyType.FINAL, new MVCArrayList<ConversionWarning>());
		registerProperty(ImportModel.SELECTED_NODE, PropertyType.FINAL, new TridasRepresentationTableTreeRow());
		registerProperty(ImportModel.ORIGINAL_FILE, PropertyType.FINAL, new File("blah"));
	}
	
	/**
	 * Get the TRiDaS tree model representing the legacy file 
	 * @return
	 */
	public TridasRepresentationTreeModel getTreeModel()
	{
		return (TridasRepresentationTreeModel) getProperty(ImportModel.TREE_MODEL);
	}
		
	/**
	 * Set the tree model to use
	 * @param tm
	 */
	public void setTreeModel(TridasRepresentationTreeModel tm)
	{
		setProperty(ImportModel.TREE_MODEL, tm);
	}
	
	/**
	 * Set the file we are going to import
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void setFileToImport(File file) throws IOException
	{

		getFileContents(file);
				
		setProperty(ImportModel.ORIGINAL_FILE, file);

	}
	
	/**
	 * Get the file we are going to import
	 * 
	 * @return
	 */
	public File getFileToImport()
	{
		return (File) getProperty(ImportModel.ORIGINAL_FILE);
	}
	
	/**
	 * Get the contents of the file we are going to import
	 * @return
	 */
	public String getFileToImportContents()
	{
		try {
			return getFileContents((File) getProperty(ImportModel.ORIGINAL_FILE));
		} catch (IOException e) { }
		return null;
	}
	
	/**
	 * Set the selected node
	 * @param node
	 */
	public void setSelectedNode(TridasRepresentationTableTreeRow node)
	{
		setProperty(ImportModel.SELECTED_NODE, node);
	}
	
	/**
	 * Get the currently selected node
	 * 
	 * @return
	 */
	public TridasRepresentationTableTreeRow getSelectedNode()
	{
		return (TridasRepresentationTableTreeRow) getProperty(ImportModel.SELECTED_NODE);
	}
	
	/**
	 * Get array of conversion warnings
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ConversionWarning[] getConversionWarnings()
	{
		MVCArrayList list = (MVCArrayList) getProperty(ImportModel.CONVERSION_WARNINGS);
		return (ConversionWarning[]) list.toArray();
	}
	
	/**
	 * Set the list of conversion warnings
	 * @param warnings
	 */
	public void setConversionWarnings(ConversionWarning[] warnings)
	{
		MVCArrayList<ConversionWarning> list = new MVCArrayList<ConversionWarning>();
		for(ConversionWarning warning : warnings)
		{
			list.add(warning);
		}
		setProperty(ImportModel.CONVERSION_WARNINGS, list);
	}
	
	
	private String getFileContents(File file) throws IOException
	{
	    //...checks on aFile are elided
	    StringBuilder contents = new StringBuilder();

	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      BufferedReader input =  new BufferedReader(new FileReader(file));
	      try {
	        String line = null; //not declared within while loop
	        /*
	        * readLine is a bit quirky :
	        * it returns the content of a line MINUS the newline.
	        * it returns null only for the END of the stream.
	        * it returns an empty String if two newlines appear in a row.
	        */
	        while (( line = input.readLine()) != null){
	          contents.append(line);
	          contents.append(System.getProperty("line.separator"));
	        }
	      }
	      finally {
	        input.close();
	      }
	    
	    
	    return contents.toString();
	  
	}
	
	
}
