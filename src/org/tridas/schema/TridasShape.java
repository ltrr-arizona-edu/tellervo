
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.tridas.org/1.2>controlledVoc">
 *       &lt;attribute name="normalTridas" type="{http://www.tridas.org/1.2}normalTridasShape" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "shape")
public class TridasShape
    extends ControlledVoc
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlAttribute
    protected NormalTridasShape normalTridas;

    /**
     * Gets the value of the normalTridas property.
     * 
     * @return
     *     possible object is
     *     {@link NormalTridasShape }
     *     
     */
    public NormalTridasShape getNormalTridas() {
        return normalTridas;
    }

    /**
     * Sets the value of the normalTridas property.
     * 
     * @param value
     *     allowed object is
     *     {@link NormalTridasShape }
     *     
     */
    public void setNormalTridas(NormalTridasShape value) {
        this.normalTridas = value;
    }

    public boolean isSetNormalTridas() {
        return (this.normalTridas!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasShape)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final TridasShape that = ((TridasShape) object);
        equalsBuilder.append(this.getNormalTridas(), that.getNormalTridas());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasShape)) {
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
        super.hashCode(hashCodeBuilder);
        hashCodeBuilder.append(this.getNormalTridas());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            NormalTridasShape theNormalTridas;
            theNormalTridas = this.getNormalTridas();
            toStringBuilder.append("normalTridas", theNormalTridas);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasShape copy = ((target == null)?((TridasShape) createCopy()):((TridasShape) target));
        super.copyTo(copy, copyBuilder);
        {
            NormalTridasShape sourceNormalTridas;
            sourceNormalTridas = this.getNormalTridas();
            NormalTridasShape copyNormalTridas = ((NormalTridasShape) copyBuilder.copy(sourceNormalTridas));
            copy.setNormalTridas(copyNormalTridas);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasShape();
    }

}
