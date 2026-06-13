package com.brilworks.mockup.modules.social.util;

import com.brilworks.mockup.modules.social.dto.FieldsTransfer;
import com.brilworks.mockup.modules.social.dto.SocialProfileDetailsTransfer;
import com.brilworks.mockup.modules.social.enums.ProviderEnum;
import com.brilworks.mockup.exceptions.NotAcceptableException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OAuthUtils {

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;
    private static final Logger logger = Logger.getLogger(OAuthUtils.class.getName());

    public OAuth2Parameters getOauth2Parameters(){
        logger.info("Retrieving OAuth2Parameters");
        OAuth2Parameters parameters = new OAuth2Parameters();
        // Set the redirect URI for the OAuth flow
        parameters.setRedirectUri(Constants.REDIRECT_URL);
        parameters.setScope("email, public_profile");
        return parameters;
    }


    public SocialProfileDetailsTransfer getRestTemplateFacebook(String token) {
        logger.info("Retrieving user profile from Facebook using RestTemplate");
        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        // Construct the URL with the fields you want to retrieve
        String url = Constants.FACEBOOK_API_URL + token;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate template = new RestTemplate();
        try{
            // Make a GET request to the Facebook Graph API
            ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                String response = responseEntity.getBody();
                if (StringUtils.isEmpty(response)) {
                    throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.INVALID_RESPONCE);
                }
                // Set responce field in FieldsTransfer to reduce code length & improve readability
                FieldsTransfer fields = new FieldsTransfer();
                fields.setId(Constants.FACEBOOK_ID);
                fields.setEmail(Constants.FACEBOOK_EMAIL);
                fields.setFirstName(Constants.FACEBOOK_FIRSTNAME);
                fields.setLastName(Constants.FACEBOOK_LASTNAME);
                SocialProfileDetailsTransfer socialProfileDetails = getProfileDetailsFromResponse(response, fields);
                socialProfileDetails.setProvider(ProviderEnum.FACEBOOK);
                return socialProfileDetails;
            }
        }catch (Exception e){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.TOKEN_INVALID_OR_EXPIRED);
        }
        return (SocialProfileDetailsTransfer) Constants.NULL_CONSTANT;
    }

    public SocialProfileDetailsTransfer getRestTemplateLinkedIn(String token){
        logger.info("Retrieving user profile from LinkedIn using RestTemplate");
        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate template = new RestTemplate();
        try{
            // Make a GET request to the LinkedIn API
            ResponseEntity<String> responseEntity = template.exchange(Constants.LINKEDIN_API_URL, HttpMethod.GET, requestEntity, String.class);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)){
                String response = responseEntity.getBody();
                if (StringUtils.isEmpty(response)){ // TODO - Jay check same all over the project.
                    throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.INVALID_RESPONCE);
                }
             // Set responce field in FieldsTransfer to reduce code length & improve readability
                FieldsTransfer fields = new FieldsTransfer();
                fields.setId(Constants.LINKEDIN_ID); // TODO - Jay - move this all string to constants
                fields.setFirstName(Constants.LINKEDIN_FIRSTNAME);
                fields.setLastName(Constants.LINKEDIN_LASTNAME);
                fields.setEmail(Constants.LINKEDIN_EMAIL);
                SocialProfileDetailsTransfer socialProfileDetailsTransfer = getProfileDetailsFromResponse(response, fields);
                socialProfileDetailsTransfer.setProvider(ProviderEnum.LINKEDIN);
                return socialProfileDetailsTransfer;
            }
        }catch (Exception e){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.TOKEN_INVALID_OR_EXPIRED);
        }
        return (SocialProfileDetailsTransfer) Constants.NULL_CONSTANT;
    }

    public SocialProfileDetailsTransfer getProfileDetailsFromResponse(String response, FieldsTransfer fields) {
        logger.info("Retrieving user profile details from API response");
        ObjectMapper mapper = new ObjectMapper();
        SocialProfileDetailsTransfer socialProfileDetails = null;
        try {
            socialProfileDetails = new SocialProfileDetailsTransfer();
            // Parse the API response as JSON
            JsonNode jsonNode = mapper.readTree(response);
            // Retrieve and set the user ID
            JsonNode idNode = jsonNode.get(fields.getId());
            socialProfileDetails.setId(Optional.ofNullable(jsonNode.get(fields.getId()))
                    .filter(JsonNode::isTextual)
                    .map(JsonNode::asText)
                    .orElseThrow(() -> new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.SOCIAL_ID_IS_NOT_NULL)));
            // Retrieve and set the email if available
            Optional.ofNullable(jsonNode.get(fields.getEmail()))
                    .filter(JsonNode::isTextual)
                    .map(JsonNode::asText)
                    .ifPresent(socialProfileDetails::setEmail);
            // Retrieve and set the first name if available
            Optional.ofNullable(jsonNode.get(fields.getFirstName()))
                    .filter(JsonNode::isTextual)
                    .map(JsonNode::asText)
                    .ifPresent(socialProfileDetails::setFirstName);
            // Retrieve and set the last name if available
            Optional.ofNullable(jsonNode.get(fields.getLastName()))
                    .filter(JsonNode::isTextual)
                    .map(JsonNode::asText)
                    .ifPresent(socialProfileDetails::setLastName);

        } catch (Exception ex) {
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.INVALID_RESPONCE);
        }
        return socialProfileDetails;
    }



}
