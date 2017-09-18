
package eu.europa.ec.fisheries.schema.audit.search.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
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
 * <p>Java class for AuditLogListQuery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AuditLogListQuery"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pagination" type="{urn:search.audit.schema.fisheries.ec.europa.eu:v1}ListPagination"/&gt;
 *         &lt;element name="auditSearchCriteria" type="{urn:search.audit.schema.fisheries.ec.europa.eu:v1}ListCriteria" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuditLogListQuery", propOrder = {
    "pagination",
    "auditSearchCriteria"
})
public class AuditLogListQuery
    implements Serializable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected ListPagination pagination;
    @XmlElement(required = true)
    protected List<ListCriteria> auditSearchCriteria;

    /**
     * Gets the value of the pagination property.
     * 
     * @return
     *     possible object is
     *     {@link ListPagination }
     *     
     */
    public ListPagination getPagination() {
        return pagination;
    }

    /**
     * Sets the value of the pagination property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListPagination }
     *     
     */
    public void setPagination(ListPagination value) {
        this.pagination = value;
    }

    /**
     * Gets the value of the auditSearchCriteria property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the auditSearchCriteria property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuditSearchCriteria().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ListCriteria }
     * 
     * 
     */
    public List<ListCriteria> getAuditSearchCriteria() {
        if (auditSearchCriteria == null) {
            auditSearchCriteria = new ArrayList<ListCriteria>();
        }
        return this.auditSearchCriteria;
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            ListPagination thePagination;
            thePagination = this.getPagination();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "pagination", thePagination), currentHashCode, thePagination);
        }
        {
            List<ListCriteria> theAuditSearchCriteria;
            theAuditSearchCriteria = (((this.auditSearchCriteria!= null)&&(!this.auditSearchCriteria.isEmpty()))?this.getAuditSearchCriteria():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "auditSearchCriteria", theAuditSearchCriteria), currentHashCode, theAuditSearchCriteria);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof AuditLogListQuery)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final AuditLogListQuery that = ((AuditLogListQuery) object);
        {
            ListPagination lhsPagination;
            lhsPagination = this.getPagination();
            ListPagination rhsPagination;
            rhsPagination = that.getPagination();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "pagination", lhsPagination), LocatorUtils.property(thatLocator, "pagination", rhsPagination), lhsPagination, rhsPagination)) {
                return false;
            }
        }
        {
            List<ListCriteria> lhsAuditSearchCriteria;
            lhsAuditSearchCriteria = (((this.auditSearchCriteria!= null)&&(!this.auditSearchCriteria.isEmpty()))?this.getAuditSearchCriteria():null);
            List<ListCriteria> rhsAuditSearchCriteria;
            rhsAuditSearchCriteria = (((that.auditSearchCriteria!= null)&&(!that.auditSearchCriteria.isEmpty()))?that.getAuditSearchCriteria():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "auditSearchCriteria", lhsAuditSearchCriteria), LocatorUtils.property(thatLocator, "auditSearchCriteria", rhsAuditSearchCriteria), lhsAuditSearchCriteria, rhsAuditSearchCriteria)) {
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
            ListPagination thePagination;
            thePagination = this.getPagination();
            strategy.appendField(locator, this, "pagination", buffer, thePagination);
        }
        {
            List<ListCriteria> theAuditSearchCriteria;
            theAuditSearchCriteria = (((this.auditSearchCriteria!= null)&&(!this.auditSearchCriteria.isEmpty()))?this.getAuditSearchCriteria():null);
            strategy.appendField(locator, this, "auditSearchCriteria", buffer, theAuditSearchCriteria);
        }
        return buffer;
    }

}
