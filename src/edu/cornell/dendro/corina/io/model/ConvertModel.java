/**
 * Created at Jan 15, 2011, 6:03:34 AM
 */
package edu.cornell.dendro.corina.io.model;

import javax.swing.tree.DefaultMutableTreeNode;

import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.schema.TridasProject;

import edu.cornell.dendro.corina.tridasv2.LabCode;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author Daniel
 *
 */
public class ConvertModel extends HashModel {
	private static final long serialVersionUID = 1L;
	
	public static final String TRIDAS_PROJECTS = "tridasProjects";
	public static final String WRITER_OBJECTS = "writersObjects";
	public static final String TREE_NODES = "treeNodes";
	public static final String LAB_CODES = "labCodes";

	public ConvertModel(){
		registerProperty(TRIDAS_PROJECTS, PropertyType.READ_WRITE);
		registerProperty(WRITER_OBJECTS, PropertyType.READ_WRITE);
		registerProperty(TREE_NODES, PropertyType.FINAL, new MVCArrayList<DefaultMutableTreeNode>());
	}
	
	public ConvertModel(MVCArrayList<DefaultMutableTreeNode> argNodes){
		registerProperty(TRIDAS_PROJECTS, PropertyType.READ_WRITE);
		registerProperty(WRITER_OBJECTS, PropertyType.READ_WRITE);
		registerProperty(TREE_NODES, PropertyType.FINAL, argNodes);
	}
	
	public void setTridasProjects(TridasProject[] argProjects){
		setProperty(TRIDAS_PROJECTS, argProjects);
	}
	
	public TridasProject[] getTridasProjects(){
		return (TridasProject[])getProperty(TRIDAS_PROJECTS);
	}
	
	public void setWriterObjects(WriterObject[] argObjects){
		setProperty(WRITER_OBJECTS, argObjects);
	}
	
	public WriterObject[] getWriterObjects(){
		return (WriterObject[])getProperty(WRITER_OBJECTS);
	}
	
	@SuppressWarnings("unchecked")
	public MVCArrayList<DefaultMutableTreeNode> getNodes(){
		return (MVCArrayList<DefaultMutableTreeNode>) getProperty(TREE_NODES);
	}
	public LabCode[] getLabCodes(){
		return (LabCode[]) getProperty(LAB_CODES);
	}
	
	public void setLabCodes(LabCode[] argCodes){
		setProperty(LAB_CODES, argCodes);
	}
	
	public static class WriterObject {
		public String file;
		public String errorMessage = null;
		public AbstractDendroCollectionWriter writer = null;
		public boolean warnings = false;
	}
}
