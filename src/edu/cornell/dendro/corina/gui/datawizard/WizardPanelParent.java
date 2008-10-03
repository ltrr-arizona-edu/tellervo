package edu.cornell.dendro.corina.gui.datawizard;

import java.awt.Dimension;

public interface WizardPanelParent {
	/**
	 * Get the dimensions of the window we're going to be in.
	 * @return
	 */
	public Dimension getContainerPreferredSize();
	
	/**
	 * Tell our parent that our ready state changed,
	 * so it can enable/disable the 'next' button
	 */
	public void notifyPanelStateChanged(BaseContentPanel<?> panel);
	
	/**
	 * Get a defaults object for the specified class
	 */
	public Object getDefaultsForClass(Class<?> clazz);
}
