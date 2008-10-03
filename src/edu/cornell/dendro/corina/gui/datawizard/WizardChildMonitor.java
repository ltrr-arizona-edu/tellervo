package edu.cornell.dendro.corina.gui.datawizard;

public interface WizardChildMonitor {
	
	/**
	 * Called when the content panel wants to tell us something
	 */
	public void notifyChildFormStateChanged();
}
