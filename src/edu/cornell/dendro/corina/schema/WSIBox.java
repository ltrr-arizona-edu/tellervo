
package edu.cornell.dendro.corina.schema;

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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Copyable;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;
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
 *         &lt;element ref="{http://www.tridas.org/1.3}title"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}identifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}createdTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}lastModifiedTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}comments" minOccurs="0"/>
 *         &lt;element name="trackingLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="curationLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sampleCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}sample" maxOccurs="unbounded" minOccurs="0"/>
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
public class WSIBox implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString, ITridas
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(namespace = "http://www.tridas.org/1.3", required = true)
    protected String title;
    @XmlElement(namespace = "http://www.tridas.org/1.3")
    protected TridasIdentifier identifier;
    @XmlElement(namespace = "http://www.tridas.org/1.3")
    protected DateTime createdTimestamp;
    @XmlElement(namespace = "http://www.tridas.org/1.3")
    protected DateTime lastModifiedTimestamp;
    @XmlElement(namespace = "http://www.tridas.org/1.3")
    protected String comments;
    protected String trackingLocation;
    protected String curationLocation;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer sampleCount;
    @XmlElement(name = "sample", namespace = "http://www.tridas.org/1.3")
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSIBox)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSIBox that = ((WSIBox) object);
        equalsBuilder.append(this.getTitle(), that.getTitle());
        equalsBuilder.append(this.getIdentifier(), that.getIdentifier());
        equalsBuilder.append(this.getCreatedTimestamp(), that.getCreatedTimestamp());
        equalsBuilder.append(this.getLastModifiedTimestamp(), that.getLastModifiedTimestamp());
        equalsBuilder.append(this.getComments(), that.getComments());
        equalsBuilder.append(this.getTrackingLocation(), that.getTrackingLocation());
        equalsBuilder.append(this.getCurationLocation(), that.getCurationLocation());
        equalsBuilder.append(this.getSampleCount(), that.getSampleCount());
        equalsBuilder.append(this.getSamples(), that.getSamples());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSIBox)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getTitle());
        hashCodeBuilder.append(this.getIdentifier());
        hashCodeBuilder.append(this.getCreatedTimestamp());
        hashCodeBuilder.append(this.getLastModifiedTimestamp());
        hashCodeBuilder.append(this.getComments());
        hashCodeBuilder.append(this.getTrackingLocation());
        hashCodeBuilder.append(this.getCurationLocation());
        hashCodeBuilder.append(this.getSampleCount());
        hashCodeBuilder.append(this.getSamples());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theTitle;
            theTitle = this.getTitle();
            toStringBuilder.append("title", theTitle);
        }
        {
            TridasIdentifier theIdentifier;
            theIdentifier = this.getIdentifier();
            toStringBuilder.append("identifier", theIdentifier);
        }
        {
            DateTime theCreatedTimestamp;
            theCreatedTimestamp = this.getCreatedTimestamp();
            toStringBuilder.append("createdTimestamp", theCreatedTimestamp);
        }
        {
            DateTime theLastModifiedTimestamp;
            theLastModifiedTimestamp = this.getLastModifiedTimestamp();
            toStringBuilder.append("lastModifiedTimestamp", theLastModifiedTimestamp);
        }
        {
            String theComments;
            theComments = this.getComments();
            toStringBuilder.append("comments", theComments);
        }
        {
            String theTrackingLocation;
            theTrackingLocation = this.getTrackingLocation();
            toStringBuilder.append("trackingLocation", theTrackingLocation);
        }
        {
            String theCurationLocation;
            theCurationLocation = this.getCurationLocation();
            toStringBuilder.append("curationLocation", theCurationLocation);
        }
        {
            Integer theSampleCount;
            theSampleCount = this.getSampleCount();
            toStringBuilder.append("sampleCount", theSampleCount);
        }
        {
            List<TridasSample> theSamples;
            theSamples = this.getSamples();
            toStringBuilder.append("samples", theSamples);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSIBox copy = ((target == null)?((WSIBox) createCopy()):((WSIBox) target));
        if (this.isSetTitle()) {
            String sourceTitle;
            sourceTitle = this.getTitle();
            String copyTitle = ((String) copyBuilder.copy(sourceTitle));
            copy.setTitle(copyTitle);
        } else {
            copy.title = null;
        }
        if (this.isSetIdentifier()) {
            TridasIdentifier sourceIdentifier;
            sourceIdentifier = this.getIdentifier();
            TridasIdentifier copyIdentifier = ((TridasIdentifier) copyBuilder.copy(sourceIdentifier));
            copy.setIdentifier(copyIdentifier);
        } else {
            copy.identifier = null;
        }
        if (this.isSetCreatedTimestamp()) {
            DateTime sourceCreatedTimestamp;
            sourceCreatedTimestamp = this.getCreatedTimestamp();
            DateTime copyCreatedTimestamp = ((DateTime) copyBuilder.copy(sourceCreatedTimestamp));
            copy.setCreatedTimestamp(copyCreatedTimestamp);
        } else {
            copy.createdTimestamp = null;
        }
        if (this.isSetLastModifiedTimestamp()) {
            DateTime sourceLastModifiedTimestamp;
            sourceLastModifiedTimestamp = this.getLastModifiedTimestamp();
            DateTime copyLastModifiedTimestamp = ((DateTime) copyBuilder.copy(sourceLastModifiedTimestamp));
            copy.setLastModifiedTimestamp(copyLastModifiedTimestamp);
        } else {
            copy.lastModifiedTimestamp = null;
        }
        if (this.isSetComments()) {
            String sourceComments;
            sourceComments = this.getComments();
            String copyComments = ((String) copyBuilder.copy(sourceComments));
            copy.setComments(copyComments);
        } else {
            copy.comments = null;
        }
        if (this.isSetTrackingLocation()) {
            String sourceTrackingLocation;
            sourceTrackingLocation = this.getTrackingLocation();
            String copyTrackingLocation = ((String) copyBuilder.copy(sourceTrackingLocation));
            copy.setTrackingLocation(copyTrackingLocation);
        } else {
            copy.trackingLocation = null;
        }
        if (this.isSetCurationLocation()) {
            String sourceCurationLocation;
            sourceCurationLocation = this.getCurationLocation();
            String copyCurationLocation = ((String) copyBuilder.copy(sourceCurationLocation));
            copy.setCurationLocation(copyCurationLocation);
        } else {
            copy.curationLocation = null;
        }
        if (this.isSetSampleCount()) {
            Integer sourceSampleCount;
            sourceSampleCount = this.getSampleCount();
            Integer copySampleCount = ((Integer) copyBuilder.copy(sourceSampleCount));
            copy.setSampleCount(copySampleCount);
        } else {
            copy.sampleCount = null;
        }
        if (this.isSetSamples()) {
            List<TridasSample> sourceSamples;
            sourceSamples = this.getSamples();
            @SuppressWarnings("unchecked")
            List<TridasSample> copySamples = ((List<TridasSample> ) copyBuilder.copy(sourceSamples));
            copy.setSamples(copySamples);
        } else {
            copy.unsetSamples();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSIBox();
    }

}
