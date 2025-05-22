package com.oleg.customer.costs.analytics.period_costs.value_object;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Period(int year, int month) {

    public static Period of(LocalDate createAt) {
        if (createAt == null) return null;

        int year = createAt.getYear();
        int quarter = createAt.getMonthValue();

        return new Period(year, quarter);
    }

    public static Period of(LocalDateTime createAt) {
        if (createAt == null) return null;

        int year = createAt.getYear();
        int quarter = createAt.getMonthValue();

        return new Period(year, quarter);
    }

    public static Period of(Integer periodIndex) {
        if (periodIndex == null) return null;

        int year = (periodIndex - 1) / 12;
        int quarter = periodIndex - year * 12;

        return new Period(year, quarter);
    }

    public int periodIndex() {
        return year * 12 + month;
    }

    public LocalDate toLocalDate() {
        return LocalDate.of(year, month, 1);
    }
}