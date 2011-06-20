
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
import org.tridas.schema.ControlledVoc;


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
 *         &lt;element name="sapwood" type="{http://www.tridas.org/1.2.2}controlledVoc" maxOccurs="unbounded" minOccurs="0"/>
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
    "sapwoods"
})
@XmlRootElement(name = "sapwoodDictionary")
public class WSISapwoodDictionary implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "sapwood")
    protected List<ControlledVoc> sapwoods;

    /**
     * Gets the value of the sapwoods property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sapwoods property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSapwoods().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlledVoc }
     * 
     * 
     */
    public List<ControlledVoc> getSapwoods() {
        if (sapwoods == null) {
            sapwoods = new ArrayList<ControlledVoc>();
        }
        return this.sapwoods;
    }

    public boolean isSetSapwoods() {
        return ((this.sapwoods!= null)&&(!this.sapwoods.isEmpty()));
    }

    public void unsetSapwoods() {
        this.sapwoods = null;
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
            List<ControlledVoc> theSapwoods;
            theSapwoods = (this.isSetSapwoods()?this.getSapwoods():null);
            strategy.appendField(locator, this, "sapwoods", buffer, theSapwoods);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSISapwoodDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSISapwoodDictionary that = ((WSISapwoodDictionary) object);
        {
            List<ControlledVoc> lhsSapwoods;
            lhsSapwoods = (this.isSetSapwoods()?this.getSapwoods():null);
            List<ControlledVoc> rhsSapwoods;
            rhsSapwoods = (that.isSetSapwoods()?that.getSapwoods():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "sapwoods", lhsSapwoods), LocatorUtils.property(thatLocator, "sapwoods", rhsSapwoods), lhsSapwoods, rhsSapwoods)) {
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
            List<ControlledVoc> theSapwoods;
            theSapwoods = (this.isSetSapwoods()?this.getSapwoods():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "sapwoods", theSapwoods), currentHashCode, theSapwoods);
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
        if (draftCopy instanceof WSISapwoodDictionary) {
            final WSISapwoodDictionary copy = ((WSISapwoodDictionary) draftCopy);
            if (this.isSetSapwoods()) {
                List<ControlledVoc> sourceSapwoods;
                sourceSapwoods = (this.isSetSapwoods()?this.getSapwoods():null);
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copySapwoods = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "sapwoods", sourceSapwoods), sourceSapwoods));
                copy.unsetSapwoods();
                List<ControlledVoc> uniqueSapwoodsl = copy.getSapwoods();
                uniqueSapwoodsl.addAll(copySapwoods);
            } else {
                copy.unsetSapwoods();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSISapwoodDictionary();
    }

    /**
     * Sets the value of the sapwoods property.
     * 
     * @param sapwoods
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setSapwoods(List<ControlledVoc> sapwoods) {
        this.sapwoods = sapwoods;
    }

}
