package com.brilworks.mockup.modules.social.service.impl;


import com.brilworks.mockup.modules.social.dto.SocialProfileDetailsTransfer;
import com.brilworks.mockup.modules.social.dto.UserDto;
import com.brilworks.mockup.modules.social.enums.ProviderEnum;
import com.brilworks.mockup.modules.social.enums.UserStatus;
import com.brilworks.mockup.exceptions.NotAcceptableException;
import com.brilworks.mockup.modules.social.model.User;
import com.brilworks.mockup.modules.social.model.UserProvider;
import com.brilworks.mockup.modules.social.model.UserSocialHandleLogin;
import com.brilworks.mockup.modules.social.repository.UserProviderRepository;
import com.brilworks.mockup.modules.social.repository.UserRepo;
import com.brilworks.mockup.modules.social.repository.UserSocialLoginHandlesRepository;
import com.brilworks.mockup.modules.social.service.UserService;
import com.brilworks.mockup.modules.social.util.Constants;
import com.brilworks.mockup.modules.social.util.OAuthUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static  final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepo userRepository;

    @Autowired
    UserProviderRepository userProviderRepository;

    @Autowired
    OAuthUtils oAuthUtils;

    public final FacebookConnectionFactory facebookConnectionFactory;

    public UserServiceImpl(FacebookConnectionFactory facebookConnectionFactory) {
        this.facebookConnectionFactory = facebookConnectionFactory;
    }

    @Autowired
    UserSocialLoginHandlesRepository userSocialLoginHandlesRepository;

    @Override
    public UserDto saveUserData(GoogleIdToken.Payload payload, String registrationId) {
        String email = payload.getEmail();

        if (null != email){ //if provider return non-null email
            // checking existence in database
            if(checkUserExistenceByEmail(email)){
                User existingUserByEmailId = userRepository.findByEmail(payload.getEmail());
                return new UserDto(existingUserByEmailId);
            } else {
                //if user not exists by email create new user in database
                User createdByEmail = createNewUser(payload,registrationId);
                return new UserDto(createdByEmail);
            }
        } else {
            //check user existence by social id
            String socialId = payload.getSubject();
            if (userRepository.existBySocialId(socialId)){
                User existUserBySocialId = userRepository.findBySocialId(socialId);
                return new UserDto(existUserBySocialId);
            } else {
                // if user not exists by social id create new user in database
                User createdBySocialId = createNewUser(payload,registrationId);
                return new UserDto(createdBySocialId);
            }
        }
    }

    private User createNewUser(GoogleIdToken.Payload payload, String registrationId) {
        String[] fullName = getFirstNameOrLastname(payload.get("name"));
        User user = new User(fullName[0],fullName[1], payload.getEmail(),payload.getSubject());
        User newUser = userRepository.save(user);
        logger.info("user saved with id {}",newUser.getId());

        ProviderEnum providerEnum = ProviderEnum.getSocialLoginProvider(registrationId);
        UserSocialHandleLogin socialHandleLogin = new UserSocialHandleLogin(newUser.getId(),providerEnum);
        UserSocialHandleLogin socialHandle = userSocialLoginHandlesRepository.save(socialHandleLogin);
        logger.info("User social login handle saved with ID : {}",socialHandle.getId());

        return newUser;
    }

    private boolean checkUserExistenceByEmail(String email) {
        return userRepository.existByEmailId(email);
    }

    private String[] getFirstNameOrLastname(Object object) {
        String name = (String) object;
        return name.split(" ");
    }

    @Override
    @Transactional
    public String save(SocialProfileDetailsTransfer socialProfileDetailsTransfer) {
        if (null == socialProfileDetailsTransfer.getId()){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.REFERENCE_ID_SHOULD_NOT_BLANKED);
        }
