package eu.europa.ec.fisheries.uvms.audit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.schema.audit.search.v1.AuditLogListQuery;
import eu.europa.ec.fisheries.schema.audit.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.audit.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.audit.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.audit.source.v1.AuditDataSourceMethod;
import eu.europa.ec.fisheries.schema.audit.source.v1.CreateAuditLogRequest;
import eu.europa.ec.fisheries.schema.audit.source.v1.GetAuditLogListByQueryResponse;
import eu.europa.ec.fisheries.schema.audit.v1.AuditLogType;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.audit.util.DateUtil;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class AuditRestTests extends BuildAuditRestTestDeployment {

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    JMSHelper jmsHelper;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void cleanJMS() {
        jmsHelper = new JMSHelper(connectionFactory);
    }

    @Test
    public void worldsBestAndMostUsefulRestTest(){
        assertTrue(true);
    }

    @Test
    public void getAuditLogByRestQuery() throws Exception{
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
        System.out.println("Now");
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

    private static ListPagination getBasicPagination(){
        ListPagination pagination = new ListPagination();
        pagination.setPage(BigInteger.valueOf(1));
        pagination.setListSize(BigInteger.valueOf(100));
        return pagination;
    }

    private GetAuditLogListByQueryResponse getAuditListByQuery(AuditLogListQuery query) throws Exception {
        String response = getWebTarget()
                .path("audit")
                .path("list")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .post(Entity.json(query), String.class);

        return readResponseDto(response, GetAuditLogListByQueryResponse.class);
    }


    static <T> T readResponseDto(String response, Class<T> clazz) throws Exception {
        JsonReader jsonReader = Json.createReader(new StringReader(response));
        JsonObject responseDto = jsonReader.readObject();
        JsonObject data = responseDto.getJsonObject("data");
        return objectMapper.readValue(data.toString(), clazz);
    }

    public void checkEquals(AuditLogType original, AuditLogType copy){

        assertEquals(original.getAffectedObject(), copy.getAffectedObject());
        assertEquals(original.getUsername(), copy.getUsername());
        assertEquals(original.getComment(), copy.getComment());
        assertEquals(original.getObjectType(), copy.getObjectType());
        assertEquals(original.getOperation(), copy.getOperation());
    }
}
