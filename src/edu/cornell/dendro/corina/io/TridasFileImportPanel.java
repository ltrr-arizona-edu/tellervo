package edu.cornell.dendro.corina.io;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.EventListenerList;
import javax.swing.tree.TreeModel;

import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.tridas.interfaces.ITridas;
import org.tridas.io.defaults.TridasMetadataFieldSet;
import org.tridas.schema.TridasProject;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.gui.dbbrowse.TridasSelectEvent;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasSelectListener;
import edu.cornell.dendro.corina.io.view.TridasOutlineRenderData;

@SuppressWarnings("serial")
public class TridasFileImportPanel extends JPanel implements TridasSelectListener{

	private TridasProject proj;
	private TreeModel treeMdl; 
	protected EventListenerList tridasListeners = new EventListenerList();
	private Outline treeTable;
	private OutlineModel mdl;
	
	
	/**
	 * Create the panel.
	 */
	public TridasFileImportPanel(TridasProject project) {
		
		if(project==null)
		{
			TridasMetadataFieldSet tmfs = new TridasMetadataFieldSet();
			setProject(tmfs.getProjectWithDefaults(true));
		}
		else
		{
			setProject(project);
		}
		

		setupGui();

		
	}
	
	private void setupGui()
	{
	    treeMdl = new TridasTreeModel(proj);

	    //Create the Outline's model, consisting of the TreeModel and the RowModel,
	    //together with two optional values: a boolen for something or other,
	    //and the display name for the first column:
	    mdl = DefaultOutlineModel.createOutlineModel(
	            treeMdl, new TridasTreeRowModel(), true, "TRiDaS Entities");

	    
	    //Initialize the Outline object:
	    treeTable = new Outline();
	    
	    treeTable.setRenderDataProvider(new TridasOutlineRenderData());

	    //By default, the root is shown, while here that isn't necessary:
	    treeTable.setRootVisible(true);

	    //Assign the model to the Outline object:
	    treeTable.setModel(mdl);
	    
	    JScrollPane jScrollPane1 = new JScrollPane();
		//Add the Outline object to the JScrollPane:
	    jScrollPane1.setViewportView(treeTable);    
	    
	    this.setLayout(new BorderLayout());
	    
	    this.add(jScrollPane1, BorderLayout.CENTER);
	    
	    
	    treeTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
        						
				int selRow = treeTable.getSelectedRow();
				Object selected = mdl.getValueAt(selRow, 0);		

				if(selected instanceof TridasProject)
				{
					return;
				}
				else if (selected instanceof TridasObjectEx)
				{
					fireTridasSelectListener(new TridasSelectEvent(treeTable, 1, (TridasObjectEx) selected));
				}
				else if(selected instanceof ITridas)
				{
					fireTridasSelectListener(new TridasSelectEvent(treeTable, 1, (ITridas) selected));
				}
			}
		});
	}
	
	public void setProject(TridasProject project)
	{
		proj = project;
	}

	/***********
	 * LISTENERS
	 ***********/

	/**
	 * Add a listener 
	 * 
	 * @param listener
	 */
	public void addTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.add(TridasSelectListener.class, listener);
	}
	
	/**
	 * Remove a listener
	 * @param listener
	 */
	public void removeTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.remove(TridasSelectListener.class, listener);
	}
	
	/**
	 * Fire a selected entity event
	 * 
	 * @param event
	 */
	protected void fireTridasSelectListener(TridasSelectEvent event)
	{
	     Object[] listeners = tridasListeners.getListenerList();
	     // loop through each listener and pass on the event if needed
	     Integer numListeners = listeners.length;
	     for (int i = 0; i<numListeners; i+=2) 
	     {
	          if (listeners[i]==TridasSelectListener.class) 
	          {
	               // pass the event to the listeners event dispatch method
	                ((TridasSelectListener)listeners[i+1]).entitySelected(event);
	          }            
	     }

	}

	@Override
	public void entitySelected(TridasSelectEvent event) {
		fireTridasSelectListener(event);
		
	}

}
