
package eu.europa.ec.fisheries.schema.audit.source.v1;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="auditLog" type="{urn:audit.schema.fisheries.ec.europa.eu:v1}AuditLogType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="totalNumberOfPages" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="currentPage" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "auditLog",
    "totalNumberOfPages",
    "currentPage"
})
@XmlRootElement(name = "getAuditLogListByQueryResponse")
public class GetAuditLogListByQueryResponse
    implements Serializable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1L;
    protected List<AuditLogType> auditLog;
    @XmlElement(required = true)
    protected BigInteger totalNumberOfPages;
    @XmlElement(required = true)
    protected BigInteger currentPage;

    /**
     * Gets the value of the auditLog property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the auditLog property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuditLog().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuditLogType }
     * 
     * 
     */
    public List<AuditLogType> getAuditLog() {
        if (auditLog == null) {
            auditLog = new ArrayList<AuditLogType>();
        }
        return this.auditLog;
    }

    /**
     * Gets the value of the totalNumberOfPages property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    /**
     * Sets the value of the totalNumberOfPages property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalNumberOfPages(BigInteger value) {
        this.totalNumberOfPages = value;
    }

    /**
     * Gets the value of the currentPage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the value of the currentPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCurrentPage(BigInteger value) {
        this.currentPage = value;
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<AuditLogType> theAuditLog;
            theAuditLog = (((this.auditLog!= null)&&(!this.auditLog.isEmpty()))?this.getAuditLog():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "auditLog", theAuditLog), currentHashCode, theAuditLog);
        }
        {
            BigInteger theTotalNumberOfPages;
            theTotalNumberOfPages = this.getTotalNumberOfPages();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "totalNumberOfPages", theTotalNumberOfPages), currentHashCode, theTotalNumberOfPages);
        }
        {
            BigInteger theCurrentPage;
            theCurrentPage = this.getCurrentPage();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "currentPage", theCurrentPage), currentHashCode, theCurrentPage);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof GetAuditLogListByQueryResponse)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final GetAuditLogListByQueryResponse that = ((GetAuditLogListByQueryResponse) object);
        {
            List<AuditLogType> lhsAuditLog;
            lhsAuditLog = (((this.auditLog!= null)&&(!this.auditLog.isEmpty()))?this.getAuditLog():null);
            List<AuditLogType> rhsAuditLog;
            rhsAuditLog = (((that.auditLog!= null)&&(!that.auditLog.isEmpty()))?that.getAuditLog():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "auditLog", lhsAuditLog), LocatorUtils.property(thatLocator, "auditLog", rhsAuditLog), lhsAuditLog, rhsAuditLog)) {
                return false;
            }
        }
        {
            BigInteger lhsTotalNumberOfPages;
            lhsTotalNumberOfPages = this.getTotalNumberOfPages();
            BigInteger rhsTotalNumberOfPages;
            rhsTotalNumberOfPages = that.getTotalNumberOfPages();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "totalNumberOfPages", lhsTotalNumberOfPages), LocatorUtils.property(thatLocator, "totalNumberOfPages", rhsTotalNumberOfPages), lhsTotalNumberOfPages, rhsTotalNumberOfPages)) {
                return false;
            }
        }
        {
            BigInteger lhsCurrentPage;
            lhsCurrentPage = this.getCurrentPage();
            BigInteger rhsCurrentPage;
            rhsCurrentPage = that.getCurrentPage();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "currentPage", lhsCurrentPage), LocatorUtils.property(thatLocator, "currentPage", rhsCurrentPage), lhsCurrentPage, rhsCurrentPage)) {
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
        {
            List<AuditLogType> theAuditLog;
            theAuditLog = (((this.auditLog!= null)&&(!this.auditLog.isEmpty()))?this.getAuditLog():null);
            strategy.appendField(locator, this, "auditLog", buffer, theAuditLog);
        }
        {
            BigInteger theTotalNumberOfPages;
            theTotalNumberOfPages = this.getTotalNumberOfPages();
            strategy.appendField(locator, this, "totalNumberOfPages", buffer, theTotalNumberOfPages);
        }
        {
            BigInteger theCurrentPage;
            theCurrentPage = this.getCurrentPage();
            strategy.appendField(locator, this, "currentPage", buffer, theCurrentPage);
        }
        return buffer;
    }

}
