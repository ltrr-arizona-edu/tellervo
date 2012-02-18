
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
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}box" maxOccurs="unbounded" minOccurs="0"/>
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
    "boxes"
})
@XmlRootElement(name = "boxDictionary")
public class WSIBoxDictionary implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "box")
    protected List<WSIBox> boxes;

    /**
     * Gets the value of the boxes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the boxes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBoxes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIBox }
     * 
     * 
     */
    public List<WSIBox> getBoxes() {
        if (boxes == null) {
            boxes = new ArrayList<WSIBox>();
        }
        return this.boxes;
    }

    public boolean isSetBoxes() {
        return ((this.boxes!= null)&&(!this.boxes.isEmpty()));
    }

    public void unsetBoxes() {
        this.boxes = null;
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
            List<WSIBox> theBoxes;
            theBoxes = (this.isSetBoxes()?this.getBoxes():null);
            strategy.appendField(locator, this, "boxes", buffer, theBoxes);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIBoxDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIBoxDictionary that = ((WSIBoxDictionary) object);
        {
            List<WSIBox> lhsBoxes;
            lhsBoxes = (this.isSetBoxes()?this.getBoxes():null);
            List<WSIBox> rhsBoxes;
            rhsBoxes = (that.isSetBoxes()?that.getBoxes():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "boxes", lhsBoxes), LocatorUtils.property(thatLocator, "boxes", rhsBoxes), lhsBoxes, rhsBoxes)) {
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
            List<WSIBox> theBoxes;
            theBoxes = (this.isSetBoxes()?this.getBoxes():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "boxes", theBoxes), currentHashCode, theBoxes);
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
        if (draftCopy instanceof WSIBoxDictionary) {
            final WSIBoxDictionary copy = ((WSIBoxDictionary) draftCopy);
            if (this.isSetBoxes()) {
                List<WSIBox> sourceBoxes;
                sourceBoxes = (this.isSetBoxes()?this.getBoxes():null);
                @SuppressWarnings("unchecked")
                List<WSIBox> copyBoxes = ((List<WSIBox> ) strategy.copy(LocatorUtils.property(locator, "boxes", sourceBoxes), sourceBoxes));
                copy.unsetBoxes();
                List<WSIBox> uniqueBoxesl = copy.getBoxes();
                uniqueBoxesl.addAll(copyBoxes);
            } else {
                copy.unsetBoxes();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIBoxDictionary();
    }

    /**
     * Sets the value of the boxes property.
     * 
     * @param boxes
     *     allowed object is
     *     {@link WSIBox }
     *     
     */
    public void setBoxes(List<WSIBox> boxes) {
        this.boxes = boxes;
    }

}
