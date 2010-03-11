
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
import org.tridas.adapters.IntegerAdapter;
import org.tridas.interfaces.NormalTridasVoc;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.tridas.org/1.2.1>controlledVoc">
 *       &lt;attribute name="normalTridas" type="{http://www.tridas.org/1.2.1}normalTridasRemark" />
 *       &lt;attribute name="inheritedCount" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "remark")
public class TridasRemark
    extends ControlledVoc
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString, NormalTridasVoc<NormalTridasRemark>
{

    private final static long serialVersionUID = 1001L;
    @XmlAttribute(name = "normalTridas")
    protected NormalTridasRemark normalTridas;
    @XmlAttribute(name = "inheritedCount")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer inheritedCount;

    /**
     * Gets the value of the normalTridas property.
     * 
     * @return
     *     possible object is
     *     {@link NormalTridasRemark }
     *     
     */
    public NormalTridasRemark getNormalTridas() {
        return normalTridas;
    }

    /**
     * Sets the value of the normalTridas property.
     * 
     * @param value
     *     allowed object is
     *     {@link NormalTridasRemark }
     *     
     */
    public void setNormalTridas(NormalTridasRemark value) {
        this.normalTridas = value;
    }

    public boolean isSetNormalTridas() {
        return (this.normalTridas!= null);
    }

    /**
     * Gets the value of the inheritedCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getInheritedCount() {
        return inheritedCount;
    }

    /**
     * Sets the value of the inheritedCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInheritedCount(Integer value) {
        this.inheritedCount = value;
    }

    public boolean isSetInheritedCount() {
        return (this.inheritedCount!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasRemark)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final TridasRemark that = ((TridasRemark) object);
        equalsBuilder.append(this.getNormalTridas(), that.getNormalTridas());
        equalsBuilder.append(this.getInheritedCount(), that.getInheritedCount());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasRemark)) {
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
        hashCodeBuilder.append(this.getInheritedCount());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            NormalTridasRemark theNormalTridas;
            theNormalTridas = this.getNormalTridas();
            toStringBuilder.append("normalTridas", theNormalTridas);
        }
        {
            Integer theInheritedCount;
            theInheritedCount = this.getInheritedCount();
            toStringBuilder.append("inheritedCount", theInheritedCount);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasRemark copy = ((target == null)?((TridasRemark) createCopy()):((TridasRemark) target));
        super.copyTo(copy, copyBuilder);
        if (this.isSetNormalTridas()) {
            NormalTridasRemark sourceNormalTridas;
            sourceNormalTridas = this.getNormalTridas();
            NormalTridasRemark copyNormalTridas = ((NormalTridasRemark) copyBuilder.copy(sourceNormalTridas));
            copy.setNormalTridas(copyNormalTridas);
        } else {
            copy.normalTridas = null;
        }
        if (this.isSetInheritedCount()) {
            Integer sourceInheritedCount;
            sourceInheritedCount = this.getInheritedCount();
            Integer copyInheritedCount = ((Integer) copyBuilder.copy(sourceInheritedCount));
            copy.setInheritedCount(copyInheritedCount);
        } else {
            copy.inheritedCount = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasRemark();
    }

}
