
package org.tridas.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for complexPresenceAbsence.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="complexPresenceAbsence">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="unknown"/>
 *     &lt;enumeration value="not applicable"/>
 *     &lt;enumeration value="absent"/>
 *     &lt;enumeration value="complete"/>
 *     &lt;enumeration value="incomplete"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "complexPresenceAbsence")
@XmlEnum
public enum ComplexPresenceAbsence {

    @XmlEnumValue("unknown")
    UNKNOWN("unknown"),
    @XmlEnumValue("not applicable")
    NOT___APPLICABLE("not applicable"),
    @XmlEnumValue("absent")
    ABSENT("absent"),
    @XmlEnumValue("complete")
    COMPLETE("complete"),
    @XmlEnumValue("incomplete")
    INCOMPLETE("incomplete");
    private final String value;

    ComplexPresenceAbsence(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ComplexPresenceAbsence fromValue(String v) {
        for (ComplexPresenceAbsence c: ComplexPresenceAbsence.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
