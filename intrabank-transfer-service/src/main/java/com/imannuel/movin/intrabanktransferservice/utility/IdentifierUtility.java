package com.imannuel.movin.intrabanktransferservice.utility;

import java.util.UUID;

public class IdentifierUtility {
    public static String generateExternalId() {
        String randomString = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        return randomString + currentTimeMillis;
    }
}
