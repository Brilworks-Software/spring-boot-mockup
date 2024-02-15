package com.brilworks.mockup.modules.social.rest;


import com.brilworks.mockup.modules.social.dto.SocialProfileDetailsTransfer;
import com.brilworks.mockup.modules.social.service.impl.AuthServiceImpl;
import com.brilworks.mockup.modules.social.service.impl.UserServiceImpl;
import com.brilworks.mockup.modules.social.util.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/auth")
public class SocialAuthController {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    OAuthUtils oAuthUtils;

    @GetMapping("/fetchdata")
    public ResponseEntity<?> getDataFromAccessToken(@RequestParam("token") String token,
                                                    @RequestParam("id") String registrationId) throws GeneralSecurityException, IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.fetchAndSaveDataGoogleSocialLogin(token,registrationId));
    }

    // API endpoint to fetch user data from Facebook using an access token
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/facebook/fetch-user-data")
    public ResponseEntity<?> fetchUserDataFromFacebook(@RequestParam(name = "token", required = false) String accessToken){
        SocialProfileDetailsTransfer userDetails = userService.fetchUserDataFromFacebookApi(accessToken);
        return ResponseEntity.ok(userDetails);
    }

    // API endpoint to fetch user data from LinkedIn using an access token
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/linked-in/fetch-user-data")
    public ResponseEntity<?> fetchUserDataFromLinkedIn(@RequestParam(name = "token", required = false) String accessToken){
        SocialProfileDetailsTransfer userDetails = userService.fetchUserDataFromLinkedinApi(accessToken);
        return ResponseEntity.ok(userDetails);
    }

}
