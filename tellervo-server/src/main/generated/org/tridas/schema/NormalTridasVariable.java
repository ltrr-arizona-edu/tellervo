
package org.tridas.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for normalTridasVariable.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="normalTridasVariable">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ring width"/>
 *     &lt;enumeration value="earlywood width"/>
 *     &lt;enumeration value="latewood width"/>
 *     &lt;enumeration value="ring density"/>
 *     &lt;enumeration value="earlywood density"/>
 *     &lt;enumeration value="latewood density"/>
 *     &lt;enumeration value="maximum density"/>
 *     &lt;enumeration value="latewood percent"/>
 *     &lt;enumeration value="vessel size"/>
 *     &lt;enumeration value="other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "normalTridasVariable")
@XmlEnum
public enum NormalTridasVariable {

    @XmlEnumValue("ring width")
    RING___WIDTH("ring width"),
    @XmlEnumValue("earlywood width")
    EARLYWOOD___WIDTH("earlywood width"),
    @XmlEnumValue("latewood width")
    LATEWOOD___WIDTH("latewood width"),
    @XmlEnumValue("ring density")
    RING___DENSITY("ring density"),
    @XmlEnumValue("earlywood density")
    EARLYWOOD___DENSITY("earlywood density"),
    @XmlEnumValue("latewood density")
    LATEWOOD___DENSITY("latewood density"),
    @XmlEnumValue("maximum density")
    MAXIMUM___DENSITY("maximum density"),
    @XmlEnumValue("latewood percent")
    LATEWOOD___PERCENT("latewood percent"),
    @XmlEnumValue("vessel size")
    VESSEL___SIZE("vessel size"),
    @XmlEnumValue("other")
    OTHER("other");
    private final String value;

    NormalTridasVariable(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NormalTridasVariable fromValue(String v) {
        for (NormalTridasVariable c: NormalTridasVariable.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
