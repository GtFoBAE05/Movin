package com.imannuel.movin.intrabanktransferservice.service;

import com.imannuel.movin.intrabanktransferservice.dto.request.CreateIntraBankTransactionRequest;
import com.imannuel.movin.intrabanktransferservice.dto.response.IntraBankTransactionResponse;
import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransaction;
import com.imannuel.movin.intrabanktransferservice.entity.IntraBankTransactionJob;

import java.util.List;

public interface IntraBankTransactionService {
    IntraBankTransactionResponse createIntraBankTransaction(String senderId, CreateIntraBankTransactionRequest createIntraBankTransactionRequest);

    IntraBankTransactionResponse receiveIntraBankTransactionPayment(String transactionId);

    List<IntraBankTransaction> getAllPendingIntraBankTransaction();

    void processIntraBankTransaction(IntraBankTransactionJob intraBankTransactionJob, IntraBankTransaction intraBankTransaction);

    void processBatchIntraBankTransaction();

    void processExpiredIntraBankTransaction(String transactionId);

    IntraBankTransaction findIntraBankTransaction(String id);

    IntraBankTransactionResponse getIntraBankTransaction(String id);
}
