package com.oleg.customer.costs.costs.value_object;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerCosts(int id,
                            int userId,
                            int bankId,
                            int categoryId,
                            BigDecimal amount,
                            String description,
                            LocalDateTime createdAt,
                            BigDecimal commissionRate) {}