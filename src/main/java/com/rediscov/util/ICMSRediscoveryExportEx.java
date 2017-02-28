package com.rediscov.util;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.util.ITRDBTaxonConverter;
import org.tridas.schema.ControlledVoc;

import com.rediscov.schema.ICMSRediscoveryExport;

@XmlRootElement(name = "RediscoveryExport")
public class ICMSRediscoveryExportEx extends ICMSRediscoveryExport {

	private static final long serialVersionUID = 1L;
	protected final static Logger log = LoggerFactory.getLogger(ICMSRediscoveryExportEx.class);

	/**
	 * Set all ICMS fields that are constant for dendrochronology
	 */
	public void setConstantFields()
	{
		
		this.setClass1("ARCHAEOLOGY");
		this.setClass2("UNKNOWN");
		this.setClass3("VEGETAL");
		this.setClass3("WOOD");
		this.setObjectNom("DENDRO SAMPLE");
		this.setObjectStatus("STORAGE - INCOMING LOAN");
		this.setStorageUnit("EA");
		
	}
	
	/**
	 * Extract the county from the Origin field
	 * 
	 * @return
	 */
	public String getCounty()
	{
		String origin = this.getOrigin();
		String[] parts = origin.split("__");
		if(parts.length==3)
		{
			return parts[0].trim();
		}
		
		log.error("Unable to extract County information from origin field");

		return null;
		
	}
	
	/**
	 * Extract the State from the Origin field
	 * 
	 * @return
	 */
	public String getState()
	{
		String origin = this.getOrigin();
		String[] parts = origin.split("__");
		if(parts.length==3)
		{
			return parts[1].trim();
		}
		
		log.error("Unable to extract State information from origin field");
		
		return null;
		
	}
	
	/**
	 * Extract the Country from the Origin field
	 * 
	 * @return
	 */
	public String getCountry()
	{
		String origin = this.getOrigin();
		String[] parts = origin.split("__");
		if(parts.length==3)
		{
			return parts[2].trim();
		}
		
		log.error("Unable to extract Country information from origin field");

		return null;
		
	}
	
	public ControlledVoc getTaxon()
	{
		String taxon = this.getITRDBSpeciesCode();
		
		if(taxon!=null)
		{
			return ITRDBTaxonConverter.getControlledVocFromCode(taxon);
		}
		
		log.error("Unable to convert taxon field to true taxon");

		return null;
	}
	

}
