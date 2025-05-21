package com.oleg.customer.costs.loader;

import com.oleg.customer.costs.costs.source.BankLoader;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static java.util.function.Function.identity;

@Service
public class BankLoaderImpl implements BankLoader {

    private static final Logger logger = getLogger(BankLoaderImpl.class);

    private final Map<Integer, BankSubscriber> bankSubscribers;

    public BankLoaderImpl(List<BankSubscriber> bankSubscribers) {
        this.bankSubscribers = bankSubscribers.stream()
            .collect(toMap(BankSubscriber::bankId, identity()));
    }

    @Override
    public void loadBank(int userId, int bankId) {
        int counter = 0;
        int threshold = 1;
        boolean success = false;

        while (!success && counter < threshold) {
            try {
                BankSubscriber bankSubscriber = bankSubscribers.get(bankId);
                bankSubscriber.subscribe(userId);
                success = true;
            } catch (Exception e) {
                counter += 1;
                logger.error(e.getMessage(), e);
            }
        }
    }
}