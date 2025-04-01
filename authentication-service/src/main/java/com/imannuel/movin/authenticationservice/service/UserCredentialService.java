package com.imannuel.movin.authenticationservice.service;

import com.imannuel.movin.authenticationservice.entity.UserCredential;

public interface UserCredentialService {
    UserCredential createUserCredential(String userId);

    UserCredential findUserCredentialByUserId(String userId);

    void setupUserCredential(String userId, String credential, String role);
}
