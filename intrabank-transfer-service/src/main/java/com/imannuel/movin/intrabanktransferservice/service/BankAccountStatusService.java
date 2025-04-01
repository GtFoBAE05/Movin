package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.enums.BankAccountStatus;

public interface BankAccountStatusService {
    BankAccountStatus findBankAccountStatus(String status);

    boolean checkBankAccountStatusExists(String name);
}
