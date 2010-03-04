
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
 *     &lt;enumeration value="Ring width"/>
 *     &lt;enumeration value="Earlywood width"/>
 *     &lt;enumeration value="Latewood width"/>
 *     &lt;enumeration value="Ring density"/>
 *     &lt;enumeration value="Earlywood density"/>
 *     &lt;enumeration value="Latewood density"/>
 *     &lt;enumeration value="Maximum density"/>
 *     &lt;enumeration value="Latewood percent"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "normalTridasVariable")
@XmlEnum
public enum NormalTridasVariable {

    @XmlEnumValue("Ring width")
    RING_WIDTH("Ring width"),
    @XmlEnumValue("Earlywood width")
    EARLYWOOD_WIDTH("Earlywood width"),
    @XmlEnumValue("Latewood width")
    LATEWOOD_WIDTH("Latewood width"),
    @XmlEnumValue("Ring density")
    RING_DENSITY("Ring density"),
    @XmlEnumValue("Earlywood density")
    EARLYWOOD_DENSITY("Earlywood density"),
    @XmlEnumValue("Latewood density")
    LATEWOOD_DENSITY("Latewood density"),
    @XmlEnumValue("Maximum density")
    MAXIMUM_DENSITY("Maximum density"),
    @XmlEnumValue("Latewood percent")
    LATEWOOD_PERCENT("Latewood percent");
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
