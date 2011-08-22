/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.prefs.panels;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import edu.cornell.dendro.corina.ui.Builder;

public abstract class AbstractPreferencesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private String title;
	private Icon icon;
	private String subTitle;
	private JToggleButton tabButton;
	private JDialog parent;
	
	/**
	 * 
	 * @param title
	 * @param iconfilename
	 * @param subTitle
	 */
	public AbstractPreferencesPanel(String title, String iconfilename, String subTitle, JDialog parent)
	{
		this.title = title;
		this.icon = Builder.getIcon(iconfilename, 48);
		this.subTitle = subTitle;
		this.parent = parent;
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
