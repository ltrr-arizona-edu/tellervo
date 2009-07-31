
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
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
import org.tridas.annotations.TridasEditProperties;


/**
 * <p>Java class for seriesLink complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="seriesLink">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="idRef">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="xLink">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute ref="{http://www.w3.org/1999/xlink}href"/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.tridas.org/1.3}identifier"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "seriesLink", propOrder = {
    "identifier",
    "xLink",
    "idRef"
})
@TridasEditProperties(finalType = true)
public class SeriesLink
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected TridasIdentifier identifier;
    protected SeriesLink.XLink xLink;
    protected SeriesLink.IdRef idRef;

    /**
     * Gets the value of the identifier property.
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
     * Gets the value of the xLink property.
     * 
     * @return
     *     possible object is
     *     {@link SeriesLink.XLink }
     *     
     */
    public SeriesLink.XLink getXLink() {
        return xLink;
    }

    /**
     * Sets the value of the xLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeriesLink.XLink }
     *     
     */
    public void setXLink(SeriesLink.XLink value) {
        this.xLink = value;
    }

    public boolean isSetXLink() {
        return (this.xLink!= null);
    }

    /**
     * Gets the value of the idRef property.
     * 
     * @return
     *     possible object is
     *     {@link SeriesLink.IdRef }
     *     
     */
    public SeriesLink.IdRef getIdRef() {
        return idRef;
    }

    /**
     * Sets the value of the idRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeriesLink.IdRef }
     *     
     */
    public void setIdRef(SeriesLink.IdRef value) {
        this.idRef = value;
    }

    public boolean isSetIdRef() {
        return (this.idRef!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SeriesLink)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final SeriesLink that = ((SeriesLink) object);
        equalsBuilder.append(this.getIdentifier(), that.getIdentifier());
        equalsBuilder.append(this.getXLink(), that.getXLink());
        equalsBuilder.append(this.getIdRef(), that.getIdRef());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SeriesLink)) {
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
        hashCodeBuilder.append(this.getXLink());
        hashCodeBuilder.append(this.getIdRef());
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
            SeriesLink.XLink theXLink;
            theXLink = this.getXLink();
            toStringBuilder.append("xLink", theXLink);
        }
        {
            SeriesLink.IdRef theIdRef;
            theIdRef = this.getIdRef();
            toStringBuilder.append("idRef", theIdRef);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final SeriesLink copy = ((target == null)?((SeriesLink) createCopy()):((SeriesLink) target));
        if (this.isSetIdentifier()) {
            TridasIdentifier sourceIdentifier;
            sourceIdentifier = this.getIdentifier();
            TridasIdentifier copyIdentifier = ((TridasIdentifier) copyBuilder.copy(sourceIdentifier));
            copy.setIdentifier(copyIdentifier);
        } else {
            copy.identifier = null;
        }
        if (this.isSetXLink()) {
            SeriesLink.XLink sourceXLink;
            sourceXLink = this.getXLink();
            SeriesLink.XLink copyXLink = ((SeriesLink.XLink) copyBuilder.copy(sourceXLink));
            copy.setXLink(copyXLink);
        } else {
            copy.xLink = null;
        }
        if (this.isSetIdRef()) {
            SeriesLink.IdRef sourceIdRef;
            sourceIdRef = this.getIdRef();
            SeriesLink.IdRef copyIdRef = ((SeriesLink.IdRef) copyBuilder.copy(sourceIdRef));
            copy.setIdRef(copyIdRef);
        } else {
            copy.idRef = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new SeriesLink();
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class IdRef
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        @XmlAttribute(name = "ref")
        @XmlIDREF
        @XmlSchemaType(name = "IDREF")
        protected Object ref;

        /**
         * Gets the value of the ref property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getRef() {
            return ref;
        }

        /**
         * Sets the value of the ref property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setRef(Object value) {
            this.ref = value;
        }

        public boolean isSetRef() {
            return (this.ref!= null);
        }

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof SeriesLink.IdRef)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final SeriesLink.IdRef that = ((SeriesLink.IdRef) object);
            equalsBuilder.append(this.getRef(), that.getRef());
        }

        public boolean equals(Object object) {
            if (!(object instanceof SeriesLink.IdRef)) {
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
            hashCodeBuilder.append(this.getRef());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                Object theRef;
                theRef = this.getRef();
                toStringBuilder.append("ref", theRef);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final SeriesLink.IdRef copy = ((target == null)?((SeriesLink.IdRef) createCopy()):((SeriesLink.IdRef) target));
            if (this.isSetRef()) {
                Object sourceRef;
                sourceRef = this.getRef();
                Object copyRef = ((Object) copyBuilder.copy(sourceRef));
                copy.setRef(copyRef);
            } else {
                copy.ref = null;
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new SeriesLink.IdRef();
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute ref="{http://www.w3.org/1999/xlink}href"/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class XLink
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        @XmlAttribute(name = "href", namespace = "http://www.w3.org/1999/xlink")
        @XmlSchemaType(name = "anyURI")
        protected String href;

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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof SeriesLink.XLink)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final SeriesLink.XLink that = ((SeriesLink.XLink) object);
            equalsBuilder.append(this.getHref(), that.getHref());
        }

        public boolean equals(Object object) {
            if (!(object instanceof SeriesLink.XLink)) {
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
            hashCodeBuilder.append(this.getHref());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                String theHref;
                theHref = this.getHref();
                toStringBuilder.append("href", theHref);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final SeriesLink.XLink copy = ((target == null)?((SeriesLink.XLink) createCopy()):((SeriesLink.XLink) target));
            if (this.isSetHref()) {
                String sourceHref;
                sourceHref = this.getHref();
                String copyHref = ((String) copyBuilder.copy(sourceHref));
                copy.setHref(copyHref);
            } else {
                copy.href = null;
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new SeriesLink.XLink();
        }

    }

}
