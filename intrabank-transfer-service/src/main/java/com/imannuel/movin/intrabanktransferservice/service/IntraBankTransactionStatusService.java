package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.enums.IntrabankTransactionStatus;

public interface IntraBankTransactionStatusService {
    IntrabankTransactionStatus findTransactionStatus(String transactionStatus);

    boolean checkTransactionStatusExists(String status);
}
