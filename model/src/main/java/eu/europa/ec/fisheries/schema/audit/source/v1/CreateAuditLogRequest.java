
package eu.europa.ec.fisheries.schema.audit.source.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:source.audit.schema.fisheries.ec.europa.eu:v1}AuditBaseRequest"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="auditLog" type="{urn:audit.schema.fisheries.ec.europa.eu:v1}AuditLogType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "auditLog"
})
@XmlRootElement(name = "createAuditLogRequest")
public class CreateAuditLogRequest
    extends AuditBaseRequest
    implements Serializable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected AuditLogType auditLog;

    /**
     * Gets the value of the auditLog property.
     * 
     * @return
     *     possible object is
     *     {@link AuditLogType }
     *     
     */
    public AuditLogType getAuditLog() {
        return auditLog;
    }

    /**
     * Sets the value of the auditLog property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuditLogType }
     *     
     */
    public void setAuditLog(AuditLogType value) {
        this.auditLog = value;
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = super.hashCode(locator, strategy);
        {
            AuditLogType theAuditLog;
            theAuditLog = this.getAuditLog();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "auditLog", theAuditLog), currentHashCode, theAuditLog);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof CreateAuditLogRequest)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final CreateAuditLogRequest that = ((CreateAuditLogRequest) object);
        {
            AuditLogType lhsAuditLog;
            lhsAuditLog = this.getAuditLog();
            AuditLogType rhsAuditLog;
            rhsAuditLog = that.getAuditLog();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "auditLog", lhsAuditLog), LocatorUtils.property(thatLocator, "auditLog", rhsAuditLog), lhsAuditLog, rhsAuditLog)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        super.appendFields(locator, buffer, strategy);
        {
            AuditLogType theAuditLog;
            theAuditLog = this.getAuditLog();
            strategy.appendField(locator, this, "auditLog", buffer, theAuditLog);
        }
        return buffer;
    }

}
