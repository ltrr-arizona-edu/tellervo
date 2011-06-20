package edu.cornell.dendro.corina.prefs.panels;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import edu.cornell.dendro.corina.ui.Builder;

public abstract class AbstractPreferencesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private String title;
	private Icon icon;
	private String subTitle;
	private JToggleButton tabButton;
	
	
	/**
	 * 
	 * @param title
	 * @param iconfilename
	 * @param subTitle
	 */
	public AbstractPreferencesPanel(String title, String iconfilename, String subTitle)
	{
		this.title = title;
		this.icon = Builder.getIcon(iconfilename, 48);
		this.subTitle = subTitle;
		tabButton = new JToggleButton();
		tabButton.setFont(new Font("Dialog", Font.PLAIN, 10));
		tabButton.setText(title);
		tabButton.setIcon(icon);
		
		
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Icon getIcon()
	{
		return icon;
	}
	
	public String getSubTitle()
	{
		return subTitle;
	}
		
	public JToggleButton getTabButton()
	{
		return tabButton;
	}
	
	public String getPageTitle()
	{
		return title + " Preferences";
	}
	
	public abstract void refresh();
	
	
}
