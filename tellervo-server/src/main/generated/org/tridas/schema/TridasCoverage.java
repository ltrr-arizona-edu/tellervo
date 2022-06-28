
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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}coverageTemporal"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}coverageTemporalFoundation"/>
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
    "coverageTemporal",
    "coverageTemporalFoundation"
})
@XmlRootElement(name = "coverage")
public class TridasCoverage
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected String coverageTemporal;
    @XmlElement(required = true)
    protected String coverageTemporalFoundation;

    /**
     * Gets the value of the coverageTemporal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoverageTemporal() {
        return coverageTemporal;
    }

    /**
     * Sets the value of the coverageTemporal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoverageTemporal(String value) {
        this.coverageTemporal = value;
    }

    public boolean isSetCoverageTemporal() {
        return (this.coverageTemporal!= null);
    }

    /**
     * Gets the value of the coverageTemporalFoundation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoverageTemporalFoundation() {
        return coverageTemporalFoundation;
    }

    /**
     * Sets the value of the coverageTemporalFoundation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoverageTemporalFoundation(String value) {
        this.coverageTemporalFoundation = value;
    }

    public boolean isSetCoverageTemporalFoundation() {
        return (this.coverageTemporalFoundation!= null);
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
            String theCoverageTemporal;
            theCoverageTemporal = this.getCoverageTemporal();
            strategy.appendField(locator, this, "coverageTemporal", buffer, theCoverageTemporal);
        }
        {
            String theCoverageTemporalFoundation;
            theCoverageTemporalFoundation = this.getCoverageTemporalFoundation();
            strategy.appendField(locator, this, "coverageTemporalFoundation", buffer, theCoverageTemporalFoundation);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasCoverage)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasCoverage that = ((TridasCoverage) object);
        {
            String lhsCoverageTemporal;
            lhsCoverageTemporal = this.getCoverageTemporal();
            String rhsCoverageTemporal;
            rhsCoverageTemporal = that.getCoverageTemporal();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "coverageTemporal", lhsCoverageTemporal), LocatorUtils.property(thatLocator, "coverageTemporal", rhsCoverageTemporal), lhsCoverageTemporal, rhsCoverageTemporal)) {
                return false;
            }
        }
        {
            String lhsCoverageTemporalFoundation;
            lhsCoverageTemporalFoundation = this.getCoverageTemporalFoundation();
            String rhsCoverageTemporalFoundation;
            rhsCoverageTemporalFoundation = that.getCoverageTemporalFoundation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "coverageTemporalFoundation", lhsCoverageTemporalFoundation), LocatorUtils.property(thatLocator, "coverageTemporalFoundation", rhsCoverageTemporalFoundation), lhsCoverageTemporalFoundation, rhsCoverageTemporalFoundation)) {
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
            String theCoverageTemporal;
            theCoverageTemporal = this.getCoverageTemporal();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "coverageTemporal", theCoverageTemporal), currentHashCode, theCoverageTemporal);
        }
        {
            String theCoverageTemporalFoundation;
            theCoverageTemporalFoundation = this.getCoverageTemporalFoundation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "coverageTemporalFoundation", theCoverageTemporalFoundation), currentHashCode, theCoverageTemporalFoundation);
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
        if (draftCopy instanceof TridasCoverage) {
            final TridasCoverage copy = ((TridasCoverage) draftCopy);
            if (this.isSetCoverageTemporal()) {
                String sourceCoverageTemporal;
                sourceCoverageTemporal = this.getCoverageTemporal();
                String copyCoverageTemporal = ((String) strategy.copy(LocatorUtils.property(locator, "coverageTemporal", sourceCoverageTemporal), sourceCoverageTemporal));
                copy.setCoverageTemporal(copyCoverageTemporal);
            } else {
                copy.coverageTemporal = null;
            }
            if (this.isSetCoverageTemporalFoundation()) {
                String sourceCoverageTemporalFoundation;
                sourceCoverageTemporalFoundation = this.getCoverageTemporalFoundation();
                String copyCoverageTemporalFoundation = ((String) strategy.copy(LocatorUtils.property(locator, "coverageTemporalFoundation", sourceCoverageTemporalFoundation), sourceCoverageTemporalFoundation));
                copy.setCoverageTemporalFoundation(copyCoverageTemporalFoundation);
            } else {
                copy.coverageTemporalFoundation = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasCoverage();
    }

}
