package com.oleg.customer.costs.analytics.period_costs.subscriber;

import com.oleg.customer.costs.analytics.categorized_costs.message.CategorizedCostsAnalyticsMessage;
import com.oleg.customer.costs.analytics.core.Message;
import com.oleg.customer.costs.analytics.core.Publisher;
import com.oleg.customer.costs.analytics.core.Subscriber;
import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import com.oleg.customer.costs.analytics.customer_costs.source.AdminCustomerCostsSource;
import com.oleg.customer.costs.analytics.period_costs.command.PeriodCostAnalyticsCreateCommand;
import com.oleg.customer.costs.analytics.period_costs.entity.PeriodCostsAnalytics;
import com.oleg.customer.costs.analytics.period_costs.message.PeriodCostsAnalyticsMessage;
import com.oleg.customer.costs.analytics.period_costs.source.AdminPeriodCostsAnalyticsSource;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;

@Component
class PeriodCostsAnalyticsSubscriber implements Subscriber {

    private static final Logger logger = getLogger(PeriodCostsAnalyticsSubscriber.class);

    private final Publisher publisher;
    private final AdminCustomerCostsSource adminCustomerCostsSource;
    private final AdminPeriodCostsAnalyticsSource adminPeriodCostsAnalyticsSource;

    PeriodCostsAnalyticsSubscriber(@Lazy Publisher publisher,
                                   AdminCustomerCostsSource adminCustomerCostsSource,
                                   AdminPeriodCostsAnalyticsSource adminPeriodCostsAnalyticsSource) {
        this.publisher = publisher;
        this.adminCustomerCostsSource = adminCustomerCostsSource;
        this.adminPeriodCostsAnalyticsSource = adminPeriodCostsAnalyticsSource;
    }

    @Override
    @Transactional
    public void onMessage(Message m) {
        if (m instanceof PeriodCostsAnalyticsMessage message) {
            var customerCostsByCommand = message.customerCosts()
                .stream()
                .collect(groupingBy(PeriodCostAnalyticsCreateCommand::new, toList()));

            var existed = adminPeriodCostsAnalyticsSource.get(customerCostsByCommand.keySet());
            var forCreate = customerCostsByCommand.keySet()
                .stream()
                .filter(not(existed::containsKey))
                .collect(toSet());

            var created = adminPeriodCostsAnalyticsSource.create(forCreate);
            logger.info("Period costs analytics created {}.", created.size());

            var forUpdate = forUpdate(existed, created, customerCostsByCommand);

            logger.info(
                "Period costs analytics updated {}.",
                adminPeriodCostsAnalyticsSource.update(forUpdate.values())
            );

            adminCustomerCostsSource.bindCustomerCostsByPeriod(message.customerCosts());

            publisher.publishMessage(
                new CategorizedCostsAnalyticsMessage(message.customerCosts(), forUpdate)
            );
        }
    }

    private Map<Integer, PeriodCostsAnalytics> forUpdate(Map<PeriodCostAnalyticsCreateCommand, PeriodCostsAnalytics> existed,
                                                         Map<PeriodCostAnalyticsCreateCommand, PeriodCostsAnalytics> created,
                                                         Map<PeriodCostAnalyticsCreateCommand, List<CustomerCosts>> customerCostsByCommand) {
        var forUpdate = new HashMap<Integer, PeriodCostsAnalytics>();

        var previousAnalytics = getPreviousAnalytics(created.keySet());
        customerCostsByCommand.forEach((command, customerCosts) -> {
            final PeriodCostsAnalytics periodCostsAnalytics;

            if (existed.containsKey(command)) {
                periodCostsAnalytics = existed.get(command);
            } else {
                periodCostsAnalytics = created.get(command);

                PeriodCostsAnalytics previousAnalytic = previousAnalytics.get(command.shiftMonthBack());
                periodCostsAnalytics.addPrevious(previousAnalytic);
            }

            customerCosts.forEach(periodCostsAnalytics::addCustomerCosts);

            forUpdate.put(periodCostsAnalytics.id(), periodCostsAnalytics);
        });

        return forUpdate;
    }

    private Map<PeriodCostAnalyticsCreateCommand, PeriodCostsAnalytics> getPreviousAnalytics(Set<PeriodCostAnalyticsCreateCommand> commands) {
        var previousCommands = commands.stream()
            .map(PeriodCostAnalyticsCreateCommand::shiftMonthBack)
            .toList();

        return adminPeriodCostsAnalyticsSource.get(previousCommands);
    }

    @Override
    public Class<? extends Message> getSupportedMessage() {
        return PeriodCostsAnalyticsMessage.class;
    }
}