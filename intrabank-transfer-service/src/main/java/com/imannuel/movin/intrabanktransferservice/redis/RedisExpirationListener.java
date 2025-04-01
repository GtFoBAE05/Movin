package com.imannuel.movin.intrabanktransferservice.redis;

import com.imannuel.movin.intrabanktransferservice.service.IntraBankTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisExpirationListener extends KeyExpirationEventMessageListener {
    private final IntraBankTransactionService intraBankTransactionService;

    public RedisExpirationListener(RedisMessageListenerContainer listenerContainer, IntraBankTransactionService intraBankTransactionService) {
        super(listenerContainer);
        this.intraBankTransactionService = intraBankTransactionService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Recived expired key: {}", expiredKey);

        if (expiredKey.startsWith("expired-intra-bank-transaction:")) {
            intraBankTransactionService.processExpiredIntraBankTransaction(expiredKey.split(":")[1]);
        }


    }
}
