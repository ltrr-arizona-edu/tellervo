
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
public class WSIWmsServerDictionary implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
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
            List<WSIWmsServer> theWmsServers;
            theWmsServers = (this.isSetWmsServers()?this.getWmsServers():null);
            strategy.appendField(locator, this, "wmsServers", buffer, theWmsServers);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIWmsServerDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIWmsServerDictionary that = ((WSIWmsServerDictionary) object);
        {
            List<WSIWmsServer> lhsWmsServers;
            lhsWmsServers = (this.isSetWmsServers()?this.getWmsServers():null);
            List<WSIWmsServer> rhsWmsServers;
            rhsWmsServers = (that.isSetWmsServers()?that.getWmsServers():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "wmsServers", lhsWmsServers), LocatorUtils.property(thatLocator, "wmsServers", rhsWmsServers), lhsWmsServers, rhsWmsServers)) {
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
            List<WSIWmsServer> theWmsServers;
            theWmsServers = (this.isSetWmsServers()?this.getWmsServers():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "wmsServers", theWmsServers), currentHashCode, theWmsServers);
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
        if (draftCopy instanceof WSIWmsServerDictionary) {
            final WSIWmsServerDictionary copy = ((WSIWmsServerDictionary) draftCopy);
            if (this.isSetWmsServers()) {
                List<WSIWmsServer> sourceWmsServers;
                sourceWmsServers = (this.isSetWmsServers()?this.getWmsServers():null);
                @SuppressWarnings("unchecked")
                List<WSIWmsServer> copyWmsServers = ((List<WSIWmsServer> ) strategy.copy(LocatorUtils.property(locator, "wmsServers", sourceWmsServers), sourceWmsServers));
                copy.unsetWmsServers();
                List<WSIWmsServer> uniqueWmsServersl = copy.getWmsServers();
                uniqueWmsServersl.addAll(copyWmsServers);
            } else {
                copy.unsetWmsServers();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIWmsServerDictionary();
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

}
