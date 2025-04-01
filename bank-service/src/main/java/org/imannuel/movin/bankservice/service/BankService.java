package org.imannuel.movin.bankservice.service;

import org.imannuel.movin.bankservice.dto.request.BankRequest;
import org.imannuel.movin.bankservice.dto.request.PaymentOptionsRequest;
import org.imannuel.movin.bankservice.dto.response.BankCustomerResponse;
import org.imannuel.movin.bankservice.dto.response.BankResponse;
import org.imannuel.movin.bankservice.entity.Bank;

import java.util.List;

public interface BankService {
    Bank findBank(String code);

    Bank findBankById(String id);

    boolean checkBankExists(String code);

    List<BankCustomerResponse> getAllBanksForCustomer();

    List<BankCustomerResponse> getAvailablePaymentOptions(PaymentOptionsRequest paymentOptionsRequest);

    List<BankResponse> getAllBanksForAdmin();

    BankResponse createBank(BankRequest bankRequest);

    BankResponse updateBank(String code, BankRequest bankRequest);
}
