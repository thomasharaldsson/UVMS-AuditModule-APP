
package eu.europa.ec.fisheries.schema.audit.search.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SearchKey.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SearchKey"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="USER"/&gt;
 *     &lt;enumeration value="OPERATION"/&gt;
 *     &lt;enumeration value="TYPE"/&gt;
 *     &lt;enumeration value="FROM_DATE"/&gt;
 *     &lt;enumeration value="TO_DATE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SearchKey")
@XmlEnum
public enum SearchKey {

    USER,
    OPERATION,
    TYPE,
    FROM_DATE,
    TO_DATE;

    public String value() {
        return name();
    }

    public static SearchKey fromValue(String v) {
        return valueOf(v);
    }

}
