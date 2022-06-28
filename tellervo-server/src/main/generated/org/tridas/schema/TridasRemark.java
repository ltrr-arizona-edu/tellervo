
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
import org.tridas.adapters.IntegerAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.tridas.org/1.2.2>controlledVoc">
 *       &lt;attribute name="normalTridas" type="{http://www.tridas.org/1.2.2}normalTridasRemark" />
 *       &lt;attribute name="inheritedCount" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "remark")
public class TridasRemark
    extends ControlledVoc
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlAttribute(name = "normalTridas")
    protected NormalTridasRemark normalTridas;
    @XmlAttribute(name = "inheritedCount")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer inheritedCount;

    /**
     * Gets the value of the normalTridas property.
     * 
     * @return
     *     possible object is
     *     {@link NormalTridasRemark }
     *     
     */
    public NormalTridasRemark getNormalTridas() {
        return normalTridas;
    }

    /**
     * Sets the value of the normalTridas property.
     * 
     * @param value
     *     allowed object is
     *     {@link NormalTridasRemark }
     *     
     */
    public void setNormalTridas(NormalTridasRemark value) {
        this.normalTridas = value;
    }

    public boolean isSetNormalTridas() {
        return (this.normalTridas!= null);
    }

    /**
     * Gets the value of the inheritedCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getInheritedCount() {
        return inheritedCount;
    }

    /**
     * Sets the value of the inheritedCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInheritedCount(Integer value) {
        this.inheritedCount = value;
    }

    public boolean isSetInheritedCount() {
        return (this.inheritedCount!= null);
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
            NormalTridasRemark theNormalTridas;
            theNormalTridas = this.getNormalTridas();
            strategy.appendField(locator, this, "normalTridas", buffer, theNormalTridas);
        }
        {
            Integer theInheritedCount;
            theInheritedCount = this.getInheritedCount();
            strategy.appendField(locator, this, "inheritedCount", buffer, theInheritedCount);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasRemark)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final TridasRemark that = ((TridasRemark) object);
        {
            NormalTridasRemark lhsNormalTridas;
            lhsNormalTridas = this.getNormalTridas();
            NormalTridasRemark rhsNormalTridas;
            rhsNormalTridas = that.getNormalTridas();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "normalTridas", lhsNormalTridas), LocatorUtils.property(thatLocator, "normalTridas", rhsNormalTridas), lhsNormalTridas, rhsNormalTridas)) {
                return false;
            }
        }
        {
            Integer lhsInheritedCount;
            lhsInheritedCount = this.getInheritedCount();
            Integer rhsInheritedCount;
            rhsInheritedCount = that.getInheritedCount();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "inheritedCount", lhsInheritedCount), LocatorUtils.property(thatLocator, "inheritedCount", rhsInheritedCount), lhsInheritedCount, rhsInheritedCount)) {
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
            NormalTridasRemark theNormalTridas;
            theNormalTridas = this.getNormalTridas();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "normalTridas", theNormalTridas), currentHashCode, theNormalTridas);
        }
        {
            Integer theInheritedCount;
            theInheritedCount = this.getInheritedCount();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "inheritedCount", theInheritedCount), currentHashCode, theInheritedCount);
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
        if (draftCopy instanceof TridasRemark) {
            final TridasRemark copy = ((TridasRemark) draftCopy);
            if (this.isSetNormalTridas()) {
                NormalTridasRemark sourceNormalTridas;
                sourceNormalTridas = this.getNormalTridas();
                NormalTridasRemark copyNormalTridas = ((NormalTridasRemark) strategy.copy(LocatorUtils.property(locator, "normalTridas", sourceNormalTridas), sourceNormalTridas));
                copy.setNormalTridas(copyNormalTridas);
            } else {
                copy.normalTridas = null;
            }
            if (this.isSetInheritedCount()) {
                Integer sourceInheritedCount;
                sourceInheritedCount = this.getInheritedCount();
                Integer copyInheritedCount = ((Integer) strategy.copy(LocatorUtils.property(locator, "inheritedCount", sourceInheritedCount), sourceInheritedCount));
                copy.setInheritedCount(copyInheritedCount);
            } else {
                copy.inheritedCount = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasRemark();
    }

}
