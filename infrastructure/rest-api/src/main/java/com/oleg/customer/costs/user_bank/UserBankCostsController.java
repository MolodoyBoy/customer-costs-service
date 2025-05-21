package com.oleg.customer.costs.user_bank;

import com.oleg.customer.costs.api.UserBankCostsApi;
import com.oleg.customer.costs.common.Paginator;
import com.oleg.customer.costs.costs.query.CustomerCostsQuery;
import com.oleg.customer.costs.costs.service.CustomerCostsService;
import com.oleg.customer.costs.model.UserBankCostsCountDto;
import com.oleg.customer.costs.model.UserBankCostsListDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(
    origins = "http://localhost:3000",
    exposedHeaders = "Authorization"
)
public class UserBankCostsController implements UserBankCostsApi {

    private final CustomerCostsService customerCostsService;
    private final CustomerCostsConverter customerCostsConverter;

    public UserBankCostsController(CustomerCostsService customerCostsService,
                                   CustomerCostsConverter customerCostsConverter) {
        this.customerCostsService = customerCostsService;
        this.customerCostsConverter = customerCostsConverter;
    }

    @Override
    public UserBankCostsListDto getUserBankCosts(Integer bankId, Integer page) {
        List<CustomerCostsQuery> customerCosts = customerCostsService.getCustomerCosts(bankId, new Paginator(page));
        return new UserBankCostsListDto().values(customerCostsConverter.convert(customerCosts));
    }

    @Override
    public UserBankCostsCountDto getUserBankCostsCount(Integer bankId) {
        int customerCostsCount = customerCostsService.getCustomerCostsCount(bankId);
        return new UserBankCostsCountDto().count(customerCostsCount);
    }
}