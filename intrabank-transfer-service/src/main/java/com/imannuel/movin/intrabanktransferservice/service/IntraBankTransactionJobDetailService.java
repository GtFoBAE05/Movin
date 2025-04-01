package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransaction;
import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransactionJob;

public interface IntraBankTransactionJobDetailService {
    void createSuccessIntraBankTransactionJobDetail(IntraBankTransactionJob intraBankTransactionJob, IntraBankTransaction intraBankTransaction);

    void createFailedIntraBankTransactionJobDetail(IntraBankTransactionJob intraBankTransactionJob, String errorMessage, IntraBankTransaction intraBankTransaction);
}
