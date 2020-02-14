package eu.europa.ec.fisheries.uvms.audit.rest;

import eu.europa.ec.fisheries.schema.audit.search.v1.AuditLogListQuery;
import eu.europa.ec.fisheries.schema.audit.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.audit.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.audit.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.audit.source.v1.AuditDataSourceMethod;
import eu.europa.ec.fisheries.schema.audit.source.v1.CreateAuditLogRequest;
import eu.europa.ec.fisheries.schema.audit.source.v1.GetAuditLogListByQueryResponse;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class AuditRestTests extends BuildAuditRestTestDeployment {

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    private JMSHelper jmsHelper;

    @Before
    public void cleanJMS() {
        jmsHelper = new JMSHelper(connectionFactory);
    }

    @Test
    public void getAuditLogByRestQuery() throws Exception {
        CreateAuditLogRequest request = new CreateAuditLogRequest();
        request.setMethod(AuditDataSourceMethod.CREATE);

        AuditLogType audit = getBasicAuditLog();
        request.setAuditLog(audit);

        String xml = JAXBMarshaller.marshallJaxBObjectToString(request);
        jmsHelper.sendAuditMessage(xml, AuditDataSourceMethod.CREATE.value());
        Thread.sleep(500);

        AuditLogListQuery query = new AuditLogListQuery();
        query.setPagination(getBasicPagination());
        ListCriteria criteria = new ListCriteria();
        criteria.setKey(SearchKey.FROM_DATE);
        criteria.setValue(audit.getTimestamp());
        query.getAuditSearchCriteria().add(criteria);

        GetAuditLogListByQueryResponse response = getAuditListByQuery(query);
        assertEquals(1, response.getAuditLog().size());
        checkEquals(audit, response.getAuditLog().get(0));
    }

    @Test
    public void getAuditLogByRestQueryByOperation() throws Exception {
        CreateAuditLogRequest request = new CreateAuditLogRequest();
        request.setMethod(AuditDataSourceMethod.CREATE);

        AuditLogType audit = getBasicAuditLog();
        audit.setOperation(audit.getOperation() + UUID.fromString(audit.getAffectedObject()).getLeastSignificantBits());
        request.setAuditLog(audit);

        String xml = JAXBMarshaller.marshallJaxBObjectToString(request);
        jmsHelper.sendAuditMessage(xml, AuditDataSourceMethod.CREATE.value());
        Thread.sleep(500);

        AuditLogListQuery query = new AuditLogListQuery();
        query.setPagination(getBasicPagination());
        ListCriteria criteria = new ListCriteria();
        criteria.setKey(SearchKey.OPERATION);
        criteria.setValue(audit.getOperation());
        query.getAuditSearchCriteria().add(criteria);

        GetAuditLogListByQueryResponse response = getAuditListByQuery(query);
        assertEquals(1, response.getAuditLog().size());
        checkEquals(audit, response.getAuditLog().get(0));
    }

    @Test
    public void getSeveralAuditLogByRestQueryByUserAndTimestamp() throws Exception {
        CreateAuditLogRequest request = new CreateAuditLogRequest();
        request.setMethod(AuditDataSourceMethod.CREATE);

        Instant timestamp = Instant.now();
        String username = "";

        for (int i = 0; i < 10; i++) {
            AuditLogType audit = getBasicAuditLog();
            username = audit.getUsername();
            request.setAuditLog(audit);

            String xml = JAXBMarshaller.marshallJaxBObjectToString(request);
            jmsHelper.sendAuditMessage(xml, AuditDataSourceMethod.CREATE.value());
        }
        Thread.sleep(500);

        AuditLogListQuery query = new AuditLogListQuery();
        query.setPagination(getBasicPagination());
        ListCriteria criteria = new ListCriteria();
        criteria.setKey(SearchKey.USER);
        criteria.setValue(username);
        query.getAuditSearchCriteria().add(criteria);

        criteria = new ListCriteria();
        criteria.setKey(SearchKey.FROM_DATE);
        criteria.setValue(DateUtils.dateToEpochMilliseconds(timestamp));
        query.getAuditSearchCriteria().add(criteria);

        GetAuditLogListByQueryResponse response = getAuditListByQuery(query);
        assertEquals(10, response.getAuditLog().size());
    }

    private static AuditLogType getBasicAuditLog() {
        AuditLogType audit = new AuditLogType();
        audit.setAffectedObject(UUID.randomUUID().toString());
        audit.setComment("Test Comment");
        audit.setOperation("Test Operation");
        audit.setUsername("Test User");
        audit.setObjectType("Test Object Type");
        audit.setTimestamp(DateUtils.dateToEpochMilliseconds(Instant.now()));
        return audit;
    }

    private static ListPagination getBasicPagination() {
        ListPagination pagination = new ListPagination();
        pagination.setPage(BigInteger.valueOf(1));
        pagination.setListSize(BigInteger.valueOf(100));
        return pagination;
    }

    private GetAuditLogListByQueryResponse getAuditListByQuery(AuditLogListQuery query) {
        return getWebTarget()
                .path("audit")
                .path("list")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .post(Entity.json(query), GetAuditLogListByQueryResponse.class);
    }

    public void checkEquals(AuditLogType original, AuditLogType copy) {
        assertEquals(original.getAffectedObject(), copy.getAffectedObject());
        assertEquals(original.getUsername(), copy.getUsername());
        assertEquals(original.getComment(), copy.getComment());
        assertEquals(original.getObjectType(), copy.getObjectType());
        assertEquals(original.getOperation(), copy.getOperation());
    }
}
