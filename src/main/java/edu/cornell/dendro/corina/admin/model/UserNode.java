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
 *     Dan Girshovich
 ******************************************************************************/

package edu.cornell.dendro.corina.admin.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import edu.cornell.dendro.corina.schema.WSISecurityUser;

public class UserNode implements Transferable {

	/**
	 * Used by UserGroupTree.MyNode for drag and drop
	 */
	private static final long serialVersionUID = 1L;
	
	final public static DataFlavor ID_FLAVOR = new DataFlavor(String.class, "User Id");
	
	static DataFlavor flavors[] = {ID_FLAVOR};
	
	private String id;
	
	public UserNode(String argId){
		id = argId;
	}
	
	public String getId(){
		return id;
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(ID_FLAVOR)) {
		      return this;
		    }
	    else {
	    	throw new UnsupportedFlavorException(flavor);
	    }
		
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(ID_FLAVOR);
	}

}
