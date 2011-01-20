/**
 * Created at Jan 15, 2011, 5:51:41 AM
 */
package edu.cornell.dendro.corina.io.command;

import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.IDendroFile;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.exceptions.IncompleteTridasDataException;
import org.tridas.io.exceptions.IncorrectDefaultFieldsException;
import org.tridas.io.naming.HierarchicalNamingConvention;
import org.tridas.io.naming.INamingConvention;
import org.tridas.io.naming.NumericalNamingConvention;
import org.tridas.io.naming.UUIDNamingConvention;
import org.tridas.schema.TridasProject;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.components.popup.ProgressPopup;
import edu.cornell.dendro.corina.components.popup.ProgressPopupModel;
import edu.cornell.dendro.corina.io.control.ConvertEvent;
import edu.cornell.dendro.corina.io.model.ConvertModel;
import edu.cornell.dendro.corina.io.model.ConvertModel.WriterObject;
import edu.cornell.dendro.corina.tridasv2.LabCodeFormatter;


/**
 * @author Daniel
 *
 */
public class ConvertCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(ConvertCommand.class);
	
	public void execute(MVCEvent argEvent) {
		ConvertEvent event = (ConvertEvent) argEvent;
		String outputFormat = event.outputFormat;
		ConvertModel model = event.model;
		INamingConvention naming;
		
		try {
			MVC.splitOff();
		} catch (IllegalThreadException e1) {
			e1.printStackTrace();
		} catch (IncorrectThreadException e1) {
			e1.printStackTrace();
		}
		
		boolean outputFormatFound = false;
		for (String format : TridasIO.getSupportedWritingFormats()) {
			if (format.equalsIgnoreCase(outputFormat)) {
				outputFormat = format;
				outputFormatFound = true;
			}
		}
		
		if (!outputFormatFound) {
			JOptionPane.showMessageDialog(null, "Error finding output format "+ outputFormat,
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		convertFiles(model, event.namingConvention, outputFormat, event.modal);
	}
	
	private void convertFiles(ConvertModel model, INamingConvention argNaming, String argFormat, JFrame argModal) {
		
		ProgressPopup storedConvertProgress = null;
		try {
			
			ProgressPopupModel dialogModel = new ProgressPopupModel();
			dialogModel.setCancelString("Cancel");
			
			final ProgressPopup convertProgress = new ProgressPopup(argModal, true, dialogModel);
			storedConvertProgress = convertProgress;
			// i have to do this in a different thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					convertProgress.setVisible(true);
				}
			});
			
			TridasProject[] projects = model.getTridasProjects();
			
			ArrayList<WriterObject> structs = new ArrayList<ConvertModel.WriterObject>();
			
			for (int i = 0; i < projects.length; i++) {
				if(dialogModel.isCanceled()){
					break;
				}
				
				TridasProject project = projects[i];
				WriterObject struct = new WriterObject();
				structs.add(struct);
				
				dialogModel.setStatusString("Converting project '"+project.getTitle()+"'");
				
				AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(argFormat);
				
				if (argNaming instanceof NumericalNamingConvention) {
					((NumericalNamingConvention) argNaming).setBaseFilename(LabCodeFormatter.getDefaultFormatter().format(model.getLabCodes()[i]).toString());
				}
				writer.setNamingConvention(argNaming);
				
				
				if (struct.errorMessage != null) {
					continue;
				}
				
				try {
					writer.loadProject(project);
				} catch (IncompleteTridasDataException e) {
					struct.errorMessage = e.getMessage();
				} catch (ConversionWarningException e) {
					struct.errorMessage = e.getLocalizedMessage();
				}
				struct.writer = writer;
				
				
				if (struct.errorMessage==null && writer.getFiles().length == 0) {
					struct.errorMessage = "No files to write";
				}
				
				if(writer.getWarnings().length != 0){
					struct.warnings = true;
				}
				
				dialogModel.setPercent(i * 100 / projects.length);
			}
			constructNodes(model, structs, argNaming);
			
		} catch (Exception e) {
			log.error("Exception thrown while converting", e);
			throw new RuntimeException(e);
		} finally {
			if (storedConvertProgress != null) {
				storedConvertProgress.setVisible(false);
			}
		}
	}
	
	private void constructNodes(ConvertModel argModel, ArrayList<WriterObject> list, INamingConvention argNaming) {
		ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		
		argModel.getRootNode().removeAllChildren();
		argModel.setWriterObjects(list.toArray(new WriterObject[0]));
		
		// we need to set processed/failed/convertedWithWarnings
		int processed = 0;
		int failed = 0;
		int convWWarnings = 0;
		
		for (WriterObject s : list) {
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(new StructWrapper(s));
			nodes.add(leaf);
			
			processed++;
			
			boolean fail = false;
			if (s.errorMessage != null) {
				DefaultMutableTreeNode errorMessage = new DefaultMutableTreeNode(s.errorMessage);
				leaf.add(errorMessage);
				nodes.add(leaf);
				failed++;
				fail = true;
			}
			
			if (s.writer != null) {
				for (IDendroFile file : s.writer.getFiles()) {
					DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(new DendroWrapper(file, argNaming));
					
					if (file.getDefaults().getWarnings().size() != 0) {
						// put them all in a hash set first so no duplicates.
						HashSet<String> set = new HashSet<String>();
						
						for (ConversionWarning warning : file.getDefaults().getWarnings()) {
							set.add(warning.toStringWithField());
						}
						
						for(String warning : set){
							DefaultMutableTreeNode warningNode = new DefaultMutableTreeNode(warning);
							fileNode.add(warningNode);
						}
					}
					leaf.add(fileNode);
				}
			}
			
			
			if (s.writer != null && s.writer.getWarnings().length != 0) {
				DefaultMutableTreeNode writerWarnings = new DefaultMutableTreeNode("Writer Warnings");
				
				// put them all in a hash set first so no duplicates.
				HashSet<String> set = new HashSet<String>();
				for (ConversionWarning warning : s.writer.getWarnings()) {
					set.add(warning.toStringWithField());
				}
				
				for(String warning : set){
					DefaultMutableTreeNode warn = new DefaultMutableTreeNode(warning);
					writerWarnings.add(warn);
				}
				leaf.add(writerWarnings);
			}
			
			if (s.warnings) {
				if (!fail) { // make sure we didn't already count this file as a fail
					convWWarnings++;
				}
			}
		}
		
		for(DefaultMutableTreeNode node : nodes){
			argModel.getRootNode().add(node);
		}
	}
//	
//	
//	// wrapper for putting in tree nodes
	public static class StructWrapper {
		public WriterObject struct;
		
		public StructWrapper(WriterObject argStruct) {
			struct = argStruct;
		}
		
		@Override
		public String toString() {
			return struct.file;
		}
	}
	public static class DendroWrapper {
		public IDendroFile file;
		public INamingConvention convention;
		
		public DendroWrapper(IDendroFile argFile, INamingConvention argConvention) {
			file = argFile;
			convention = argConvention;
		}
		
		@Override
		public String toString() {
			return convention.getFilename(file) + "." + file.getExtension();
		}
	}
}
