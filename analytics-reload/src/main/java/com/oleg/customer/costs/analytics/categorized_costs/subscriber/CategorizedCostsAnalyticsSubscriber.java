package com.oleg.customer.costs.analytics.categorized_costs.subscriber;

import com.oleg.customer.costs.analytics.categorized_costs.command.CategorizedCostAnalyticsCreateCommand;
import com.oleg.customer.costs.analytics.categorized_costs.entity.CategorizedCostsAnalytics;
import com.oleg.customer.costs.analytics.categorized_costs.message.CategorizedCostsAnalyticsMessage;
import com.oleg.customer.costs.analytics.categorized_costs.source.AdminCategorizedCostsAnalyticsSource;
import com.oleg.customer.costs.analytics.core.Message;
import com.oleg.customer.costs.analytics.core.Subscriber;
import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import com.oleg.customer.costs.analytics.customer_costs.source.AdminCustomerCostsSource;
import com.oleg.customer.costs.analytics.period_costs.entity.PeriodCostsAnalytics;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;

@Component
class CategorizedCostsAnalyticsSubscriber implements Subscriber {

    private static final Logger logger = getLogger(CategorizedCostsAnalyticsSubscriber.class);

    private final AdminCustomerCostsSource adminCustomerCostsSource;
    private final AdminCategorizedCostsAnalyticsSource adminCategorizedCostsAnalyticsSource;

    CategorizedCostsAnalyticsSubscriber(AdminCustomerCostsSource adminCustomerCostsSource,
                                        AdminCategorizedCostsAnalyticsSource adminCategorizedCostsAnalyticsSource) {
        this.adminCustomerCostsSource = adminCustomerCostsSource;
        this.adminCategorizedCostsAnalyticsSource = adminCategorizedCostsAnalyticsSource;
    }

    @Override
    @Transactional
    public void onMessage(Message m) {
        if (m instanceof CategorizedCostsAnalyticsMessage message) {
            var customerCostsByCommand = message.customerCosts()
                .stream()
                .collect(groupingBy(CategorizedCostAnalyticsCreateCommand::new, toList()));

            var existed = adminCategorizedCostsAnalyticsSource.get(customerCostsByCommand.keySet());
            var forCreate = customerCostsByCommand.keySet().stream()
                .filter(not(existed::containsKey))
                .collect(toSet());

            var created = adminCategorizedCostsAnalyticsSource.create(forCreate);
            logger.info("Categorized costs analytics created {}.", created.size());

            var forUpdate = forUpdate(
                message.periodCostsAnalyticsById(),
                existed,
                created,
                customerCostsByCommand
            );

            logger.info(
                "Categorized costs analytics updated {}.",
                adminCategorizedCostsAnalyticsSource.update(forUpdate)
            );

            adminCustomerCostsSource.bindCustomerCostsByCategory(message.customerCosts());
        }
    }

    private List<CategorizedCostsAnalytics> forUpdate(Map<Integer, PeriodCostsAnalytics> periodCostsAnalytics,
                                                      Map<CategorizedCostAnalyticsCreateCommand, CategorizedCostsAnalytics> existed,
                                                      Map<CategorizedCostAnalyticsCreateCommand, CategorizedCostsAnalytics> created,
                                                      Map<CategorizedCostAnalyticsCreateCommand, List<CustomerCosts>> customerCostsByCommand) {
        var forUpdate = new ArrayList<CategorizedCostsAnalytics>();

        customerCostsByCommand.forEach((command, customerCosts) -> {
            final CategorizedCostsAnalytics categorizedCostsAnalytics = existed.containsKey(command)
                ? existed.get(command)
                : created.get(command);

            PeriodCostsAnalytics periodCostsAnalytic = periodCostsAnalytics.get(command.periodCostsAnalyticsId());
            customerCosts.forEach(categorizedCostsAnalytics::addCustomerCosts);

            categorizedCostsAnalytics.addPeriodCostsAnalytics(periodCostsAnalytic);
            forUpdate.add(categorizedCostsAnalytics);
        });

        return forUpdate;
    }

    @Override
    public Class<? extends Message> getSupportedMessage() {
        return CategorizedCostsAnalyticsMessage.class;
    }
}