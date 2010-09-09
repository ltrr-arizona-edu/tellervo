
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
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}wmsServer" maxOccurs="unbounded" minOccurs="0"/>
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
    "wmsServers"
})
@XmlRootElement(name = "wmsServerDictionary")
public class WSIWmsServerDictionary implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "wmsServer")
    protected List<WSIWmsServer> wmsServers;

    /**
     * Gets the value of the wmsServers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wmsServers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWmsServers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIWmsServer }
     * 
     * 
     */
    public List<WSIWmsServer> getWmsServers() {
        if (wmsServers == null) {
            wmsServers = new ArrayList<WSIWmsServer>();
        }
        return this.wmsServers;
    }

    public boolean isSetWmsServers() {
        return ((this.wmsServers!= null)&&(!this.wmsServers.isEmpty()));
    }

    public void unsetWmsServers() {
        this.wmsServers = null;
    }

    /**
     * Sets the value of the wmsServers property.
     * 
     * @param wmsServers
     *     allowed object is
     *     {@link WSIWmsServer }
     *     
     */
    public void setWmsServers(List<WSIWmsServer> wmsServers) {
        this.wmsServers = wmsServers;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSIWmsServerDictionary)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSIWmsServerDictionary that = ((WSIWmsServerDictionary) object);
        equalsBuilder.append(this.getWmsServers(), that.getWmsServers());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSIWmsServerDictionary)) {
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
        hashCodeBuilder.append(this.getWmsServers());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<WSIWmsServer> theWmsServers;
            theWmsServers = this.getWmsServers();
            toStringBuilder.append("wmsServers", theWmsServers);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSIWmsServerDictionary copy = ((target == null)?((WSIWmsServerDictionary) createCopy()):((WSIWmsServerDictionary) target));
        if (this.isSetWmsServers()) {
            List<WSIWmsServer> sourceWmsServers;
            sourceWmsServers = this.getWmsServers();
            @SuppressWarnings("unchecked")
            List<WSIWmsServer> copyWmsServers = ((List<WSIWmsServer> ) copyBuilder.copy(sourceWmsServers));
            copy.setWmsServers(copyWmsServers);
        } else {
            copy.unsetWmsServers();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSIWmsServerDictionary();
    }

}
