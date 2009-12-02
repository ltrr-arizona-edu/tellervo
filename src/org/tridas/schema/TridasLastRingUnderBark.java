
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
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
 *       &lt;attribute name="presence" use="required" type="{http://www.tridas.org/1.3}presenceAbsence" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "lastRingUnderBark")
public class TridasLastRingUnderBark
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlValue
    protected String content;
    @XmlAttribute(name = "presence", required = true)
    protected PresenceAbsence presence;

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    public boolean isSetContent() {
        return (this.content!= null);
    }

    /**
     * Gets the value of the presence property.
     * 
     * @return
     *     possible object is
     *     {@link PresenceAbsence }
     *     
     */
    public PresenceAbsence getPresence() {
        return presence;
    }

    /**
     * Sets the value of the presence property.
     * 
     * @param value
     *     allowed object is
     *     {@link PresenceAbsence }
     *     
     */
    public void setPresence(PresenceAbsence value) {
        this.presence = value;
    }

    public boolean isSetPresence() {
        return (this.presence!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasLastRingUnderBark)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasLastRingUnderBark that = ((TridasLastRingUnderBark) object);
        equalsBuilder.append(this.getContent(), that.getContent());
        equalsBuilder.append(this.getPresence(), that.getPresence());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasLastRingUnderBark)) {
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
        hashCodeBuilder.append(this.getContent());
        hashCodeBuilder.append(this.getPresence());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theContent;
            theContent = this.getContent();
            toStringBuilder.append("content", theContent);
        }
        {
            PresenceAbsence thePresence;
            thePresence = this.getPresence();
            toStringBuilder.append("presence", thePresence);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasLastRingUnderBark copy = ((target == null)?((TridasLastRingUnderBark) createCopy()):((TridasLastRingUnderBark) target));
        if (this.isSetContent()) {
            String sourceContent;
            sourceContent = this.getContent();
            String copyContent = ((String) copyBuilder.copy(sourceContent));
            copy.setContent(copyContent);
        } else {
            copy.content = null;
        }
        if (this.isSetPresence()) {
            PresenceAbsence sourcePresence;
            sourcePresence = this.getPresence();
            PresenceAbsence copyPresence = ((PresenceAbsence) copyBuilder.copy(sourcePresence));
            copy.setPresence(copyPresence);
        } else {
            copy.presence = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasLastRingUnderBark();
    }

}
