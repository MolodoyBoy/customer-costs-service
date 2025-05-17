package com.oleg.customer.costs.loader;

import com.oleg.customer.costs.costs.source.CustomerCostsLoader;
import com.oleg.customer.costs.costs.source.GetCustomerCostsSource;
import com.oleg.customer.costs.costs.source.ManageCustomerCosts;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import com.oleg.customer.costs.source.CustomerCostsBankSource;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;
import static java.util.concurrent.Executors.newFixedThreadPool;

@Service
public class CustomerCostsLoaderImpl implements CustomerCostsLoader {

    private static final Logger logger = getLogger(CustomerCostsLoaderImpl.class);

    private final ExecutorService executorService;
    private final ManageCustomerCosts manageCustomerCosts;
    private final CustomerCostsBankSource customerCostsBankSource;

    public CustomerCostsLoaderImpl(ManageCustomerCosts manageCustomerCosts,
                                   CustomerCostsBankSource customerCostsBankSource) {
        this.manageCustomerCosts = manageCustomerCosts;
        this.customerCostsBankSource = customerCostsBankSource;
        this.executorService = newFixedThreadPool(5);
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            currentThread().interrupt();
        } finally {
            executorService.shutdownNow();
        }
    }

    @Override
    public void loadCustomerCosts(int userId, int bankId) {
        executorService.submit(() -> task(userId, bankId));
    }

    private void task(int userId, int bankId) {
        int counter = 0;
        int threshold = 10;
        boolean success = false;

        while (!success && counter < threshold) {
            try {
                List<CustomerCosts> customerCosts = customerCostsBankSource.getCustomerCosts(userId, bankId);
                manageCustomerCosts.save(customerCosts);
                success = true;
            } catch (Exception e) {
                counter += 1;
                logger.error(e.getMessage(), e);
            }
        }
    }
}