package com.oleg.customer.costs;

import com.oleg.customer.costs.loader.BankSubscriber;
import com.oleg.customer.costs.monobank.UserAccountsSource;
import com.oleg.customer.costs.user_management.UserTokenSource;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MonobankSubscriber implements BankSubscriber {

    private static final int BANK_ID = 1;
    private static final Logger logger = getLogger(MonobankSubscriber.class);
    private static final String webhookUrl = "https://oleg-customer-costs-service.herokuapp.com/monobank/webhook";

    private final WebClient monobankWebClient;
    private final UserTokenSource userTokenSource;
    private final UserAccountsSource userAccountsSource;

    public MonobankSubscriber(WebClient monobankWebClient,
                              UserTokenSource userTokenSource,
                              UserAccountsSource userAccountsSource) {
        this.userTokenSource = userTokenSource;
        this.monobankWebClient = monobankWebClient;
        this.userAccountsSource = userAccountsSource;
    }

    @Override
    public int bankId() {
        return BANK_ID;
    }

    @Override
    public void subscribe(int userId) {
        Mono<List<String>> mono = monobankWebClient.get()
            .uri("/personal/client-info")
            .header("X-Token", userTokenSource.getUserToken(userId, BANK_ID))
            .header("Content-Type", "application/json")
            .retrieve()
            .bodyToMono(ClientInfo.class)
            .map(clientInfo -> clientInfo.getAccounts().stream()
                    .map(ClientInfo.Account::getId)
                    .toList()
            );

        List<String> accountIds = mono.block();
        if (accountIds == null || accountIds.isEmpty()) {
            throw new IllegalArgumentException("User has not account for monobank!");
        }

        logger.info("Accounts retrieved for user {}: {}", userId, accountIds);

        userAccountsSource.addAccount(userId, accountIds);

        Map<String, String> body = Map.of("webHookUrl", webhookUrl);
        monobankWebClient.post()
            .uri("/personal/webhook")
            .bodyValue(body)
            .header("X-Token", userTokenSource.getUserToken(userId, BANK_ID))
            .header("Content-Type", "application/json")
            .retrieve()
            .bodyToMono(Void.class)
            .doOnSuccess(aVoid -> logger.info("Webhook successfully subscribed."))
            .block();
    }
}