
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
import org.tridas.interfaces.HumanName;
import org.tridas.interfaces.IdAble;


/**
 * <p>Java class for securityUser complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="securityUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="memberOf">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="securityGroup" type="{http://dendro.cornell.edu/schema/corina/1.0}securityGroup" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="username" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="firstName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isActive" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "securityUser", propOrder = {
    "memberOf"
})
public class SecurityUser
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString, HumanName, IdAble
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected SecurityUser.MemberOf memberOf;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String id;
    @XmlAttribute(name = "username", required = true)
    protected String username;
    @XmlAttribute(name = "firstName", required = true)
    protected String firstName;
    @XmlAttribute(name = "lastName", required = true)
    protected String lastName;
    @XmlAttribute(name = "isActive", required = true)
    protected boolean isActive;

    /**
     * Gets the value of the memberOf property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityUser.MemberOf }
     *     
     */
    public SecurityUser.MemberOf getMemberOf() {
        return memberOf;
    }

    /**
     * Sets the value of the memberOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityUser.MemberOf }
     *     
     */
    public void setMemberOf(SecurityUser.MemberOf value) {
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
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * Sets the value of the isActive property.
     * 
     */
    public void setIsActive(boolean value) {
        this.isActive = value;
    }

    public boolean isSetIsActive() {
        return true;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SecurityUser)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final SecurityUser that = ((SecurityUser) object);
        equalsBuilder.append(this.getMemberOf(), that.getMemberOf());
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getUsername(), that.getUsername());
        equalsBuilder.append(this.getFirstName(), that.getFirstName());
        equalsBuilder.append(this.getLastName(), that.getLastName());
        equalsBuilder.append(this.isIsActive(), that.isIsActive());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SecurityUser)) {
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
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            SecurityUser.MemberOf theMemberOf;
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
            boolean theIsActive;
            theIsActive = this.isIsActive();
            toStringBuilder.append("isActive", theIsActive);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final SecurityUser copy = ((target == null)?((SecurityUser) createCopy()):((SecurityUser) target));
        if (this.isSetMemberOf()) {
            SecurityUser.MemberOf sourceMemberOf;
            sourceMemberOf = this.getMemberOf();
            SecurityUser.MemberOf copyMemberOf = ((SecurityUser.MemberOf) copyBuilder.copy(sourceMemberOf));
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
            boolean sourceIsActive;
            sourceIsActive = this.isIsActive();
            boolean copyIsActive = copyBuilder.copy(sourceIsActive);
            copy.setIsActive(copyIsActive);
        } else {
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new SecurityUser();
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
     *         &lt;element name="securityGroup" type="{http://dendro.cornell.edu/schema/corina/1.0}securityGroup" maxOccurs="unbounded" minOccurs="0"/>
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
        protected List<SecurityGroup> securityGroups;

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
         * {@link SecurityGroup }
         * 
         * 
         */
        public List<SecurityGroup> getSecurityGroups() {
            if (securityGroups == null) {
                securityGroups = new ArrayList<SecurityGroup>();
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
         *     {@link SecurityGroup }
         *     
         */
        public void setSecurityGroups(List<SecurityGroup> securityGroups) {
            this.securityGroups = securityGroups;
        }

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof SecurityUser.MemberOf)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final SecurityUser.MemberOf that = ((SecurityUser.MemberOf) object);
            equalsBuilder.append(this.getSecurityGroups(), that.getSecurityGroups());
        }

        public boolean equals(Object object) {
            if (!(object instanceof SecurityUser.MemberOf)) {
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
                List<SecurityGroup> theSecurityGroups;
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
            final SecurityUser.MemberOf copy = ((target == null)?((SecurityUser.MemberOf) createCopy()):((SecurityUser.MemberOf) target));
            if (this.isSetSecurityGroups()) {
                List<SecurityGroup> sourceSecurityGroups;
                sourceSecurityGroups = this.getSecurityGroups();
                @SuppressWarnings("unchecked")
                List<SecurityGroup> copySecurityGroups = ((List<SecurityGroup> ) copyBuilder.copy(sourceSecurityGroups));
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
            return new SecurityUser.MemberOf();
        }

    }

}
