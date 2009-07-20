
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
 *         &lt;element ref="{http://www.tridas.org/1.3}measurementSeriesPlaceholder"/>
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
    "measurementSeriesPlaceholder"
})
@XmlRootElement(name = "radiusPlaceholder")
public class TridasRadiusPlaceholder
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected TridasMeasurementSeriesPlaceholder measurementSeriesPlaceholder;

    /**
     * Gets the value of the measurementSeriesPlaceholder property.
     * 
     * @return
     *     possible object is
     *     {@link TridasMeasurementSeriesPlaceholder }
     *     
     */
    public TridasMeasurementSeriesPlaceholder getMeasurementSeriesPlaceholder() {
        return measurementSeriesPlaceholder;
    }

    /**
     * Sets the value of the measurementSeriesPlaceholder property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasMeasurementSeriesPlaceholder }
     *     
     */
    public void setMeasurementSeriesPlaceholder(TridasMeasurementSeriesPlaceholder value) {
        this.measurementSeriesPlaceholder = value;
    }

    public boolean isSetMeasurementSeriesPlaceholder() {
        return (this.measurementSeriesPlaceholder!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasRadiusPlaceholder)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasRadiusPlaceholder that = ((TridasRadiusPlaceholder) object);
        equalsBuilder.append(this.getMeasurementSeriesPlaceholder(), that.getMeasurementSeriesPlaceholder());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasRadiusPlaceholder)) {
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
        hashCodeBuilder.append(this.getMeasurementSeriesPlaceholder());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            TridasMeasurementSeriesPlaceholder theMeasurementSeriesPlaceholder;
            theMeasurementSeriesPlaceholder = this.getMeasurementSeriesPlaceholder();
            toStringBuilder.append("measurementSeriesPlaceholder", theMeasurementSeriesPlaceholder);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasRadiusPlaceholder copy = ((target == null)?((TridasRadiusPlaceholder) createCopy()):((TridasRadiusPlaceholder) target));
        {
            TridasMeasurementSeriesPlaceholder sourceMeasurementSeriesPlaceholder;
            sourceMeasurementSeriesPlaceholder = this.getMeasurementSeriesPlaceholder();
            TridasMeasurementSeriesPlaceholder copyMeasurementSeriesPlaceholder = ((TridasMeasurementSeriesPlaceholder) copyBuilder.copy(sourceMeasurementSeriesPlaceholder));
            copy.setMeasurementSeriesPlaceholder(copyMeasurementSeriesPlaceholder);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasRadiusPlaceholder();
    }

}
