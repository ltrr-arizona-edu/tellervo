/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.labelgen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.tellervo.desktop.util.Base64;
import org.tellervo.schema.WSIBox;
import org.tridas.schema.TridasSample;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;


public class LabBarcode extends Barcode128 {

	/**
	 * An enum representing the type of this barcode
	 * 
	 * @author Lucas Madar
	 */
	public static enum Type {
		SAMPLE('S'),
		BOX('B'),
		SERIES('Z');
		
		/** The character we prepend to the UUID */
		private char charVal;
		
		Type(char c) {
			charVal = c;
		}
		
		public char getPrefix() {
			return charVal;
		}
		
		public static Type valueOf(char c) {
			for(Type type : values()) {
				if(type.charVal == c)
					return type;
			}
			
			throw new IllegalArgumentException("Invalid type");
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
				
		setCode(encode(uuidType, uuid));
		setAltText(uuidType + " " + uuid.toString());
		setCodeType(Barcode.CODE128);
		setFont(barcodeFont.getBaseFont());
	}

	/**
	 * Encode a UUID and Type into base64
	 * @param uuidType
	 * @param uuid
	 * @returns a string representing the base64 uuid
	 */
	public static String encode(Type uuidType, UUID uuid) {
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
		
		return b64enc;
	}
	
	/**
	 * Silly class for a return value when decoding lab barcodes
	 * @author Lucas Madar
	 */
	public static class DecodedBarcode {
		public Type uuidType;
		public UUID uuid;
	}
	
	/**
	 * Decodes an encoded barcode
	 * 
	 * @param b64enc
	 * @return A decoded barcode structure
	 */
	public static DecodedBarcode decode(String b64enc) {
		byte[] bytes;
		
		try {
			bytes = Base64.decode(b64enc);
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid barcode value (not base64)");
		}
		
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			char type = (char) dis.readByte();
			char colon = (char) dis.readByte();
			long msb = dis.readLong();
			long lsb = dis.readLong();
			
			if(colon != ':')
				throw new IllegalArgumentException("Barcode is not a Tellervo barcode (invalid format)");
			
			DecodedBarcode barcode = new DecodedBarcode();
			
			// load uuid
			barcode.uuid = new UUID(msb, lsb);
			try {
				barcode.uuidType = Type.valueOf(type);
			} catch (IllegalArgumentException iae) {
				throw new IllegalArgumentException("Invalid barcode type: " + type);
			}
			
			return barcode;
		} catch (IOException e) {
			throw new IllegalArgumentException("Barcode is not a Tellervo barcode");
		}
	}
	
	/**
	 * Create a bar code for this box
	 * 
	 * @return Image 
	 */
	public static Image getBoxBarcode(WSIBox b, PdfContentByte cb)
	{
	
		return getBoxBarcode(b, cb, 50f);
	
	}
	
	public static Image getBoxBarcode(WSIBox b, PdfContentByte cb, float barheight)
	{
		return getBoxBarcode(b, cb, barheight, 0.7f);
	}
	
	public static Image getBoxBarcode(WSIBox b, PdfContentByte cb, float barheight, float size)
	{
		UUID uuid = UUID.fromString(b.getIdentifier().getValue());
		LabBarcode barcode = new LabBarcode(LabBarcode.Type.BOX, uuid);

		
		barcode.setFont(null);
		
		// Original works for 4" labels
		//barcode.setX(0.7f);
		barcode.setX(size);
		barcode.setSize(6f);
		barcode.setBaseline(8f);
		barcode.setBarHeight(barheight);
		
		Image image = barcode.createImageWithBarcode(cb, null, null);
	
		return image;
	
	}
	
	public static Image getSampleBarcode(TridasSample b, PdfContentByte cb)
	{
		return getSampleBarcode(b, cb, 10f, 0.7f);
	}
	
	public static Image getSampleBarcode(TridasSample b, PdfContentByte cb, float barcodesize)
	{
		return getSampleBarcode(b, cb, 10f, barcodesize);
	}
		
	public static Image getSampleBarcode(TridasSample b, PdfContentByte cb, float barheight, float size)
	{
		UUID uuid = UUID.fromString(b.getIdentifier().getValue());
		LabBarcode barcode = new LabBarcode(LabBarcode.Type.SAMPLE, uuid);

		
		barcode.setFont(null);
		
		// ORiginal
		//barcode.setX(0.7f);
		barcode.setX(0.65f);
		barcode.setSize(size);
		barcode.setBaseline(8f);
		barcode.setBarHeight(barheight);
		
		Image image = barcode.createImageWithBarcode(cb, null, null);
	
		return image;
	
	}
}
