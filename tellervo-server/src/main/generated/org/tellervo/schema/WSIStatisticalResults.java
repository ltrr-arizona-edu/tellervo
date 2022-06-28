
package org.tellervo.schema;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}statistic" maxOccurs="unbounded" minOccurs="0"/>
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
    "statistics"
})
@XmlRootElement(name = "statisticalResults")
public class WSIStatisticalResults implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "statistic")
    protected List<WSIStatistic> statistics;

    /**
     * Gets the value of the statistics property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statistics property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatistics().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIStatistic }
     * 
     * 
     */
    public List<WSIStatistic> getStatistics() {
        if (statistics == null) {
            statistics = new ArrayList<WSIStatistic>();
        }
        return this.statistics;
    }

    public boolean isSetStatistics() {
        return ((this.statistics!= null)&&(!this.statistics.isEmpty()));
    }

    public void unsetStatistics() {
        this.statistics = null;
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
            List<WSIStatistic> theStatistics;
            theStatistics = (this.isSetStatistics()?this.getStatistics():null);
            strategy.appendField(locator, this, "statistics", buffer, theStatistics);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIStatisticalResults)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIStatisticalResults that = ((WSIStatisticalResults) object);
        {
            List<WSIStatistic> lhsStatistics;
            lhsStatistics = (this.isSetStatistics()?this.getStatistics():null);
            List<WSIStatistic> rhsStatistics;
            rhsStatistics = (that.isSetStatistics()?that.getStatistics():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "statistics", lhsStatistics), LocatorUtils.property(thatLocator, "statistics", rhsStatistics), lhsStatistics, rhsStatistics)) {
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
            List<WSIStatistic> theStatistics;
            theStatistics = (this.isSetStatistics()?this.getStatistics():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "statistics", theStatistics), currentHashCode, theStatistics);
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
        if (draftCopy instanceof WSIStatisticalResults) {
            final WSIStatisticalResults copy = ((WSIStatisticalResults) draftCopy);
            if (this.isSetStatistics()) {
                List<WSIStatistic> sourceStatistics;
                sourceStatistics = (this.isSetStatistics()?this.getStatistics():null);
                @SuppressWarnings("unchecked")
                List<WSIStatistic> copyStatistics = ((List<WSIStatistic> ) strategy.copy(LocatorUtils.property(locator, "statistics", sourceStatistics), sourceStatistics));
                copy.unsetStatistics();
                List<WSIStatistic> uniqueStatisticsl = copy.getStatistics();
                uniqueStatisticsl.addAll(copyStatistics);
            } else {
                copy.unsetStatistics();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIStatisticalResults();
    }

    /**
     * Sets the value of the statistics property.
     * 
     * @param statistics
     *     allowed object is
     *     {@link WSIStatistic }
     *     
     */
    public void setStatistics(List<WSIStatistic> statistics) {
        this.statistics = statistics;
    }

}
