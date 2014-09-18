
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.tridas.org/1.2.2>controlledVoc">
 *       &lt;attribute name="normalTridas" type="{http://www.tridas.org/1.2.2}normalTridasUnit" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "unit")
public class TridasUnit
    extends ControlledVoc
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlAttribute(name = "normalTridas")
    protected NormalTridasUnit normalTridas;

    /**
     * Gets the value of the normalTridas property.
     * 
     * @return
     *     possible object is
     *     {@link NormalTridasUnit }
     *     
     */
    public NormalTridasUnit getNormalTridas() {
        return normalTridas;
    }

    /**
     * Sets the value of the normalTridas property.
     * 
     * @param value
     *     allowed object is
     *     {@link NormalTridasUnit }
     *     
     */
    public void setNormalTridas(NormalTridasUnit value) {
        this.normalTridas = value;
    }

    public boolean isSetNormalTridas() {
        return (this.normalTridas!= null);
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
        super.appendFields(locator, buffer, strategy);
        {
            NormalTridasUnit theNormalTridas;
            theNormalTridas = this.getNormalTridas();
            strategy.appendField(locator, this, "normalTridas", buffer, theNormalTridas);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasUnit)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final TridasUnit that = ((TridasUnit) object);
        {
            NormalTridasUnit lhsNormalTridas;
            lhsNormalTridas = this.getNormalTridas();
            NormalTridasUnit rhsNormalTridas;
            rhsNormalTridas = that.getNormalTridas();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "normalTridas", lhsNormalTridas), LocatorUtils.property(thatLocator, "normalTridas", rhsNormalTridas), lhsNormalTridas, rhsNormalTridas)) {
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
        int currentHashCode = super.hashCode(locator, strategy);
        {
            NormalTridasUnit theNormalTridas;
            theNormalTridas = this.getNormalTridas();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "normalTridas", theNormalTridas), currentHashCode, theNormalTridas);
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
        super.copyTo(locator, draftCopy, strategy);
        if (draftCopy instanceof TridasUnit) {
            final TridasUnit copy = ((TridasUnit) draftCopy);
            if (this.isSetNormalTridas()) {
                NormalTridasUnit sourceNormalTridas;
                sourceNormalTridas = this.getNormalTridas();
                NormalTridasUnit copyNormalTridas = ((NormalTridasUnit) strategy.copy(LocatorUtils.property(locator, "normalTridas", sourceNormalTridas), sourceNormalTridas));
                copy.setNormalTridas(copyNormalTridas);
            } else {
                copy.normalTridas = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasUnit();
    }

}
