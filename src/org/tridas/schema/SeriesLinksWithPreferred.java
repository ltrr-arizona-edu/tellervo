
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * <p>Java class for seriesLinksWithPreferred complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="seriesLinksWithPreferred">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tridas.org/1.3}seriesLinks">
 *       &lt;sequence>
 *         &lt;element name="preferredSeries" type="{http://www.tridas.org/1.3}seriesLink" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
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
    extends SeriesLinks
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SeriesLinksWithPreferred)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final SeriesLinksWithPreferred that = ((SeriesLinksWithPreferred) object);
        equalsBuilder.append(this.getPreferredSeries(), that.getPreferredSeries());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SeriesLinksWithPreferred)) {
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
        super.hashCode(hashCodeBuilder);
        hashCodeBuilder.append(this.getPreferredSeries());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            SeriesLink thePreferredSeries;
            thePreferredSeries = this.getPreferredSeries();
            toStringBuilder.append("preferredSeries", thePreferredSeries);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final SeriesLinksWithPreferred copy = ((target == null)?((SeriesLinksWithPreferred) createCopy()):((SeriesLinksWithPreferred) target));
        super.copyTo(copy, copyBuilder);
        if (this.isSetPreferredSeries()) {
            SeriesLink sourcePreferredSeries;
            sourcePreferredSeries = this.getPreferredSeries();
            SeriesLink copyPreferredSeries = ((SeriesLink) copyBuilder.copy(sourcePreferredSeries));
            copy.setPreferredSeries(copyPreferredSeries);
        } else {
            copy.preferredSeries = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new SeriesLinksWithPreferred();
    }

}
