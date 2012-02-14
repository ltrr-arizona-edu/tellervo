package org.tellervo.desktop.admin.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.tellervo.desktop.schema.WSISecurityUser;


@SuppressWarnings("serial")
public class TransferableUser implements Transferable {

	public final static DataFlavor FLAVOR = new DataFlavor(WSISecurityUser.class, "User");
	private static DataFlavor[] flavors = {FLAVOR};
	private WSISecurityUser user;
	
	public TransferableUser(WSISecurityUser argUser){
		user = argUser;
	}
	
	public WSISecurityUser getUser(){
		return user;
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		
		if(!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
		
		return user;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return FLAVOR.equals(flavor);
	}

}
