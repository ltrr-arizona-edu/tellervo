package org.tellervo.desktop.labelgen;

import com.itextpdf.text.Rectangle;

public class BoxBarcodeRoll3x1Point75Style extends BoxBarcodeRollStyle {


	
	
	public BoxBarcodeRoll3x1Point75Style() {
		super(new Rectangle(216, 108), "Box barcodes 3 x 1.75\"", "Simple box barcode labels for printing on a roll - 3 x 1.75\"", ItemType.BOX);
		this.setBarcodeSize(0.65f);
	}

	

	
	

}
