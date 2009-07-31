
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSIAuthenticate)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSIAuthenticate that = ((WSIAuthenticate) object);
        equalsBuilder.append(this.getUsername(), that.getUsername());
        equalsBuilder.append(this.getPassword(), that.getPassword());
        equalsBuilder.append(this.getCnonce(), that.getCnonce());
        equalsBuilder.append(this.getSnonce(), that.getSnonce());
        equalsBuilder.append(this.getHash(), that.getHash());
        equalsBuilder.append(this.getSeq(), that.getSeq());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSIAuthenticate)) {
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
        hashCodeBuilder.append(this.getUsername());
        hashCodeBuilder.append(this.getPassword());
        hashCodeBuilder.append(this.getCnonce());
        hashCodeBuilder.append(this.getSnonce());
        hashCodeBuilder.append(this.getHash());
        hashCodeBuilder.append(this.getSeq());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theUsername;
            theUsername = this.getUsername();
            toStringBuilder.append("username", theUsername);
        }
        {
            String thePassword;
            thePassword = this.getPassword();
            toStringBuilder.append("password", thePassword);
        }
        {
            String theCnonce;
            theCnonce = this.getCnonce();
            toStringBuilder.append("cnonce", theCnonce);
        }
        {
            String theSnonce;
            theSnonce = this.getSnonce();
            toStringBuilder.append("snonce", theSnonce);
        }
        {
            String theHash;
            theHash = this.getHash();
            toStringBuilder.append("hash", theHash);
        }
        {
            String theSeq;
            theSeq = this.getSeq();
            toStringBuilder.append("seq", theSeq);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSIAuthenticate copy = ((target == null)?((WSIAuthenticate) createCopy()):((WSIAuthenticate) target));
        if (this.isSetUsername()) {
            String sourceUsername;
            sourceUsername = this.getUsername();
            String copyUsername = ((String) copyBuilder.copy(sourceUsername));
            copy.setUsername(copyUsername);
        } else {
            copy.username = null;
        }
        if (this.isSetPassword()) {
            String sourcePassword;
            sourcePassword = this.getPassword();
            String copyPassword = ((String) copyBuilder.copy(sourcePassword));
            copy.setPassword(copyPassword);
        } else {
            copy.password = null;
        }
        if (this.isSetCnonce()) {
            String sourceCnonce;
            sourceCnonce = this.getCnonce();
            String copyCnonce = ((String) copyBuilder.copy(sourceCnonce));
            copy.setCnonce(copyCnonce);
        } else {
            copy.cnonce = null;
        }
        if (this.isSetSnonce()) {
            String sourceSnonce;
            sourceSnonce = this.getSnonce();
            String copySnonce = ((String) copyBuilder.copy(sourceSnonce));
            copy.setSnonce(copySnonce);
        } else {
            copy.snonce = null;
        }
        if (this.isSetHash()) {
            String sourceHash;
            sourceHash = this.getHash();
            String copyHash = ((String) copyBuilder.copy(sourceHash));
            copy.setHash(copyHash);
        } else {
            copy.hash = null;
        }
        if (this.isSetSeq()) {
            String sourceSeq;
            sourceSeq = this.getSeq();
            String copySeq = ((String) copyBuilder.copy(sourceSeq));
            copy.setSeq(copySeq);
        } else {
            copy.seq = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSIAuthenticate();
    }

}
