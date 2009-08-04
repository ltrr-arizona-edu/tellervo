package edu.cornell.dendro.corina.util.openrecent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;

/**
 * A kludgy class for the OpenRecent menu
 * Store more information, and then serialize/deserialize
 * 
 * @author Lucas Madar
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "identifier",
    "fileName",
    "sampleType",
    "loaderType",
    "displayName",
    "version",
    "range"
})
@XmlRootElement(namespace = "corina:internal", name = "seriesDescriptor")
public class SeriesDescriptor implements OpenableDocumentDescriptor {
    protected TridasIdentifier identifier;
    protected String fileName;
    @XmlElement(required = true)
    protected String displayName;
    protected String version;
    protected SampleType sampleType;
    @XmlElement(required = true)
    protected LoaderType loaderType;
    protected Range range;
    
    /**
     * Default constructor
     */
    public SeriesDescriptor() {
    }

    /**
     * Construct from a sample
     * @param s
     */
    public SeriesDescriptor(Sample s) {
    	ITridasSeries series = s.getSeries();

    	setDisplayName(s.getDisplayTitle());
    	setIdentifier(series.getIdentifier());
    	setSampleType(s.getSampleType());
    	setRange(s.getRange());
    	
    	if(series instanceof ITridasDerivedSeries)
    		setVersion(((ITridasDerivedSeries)series).getVersion());
    	
    	if(s.getLoader() instanceof CorinaWsiTridasElement)
    		setLoaderType(LoaderType.CWTE);
    	else if(s.getLoader() instanceof FileElement)
    		setLoaderType(LoaderType.FILE);
    	else
    		setLoaderType(LoaderType.UNKNOWN);
    }
    
	/**
	 * @return the identifier
	 */
	public TridasIdentifier getIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(TridasIdentifier identifier) {
		this.identifier = identifier;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the sampleType
	 */
	public SampleType getSampleType() {
		return sampleType;
	}
	/**
	 * @param type the sampleType to set
	 */
	public void setSampleType(SampleType type) {
		this.sampleType = type;
	}
	/**
	 * @return the loaderType
	 */
	public LoaderType getLoaderType() {
		return loaderType;
	}
	/**
	 * @param type the loaderType to set
	 */
	public void setLoaderType(LoaderType type) {
		this.loaderType = type;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the range
	 */
	public Range getRange() {
		return range;
	}
	/**
	 * @param range the range to set
	 */
	public void setRange(Range range) {
		this.range = range;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof SeriesDescriptor) {
			SeriesDescriptor them = ((SeriesDescriptor)o);

			// both null!
			if(identifier == null && them.identifier == null) {
				// no display name? :( not equal!
				if(displayName == null)
					return false;
				
				return displayName.equalsIgnoreCase(them.displayName);
			}
			
			// one has id, one doesn't!
			if(them.identifier == null || identifier == null)
				return false;
			
			return identifier.equals(them.identifier);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (identifier == null) 
				? ((displayName == null) ? 0 : displayName.hashCode()) 
				: identifier.hashCode();
	}
	
	@XmlEnum
	/**
	 * An enum to represent our loader types
	 */
	public enum LoaderType {
		@XmlEnumValue("Corina/TRiDaS WSI") CWTE("Corina/TRiDaS WSI"),
		@XmlEnumValue("File") FILE("File"),
		@XmlEnumValue("Unknown") UNKNOWN("Unknown");
	    private final String value;

	    LoaderType(String v) {
	        value = v;
	    }

	    public String value() {
	        return value;
	    }
	    
	    public static LoaderType fromValue(String v) {
	        for (LoaderType c: LoaderType.values()) {
	            if (c.value.equals(v)) {
	                return c;
	            }
	        }
	        throw new IllegalArgumentException(v);
	    }
	}
}
