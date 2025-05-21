package com.oleg.customer.costs.costs.service;

import com.oleg.customer.costs.common.Paginator;
import com.oleg.customer.costs.costs.query.CustomerCostsQuery;
import com.oleg.customer.costs.costs.source.CustomerCostsCategoryClassifier;
import com.oleg.customer.costs.costs.source.GetCustomerCostsSource;
import com.oleg.customer.costs.costs.query.CreateCustomerCostsCmd;
import com.oleg.customer.costs.costs.source.ManageCustomerCosts;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import com.oleg.customer.costs.monobank.UserAccountsSource;
import com.oleg.customer.costs.user_management.UserContext;
import com.oleg.customer.costs.user_spending.UserSpendingSource;
import com.oleg.customer.costs.user_spending.command.CreateUserSpending;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class CustomerCostsService {

    private final UserContext userContext;
    private final UserSpendingSource userSpendingSource;
    private final UserAccountsSource userAccountsSource;
    private final ManageCustomerCosts manageCustomerCosts;
    private final GetCustomerCostsSource getCustomerCostsSource;
    private final CustomerCostsCategoryClassifier customerCostsCategoryClassifier;

    public CustomerCostsService(UserContext userContext,
                                UserSpendingSource userSpendingSource,
                                UserAccountsSource userAccountsSource,
                                ManageCustomerCosts manageCustomerCosts,
                                GetCustomerCostsSource getCustomerCostsSource,
                                CustomerCostsCategoryClassifier customerCostsCategoryClassifier) {
        this.userContext = userContext;
        this.userSpendingSource = userSpendingSource;
        this.userAccountsSource = userAccountsSource;
        this.manageCustomerCosts = manageCustomerCosts;
        this.getCustomerCostsSource = getCustomerCostsSource;
        this.customerCostsCategoryClassifier = customerCostsCategoryClassifier;
    }

    public int getCustomerCostsCount(int bankId) {
        int userId = userContext.id();
        return getCustomerCostsSource.getCustomerCostsCount(userId, bankId);
    }

    public List<CustomerCostsQuery> getCustomerCosts(int bankId, Paginator paginator) {
        int userId = userContext.id();
        return getCustomerCostsSource.getCustomerCosts(userId, bankId, paginator);
    }

    public void saveCustomerCosts(CreateCustomerCostsCmd cmd) {
        Integer userId = userAccountsSource.getUserIdByAccountNumber(cmd.accountId());

        Integer categoryId = customerCostsCategoryClassifier.classify(cmd);
        CustomerCosts categorizedCosts = new CustomerCosts(
            -1,
            userId,
            cmd.bankId(),
            categoryId,
            cmd.amount(),
            cmd.description(),
            cmd.createdAt(),
            cmd.commissionRate()
        );

        manageCustomerCosts.save(List.of(categorizedCosts));

        BigDecimal currentAmount = currentAmount(List.of(categorizedCosts));

        BigDecimal previousMaxSpending = userSpendingSource.getPreviousMaxSpending(userId, cmd.bankId());
        CreateUserSpending createUserSpending = new CreateUserSpending(
            userId,
            cmd.bankId(),
            previousMaxSpending,
            currentAmount
        );

        userSpendingSource.createUserSpending(createUserSpending);
    }

    private BigDecimal currentAmount(List<CustomerCosts> customerCosts) {
        return customerCosts.stream()
            .filter(this::dateFilter)
            .map(CustomerCosts::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean dateFilter(CustomerCosts customerCosts) {
        YearMonth yearMonth = YearMonth.now();
        LocalDateTime createdAt = customerCosts.createdAt();

        return yearMonth.equals(YearMonth.of(createdAt.getYear(), createdAt.getMonth()));
    }
}