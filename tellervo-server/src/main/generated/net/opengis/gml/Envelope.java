
package net.opengis.gml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

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
            Pos theLowerCorner;
            theLowerCorner = this.getLowerCorner();
            strategy.appendField(locator, this, "lowerCorner", buffer, theLowerCorner);
        }
        {
            Pos theUpperCorner;
            theUpperCorner = this.getUpperCorner();
            strategy.appendField(locator, this, "upperCorner", buffer, theUpperCorner);
        }
        {
            String theSrsName;
            theSrsName = this.getSrsName();
            strategy.appendField(locator, this, "srsName", buffer, theSrsName);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Envelope)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Envelope that = ((Envelope) object);
        {
            Pos lhsLowerCorner;
            lhsLowerCorner = this.getLowerCorner();
            Pos rhsLowerCorner;
            rhsLowerCorner = that.getLowerCorner();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "lowerCorner", lhsLowerCorner), LocatorUtils.property(thatLocator, "lowerCorner", rhsLowerCorner), lhsLowerCorner, rhsLowerCorner)) {
                return false;
            }
        }
        {
            Pos lhsUpperCorner;
            lhsUpperCorner = this.getUpperCorner();
            Pos rhsUpperCorner;
            rhsUpperCorner = that.getUpperCorner();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "upperCorner", lhsUpperCorner), LocatorUtils.property(thatLocator, "upperCorner", rhsUpperCorner), lhsUpperCorner, rhsUpperCorner)) {
                return false;
            }
        }
        {
            String lhsSrsName;
            lhsSrsName = this.getSrsName();
            String rhsSrsName;
            rhsSrsName = that.getSrsName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "srsName", lhsSrsName), LocatorUtils.property(thatLocator, "srsName", rhsSrsName), lhsSrsName, rhsSrsName)) {
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
            Pos theLowerCorner;
            theLowerCorner = this.getLowerCorner();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "lowerCorner", theLowerCorner), currentHashCode, theLowerCorner);
        }
        {
            Pos theUpperCorner;
            theUpperCorner = this.getUpperCorner();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "upperCorner", theUpperCorner), currentHashCode, theUpperCorner);
        }
        {
            String theSrsName;
            theSrsName = this.getSrsName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "srsName", theSrsName), currentHashCode, theSrsName);
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
        if (draftCopy instanceof Envelope) {
            final Envelope copy = ((Envelope) draftCopy);
            if (this.isSetLowerCorner()) {
                Pos sourceLowerCorner;
                sourceLowerCorner = this.getLowerCorner();
                Pos copyLowerCorner = ((Pos) strategy.copy(LocatorUtils.property(locator, "lowerCorner", sourceLowerCorner), sourceLowerCorner));
                copy.setLowerCorner(copyLowerCorner);
            } else {
                copy.lowerCorner = null;
            }
            if (this.isSetUpperCorner()) {
                Pos sourceUpperCorner;
                sourceUpperCorner = this.getUpperCorner();
                Pos copyUpperCorner = ((Pos) strategy.copy(LocatorUtils.property(locator, "upperCorner", sourceUpperCorner), sourceUpperCorner));
                copy.setUpperCorner(copyUpperCorner);
            } else {
                copy.upperCorner = null;
            }
            if (this.isSetSrsName()) {
                String sourceSrsName;
                sourceSrsName = this.getSrsName();
                String copySrsName = ((String) strategy.copy(LocatorUtils.property(locator, "srsName", sourceSrsName), sourceSrsName));
                copy.setSrsName(copySrsName);
            } else {
                copy.srsName = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new Envelope();
    }

}
