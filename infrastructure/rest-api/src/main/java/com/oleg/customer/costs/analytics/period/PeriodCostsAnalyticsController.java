package com.oleg.customer.costs.analytics.period;

import com.oleg.customer.costs.analytics.period_costs.usecase.PeriodCostsAnalyticsUseCase;
import com.oleg.customer.costs.api.PeriodCostsAnalyticsApi;
import com.oleg.customer.costs.model.AnalyticPeriodsDto;
import com.oleg.customer.costs.model.PeriodCostsAnalyticsResponseDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(
    origins = "http://localhost:3000",
    exposedHeaders = {"Authorization"}
)
public class PeriodCostsAnalyticsController implements PeriodCostsAnalyticsApi {

    private final AnalyticPeriodConverter analyticPeriodConverter;
    private final PeriodCostsAnalyticsUseCase periodCostsAnalyticsUseCase;
    private final PeriodCostsAnalyticsConverter periodCostsAnalyticsConverter;

    public PeriodCostsAnalyticsController(AnalyticPeriodConverter analyticPeriodConverter,
                                          PeriodCostsAnalyticsUseCase periodCostsAnalyticsUseCase,
                                          PeriodCostsAnalyticsConverter periodCostsAnalyticsConverter) {
        this.analyticPeriodConverter = analyticPeriodConverter;
        this.periodCostsAnalyticsUseCase = periodCostsAnalyticsUseCase;
        this.periodCostsAnalyticsConverter = periodCostsAnalyticsConverter;
    }

    @Override
    public AnalyticPeriodsDto getAnalyticsPeriods() {
        var analyticPeriods = periodCostsAnalyticsUseCase.get();
        return analyticPeriodConverter.convert(analyticPeriods);
    }

    @Override
    public PeriodCostsAnalyticsResponseDto getPeriodCostsAnalytics(Integer id, Integer limit) {
        var periodCostsAnalytics = periodCostsAnalyticsUseCase.get(id, limit);
        return periodCostsAnalyticsConverter.convert(periodCostsAnalytics);
    }
}