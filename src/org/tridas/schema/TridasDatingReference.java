
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element name="linkSeries" type="{http://www.tridas.org/1.3}seriesLink"/>
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
    "linkSeries"
})
@XmlRootElement(name = "datingReference")
@TridasEditProperties(finalType = true)
public class TridasDatingReference
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected SeriesLink linkSeries;

    /**
     * Gets the value of the linkSeries property.
     * 
     * @return
     *     possible object is
     *     {@link SeriesLink }
     *     
     */
    public SeriesLink getLinkSeries() {
        return linkSeries;
    }

    /**
     * Sets the value of the linkSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeriesLink }
     *     
     */
    public void setLinkSeries(SeriesLink value) {
        this.linkSeries = value;
    }

    public boolean isSetLinkSeries() {
        return (this.linkSeries!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasDatingReference)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasDatingReference that = ((TridasDatingReference) object);
        equalsBuilder.append(this.getLinkSeries(), that.getLinkSeries());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasDatingReference)) {
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
        hashCodeBuilder.append(this.getLinkSeries());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            SeriesLink theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            toStringBuilder.append("linkSeries", theLinkSeries);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasDatingReference copy = ((target == null)?((TridasDatingReference) createCopy()):((TridasDatingReference) target));
        {
            SeriesLink sourceLinkSeries;
            sourceLinkSeries = this.getLinkSeries();
            SeriesLink copyLinkSeries = ((SeriesLink) copyBuilder.copy(sourceLinkSeries));
            copy.setLinkSeries(copyLinkSeries);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasDatingReference();
    }

}
