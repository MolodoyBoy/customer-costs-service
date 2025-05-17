package com.oleg.customer.costs.costs;

import com.oleg.customer.costs.common.Paginator;
import com.oleg.customer.costs.costs.source.GetCustomerCostsSource;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import com.oleg.customer.costs.user_management.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCostsService {

    private final UserContext userContext;
    private final GetCustomerCostsSource getCustomerCostsSource;

    public CustomerCostsService(UserContext userContext,
                                GetCustomerCostsSource getCustomerCostsSource) {
        this.userContext = userContext;
        this.getCustomerCostsSource = getCustomerCostsSource;
    }

    public int getCustomerCostsCount(int bankId) {
        int userId = userContext.id();
        return getCustomerCostsSource.getCustomerCostsCount(userId, bankId);
    }

    public List<CustomerCosts> getCustomerCosts(int bankId, Paginator paginator) {
        int userId = userContext.id();
        return getCustomerCostsSource.getCustomerCosts(userId, bankId, paginator);
    }
}