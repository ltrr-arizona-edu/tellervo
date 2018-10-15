package org.tellervo.desktop.labelgen;

import java.util.ArrayList;

import org.tellervo.desktop.labelgen.AbstractTellervoLabelStyle.ItemType;

/**
 * Factory for accessing the various labels styles defined for use in Tellervo
 * 
 * @author pbrewer
 *
 */
public class LabelStyleFactory {
	
	// Array of label style classes
	private final static AbstractTellervoLabelStyle[] availableStyles = new AbstractTellervoLabelStyle[]{
		new BoxBarcode12UpLabelStyle(), 
		new BoxBarcodeRollStyle(),
		new BoxBarcodeRoll3Point5x1Style(),
		new BoxBarcodeRoll3Point5xPoint75Style(),
		new BoxBarcodeRoll4xPoint375Style(),
		new BoxLabel1Style(),
		new BoxLabel2Style(),
		new SampleLabel1Style(),
		new SampleLabelRoll4xPoint375AllInStyle(),
		new SampleLabelRoll4xPoint375TwoLabelStyle(),
		
		};

	
	public static AbstractTellervoLabelStyle[] getAvailableStyles()
	{
		return availableStyles;
	}
	
	public static AbstractTellervoLabelStyle[] getAvailableBoxStyles()
	{
		ArrayList<AbstractTellervoLabelStyle> styles = new ArrayList<AbstractTellervoLabelStyle>();
		
		for(int i=0; i<availableStyles.length; i++)
		{
			AbstractTellervoLabelStyle style = availableStyles[i];
			if(style.getItemType().equals(ItemType.BOX))
			{
				styles.add(style);
			}
		}
		
		return styles.toArray(new AbstractTellervoLabelStyle[styles.size()]);
	}
	
	public static  AbstractTellervoLabelStyle[] getAvailableSampleStyles()
	{
		ArrayList<AbstractTellervoLabelStyle> styles = new ArrayList<AbstractTellervoLabelStyle>();
		
		for(int i=0; i<availableStyles.length; i++)
		{
			AbstractTellervoLabelStyle style = availableStyles[i];
			if(style.getItemType().equals(ItemType.SAMPLE))
			{
				styles.add(style);
			}
		}
		
		return styles.toArray(new AbstractTellervoLabelStyle[styles.size()]);
	}
}
