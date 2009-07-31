
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
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
 * <p>Java class for corina element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="corina">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}request"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}header"/>
 *             &lt;choice>
 *               &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}content" minOccurs="0"/>
 *               &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}help" minOccurs="0"/>
 *             &lt;/choice>
 *           &lt;/sequence>
 *         &lt;/choice>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "help",
    "content",
    "request"
})
@XmlRootElement(name = "corina")
public class WSIRootElement
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected WSIHeader header;
    protected WSIHelp help;
    protected WSIContent content;
    protected WSIRequest request;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link WSIHeader }
     *     
     */
    public WSIHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIHeader }
     *     
     */
    public void setHeader(WSIHeader value) {
        this.header = value;
    }

    public boolean isSetHeader() {
        return (this.header!= null);
    }

    /**
     * Gets the value of the help property.
     * 
     * @return
     *     possible object is
     *     {@link WSIHelp }
     *     
     */
    public WSIHelp getHelp() {
        return help;
    }

    /**
     * Sets the value of the help property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIHelp }
     *     
     */
    public void setHelp(WSIHelp value) {
        this.help = value;
    }

    public boolean isSetHelp() {
        return (this.help!= null);
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link WSIContent }
     *     
     */
    public WSIContent getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIContent }
     *     
     */
    public void setContent(WSIContent value) {
        this.content = value;
    }

    public boolean isSetContent() {
        return (this.content!= null);
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link WSIRequest }
     *     
     */
    public WSIRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIRequest }
     *     
     */
    public void setRequest(WSIRequest value) {
        this.request = value;
    }

    public boolean isSetRequest() {
        return (this.request!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSIRootElement)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSIRootElement that = ((WSIRootElement) object);
        equalsBuilder.append(this.getHeader(), that.getHeader());
        equalsBuilder.append(this.getHelp(), that.getHelp());
        equalsBuilder.append(this.getContent(), that.getContent());
        equalsBuilder.append(this.getRequest(), that.getRequest());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSIRootElement)) {
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
        hashCodeBuilder.append(this.getHeader());
        hashCodeBuilder.append(this.getHelp());
        hashCodeBuilder.append(this.getContent());
        hashCodeBuilder.append(this.getRequest());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            WSIHeader theHeader;
            theHeader = this.getHeader();
            toStringBuilder.append("header", theHeader);
        }
        {
            WSIHelp theHelp;
            theHelp = this.getHelp();
            toStringBuilder.append("help", theHelp);
        }
        {
            WSIContent theContent;
            theContent = this.getContent();
            toStringBuilder.append("content", theContent);
        }
        {
            WSIRequest theRequest;
            theRequest = this.getRequest();
            toStringBuilder.append("request", theRequest);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSIRootElement copy = ((target == null)?((WSIRootElement) createCopy()):((WSIRootElement) target));
        if (this.isSetHeader()) {
            WSIHeader sourceHeader;
            sourceHeader = this.getHeader();
            WSIHeader copyHeader = ((WSIHeader) copyBuilder.copy(sourceHeader));
            copy.setHeader(copyHeader);
        } else {
            copy.header = null;
        }
        if (this.isSetHelp()) {
            WSIHelp sourceHelp;
            sourceHelp = this.getHelp();
            WSIHelp copyHelp = ((WSIHelp) copyBuilder.copy(sourceHelp));
            copy.setHelp(copyHelp);
        } else {
            copy.help = null;
        }
        if (this.isSetContent()) {
            WSIContent sourceContent;
            sourceContent = this.getContent();
            WSIContent copyContent = ((WSIContent) copyBuilder.copy(sourceContent));
            copy.setContent(copyContent);
        } else {
            copy.content = null;
        }
        if (this.isSetRequest()) {
            WSIRequest sourceRequest;
            sourceRequest = this.getRequest();
            WSIRequest copyRequest = ((WSIRequest) copyBuilder.copy(sourceRequest));
            copy.setRequest(copyRequest);
        } else {
            copy.request = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSIRootElement();
    }

}
