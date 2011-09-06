package edu.cornell.dendro.corina.admin.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import edu.cornell.dendro.corina.schema.WSISecurityGroup;

@SuppressWarnings("serial")
public class TransferableGroup implements Transferable {

	public final static DataFlavor FLAVOR = new DataFlavor(WSISecurityGroup.class, "Group");
	private static DataFlavor[] flavors = {FLAVOR};
	private WSISecurityGroup group;
	
	public TransferableGroup(WSISecurityGroup argGroup){
		group = argGroup;
	}
	
	public WSISecurityGroup getGroup(){
		return group;
	}
		
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		
		if(!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
		
		return group;
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
