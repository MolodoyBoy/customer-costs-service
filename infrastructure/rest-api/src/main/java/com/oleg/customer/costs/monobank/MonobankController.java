package com.oleg.customer.costs.monobank;

import com.oleg.customer.costs.costs.query.CreateCustomerCostsCmd;
import com.oleg.customer.costs.costs.service.CustomerCostsService;
import com.oleg.customer.costs.monobank.dto.MonoWebhookPayload;
import com.oleg.customer.costs.monobank.dto.StatementItem;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.slf4j.LoggerFactory.getLogger;

@CrossOrigin(
    origins = "http://localhost:3000"
)
@RestController
@RequestMapping("/monobank/webhook")
public class MonobankController {

    private static final Logger logger = getLogger(MonobankController.class);

    private final CustomerCostsService customerCostsService;

    public MonobankController(CustomerCostsService customerCostsService) {
        this.customerCostsService = customerCostsService;
    }

    @GetMapping
    public ResponseEntity<Void> verify() {
        logger.info("Monobank webhook verification request received.");

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> handle(@RequestBody(required = false) MonoWebhookPayload payload) {
        if (payload == null) {
            return ResponseEntity.ok().build();
        }

        logger.info("Monobank query received.");

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
            convert(statementItem.getAmount()),
            statementItem.getDescription(),
            LocalDateTime.ofInstant(statementItem.getTime(), ZoneId.systemDefault())
        );
    }

    private BigDecimal convert(double amount) {
        return BigDecimal.valueOf(amount)
            .divide(BigDecimal.valueOf(100))
            .abs();
    }
}