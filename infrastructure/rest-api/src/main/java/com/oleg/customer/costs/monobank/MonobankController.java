package com.oleg.customer.costs.monobank;

import com.oleg.customer.costs.costs.query.CreateCustomerCostsCmd;
import com.oleg.customer.costs.costs.service.CustomerCostsService;
import com.oleg.customer.costs.monobank.dto.MonoWebhookPayload;
import com.oleg.customer.costs.monobank.dto.StatementItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController("/monobank/webhook")
@CrossOrigin(
    origins = "http://localhost:3000",
    exposedHeaders = {"Authorization"}
)
public class MonobankController {

    private final CustomerCostsService customerCostsService;

    public MonobankController(CustomerCostsService customerCostsService) {
        this.customerCostsService = customerCostsService;
    }

    @GetMapping
    public ResponseEntity<Void> verify() {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> handle(@RequestBody(required = false) MonoWebhookPayload payload) {
        if (payload == null) {
            return ResponseEntity.ok().build();
        }

        MonoWebhookPayload.Data data = payload.getData();
        StatementItem statementItem = data.getStatementItem();
        customerCostsService.saveCustomerCosts(convert(data.getAccount(), statementItem));

        return ResponseEntity.ok().build();
    }

    private CreateCustomerCostsCmd convert(String accountId, StatementItem statementItem) {
        return new CreateCustomerCostsCmd(
            statementItem.getId(),
            1,
            accountId,
            BigDecimal.valueOf(statementItem.getAmount()).divide(BigDecimal.valueOf(100)),
            statementItem.getDescription(),
            LocalDateTime.ofInstant(statementItem.getTime(), ZoneId.systemDefault())
        );
    }
}