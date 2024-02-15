package com.brilworks.mockup.modules.social.config;


import com.brilworks.mockup.modules.social.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class SocialConfig {

    @Bean
    public FacebookConnectionFactory createConnectionFactory(){
        return new FacebookConnectionFactory(Constants.APP_ID_FOR_FACEBOOK, Constants.APP_SECRET_FOR_FACEBOOK);
    }

}
