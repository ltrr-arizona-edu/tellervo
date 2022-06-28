
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="linkSeries" type="{http://www.tridas.org/1.2.2}seriesLink"/>
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
public class TridasDatingReference
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

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
            SeriesLink theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            strategy.appendField(locator, this, "linkSeries", buffer, theLinkSeries);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasDatingReference)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasDatingReference that = ((TridasDatingReference) object);
        {
            SeriesLink lhsLinkSeries;
            lhsLinkSeries = this.getLinkSeries();
            SeriesLink rhsLinkSeries;
            rhsLinkSeries = that.getLinkSeries();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "linkSeries", lhsLinkSeries), LocatorUtils.property(thatLocator, "linkSeries", rhsLinkSeries), lhsLinkSeries, rhsLinkSeries)) {
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
            SeriesLink theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "linkSeries", theLinkSeries), currentHashCode, theLinkSeries);
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
        if (draftCopy instanceof TridasDatingReference) {
            final TridasDatingReference copy = ((TridasDatingReference) draftCopy);
            if (this.isSetLinkSeries()) {
                SeriesLink sourceLinkSeries;
                sourceLinkSeries = this.getLinkSeries();
                SeriesLink copyLinkSeries = ((SeriesLink) strategy.copy(LocatorUtils.property(locator, "linkSeries", sourceLinkSeries), sourceLinkSeries));
                copy.setLinkSeries(copyLinkSeries);
            } else {
                copy.linkSeries = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasDatingReference();
    }

}
