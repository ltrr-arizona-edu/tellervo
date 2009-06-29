
package org.tridas.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="preferredSeries" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="idRef">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="xLink">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute ref="{http://www.w3.org/1999/xlink}href"/>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element ref="{http://www.tridas.org/1.2}identifier"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="idRef">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="xLink">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute ref="{http://www.w3.org/1999/xlink}href"/>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element ref="{http://www.tridas.org/1.2}identifier"/>
 *         &lt;/choice>
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
    "preferredSeries",
    "idRevesAndXLinksAndIdentifiers"
})
@XmlRootElement(name = "linkSeries")
public class TridasLinkSeries
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected TridasLinkSeries.PreferredSeries preferredSeries;
    @XmlElements({
        @XmlElement(name = "idRef", type = TridasLinkSeries.IdRef.class),
        @XmlElement(name = "identifier", type = TridasIdentifier.class),
        @XmlElement(name = "xLink", type = TridasLinkSeries.XLink.class)
    })
    protected List<Object> idRevesAndXLinksAndIdentifiers;

    /**
     * Gets the value of the preferredSeries property.
     * 
     * @return
     *     possible object is
     *     {@link TridasLinkSeries.PreferredSeries }
     *     
     */
    public TridasLinkSeries.PreferredSeries getPreferredSeries() {
        return preferredSeries;
    }

    /**
     * Sets the value of the preferredSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasLinkSeries.PreferredSeries }
     *     
     */
    public void setPreferredSeries(TridasLinkSeries.PreferredSeries value) {
        this.preferredSeries = value;
    }

    public boolean isSetPreferredSeries() {
        return (this.preferredSeries!= null);
    }

    /**
     * Gets the value of the idRevesAndXLinksAndIdentifiers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idRevesAndXLinksAndIdentifiers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdRevesAndXLinksAndIdentifiers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasLinkSeries.IdRef }
     * {@link TridasIdentifier }
     * {@link TridasLinkSeries.XLink }
     * 
     * 
     */
    public List<Object> getIdRevesAndXLinksAndIdentifiers() {
        if (idRevesAndXLinksAndIdentifiers == null) {
            idRevesAndXLinksAndIdentifiers = new ArrayList<Object>();
        }
        return this.idRevesAndXLinksAndIdentifiers;
    }

    public boolean isSetIdRevesAndXLinksAndIdentifiers() {
        return ((this.idRevesAndXLinksAndIdentifiers!= null)&&(!this.idRevesAndXLinksAndIdentifiers.isEmpty()));
    }

    public void unsetIdRevesAndXLinksAndIdentifiers() {
        this.idRevesAndXLinksAndIdentifiers = null;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasLinkSeries)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasLinkSeries that = ((TridasLinkSeries) object);
        equalsBuilder.append(this.getPreferredSeries(), that.getPreferredSeries());
        equalsBuilder.append(this.getIdRevesAndXLinksAndIdentifiers(), that.getIdRevesAndXLinksAndIdentifiers());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasLinkSeries)) {
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
        hashCodeBuilder.append(this.getPreferredSeries());
        hashCodeBuilder.append(this.getIdRevesAndXLinksAndIdentifiers());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            TridasLinkSeries.PreferredSeries thePreferredSeries;
            thePreferredSeries = this.getPreferredSeries();
            toStringBuilder.append("preferredSeries", thePreferredSeries);
        }
        {
            List<Object> theIdRevesAndXLinksAndIdentifiers;
            theIdRevesAndXLinksAndIdentifiers = this.getIdRevesAndXLinksAndIdentifiers();
            toStringBuilder.append("idRevesAndXLinksAndIdentifiers", theIdRevesAndXLinksAndIdentifiers);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasLinkSeries copy = ((target == null)?((TridasLinkSeries) createCopy()):((TridasLinkSeries) target));
        {
            TridasLinkSeries.PreferredSeries sourcePreferredSeries;
            sourcePreferredSeries = this.getPreferredSeries();
            TridasLinkSeries.PreferredSeries copyPreferredSeries = ((TridasLinkSeries.PreferredSeries) copyBuilder.copy(sourcePreferredSeries));
            copy.setPreferredSeries(copyPreferredSeries);
        }
        {
            List<Object> sourceIdRevesAndXLinksAndIdentifiers;
            sourceIdRevesAndXLinksAndIdentifiers = this.getIdRevesAndXLinksAndIdentifiers();
            List<Object> copyIdRevesAndXLinksAndIdentifiers = ((List<Object> ) copyBuilder.copy(sourceIdRevesAndXLinksAndIdentifiers));
            copy.unsetIdRevesAndXLinksAndIdentifiers();
            List<Object> uniqueIdRevesAndXLinksAndIdentifiersl = copy.getIdRevesAndXLinksAndIdentifiers();
            uniqueIdRevesAndXLinksAndIdentifiersl.addAll(copyIdRevesAndXLinksAndIdentifiers);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasLinkSeries();
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
    public static class IdRef implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        @XmlAttribute
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
            if (!(object instanceof TridasLinkSeries.IdRef)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasLinkSeries.IdRef that = ((TridasLinkSeries.IdRef) object);
            equalsBuilder.append(this.getRef(), that.getRef());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasLinkSeries.IdRef)) {
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
            final TridasLinkSeries.IdRef copy = ((target == null)?((TridasLinkSeries.IdRef) createCopy()):((TridasLinkSeries.IdRef) target));
            {
                Object sourceRef;
                sourceRef = this.getRef();
                Object copyRef = ((Object) copyBuilder.copy(sourceRef));
                copy.setRef(copyRef);
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasLinkSeries.IdRef();
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
     *         &lt;element ref="{http://www.tridas.org/1.2}identifier"/>
     *       &lt;/choice>
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
        "xLink",
        "idRef"
    })
    public static class PreferredSeries
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        protected TridasIdentifier identifier;
        protected TridasLinkSeries.PreferredSeries.XLink xLink;
        protected TridasLinkSeries.PreferredSeries.IdRef idRef;

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
         *     {@link TridasLinkSeries.PreferredSeries.XLink }
         *     
         */
        public TridasLinkSeries.PreferredSeries.XLink getXLink() {
            return xLink;
        }

        /**
         * Sets the value of the xLink property.
         * 
         * @param value
         *     allowed object is
         *     {@link TridasLinkSeries.PreferredSeries.XLink }
         *     
         */
        public void setXLink(TridasLinkSeries.PreferredSeries.XLink value) {
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
         *     {@link TridasLinkSeries.PreferredSeries.IdRef }
         *     
         */
        public TridasLinkSeries.PreferredSeries.IdRef getIdRef() {
            return idRef;
        }

        /**
         * Sets the value of the idRef property.
         * 
         * @param value
         *     allowed object is
         *     {@link TridasLinkSeries.PreferredSeries.IdRef }
         *     
         */
        public void setIdRef(TridasLinkSeries.PreferredSeries.IdRef value) {
            this.idRef = value;
        }

        public boolean isSetIdRef() {
            return (this.idRef!= null);
        }

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasLinkSeries.PreferredSeries)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasLinkSeries.PreferredSeries that = ((TridasLinkSeries.PreferredSeries) object);
            equalsBuilder.append(this.getIdentifier(), that.getIdentifier());
            equalsBuilder.append(this.getXLink(), that.getXLink());
            equalsBuilder.append(this.getIdRef(), that.getIdRef());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasLinkSeries.PreferredSeries)) {
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
                TridasLinkSeries.PreferredSeries.XLink theXLink;
                theXLink = this.getXLink();
                toStringBuilder.append("xLink", theXLink);
            }
            {
                TridasLinkSeries.PreferredSeries.IdRef theIdRef;
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
            final TridasLinkSeries.PreferredSeries copy = ((target == null)?((TridasLinkSeries.PreferredSeries) createCopy()):((TridasLinkSeries.PreferredSeries) target));
            {
                TridasIdentifier sourceIdentifier;
                sourceIdentifier = this.getIdentifier();
                TridasIdentifier copyIdentifier = ((TridasIdentifier) copyBuilder.copy(sourceIdentifier));
                copy.setIdentifier(copyIdentifier);
            }
            {
                TridasLinkSeries.PreferredSeries.XLink sourceXLink;
                sourceXLink = this.getXLink();
                TridasLinkSeries.PreferredSeries.XLink copyXLink = ((TridasLinkSeries.PreferredSeries.XLink) copyBuilder.copy(sourceXLink));
                copy.setXLink(copyXLink);
            }
            {
                TridasLinkSeries.PreferredSeries.IdRef sourceIdRef;
                sourceIdRef = this.getIdRef();
                TridasLinkSeries.PreferredSeries.IdRef copyIdRef = ((TridasLinkSeries.PreferredSeries.IdRef) copyBuilder.copy(sourceIdRef));
                copy.setIdRef(copyIdRef);
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasLinkSeries.PreferredSeries();
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
            @XmlAttribute
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
                if (!(object instanceof TridasLinkSeries.PreferredSeries.IdRef)) {
                    equalsBuilder.appendSuper(false);
                    return ;
                }
                if (this == object) {
                    return ;
                }
                final TridasLinkSeries.PreferredSeries.IdRef that = ((TridasLinkSeries.PreferredSeries.IdRef) object);
                equalsBuilder.append(this.getRef(), that.getRef());
            }

            public boolean equals(Object object) {
                if (!(object instanceof TridasLinkSeries.PreferredSeries.IdRef)) {
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
                final TridasLinkSeries.PreferredSeries.IdRef copy = ((target == null)?((TridasLinkSeries.PreferredSeries.IdRef) createCopy()):((TridasLinkSeries.PreferredSeries.IdRef) target));
                {
                    Object sourceRef;
                    sourceRef = this.getRef();
                    Object copyRef = ((Object) copyBuilder.copy(sourceRef));
                    copy.setRef(copyRef);
                }
                return copy;
            }

            public Object copyTo(Object target) {
                final CopyBuilder copyBuilder = new JAXBCopyBuilder();
                return copyTo(target, copyBuilder);
            }

            public Object createCopy() {
                return new TridasLinkSeries.PreferredSeries.IdRef();
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
            @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
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
                if (!(object instanceof TridasLinkSeries.PreferredSeries.XLink)) {
                    equalsBuilder.appendSuper(false);
                    return ;
                }
                if (this == object) {
                    return ;
                }
                final TridasLinkSeries.PreferredSeries.XLink that = ((TridasLinkSeries.PreferredSeries.XLink) object);
                equalsBuilder.append(this.getHref(), that.getHref());
            }

            public boolean equals(Object object) {
                if (!(object instanceof TridasLinkSeries.PreferredSeries.XLink)) {
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
                final TridasLinkSeries.PreferredSeries.XLink copy = ((target == null)?((TridasLinkSeries.PreferredSeries.XLink) createCopy()):((TridasLinkSeries.PreferredSeries.XLink) target));
                {
                    String sourceHref;
                    sourceHref = this.getHref();
                    String copyHref = ((String) copyBuilder.copy(sourceHref));
                    copy.setHref(copyHref);
                }
                return copy;
            }

            public Object copyTo(Object target) {
                final CopyBuilder copyBuilder = new JAXBCopyBuilder();
                return copyTo(target, copyBuilder);
            }

            public Object createCopy() {
                return new TridasLinkSeries.PreferredSeries.XLink();
            }

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
    public static class XLink implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
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
            if (!(object instanceof TridasLinkSeries.XLink)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasLinkSeries.XLink that = ((TridasLinkSeries.XLink) object);
            equalsBuilder.append(this.getHref(), that.getHref());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasLinkSeries.XLink)) {
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
            final TridasLinkSeries.XLink copy = ((target == null)?((TridasLinkSeries.XLink) createCopy()):((TridasLinkSeries.XLink) target));
            {
                String sourceHref;
                sourceHref = this.getHref();
                String copyHref = ((String) copyBuilder.copy(sourceHref));
                copy.setHref(copyHref);
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasLinkSeries.XLink();
        }

    }

}
