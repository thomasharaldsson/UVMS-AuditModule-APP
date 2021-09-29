/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.audit.service.mapper.search;

import eu.europa.ec.fisheries.schema.audit.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.audit.search.v1.SearchKey;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchFieldMapper {

    private SearchFieldMapper() {}

    private static final Logger LOG = LoggerFactory.getLogger(SearchFieldMapper.class);

    /**
     * Creates a search SQL based on the search fields
     *
     * @param searchFields
     * @param isDynamic
     * @return
     * @throws ParseException
     */
    public static String createSelectSearchSql(List<SearchValue> searchFields, boolean isDynamic) {
        StringBuilder selectBuffer = new StringBuilder();
        selectBuffer.append("SELECT ").append(SearchTables.AUDIT.getTableAlias()).append(" FROM ").append(SearchTables.AUDIT.getTableName())
                .append(" ").append(SearchTables.AUDIT.getTableAlias()).append(" ");
        if (searchFields != null) {
            selectBuffer.append(createSearchSql(searchFields, isDynamic));
        }
        selectBuffer.append(" ORDER BY ").append(SearchTables.AUDIT.getTableAlias()).append(".timestamp DESC");
        LOG.debug("[ SQL: ] " + selectBuffer.toString());
        return selectBuffer.toString();
    }

    /**
     * Creates a JPQL count query based on the search fields. This is used for
     * when paginating lists
     *
     * @param searchFields
     * @param isDynamic
     * @return
     * @throws ParseException
     */
    public static String createCountSearchSql(List<SearchValue> searchFields, boolean isDynamic) {
        StringBuilder countBuffer = new StringBuilder();
        countBuffer.append("SELECT COUNT(").append(SearchTables.AUDIT.getTableAlias()).append(") FROM ").append(SearchTables.AUDIT.getTableName())
                .append(" ").append(SearchTables.AUDIT.getTableAlias()).append(" ");
        if (searchFields != null) {
            countBuffer.append(createSearchSql(searchFields, isDynamic));
        }
        LOG.debug("[ COUNT SQL: ]" + countBuffer.toString());
        return countBuffer.toString();
    }

    /**
     * Created the complete search SQL with joins and sets the values based on
     * the criterias
     *
     * @param criterias
     * @param dynamic
     * @return
     * @throws ParseException
     */
    private static String createSearchSql(List<SearchValue> criterias, boolean dynamic) {
        String OPERATOR = " OR ";
        if (dynamic) {
            OPERATOR = " AND ";
        }
        StringBuilder builder = new StringBuilder();
        HashMap<SearchField, List<SearchValue>> orderedValues = combineSearchFields(criterias);
        if (!orderedValues.isEmpty()) {
            builder.append("WHERE ");
            boolean first = true;
            for (Entry<SearchField, List<SearchValue>> criteria : orderedValues.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    builder.append(OPERATOR);
                }
                if (criteria.getValue().size() == 1) {
                    SearchValue searchValue = criteria.getValue().get(0);
                    builder.append(buildTableAliasname(searchValue.getField())).append(setValueAsType(searchValue));
                } else if (criteria.getValue().size() > 1) {
                    builder.append(buildInSqlStatement(criteria.getValue(), criteria.getKey()));
                }
            }
        }

        return builder.toString();
    }

    /**
     * Creates at String that sets values based on what class the SearchValue
     * has. A String class returns [ = 'value' ] A Integer returns [ = value ]
     * Date is specificaly handled and can return [ >= 'datavalue' ] or [ <=
     * 'datavalue' ]
     *
     * @param entry
     * @return
     * @throws ParseException
     */
    private static String setValueAsType(SearchValue entry) {
        StringBuilder builder = new StringBuilder();
        if (entry.getField().getClazz().isAssignableFrom(Instant.class)) {
            switch (entry.getField()) {
                case FROM_DATE:
                    builder.append(" >= ").append(":fromDate ");
                    break;
                case TO_DATE:
                    builder.append(" <= ").append(":toDate ");
                    break;
                default:
                    builder.append(" = ").append(":date ");
                    break;
            }
        } else {
            builder.append(" = ").append(buildValueFromClassType(entry));
        }

        return builder.toString();
    }

    /**
     * Builds a table alias for the query based on the search field
     * <p>
     * EG [ theTableAlias.theColumnName ]
     *
     * @param field
     * @return
     */
    private static String buildTableAliasname(SearchField field) {
        StringBuilder builder = new StringBuilder();
        builder.append(field.getSearchTables().getTableAlias()).append(".").append(field.getFieldName());
        return builder.toString();
    }

    /**
     * Returns the representation of the value
     * <p>
     * if Integer [ value ] else [ 'value' ]
     *
     * @param entry
     * @return
     */
    private static String buildValueFromClassType(SearchValue entry) {
        StringBuilder builder = new StringBuilder();
        if (entry.getField().getClazz().isAssignableFrom(Integer.class)) {
            builder.append(entry.getValue());
        } else {
            builder.append("'").append(entry.getValue()).append("'");
        }
        return builder.toString();
    }

    /**
     * Builds an IN JPQL representation for lists of values
     * <p>
     * The resulting String = [ mc.value IN ( 'ABC123', 'ABC321' ) ]
     *
     * @param searchValues
     * @param field
     * @return
     */
    private static String buildInSqlStatement(List<SearchValue> searchValues, SearchField field) {
        StringBuilder builder = new StringBuilder();
        builder.append(buildTableAliasname(field));
        builder.append(" IN ( ");
        boolean first = true;
        for (SearchValue searchValue : searchValues) {
            if (first) {
                first = false;
                builder.append(buildValueFromClassType(searchValue));
            } else {
                builder.append(", ").append(buildValueFromClassType(searchValue));
            }
        }
        builder.append(" )");
        return builder.toString();
    }

    /**
     * Takes all the search values and categorizes them in lists to a key
     * according to the SearchField
     *
     * @param searchValues
     * @return
     */
    private static HashMap<SearchField, List<SearchValue>> combineSearchFields(List<SearchValue> searchValues) {
        HashMap<SearchField, List<SearchValue>> values = new HashMap<>();
        for (SearchValue search : searchValues) {
            if (values.containsKey(search.getField())) {
                values.get(search.getField()).add(search);
            } else {
                values.put(search.getField(), new ArrayList<>(Arrays.asList(search)));
            }

        }
        return values;
    }

    /**
     * Converts List<ListCriteria> to List<SearchValue> so that a JPQL query can
     * be built based on the criterias
     *
     * @param listCriterias
     * @return
     */
    public static List<SearchValue> mapSearchField(List<ListCriteria> listCriterias) {

        if (listCriterias == null || listCriterias.isEmpty()) {
            LOG.debug(" Non valid search criteria when mapping ListCriterias to SearchValue, List is null or empty");
            return new ArrayList<>();
        }

        List<SearchValue> searchFields = new ArrayList<>();
        for (ListCriteria criteria : listCriterias) {
            try {
                SearchField field = mapCriteria(criteria.getKey());
                searchFields.add(new SearchValue(field, criteria.getValue()));
            } catch (Exception ex) {
                LOG.debug("[ Error when mapping to search field.. continuing with other criterias. Errormessage: {}  {}]" , ex.getMessage(), ex);
            }
        }

        return searchFields;
    }

    /**
     * Maps the Search Key to a SearchField. All SearchKeys that are not a part
     * of Audit are excluded
     *
     * @param key
     * @return
     */
    private static SearchField mapCriteria(SearchKey key) {
        switch (key) {
            case USER:
                return SearchField.USER;
            case OPERATION:
                return SearchField.OPERATION;
            case TYPE:
                return SearchField.TYPE;
            case TO_DATE:
                return SearchField.TO_DATE;
            case FROM_DATE:
                return SearchField.FROM_DATE;
            default:
                throw new IllegalArgumentException("No field found: " + key.name());
        }
    }

}