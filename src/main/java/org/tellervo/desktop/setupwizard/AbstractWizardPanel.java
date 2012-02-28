package org.tellervo.desktop.setupwizard;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

public abstract class AbstractWizardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private String title;
	private String instructions;
	private ArrayList<Class<?extends AbstractWizardPanel>> disablePageClassArray = new ArrayList<Class<?extends AbstractWizardPanel>>();
	private ArrayList<Class<?extends AbstractWizardPanel>> enablePageClassArray= new ArrayList<Class<?extends AbstractWizardPanel>>();
	
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
	
	public ArrayList<Class<?extends AbstractWizardPanel>> getDisablePageClassArray()
	{		
		return disablePageClassArray;
	}
	
	public ArrayList<Class<?extends AbstractWizardPanel>> getEnablePageClassArray()
	{		
		return enablePageClassArray;
	}
	
	protected void setPageClassToEnableOrDisable(Class<? extends AbstractWizardPanel> clazz, Boolean b)
	{

		if(disablePageClassArray.contains(clazz)) disablePageClassArray.remove(clazz);
		if(enablePageClassArray.contains(clazz)) enablePageClassArray.remove(clazz);

		
		if(b)
		{
			enablePageClassArray.add(clazz);
		}
		else
		{
			disablePageClassArray.add(clazz);
		}
	
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getInstructions() {
		return instructions;
	}
	
	/**
	 * Function to override if tasks should be performed when page 
	 * is shown
	 */
	public void initialViewTasks()
	{
		
	}
	
	
}
