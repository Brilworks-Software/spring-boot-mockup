package com.brilworks.mockup.modules.social.service.impl;


import com.brilworks.mockup.modules.social.dto.UserDto;
import com.brilworks.mockup.exceptions.TokenNotValidException;
import com.brilworks.mockup.modules.social.service.AuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {
    private static  final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String CLIENT_ID = "207121068678-bidhlhp6ap5tjc0p9k6s9bs5devnatfi.apps.googleusercontent.com";

    @Autowired
    UserServiceImpl userService;
    @Override
    public UserDto fetchAndSaveDataGoogleSocialLogin(String token, String registrationId) throws GeneralSecurityException, IOException {
        GoogleIdToken googleIdToken  = verifyToken(token);
        if(null == googleIdToken){
            logger.error("google id token found null {}",token);
            throw new TokenNotValidException(401001,"Token not valid","Token not valid");
        }
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        return userService.saveUserData(payload,registrationId);
    }

    // method to verify the integrity of token
    private GoogleIdToken verifyToken(String token) throws GeneralSecurityException, IOException {

        GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        return  googleIdTokenVerifier.verify(token);
    }
}
