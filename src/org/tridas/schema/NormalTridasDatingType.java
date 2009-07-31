
package org.tridas.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for normalTridasDatingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="normalTridasDatingType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Absolute"/>
 *     &lt;enumeration value="Dated with uncertainty"/>
 *     &lt;enumeration value="Relative"/>
 *     &lt;enumeration value="Radiocarbon"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "normalTridasDatingType")
@XmlEnum
public enum NormalTridasDatingType {

    @XmlEnumValue("Absolute")
    ABSOLUTE("Absolute"),
    @XmlEnumValue("Dated with uncertainty")
    DATED___WITH___UNCERTAINTY("Dated with uncertainty"),
    @XmlEnumValue("Relative")
    RELATIVE("Relative"),
    @XmlEnumValue("Radiocarbon")
    RADIOCARBON("Radiocarbon");
    private final String value;

    NormalTridasDatingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NormalTridasDatingType fromValue(String v) {
        for (NormalTridasDatingType c: NormalTridasDatingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
