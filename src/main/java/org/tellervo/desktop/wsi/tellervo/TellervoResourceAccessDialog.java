/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.wsi.tellervo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;


public class TellervoResourceAccessDialog extends JDialog implements ResourceEventListener {
	private static final long serialVersionUID = 1L;
	
	private JList list;
	private TellervoResource myResource;
	private boolean success = false;
	private Boolean completed = false;
	private Exception failException = null;
	private JProgressBar progressBar;
	private Boolean runInBackground = false;
	private Component parent;
	private Integer currentProgress = null;
	private Integer totalProgress = null;
	
	/**
	 * Construct an access dialog with no owner parent (not preferable)
	 * @param resource
	 */
	public TellervoResourceAccessDialog(TellervoResource resource, Integer progress, Integer totalProgress) {
		super();
		initialize(resource);
		setProgress(progress, totalProgress);
	}

	/**
	 * Construct an access dialog as a parent of a dialog
	 * @param dialog
	 * @param resource
	 */
	public TellervoResourceAccessDialog(Dialog dialog, TellervoResource resource, Integer progress, Integer totalProgress) {
		super(dialog);
		parent = dialog;
		initialize(resource);
		setProgress(progress, totalProgress);

	}

	/**
	 * Construct an access dialog as a parent of a frame
	 * 
	 * @param frame
	 * @param resource
	 */
	public TellervoResourceAccessDialog(Frame frame, TellervoResource resource, Integer progress, Integer totalProgress) {
		super(frame);
		parent = frame;
		initialize(resource);
		setProgress(progress, totalProgress);
	}

	/**
	 * Construct an access dialog as a parent of a frame
	 * 
	 * @param frame
	 * @param resource
	 */
	public TellervoResourceAccessDialog(Window window, TellervoResource resource, Integer progress, Integer totalProgress) {
		super(window);
		parent = window;
		initialize(resource);
		setProgress(progress, totalProgress);
	}
	
	/**
	 * Construct an access dialog as a parent of a frame
	 * 
	 * @param window
	 * @param resource
	 */
	public TellervoResourceAccessDialog(Window window, TellervoResource resource) {
		super(window);
		parent = window;
		initialize(resource);
	}
	
	
	/**
	 * Construct an access dialog with no owner parent (not preferable)
	 * @param resource
	 */
	public TellervoResourceAccessDialog(TellervoResource resource) {
		super();
		
		initialize(resource);
	}

	/**
	 * Construct an access dialog as a parent of a dialog
	 * @param dialog
	 * @param resource
	 */
	public TellervoResourceAccessDialog(Dialog dialog, TellervoResource resource) {
		super(dialog);
		parent = dialog;
		initialize(resource);
	}

	/**
	 * Construct an access dialog as a parent of a frame
	 * 
	 * @param frame
	 * @param resource
	 */
	public TellervoResourceAccessDialog(Frame frame, TellervoResource resource) {
		super(frame);
		parent = frame;
		initialize(resource);
	}

	public void setProgress(Integer current, Integer whole)
	{
		this.currentProgress = current;
		this.totalProgress = whole;
		
		if(current!=null && whole!=null && current<=whole && current>=0 && whole>0)
		{
			progressBar.setIndeterminate(false);
			progressBar.setMaximum(totalProgress);
			progressBar.setValue(currentProgress);
		}
		else
		{
			progressBar.setIndeterminate(true);
		}
	}
	
	/**
	 * Convenience method that allows ease of use for dialogs OR frames
	 * 
	 * @param window
	 * @param resource
	 * @return
	 */
	public static TellervoResourceAccessDialog forWindow(Window window, TellervoResource resource) {
		if(window == null || window instanceof Frame)
			return new TellervoResourceAccessDialog((Frame) window, resource);
		else if(window instanceof Dialog)
			return new TellervoResourceAccessDialog((Dialog) window, resource);
		
		throw new IllegalArgumentException("Window isn't null, frame, or dialog?");
	}
	
