
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
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
import org.tridas.annotations.TridasEditProperties;
import org.tridas.interfaces.ITridas;


/**
 * 
 * 				Base data type inherited by all TRiDaS entities.  Contains the fields common to all TRiDaS data entities.
 * 			
 * 
 * <p>Java class for tridasEntity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tridasEntity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2}title"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}identifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}createdTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}lastModifiedTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}comments" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tridasEntity", propOrder = {
    "title",
    "identifier",
    "createdTimestamp",
    "lastModifiedTimestamp",
    "comments"
})
@XmlSeeAlso({
    TridasObjectEx.class,
    BaseSeries.class,
    TridasProject.class,
    TridasRadius.class,
    TridasSample.class,
    TridasElement.class
})
public abstract class TridasEntity implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString, ITridas
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected String title;
    protected TridasIdentifier identifier;
    @TridasEditProperties(readOnly = true)
    protected DateTime createdTimestamp;
    @TridasEditProperties(readOnly = true)
    protected DateTime lastModifiedTimestamp;
    protected String comments;

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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasEntity)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasEntity that = ((TridasEntity) object);
        equalsBuilder.append(this.getTitle(), that.getTitle());
        equalsBuilder.append(this.getIdentifier(), that.getIdentifier());
        equalsBuilder.append(this.getCreatedTimestamp(), that.getCreatedTimestamp());
        equalsBuilder.append(this.getLastModifiedTimestamp(), that.getLastModifiedTimestamp());
        equalsBuilder.append(this.getComments(), that.getComments());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasEntity)) {
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
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        if (null == target) {
            throw new IllegalArgumentException("Target argument must not be null for abstract copyable classes.");
        }
        final TridasEntity copy = ((TridasEntity) target);
        {
            String sourceTitle;
            sourceTitle = this.getTitle();
            String copyTitle = ((String) copyBuilder.copy(sourceTitle));
            copy.setTitle(copyTitle);
        }
        {
            TridasIdentifier sourceIdentifier;
            sourceIdentifier = this.getIdentifier();
            TridasIdentifier copyIdentifier = ((TridasIdentifier) copyBuilder.copy(sourceIdentifier));
            copy.setIdentifier(copyIdentifier);
        }
        {
            DateTime sourceCreatedTimestamp;
            sourceCreatedTimestamp = this.getCreatedTimestamp();
            DateTime copyCreatedTimestamp = ((DateTime) copyBuilder.copy(sourceCreatedTimestamp));
            copy.setCreatedTimestamp(copyCreatedTimestamp);
        }
        {
            DateTime sourceLastModifiedTimestamp;
            sourceLastModifiedTimestamp = this.getLastModifiedTimestamp();
            DateTime copyLastModifiedTimestamp = ((DateTime) copyBuilder.copy(sourceLastModifiedTimestamp));
            copy.setLastModifiedTimestamp(copyLastModifiedTimestamp);
        }
        {
            String sourceComments;
            sourceComments = this.getComments();
            String copyComments = ((String) copyBuilder.copy(sourceComments));
            copy.setComments(copyComments);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

}
