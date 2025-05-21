package com.oleg.customer.costs.loader;

import com.oleg.customer.costs.costs.source.BankLoader;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BankLoaderImpl implements BankLoader {

    private static final Logger logger = getLogger(BankLoaderImpl.class);

    private final ExecutorService executorService;
    private final Map<Integer, BankSubscriber> bankSubscribers;

    public BankLoaderImpl(List<BankSubscriber> bankSubscribers) {
        this.executorService = newFixedThreadPool(5);
        this.bankSubscribers = bankSubscribers.stream()
            .collect(toMap(BankSubscriber::bankId, identity()));
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
    public void loadBank(int userId, int bankId) {
        executorService.submit(() -> load(userId, bankId));
    }

    public void load(int userId, int bankId) {
        int counter = 0;
        int threshold = 5;
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