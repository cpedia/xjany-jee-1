package com.lti.util;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.OrderEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.Assert;

import com.lti.type.PaginationSupport;


public abstract class HibernateUtils {

    private static Log logger = LogFactory.getLog(HibernateUtils.class);

    public static String removeSelect(String sql) {
        Assert.notNull(sql, "sql must be specified ");
        int beginPos = sql.toLowerCase().indexOf("from");
        Assert.isTrue(beginPos != -1, " the sql : " + sql + " must has a keyword 'from'");
        return sql.substring(beginPos);
    }

    public static String removeOrders(String sql) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static int getTotalCount(HibernateTemplate hibernateTemplate, String queryString, boolean isNamedQuery,
            String[] paramNames, Object[] paramValues) throws IllegalArgumentException, DataAccessException {
        if (StringUtils.isBlank(queryString)) {
            throw new IllegalArgumentException(" queryString can't be blank ");
        }
        String countQueryString = " select count (*) " + removeSelect(removeOrders(queryString));
        List countList;
        if (isNamedQuery) {
            countList = hibernateTemplate.findByNamedParam(countQueryString, paramNames, paramValues);
        } else {
            countList = hibernateTemplate.find(countQueryString, paramValues);
        }
        return ((Integer) countList.get(0)).intValue();
    }

    public static List getPageResult(Criteria criteria, int offset, int maxPageItems) throws HibernateException {
        criteria.setFirstResult(offset);
        criteria.setMaxResults(maxPageItems);
        return criteria.list();
    }

    public static PaginationSupport findByCriteria(HibernateTemplate hibernateTemplate,
            final DetachedCriteria criteria, final int firstResult, final int maxResults) throws DataAccessException {

        return (PaginationSupport) hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria executableCriteria = criteria.getExecutableCriteria(session);

                // Get the orginal orderEntries
                OrderEntry[] orderEntries = HibernateUtils.getOrders(executableCriteria);
                // Remove the orders
                executableCriteria = HibernateUtils.removeOrders(executableCriteria);
                // get the original projection
                Projection projection = HibernateUtils.getProjection(executableCriteria);

                int totalCount = ((Integer) executableCriteria.setProjection(Projections.rowCount()).uniqueResult())
                        .intValue();

                executableCriteria.setProjection(projection);
                if (projection == null) {
                    // Set the ResultTransformer to get the same object
                    // structure with hql
                    executableCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
                }
                // Add the orginal orderEntries
                executableCriteria = HibernateUtils.addOrders(executableCriteria, orderEntries);

                // Now, the Projection and the orderEntries have been resumed
                List items = HibernateUtils.getPageResult(executableCriteria, firstResult, maxResults);
                return new PaginationSupport(items, totalCount, firstResult, maxResults);
            }
        }, true);
    }

    public static Projection getProjection(Criteria criteria) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        return impl.getProjection();
    }

    public static OrderEntry[] getOrders(Criteria criteria) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        Field field = getOrderEntriesField(criteria);
        try {
            return (OrderEntry[]) ((List) field.get(impl)).toArray(new OrderEntry[0]);
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility can't throw ");
        }
    }

    public static Criteria removeOrders(Criteria criteria) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        try {
            Field field = getOrderEntriesField(criteria);
            field.set(impl, new ArrayList());
            return impl;
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility can't throw ");
        }
    }

    public static Criteria addOrders(Criteria criteria, OrderEntry[] orderEntries) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        try {
            Field field = getOrderEntriesField(criteria);
            for (int i = 0; i < orderEntries.length; i++) {
                List innerOrderEntries = (List) field.get(criteria);
                innerOrderEntries.add(orderEntries[i]);
            }
            return impl;
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility can't throw ");
        }
    }

    private static Field getOrderEntriesField(Criteria criteria) {
        Assert.notNull(criteria, " criteria is requried. ");
        try {
            Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            throw new InternalError();
        }
    }

} 