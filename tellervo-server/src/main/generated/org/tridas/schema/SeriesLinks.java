
package org.tridas.schema;

import java.util.ArrayList;
import java.util.List;
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
 * <p>Java class for seriesLinks complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="seriesLinks">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="series" type="{http://www.tridas.org/1.2.2}seriesLink" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "seriesLinks", propOrder = {
    "series"
})
public class SeriesLinks
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected List<SeriesLink> series;

    /**
     * Gets the value of the series property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the series property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSeries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SeriesLink }
     * 
     * 
     */
    public List<SeriesLink> getSeries() {
        if (series == null) {
            series = new ArrayList<SeriesLink>();
        }
        return this.series;
    }

    public boolean isSetSeries() {
        return ((this.series!= null)&&(!this.series.isEmpty()));
    }

    public void unsetSeries() {
        this.series = null;
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
            List<SeriesLink> theSeries;
            theSeries = (this.isSetSeries()?this.getSeries():null);
            strategy.appendField(locator, this, "series", buffer, theSeries);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof SeriesLinks)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final SeriesLinks that = ((SeriesLinks) object);
        {
            List<SeriesLink> lhsSeries;
            lhsSeries = (this.isSetSeries()?this.getSeries():null);
            List<SeriesLink> rhsSeries;
            rhsSeries = (that.isSetSeries()?that.getSeries():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "series", lhsSeries), LocatorUtils.property(thatLocator, "series", rhsSeries), lhsSeries, rhsSeries)) {
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
            List<SeriesLink> theSeries;
            theSeries = (this.isSetSeries()?this.getSeries():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "series", theSeries), currentHashCode, theSeries);
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
        if (draftCopy instanceof SeriesLinks) {
            final SeriesLinks copy = ((SeriesLinks) draftCopy);
            if (this.isSetSeries()) {
                List<SeriesLink> sourceSeries;
                sourceSeries = (this.isSetSeries()?this.getSeries():null);
                @SuppressWarnings("unchecked")
                List<SeriesLink> copySeries = ((List<SeriesLink> ) strategy.copy(LocatorUtils.property(locator, "series", sourceSeries), sourceSeries));
                copy.unsetSeries();
                List<SeriesLink> uniqueSeriesl = copy.getSeries();
                uniqueSeriesl.addAll(copySeries);
            } else {
                copy.unsetSeries();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new SeriesLinks();
    }

    /**
     * Sets the value of the series property.
     * 
     * @param series
     *     allowed object is
     *     {@link SeriesLink }
     *     
     */
    public void setSeries(List<SeriesLink> series) {
        this.series = series;
    }

}
