
package org.tellervo.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isActive" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="userMembers" type="{http://dendro.cornell.edu/schema/tellervo/1.0}idList" />
 *       &lt;attribute name="groupMembers" type="{http://dendro.cornell.edu/schema/tellervo/1.0}idList" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "securityGroup")
public class WSISecurityGroup implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "isActive")
    protected Boolean isActive;
    @XmlAttribute(name = "userMembers")
    protected List<String> userMembers;
    @XmlAttribute(name = "groupMembers")
    protected List<String> groupMembers;

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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name!= null);
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    public boolean isSetDescription() {
        return (this.description!= null);
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
     * Gets the value of the userMembers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userMembers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserMembers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUserMembers() {
        if (userMembers == null) {
            userMembers = new ArrayList<String>();
        }
        return this.userMembers;
    }

    public boolean isSetUserMembers() {
        return ((this.userMembers!= null)&&(!this.userMembers.isEmpty()));
    }

    public void unsetUserMembers() {
        this.userMembers = null;
    }

    /**
     * Gets the value of the groupMembers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groupMembers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroupMembers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getGroupMembers() {
        if (groupMembers == null) {
            groupMembers = new ArrayList<String>();
        }
        return this.groupMembers;
    }

    public boolean isSetGroupMembers() {
        return ((this.groupMembers!= null)&&(!this.groupMembers.isEmpty()));
    }

    public void unsetGroupMembers() {
        this.groupMembers = null;
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
            String theId;
            theId = this.getId();
            strategy.appendField(locator, this, "id", buffer, theId);
        }
        {
            String theName;
            theName = this.getName();
            strategy.appendField(locator, this, "name", buffer, theName);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            strategy.appendField(locator, this, "description", buffer, theDescription);
        }
        {
            boolean theIsActive;
            theIsActive = (this.isSetIsActive()?this.isIsActive():false);
            strategy.appendField(locator, this, "isActive", buffer, theIsActive);
        }
        {
            List<String> theUserMembers;
            theUserMembers = (this.isSetUserMembers()?this.getUserMembers():null);
            strategy.appendField(locator, this, "userMembers", buffer, theUserMembers);
        }
        {
            List<String> theGroupMembers;
            theGroupMembers = (this.isSetGroupMembers()?this.getGroupMembers():null);
            strategy.appendField(locator, this, "groupMembers", buffer, theGroupMembers);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSISecurityGroup)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSISecurityGroup that = ((WSISecurityGroup) object);
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
            String lhsName;
            lhsName = this.getName();
            String rhsName;
            rhsName = that.getName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "name", lhsName), LocatorUtils.property(thatLocator, "name", rhsName), lhsName, rhsName)) {
                return false;
            }
        }
        {
            String lhsDescription;
            lhsDescription = this.getDescription();
            String rhsDescription;
            rhsDescription = that.getDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "description", lhsDescription), LocatorUtils.property(thatLocator, "description", rhsDescription), lhsDescription, rhsDescription)) {
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
            List<String> lhsUserMembers;
            lhsUserMembers = (this.isSetUserMembers()?this.getUserMembers():null);
            List<String> rhsUserMembers;
            rhsUserMembers = (that.isSetUserMembers()?that.getUserMembers():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "userMembers", lhsUserMembers), LocatorUtils.property(thatLocator, "userMembers", rhsUserMembers), lhsUserMembers, rhsUserMembers)) {
                return false;
            }
        }
        {
            List<String> lhsGroupMembers;
            lhsGroupMembers = (this.isSetGroupMembers()?this.getGroupMembers():null);
            List<String> rhsGroupMembers;
            rhsGroupMembers = (that.isSetGroupMembers()?that.getGroupMembers():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "groupMembers", lhsGroupMembers), LocatorUtils.property(thatLocator, "groupMembers", rhsGroupMembers), lhsGroupMembers, rhsGroupMembers)) {
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
            String theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        {
            String theName;
            theName = this.getName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "name", theName), currentHashCode, theName);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
        }
        {
            boolean theIsActive;
            theIsActive = (this.isSetIsActive()?this.isIsActive():false);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "isActive", theIsActive), currentHashCode, theIsActive);
        }
        {
            List<String> theUserMembers;
            theUserMembers = (this.isSetUserMembers()?this.getUserMembers():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "userMembers", theUserMembers), currentHashCode, theUserMembers);
        }
        {
            List<String> theGroupMembers;
            theGroupMembers = (this.isSetGroupMembers()?this.getGroupMembers():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "groupMembers", theGroupMembers), currentHashCode, theGroupMembers);
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
        if (draftCopy instanceof WSISecurityGroup) {
            final WSISecurityGroup copy = ((WSISecurityGroup) draftCopy);
            if (this.isSetId()) {
                String sourceId;
                sourceId = this.getId();
                String copyId = ((String) strategy.copy(LocatorUtils.property(locator, "id", sourceId), sourceId));
                copy.setId(copyId);
            } else {
                copy.id = null;
            }
            if (this.isSetName()) {
                String sourceName;
                sourceName = this.getName();
                String copyName = ((String) strategy.copy(LocatorUtils.property(locator, "name", sourceName), sourceName));
                copy.setName(copyName);
            } else {
                copy.name = null;
            }
            if (this.isSetDescription()) {
                String sourceDescription;
                sourceDescription = this.getDescription();
                String copyDescription = ((String) strategy.copy(LocatorUtils.property(locator, "description", sourceDescription), sourceDescription));
                copy.setDescription(copyDescription);
            } else {
                copy.description = null;
            }
            if (this.isSetIsActive()) {
                boolean sourceIsActive;
                sourceIsActive = (this.isSetIsActive()?this.isIsActive():false);
                boolean copyIsActive = strategy.copy(LocatorUtils.property(locator, "isActive", sourceIsActive), sourceIsActive);
                copy.setIsActive(copyIsActive);
            } else {
                copy.unsetIsActive();
            }
            if (this.isSetUserMembers()) {
                List<String> sourceUserMembers;
                sourceUserMembers = (this.isSetUserMembers()?this.getUserMembers():null);
                @SuppressWarnings("unchecked")
                List<String> copyUserMembers = ((List<String> ) strategy.copy(LocatorUtils.property(locator, "userMembers", sourceUserMembers), sourceUserMembers));
                copy.unsetUserMembers();
                List<String> uniqueUserMembersl = copy.getUserMembers();
                uniqueUserMembersl.addAll(copyUserMembers);
            } else {
                copy.unsetUserMembers();
            }
            if (this.isSetGroupMembers()) {
                List<String> sourceGroupMembers;
                sourceGroupMembers = (this.isSetGroupMembers()?this.getGroupMembers():null);
                @SuppressWarnings("unchecked")
                List<String> copyGroupMembers = ((List<String> ) strategy.copy(LocatorUtils.property(locator, "groupMembers", sourceGroupMembers), sourceGroupMembers));
                copy.unsetGroupMembers();
                List<String> uniqueGroupMembersl = copy.getGroupMembers();
                uniqueGroupMembersl.addAll(copyGroupMembers);
            } else {
                copy.unsetGroupMembers();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSISecurityGroup();
    }

    /**
     * Sets the value of the userMembers property.
     * 
     * @param userMembers
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserMembers(List<String> userMembers) {
        this.userMembers = userMembers;
    }

    /**
     * Sets the value of the groupMembers property.
     * 
     * @param groupMembers
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

}
