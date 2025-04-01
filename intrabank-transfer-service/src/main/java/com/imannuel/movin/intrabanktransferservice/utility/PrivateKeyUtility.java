package com.imannuel.movin.intrabanktransferservice.utility;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class PrivateKeyUtility {
    public static PrivateKey getPrivateKeyFromPEM(String privateKeyPEM) throws Exception {
        String privateKeyPEMFormatted = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEMFormatted);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return keyFactory.generatePrivate(keySpec);
    }
}
