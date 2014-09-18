
package org.tellervo.schema;

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
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}domain" maxOccurs="unbounded" minOccurs="0"/>
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
    "domains"
})
@XmlRootElement(name = "domainDictionary")
public class WSIDomainDictionary implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "domain")
    protected List<WSIDomain> domains;

    /**
     * Gets the value of the domains property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domains property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomains().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIDomain }
     * 
     * 
     */
    public List<WSIDomain> getDomains() {
        if (domains == null) {
            domains = new ArrayList<WSIDomain>();
        }
        return this.domains;
    }

    public boolean isSetDomains() {
        return ((this.domains!= null)&&(!this.domains.isEmpty()));
    }

    public void unsetDomains() {
        this.domains = null;
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
            List<WSIDomain> theDomains;
            theDomains = (this.isSetDomains()?this.getDomains():null);
            strategy.appendField(locator, this, "domains", buffer, theDomains);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIDomainDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIDomainDictionary that = ((WSIDomainDictionary) object);
        {
            List<WSIDomain> lhsDomains;
            lhsDomains = (this.isSetDomains()?this.getDomains():null);
            List<WSIDomain> rhsDomains;
            rhsDomains = (that.isSetDomains()?that.getDomains():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "domains", lhsDomains), LocatorUtils.property(thatLocator, "domains", rhsDomains), lhsDomains, rhsDomains)) {
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
            List<WSIDomain> theDomains;
            theDomains = (this.isSetDomains()?this.getDomains():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "domains", theDomains), currentHashCode, theDomains);
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
        if (draftCopy instanceof WSIDomainDictionary) {
            final WSIDomainDictionary copy = ((WSIDomainDictionary) draftCopy);
            if (this.isSetDomains()) {
                List<WSIDomain> sourceDomains;
                sourceDomains = (this.isSetDomains()?this.getDomains():null);
                @SuppressWarnings("unchecked")
                List<WSIDomain> copyDomains = ((List<WSIDomain> ) strategy.copy(LocatorUtils.property(locator, "domains", sourceDomains), sourceDomains));
                copy.unsetDomains();
                List<WSIDomain> uniqueDomainsl = copy.getDomains();
                uniqueDomainsl.addAll(copyDomains);
            } else {
                copy.unsetDomains();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIDomainDictionary();
    }

    /**
     * Sets the value of the domains property.
     * 
     * @param domains
     *     allowed object is
     *     {@link WSIDomain }
     *     
     */
    public void setDomains(List<WSIDomain> domains) {
        this.domains = domains;
    }

}
