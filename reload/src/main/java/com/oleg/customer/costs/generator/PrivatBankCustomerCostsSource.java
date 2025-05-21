package com.oleg.customer.costs.generator;

import com.oleg.customer.costs.costs.source.GetCostsCategorySource;
import com.oleg.customer.costs.costs.value_object.CostsCategory;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.slf4j.LoggerFactory.getLogger;
import static java.util.stream.Collectors.toMap;
import static java.util.function.Function.identity;

@Component
public class PrivatBankCustomerCostsSource  {

    private static final int BANK_ID = 2;
    private static final Logger logger = getLogger(PrivatBankCustomerCostsSource.class);

    private static final LocalDate END_DATE = LocalDate.now();
    private static final LocalDate START_DATE = LocalDate.of(2023, 4, 1);

    private final DescriptionMappings descriptionMappings;
    private final Map<Integer, CostsCategory> categoryMappings;

    public PrivatBankCustomerCostsSource(DescriptionMappings descriptionMappings,
                                         GetCostsCategorySource getCostsCategorySource) {
        this.descriptionMappings = descriptionMappings;
        this.categoryMappings = getCostsCategorySource.getAll()
            .stream()
            .collect(toMap(CostsCategory::id, identity()));
    }

    public int bankId() {
        return BANK_ID;
    }


    public List<CustomerCosts> getCustomerCosts(int userId) {
        List<CustomerCosts> result = new ArrayList<>();

        LocalDate currentDate = START_DATE;
        while (!currentDate.isAfter(END_DATE)) {

            int txCount = ThreadLocalRandom.current().nextInt(1, 8);

            final LocalDate cD = currentDate;
            List<LocalDateTime> times = IntStream.range(0, txCount)
                .mapToObj(i -> randomDateTimeWithinDay(cD))
                .sorted()
                .toList();

            for (LocalDateTime dateTime : times) {
                int categoryId = randomCategoryId();
                String categoryDescription = categoryMappings.get(categoryId).description();

                BigDecimal amount = randomAmountForCategory();
                String description = generateDescription(categoryDescription);

                result.add(
                    new CustomerCosts(
                        -1,
                        userId,
                        BANK_ID,
                        categoryId,
                        amount,
                        description,
                        dateTime,
                        BigDecimal.TEN
                    )
                );
            }

            currentDate = currentDate.plusDays(1);
        }

        logger.info("Customer costs generated for user {}.", userId);

        return result;
    }

    private LocalDateTime randomDateTimeWithinDay(LocalDate date) {
        int hour = ThreadLocalRandom.current().nextInt(0, 24);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);
        int second = ThreadLocalRandom.current().nextInt(0, 60);
        return LocalDateTime.of(date, LocalTime.of(hour, minute, second));
    }

    private int randomCategoryId() {
        return ThreadLocalRandom.current().nextInt(1, categoryMappings.size() + 1);
    }

    private BigDecimal randomAmountForCategory() {
        double min = 10.0;
        double max = 500.0;

        double value = ThreadLocalRandom.current().nextDouble(min, max);
        return BigDecimal.valueOf(Math.round(value * 100) / 100.0);
    }

    private String generateDescription(String categoryDescription) {
        String template = descriptionMappings.getDescription(
            ThreadLocalRandom.current().nextInt(descriptionMappings.size())
        );

        return String.format(template, categoryDescription);
    }
}