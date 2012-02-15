
package org.tellervo.schema;

import java.io.Serializable;
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
 *         &lt;element ref="{http://www.tellervo.org/schema/tellervo/1.0}securityUser" maxOccurs="unbounded" minOccurs="0"/>
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
    "securityUsers"
})
@XmlRootElement(name = "securityUserDictionary")
public class WSISecurityUserDictionary implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "securityUser")
    protected List<WSISecurityUser> securityUsers;

    /**
     * Gets the value of the securityUsers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityUsers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityUsers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSISecurityUser }
     * 
     * 
     */
    public List<WSISecurityUser> getSecurityUsers() {
        if (securityUsers == null) {
            securityUsers = new ArrayList<WSISecurityUser>();
        }
        return this.securityUsers;
    }

    public boolean isSetSecurityUsers() {
        return ((this.securityUsers!= null)&&(!this.securityUsers.isEmpty()));
    }

    public void unsetSecurityUsers() {
        this.securityUsers = null;
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
            List<WSISecurityUser> theSecurityUsers;
            theSecurityUsers = (this.isSetSecurityUsers()?this.getSecurityUsers():null);
            strategy.appendField(locator, this, "securityUsers", buffer, theSecurityUsers);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSISecurityUserDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSISecurityUserDictionary that = ((WSISecurityUserDictionary) object);
        {
            List<WSISecurityUser> lhsSecurityUsers;
            lhsSecurityUsers = (this.isSetSecurityUsers()?this.getSecurityUsers():null);
            List<WSISecurityUser> rhsSecurityUsers;
            rhsSecurityUsers = (that.isSetSecurityUsers()?that.getSecurityUsers():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "securityUsers", lhsSecurityUsers), LocatorUtils.property(thatLocator, "securityUsers", rhsSecurityUsers), lhsSecurityUsers, rhsSecurityUsers)) {
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
            List<WSISecurityUser> theSecurityUsers;
            theSecurityUsers = (this.isSetSecurityUsers()?this.getSecurityUsers():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "securityUsers", theSecurityUsers), currentHashCode, theSecurityUsers);
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
        if (draftCopy instanceof WSISecurityUserDictionary) {
            final WSISecurityUserDictionary copy = ((WSISecurityUserDictionary) draftCopy);
            if (this.isSetSecurityUsers()) {
                List<WSISecurityUser> sourceSecurityUsers;
                sourceSecurityUsers = (this.isSetSecurityUsers()?this.getSecurityUsers():null);
                @SuppressWarnings("unchecked")
                List<WSISecurityUser> copySecurityUsers = ((List<WSISecurityUser> ) strategy.copy(LocatorUtils.property(locator, "securityUsers", sourceSecurityUsers), sourceSecurityUsers));
                copy.unsetSecurityUsers();
                List<WSISecurityUser> uniqueSecurityUsersl = copy.getSecurityUsers();
                uniqueSecurityUsersl.addAll(copySecurityUsers);
            } else {
                copy.unsetSecurityUsers();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSISecurityUserDictionary();
    }

    /**
     * Sets the value of the securityUsers property.
     * 
     * @param securityUsers
     *     allowed object is
     *     {@link WSISecurityUser }
     *     
     */
    public void setSecurityUsers(List<WSISecurityUser> securityUsers) {
        this.securityUsers = securityUsers;
    }

}
