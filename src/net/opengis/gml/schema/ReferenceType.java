
package net.opengis.gml.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
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
 * 
 *             A pattern or base for derived types used to specify complex
 *             types corresponding to a UML aggregation association. An
 *             instance of this type serves as a pointer to a remote Object.
 *          
 * 
 * <p>Java class for ReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://www.opengis.net/gml}AssociationAttributeGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferenceType")
public class ReferenceType
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlAttribute(name = "type", namespace = "http://www.w3.org/1999/xlink")
    protected String type;
    @XmlAttribute(name = "href", namespace = "http://www.w3.org/1999/xlink")
    @XmlSchemaType(name = "anyURI")
    protected String href;
    @XmlAttribute(name = "role", namespace = "http://www.w3.org/1999/xlink")
    @XmlSchemaType(name = "anyURI")
    protected String role;
    @XmlAttribute(name = "arcrole", namespace = "http://www.w3.org/1999/xlink")
    @XmlSchemaType(name = "anyURI")
    protected String arcrole;
    @XmlAttribute(name = "title", namespace = "http://www.w3.org/1999/xlink")
    protected String title;
    @XmlAttribute(name = "show", namespace = "http://www.w3.org/1999/xlink")
    protected String show;
    @XmlAttribute(name = "actuate", namespace = "http://www.w3.org/1999/xlink")
    protected String actuate;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "simple";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    public boolean isSetHref() {
        return (this.href!= null);
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

    public boolean isSetRole() {
        return (this.role!= null);
    }

    /**
     * Gets the value of the arcrole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArcrole() {
        return arcrole;
    }

    /**
     * Sets the value of the arcrole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArcrole(String value) {
        this.arcrole = value;
    }

    public boolean isSetArcrole() {
        return (this.arcrole!= null);
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    public boolean isSetTitle() {
        return (this.title!= null);
    }

    /**
     * Gets the value of the show property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShow() {
        return show;
    }

    /**
     * Sets the value of the show property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShow(String value) {
        this.show = value;
    }

    public boolean isSetShow() {
        return (this.show!= null);
    }

    /**
     * Gets the value of the actuate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActuate() {
        return actuate;
    }

    /**
     * Sets the value of the actuate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActuate(String value) {
        this.actuate = value;
    }

    public boolean isSetActuate() {
        return (this.actuate!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof ReferenceType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final ReferenceType that = ((ReferenceType) object);
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getHref(), that.getHref());
        equalsBuilder.append(this.getRole(), that.getRole());
        equalsBuilder.append(this.getArcrole(), that.getArcrole());
        equalsBuilder.append(this.getTitle(), that.getTitle());
        equalsBuilder.append(this.getShow(), that.getShow());
        equalsBuilder.append(this.getActuate(), that.getActuate());
    }

    public boolean equals(Object object) {
        if (!(object instanceof ReferenceType)) {
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
        hashCodeBuilder.append(this.getType());
        hashCodeBuilder.append(this.getHref());
        hashCodeBuilder.append(this.getRole());
        hashCodeBuilder.append(this.getArcrole());
        hashCodeBuilder.append(this.getTitle());
        hashCodeBuilder.append(this.getShow());
        hashCodeBuilder.append(this.getActuate());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
        {
            String theHref;
            theHref = this.getHref();
            toStringBuilder.append("href", theHref);
        }
        {
            String theRole;
            theRole = this.getRole();
            toStringBuilder.append("role", theRole);
        }
        {
            String theArcrole;
            theArcrole = this.getArcrole();
            toStringBuilder.append("arcrole", theArcrole);
        }
        {
            String theTitle;
            theTitle = this.getTitle();
            toStringBuilder.append("title", theTitle);
        }
        {
            String theShow;
            theShow = this.getShow();
            toStringBuilder.append("show", theShow);
        }
        {
            String theActuate;
            theActuate = this.getActuate();
            toStringBuilder.append("actuate", theActuate);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final ReferenceType copy = ((target == null)?((ReferenceType) createCopy()):((ReferenceType) target));
        if (this.isSetType()) {
            String sourceType;
            sourceType = this.getType();
            String copyType = ((String) copyBuilder.copy(sourceType));
            copy.setType(copyType);
        } else {
            copy.type = null;
        }
        if (this.isSetHref()) {
            String sourceHref;
            sourceHref = this.getHref();
            String copyHref = ((String) copyBuilder.copy(sourceHref));
            copy.setHref(copyHref);
        } else {
            copy.href = null;
        }
        if (this.isSetRole()) {
            String sourceRole;
            sourceRole = this.getRole();
            String copyRole = ((String) copyBuilder.copy(sourceRole));
            copy.setRole(copyRole);
        } else {
            copy.role = null;
        }
        if (this.isSetArcrole()) {
            String sourceArcrole;
            sourceArcrole = this.getArcrole();
            String copyArcrole = ((String) copyBuilder.copy(sourceArcrole));
            copy.setArcrole(copyArcrole);
        } else {
            copy.arcrole = null;
        }
        if (this.isSetTitle()) {
            String sourceTitle;
            sourceTitle = this.getTitle();
            String copyTitle = ((String) copyBuilder.copy(sourceTitle));
            copy.setTitle(copyTitle);
        } else {
            copy.title = null;
        }
        if (this.isSetShow()) {
            String sourceShow;
            sourceShow = this.getShow();
            String copyShow = ((String) copyBuilder.copy(sourceShow));
            copy.setShow(copyShow);
        } else {
            copy.show = null;
        }
        if (this.isSetActuate()) {
            String sourceActuate;
            sourceActuate = this.getActuate();
            String copyActuate = ((String) copyBuilder.copy(sourceActuate));
            copy.setActuate(copyActuate);
        } else {
            copy.actuate = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new ReferenceType();
    }

}
