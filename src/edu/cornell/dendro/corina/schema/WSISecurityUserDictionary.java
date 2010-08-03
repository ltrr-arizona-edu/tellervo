
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}securityUser" maxOccurs="unbounded" minOccurs="0"/>
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
public class WSISecurityUserDictionary implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSISecurityUserDictionary)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSISecurityUserDictionary that = ((WSISecurityUserDictionary) object);
        equalsBuilder.append(this.getSecurityUsers(), that.getSecurityUsers());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSISecurityUserDictionary)) {
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
        hashCodeBuilder.append(this.getSecurityUsers());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<WSISecurityUser> theSecurityUsers;
            theSecurityUsers = this.getSecurityUsers();
            toStringBuilder.append("securityUsers", theSecurityUsers);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSISecurityUserDictionary copy = ((target == null)?((WSISecurityUserDictionary) createCopy()):((WSISecurityUserDictionary) target));
        if (this.isSetSecurityUsers()) {
            List<WSISecurityUser> sourceSecurityUsers;
            sourceSecurityUsers = this.getSecurityUsers();
            @SuppressWarnings("unchecked")
            List<WSISecurityUser> copySecurityUsers = ((List<WSISecurityUser> ) copyBuilder.copy(sourceSecurityUsers));
            copy.setSecurityUsers(copySecurityUsers);
        } else {
            copy.unsetSecurityUsers();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSISecurityUserDictionary();
    }

}
