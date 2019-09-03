package eu.europa.ec.fisheries.uvms.audit.service;

import eu.europa.ec.fisheries.schema.audit.search.v1.AuditLogListQuery;
import eu.europa.ec.fisheries.schema.audit.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.audit.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.audit.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.audit.source.v1.AuditDataSourceMethod;
import eu.europa.ec.fisheries.schema.audit.source.v1.CreateAuditLogRequest;
import eu.europa.ec.fisheries.schema.audit.source.v1.GetAuditLogListByQueryResponse;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.audit.service.bean.AuditServiceBean;
import eu.europa.ec.fisheries.uvms.audit.util.DateUtil;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.TextMessage;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(Arquillian.class)
public class AuditJMSTests extends BuildAuditServiceTestDeployment {

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Inject
    private AuditServiceBean auditServiceBean;

    private JMSHelper jmsHelper;

    @Before
    public void cleanJMS() {
        jmsHelper = new JMSHelper(connectionFactory);
    }

    @Test
    @OperateOnDeployment("auditservice")
    public void createAuditLog() throws Exception{
        CreateAuditLogRequest request = new CreateAuditLogRequest();
        request.setMethod(AuditDataSourceMethod.CREATE);

        AuditLogType audit = new AuditLogType();
        audit.setAffectedObject(UUID.randomUUID().toString());
        audit.setComment("Test Comment");
        audit.setOperation("Test Operation");
        audit.setUsername("Test User");
        audit.setObjectType("Test Object Type");
        audit.setTimestamp(DateUtil.parseUTCDateToString(Instant.now()));
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
        GetAuditLogListByQueryResponse response = auditServiceBean.getList(query);

        assertEquals(1, response.getAuditLog().size());
        AuditLogType log = response.getAuditLog().get(0);
        assertEquals(audit.getAffectedObject(), log.getAffectedObject());
        assertEquals(audit.getComment(), log.getComment());
        assertEquals(audit.getUsername(), log.getUsername());
        assertEquals(audit.getObjectType(), log.getObjectType());
        assertEquals(audit.getOperation(), log.getOperation());
    }

    @Test
    @OperateOnDeployment("auditservice")
    public void createAuditLogAndGetByUser() throws Exception{
        CreateAuditLogRequest request = new CreateAuditLogRequest();
        request.setMethod(AuditDataSourceMethod.CREATE);

        AuditLogType audit = new AuditLogType();
        audit.setAffectedObject(UUID.randomUUID().toString());
        audit.setComment("Test Comment");
        audit.setOperation("Test Operation");
        audit.setUsername("Test User" + UUID.randomUUID().getLeastSignificantBits());
        audit.setObjectType("Test Object Type");
        audit.setTimestamp(DateUtil.parseUTCDateToString(Instant.now()));
        request.setAuditLog(audit);

        String xml = JAXBMarshaller.marshallJaxBObjectToString(request);
        jmsHelper.sendAuditMessage(xml, AuditDataSourceMethod.CREATE.value());
        Thread.sleep(500);

        AuditLogListQuery query = new AuditLogListQuery();
        query.setPagination(getBasicPagination());
        ListCriteria criteria = new ListCriteria();
        criteria.setKey(SearchKey.USER);
        criteria.setValue(audit.getUsername());
        query.getAuditSearchCriteria().add(criteria);
        GetAuditLogListByQueryResponse response = auditServiceBean.getList(query);

        assertEquals(1, response.getAuditLog().size());
        AuditLogType log = response.getAuditLog().get(0);
        assertEquals(audit.getAffectedObject(), log.getAffectedObject());
    }

    @Test
    public void ping() throws Exception{
        String corrID = jmsHelper.sendAuditMessage("", AuditDataSourceMethod.PING.value());

        TextMessage returnMessage = (TextMessage) jmsHelper.listenForResponse(corrID);

        assertTrue(returnMessage.getText().contains("pong"));
    }

    private static ListPagination getBasicPagination(){
        ListPagination pagination = new ListPagination();
        pagination.setPage(BigInteger.valueOf(1));
        pagination.setListSize(BigInteger.valueOf(100));
        return pagination;
    }
}
