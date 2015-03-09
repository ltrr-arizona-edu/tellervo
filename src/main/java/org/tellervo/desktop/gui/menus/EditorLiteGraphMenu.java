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
package org.tellervo.desktop.gui.menus;

import java.awt.Window;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tellervo.desktop.gui.menus.actions.GraphCurrentSeriesAction;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.I18n;

@SuppressWarnings("serial")
public class EditorLiteGraphMenu extends JMenu {

	private JMenuItem plot;

	
	public EditorLiteGraphMenu(Window parent, Sample s) {
		super(I18n.getText("menus.graph")); // i18n bypasses mnemonic here!

		// plot
		Action graphSeriesAction = new GraphCurrentSeriesAction(s);
		plot = new JMenuItem(graphSeriesAction);
		add(plot);
		
		
	}



}
