
package org.tellervo.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;element name="permissionToCreate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="permissionToRead" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="permissionToUpdate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="permissionToDelete" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="permissionDenied" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="entity" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="type" use="required" type="{http://www.tellervo.org/schema/1.0}permissionsEntityType" />
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{http://www.tellervo.org/schema/1.0}securityUser"/>
 *           &lt;element ref="{http://www.tellervo.org/schema/1.0}securityGroup"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="decidedBy" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "permissionToCreate",
    "permissionToRead",
    "permissionToUpdate",
    "permissionToDelete",
    "permissionDenied",
    "entities",
    "securityUsersAndSecurityGroups"
})
@XmlRootElement(name = "permission")
public class WSIPermission implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected Boolean permissionToCreate;
    protected Boolean permissionToRead;
    protected Boolean permissionToUpdate;
    protected Boolean permissionToDelete;
    protected Boolean permissionDenied;
    @XmlElement(name = "entity", required = true)
    protected List<WSIPermission.Entity> entities;
    @XmlElements({
        @XmlElement(name = "securityUser", type = WSISecurityUser.class),
        @XmlElement(name = "securityGroup", type = WSISecurityGroup.class)
    })
    protected List<Object> securityUsersAndSecurityGroups;
    @XmlAttribute(name = "decidedBy")
    protected String decidedBy;

    /**
     * Gets the value of the permissionToCreate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPermissionToCreate() {
        return permissionToCreate;
    }

    /**
     * Sets the value of the permissionToCreate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPermissionToCreate(Boolean value) {
        this.permissionToCreate = value;
    }

    public boolean isSetPermissionToCreate() {
        return (this.permissionToCreate!= null);
    }

    /**
     * Gets the value of the permissionToRead property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPermissionToRead() {
        return permissionToRead;
    }

    /**
     * Sets the value of the permissionToRead property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPermissionToRead(Boolean value) {
        this.permissionToRead = value;
    }

    public boolean isSetPermissionToRead() {
        return (this.permissionToRead!= null);
    }

    /**
     * Gets the value of the permissionToUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPermissionToUpdate() {
        return permissionToUpdate;
    }

    /**
     * Sets the value of the permissionToUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPermissionToUpdate(Boolean value) {
        this.permissionToUpdate = value;
    }

    public boolean isSetPermissionToUpdate() {
        return (this.permissionToUpdate!= null);
    }

    /**
     * Gets the value of the permissionToDelete property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPermissionToDelete() {
        return permissionToDelete;
    }

    /**
     * Sets the value of the permissionToDelete property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPermissionToDelete(Boolean value) {
        this.permissionToDelete = value;
    }

    public boolean isSetPermissionToDelete() {
        return (this.permissionToDelete!= null);
    }

    /**
     * Gets the value of the permissionDenied property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPermissionDenied() {
        return permissionDenied;
    }

    /**
     * Sets the value of the permissionDenied property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPermissionDenied(Boolean value) {
        this.permissionDenied = value;
    }

    public boolean isSetPermissionDenied() {
        return (this.permissionDenied!= null);
    }

    /**
     * Gets the value of the entities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIPermission.Entity }
     * 
     * 
     */
    public List<WSIPermission.Entity> getEntities() {
        if (entities == null) {
            entities = new ArrayList<WSIPermission.Entity>();
        }
        return this.entities;
    }

    public boolean isSetEntities() {
        return ((this.entities!= null)&&(!this.entities.isEmpty()));
    }

    public void unsetEntities() {
        this.entities = null;
    }

    /**
     * Gets the value of the securityUsersAndSecurityGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityUsersAndSecurityGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityUsersAndSecurityGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSISecurityUser }
     * {@link WSISecurityGroup }
     * 
     * 
     */
    public List<Object> getSecurityUsersAndSecurityGroups() {
        if (securityUsersAndSecurityGroups == null) {
            securityUsersAndSecurityGroups = new ArrayList<Object>();
        }
        return this.securityUsersAndSecurityGroups;
    }

    public boolean isSetSecurityUsersAndSecurityGroups() {
        return ((this.securityUsersAndSecurityGroups!= null)&&(!this.securityUsersAndSecurityGroups.isEmpty()));
    }

    public void unsetSecurityUsersAndSecurityGroups() {
        this.securityUsersAndSecurityGroups = null;
    }

    /**
     * Gets the value of the decidedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecidedBy() {
        return decidedBy;
    }

    /**
     * Sets the value of the decidedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecidedBy(String value) {
        this.decidedBy = value;
    }

    public boolean isSetDecidedBy() {
        return (this.decidedBy!= null);
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
            Boolean thePermissionToCreate;
            thePermissionToCreate = this.isPermissionToCreate();
            strategy.appendField(locator, this, "permissionToCreate", buffer, thePermissionToCreate);
        }
        {
            Boolean thePermissionToRead;
            thePermissionToRead = this.isPermissionToRead();
            strategy.appendField(locator, this, "permissionToRead", buffer, thePermissionToRead);
        }
        {
            Boolean thePermissionToUpdate;
            thePermissionToUpdate = this.isPermissionToUpdate();
            strategy.appendField(locator, this, "permissionToUpdate", buffer, thePermissionToUpdate);
        }
        {
            Boolean thePermissionToDelete;
            thePermissionToDelete = this.isPermissionToDelete();
            strategy.appendField(locator, this, "permissionToDelete", buffer, thePermissionToDelete);
        }
        {
            Boolean thePermissionDenied;
            thePermissionDenied = this.isPermissionDenied();
            strategy.appendField(locator, this, "permissionDenied", buffer, thePermissionDenied);
        }
        {
            List<WSIPermission.Entity> theEntities;
            theEntities = (this.isSetEntities()?this.getEntities():null);
            strategy.appendField(locator, this, "entities", buffer, theEntities);
        }
        {
            List<Object> theSecurityUsersAndSecurityGroups;
            theSecurityUsersAndSecurityGroups = (this.isSetSecurityUsersAndSecurityGroups()?this.getSecurityUsersAndSecurityGroups():null);
            strategy.appendField(locator, this, "securityUsersAndSecurityGroups", buffer, theSecurityUsersAndSecurityGroups);
        }
        {
            String theDecidedBy;
            theDecidedBy = this.getDecidedBy();
            strategy.appendField(locator, this, "decidedBy", buffer, theDecidedBy);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIPermission)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIPermission that = ((WSIPermission) object);
        {
            Boolean lhsPermissionToCreate;
            lhsPermissionToCreate = this.isPermissionToCreate();
            Boolean rhsPermissionToCreate;
            rhsPermissionToCreate = that.isPermissionToCreate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "permissionToCreate", lhsPermissionToCreate), LocatorUtils.property(thatLocator, "permissionToCreate", rhsPermissionToCreate), lhsPermissionToCreate, rhsPermissionToCreate)) {
                return false;
            }
        }
        {
            Boolean lhsPermissionToRead;
            lhsPermissionToRead = this.isPermissionToRead();
            Boolean rhsPermissionToRead;
            rhsPermissionToRead = that.isPermissionToRead();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "permissionToRead", lhsPermissionToRead), LocatorUtils.property(thatLocator, "permissionToRead", rhsPermissionToRead), lhsPermissionToRead, rhsPermissionToRead)) {
                return false;
            }
        }
        {
            Boolean lhsPermissionToUpdate;
            lhsPermissionToUpdate = this.isPermissionToUpdate();
            Boolean rhsPermissionToUpdate;
            rhsPermissionToUpdate = that.isPermissionToUpdate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "permissionToUpdate", lhsPermissionToUpdate), LocatorUtils.property(thatLocator, "permissionToUpdate", rhsPermissionToUpdate), lhsPermissionToUpdate, rhsPermissionToUpdate)) {
                return false;
            }
        }
        {
            Boolean lhsPermissionToDelete;
            lhsPermissionToDelete = this.isPermissionToDelete();
            Boolean rhsPermissionToDelete;
            rhsPermissionToDelete = that.isPermissionToDelete();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "permissionToDelete", lhsPermissionToDelete), LocatorUtils.property(thatLocator, "permissionToDelete", rhsPermissionToDelete), lhsPermissionToDelete, rhsPermissionToDelete)) {
                return false;
            }
        }
        {
            Boolean lhsPermissionDenied;
            lhsPermissionDenied = this.isPermissionDenied();
            Boolean rhsPermissionDenied;
            rhsPermissionDenied = that.isPermissionDenied();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "permissionDenied", lhsPermissionDenied), LocatorUtils.property(thatLocator, "permissionDenied", rhsPermissionDenied), lhsPermissionDenied, rhsPermissionDenied)) {
                return false;
            }
        }
        {
            List<WSIPermission.Entity> lhsEntities;
            lhsEntities = (this.isSetEntities()?this.getEntities():null);
            List<WSIPermission.Entity> rhsEntities;
            rhsEntities = (that.isSetEntities()?that.getEntities():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "entities", lhsEntities), LocatorUtils.property(thatLocator, "entities", rhsEntities), lhsEntities, rhsEntities)) {
                return false;
            }
        }
        {
            List<Object> lhsSecurityUsersAndSecurityGroups;
            lhsSecurityUsersAndSecurityGroups = (this.isSetSecurityUsersAndSecurityGroups()?this.getSecurityUsersAndSecurityGroups():null);
            List<Object> rhsSecurityUsersAndSecurityGroups;
            rhsSecurityUsersAndSecurityGroups = (that.isSetSecurityUsersAndSecurityGroups()?that.getSecurityUsersAndSecurityGroups():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "securityUsersAndSecurityGroups", lhsSecurityUsersAndSecurityGroups), LocatorUtils.property(thatLocator, "securityUsersAndSecurityGroups", rhsSecurityUsersAndSecurityGroups), lhsSecurityUsersAndSecurityGroups, rhsSecurityUsersAndSecurityGroups)) {
                return false;
            }
        }
        {
            String lhsDecidedBy;
            lhsDecidedBy = this.getDecidedBy();
            String rhsDecidedBy;
            rhsDecidedBy = that.getDecidedBy();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "decidedBy", lhsDecidedBy), LocatorUtils.property(thatLocator, "decidedBy", rhsDecidedBy), lhsDecidedBy, rhsDecidedBy)) {
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
            Boolean thePermissionToCreate;
            thePermissionToCreate = this.isPermissionToCreate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "permissionToCreate", thePermissionToCreate), currentHashCode, thePermissionToCreate);
        }
        {
            Boolean thePermissionToRead;
            thePermissionToRead = this.isPermissionToRead();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "permissionToRead", thePermissionToRead), currentHashCode, thePermissionToRead);
        }
        {
            Boolean thePermissionToUpdate;
            thePermissionToUpdate = this.isPermissionToUpdate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "permissionToUpdate", thePermissionToUpdate), currentHashCode, thePermissionToUpdate);
        }
        {
            Boolean thePermissionToDelete;
            thePermissionToDelete = this.isPermissionToDelete();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "permissionToDelete", thePermissionToDelete), currentHashCode, thePermissionToDelete);
        }
        {
            Boolean thePermissionDenied;
            thePermissionDenied = this.isPermissionDenied();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "permissionDenied", thePermissionDenied), currentHashCode, thePermissionDenied);
        }
        {
            List<WSIPermission.Entity> theEntities;
            theEntities = (this.isSetEntities()?this.getEntities():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "entities", theEntities), currentHashCode, theEntities);
        }
        {
            List<Object> theSecurityUsersAndSecurityGroups;
            theSecurityUsersAndSecurityGroups = (this.isSetSecurityUsersAndSecurityGroups()?this.getSecurityUsersAndSecurityGroups():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "securityUsersAndSecurityGroups", theSecurityUsersAndSecurityGroups), currentHashCode, theSecurityUsersAndSecurityGroups);
        }
        {
            String theDecidedBy;
            theDecidedBy = this.getDecidedBy();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "decidedBy", theDecidedBy), currentHashCode, theDecidedBy);
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
        if (draftCopy instanceof WSIPermission) {
            final WSIPermission copy = ((WSIPermission) draftCopy);
            if (this.isSetPermissionToCreate()) {
                Boolean sourcePermissionToCreate;
                sourcePermissionToCreate = this.isPermissionToCreate();
                Boolean copyPermissionToCreate = ((Boolean) strategy.copy(LocatorUtils.property(locator, "permissionToCreate", sourcePermissionToCreate), sourcePermissionToCreate));
                copy.setPermissionToCreate(copyPermissionToCreate);
            } else {
                copy.permissionToCreate = null;
            }
            if (this.isSetPermissionToRead()) {
                Boolean sourcePermissionToRead;
                sourcePermissionToRead = this.isPermissionToRead();
                Boolean copyPermissionToRead = ((Boolean) strategy.copy(LocatorUtils.property(locator, "permissionToRead", sourcePermissionToRead), sourcePermissionToRead));
                copy.setPermissionToRead(copyPermissionToRead);
            } else {
                copy.permissionToRead = null;
            }
            if (this.isSetPermissionToUpdate()) {
                Boolean sourcePermissionToUpdate;
                sourcePermissionToUpdate = this.isPermissionToUpdate();
                Boolean copyPermissionToUpdate = ((Boolean) strategy.copy(LocatorUtils.property(locator, "permissionToUpdate", sourcePermissionToUpdate), sourcePermissionToUpdate));
                copy.setPermissionToUpdate(copyPermissionToUpdate);
            } else {
                copy.permissionToUpdate = null;
            }
            if (this.isSetPermissionToDelete()) {
                Boolean sourcePermissionToDelete;
                sourcePermissionToDelete = this.isPermissionToDelete();
                Boolean copyPermissionToDelete = ((Boolean) strategy.copy(LocatorUtils.property(locator, "permissionToDelete", sourcePermissionToDelete), sourcePermissionToDelete));
                copy.setPermissionToDelete(copyPermissionToDelete);
            } else {
                copy.permissionToDelete = null;
            }
            if (this.isSetPermissionDenied()) {
                Boolean sourcePermissionDenied;
                sourcePermissionDenied = this.isPermissionDenied();
                Boolean copyPermissionDenied = ((Boolean) strategy.copy(LocatorUtils.property(locator, "permissionDenied", sourcePermissionDenied), sourcePermissionDenied));
                copy.setPermissionDenied(copyPermissionDenied);
            } else {
                copy.permissionDenied = null;
            }
            if (this.isSetEntities()) {
                List<WSIPermission.Entity> sourceEntities;
                sourceEntities = (this.isSetEntities()?this.getEntities():null);
                @SuppressWarnings("unchecked")
                List<WSIPermission.Entity> copyEntities = ((List<WSIPermission.Entity> ) strategy.copy(LocatorUtils.property(locator, "entities", sourceEntities), sourceEntities));
                copy.unsetEntities();
                List<WSIPermission.Entity> uniqueEntitiesl = copy.getEntities();
                uniqueEntitiesl.addAll(copyEntities);
            } else {
                copy.unsetEntities();
            }
            if (this.isSetSecurityUsersAndSecurityGroups()) {
                List<Object> sourceSecurityUsersAndSecurityGroups;
                sourceSecurityUsersAndSecurityGroups = (this.isSetSecurityUsersAndSecurityGroups()?this.getSecurityUsersAndSecurityGroups():null);
                @SuppressWarnings("unchecked")
                List<Object> copySecurityUsersAndSecurityGroups = ((List<Object> ) strategy.copy(LocatorUtils.property(locator, "securityUsersAndSecurityGroups", sourceSecurityUsersAndSecurityGroups), sourceSecurityUsersAndSecurityGroups));
                copy.unsetSecurityUsersAndSecurityGroups();
                List<Object> uniqueSecurityUsersAndSecurityGroupsl = copy.getSecurityUsersAndSecurityGroups();
                uniqueSecurityUsersAndSecurityGroupsl.addAll(copySecurityUsersAndSecurityGroups);
            } else {
                copy.unsetSecurityUsersAndSecurityGroups();
            }
            if (this.isSetDecidedBy()) {
                String sourceDecidedBy;
                sourceDecidedBy = this.getDecidedBy();
                String copyDecidedBy = ((String) strategy.copy(LocatorUtils.property(locator, "decidedBy", sourceDecidedBy), sourceDecidedBy));
                copy.setDecidedBy(copyDecidedBy);
            } else {
                copy.decidedBy = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIPermission();
    }

    /**
     * Sets the value of the entities property.
     * 
     * @param entities
     *     allowed object is
     *     {@link WSIPermission.Entity }
     *     
     */
    public void setEntities(List<WSIPermission.Entity> entities) {
        this.entities = entities;
    }

    /**
     * Sets the value of the securityUsersAndSecurityGroups property.
     * 
     * @param securityUsersAndSecurityGroups
     *     allowed object is
     *     {@link WSISecurityUser }
     *     {@link WSISecurityGroup }
     *     
     */
    public void setSecurityUsersAndSecurityGroups(List<Object> securityUsersAndSecurityGroups) {
        this.securityUsersAndSecurityGroups = securityUsersAndSecurityGroups;
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
     *       &lt;attribute name="type" use="required" type="{http://www.tellervo.org/schema/1.0}permissionsEntityType" />
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Entity
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlAttribute(name = "type", required = true)
        protected PermissionsEntityType type;
        @XmlAttribute(name = "id")
        protected String id;

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link PermissionsEntityType }
         *     
         */
        public PermissionsEntityType getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link PermissionsEntityType }
         *     
         */
        public void setType(PermissionsEntityType value) {
            this.type = value;
        }

        public boolean isSetType() {
            return (this.type!= null);
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
                PermissionsEntityType theType;
                theType = this.getType();
                strategy.appendField(locator, this, "type", buffer, theType);
            }
            {
                String theId;
                theId = this.getId();
                strategy.appendField(locator, this, "id", buffer, theId);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof WSIPermission.Entity)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final WSIPermission.Entity that = ((WSIPermission.Entity) object);
            {
                PermissionsEntityType lhsType;
                lhsType = this.getType();
                PermissionsEntityType rhsType;
                rhsType = that.getType();
                if (!strategy.equals(LocatorUtils.property(thisLocator, "type", lhsType), LocatorUtils.property(thatLocator, "type", rhsType), lhsType, rhsType)) {
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
            return true;
        }

        public boolean equals(Object object) {
            final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
            return equals(null, null, object, strategy);
        }

        public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
            int currentHashCode = 1;
            {
                PermissionsEntityType theType;
                theType = this.getType();
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), currentHashCode, theType);
            }
            {
                String theId;
                theId = this.getId();
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
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
            if (draftCopy instanceof WSIPermission.Entity) {
                final WSIPermission.Entity copy = ((WSIPermission.Entity) draftCopy);
                if (this.isSetType()) {
                    PermissionsEntityType sourceType;
                    sourceType = this.getType();
                    PermissionsEntityType copyType = ((PermissionsEntityType) strategy.copy(LocatorUtils.property(locator, "type", sourceType), sourceType));
                    copy.setType(copyType);
                } else {
                    copy.type = null;
                }
                if (this.isSetId()) {
                    String sourceId;
                    sourceId = this.getId();
                    String copyId = ((String) strategy.copy(LocatorUtils.property(locator, "id", sourceId), sourceId));
                    copy.setId(copyId);
                } else {
                    copy.id = null;
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new WSIPermission.Entity();
        }

    }

}
