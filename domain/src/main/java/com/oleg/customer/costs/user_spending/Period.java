package com.oleg.customer.costs.user_spending;

import java.time.YearMonth;

public record Period(int year, int month) {

    public static Period of(YearMonth yearMonth) {
        if (yearMonth == null) return null;

        int year = yearMonth.getYear();
        int quarter = yearMonth.getMonthValue();

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
}
