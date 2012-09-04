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
//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package org.tellervo.desktop.gui;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.tellervo.desktop.ui.I18n;


@SuppressWarnings("serial")
public class ConfirmSave extends JDialog {

    public static void showDialog(SaveableDocument doc)
    {
    	if(doc instanceof Component)
    	{	
    		showDialog(doc, (Component)doc);
    	}
    	else
    	{
    		showDialog(doc, null);
    	}
    }
    
    private static void showDialog(SaveableDocument doc, Component parent) {
    	    	
		Object[] options = {I18n.getText("general.save"),
				I18n.getText("general.discard"),
                I18n.getText("general.cancel")};

		int n = JOptionPane.showOptionDialog(parent, 
			"The series \""+ doc.getDocumentTitle() + "\" has been modified.\nDo you want to save your changes?",
			"Save changes?",
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			options[0]);
		
		if(n==JOptionPane.CANCEL_OPTION)
		{
			return;
		}
		
		if(n==JOptionPane.NO_OPTION)
		{
			((JFrame) doc).dispose();
		}
		
		if(n==JOptionPane.YES_OPTION)
		{
		    // save, then close document
		    doc.save();
		    
		    // only close the dialog if they actually went through with the save
		    if(doc.isSaved())  	((JFrame) doc).dispose();
		}
    }
    	
}
