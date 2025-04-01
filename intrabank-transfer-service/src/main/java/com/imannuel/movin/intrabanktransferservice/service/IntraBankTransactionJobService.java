package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransactionJob;

public interface IntraBankTransactionJobService {
    IntraBankTransactionJob createIntraBankTransactionJob();

    void finishIntraBankTransactionJob(IntraBankTransactionJob intraBankTransactionJob);
}
