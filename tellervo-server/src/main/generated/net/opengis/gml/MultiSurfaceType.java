
package net.opengis.gml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 * 
 *             A MultiSurface is defined by one or more Surfaces,
 *             referenced through surfaceMember elements.
 *          
 * 
 * <p>Java class for MultiSurfaceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultiSurfaceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractGeometricAggregateType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}surfaceMember" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiSurfaceType", propOrder = {
    "surfaceMembers"
})
public class MultiSurfaceType
    extends AbstractGeometricAggregateType
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "surfaceMember")
    protected List<SurfaceMember> surfaceMembers;

    /**
     * Gets the value of the surfaceMembers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the surfaceMembers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSurfaceMembers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SurfaceMember }
     * 
     * 
     */
    public List<SurfaceMember> getSurfaceMembers() {
        if (surfaceMembers == null) {
            surfaceMembers = new ArrayList<SurfaceMember>();
        }
        return this.surfaceMembers;
    }

    public boolean isSetSurfaceMembers() {
        return ((this.surfaceMembers!= null)&&(!this.surfaceMembers.isEmpty()));
    }

    public void unsetSurfaceMembers() {
        this.surfaceMembers = null;
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
        super.appendFields(locator, buffer, strategy);
        {
            List<SurfaceMember> theSurfaceMembers;
            theSurfaceMembers = (this.isSetSurfaceMembers()?this.getSurfaceMembers():null);
            strategy.appendField(locator, this, "surfaceMembers", buffer, theSurfaceMembers);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof MultiSurfaceType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final MultiSurfaceType that = ((MultiSurfaceType) object);
        {
            List<SurfaceMember> lhsSurfaceMembers;
            lhsSurfaceMembers = (this.isSetSurfaceMembers()?this.getSurfaceMembers():null);
            List<SurfaceMember> rhsSurfaceMembers;
            rhsSurfaceMembers = (that.isSetSurfaceMembers()?that.getSurfaceMembers():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "surfaceMembers", lhsSurfaceMembers), LocatorUtils.property(thatLocator, "surfaceMembers", rhsSurfaceMembers), lhsSurfaceMembers, rhsSurfaceMembers)) {
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
        int currentHashCode = super.hashCode(locator, strategy);
        {
            List<SurfaceMember> theSurfaceMembers;
            theSurfaceMembers = (this.isSetSurfaceMembers()?this.getSurfaceMembers():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "surfaceMembers", theSurfaceMembers), currentHashCode, theSurfaceMembers);
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
        super.copyTo(locator, draftCopy, strategy);
        if (draftCopy instanceof MultiSurfaceType) {
            final MultiSurfaceType copy = ((MultiSurfaceType) draftCopy);
            if (this.isSetSurfaceMembers()) {
                List<SurfaceMember> sourceSurfaceMembers;
                sourceSurfaceMembers = (this.isSetSurfaceMembers()?this.getSurfaceMembers():null);
                @SuppressWarnings("unchecked")
                List<SurfaceMember> copySurfaceMembers = ((List<SurfaceMember> ) strategy.copy(LocatorUtils.property(locator, "surfaceMembers", sourceSurfaceMembers), sourceSurfaceMembers));
                copy.unsetSurfaceMembers();
                List<SurfaceMember> uniqueSurfaceMembersl = copy.getSurfaceMembers();
                uniqueSurfaceMembersl.addAll(copySurfaceMembers);
            } else {
                copy.unsetSurfaceMembers();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new MultiSurfaceType();
    }

    /**
     * Sets the value of the surfaceMembers property.
     * 
     * @param surfaceMembers
     *     allowed object is
     *     {@link SurfaceMember }
     *     
     */
    public void setSurfaceMembers(List<SurfaceMember> surfaceMembers) {
        this.surfaceMembers = surfaceMembers;
    }

}
