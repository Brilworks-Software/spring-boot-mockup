package com.brilworks.mockup.config;

import io.sentry.Sentry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class SentryConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(SentryConfiguration.class);
    @Value("${mockup.enabled.sentry:false}")
    private Boolean sentryEnabled;
    @Value("${mockup.enabled.sentry.dsn.url:null}")
    private String dsnUrl;
    @Value("${mockup.enabled.sentry.application.environment:local}")
    private String environment;

    @PostConstruct
    void initialize() {
        if (sentryEnabled) {
            LOGGER.info("Sentry is enabled for APP");
            Sentry.init(options -> {
                options.setDsn(dsnUrl);
                options.setEnvironment(environment);
                String release = "" + environment + "__RELEASE_" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()) + "";
                options.setRelease(release);
                options.addInAppInclude("*");
                options.setTracesSampleRate(0.3);
            });
        }
    }
}
