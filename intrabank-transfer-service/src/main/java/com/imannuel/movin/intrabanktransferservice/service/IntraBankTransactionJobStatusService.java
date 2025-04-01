package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.enums.IntraBankTransactionJobStatus;

public interface IntraBankTransactionJobStatusService {
    IntraBankTransactionJobStatus findIntraBankTransactionJobStatus(String intraBankTransactionJobStatus);

    boolean checkIntraBankTransactionJobStatusExists(String status);
}
