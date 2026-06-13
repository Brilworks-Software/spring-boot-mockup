package com.brilworks.mockup.modules.email.service.impl;

import com.brilworks.mockup.modules.email.dto.EmailSuccessDto;
import com.brilworks.mockup.modules.email.service.AwsSesEmailService;
import com.brilworks.mockup.modules.email.util.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;


@Service
public class AwsSesEmailServiceImpl implements AwsSesEmailService {

    @Autowired
    EmailUtils emailUtils;
    @Override
    public EmailSuccessDto sendEmailUsingAWSSES(String sender, String receiver) {

        Region region = Region.US_WEST_1;

        SesClient sesClient = SesClient.builder()
                .region(region)
                .build();

        String bodyHTML = "<html>" + "<head></head>" + "<body>" + "<h1>Hello!</h1>"
                + "<p> See the list of customers.</p>" + "</body>" + "</html>";

        return emailUtils.sendEmailUsingAWSSES(sesClient,sender,receiver,"AWS SES",bodyHTML);
    }
}
