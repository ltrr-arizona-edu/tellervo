
package org.tridas.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for normalTridasMeasuringMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="normalTridasMeasuringMethod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="measuring platform"/>
 *     &lt;enumeration value="hand lens and graticule"/>
 *     &lt;enumeration value="onscreen measuring"/>
 *     &lt;enumeration value="visual estimate"/>
 *     &lt;enumeration value="other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "normalTridasMeasuringMethod")
@XmlEnum
public enum NormalTridasMeasuringMethod {

    @XmlEnumValue("measuring platform")
    MEASURING___PLATFORM("measuring platform"),
    @XmlEnumValue("hand lens and graticule")
    HAND___LENS___AND___GRATICULE("hand lens and graticule"),
    @XmlEnumValue("onscreen measuring")
    ONSCREEN___MEASURING("onscreen measuring"),
    @XmlEnumValue("visual estimate")
    VISUAL___ESTIMATE("visual estimate"),
    @XmlEnumValue("other")
    OTHER("other");
    private final String value;

    NormalTridasMeasuringMethod(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NormalTridasMeasuringMethod fromValue(String v) {
        for (NormalTridasMeasuringMethod c: NormalTridasMeasuringMethod.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
