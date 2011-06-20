
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
import org.tridas.interfaces.HumanName;
import org.tridas.interfaces.IdAble;


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
public class WSISecurityUser implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString, HumanName, IdAble
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
            WSISecurityUser.MemberOf theMemberOf;
            theMemberOf = this.getMemberOf();
            strategy.appendField(locator, this, "memberOf", buffer, theMemberOf);
        }
        {
            String theId;
            theId = this.getId();
            strategy.appendField(locator, this, "id", buffer, theId);
        }
        {
            String theUsername;
            theUsername = this.getUsername();
            strategy.appendField(locator, this, "username", buffer, theUsername);
        }
        {
            String theFirstName;
            theFirstName = this.getFirstName();
            strategy.appendField(locator, this, "firstName", buffer, theFirstName);
        }
        {
            String theLastName;
            theLastName = this.getLastName();
            strategy.appendField(locator, this, "lastName", buffer, theLastName);
        }
        {
            boolean theIsActive;
            theIsActive = (this.isSetIsActive()?this.isIsActive():false);
            strategy.appendField(locator, this, "isActive", buffer, theIsActive);
        }
        {
            String theHashOfPassword;
            theHashOfPassword = this.getHashOfPassword();
            strategy.appendField(locator, this, "hashOfPassword", buffer, theHashOfPassword);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSISecurityUser)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSISecurityUser that = ((WSISecurityUser) object);
        {
            WSISecurityUser.MemberOf lhsMemberOf;
            lhsMemberOf = this.getMemberOf();
            WSISecurityUser.MemberOf rhsMemberOf;
            rhsMemberOf = that.getMemberOf();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "memberOf", lhsMemberOf), LocatorUtils.property(thatLocator, "memberOf", rhsMemberOf), lhsMemberOf, rhsMemberOf)) {
                return false;
            }
        }
        {
            String lhsId;
            lhsId = this.getId();
            String rhsId;
            rhsId = that.getId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "id", lhsId), LocatorUtils.property(thatLocator, "id", rhsId), lhsId, rhsId)) {
                return false;
            }
        }
        {
            String lhsUsername;
            lhsUsername = this.getUsername();
            String rhsUsername;
            rhsUsername = that.getUsername();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "username", lhsUsername), LocatorUtils.property(thatLocator, "username", rhsUsername), lhsUsername, rhsUsername)) {
                return false;
            }
        }
        {
            String lhsFirstName;
            lhsFirstName = this.getFirstName();
            String rhsFirstName;
            rhsFirstName = that.getFirstName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "firstName", lhsFirstName), LocatorUtils.property(thatLocator, "firstName", rhsFirstName), lhsFirstName, rhsFirstName)) {
                return false;
            }
        }
        {
            String lhsLastName;
            lhsLastName = this.getLastName();
            String rhsLastName;
            rhsLastName = that.getLastName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "lastName", lhsLastName), LocatorUtils.property(thatLocator, "lastName", rhsLastName), lhsLastName, rhsLastName)) {
                return false;
            }
        }
        {
            boolean lhsIsActive;
            lhsIsActive = (this.isSetIsActive()?this.isIsActive():false);
            boolean rhsIsActive;
            rhsIsActive = (that.isSetIsActive()?that.isIsActive():false);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "isActive", lhsIsActive), LocatorUtils.property(thatLocator, "isActive", rhsIsActive), lhsIsActive, rhsIsActive)) {
                return false;
            }
        }
        {
            String lhsHashOfPassword;
            lhsHashOfPassword = this.getHashOfPassword();
            String rhsHashOfPassword;
            rhsHashOfPassword = that.getHashOfPassword();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "hashOfPassword", lhsHashOfPassword), LocatorUtils.property(thatLocator, "hashOfPassword", rhsHashOfPassword), lhsHashOfPassword, rhsHashOfPassword)) {
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
            WSISecurityUser.MemberOf theMemberOf;
            theMemberOf = this.getMemberOf();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "memberOf", theMemberOf), currentHashCode, theMemberOf);
        }
        {
            String theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        {
            String theUsername;
            theUsername = this.getUsername();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "username", theUsername), currentHashCode, theUsername);
        }
        {
            String theFirstName;
            theFirstName = this.getFirstName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "firstName", theFirstName), currentHashCode, theFirstName);
        }
        {
            String theLastName;
            theLastName = this.getLastName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "lastName", theLastName), currentHashCode, theLastName);
        }
        {
            boolean theIsActive;
            theIsActive = (this.isSetIsActive()?this.isIsActive():false);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "isActive", theIsActive), currentHashCode, theIsActive);
        }
        {
            String theHashOfPassword;
            theHashOfPassword = this.getHashOfPassword();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "hashOfPassword", theHashOfPassword), currentHashCode, theHashOfPassword);
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
        if (draftCopy instanceof WSISecurityUser) {
            final WSISecurityUser copy = ((WSISecurityUser) draftCopy);
            if (this.isSetMemberOf()) {
                WSISecurityUser.MemberOf sourceMemberOf;
                sourceMemberOf = this.getMemberOf();
                WSISecurityUser.MemberOf copyMemberOf = ((WSISecurityUser.MemberOf) strategy.copy(LocatorUtils.property(locator, "memberOf", sourceMemberOf), sourceMemberOf));
                copy.setMemberOf(copyMemberOf);
            } else {
                copy.memberOf = null;
            }
            if (this.isSetId()) {
                String sourceId;
                sourceId = this.getId();
                String copyId = ((String) strategy.copy(LocatorUtils.property(locator, "id", sourceId), sourceId));
                copy.setId(copyId);
            } else {
                copy.id = null;
            }
            if (this.isSetUsername()) {
                String sourceUsername;
                sourceUsername = this.getUsername();
                String copyUsername = ((String) strategy.copy(LocatorUtils.property(locator, "username", sourceUsername), sourceUsername));
                copy.setUsername(copyUsername);
            } else {
                copy.username = null;
            }
            if (this.isSetFirstName()) {
                String sourceFirstName;
                sourceFirstName = this.getFirstName();
                String copyFirstName = ((String) strategy.copy(LocatorUtils.property(locator, "firstName", sourceFirstName), sourceFirstName));
                copy.setFirstName(copyFirstName);
            } else {
                copy.firstName = null;
            }
            if (this.isSetLastName()) {
                String sourceLastName;
                sourceLastName = this.getLastName();
                String copyLastName = ((String) strategy.copy(LocatorUtils.property(locator, "lastName", sourceLastName), sourceLastName));
                copy.setLastName(copyLastName);
            } else {
                copy.lastName = null;
            }
            if (this.isSetIsActive()) {
                boolean sourceIsActive;
                sourceIsActive = (this.isSetIsActive()?this.isIsActive():false);
                boolean copyIsActive = strategy.copy(LocatorUtils.property(locator, "isActive", sourceIsActive), sourceIsActive);
                copy.setIsActive(copyIsActive);
            } else {
                copy.unsetIsActive();
            }
            if (this.isSetHashOfPassword()) {
                String sourceHashOfPassword;
                sourceHashOfPassword = this.getHashOfPassword();
                String copyHashOfPassword = ((String) strategy.copy(LocatorUtils.property(locator, "hashOfPassword", sourceHashOfPassword), sourceHashOfPassword));
                copy.setHashOfPassword(copyHashOfPassword);
            } else {
                copy.hashOfPassword = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
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
        implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
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
            if (!(object instanceof WSISecurityUser.MemberOf)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final WSISecurityUser.MemberOf that = ((WSISecurityUser.MemberOf) object);
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
            if (draftCopy instanceof WSISecurityUser.MemberOf) {
                final WSISecurityUser.MemberOf copy = ((WSISecurityUser.MemberOf) draftCopy);
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
            return new WSISecurityUser.MemberOf();
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

}