//        List<String> referenceIds = userRepository.findReferenceIds(); // We can pass ORG_ID if needed
        // TODO - Jay - create query that return boolean based on passed Ref_ID
        // Check if the current social profile's ID already exists in the repository
        if (null == socialProfileDetailsTransfer.getEmail()){
            if (!userRepository.existBySocialId(socialProfileDetailsTransfer.getId())) {
                User user = new User(socialProfileDetailsTransfer.getFirstName(),
                        socialProfileDetailsTransfer.getLastName(),
                        socialProfileDetailsTransfer.getEmail(),
                        socialProfileDetailsTransfer.getId()); // TODO - Jay - Pass all the fields here
                userRepository.save(user);
                UserProvider userProvider = new UserProvider(user.getId(), socialProfileDetailsTransfer.getProvider());
                userProviderRepository.save(userProvider);
                return Constants.SUCCESS;
            }else {
                User user = userRepository.findBySocialId(socialProfileDetailsTransfer.getId());
                if (!userProviderRepository.existsByUserIdAndProvider(user.getId(), socialProfileDetailsTransfer.getProvider())){
                    UserProvider userProvider = new UserProvider(user.getId(), socialProfileDetailsTransfer.getProvider());
                    userProviderRepository.save(userProvider);
                    return Constants.LOGIN_WITH_DIFFERENT_PROVIDER;
                }
                return Constants.ALREADY_EXIST;
            }
        }

        if (!userRepository.existByEmailId(socialProfileDetailsTransfer.getEmail())) {
            User user = new User(socialProfileDetailsTransfer.getFirstName(),
                    socialProfileDetailsTransfer.getLastName(),
                    socialProfileDetailsTransfer.getEmail(),
                    socialProfileDetailsTransfer.getId()); // TODO - Jay - Pass all the fields here
            userRepository.save(user);
            UserProvider userProvider = new UserProvider(user.getId(), socialProfileDetailsTransfer.getProvider());
            userProviderRepository.save(userProvider);
            return Constants.SUCCESS;
        }else {
            User user = userRepository.findByEmail(socialProfileDetailsTransfer.getEmail());
            if (!userProviderRepository.existsByUserIdAndProvider(user.getId(), socialProfileDetailsTransfer.getProvider())){
                UserProvider userProvider = new UserProvider(user.getId(), socialProfileDetailsTransfer.getProvider());
                userProviderRepository.save(userProvider);
                return Constants.LOGIN_WITH_DIFFERENT_PROVIDER;
            }
            return Constants.ALREADY_EXIST;
        }
    }

    @Override
    public SocialProfileDetailsTransfer fetchUserDataFromFacebookApi(String accessToken){
        if (null == accessToken){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ACCESS_TOKEN_IS_NOT_NULL);
        }
        // Fetch user profile data using the access token
        SocialProfileDetailsTransfer socialProfileDetailsTransfer = oAuthUtils.getRestTemplateFacebook(accessToken);
        String responce = save(socialProfileDetailsTransfer);
        if (responce.equals(Constants.SUCCESS)){
            socialProfileDetailsTransfer.setStatus(UserStatus.SAVED);
        } else if (responce.equals(Constants.ALREADY_EXIST)) {
            socialProfileDetailsTransfer.setStatus(UserStatus.ALREADY_EXIST);
        } else if (responce.equals(Constants.LOGIN_WITH_DIFFERENT_PROVIDER)){
            socialProfileDetailsTransfer.setStatus(UserStatus.LOGIN_WITH_DIFFERENT_PROVIDER);
        } else {
            socialProfileDetailsTransfer.setStatus(UserStatus.FAIL);
        }
        return socialProfileDetailsTransfer;
    }

    @Override
    public SocialProfileDetailsTransfer fetchUserDataFromLinkedinApi(String accessToken) {
        if (null == accessToken){
            throw new NotAcceptableException(NotAcceptableException.NotAcceptableExceptionMSG.ACCESS_TOKEN_IS_NOT_NULL);
        }
//         Fetch user profile data using the access token
        SocialProfileDetailsTransfer socialProfileDetailsTransfer = oAuthUtils.getRestTemplateLinkedIn(accessToken);
        String responce = save(socialProfileDetailsTransfer);
        if (responce.equals(Constants.SUCCESS)){
            socialProfileDetailsTransfer.setStatus(UserStatus.SAVED);
        } else if (responce.equals(Constants.ALREADY_EXIST)) {
            socialProfileDetailsTransfer.setStatus(UserStatus.ALREADY_EXIST);
        } else if (responce.equals(Constants.LOGIN_WITH_DIFFERENT_PROVIDER)){
            socialProfileDetailsTransfer.setStatus(UserStatus.LOGIN_WITH_DIFFERENT_PROVIDER);
        } else {
            socialProfileDetailsTransfer.setStatus(UserStatus.FAIL);
        }
        return socialProfileDetailsTransfer;
    }
}
