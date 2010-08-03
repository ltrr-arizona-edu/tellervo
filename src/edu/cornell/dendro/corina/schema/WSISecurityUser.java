
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
 *         &lt;element name="memberOf" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}securityGroup" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="username" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="firstName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isActive" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="hashOfPassword" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "memberOf"
})
@XmlRootElement(name = "securityUser")
public class WSISecurityUser implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected WSISecurityUser.MemberOf memberOf;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String id;
    @XmlAttribute(name = "username")
    protected String username;
    @XmlAttribute(name = "firstName")
    protected String firstName;
    @XmlAttribute(name = "lastName")
    protected String lastName;
    @XmlAttribute(name = "isActive")
    protected Boolean isActive;
    @XmlAttribute(name = "hashOfPassword")
    protected String hashOfPassword;

    /**
     * Gets the value of the memberOf property.
     * 
     * @return
     *     possible object is
     *     {@link WSISecurityUser.MemberOf }
     *     
     */
    public WSISecurityUser.MemberOf getMemberOf() {
        return memberOf;
    }

    /**
     * Sets the value of the memberOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSISecurityUser.MemberOf }
     *     
     */
    public void setMemberOf(WSISecurityUser.MemberOf value) {
        this.memberOf = value;
    }

    public boolean isSetMemberOf() {
        return (this.memberOf!= null);
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id!= null);
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    public boolean isSetUsername() {
        return (this.username!= null);
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    public boolean isSetFirstName() {
        return (this.firstName!= null);
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    public boolean isSetLastName() {
        return (this.lastName!= null);
    }

    /**
     * Gets the value of the isActive property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * Sets the value of the isActive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsActive(boolean value) {
        this.isActive = value;
    }

    public boolean isSetIsActive() {
        return (this.isActive!= null);
    }

    public void unsetIsActive() {
        this.isActive = null;
    }

    /**
     * Gets the value of the hashOfPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashOfPassword() {
        return hashOfPassword;
    }

    /**
     * Sets the value of the hashOfPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashOfPassword(String value) {
        this.hashOfPassword = value;
    }

    public boolean isSetHashOfPassword() {
        return (this.hashOfPassword!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSISecurityUser)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSISecurityUser that = ((WSISecurityUser) object);
        equalsBuilder.append(this.getMemberOf(), that.getMemberOf());
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getUsername(), that.getUsername());
        equalsBuilder.append(this.getFirstName(), that.getFirstName());
        equalsBuilder.append(this.getLastName(), that.getLastName());
        equalsBuilder.append(this.isIsActive(), that.isIsActive());
        equalsBuilder.append(this.getHashOfPassword(), that.getHashOfPassword());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSISecurityUser)) {
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
        hashCodeBuilder.append(this.getMemberOf());
        hashCodeBuilder.append(this.getId());
        hashCodeBuilder.append(this.getUsername());
        hashCodeBuilder.append(this.getFirstName());
        hashCodeBuilder.append(this.getLastName());
        hashCodeBuilder.append(this.isIsActive());
        hashCodeBuilder.append(this.getHashOfPassword());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            WSISecurityUser.MemberOf theMemberOf;
            theMemberOf = this.getMemberOf();
            toStringBuilder.append("memberOf", theMemberOf);
        }
        {
            String theId;
            theId = this.getId();
            toStringBuilder.append("id", theId);
        }
        {
            String theUsername;
            theUsername = this.getUsername();
            toStringBuilder.append("username", theUsername);
        }
        {
            String theFirstName;
            theFirstName = this.getFirstName();
            toStringBuilder.append("firstName", theFirstName);
        }
        {
            String theLastName;
            theLastName = this.getLastName();
            toStringBuilder.append("lastName", theLastName);
        }
        {
            Boolean theIsActive;
            theIsActive = this.isIsActive();
            toStringBuilder.append("isActive", theIsActive);
        }
        {
            String theHashOfPassword;
            theHashOfPassword = this.getHashOfPassword();
            toStringBuilder.append("hashOfPassword", theHashOfPassword);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSISecurityUser copy = ((target == null)?((WSISecurityUser) createCopy()):((WSISecurityUser) target));
        if (this.isSetMemberOf()) {
            WSISecurityUser.MemberOf sourceMemberOf;
            sourceMemberOf = this.getMemberOf();
            WSISecurityUser.MemberOf copyMemberOf = ((WSISecurityUser.MemberOf) copyBuilder.copy(sourceMemberOf));
            copy.setMemberOf(copyMemberOf);
        } else {
            copy.memberOf = null;
        }
        if (this.isSetId()) {
            String sourceId;
            sourceId = this.getId();
            String copyId = ((String) copyBuilder.copy(sourceId));
            copy.setId(copyId);
        } else {
            copy.id = null;
        }
        if (this.isSetUsername()) {
            String sourceUsername;
            sourceUsername = this.getUsername();
            String copyUsername = ((String) copyBuilder.copy(sourceUsername));
            copy.setUsername(copyUsername);
        } else {
            copy.username = null;
        }
        if (this.isSetFirstName()) {
            String sourceFirstName;
            sourceFirstName = this.getFirstName();
            String copyFirstName = ((String) copyBuilder.copy(sourceFirstName));
            copy.setFirstName(copyFirstName);
        } else {
            copy.firstName = null;
        }
        if (this.isSetLastName()) {
            String sourceLastName;
            sourceLastName = this.getLastName();
            String copyLastName = ((String) copyBuilder.copy(sourceLastName));
            copy.setLastName(copyLastName);
        } else {
            copy.lastName = null;
        }
        if (this.isSetIsActive()) {
            Boolean sourceIsActive;
            sourceIsActive = this.isIsActive();
            Boolean copyIsActive = ((Boolean) copyBuilder.copy(sourceIsActive));
            copy.setIsActive(copyIsActive);
        } else {
            copy.unsetIsActive();
        }
        if (this.isSetHashOfPassword()) {
            String sourceHashOfPassword;
            sourceHashOfPassword = this.getHashOfPassword();
            String copyHashOfPassword = ((String) copyBuilder.copy(sourceHashOfPassword));
            copy.setHashOfPassword(copyHashOfPassword);
        } else {
            copy.hashOfPassword = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSISecurityUser();
    }


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
     *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}securityGroup" maxOccurs="unbounded" minOccurs="0"/>
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
    public static class MemberOf
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof WSISecurityUser.MemberOf)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final WSISecurityUser.MemberOf that = ((WSISecurityUser.MemberOf) object);
            equalsBuilder.append(this.getSecurityGroups(), that.getSecurityGroups());
        }

        public boolean equals(Object object) {
            if (!(object instanceof WSISecurityUser.MemberOf)) {
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
            hashCodeBuilder.append(this.getSecurityGroups());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<WSISecurityGroup> theSecurityGroups;
                theSecurityGroups = this.getSecurityGroups();
                toStringBuilder.append("securityGroups", theSecurityGroups);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final WSISecurityUser.MemberOf copy = ((target == null)?((WSISecurityUser.MemberOf) createCopy()):((WSISecurityUser.MemberOf) target));
            if (this.isSetSecurityGroups()) {
                List<WSISecurityGroup> sourceSecurityGroups;
                sourceSecurityGroups = this.getSecurityGroups();
                @SuppressWarnings("unchecked")
                List<WSISecurityGroup> copySecurityGroups = ((List<WSISecurityGroup> ) copyBuilder.copy(sourceSecurityGroups));
                copy.setSecurityGroups(copySecurityGroups);
            } else {
                copy.unsetSecurityGroups();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new WSISecurityUser.MemberOf();
        }

    }

}
