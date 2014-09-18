
package org.tridas.schema;

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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}project" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}vocabulary" minOccurs="0"/>
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
    "projects",
    "vocabulary"
})
@XmlRootElement(name = "tridas")
public class TridasTridas
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "project")
    protected List<TridasProject> projects;
    protected TridasVocabulary vocabulary;

    /**
     * Gets the value of the projects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the projects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProjects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasProject }
     * 
     * 
     */
    public List<TridasProject> getProjects() {
        if (projects == null) {
            projects = new ArrayList<TridasProject>();
        }
        return this.projects;
    }

    public boolean isSetProjects() {
        return ((this.projects!= null)&&(!this.projects.isEmpty()));
    }

    public void unsetProjects() {
        this.projects = null;
    }

    /**
     * Gets the value of the vocabulary property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary }
     *     
     */
    public TridasVocabulary getVocabulary() {
        return vocabulary;
    }

    /**
     * Sets the value of the vocabulary property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary }
     *     
     */
    public void setVocabulary(TridasVocabulary value) {
        this.vocabulary = value;
    }

    public boolean isSetVocabulary() {
        return (this.vocabulary!= null);
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
            List<TridasProject> theProjects;
            theProjects = (this.isSetProjects()?this.getProjects():null);
            strategy.appendField(locator, this, "projects", buffer, theProjects);
        }
        {
            TridasVocabulary theVocabulary;
            theVocabulary = this.getVocabulary();
            strategy.appendField(locator, this, "vocabulary", buffer, theVocabulary);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasTridas)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasTridas that = ((TridasTridas) object);
        {
            List<TridasProject> lhsProjects;
            lhsProjects = (this.isSetProjects()?this.getProjects():null);
            List<TridasProject> rhsProjects;
            rhsProjects = (that.isSetProjects()?that.getProjects():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "projects", lhsProjects), LocatorUtils.property(thatLocator, "projects", rhsProjects), lhsProjects, rhsProjects)) {
                return false;
            }
        }
        {
            TridasVocabulary lhsVocabulary;
            lhsVocabulary = this.getVocabulary();
            TridasVocabulary rhsVocabulary;
            rhsVocabulary = that.getVocabulary();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "vocabulary", lhsVocabulary), LocatorUtils.property(thatLocator, "vocabulary", rhsVocabulary), lhsVocabulary, rhsVocabulary)) {
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
            List<TridasProject> theProjects;
            theProjects = (this.isSetProjects()?this.getProjects():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "projects", theProjects), currentHashCode, theProjects);
        }
        {
            TridasVocabulary theVocabulary;
            theVocabulary = this.getVocabulary();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "vocabulary", theVocabulary), currentHashCode, theVocabulary);
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
        if (draftCopy instanceof TridasTridas) {
            final TridasTridas copy = ((TridasTridas) draftCopy);
            if (this.isSetProjects()) {
                List<TridasProject> sourceProjects;
                sourceProjects = (this.isSetProjects()?this.getProjects():null);
                @SuppressWarnings("unchecked")
                List<TridasProject> copyProjects = ((List<TridasProject> ) strategy.copy(LocatorUtils.property(locator, "projects", sourceProjects), sourceProjects));
                copy.unsetProjects();
                List<TridasProject> uniqueProjectsl = copy.getProjects();
                uniqueProjectsl.addAll(copyProjects);
            } else {
                copy.unsetProjects();
            }
            if (this.isSetVocabulary()) {
                TridasVocabulary sourceVocabulary;
                sourceVocabulary = this.getVocabulary();
                TridasVocabulary copyVocabulary = ((TridasVocabulary) strategy.copy(LocatorUtils.property(locator, "vocabulary", sourceVocabulary), sourceVocabulary));
                copy.setVocabulary(copyVocabulary);
            } else {
                copy.vocabulary = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasTridas();
    }

    /**
     * Sets the value of the projects property.
     * 
     * @param projects
     *     allowed object is
     *     {@link TridasProject }
     *     
     */
    public void setProjects(List<TridasProject> projects) {
        this.projects = projects;
    }

}
