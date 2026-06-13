package com.brilworks.mockup.modules.social.service;

import com.brilworks.mockup.modules.social.dto.UserDto;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthService {
    UserDto fetchAndSaveDataGoogleSocialLogin(String token, String registrationId) throws GeneralSecurityException, IOException;
}