	private void initialize(TellervoResource resource) {
		resource.setOwnerWindow(this);
		
		// attach to our resource
		this.myResource = resource;
		myResource.addResourceEventListener(this);
		
		// Set dialog defaults
		setModal(true);
		setTitle("Please wait...");
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		// Create form items
		list = new JList(new DefaultListModel());
		list.setVisibleRowCount(10);
	    list.setBorder(BorderFactory.createTitledBorder("Status"));
	    progressBar = new JProgressBar();
	    

	    progressBar.setIndeterminate(true);
	    
	    progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
	    
	    final TellervoResourceAccessDialog glue = this;
	    progressBar.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mousee) {
				Object[] options = new String[] { "Abort", "Continue" };
				int ret = JOptionPane.showOptionDialog(progressBar, 
						"Would you like to abort the operation\n" +
						"'" + myResource.getQueryType() + "' on '" + 
						myResource.getResourceName() + "'", 
						"Abort?", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, 
						options, options[1]);
				
				if(ret == 0) {
					myResource.removeResourceEventListener(glue);
					myResource.abortQuery();
					
					glue.failException = new UserCancelledException();
					glue.setSuccessful(false);
				}
			}
	    });
	 
	    // Add items to dialog
	    getContentPane().add(list, BorderLayout.CENTER);
	    getContentPane().add(progressBar, BorderLayout.SOUTH);
	    
	    // Add basic nerdy info to list 
		addStatus(myResource.getQueryType() + " on: " + 
				  myResource.getResourceName());
		
		// more nerdy info if this is a ResourceObject
		/*
		if(myResource instanceof ResourceObject<?>) {
			ResourceObject<? >myResourceObject = (ResourceObject<?>) myResource;
			
			if(myResourceObject.getIdentifier() != null)
				addStatus("Identifier: " + myResourceObject.getIdentifier());	

			if(myResourceObject.getSearchParameters() != null)
				addStatus("Search params: " + myResourceObject.getSearchParameters());
		}
		*/
		
		addStatus("Connecting to server...");	
		
		// Hide nerdy info
		list.setVisible(false);
	    
		// Finish up
		pack();
	    //setSize(300, 200);
		
		if(parent!=null)
		{
			this.setLocationRelativeTo(parent);
		}
		else
		{
			Center.center(this);
		}
		
	}
	
	public void addStatus(String s) {
		((DefaultListModel)list.getModel()).addElement(s);
		list.ensureIndexIsVisible(list.getModel().getSize() - 1);
	}
	
	public boolean isSuccessful() {
		return success;
	}
	
	public Exception getFailException() {
		return failException;
	}
	
	private void setSuccessful(boolean successful) {
		synchronized(completed) {
			completed = true;
		}
		this.success = successful;
		dispose();
	}
	
	public void resourceChanged(ResourceEvent re) {		
		int eventType = re.getEventType();
		switch(eventType) {
		case ResourceEvent.RESOURCE_QUERY_COMPLETE:
			setSuccessful(true);
			break;
			
		case ResourceEvent.RESOURCE_QUERY_FAILED:
			failException = re.getAttachedException();
			setSuccessful(false);
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_OUT:
			addStatus("Sent request, awaiting reply...");
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_IN:
			addStatus("Reply received, processing...");
			break;
			
		default:
			break;
		}
	}

	public void setRunInBackground(Boolean b)
	{
		this.runInBackground = b;
		if(runInBackground) {
			synchronized(completed) {
				if(completed)
					return;
			}
		}
		
		super.setVisible(false);
	}
	
	public Boolean getRunInBackground()
	{
		return runInBackground;
	}
	
	@Override
	public void setVisible(boolean visible) {
		/*if(parent != null)
		{
			parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		*/
		
		if(visible) {
			synchronized(completed) {
				if(completed)
					return;
			}
		}
		
		super.setVisible(visible);
	}
	
	public void hideDialog()
	{
		super.setVisible(false);
	}
	
	public void dispose() {
		myResource.removeResourceEventListener(this);
		if(parent != null)
		{
			parent.setCursor(Cursor.getDefaultCursor());
		}

		this.setCursor(Cursor.getDefaultCursor());
		
		super.dispose();
	}
}
