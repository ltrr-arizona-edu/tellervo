
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * <p>Java class for seriesLinksWithPreferred complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="seriesLinksWithPreferred">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="preferredSeries" type="{http://www.tridas.org/1.2.2}seriesLink" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "seriesLinksWithPreferred", propOrder = {
    "preferredSeries"
})
public class SeriesLinksWithPreferred
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected SeriesLink preferredSeries;

    /**
     * Gets the value of the preferredSeries property.
     * 
     * @return
     *     possible object is
     *     {@link SeriesLink }
     *     
     */
    public SeriesLink getPreferredSeries() {
        return preferredSeries;
    }

    /**
     * Sets the value of the preferredSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeriesLink }
     *     
     */
    public void setPreferredSeries(SeriesLink value) {
        this.preferredSeries = value;
    }

    public boolean isSetPreferredSeries() {
        return (this.preferredSeries!= null);
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
            SeriesLink thePreferredSeries;
            thePreferredSeries = this.getPreferredSeries();
            strategy.appendField(locator, this, "preferredSeries", buffer, thePreferredSeries);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof SeriesLinksWithPreferred)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final SeriesLinksWithPreferred that = ((SeriesLinksWithPreferred) object);
        {
            SeriesLink lhsPreferredSeries;
            lhsPreferredSeries = this.getPreferredSeries();
            SeriesLink rhsPreferredSeries;
            rhsPreferredSeries = that.getPreferredSeries();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "preferredSeries", lhsPreferredSeries), LocatorUtils.property(thatLocator, "preferredSeries", rhsPreferredSeries), lhsPreferredSeries, rhsPreferredSeries)) {
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
            SeriesLink thePreferredSeries;
            thePreferredSeries = this.getPreferredSeries();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "preferredSeries", thePreferredSeries), currentHashCode, thePreferredSeries);
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
        if (draftCopy instanceof SeriesLinksWithPreferred) {
            final SeriesLinksWithPreferred copy = ((SeriesLinksWithPreferred) draftCopy);
            if (this.isSetPreferredSeries()) {
                SeriesLink sourcePreferredSeries;
                sourcePreferredSeries = this.getPreferredSeries();
                SeriesLink copyPreferredSeries = ((SeriesLink) strategy.copy(LocatorUtils.property(locator, "preferredSeries", sourcePreferredSeries), sourcePreferredSeries));
                copy.setPreferredSeries(copyPreferredSeries);
            } else {
                copy.preferredSeries = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new SeriesLinksWithPreferred();
    }

}
