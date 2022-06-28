
package net.opengis.gml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurveInterpolationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CurveInterpolationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="linear"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CurveInterpolationType")
@XmlEnum
public enum CurveInterpolationType {

    @XmlEnumValue("linear")
    LINEAR("linear");
    private final String value;

    CurveInterpolationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CurveInterpolationType fromValue(String v) {
        for (CurveInterpolationType c: CurveInterpolationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
