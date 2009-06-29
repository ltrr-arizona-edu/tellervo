
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
import org.tridas.annotations.TridasCustomDictionary;
import org.tridas.annotations.TridasCustomDictionaryType;


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
 *         &lt;element ref="{http://www.tridas.org/1.2}coverageTemporal"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}coverageTemporalFoundation"/>
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    @TridasCustomDictionary(dictionary = "coverageTemporal", type = TridasCustomDictionaryType.CORINA_CONTROLLEDVOC)
    protected String coverageTemporal;
    @XmlElement(required = true)
    @TridasCustomDictionary(dictionary = "coverageTemporalFoundation", type = TridasCustomDictionaryType.CORINA_CONTROLLEDVOC)
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasCoverage)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasCoverage that = ((TridasCoverage) object);
        equalsBuilder.append(this.getCoverageTemporal(), that.getCoverageTemporal());
        equalsBuilder.append(this.getCoverageTemporalFoundation(), that.getCoverageTemporalFoundation());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasCoverage)) {
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
        hashCodeBuilder.append(this.getCoverageTemporal());
        hashCodeBuilder.append(this.getCoverageTemporalFoundation());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theCoverageTemporal;
            theCoverageTemporal = this.getCoverageTemporal();
            toStringBuilder.append("coverageTemporal", theCoverageTemporal);
        }
        {
            String theCoverageTemporalFoundation;
            theCoverageTemporalFoundation = this.getCoverageTemporalFoundation();
            toStringBuilder.append("coverageTemporalFoundation", theCoverageTemporalFoundation);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasCoverage copy = ((target == null)?((TridasCoverage) createCopy()):((TridasCoverage) target));
        {
            String sourceCoverageTemporal;
            sourceCoverageTemporal = this.getCoverageTemporal();
            String copyCoverageTemporal = ((String) copyBuilder.copy(sourceCoverageTemporal));
            copy.setCoverageTemporal(copyCoverageTemporal);
        }
        {
            String sourceCoverageTemporalFoundation;
            sourceCoverageTemporalFoundation = this.getCoverageTemporalFoundation();
            String copyCoverageTemporalFoundation = ((String) copyBuilder.copy(sourceCoverageTemporalFoundation));
            copy.setCoverageTemporalFoundation(copyCoverageTemporalFoundation);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasCoverage();
    }

}
