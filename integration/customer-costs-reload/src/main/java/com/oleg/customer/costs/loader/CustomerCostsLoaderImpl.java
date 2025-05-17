package com.oleg.customer.costs.loader;

import com.oleg.customer.costs.costs.source.CustomerCostsLoader;
import com.oleg.customer.costs.costs.source.GetCustomerCostsSource;
import com.oleg.customer.costs.costs.source.ManageCustomerCosts;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.slf4j.LoggerFactory.getLogger;
import static java.util.concurrent.Executors.newFixedThreadPool;

@Service
public class CustomerCostsLoaderImpl implements CustomerCostsLoader {

    private static final Logger logger = getLogger(CustomerCostsLoaderImpl.class);

    private final ExecutorService executorService;
    private final ManageCustomerCosts manageCustomerCosts;
    private final GetCustomerCostsSource getCustomerCostsSource;

    public CustomerCostsLoaderImpl(ManageCustomerCosts manageCustomerCosts,
                                   GetCustomerCostsSource getCustomerCostsSource) {
        this.manageCustomerCosts = manageCustomerCosts;
        this.getCustomerCostsSource = getCustomerCostsSource;
        this.executorService = newFixedThreadPool(5);
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
                List<CustomerCosts> customerCosts = getCustomerCostsSource.getCustomerCosts(userId, bankId, null);
                manageCustomerCosts.save(customerCosts);
                success = true;
            } catch (Exception e) {
                counter += 1;
                logger.error(e.getMessage(), e);
            }
        }
    }
}