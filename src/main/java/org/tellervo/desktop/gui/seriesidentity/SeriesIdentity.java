package org.tellervo.desktop.gui.seriesidentity;

import java.io.File;

import org.tellervo.desktop.io.IdentityItem;
import org.tellervo.desktop.sample.Sample;
import org.tridas.io.AbstractDendroFormat;

/**
 * This class contains the identity of a series being imported from a legacy text file 
 * 
 * @author pbrewer
 *
 */
public class SeriesIdentity {

	private IdentityItem objectItem = new IdentityItem();
	private IdentityItem subObjectItem = new IdentityItem();
	private IdentityItem elementItem = new IdentityItem();
	private IdentityItem sampleItem = new IdentityItem();
	private IdentityItem radiusItem = new IdentityItem();
	private IdentityItem seriesItem = new IdentityItem();
	private File file;
	private AbstractDendroFormat format;
	private Sample sample;
	
	public SeriesIdentity(File file, AbstractDendroFormat format, Sample s)
	{
		this.setFile(file);
		this.setFormat(format);
		this.setSample(s);
		
	}
	
	


	public AbstractDendroFormat getFormat() {
		return format;
	}



	public void setFormat(AbstractDendroFormat format) {
		this.format = format;
	}



	public File getFile() {
		return file;
	}



	public void setFile(File file) {
		this.file = file;
	}



	public Sample getSample() {
		return sample;
	}



	public void setSample(Sample sample) {
		this.sample = sample;
	}
		
	
	public IdentityItem getObjectItem() {
		return objectItem;
	}

	public IdentityItem getSubObjectItem() {
		return subObjectItem;
	}

	public void setSubobjectItem(IdentityItem objectItem) {
		this.subObjectItem = objectItem;
	}
	

	public void setObjectItem(IdentityItem objectItem) {
		this.objectItem = objectItem;
	}




	public IdentityItem getElementItem() {
		return elementItem;
	}




	public void setElementItem(IdentityItem elementItem) {
		this.elementItem = elementItem;
	}




	public IdentityItem getSampleItem() {
		return sampleItem;
	}




	public void setSampleItem(IdentityItem sampleItem) {
		this.sampleItem = sampleItem;
	}




	public IdentityItem getRadiusItem() {
		return radiusItem;
	}




	public void setRadiusItem(IdentityItem radiusItem) {
		this.radiusItem = radiusItem;
	}




	public IdentityItem getSeriesItem() {
		return seriesItem;
	}




	public void setSeriesItem(IdentityItem seriesItem) {
		this.seriesItem = seriesItem;
	}
	
	
	
}
