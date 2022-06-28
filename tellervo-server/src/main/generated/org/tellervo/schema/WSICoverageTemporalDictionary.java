
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
import org.tridas.schema.ControlledVoc;


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
 *         &lt;element name="coverageTemporal" type="{http://www.tridas.org/1.2.2}controlledVoc" maxOccurs="unbounded" minOccurs="0"/>
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
    "coverageTemporals"
})
@XmlRootElement(name = "coverageTemporalDictionary")
public class WSICoverageTemporalDictionary implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "coverageTemporal")
    protected List<ControlledVoc> coverageTemporals;

    /**
     * Gets the value of the coverageTemporals property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coverageTemporals property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoverageTemporals().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlledVoc }
     * 
     * 
     */
    public List<ControlledVoc> getCoverageTemporals() {
        if (coverageTemporals == null) {
            coverageTemporals = new ArrayList<ControlledVoc>();
        }
        return this.coverageTemporals;
    }

    public boolean isSetCoverageTemporals() {
        return ((this.coverageTemporals!= null)&&(!this.coverageTemporals.isEmpty()));
    }

    public void unsetCoverageTemporals() {
        this.coverageTemporals = null;
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
            List<ControlledVoc> theCoverageTemporals;
            theCoverageTemporals = (this.isSetCoverageTemporals()?this.getCoverageTemporals():null);
            strategy.appendField(locator, this, "coverageTemporals", buffer, theCoverageTemporals);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSICoverageTemporalDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSICoverageTemporalDictionary that = ((WSICoverageTemporalDictionary) object);
        {
            List<ControlledVoc> lhsCoverageTemporals;
            lhsCoverageTemporals = (this.isSetCoverageTemporals()?this.getCoverageTemporals():null);
            List<ControlledVoc> rhsCoverageTemporals;
            rhsCoverageTemporals = (that.isSetCoverageTemporals()?that.getCoverageTemporals():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "coverageTemporals", lhsCoverageTemporals), LocatorUtils.property(thatLocator, "coverageTemporals", rhsCoverageTemporals), lhsCoverageTemporals, rhsCoverageTemporals)) {
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
            List<ControlledVoc> theCoverageTemporals;
            theCoverageTemporals = (this.isSetCoverageTemporals()?this.getCoverageTemporals():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "coverageTemporals", theCoverageTemporals), currentHashCode, theCoverageTemporals);
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
        if (draftCopy instanceof WSICoverageTemporalDictionary) {
            final WSICoverageTemporalDictionary copy = ((WSICoverageTemporalDictionary) draftCopy);
            if (this.isSetCoverageTemporals()) {
                List<ControlledVoc> sourceCoverageTemporals;
                sourceCoverageTemporals = (this.isSetCoverageTemporals()?this.getCoverageTemporals():null);
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyCoverageTemporals = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "coverageTemporals", sourceCoverageTemporals), sourceCoverageTemporals));
                copy.unsetCoverageTemporals();
                List<ControlledVoc> uniqueCoverageTemporalsl = copy.getCoverageTemporals();
                uniqueCoverageTemporalsl.addAll(copyCoverageTemporals);
            } else {
                copy.unsetCoverageTemporals();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSICoverageTemporalDictionary();
    }

    /**
     * Sets the value of the coverageTemporals property.
     * 
     * @param coverageTemporals
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setCoverageTemporals(List<ControlledVoc> coverageTemporals) {
        this.coverageTemporals = coverageTemporals;
    }

}
