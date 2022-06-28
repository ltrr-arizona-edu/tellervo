
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
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}securityGroup" maxOccurs="unbounded" minOccurs="0"/>
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
    "securityGroups"
})
@XmlRootElement(name = "securityGroupDictionary")
public class WSISecurityGroupDictionary implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "securityGroup")
    protected List<WSISecurityGroup> securityGroups;

    /**
     * Gets the value of the securityGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSISecurityGroup }
     * 
     * 
     */
    public List<WSISecurityGroup> getSecurityGroups() {
        if (securityGroups == null) {
            securityGroups = new ArrayList<WSISecurityGroup>();
        }
        return this.securityGroups;
    }

    public boolean isSetSecurityGroups() {
        return ((this.securityGroups!= null)&&(!this.securityGroups.isEmpty()));
    }

    public void unsetSecurityGroups() {
        this.securityGroups = null;
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
            List<WSISecurityGroup> theSecurityGroups;
            theSecurityGroups = (this.isSetSecurityGroups()?this.getSecurityGroups():null);
            strategy.appendField(locator, this, "securityGroups", buffer, theSecurityGroups);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSISecurityGroupDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSISecurityGroupDictionary that = ((WSISecurityGroupDictionary) object);
        {
            List<WSISecurityGroup> lhsSecurityGroups;
            lhsSecurityGroups = (this.isSetSecurityGroups()?this.getSecurityGroups():null);
            List<WSISecurityGroup> rhsSecurityGroups;
            rhsSecurityGroups = (that.isSetSecurityGroups()?that.getSecurityGroups():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "securityGroups", lhsSecurityGroups), LocatorUtils.property(thatLocator, "securityGroups", rhsSecurityGroups), lhsSecurityGroups, rhsSecurityGroups)) {
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
            List<WSISecurityGroup> theSecurityGroups;
            theSecurityGroups = (this.isSetSecurityGroups()?this.getSecurityGroups():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "securityGroups", theSecurityGroups), currentHashCode, theSecurityGroups);
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
        if (draftCopy instanceof WSISecurityGroupDictionary) {
            final WSISecurityGroupDictionary copy = ((WSISecurityGroupDictionary) draftCopy);
            if (this.isSetSecurityGroups()) {
                List<WSISecurityGroup> sourceSecurityGroups;
                sourceSecurityGroups = (this.isSetSecurityGroups()?this.getSecurityGroups():null);
                @SuppressWarnings("unchecked")
                List<WSISecurityGroup> copySecurityGroups = ((List<WSISecurityGroup> ) strategy.copy(LocatorUtils.property(locator, "securityGroups", sourceSecurityGroups), sourceSecurityGroups));
                copy.unsetSecurityGroups();
                List<WSISecurityGroup> uniqueSecurityGroupsl = copy.getSecurityGroups();
                uniqueSecurityGroupsl.addAll(copySecurityGroups);
            } else {
                copy.unsetSecurityGroups();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSISecurityGroupDictionary();
    }

    /**
     * Sets the value of the securityGroups property.
     * 
     * @param securityGroups
     *     allowed object is
     *     {@link WSISecurityGroup }
     *     
     */
    public void setSecurityGroups(List<WSISecurityGroup> securityGroups) {
        this.securityGroups = securityGroups;
    }

}
