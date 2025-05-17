package com.oleg.customer.costs.bank;

import com.oleg.customer.costs.api.BanksApi;
import com.oleg.customer.costs.model.BanksListDto;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankController implements BanksApi {

    private final BankService bankService;
    private final BankConverter bankConverter;

    public BankController(BankService bankService, BankConverter bankConverter) {
        this.bankService = bankService;
        this.bankConverter = bankConverter;
    }

    @Override
    public BanksListDto getSupportedBanks() {
        List<Bank> supportedBanks = bankService.getSupportedBanks();
        return new BanksListDto().values(bankConverter.convert(supportedBanks));
    }
}
