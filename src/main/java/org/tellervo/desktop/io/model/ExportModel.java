/**
 * Created at Jan 19, 2011, 1:38:29 PM
 */
package org.tellervo.desktop.io.model;

import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;

import org.tellervo.desktop.sample.ElementList;

import com.dmurph.mvc.model.AbstractModel;
import com.dmurph.mvc.model.MVCArrayList;



/**
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class ExportModel extends AbstractModel {

	public static enum Grouping {
		PACKED, SEPARATE
	}
	private String format = null;
	private ElementList elements = null;
	private final MVCArrayList<DefaultMutableTreeNode> nodes = new MVCArrayList<DefaultMutableTreeNode>();
	private JFrame exportView;
	private String exportDirectory = null;

	/**
	 * @return the exportDirectory
	 */
	public String getExportDirectory() {
		return exportDirectory;
	}

	/**
	 * @param argExportDirectory the exportDirectory to set
	 */
	public void setExportDirectory(String argExportDirectory) {
		String old = exportDirectory;
		exportDirectory = argExportDirectory;
		firePropertyChange("exportDirectory", old, exportDirectory);
	}

	/**
	 * @return the rootNode
	 */
	public MVCArrayList<DefaultMutableTreeNode> getNodes() {
		return nodes;
	}
	
	/**
	 * @return the exportView
	 */
	public JFrame getExportView() {
		return exportView;
	}

	/**
	 * @param argExportView the exportView to set
	 */
	public void setExportView(JFrame argExportView) {
		JFrame old = exportView;
		exportView = argExportView;
		firePropertyChange("exportView", old, exportView);
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
