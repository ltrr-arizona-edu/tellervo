
package net.opengis.gml.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 * 
 *             Envelope defines an extent using a pair of positions
 *             defining opposite corners in arbitrary dimensions. The
 *             first direct position is the "lower corner" (a coordinate
 *             position consisting of all the minimal ordinates for each
 *             dimension for all points within the envelope), the second
 *             one the "upper corner" (a coordinate position consisting
 *             of all the maximal ordinates for each dimension for all
 *             points within the envelope).
 *          
 * 
 * <p>Java class for EnvelopeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnvelopeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lowerCorner" type="{http://www.opengis.net/gml}DirectPositionType"/>
 *         &lt;element name="upperCorner" type="{http://www.opengis.net/gml}DirectPositionType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="srsName" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnvelopeType", propOrder = {
    "lowerCorner",
    "upperCorner"
})
@XmlRootElement(name = "Envelope")
public class Envelope
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected Pos lowerCorner;
    @XmlElement(required = true)
    protected Pos upperCorner;
    @XmlAttribute(name = "srsName", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String srsName;

    /**
     * Gets the value of the lowerCorner property.
     * 
     * @return
     *     possible object is
     *     {@link Pos }
     *     
     */
    public Pos getLowerCorner() {
        return lowerCorner;
    }

    /**
     * Sets the value of the lowerCorner property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pos }
     *     
     */
    public void setLowerCorner(Pos value) {
        this.lowerCorner = value;
    }

    public boolean isSetLowerCorner() {
        return (this.lowerCorner!= null);
    }

    /**
     * Gets the value of the upperCorner property.
     * 
     * @return
     *     possible object is
     *     {@link Pos }
     *     
     */
    public Pos getUpperCorner() {
        return upperCorner;
    }

    /**
     * Sets the value of the upperCorner property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pos }
     *     
     */
    public void setUpperCorner(Pos value) {
        this.upperCorner = value;
    }

    public boolean isSetUpperCorner() {
        return (this.upperCorner!= null);
    }

    /**
     * Gets the value of the srsName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrsName() {
        return srsName;
    }

    /**
     * Sets the value of the srsName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrsName(String value) {
        this.srsName = value;
    }

    public boolean isSetSrsName() {
        return (this.srsName!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Envelope)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Envelope that = ((Envelope) object);
        equalsBuilder.append(this.getLowerCorner(), that.getLowerCorner());
        equalsBuilder.append(this.getUpperCorner(), that.getUpperCorner());
        equalsBuilder.append(this.getSrsName(), that.getSrsName());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Envelope)) {
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
        hashCodeBuilder.append(this.getLowerCorner());
        hashCodeBuilder.append(this.getUpperCorner());
        hashCodeBuilder.append(this.getSrsName());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            Pos theLowerCorner;
            theLowerCorner = this.getLowerCorner();
            toStringBuilder.append("lowerCorner", theLowerCorner);
        }
        {
            Pos theUpperCorner;
            theUpperCorner = this.getUpperCorner();
            toStringBuilder.append("upperCorner", theUpperCorner);
        }
        {
            String theSrsName;
            theSrsName = this.getSrsName();
            toStringBuilder.append("srsName", theSrsName);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final Envelope copy = ((target == null)?((Envelope) createCopy()):((Envelope) target));
        if (this.isSetLowerCorner()) {
            Pos sourceLowerCorner;
            sourceLowerCorner = this.getLowerCorner();
            Pos copyLowerCorner = ((Pos) copyBuilder.copy(sourceLowerCorner));
            copy.setLowerCorner(copyLowerCorner);
        } else {
            copy.lowerCorner = null;
        }
        if (this.isSetUpperCorner()) {
            Pos sourceUpperCorner;
            sourceUpperCorner = this.getUpperCorner();
            Pos copyUpperCorner = ((Pos) copyBuilder.copy(sourceUpperCorner));
            copy.setUpperCorner(copyUpperCorner);
        } else {
            copy.upperCorner = null;
        }
        if (this.isSetSrsName()) {
            String sourceSrsName;
            sourceSrsName = this.getSrsName();
            String copySrsName = ((String) copyBuilder.copy(sourceSrsName));
            copy.setSrsName(copySrsName);
        } else {
            copy.srsName = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new Envelope();
    }

}
