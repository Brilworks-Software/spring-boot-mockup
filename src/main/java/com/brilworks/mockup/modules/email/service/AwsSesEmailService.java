package com.brilworks.mockup.modules.email.service;

import com.brilworks.mockup.modules.email.dto.EmailSuccessDto;

public interface AwsSesEmailService {

    EmailSuccessDto sendEmailUsingAWSSES(String sender, String receiver);
}
