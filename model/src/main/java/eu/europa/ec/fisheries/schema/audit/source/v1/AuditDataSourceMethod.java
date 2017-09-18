
package eu.europa.ec.fisheries.schema.audit.source.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuditDataSourceMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AuditDataSourceMethod"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AUDITLOG_LIST"/&gt;
 *     &lt;enumeration value="CREATE"/&gt;
 *     &lt;enumeration value="PING"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AuditDataSourceMethod")
@XmlEnum
public enum AuditDataSourceMethod {

    AUDITLOG_LIST,
    CREATE,
    PING;

    public String value() {
        return name();
    }

    public static AuditDataSourceMethod fromValue(String v) {
        return valueOf(v);
    }

}
