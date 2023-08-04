package com.brilworks.mockup.rest;

import com.brilworks.mockup.config.ApplicationConfig;
import com.brilworks.mockup.dto.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    private static final Logger LOG = LoggerFactory.getLogger(HealthController.class);

    private final ApplicationConfig config;

    public HealthController(ApplicationConfig config) {
        this.config = config;
    }

    @GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public SystemInfo getSystemInfo() {
        long uptime = System.currentTimeMillis() - config.getStartTime();
        LOG.info("getSystemInfo: appId={}, uptime={}", config.getId(), uptime);
        return new SystemInfo(config.getId(), "spring-mockup", "1.0.0", System.currentTimeMillis(), uptime);
    }
}
