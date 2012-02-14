
package org.tellervo.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
import org.tridas.adapters.IntegerAdapter;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.DateTime;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasSample;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}title"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}identifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}createdTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}lastModifiedTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}comments" minOccurs="0"/>
 *         &lt;element name="trackingLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="curationLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sampleCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}sample" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "identifier",
    "createdTimestamp",
    "lastModifiedTimestamp",
    "comments",
    "trackingLocation",
    "curationLocation",
    "sampleCount",
    "samples"
})
@XmlRootElement(name = "box")
public class WSIBox implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString, ITridas
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(namespace = "http://www.tridas.org/1.2.2", required = true)
    protected String title;
    @XmlElement(namespace = "http://www.tridas.org/1.2.2")
    protected TridasIdentifier identifier;
    @XmlElement(namespace = "http://www.tridas.org/1.2.2")
    protected DateTime createdTimestamp;
    @XmlElement(namespace = "http://www.tridas.org/1.2.2")
    protected DateTime lastModifiedTimestamp;
    @XmlElement(namespace = "http://www.tridas.org/1.2.2")
    protected String comments;
    protected String trackingLocation;
    protected String curationLocation;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer sampleCount;
    @XmlElement(name = "sample", namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasSample> samples;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    public boolean isSetTitle() {
        return (this.title!= null);
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link TridasIdentifier }
     *     
     */
    public TridasIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasIdentifier }
     *     
     */
    public void setIdentifier(TridasIdentifier value) {
        this.identifier = value;
    }

    public boolean isSetIdentifier() {
        return (this.identifier!= null);
    }

    /**
     * Gets the value of the createdTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * Sets the value of the createdTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setCreatedTimestamp(DateTime value) {
        this.createdTimestamp = value;
    }

    public boolean isSetCreatedTimestamp() {
        return (this.createdTimestamp!= null);
    }

    /**
     * Gets the value of the lastModifiedTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    /**
     * Sets the value of the lastModifiedTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setLastModifiedTimestamp(DateTime value) {
        this.lastModifiedTimestamp = value;
    }

    public boolean isSetLastModifiedTimestamp() {
        return (this.lastModifiedTimestamp!= null);
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

    public boolean isSetComments() {
        return (this.comments!= null);
    }

    /**
     * Gets the value of the trackingLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrackingLocation() {
        return trackingLocation;
    }

    /**
     * Sets the value of the trackingLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrackingLocation(String value) {
        this.trackingLocation = value;
    }

    public boolean isSetTrackingLocation() {
        return (this.trackingLocation!= null);
    }

    /**
     * Gets the value of the curationLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurationLocation() {
        return curationLocation;
    }

    /**
     * Sets the value of the curationLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurationLocation(String value) {
        this.curationLocation = value;
    }

    public boolean isSetCurationLocation() {
        return (this.curationLocation!= null);
    }

    /**
     * Gets the value of the sampleCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getSampleCount() {
        return sampleCount;
    }

    /**
     * Sets the value of the sampleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSampleCount(Integer value) {
        this.sampleCount = value;
    }

    public boolean isSetSampleCount() {
        return (this.sampleCount!= null);
    }

    /**
     * Gets the value of the samples property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the samples property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSamples().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasSample }
     * 
     * 
     */
    public List<TridasSample> getSamples() {
        if (samples == null) {
            samples = new ArrayList<TridasSample>();
        }
        return this.samples;
    }

    public boolean isSetSamples() {
        return ((this.samples!= null)&&(!this.samples.isEmpty()));
    }

    public void unsetSamples() {
        this.samples = null;
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            String theTitle;
            theTitle = this.getTitle();
            strategy.appendField(locator, this, "title", buffer, theTitle);
        }
        {
            TridasIdentifier theIdentifier;
            theIdentifier = this.getIdentifier();
            strategy.appendField(locator, this, "identifier", buffer, theIdentifier);
        }
        {
            DateTime theCreatedTimestamp;
            theCreatedTimestamp = this.getCreatedTimestamp();
            strategy.appendField(locator, this, "createdTimestamp", buffer, theCreatedTimestamp);
        }
        {
            DateTime theLastModifiedTimestamp;
            theLastModifiedTimestamp = this.getLastModifiedTimestamp();
            strategy.appendField(locator, this, "lastModifiedTimestamp", buffer, theLastModifiedTimestamp);
        }
        {
            String theComments;
            theComments = this.getComments();
            strategy.appendField(locator, this, "comments", buffer, theComments);
        }
        {
            String theTrackingLocation;
            theTrackingLocation = this.getTrackingLocation();
            strategy.appendField(locator, this, "trackingLocation", buffer, theTrackingLocation);
        }
        {
            String theCurationLocation;
            theCurationLocation = this.getCurationLocation();
            strategy.appendField(locator, this, "curationLocation", buffer, theCurationLocation);
        }
        {
            Integer theSampleCount;
            theSampleCount = this.getSampleCount();
            strategy.appendField(locator, this, "sampleCount", buffer, theSampleCount);
        }
        {
            List<TridasSample> theSamples;
            theSamples = (this.isSetSamples()?this.getSamples():null);
            strategy.appendField(locator, this, "samples", buffer, theSamples);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIBox)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIBox that = ((WSIBox) object);
        {
            String lhsTitle;
            lhsTitle = this.getTitle();
            String rhsTitle;
            rhsTitle = that.getTitle();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "title", lhsTitle), LocatorUtils.property(thatLocator, "title", rhsTitle), lhsTitle, rhsTitle)) {
                return false;
            }
        }
        {
            TridasIdentifier lhsIdentifier;
            lhsIdentifier = this.getIdentifier();
            TridasIdentifier rhsIdentifier;
            rhsIdentifier = that.getIdentifier();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "identifier", lhsIdentifier), LocatorUtils.property(thatLocator, "identifier", rhsIdentifier), lhsIdentifier, rhsIdentifier)) {
                return false;
            }
        }
        {
            DateTime lhsCreatedTimestamp;
            lhsCreatedTimestamp = this.getCreatedTimestamp();
            DateTime rhsCreatedTimestamp;
            rhsCreatedTimestamp = that.getCreatedTimestamp();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "createdTimestamp", lhsCreatedTimestamp), LocatorUtils.property(thatLocator, "createdTimestamp", rhsCreatedTimestamp), lhsCreatedTimestamp, rhsCreatedTimestamp)) {
                return false;
            }
        }
        {
            DateTime lhsLastModifiedTimestamp;
            lhsLastModifiedTimestamp = this.getLastModifiedTimestamp();
            DateTime rhsLastModifiedTimestamp;
            rhsLastModifiedTimestamp = that.getLastModifiedTimestamp();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "lastModifiedTimestamp", lhsLastModifiedTimestamp), LocatorUtils.property(thatLocator, "lastModifiedTimestamp", rhsLastModifiedTimestamp), lhsLastModifiedTimestamp, rhsLastModifiedTimestamp)) {
                return false;
            }
        }
        {
            String lhsComments;
            lhsComments = this.getComments();
            String rhsComments;
            rhsComments = that.getComments();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "comments", lhsComments), LocatorUtils.property(thatLocator, "comments", rhsComments), lhsComments, rhsComments)) {
                return false;
            }
        }
        {
            String lhsTrackingLocation;
            lhsTrackingLocation = this.getTrackingLocation();
            String rhsTrackingLocation;
            rhsTrackingLocation = that.getTrackingLocation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "trackingLocation", lhsTrackingLocation), LocatorUtils.property(thatLocator, "trackingLocation", rhsTrackingLocation), lhsTrackingLocation, rhsTrackingLocation)) {
                return false;
            }
        }
        {
            String lhsCurationLocation;
            lhsCurationLocation = this.getCurationLocation();
            String rhsCurationLocation;
            rhsCurationLocation = that.getCurationLocation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "curationLocation", lhsCurationLocation), LocatorUtils.property(thatLocator, "curationLocation", rhsCurationLocation), lhsCurationLocation, rhsCurationLocation)) {
                return false;
            }
        }
        {
            Integer lhsSampleCount;
            lhsSampleCount = this.getSampleCount();
            Integer rhsSampleCount;
            rhsSampleCount = that.getSampleCount();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "sampleCount", lhsSampleCount), LocatorUtils.property(thatLocator, "sampleCount", rhsSampleCount), lhsSampleCount, rhsSampleCount)) {
                return false;
            }
        }
        {
            List<TridasSample> lhsSamples;
            lhsSamples = (this.isSetSamples()?this.getSamples():null);
            List<TridasSample> rhsSamples;
            rhsSamples = (that.isSetSamples()?that.getSamples():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "samples", lhsSamples), LocatorUtils.property(thatLocator, "samples", rhsSamples), lhsSamples, rhsSamples)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            String theTitle;
            theTitle = this.getTitle();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "title", theTitle), currentHashCode, theTitle);
        }
        {
            TridasIdentifier theIdentifier;
            theIdentifier = this.getIdentifier();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "identifier", theIdentifier), currentHashCode, theIdentifier);
        }
        {
            DateTime theCreatedTimestamp;
            theCreatedTimestamp = this.getCreatedTimestamp();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "createdTimestamp", theCreatedTimestamp), currentHashCode, theCreatedTimestamp);
        }
        {
            DateTime theLastModifiedTimestamp;
            theLastModifiedTimestamp = this.getLastModifiedTimestamp();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "lastModifiedTimestamp", theLastModifiedTimestamp), currentHashCode, theLastModifiedTimestamp);
        }
        {
            String theComments;
            theComments = this.getComments();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "comments", theComments), currentHashCode, theComments);
        }
        {
            String theTrackingLocation;
            theTrackingLocation = this.getTrackingLocation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "trackingLocation", theTrackingLocation), currentHashCode, theTrackingLocation);
        }
        {
            String theCurationLocation;
            theCurationLocation = this.getCurationLocation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "curationLocation", theCurationLocation), currentHashCode, theCurationLocation);
        }
        {
            Integer theSampleCount;
            theSampleCount = this.getSampleCount();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "sampleCount", theSampleCount), currentHashCode, theSampleCount);
        }
        {
            List<TridasSample> theSamples;
            theSamples = (this.isSetSamples()?this.getSamples():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "samples", theSamples), currentHashCode, theSamples);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public Object clone() {
        return copyTo(createNewInstance());
    }

    public Object copyTo(Object target) {
        final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
        return copyTo(null, target, strategy);
    }

    public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
        final Object draftCopy = ((target == null)?createNewInstance():target);
        if (draftCopy instanceof WSIBox) {
            final WSIBox copy = ((WSIBox) draftCopy);
            if (this.isSetTitle()) {
                String sourceTitle;
                sourceTitle = this.getTitle();
                String copyTitle = ((String) strategy.copy(LocatorUtils.property(locator, "title", sourceTitle), sourceTitle));
                copy.setTitle(copyTitle);
            } else {
                copy.title = null;
            }
            if (this.isSetIdentifier()) {
                TridasIdentifier sourceIdentifier;
                sourceIdentifier = this.getIdentifier();
                TridasIdentifier copyIdentifier = ((TridasIdentifier) strategy.copy(LocatorUtils.property(locator, "identifier", sourceIdentifier), sourceIdentifier));
                copy.setIdentifier(copyIdentifier);
            } else {
                copy.identifier = null;
            }
            if (this.isSetCreatedTimestamp()) {
                DateTime sourceCreatedTimestamp;
                sourceCreatedTimestamp = this.getCreatedTimestamp();
                DateTime copyCreatedTimestamp = ((DateTime) strategy.copy(LocatorUtils.property(locator, "createdTimestamp", sourceCreatedTimestamp), sourceCreatedTimestamp));
                copy.setCreatedTimestamp(copyCreatedTimestamp);
            } else {
                copy.createdTimestamp = null;
            }
            if (this.isSetLastModifiedTimestamp()) {
                DateTime sourceLastModifiedTimestamp;
                sourceLastModifiedTimestamp = this.getLastModifiedTimestamp();
                DateTime copyLastModifiedTimestamp = ((DateTime) strategy.copy(LocatorUtils.property(locator, "lastModifiedTimestamp", sourceLastModifiedTimestamp), sourceLastModifiedTimestamp));
                copy.setLastModifiedTimestamp(copyLastModifiedTimestamp);
            } else {
                copy.lastModifiedTimestamp = null;
            }
            if (this.isSetComments()) {
                String sourceComments;
                sourceComments = this.getComments();
                String copyComments = ((String) strategy.copy(LocatorUtils.property(locator, "comments", sourceComments), sourceComments));
                copy.setComments(copyComments);
            } else {
                copy.comments = null;
            }
            if (this.isSetTrackingLocation()) {
                String sourceTrackingLocation;
                sourceTrackingLocation = this.getTrackingLocation();
                String copyTrackingLocation = ((String) strategy.copy(LocatorUtils.property(locator, "trackingLocation", sourceTrackingLocation), sourceTrackingLocation));
                copy.setTrackingLocation(copyTrackingLocation);
            } else {
                copy.trackingLocation = null;
            }
            if (this.isSetCurationLocation()) {
                String sourceCurationLocation;
                sourceCurationLocation = this.getCurationLocation();
                String copyCurationLocation = ((String) strategy.copy(LocatorUtils.property(locator, "curationLocation", sourceCurationLocation), sourceCurationLocation));
                copy.setCurationLocation(copyCurationLocation);
            } else {
                copy.curationLocation = null;
            }
            if (this.isSetSampleCount()) {
                Integer sourceSampleCount;
                sourceSampleCount = this.getSampleCount();
                Integer copySampleCount = ((Integer) strategy.copy(LocatorUtils.property(locator, "sampleCount", sourceSampleCount), sourceSampleCount));
                copy.setSampleCount(copySampleCount);
            } else {
                copy.sampleCount = null;
            }
            if (this.isSetSamples()) {
                List<TridasSample> sourceSamples;
                sourceSamples = (this.isSetSamples()?this.getSamples():null);
                @SuppressWarnings("unchecked")
                List<TridasSample> copySamples = ((List<TridasSample> ) strategy.copy(LocatorUtils.property(locator, "samples", sourceSamples), sourceSamples));
                copy.unsetSamples();
                List<TridasSample> uniqueSamplesl = copy.getSamples();
                uniqueSamplesl.addAll(copySamples);
            } else {
                copy.unsetSamples();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIBox();
    }

    /**
     * Sets the value of the samples property.
     * 
     * @param samples
     *     allowed object is
     *     {@link TridasSample }
     *     
     */
    public void setSamples(List<TridasSample> samples) {
        this.samples = samples;
    }

}
