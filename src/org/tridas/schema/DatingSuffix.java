
package org.tridas.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for datingSuffix.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="datingSuffix">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AD"/>
 *     &lt;enumeration value="BC"/>
 *     &lt;enumeration value="BP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "datingSuffix")
@XmlEnum
public enum DatingSuffix {

    AD,
    BC,
    BP;

    public String value() {
        return name();
    }

    public static DatingSuffix fromValue(String v) {
        return valueOf(v);
    }

}
