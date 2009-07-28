package edu.cornell.dendro.corina.util.labels;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;

import edu.cornell.dendro.corina.util.Base64;

public class LabBarcode extends Barcode128 {

	/**
	 * An enum representing the type of this barcode
	 * 
	 * @author Lucas Madar
	 */
	public static enum Type {
		SAMPLE('S'),
		BOX('B');
		
		/** The character we prepend to the UUID */
		private char charVal;
		
		Type(char c) {
			charVal = c;
		}
		
		public char getPrefix() {
			return charVal;
		}
	}
	
	/** The font we use to label the barcode */
	private static Font barcodeFont = FontFactory.getFont(FontFactory.COURIER);

	/**
	 * Construct a new Lab Barcode representing a given UUID and UUID type
	 * 
	 * @param uuidType One of LabBarcode.Type
	 * @param uuid The UUID to encode
	 */
	public LabBarcode(Type uuidType, UUID uuid) {
		super();
		
		// convert the uuid into a raw byte array
		ByteArrayOutputStream bbis = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bbis);
		
		String b64enc;
		try {
			dos.writeByte((byte) uuidType.getPrefix());
			dos.writeByte((byte) ':');
			
			// start with high bits... is this proper?
			dos.writeLong(uuid.getMostSignificantBits());
			dos.writeLong(uuid.getLeastSignificantBits());
			
			b64enc = Base64.encodeBytes(bbis.toByteArray());
		} catch (IOException e) {
			// this should never happen, since it's a ByteArrayOutputStream
			throw new RuntimeException(e);
		}
		
		setCode(b64enc);
		setAltText(uuidType + " " + uuid.toString());
		setCodeType(Barcode.CODE128);
		setFont(barcodeFont.getBaseFont());
	}
}
