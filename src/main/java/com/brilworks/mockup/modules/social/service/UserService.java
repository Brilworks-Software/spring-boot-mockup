package com.brilworks.mockup.modules.social.service;


import com.brilworks.mockup.modules.social.dto.SocialProfileDetailsTransfer;
import com.brilworks.mockup.modules.social.dto.UserDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface UserService{
    UserDto saveUserData(GoogleIdToken.Payload payload, String registrationID);
    public String save(SocialProfileDetailsTransfer socialProfileDetailsTransfer);
    public SocialProfileDetailsTransfer fetchUserDataFromFacebookApi(String accessToken) ;
    public SocialProfileDetailsTransfer fetchUserDataFromLinkedinApi(String accessToken);
}
