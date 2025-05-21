package com.oleg.customer.costs.costs;

import com.oleg.customer.costs.common.Paginator;
import com.oleg.customer.costs.costs.mapper.CustomerCostsMapper;
import com.oleg.customer.costs.costs.mapper.CustomerCostsQueryMapper;
import com.oleg.customer.costs.costs.query.CustomerCostsQuery;
import com.oleg.customer.costs.costs.source.GetCustomerCostsSource;
import com.oleg.customer.costs.costs.source.ManageCustomerCosts;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.List;
import java.util.Collection;

import static org.jooq.impl.DSL.count;
import static com.oleg.fund.customer.costs.analytics.tables.CostsCategory.COSTS_CATEGORY;
import static com.oleg.fund.customer.costs.analytics.tables.CustomerCosts.CUSTOMER_COSTS;

@Repository
class DbCustomerCostsSource implements ManageCustomerCosts, GetCustomerCostsSource {

    private final DSLContext dslContext;
    private final CustomerCostsMapper customerCostsMapper;
    private final CustomerCostsQueryMapper customerCostsQueryMapper;
    private final DbCustomerCostsEventSource dbCustomerCostsEventSource;

    DbCustomerCostsSource(DSLContext dslContext,
                          CustomerCostsMapper customerCostsMapper,
                          CustomerCostsQueryMapper customerCostsQueryMapper,
                          DbCustomerCostsEventSource dbCustomerCostsEventSource) {
        this.dslContext = dslContext;
        this.customerCostsMapper = customerCostsMapper;
        this.customerCostsQueryMapper = customerCostsQueryMapper;
        this.dbCustomerCostsEventSource = dbCustomerCostsEventSource;
    }

    @Override
    @Transactional
    public void save(Collection<CustomerCosts> customerCosts) {
        List<Record> records = customerCostsMapper.toRecords(customerCosts);
        Set<Integer> ids = dslContext.insertInto(CUSTOMER_COSTS)
            .set(records)
            .returning(CUSTOMER_COSTS.ID)
            .fetchSet(CUSTOMER_COSTS.ID);

        dbCustomerCostsEventSource.insert(ids);
    }

    @Override
    public int getCustomerCostsCount(int userId, int bankId) {
        var countField = count(CUSTOMER_COSTS.ID);
        return dslContext.select(countField)
            .from(CUSTOMER_COSTS)
            .where(CUSTOMER_COSTS.USER_ID.eq(userId).and(CUSTOMER_COSTS.BANK_ID.eq(bankId)))
            .fetch(countField)
            .stream()
            .findFirst()
            .orElse(0);
    }

    @Override
    public List<CustomerCosts> getCustomerCosts(Set<Integer> ids) {
        return dslContext.select()
            .from(CUSTOMER_COSTS)
            .where(CUSTOMER_COSTS.ID.in(ids))
            .fetch(customerCostsMapper);
    }

    @Override
    public List<CustomerCostsQuery> getCustomerCosts(int userId, int bankId, Paginator paginator) {
        var orderByStep = dslContext.select()
            .from(CUSTOMER_COSTS)
            .innerJoin(COSTS_CATEGORY).on(COSTS_CATEGORY.ID.eq(CUSTOMER_COSTS.CATEGORY_ID))
            .where(CUSTOMER_COSTS.USER_ID.eq(userId).and(CUSTOMER_COSTS.BANK_ID.eq(bankId)))
            .orderBy(CUSTOMER_COSTS.CREATED_AT.desc());

        if (paginator != null) {
            return orderByStep.offset(paginator.skip())
                .limit(paginator.limit())
                .fetch(customerCostsQueryMapper);
        }

        return orderByStep.fetch(customerCostsQueryMapper);
    }
}