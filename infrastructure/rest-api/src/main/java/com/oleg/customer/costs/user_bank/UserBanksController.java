package com.oleg.customer.costs.user_bank;

import com.oleg.customer.costs.api.UserBanksApi;
import com.oleg.customer.costs.bank.Bank;
import com.oleg.customer.costs.bank.BankConverter;
import com.oleg.customer.costs.model.AddUserBankDto;
import com.oleg.customer.costs.model.BankDto;
import com.oleg.customer.costs.model.BanksListDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(
    origins = "http://localhost:3000",
    exposedHeaders = "Authorization"
)
public class UserBanksController implements UserBanksApi {

    private final BankConverter bankConverter;
    private final UserBankService userBankService;

    public UserBanksController(BankConverter bankConverter, UserBankService userBankService) {
        this.bankConverter = bankConverter;
        this.userBankService = userBankService;
    }

    @Override
    public void addUserBank(AddUserBankDto dto) {
        userBankService.addUserBank(dto.getBankId(), dto.getUserToken());
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