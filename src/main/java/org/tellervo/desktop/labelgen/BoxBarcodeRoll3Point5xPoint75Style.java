package org.tellervo.desktop.labelgen;

import com.itextpdf.text.Rectangle;

public class BoxBarcodeRoll3Point5xPoint75Style extends BoxBarcodeRollStyle {


	
	
	public BoxBarcodeRoll3Point5xPoint75Style() {
		super(new Rectangle(252, 54), "Box barcodes 3.5 x 0.75\"", "Simple box barcode labels for printing on a roll - 3.5 x 0.75\"", ItemType.BOX);
		this.setBarcodeSize(0.65f);
	}

	

	
	

}
