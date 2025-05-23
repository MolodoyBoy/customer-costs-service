package com.oleg.customer.costs.analytics.category;

import com.oleg.customer.costs.analytics.categorized_costs.usecase.CategorizedCostsAnalyticsUseCase;
import com.oleg.customer.costs.analytics.common.value_object.Paginator;
import com.oleg.customer.costs.api.CategorizedCostsAnalyticsApi;
import com.oleg.customer.costs.model.CategorizedCostsAnalyticsResponseDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategorizedCostsAnalyticsController implements CategorizedCostsAnalyticsApi {

    private final CategorizedCostsAnalyticsUseCase categorizedCostsAnalyticsUseCase;
    private final CategorizedCostsAnalyticsConverter categorizedCostsAnalyticsConverter;

    public CategorizedCostsAnalyticsController(CategorizedCostsAnalyticsUseCase categorizedCostsAnalyticsUseCase,
                                               CategorizedCostsAnalyticsConverter categorizedCostsAnalyticsConverter) {
        this.categorizedCostsAnalyticsUseCase = categorizedCostsAnalyticsUseCase;
        this.categorizedCostsAnalyticsConverter = categorizedCostsAnalyticsConverter;
    }

    @Override
    public CategorizedCostsAnalyticsResponseDto getCategorizedCostsAnalytics(Integer id, Integer page, Integer pageSize) {
        Paginator paginator = convert(page, pageSize);
        var categorizedCostsAnalytics = categorizedCostsAnalyticsUseCase.get(id, paginator);
        return categorizedCostsAnalyticsConverter.convert(categorizedCostsAnalytics);
    }

    private Paginator convert(Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            return null;
        }

        return new Paginator(page, pageSize);
    }
}