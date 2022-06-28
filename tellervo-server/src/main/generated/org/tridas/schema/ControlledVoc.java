
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
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


/**
 * 
 * 				A controlled vocabulary is used to limit users to a pick list of values.
 * 			
 * 
 * <p>Java class for controlledVoc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="controlledVoc">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="normalStd" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="normalId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="normal" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="lang" type="{http://www.w3.org/2001/XMLSchema}language" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "controlledVoc", propOrder = {
    "value"
})
@XmlSeeAlso({
    TridasMeasuringMethod.class,
    TridasShape.class,
    TridasUnit.class,
    TridasVariable.class,
    TridasRemark.class
})
public class ControlledVoc
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlValue
    protected String value;
    @XmlAttribute(name = "normalStd")
    @XmlSchemaType(name = "anySimpleType")
    protected String normalStd;
    @XmlAttribute(name = "normalId")
    @XmlSchemaType(name = "anySimpleType")
    protected String normalId;
    @XmlAttribute(name = "normal")
    @XmlSchemaType(name = "anySimpleType")
    protected String normal;
    @XmlAttribute(name = "lang")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String lang;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSetValue() {
        return (this.value!= null);
    }

    /**
     * Gets the value of the normalStd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNormalStd() {
        return normalStd;
    }

    /**
     * Sets the value of the normalStd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNormalStd(String value) {
        this.normalStd = value;
    }

    public boolean isSetNormalStd() {
        return (this.normalStd!= null);
    }

    /**
     * Gets the value of the normalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNormalId() {
        return normalId;
    }

    /**
     * Sets the value of the normalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNormalId(String value) {
        this.normalId = value;
    }

    public boolean isSetNormalId() {
        return (this.normalId!= null);
    }

    /**
     * Gets the value of the normal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNormal() {
        return normal;
    }

    /**
     * Sets the value of the normal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNormal(String value) {
        this.normal = value;
    }

    public boolean isSetNormal() {
        return (this.normal!= null);
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    public boolean isSetLang() {
        return (this.lang!= null);
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
            String theValue;
            theValue = this.getValue();
            strategy.appendField(locator, this, "value", buffer, theValue);
        }
        {
            String theNormalStd;
            theNormalStd = this.getNormalStd();
            strategy.appendField(locator, this, "normalStd", buffer, theNormalStd);
        }
        {
            String theNormalId;
            theNormalId = this.getNormalId();
            strategy.appendField(locator, this, "normalId", buffer, theNormalId);
        }
        {
            String theNormal;
            theNormal = this.getNormal();
            strategy.appendField(locator, this, "normal", buffer, theNormal);
        }
        {
            String theLang;
            theLang = this.getLang();
            strategy.appendField(locator, this, "lang", buffer, theLang);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof ControlledVoc)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final ControlledVoc that = ((ControlledVoc) object);
        {
            String lhsValue;
            lhsValue = this.getValue();
            String rhsValue;
            rhsValue = that.getValue();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "value", lhsValue), LocatorUtils.property(thatLocator, "value", rhsValue), lhsValue, rhsValue)) {
                return false;
            }
        }
        {
            String lhsNormalStd;
            lhsNormalStd = this.getNormalStd();
            String rhsNormalStd;
            rhsNormalStd = that.getNormalStd();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "normalStd", lhsNormalStd), LocatorUtils.property(thatLocator, "normalStd", rhsNormalStd), lhsNormalStd, rhsNormalStd)) {
                return false;
            }
        }
        {
            String lhsNormalId;
            lhsNormalId = this.getNormalId();
            String rhsNormalId;
            rhsNormalId = that.getNormalId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "normalId", lhsNormalId), LocatorUtils.property(thatLocator, "normalId", rhsNormalId), lhsNormalId, rhsNormalId)) {
                return false;
            }
        }
        {
            String lhsNormal;
            lhsNormal = this.getNormal();
            String rhsNormal;
            rhsNormal = that.getNormal();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "normal", lhsNormal), LocatorUtils.property(thatLocator, "normal", rhsNormal), lhsNormal, rhsNormal)) {
                return false;
            }
        }
        {
            String lhsLang;
            lhsLang = this.getLang();
            String rhsLang;
            rhsLang = that.getLang();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "lang", lhsLang), LocatorUtils.property(thatLocator, "lang", rhsLang), lhsLang, rhsLang)) {
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
            String theValue;
            theValue = this.getValue();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "value", theValue), currentHashCode, theValue);
        }
        {
            String theNormalStd;
            theNormalStd = this.getNormalStd();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "normalStd", theNormalStd), currentHashCode, theNormalStd);
        }
        {
            String theNormalId;
            theNormalId = this.getNormalId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "normalId", theNormalId), currentHashCode, theNormalId);
        }
        {
            String theNormal;
            theNormal = this.getNormal();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "normal", theNormal), currentHashCode, theNormal);
        }
        {
            String theLang;
            theLang = this.getLang();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "lang", theLang), currentHashCode, theLang);
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
        if (draftCopy instanceof ControlledVoc) {
            final ControlledVoc copy = ((ControlledVoc) draftCopy);
            if (this.isSetValue()) {
                String sourceValue;
                sourceValue = this.getValue();
                String copyValue = ((String) strategy.copy(LocatorUtils.property(locator, "value", sourceValue), sourceValue));
                copy.setValue(copyValue);
            } else {
                copy.value = null;
            }
            if (this.isSetNormalStd()) {
                String sourceNormalStd;
                sourceNormalStd = this.getNormalStd();
                String copyNormalStd = ((String) strategy.copy(LocatorUtils.property(locator, "normalStd", sourceNormalStd), sourceNormalStd));
                copy.setNormalStd(copyNormalStd);
            } else {
                copy.normalStd = null;
            }
            if (this.isSetNormalId()) {
                String sourceNormalId;
                sourceNormalId = this.getNormalId();
                String copyNormalId = ((String) strategy.copy(LocatorUtils.property(locator, "normalId", sourceNormalId), sourceNormalId));
                copy.setNormalId(copyNormalId);
            } else {
                copy.normalId = null;
            }
            if (this.isSetNormal()) {
                String sourceNormal;
                sourceNormal = this.getNormal();
                String copyNormal = ((String) strategy.copy(LocatorUtils.property(locator, "normal", sourceNormal), sourceNormal));
                copy.setNormal(copyNormal);
            } else {
                copy.normal = null;
            }
            if (this.isSetLang()) {
                String sourceLang;
                sourceLang = this.getLang();
                String copyLang = ((String) strategy.copy(LocatorUtils.property(locator, "lang", sourceLang), sourceLang));
                copy.setLang(copyLang);
            } else {
                copy.lang = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new ControlledVoc();
    }

}
