
package org.tellervo.schema;

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
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}configuration" maxOccurs="unbounded" minOccurs="0"/>
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
    "configurations"
})
@XmlRootElement(name = "configurationDictionary")
public class WSIConfigurationDictionary implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "configuration")
    protected List<WSIConfiguration> configurations;

    /**
     * Gets the value of the configurations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the configurations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfigurations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIConfiguration }
     * 
     * 
     */
    public List<WSIConfiguration> getConfigurations() {
        if (configurations == null) {
            configurations = new ArrayList<WSIConfiguration>();
        }
        return this.configurations;
    }

    public boolean isSetConfigurations() {
        return ((this.configurations!= null)&&(!this.configurations.isEmpty()));
    }

    public void unsetConfigurations() {
        this.configurations = null;
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
            List<WSIConfiguration> theConfigurations;
            theConfigurations = (this.isSetConfigurations()?this.getConfigurations():null);
            strategy.appendField(locator, this, "configurations", buffer, theConfigurations);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIConfigurationDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIConfigurationDictionary that = ((WSIConfigurationDictionary) object);
        {
            List<WSIConfiguration> lhsConfigurations;
            lhsConfigurations = (this.isSetConfigurations()?this.getConfigurations():null);
            List<WSIConfiguration> rhsConfigurations;
            rhsConfigurations = (that.isSetConfigurations()?that.getConfigurations():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "configurations", lhsConfigurations), LocatorUtils.property(thatLocator, "configurations", rhsConfigurations), lhsConfigurations, rhsConfigurations)) {
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
            List<WSIConfiguration> theConfigurations;
            theConfigurations = (this.isSetConfigurations()?this.getConfigurations():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "configurations", theConfigurations), currentHashCode, theConfigurations);
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
        if (draftCopy instanceof WSIConfigurationDictionary) {
            final WSIConfigurationDictionary copy = ((WSIConfigurationDictionary) draftCopy);
            if (this.isSetConfigurations()) {
                List<WSIConfiguration> sourceConfigurations;
                sourceConfigurations = (this.isSetConfigurations()?this.getConfigurations():null);
                @SuppressWarnings("unchecked")
                List<WSIConfiguration> copyConfigurations = ((List<WSIConfiguration> ) strategy.copy(LocatorUtils.property(locator, "configurations", sourceConfigurations), sourceConfigurations));
                copy.unsetConfigurations();
                List<WSIConfiguration> uniqueConfigurationsl = copy.getConfigurations();
                uniqueConfigurationsl.addAll(copyConfigurations);
            } else {
                copy.unsetConfigurations();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIConfigurationDictionary();
    }

    /**
     * Sets the value of the configurations property.
     * 
     * @param configurations
     *     allowed object is
     *     {@link WSIConfiguration }
     *     
     */
    public void setConfigurations(List<WSIConfiguration> configurations) {
        this.configurations = configurations;
    }

}
