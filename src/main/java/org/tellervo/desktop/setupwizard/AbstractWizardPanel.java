package org.tellervo.desktop.setupwizard;

import java.awt.Color;

import javax.swing.JPanel;

public abstract class AbstractWizardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private String title;
	private String instructions;
	private Class<? extends AbstractWizardPanel> disablePageClass = null;
	private Class<? extends AbstractWizardPanel> enablePageClass = null;
	
	public AbstractWizardPanel(String title, String instructions)
	{
		this.setTitle(title);
		this.setInstructions(instructions);
		this.setBackground(Color.WHITE);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public Class<? extends AbstractWizardPanel> getDisablePageClass()
	{		
		return disablePageClass;
	}
	
	public Class<? extends AbstractWizardPanel> getEnablePageClass()
	{		
		return enablePageClass;
	}
	
	protected void setPageClassToEnableOrDisable(Class<? extends AbstractWizardPanel> clazz, Boolean b)
	{
		if(b)
		{
			enablePageClass = clazz;
			disablePageClass = null;
		}
		else
		{
			enablePageClass = null;
			disablePageClass = clazz;
		}
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getInstructions() {
		return instructions;
	}
	
	
	
	
}
