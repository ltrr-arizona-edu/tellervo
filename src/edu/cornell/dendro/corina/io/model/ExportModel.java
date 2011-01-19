/**
 * Created at Jan 19, 2011, 1:38:29 PM
 */
package edu.cornell.dendro.corina.io.model;

import java.nio.charset.Charset;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.dmurph.mvc.model.AbstractModel;
import edu.cornell.dendro.corina.sample.ElementList;


/**
 * @author Daniel
 *
 */
public class ExportModel extends AbstractModel {

	public static enum Grouping {
		PACKED, SEPARATE
	}
	
	private String directory = null;
	private String format = null;
	private ElementList elements = null;
	private final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();

	/**
	 * @return the rootNode
	 */
	public DefaultMutableTreeNode getRootNode() {
		return rootNode;
	}
	
	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}
	/**
	 * @param argDirectory the directory to set
	 */
	public void setDirectory(String argDirectory) {
		String old = directory;
		directory = argDirectory;
		firePropertyChange("directory", old, directory);
	}
	
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	
	/**
	 * @param argFormat the format to set
	 */
	public void setFormat(String argFormat) {
		String old = format;
		format = argFormat;
		firePropertyChange("format", old, format);
	}
	
	/**
	 * @return the elements
	 */
	public ElementList getElements() {
		return elements;
	}
	
	/**
	 * @param argElements the elements to set
	 */
	public void setElements(ElementList argElements) {
		ElementList old = elements;
		elements = argElements;
		firePropertyChange("elements", old, elements);
	}
}
