
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.3}identifier" minOccurs="0"/>
 *         &lt;element name="name">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="acronym" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="place" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "identifier",
    "name",
    "place",
    "country"
})
@XmlRootElement(name = "laboratory")
public class TridasLaboratory
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected TridasIdentifier identifier;
    @XmlElement(required = true)
    protected TridasLaboratory.Name name;
    @XmlElement(required = true)
    protected String place;
    @XmlElement(required = true)
    protected String country;

    /**
     * 
     * 							Identifier for the laboratory.
     * 						
     * 
     * @return
     *     possible object is
     *     {@link TridasIdentifier }
     *     
     */
    public TridasIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasIdentifier }
     *     
     */
    public void setIdentifier(TridasIdentifier value) {
        this.identifier = value;
    }

    public boolean isSetIdentifier() {
        return (this.identifier!= null);
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link TridasLaboratory.Name }
     *     
     */
    public TridasLaboratory.Name getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasLaboratory.Name }
     *     
     */
    public void setName(TridasLaboratory.Name value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name!= null);
    }

    /**
     * Gets the value of the place property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlace() {
        return place;
    }

    /**
     * Sets the value of the place property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlace(String value) {
        this.place = value;
    }

    public boolean isSetPlace() {
        return (this.place!= null);
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    public boolean isSetCountry() {
        return (this.country!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasLaboratory)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasLaboratory that = ((TridasLaboratory) object);
        equalsBuilder.append(this.getIdentifier(), that.getIdentifier());
        equalsBuilder.append(this.getName(), that.getName());
        equalsBuilder.append(this.getPlace(), that.getPlace());
        equalsBuilder.append(this.getCountry(), that.getCountry());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasLaboratory)) {
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
        hashCodeBuilder.append(this.getIdentifier());
        hashCodeBuilder.append(this.getName());
        hashCodeBuilder.append(this.getPlace());
        hashCodeBuilder.append(this.getCountry());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            TridasIdentifier theIdentifier;
            theIdentifier = this.getIdentifier();
            toStringBuilder.append("identifier", theIdentifier);
        }
        {
            TridasLaboratory.Name theName;
            theName = this.getName();
            toStringBuilder.append("name", theName);
        }
        {
            String thePlace;
            thePlace = this.getPlace();
            toStringBuilder.append("place", thePlace);
        }
        {
            String theCountry;
            theCountry = this.getCountry();
            toStringBuilder.append("country", theCountry);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasLaboratory copy = ((target == null)?((TridasLaboratory) createCopy()):((TridasLaboratory) target));
        if (this.isSetIdentifier()) {
            TridasIdentifier sourceIdentifier;
            sourceIdentifier = this.getIdentifier();
            TridasIdentifier copyIdentifier = ((TridasIdentifier) copyBuilder.copy(sourceIdentifier));
            copy.setIdentifier(copyIdentifier);
        } else {
            copy.identifier = null;
        }
        if (this.isSetName()) {
            TridasLaboratory.Name sourceName;
            sourceName = this.getName();
            TridasLaboratory.Name copyName = ((TridasLaboratory.Name) copyBuilder.copy(sourceName));
            copy.setName(copyName);
        } else {
            copy.name = null;
        }
        if (this.isSetPlace()) {
            String sourcePlace;
            sourcePlace = this.getPlace();
            String copyPlace = ((String) copyBuilder.copy(sourcePlace));
            copy.setPlace(copyPlace);
        } else {
            copy.place = null;
        }
        if (this.isSetCountry()) {
            String sourceCountry;
            sourceCountry = this.getCountry();
            String copyCountry = ((String) copyBuilder.copy(sourceCountry));
            copy.setCountry(copyCountry);
        } else {
            copy.country = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasLaboratory();
    }


    /**
     * 
     * 								Name of the laboratory.
     * 							
     * 
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="acronym" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Name
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        @XmlValue
        protected String value;
        @XmlAttribute(name = "acronym")
        @XmlSchemaType(name = "anySimpleType")
        protected String acronym;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        public boolean isSetValue() {
            return (this.value!= null);
        }

        /**
         * Gets the value of the acronym property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAcronym() {
            return acronym;
        }

        /**
         * Sets the value of the acronym property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAcronym(String value) {
            this.acronym = value;
        }

        public boolean isSetAcronym() {
            return (this.acronym!= null);
        }

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasLaboratory.Name)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasLaboratory.Name that = ((TridasLaboratory.Name) object);
            equalsBuilder.append(this.getValue(), that.getValue());
            equalsBuilder.append(this.getAcronym(), that.getAcronym());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasLaboratory.Name)) {
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
            hashCodeBuilder.append(this.getValue());
            hashCodeBuilder.append(this.getAcronym());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                String theValue;
                theValue = this.getValue();
                toStringBuilder.append("value", theValue);
            }
            {
                String theAcronym;
                theAcronym = this.getAcronym();
                toStringBuilder.append("acronym", theAcronym);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasLaboratory.Name copy = ((target == null)?((TridasLaboratory.Name) createCopy()):((TridasLaboratory.Name) target));
            if (this.isSetValue()) {
                String sourceValue;
                sourceValue = this.getValue();
                String copyValue = ((String) copyBuilder.copy(sourceValue));
                copy.setValue(copyValue);
            } else {
                copy.value = null;
            }
            if (this.isSetAcronym()) {
                String sourceAcronym;
                sourceAcronym = this.getAcronym();
                String copyAcronym = ((String) copyBuilder.copy(sourceAcronym));
                copy.setAcronym(copyAcronym);
            } else {
                copy.acronym = null;
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasLaboratory.Name();
        }

    }

}
