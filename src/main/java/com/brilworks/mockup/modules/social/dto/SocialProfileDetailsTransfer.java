package com.brilworks.mockup.modules.social.dto;

import com.brilworks.mockup.modules.social.enums.ProviderEnum;
import com.brilworks.mockup.modules.social.enums.UserStatus;
import lombok.Data;

@Data
public class SocialProfileDetailsTransfer {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private ProviderEnum provider;
    private UserStatus status;
}
