
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
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
import org.tridas.util.TridasObjectEx;


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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}title"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}identifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}createdTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}lastModifiedTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}comments" minOccurs="0"/>
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
    TridasRadius.class,
    TridasSample.class,
    TridasElement.class,
    TridasProject.class
})
public abstract class TridasEntity implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected String title;
    protected TridasIdentifier identifier;
    protected DateTime createdTimestamp;
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
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasEntity)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasEntity that = ((TridasEntity) object);
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
        if (null == target) {
            throw new IllegalArgumentException("Target argument must not be null for abstract copyable classes.");
        }
        if (target instanceof TridasEntity) {
            final TridasEntity copy = ((TridasEntity) target);
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
        }
        return target;
    }

}
