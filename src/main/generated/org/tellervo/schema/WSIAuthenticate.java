
package org.tellervo.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="username" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cnonce" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="snonce" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="hash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="seq" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "authenticate")
public class WSIAuthenticate
    implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlAttribute(name = "username")
    protected String username;
    @XmlAttribute(name = "password")
    protected String password;
    @XmlAttribute(name = "cnonce")
    protected String cnonce;
    @XmlAttribute(name = "snonce")
    protected String snonce;
    @XmlAttribute(name = "hash")
    protected String hash;
    @XmlAttribute(name = "seq")
    protected String seq;

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
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    public boolean isSetPassword() {
        return (this.password!= null);
    }

    /**
     * Gets the value of the cnonce property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCnonce() {
        return cnonce;
    }

    /**
     * Sets the value of the cnonce property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCnonce(String value) {
        this.cnonce = value;
    }

    public boolean isSetCnonce() {
        return (this.cnonce!= null);
    }

    /**
     * Gets the value of the snonce property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSnonce() {
        return snonce;
    }

    /**
     * Sets the value of the snonce property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSnonce(String value) {
        this.snonce = value;
    }

    public boolean isSetSnonce() {
        return (this.snonce!= null);
    }

    /**
     * Gets the value of the hash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
    }

    public boolean isSetHash() {
        return (this.hash!= null);
    }

    /**
     * Gets the value of the seq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeq() {
        return seq;
    }

    /**
     * Sets the value of the seq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeq(String value) {
        this.seq = value;
    }

    public boolean isSetSeq() {
        return (this.seq!= null);
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
            String theUsername;
            theUsername = this.getUsername();
            strategy.appendField(locator, this, "username", buffer, theUsername);
        }
        {
            String thePassword;
            thePassword = this.getPassword();
            strategy.appendField(locator, this, "password", buffer, thePassword);
        }
        {
            String theCnonce;
            theCnonce = this.getCnonce();
            strategy.appendField(locator, this, "cnonce", buffer, theCnonce);
        }
        {
            String theSnonce;
            theSnonce = this.getSnonce();
            strategy.appendField(locator, this, "snonce", buffer, theSnonce);
        }
        {
            String theHash;
            theHash = this.getHash();
            strategy.appendField(locator, this, "hash", buffer, theHash);
        }
        {
            String theSeq;
            theSeq = this.getSeq();
            strategy.appendField(locator, this, "seq", buffer, theSeq);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIAuthenticate)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIAuthenticate that = ((WSIAuthenticate) object);
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
            String lhsPassword;
            lhsPassword = this.getPassword();
            String rhsPassword;
            rhsPassword = that.getPassword();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "password", lhsPassword), LocatorUtils.property(thatLocator, "password", rhsPassword), lhsPassword, rhsPassword)) {
                return false;
            }
        }
        {
            String lhsCnonce;
            lhsCnonce = this.getCnonce();
            String rhsCnonce;
            rhsCnonce = that.getCnonce();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "cnonce", lhsCnonce), LocatorUtils.property(thatLocator, "cnonce", rhsCnonce), lhsCnonce, rhsCnonce)) {
                return false;
            }
        }
        {
            String lhsSnonce;
            lhsSnonce = this.getSnonce();
            String rhsSnonce;
            rhsSnonce = that.getSnonce();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "snonce", lhsSnonce), LocatorUtils.property(thatLocator, "snonce", rhsSnonce), lhsSnonce, rhsSnonce)) {
                return false;
            }
        }
        {
            String lhsHash;
            lhsHash = this.getHash();
            String rhsHash;
            rhsHash = that.getHash();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "hash", lhsHash), LocatorUtils.property(thatLocator, "hash", rhsHash), lhsHash, rhsHash)) {
                return false;
            }
        }
        {
            String lhsSeq;
            lhsSeq = this.getSeq();
            String rhsSeq;
            rhsSeq = that.getSeq();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "seq", lhsSeq), LocatorUtils.property(thatLocator, "seq", rhsSeq), lhsSeq, rhsSeq)) {
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
            String theUsername;
            theUsername = this.getUsername();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "username", theUsername), currentHashCode, theUsername);
        }
        {
            String thePassword;
            thePassword = this.getPassword();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "password", thePassword), currentHashCode, thePassword);
        }
        {
            String theCnonce;
            theCnonce = this.getCnonce();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "cnonce", theCnonce), currentHashCode, theCnonce);
        }
        {
            String theSnonce;
            theSnonce = this.getSnonce();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "snonce", theSnonce), currentHashCode, theSnonce);
        }
        {
            String theHash;
            theHash = this.getHash();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "hash", theHash), currentHashCode, theHash);
        }
        {
            String theSeq;
            theSeq = this.getSeq();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "seq", theSeq), currentHashCode, theSeq);
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
        if (draftCopy instanceof WSIAuthenticate) {
            final WSIAuthenticate copy = ((WSIAuthenticate) draftCopy);
            if (this.isSetUsername()) {
                String sourceUsername;
                sourceUsername = this.getUsername();
                String copyUsername = ((String) strategy.copy(LocatorUtils.property(locator, "username", sourceUsername), sourceUsername));
                copy.setUsername(copyUsername);
            } else {
                copy.username = null;
            }
            if (this.isSetPassword()) {
                String sourcePassword;
                sourcePassword = this.getPassword();
                String copyPassword = ((String) strategy.copy(LocatorUtils.property(locator, "password", sourcePassword), sourcePassword));
                copy.setPassword(copyPassword);
            } else {
                copy.password = null;
            }
            if (this.isSetCnonce()) {
                String sourceCnonce;
                sourceCnonce = this.getCnonce();
                String copyCnonce = ((String) strategy.copy(LocatorUtils.property(locator, "cnonce", sourceCnonce), sourceCnonce));
                copy.setCnonce(copyCnonce);
            } else {
                copy.cnonce = null;
            }
            if (this.isSetSnonce()) {
                String sourceSnonce;
                sourceSnonce = this.getSnonce();
                String copySnonce = ((String) strategy.copy(LocatorUtils.property(locator, "snonce", sourceSnonce), sourceSnonce));
                copy.setSnonce(copySnonce);
            } else {
                copy.snonce = null;
            }
            if (this.isSetHash()) {
                String sourceHash;
                sourceHash = this.getHash();
                String copyHash = ((String) strategy.copy(LocatorUtils.property(locator, "hash", sourceHash), sourceHash));
                copy.setHash(copyHash);
            } else {
                copy.hash = null;
            }
            if (this.isSetSeq()) {
                String sourceSeq;
                sourceSeq = this.getSeq();
                String copySeq = ((String) strategy.copy(LocatorUtils.property(locator, "seq", sourceSeq), sourceSeq));
                copy.setSeq(copySeq);
            } else {
                copy.seq = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIAuthenticate();
    }

}
