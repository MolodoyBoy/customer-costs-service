package com.oleg.customer.costs.bank;

import com.oleg.customer.costs.api.UserBanksApi;
import com.oleg.customer.costs.model.BankDto;
import com.oleg.customer.costs.model.BanksListDto;
import com.oleg.customer.costs.user_bank.UserBankService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserBanksController implements UserBanksApi {

    private final BankConverter bankConverter;
    private final UserBankService userBankService;

    public UserBanksController(BankConverter bankConverter, UserBankService userBankService) {
        this.bankConverter = bankConverter;
        this.userBankService = userBankService;
    }

    @Override
    public void addUserBank(BankDto dto) {
        Bank bank = bankConverter.convert(dto);
        userBankService.addUserBank(bank.id());
    }

    @Override
    public void deleteBank(Integer bankId) {
        userBankService.deleteUserBank(bankId);
    }

    @Override
    public BanksListDto getUserBanks() {
        List<Bank> userBanks = userBankService.getUserBanks();
        List<BankDto> values = bankConverter.convert(userBanks);
        return new BanksListDto().values(values);
    }
}