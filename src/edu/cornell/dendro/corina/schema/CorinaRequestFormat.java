
package edu.cornell.dendro.corina.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for corinaRequestFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="corinaRequestFormat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="standard"/>
 *     &lt;enumeration value="summary"/>
 *     &lt;enumeration value="comprehensive"/>
 *     &lt;enumeration value="minimal"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "corinaRequestFormat")
@XmlEnum
public enum CorinaRequestFormat {

    @XmlEnumValue("standard")
    STANDARD("standard"),
    @XmlEnumValue("summary")
    SUMMARY("summary"),
    @XmlEnumValue("comprehensive")
    COMPREHENSIVE("comprehensive"),
    @XmlEnumValue("minimal")
    MINIMAL("minimal");
    private final String value;

    CorinaRequestFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CorinaRequestFormat fromValue(String v) {
        for (CorinaRequestFormat c: CorinaRequestFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
